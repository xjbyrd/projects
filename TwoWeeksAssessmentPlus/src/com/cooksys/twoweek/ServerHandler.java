/**
 * Developer   : Jason Byrd
 * Class       : ServerHandler.java
 * Description : This class handles the server operations for a file sharing app.
 *               The server is designed to send files to the Client when requested.
 * TODO        : Interrupt all threads to close them safely
 */

package com.cooksys.twoweek;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.concurrent.Callable;

public class ServerHandler implements Callable<String>
{
	private Socket sock;
	private String devName = "Jason Byrd";
	private StringBuilder data;
	DataOutputStream dos;
	
	
	public ServerHandler(Socket sock)
	{
		this.sock = sock;
	}
	
	@Override
	public String call() throws IOException
	{
		sendNameAndFileList();
		sendFile();
		return null;
	}
	
	public void sendNameAndFileList() throws IOException
	{
		dos = new DataOutputStream(sock.getOutputStream());
		File fileDir;
		File[] fileList;
		
		// (4) Server sends DevName and file list to client
		fileDir = new File(GUIHandler.getServerDirectory());
		fileList = fileDir.listFiles();
		
		data = new StringBuilder(devName);
		
		for (File f : fileList)
		{
			if (!f.isDirectory())
			{
				data.append("," + (String)f.getName());
			}
		}
		
		dos.writeUTF(data.toString());
		dos.flush();
	}
	
	public void sendFile() throws IOException
	{
		String fileName = "";
		InputStream is = sock.getInputStream();
		DataInputStream dis = new DataInputStream(is);
		File file;
		FileInputStream fis;
		int testInt;
		
		// (6)(5-8 Repeated) Server gets name of file from Client
		fileName = dis.readUTF();
		System.out.println("Client request: " + fileName);
		// (7)(5-8 Repeated) Server sends requested file to Client
		data = new StringBuilder(GUIHandler.getServerDirectory() + fileName);
		
		file = new File(data.toString());
		
		fis = new FileInputStream(file);
		
		while ((testInt = fis.read()) != -1)
		{
			dos.writeInt(testInt);
		}
		dos.writeInt(-1);
		System.out.println("File Sent!");
		
		fis.close();
		
		if (!Server.die)
		{	
			sendFile();
		}
	}
	
	
}
