package com.guy.log;

import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Stack;
import java.util.StringTokenizer;

import com.guy.log.LogConstants;
import com.guy.log.LogHelper;
import com.guy.log.utils.LogFileOutputStream;
import com.guy.log.utils.LogTimer;

class LogHelper
{
	private static LogHelper _instance = null;
	
	private SimpleDateFormat timestampFormatter = null;
	private Date now = new Date();
	private Stack<StringBuffer> bufferStack = new Stack<StringBuffer>();
	private Stack<LogTimer> timerStack = new Stack<LogTimer>();
	private Hashtable<String, LogTimer> timerCache = new Hashtable<String, LogTimer>();
	private OutputStream os = null;
	private int logLevel = LogConstants.LOG_LEVEL_OFF;
	private int logDetail = LogConstants.LOG_DETAIL_ALL;
	private boolean logSwitch = false;
	private boolean debugMode = false;
	private boolean appendMode = true;
	private long performanceThreshold = 0L;
	private String packageFilter = null;
	private StringWriter sw = null;
	private PrintWriter pw = null;
	private XMLEncoder xmlEncoder = null;
	private ByteArrayOutputStream bos = null;

	protected LogHelper()
	{
		init();
	}
	
	protected void refreshProperties()
	{
		init();
	}
	
	protected OutputStream getOutputStream() {
		return this.os;
	}
	
	public static synchronized LogHelper getInstance()
	{
		if (_instance == null) {
			_instance = new LogHelper();
		}
		
		return _instance;
	}
	
	public int getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(int newLogLevel) {
		logLevel = newLogLevel;
	}

	public boolean isLogSwitch()
	{
		return logSwitch;
	}

	public void setLogSwitch(boolean logSwitch)
	{
		this.logSwitch = logSwitch;
	}

	public boolean isDebugMode()
	{
		return debugMode;
	}
	
	private synchronized void init()
	{
		try {
			Properties p = new Properties();
			
			try {
				ClassLoader clsLoader = Thread.currentThread().getContextClassLoader();
				InputStream is = clsLoader.getResourceAsStream(LogConstants.PRP_FILENAME);
				
				if (is != null) {
					p.load(is);
				}
			}
			catch (IOException ioEx) {
				System.out.println("WARNING: Cannot open logger.properties file, using defaults. " + ioEx.getMessage());
			}
			
			String strLogSwitch = p.getProperty(LogConstants.PRP_PROP_NAME_SWITCH, LogConstants.PRP_PROP_VALUE_SWITCH_DEFAULT);
			
			setLogSwitch(strLogSwitch.equalsIgnoreCase(LogConstants.PRP_PROP_VALUE_SWITCH_ON));

			String strAppendMode = p.getProperty(LogConstants.PRP_PROP_NAME_APPEND, LogConstants.PRP_PROP_VALUE_APPEND_DEFAULT);
			appendMode = strAppendMode.equalsIgnoreCase(LogConstants.PRP_PROP_VALUE_APPEND_ON);
			
			if (isLogSwitch()) {
				String outputFile = p.getProperty(LogConstants.PRP_PROP_NAME_OUTPUT);
				
				if (outputFile != null) {
					try {
						os = new LogFileOutputStream(outputFile, appendMode);
					}
					catch (Exception fileOutputEx) {
						System.out.println("Error opening output file [" + outputFile + "] with error - " + fileOutputEx.getMessage());
					}
				}
				else {
					os = System.out;
				}
				
				String logLevelStr = p.getProperty(LogConstants.PRP_PROP_NAME_LOGLEVEL, LogConstants.PRP_PROP_VALUE_LOGLEVEL_DEFAULT);
				logLevel = parseLogLevelStr(logLevelStr);

				String logDetailStr = p.getProperty(LogConstants.PRP_PROP_NAME_DETAIL, LogConstants.PRP_PROP_VALUE_DETAIL_DEFAULT);
				logDetail = parseLogDetailStr(logDetailStr);
				
				String logTimestampFormatStr = p.getProperty(LogConstants.PRP_PROP_NAME_TIMEFORMAT, LogConstants.PRP_PROP_VALUE_TIMEFORMAT_DEFAULT);
				timestampFormatter = new SimpleDateFormat(logTimestampFormatStr);
				
				String strDebugMode = p.getProperty(LogConstants.PRP_PROP_NAME_DEBUGMODE, LogConstants.PRP_PROP_VALUE_DEBUGMODE_DEFAULT);
				debugMode = strDebugMode.equalsIgnoreCase(LogConstants.PRP_PROP_VALUE_DEBUGMODE_ON);

				String strPerfromanceThreshold = p.getProperty(LogConstants.PRP_PROP_NAME_PERFTHRESHOLD, LogConstants.PRP_PROP_VALUE_PERFTHRESHOLD_DEFAULT);
				performanceThreshold = new Long(strPerfromanceThreshold).longValue();

				packageFilter = p.getProperty(LogConstants.PRP_PROP_NAME_PACKAGEFILTER, LogConstants.PRP_PROP_VALUE_PACKAGEFILTER_DEFAULT);
			}
		}
		catch (Exception e) {
			System.out.println("Error setting properties. " + e.getMessage());
		}
	}

