package com.guy.log.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogFileOutputStream extends OutputStream {

	private FileOutputStream os = null;
	private String filename = null;
	private String logFileName = null;

	private boolean append = false;

	private Date previousDate = new Date();
	private Date currentDate = new Date();
	private SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
	
	private long currentTime = 0L;
	
	public LogFileOutputStream(String filename) throws FileNotFoundException
	{
		this(filename, false);
	}

	public LogFileOutputStream(String filename, boolean append) throws FileNotFoundException
	{
		super();
		this.filename = filename;
		this.append = append;
		
		openLogFile();
	}

	public void flush() throws IOException
	{
		this.os.flush();
	}

	public void close() throws IOException
	{
		this.os.close();
	}

	public void write(int i) throws IOException
	{
		os.write(i);
	}

	public void write(byte[] b) throws IOException
	{
		if (currentTime == 0L) {
			currentTime = System.currentTimeMillis();
		}
		
		if (System.currentTimeMillis() > currentTime + 10000L) {
			// Time to check the date...
			currentDate.setTime(System.currentTimeMillis());
			String strDate = fmt.format(currentDate);
			
			if (!strDate.equals(fmt.format(previousDate))) {
				/*
				 * Morning! A new day has arrived...
				 */
				previousDate = currentDate;
				currentTime = System.currentTimeMillis();
				openLogFile();
			}
		}
		
		this.os.write(b);
	}
	
	private synchronized void openLogFile() throws FileNotFoundException
	{
		FileOutputStream ostream = null;
		
		try {
			String dateSuffix = fmt.format(currentDate);

			int dotPos = filename.lastIndexOf('.');
			
			if (dotPos > 0) {
				String tempName = filename.substring(0, dotPos) + "_" + dateSuffix;
				String ext = filename.substring(dotPos + 1);
				logFileName = tempName + "." + ext;
			}
			else {
				logFileName = this.filename + dateSuffix;
			}
			
			ostream = new FileOutputStream(new File(logFileName), this.append);
			
			if (this.os != null) {
				this.os.flush();
				this.os.close();
			}

			this.os = ostream;
		}
		catch (FileNotFoundException fileEx) {
			System.out.println("Error opening output file [" + logFileName + "] with error - " + fileEx.getMessage());
			throw fileEx;
		}
		catch (IOException ioEx) {
			System.out.println("IOException opening output file [" + logFileName + "] with error - " + ioEx.getMessage());
		}
	}
	
	public String getLogFileName() {
		return logFileName;
	}
}
