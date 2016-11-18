package com.guy.log;

class LogConstants
{
	public final static int MAX_BUFFER_SIZE = 20;
	
	public final static String DEFAULT_METHOD_DEF = "unknownMethod()";
	
	public final static String PRP_FILENAME = "logger.properties";
	
	public final static String PRP_PROP_NAME_SWITCH 				= "logger.switch";
	public final static String PRP_PROP_VALUE_SWITCH_OFF 			= "off";
	public final static String PRP_PROP_VALUE_SWITCH_ON 			= "on";
	public final static String PRP_PROP_VALUE_SWITCH_DEFAULT 		= PRP_PROP_VALUE_SWITCH_OFF;
	
	public final static String PRP_PROP_NAME_APPEND 				= "logger.appendMode";
	public final static String PRP_PROP_VALUE_APPEND_OFF 			= "off";
	public final static String PRP_PROP_VALUE_APPEND_ON 			= "on";
	public final static String PRP_PROP_VALUE_APPEND_DEFAULT 		= PRP_PROP_VALUE_APPEND_ON;

	public final static String PRP_PROP_NAME_OUTPUT = "logger.output";

	public final static String PRP_PROP_NAME_LOGLEVEL 				= "logger.logLevel";
	public final static String PRP_PROP_VALUE_LOGLEVEL_OFF 			= "off";
	public final static String PRP_PROP_VALUE_LOGLEVEL_ENTRYEXIT 	= "entryExit";
	public final static String PRP_PROP_VALUE_LOGLEVEL_INFO 		= "info";
	public final static String PRP_PROP_VALUE_LOGLEVEL_DEBUG 		= "debug";
	public final static String PRP_PROP_VALUE_LOGLEVEL_ERROR 		= "error";
	public final static String PRP_PROP_VALUE_LOGLEVEL_FATAL 		= "fatal";
	public final static String PRP_PROP_VALUE_LOGLEVEL_STACK 		= "stack";
	public final static String PRP_PROP_VALUE_LOGLEVEL_PERFORMANCE 	= "performance";
	public final static String PRP_PROP_VALUE_LOGLEVEL_FULL 		= "full";
	public final static String PRP_PROP_VALUE_LOGLEVEL_DEFAULT 		= "error | fatal";
	public final static String PRP_PROP_PARAM_LOGLEVEL_DELIM		= "|&+,.:;/";

	public final static String PRP_PROP_NAME_DETAIL 				= "logger.detail";
	public final static String PRP_PROP_VALUE_DETAIL_TIMESTAMP 		= "timestamp";
	public final static String PRP_PROP_VALUE_DETAIL_METHODDEF 		= "methodDef";
	public final static String PRP_PROP_VALUE_DETAIL_THREADNAME 	= "threadName";
	public final static String PRP_PROP_VALUE_DETAIL_ALL 			= "all";
	public final static String PRP_PROP_VALUE_DETAIL_DEFAULT 		= PRP_PROP_VALUE_DETAIL_ALL;
	public final static String PRP_PROP_PARAM_DETAIL_DELIM			= "|&+,.:;/";

	public final static String PRP_PROP_NAME_TIMEFORMAT 			= "logger.timeFormat";
	public final static String PRP_PROP_VALUE_TIMEFORMAT_DEFAULT 	= "dd-MMM-yyyy HH:mm:ss.S";

	public final static String PRP_PROP_NAME_DEBUGMODE 				= "logger.debugMode";
	public final static String PRP_PROP_VALUE_DEBUGMODE_OFF 		= "off";
	public final static String PRP_PROP_VALUE_DEBUGMODE_ON 			= "on";
	public final static String PRP_PROP_VALUE_DEBUGMODE_DEFAULT 	= PRP_PROP_VALUE_DEBUGMODE_OFF;

	public final static String PRP_PROP_NAME_PERFTHRESHOLD 			= "logger.performanceThreshold";
	public final static String PRP_PROP_VALUE_PERFTHRESHOLD_DEFAULT = "0";

	public final static String PRP_PROP_NAME_PACKAGEFILTER 			= "logger.packageFilter";
	public final static String PRP_PROP_VALUE_PACKAGEFILTER_DEFAULT = "";
	public final static String PRP_PROP_PARAM_PACKAGEFILTER_DELIM	= ",;: ";

	
	public final static int LOG_LEVEL_OFF			= 0x0000;
	public final static int LOG_LEVEL_ENTRYEXIT 	= 0x0001;
	public final static int LOG_LEVEL_INFO 			= 0x0002;
	public final static int LOG_LEVEL_DEBUG 		= 0x0004;
	public final static int LOG_LEVEL_ERROR 		= 0x0008;
	public final static int LOG_LEVEL_FATAL 		= 0x0010;
	public final static int LOG_LEVEL_STACK			= 0x0020;
	public final static int LOG_LEVEL_PERFORMANCE	= 0x0040;
	public final static int LOG_LEVEL_FULL			= LOG_LEVEL_ENTRYEXIT | LOG_LEVEL_INFO | LOG_LEVEL_DEBUG | LOG_LEVEL_ERROR | LOG_LEVEL_FATAL | LOG_LEVEL_STACK | LOG_LEVEL_PERFORMANCE;

	public final static int	LOG_DETAIL_TIMESTAMP	= 0x0001;
	public final static int LOG_DETAIL_METHODDEF	= 0x0002;
	public final static int LOG_DETAIL_THREADNAME	= 0x0004;
	public final static int LOG_DETAIL_ALL			= LOG_DETAIL_TIMESTAMP | LOG_DETAIL_THREADNAME | LOG_DETAIL_METHODDEF;
	
	public final static String OPEN_DELIM = "[";
	public final static String CLOSE_DELIM = "]";
	public final static String NEW_LINE = "\r\n";
	public final static String NEW_BUFFER = "[N]";
	public final static String STACK_BUFFER = "[S]";

}
