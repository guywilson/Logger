package com.guy.log.exceptions;

public class OwningClassNotSetException extends RuntimeException
{
	private static final long serialVersionUID = 8648516443939598038L;

	public OwningClassNotSetException()
	{
		this("Owning class has not been set, use setOwningClass().");
	}

	public OwningClassNotSetException(String message)
	{
		super(message);
	}
}
