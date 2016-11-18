package com.guy.log.exceptions;

public class TimerNotInitialisedException extends RuntimeException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8806238074202905743L;

	public TimerNotInitialisedException()
	{
		this("The timer has not been initialised, use LogTimer.setTime().");
	}
	
	public TimerNotInitialisedException(String error)
	{
		super(error);
	}
}
