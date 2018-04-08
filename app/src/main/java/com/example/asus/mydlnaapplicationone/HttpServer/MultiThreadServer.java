package com.example.asus.mydlnaapplicationone.HttpServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadServer {

    private int port;

    public  MultiThreadServer(int port)
    {
        this.port = port;
    }

	
	public void run() throws IOException
	{
		ServerSocket server = new ServerSocket(port);
        Socket client = null;
        System.out.println("http server:" + port);
        
        while (true) {
            client = server.accept();
            System.out.println(client+ "connetcted to http server");
            Handler handler = new Handler(client);
            Thread thread = new Thread(handler);
            thread.start();
        }
	}
}
