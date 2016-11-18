package com.guy.log;

import java.io.Serializable;

import com.guy.log.exceptions.OwningClassNotSetException;
import com.guy.log.utils.LogFileOutputStream;



/**
<p>The Logger class provides a thread-safe and configurable logging mechanism 
  with the minimum of effort required by the developer using it. There should 
  be a Logger instance per class using the framework, the owning class is set 
  on the Logger instance so that package-class-method are reported correctly in 
  the log file.<br>
  <br>
  The Logger is controlled by a properties file which must be called <strong>logger.properties</strong> 
  and must reside on the classpath. The following properties are supported, please note these are case sensitive:</p>
<table width="80%" border="0" align="left" cellpadding="2" cellspacing="2">
<tr>
    <td><em>logger.switch</em>=on|off</td>
    <td>Master logging switch.</td>
</tr>
<tr>
    <td><em>logger.appendMode</em>=on|off</td>
    <td>Controls whether logging is appended to an existing file, or overwrites it.</td>
</tr>
<tr>
    <td><em>logger.output</em>=filename</td>
    <td>Full pathname to logfile (the date will be appended to this).</td>
</tr>
<tr>
    <td><em>logger.logLevel</em>=level</td>
    <td>Log level(s) required, separated by one of the delimiters '|&+,.:;/'</td>
</tr>
<tr>
    <td><em>logger.timeFormat</em>=level</td>
    <td>Time format of logged timestamp, in standard Java time format. Default: dd-MMM-yyyy HH:mm:ss.S</td>
</tr>
<tr>
    <td><em>logger.performanceThreshold</em>=level</td>
    <td>Only log the call if the duration is greater than the specified threshold (in ms)</td>
</tr>
<tr>
    <td><em>logger.packageFilter</em>=filter</td>
    <td>Only log the call if the method definition of the calling method begins with the specified filter(s). Separate filters with a ';'</td>
</tr>
</table>

<p>&nbsp;</p>
<p>&nbsp;</p>
<p>&nbsp;</p>
<p>&nbsp;</p>
<p>&nbsp;</p>
<p>&nbsp;</p>
<p>&nbsp;</p>
<p><br>
</p>
<p align="left">Supported log levels are:<br>
</p>
<table width="80%" border="0" align="left" cellpadding="2" cellspacing="2">
<tr>
    <td><em>off</em></td>
    <td>No effect if other log levels specified, same as logger.switch=off.</td>
</tr>
<tr>
    <td><em>entryExit</em></td>
    <td>Method entry/exit logging.</td>
</tr>
<tr>
    <td><em>info</em></td>
    <td>Information logging.</td>
</tr>
<tr>
    <td><em>debug</em></td>
    <td>Debug logging.</td>
</tr>
<tr>
    <td><em>error</em></td>
    <td>Error logging for handled/recoverable errors.</td>
</tr>
<tr>
    <td><em>fatal</em></td>
    <td>Error logging for non-recoverable errors.</td>
</tr>
<tr>
    <td><em>stack</em></td>
    <td>Stack trace logging.</td>
</tr>
<tr>
    <td><em>performance</em></td>
    <td>Performance logging.</td>
</tr>
<tr>
    <td><em>full</em></td>
    <td>All of the above.</td>
</tr>
</table>

<p align="left">&nbsp;</p>
<p align="left">&nbsp;</p>
<p align="left">&nbsp;</p>
<p align="left">&nbsp;</p>
<p align="left">&nbsp;</p>
<p align="left">&nbsp;</p>
<p align="left">&nbsp;</p>
<p align="left">&nbsp;</p>
<p align="left">An example logger.properties might be:</p>
<blockquote> 
  <p>logger.output=C:/dev/logs/app.log</p>
  <p>logger.switch=on</p>
  <p>logger.appendMode=on</p>
  <p>logger.performanceThreshold=200</p>
  <p>logger.logLevel=entryExit | debug | error | performance</p>
  <p>logger.packageFilter=com.comet.ui;com.comet.db.entity</p>
</blockquote>
<p align="left">&nbsp;</p>
<p align="left">An example usage of the framework could be:</p>
<p><font size="+1" face="Courier New, Courier, mono">class LoggerTest</font></p>
<p><font size="+1" face="Courier New, Courier, mono">{</font></p>
<blockquote> 
  <p><font size="+1" face="Courier New, Courier, mono">private Logger log = new 
    Logger(LoggerTest.class);</font></p>
  <p>&nbsp;</p>
  <p><font size="+1" face="Courier New, Courier, mono">public void run() throws 
    Exception </font></p>
  <p><font size="+1" face="Courier New, Courier, mono">{</font></p>
  <blockquote> 
    <p><font size="+1" face="Courier New, Courier, mono">log.entry();</font></p>
    <p>&nbsp;</p>
    <p><font size="+1" face="Courier New, Courier, mono">try {</font></p>
    <blockquote> 
      <p><font size="+1" face="Courier New, Courier, mono">log.performanceStart("Trace call to service");</font></p>
      <p><font size="+1" face="Courier New, Courier, mono">// do something</font></p>
      <p><font size="+1" face="Courier New, Courier, mono">log.performanceEnd();</font></p>
    </blockquote>
    <p><font size="+1" face="Courier New, Courier, mono">}</font></p>
    <p><font size="+1" face="Courier New, Courier, mono">catch (Exception e) {</font></p>
    <blockquote> 
      <p><font size="+1" face="Courier New, Courier, mono">log.error(&quot;Something 
        bad happened.&quot;, e);</font></p>
      <p><font size="+1" face="Courier New, Courier, mono">throw e;</font></p>
    </blockquote>
    <p><font size="+1" face="Courier New, Courier, mono">}</font></p>
    <p><font size="+1" face="Courier New, Courier, mono">finally {</font></p>
    <blockquote> 
      <p><font size="+1" face="Courier New, Courier, mono">log.exit();</font></p>
    </blockquote>
    <p><font size="+1" face="Courier New, Courier, mono">}</font></p>
  </blockquote>
  <p><font size="+1" face="Courier New, Courier, mono">}</font></p>
</blockquote>
<p><font size="+1" face="Courier New, Courier, mono">}</font></p>
<p>&nbsp;</p>
 * 
 * @author Guy Wilson
 */
