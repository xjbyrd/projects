package com.cooksys.twoweek;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class History
{
	private String clientIP;
	private String serverDirectory;
	private String downloadDirectory;
	
	public History() { }

	public String getClientIP()
	{
		return clientIP;
	}
	
	public void setClientIP(String clientIP)
	{
		this.clientIP = clientIP;
	}
	
	public String getServerDirectory()
	{
		return serverDirectory;
	}
	
	public void setServerDirectory(String serverDirectory)
	{
		this.serverDirectory = serverDirectory;
	}
	
	public String getDownloadDirectory()
	{
		return downloadDirectory;
	}
	
	public void setDownloadDirectory(String downloadDirectory)
	{
		this.downloadDirectory = downloadDirectory;
	}
}