	private int parseLogLevelStr(String logLevelStr) throws Exception
	{
		int logLevel = LogConstants.LOG_LEVEL_OFF;
		
		try {
			StringTokenizer strTok = new StringTokenizer(logLevelStr, LogConstants.PRP_PROP_PARAM_LOGLEVEL_DELIM);
			
			while (strTok.hasMoreTokens()) {
				String token = strTok.nextToken().trim();
				
				if (token.equals(LogConstants.PRP_PROP_VALUE_LOGLEVEL_OFF)) {
					logLevel |= LogConstants.LOG_LEVEL_OFF;
				}
				else if(token.equals(LogConstants.PRP_PROP_VALUE_LOGLEVEL_ENTRYEXIT)) {
					logLevel |= LogConstants.LOG_LEVEL_ENTRYEXIT;
				}
				else if(token.equals(LogConstants.PRP_PROP_VALUE_LOGLEVEL_INFO)) {
					logLevel |= LogConstants.LOG_LEVEL_INFO;
				}
				else if(token.equals(LogConstants.PRP_PROP_VALUE_LOGLEVEL_DEBUG)) {
					logLevel |= LogConstants.LOG_LEVEL_DEBUG;
				}
				else if(token.equals(LogConstants.PRP_PROP_VALUE_LOGLEVEL_ERROR)) {
					logLevel |= LogConstants.LOG_LEVEL_ERROR;
				}
				else if(token.equals(LogConstants.PRP_PROP_VALUE_LOGLEVEL_FATAL)) {
					logLevel |= LogConstants.LOG_LEVEL_FATAL;
				}
				else if(token.equals(LogConstants.PRP_PROP_VALUE_LOGLEVEL_STACK)) {
					logLevel |= LogConstants.LOG_LEVEL_STACK;
				}
				else if(token.equals(LogConstants.PRP_PROP_VALUE_LOGLEVEL_PERFORMANCE)) {
					logLevel |= LogConstants.LOG_LEVEL_PERFORMANCE;
				}
				else if(token.equals(LogConstants.PRP_PROP_VALUE_LOGLEVEL_FULL)) {
					logLevel |= LogConstants.LOG_LEVEL_FULL;
				}
			}
		}
		catch (Exception e) {
			System.out.println("Error parsing loglevel property [" + logLevelStr + "] with error - " + e.getMessage());
			throw e;
		}
		
		return logLevel;
	}