public class Logger
{
	@SuppressWarnings("rawtypes")
	private Class owningClass = null;
	private LogHelper helper = null;
	
	/*
	 * Derives the packageName.className.methodName definition of where the
	 * logging call was made from. It does this by throwing an exception and 
	 * getting the definition from the resulting stack trace.
	 */
	private String getCallingMethodDef()
	{
		if (owningClass == null) {
			throw new OwningClassNotSetException();
		}
		
		return helper.getCallingMethodDefinition(owningClass.getName());
	}
	
	/*
	 * Public interface...
	 */
	
	/**
	 * Default constructor. If this constructor is used, the user will have to
	 * explicitly set the owning class using the setOwningClass method.
	 * 
	 */
	public Logger()
	{
		super();
		helper = helper.getInstance();
	}

	/**
	 * Construct a Logger instance and associate it with the class using it.
	 * The owningClass is used to log the class name correctly.
	 * 
	 * @param owningClass
	 */
	@SuppressWarnings("rawtypes")
	public Logger(Class owningClass)
	{
		super();
		helper = helper.getInstance();
		setOwningClass(owningClass);
	}

	/**
	 * Construct a Logger instance and associate it with the class using it.
	 * The owner is used to log the class name correctly.
	 * 
	 * @param owner
	 */
	public Logger(Object owner)
	{
		this(owner.getClass());
	}

	public String getLogFileName()
	{
		LogFileOutputStream os = (LogFileOutputStream)this.helper.getOutputStream();
		
		return os.getLogFileName(); 
	}
	
	/**
	 * Query the owning class.
	 * 
	 * @return The Class which has been set as the owner.
	 */
	@SuppressWarnings("rawtypes")
	public Class getOwningClass()
	{
		return owningClass;
	}

	/**
	 * Set or reset the owning class.
	 * 
	 * @param owningClass
	 */
	@SuppressWarnings("rawtypes")
	public void setOwningClass(Class owningClass)
	{
		this.owningClass = owningClass;
		
		if (helper.isDebugMode()) {
			System.out.println("Class definition is [" + this.owningClass.getName() + "]");
		}
	}

	/**
	 * Used for logging entry into a method. LOG_LEVEL_ENTRYEXIT must
	 * be set for logging from this method to appear in the log file.
	 */
	public void entry()
	{
		if (helper.isLogSwitch()) {
			helper.log(LogConstants.LOG_LEVEL_ENTRYEXIT, getCallingMethodDef(), "[ENTRY]");
		}
	}

	/**
	 * Used for logging exit from a method. LOG_LEVEL_ENTRYEXIT must
	 * be set for logging from this method to appear in the log file.
	 */
	public void exit()
	{
		if (helper.isLogSwitch()) {
			helper.log(LogConstants.LOG_LEVEL_ENTRYEXIT, getCallingMethodDef(), "[EXIT]");
		}
	}
	
