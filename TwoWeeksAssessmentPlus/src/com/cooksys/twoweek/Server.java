/**
 * Developer   : Jason Byrd
 * Class       : Server.java
 * Description : This class provides the multithreaded setup for the server for a file sharing app.
 *               It allows up to 10 clients to connect at a time.
 * TODO        : Interrupt all threads to close them safely
 */

package com.cooksys.twoweek;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Server implements Callable<String>
{
	public static boolean die;
	// 10.1.1.173
	@SuppressWarnings("resource")
	@Override
	public String call() throws Exception
	{
		ServerSocket ss = new ServerSocket(25251);
		Socket sock;
		ss.setSoTimeout(100);
		die = false;
		sock = new Socket();
		
		ExecutorService pool = Executors.newFixedThreadPool(10);
		Callable<String> callable;
		Future<String> future;
		Set<Future<String>> set = new HashSet<Future<String>>();
		
		System.out.println("Server Running...");
		// (1) Listens for Client to connect
		
		while(true)
		{
			try
			{
				sock = ss.accept();
				System.out.println("Connection Recieved");
				
				// (3) Spawns new thread to handle operations
					callable = new ServerHandler(sock);
					future = pool.submit(callable);
					set.add(future);
			}
			catch (IOException e){ }
			
			// (*) If Thread gets interrupted, closes socket and shuts down Server
			if(Thread.currentThread().isInterrupted())
			{
				System.out.println("Server shutting down!");
				ss.close();
				sock.close();
				die = true;
				
				System.out.println("ss closed");
				pool.shutdownNow();
				set.forEach(handler -> {handler.cancel(true);});
				//Thread.sleep(200);
				set.forEach(handler -> {System.out.println(handler.isDone());});
				System.out.println(set.size());
				break;
			}
		}
		return null;
	}
	
}