	private int parseLogDetailStr(String logDetailStr) throws Exception
	{
		int logDetail = 0x00;
		
		try {
			StringTokenizer strTok = new StringTokenizer(logDetailStr, LogConstants.PRP_PROP_PARAM_DETAIL_DELIM);
			
			while (strTok.hasMoreTokens()) {
				String token = strTok.nextToken().trim();
				
				if(token.equals(LogConstants.PRP_PROP_VALUE_DETAIL_TIMESTAMP)) {
					logDetail |= LogConstants.LOG_DETAIL_TIMESTAMP;
				}
				else if(token.equals(LogConstants.PRP_PROP_VALUE_DETAIL_METHODDEF)) {
					logDetail |= LogConstants.LOG_DETAIL_METHODDEF;
				}
				else if(token.equals(LogConstants.PRP_PROP_VALUE_DETAIL_THREADNAME)) {
					logDetail |= LogConstants.LOG_DETAIL_THREADNAME;
				}
				else if(token.equals(LogConstants.PRP_PROP_VALUE_DETAIL_ALL)) {
					logDetail |= LogConstants.LOG_DETAIL_ALL;
				}
			}
		}
		catch (Exception e) {
			System.out.println("Error parsing detail property [" + logDetailStr + "] with error - " + e.getMessage());
			throw e;
		}
		
		return logDetail;
	}

	private synchronized String getTimeNow()
	{
		now.setTime(System.currentTimeMillis());
		return timestampFormatter.format(now);
	}
	
	private synchronized String getThreadName()
	{
		return Thread.currentThread().getName();
	}
	
	private synchronized StringBuffer getBuffer()
	{
		StringBuffer buffer = null;
		
		if (bufferStack.empty()) {
			for(int i = 0;i < 5 && bufferStack.size() < LogConstants.MAX_BUFFER_SIZE;i++) {
				bufferStack.push(new StringBuffer());
			}
			
			buffer = (StringBuffer)bufferStack.pop();

			if (debugMode) {
				buffer.append(LogConstants.NEW_BUFFER);
			}
		}
		else {
			buffer = (StringBuffer)bufferStack.pop();
			
			if (debugMode) {
				buffer.append(LogConstants.STACK_BUFFER);
			}
		}
		
		return buffer;
	}
	
	private synchronized void putBuffer(StringBuffer buffer)
	{
		if (bufferStack.size() < LogConstants.MAX_BUFFER_SIZE) {
			buffer.setLength(0);
			bufferStack.push(buffer);
		}
	}
	
	private synchronized LogTimer getTimer()
	{
		LogTimer timer = null;
		
		if (timerStack.empty()) {
			for(int i = 0;i < 5 && timerStack.size() < LogConstants.MAX_BUFFER_SIZE;i++) {
				timerStack.push(new LogTimer());
			}
		}
		
		timer = (LogTimer)timerStack.pop();
		timer.setTime();

		return timer;
	}
	
	private synchronized void putTimer(LogTimer timer)
	{
		if (timerStack.size() < LogConstants.MAX_BUFFER_SIZE) {
			timer.clear();
			timerStack.push(timer);
		}
	}
	
	private synchronized void _writeBuffer(StringBuffer buffer) throws Exception
	{
		try {
			os.write(buffer.toString().getBytes());
			os.flush();
		}
		catch (Exception e) {
			System.out.println("Error writing log line - " + e.getMessage());
			throw e;
		}
	}

	private boolean isMethodDefEnabledForLogging(String methodDef)
	{
		boolean isEnabled = true;
		
		if (packageFilter.length() > 0) {
			isEnabled = false;
			
			StringTokenizer tok = new StringTokenizer(packageFilter, LogConstants.PRP_PROP_PARAM_PACKAGEFILTER_DELIM);
			
			while (tok.hasMoreTokens()) {
				if (methodDef.startsWith(tok.nextToken())) {
					isEnabled = true;
				}
			}
		}
		
		return isEnabled;
	}
	
	private synchronized void startPerformanceTimer(String methodDef)
	{
		String key = methodDef + " " + Thread.currentThread().getId();
		LogTimer timer = getTimer();
		timerCache.put(key, timer);
	}
	
