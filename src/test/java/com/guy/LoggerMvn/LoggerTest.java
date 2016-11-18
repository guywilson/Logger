package com.guy.LoggerMvn;

import java.math.BigDecimal;

import com.guy.log.Logger;
import com.guy.test.TestBean;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class LoggerTest extends TestCase
{
	private Logger logger = new Logger(LoggerTest.class);
	
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public LoggerTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( LoggerTest.class );
    }

	public void testLogging()
	{
		logger.entry();
		logger.debug("Debug msg...");
		
		logger.performanceStart("Start timing operation...");
		
		TestBean bean;
		
		try {
			bean = new TestBean();
			bean.setName("John");
			bean.setDescription("A bean named John.");
			
			BigDecimal roundingTest = new BigDecimal(0.504999);
			roundingTest = roundingTest.setScale(2, BigDecimal.ROUND_UP);
			
			logger.info("Rounded number = " + roundingTest);
			
			logger.info("Sleeping...");
			
			Thread.sleep(500L);

			logger.performanceEnd();
			logger.debug(bean);
			logger.stack();
		}
		catch (InterruptedException e) {
			logger.error("Error occurred");
		}
		
		logger.exit();
	}

	private void assertLogFileCreated()
	{
		
	}
}