	/**
	 * Used for general information logging. Controlled by the LOG_LEVEL_INFO tag.
	 * 
	 * @param msg - The message to log.
	 */
	public void info(String msg)
	{
		if (helper.isLogSwitch()) {
			helper.log(LogConstants.LOG_LEVEL_INFO, getCallingMethodDef(), "[INFO] " + msg);
		}
	}

	/**
	 * Used for debug logging that isn't required all the time. Controlled by the
	 * LOG_LEVEL_DEBUG tag.
	 * 
	 * @param msg - The message to log.
	 */
	public void debug(String msg)
	{
		if (helper.isLogSwitch()) {
			helper.log(LogConstants.LOG_LEVEL_DEBUG, getCallingMethodDef(), "[DEBUG] " + msg);
		}
	}

	/**
	 * Used for debug output of the given object in XML format. Output is controlled by
	 * the LOG_LEVEL_DEBUG tag. 
	 * 
	 * Please note that only classes which implement the
	 * serializable interface will serialise correctly into XML.
	 * 
	 * @param o - The object to dump.
	 */
	public void debug(Serializable o)
	{
		if (helper.isLogSwitch()) {
			String msg = "[DEBUG OBJ] \n" + helper.getXmlForObject(o);
			helper.log(LogConstants.LOG_LEVEL_DEBUG, getCallingMethodDef(), msg);
		}
	}

	/**
	 * Used for logging of error conditions, controlled by the LOG_LEVEL_ERROR
	 * tag.
	 * 
	 * @param msg - The message to log.
	 */
	public void error(String msg)
	{
		if (helper.isLogSwitch()) {
			helper.log(LogConstants.LOG_LEVEL_ERROR, getCallingMethodDef(), "[ERROR] " + msg);
		}
	}

	/**
	 * Used for error logging when an exception has been caught, the contents of
	 * Throwable.getMessage() are appended to the log line. Controlled by the
	 * LOG_LEVEL_ERROR tag.
	 * 
	 * @param msg - The message to log.
	 * @param th - The Throwable to append the message from.
	 */
	public void error(String msg, Throwable th)
	{
		if (helper.isLogSwitch()) {
			helper.log(LogConstants.LOG_LEVEL_ERROR, getCallingMethodDef(), "[ERROR] " + msg + " - " + th.getMessage());
		}
	}

	/**
	 * Used for fatal error logging as opposed to logging of recoverable or handled errors.
	 * Controlled by the LOG_LEVEL_FATAL tag.
	 * 
	 * @param msg - The message to log.
	 * @param th - The Throwable to append the message from.
	 */
	public void fatal(String msg, Throwable th)
	{
		if (helper.isLogSwitch()) {
			helper.log(LogConstants.LOG_LEVEL_FATAL, getCallingMethodDef(), "[FATAL ERROR] " + msg + " - " + th.getMessage());
		}
	}
	
	/**
	 * Used to log the full stack trace of the given Throwable exception. Controlled
	 * by the LOG_LEVEL_ERROR tag.
	 * 
	 * @param th - The exception to log the stack trace of.
	 */
	public void trace(Throwable th)
	{
		if (helper.isLogSwitch()) {
			helper.log(LogConstants.LOG_LEVEL_ERROR, getCallingMethodDef(), "[STACK TRACE] \n" + helper.getExceptionStackTrace(th));
		}
	}
	
	/**
	 * Used for debugging to print the calling stack at the point at which
	 * the method is called. Controlled by the LOG_LEVEL_STACK tag.
	 */
	public void stack()
	{
		if (helper.isLogSwitch()) {
			try {
				throw new Exception("**** stack trace ****");
			}
			catch (Exception e) {
				String trace = helper.getExceptionStackTrace(e);
				trace = trace.substring(trace.indexOf(")") + 3);
				trace = trace.replaceAll("at ", "-> ");
				helper.log(LogConstants.LOG_LEVEL_STACK, getCallingMethodDef(), "[CALL STACK] \n" + trace);
			}
		}
	}
	
	/**
	 * Used to log the start of a performance trace. Controlled
	 * by the LOG_LEVEL_PERFORMANCE tag.
	 * 
	 * @param msg - The message to display when tracing starts.
	 */
	public void performanceStart(String msg)
	{
		if (helper.isLogSwitch()) {
			helper.logPerformanceStart(getCallingMethodDef(), msg);
		}
	}
	
	/**
	 * Used to log the end of a performance trace, the duration in milliseconds between the call to 
	 * performanceStart() and this method is displayed. Controlled by the LOG_LEVEL_PERFORMANCE tag.
	 */
	public void performanceEnd()
	{
		if (helper.isLogSwitch()) {
			helper.logPerformanceEnd(getCallingMethodDef());
		}
	}
}