	private synchronized long stopPerformancerTimer(String methodDef)
	{
		String key = methodDef + " " + Thread.currentThread().getId();
		long timestamp = System.currentTimeMillis();
		LogTimer timer = timerCache.remove(key);
		
		long duration = timestamp - timer.getTimeStamp();
		
		putTimer(timer);
		
		return duration;
	}
	
	String getExceptionStackTrace(Throwable th)
	{
		String trace = null;
		
		if (pw == null) {
			sw = new StringWriter();
			pw = new PrintWriter(sw);
		}

		th.printStackTrace(pw);
		trace = sw.toString();
		sw.getBuffer().setLength(0);
		
		return trace;
	}
	
	String getCallingMethodDefinition(String owningClassName)
	{
		String methodDefinition = null;
		
		try {
			throw new Exception();
		}
		catch (Exception e) {
			String stackTrace = null;
			
			/*
			 * Get stack trace...
			 */
			stackTrace = getExceptionStackTrace(e);
			
			/*
			 * Find first instance of our class definition in the stack trace,
			 * the full method definition is this qualified class name up to
			 * the opening '(' of the method call...
			 */
			int i1 = stackTrace.indexOf(owningClassName);
			int i2 = stackTrace.indexOf('(', i1);
			
			if (i1 >= 0 && i2 >= 0) {
				if (i1 < stackTrace.length() && i2 < stackTrace.length()) {
					methodDefinition = stackTrace.substring(i1, i2) + "()";
				}
			}
			else {
				methodDefinition = LogConstants.DEFAULT_METHOD_DEF;
			}
		}
		
		return methodDefinition;
	}
	
	synchronized String getXmlForObject(Serializable o)
	{
		String xml = null;
		
		if (xmlEncoder == null) {
			bos = new ByteArrayOutputStream();
			xmlEncoder = new XMLEncoder(bos);
		}
		
		xmlEncoder.writeObject(o);
		xmlEncoder.close();

		xml = bos.toString();
		
		bos.reset();
		
		return xml;
	}
	
	void log(int requestedLevel, String methodDef, String message)
	{
		if (isLogSwitch() && (requestedLevel & logLevel) > 0 && isMethodDefEnabledForLogging(methodDef)) {
			StringBuffer line = getBuffer();
			
			if ((logDetail & LogConstants.LOG_DETAIL_TIMESTAMP) > 0) {
				line.append(LogConstants.OPEN_DELIM);
				line.append(getTimeNow());
				line.append(LogConstants.CLOSE_DELIM);
			}
			if ((logDetail & LogConstants.LOG_DETAIL_THREADNAME) > 0) {
				line.append(LogConstants.OPEN_DELIM);
				line.append(getThreadName());
				line.append(LogConstants.CLOSE_DELIM);
			}
			if ((logDetail & LogConstants.LOG_DETAIL_METHODDEF) > 0) {
				line.append(LogConstants.OPEN_DELIM);
				line.append(methodDef);
				line.append(LogConstants.CLOSE_DELIM);
			}
			line.append(message);
			line.append(LogConstants.NEW_LINE);

			try {
				_writeBuffer(line);
			}
			catch (Exception e) {
				System.out.println("Error writing log line - " + e.getMessage());
			}
			finally {
				putBuffer(line);
			}
		}
	}

	void logPerformanceStart(String methodDef, String msg)
	{
		startPerformanceTimer(methodDef);
		log(LogConstants.LOG_LEVEL_PERFORMANCE, methodDef, "[PERFORMANCE] [Start] " + msg);
	}

	void logPerformanceEnd(String methodDef)
	{
		long duration = stopPerformancerTimer(methodDef);
		
		if (duration > performanceThreshold) {
			log(LogConstants.LOG_LEVEL_PERFORMANCE, methodDef, "[PERFORMANCE] [End] [" + duration + "ms]");
		}
	}
}
