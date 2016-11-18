package com.guy.log.utils;

import com.guy.log.exceptions.TimerNotInitialisedException;

public class LogTimer
{
	private long timestamp;
	
	public LogTimer()
	{
		clear();
	}
	
	public void setTime()
	{
		this.timestamp = System.currentTimeMillis();
	}
	
	public void clear()
	{
		this.timestamp = -1L;
	}
	
	public long getTimeStamp() throws TimerNotInitialisedException
	{
		if (this.timestamp < 0L) {
			throw new TimerNotInitialisedException();
		}
		
		return this.timestamp;
	}
}
