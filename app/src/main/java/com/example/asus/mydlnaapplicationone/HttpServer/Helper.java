package com.example.asus.mydlnaapplicationone.HttpServer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Helper {
    
	public void closeSocket(Socket socket) {
        try {
            socket.close();
            System.out.println(socket + "�뿪��HTTP������");
            System.out.println();
            System.out.println();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void readImg(File file, Socket client, String contentType) {
        PrintStream out = null;
        FileInputStream fis = null;
        try {
            out = new PrintStream(client.getOutputStream(), true);
            fis = new FileInputStream(file);
            byte data[] = new byte[fis.available()];
            out.println("HTTP/1.0 200 OK");// ����Ӧ����Ϣ,������Ӧ��
            out.println("Content-Type:" + contentType + ";charset=UTF-8");
            out.println("Content-Length: " + file.length());
            out.println();// ���� HTTP Э��, ���н�����ͷ��Ϣ
            fis.read(data);
            out.write(data);
            fis.close();
        } catch (Exception e) {
            out.println("HTTP/1.0 500");// ����Ӧ����Ϣ,������Ӧ��
            out.println("");
            out.flush();
        } finally {
            out.close();
            closeSocket(client);
        }
    }

    public void readFile(File file, Socket client, String contentType) {
        PrintWriter out = null;
        try {
            out = new PrintWriter(client.getOutputStream(), true);
            FileReader in = new FileReader(file);
            BufferedReader breader = new BufferedReader(in);
            String s = null;
            StringBuffer sbu = new StringBuffer();
            out.println("HTTP/1.0 200 OK");// ����Ӧ����Ϣ,������Ӧ��
            out.println("Content-Type:" + contentType + ";charset=UTF-8");
            out.println();// ���� HTTP Э��, ���н�����ͷ��Ϣ
            while ((s = breader.readLine()) != null) {
                out.println(s);
            }
            System.out.println(file.getName() +" sent successfully!");

        } catch (Exception e) {
            out.println("HTTP/1.0 500");// ����Ӧ����Ϣ,������Ӧ��
            out.println("");
            out.flush();
        } finally {
            out.close();
            closeSocket(client);
        }
    }

}
