package com.example.asus.mydlnaapplicationone.HttpServer;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

public class Handler implements Runnable {
    Helper helper = new Helper();
    MIME mime = new MIME();
    HashMap<String, String> type = mime.getMime();
    String contentType = null;
    public String encoding = "UTF-8";
    private Socket client;
    PrintWriter out = null;

    public Handler(Socket client) {
        this.client = client;
    }

    public void run() {
        if (client != null) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String header = reader.readLine();
                System.out.println("�ͻ��˷��͵�������Ϣ:");
                System.out.println(header);// ��ȡ������������͹������������ͷ����������Ϣ
                String resource = (String) header.split(" ")[1];// ����������Դ�ĵ�ַ
                System.out.println("�ͻ��˷��͵�������Ϣ����");
                System.out.println("�û��������Դ��:" + resource);
                System.out.println();
                String suffix = null;
                if (resource.equals("/")) {
                    resource = "res/deviceDocument.xml";
                    String[] names = resource.split("\\.");
                    suffix = names[names.length - 1];
                    contentType = type.get(suffix);
                } else {
                    String[] names = resource.split("\\.");
                    suffix = names[names.length - 1];
                    contentType = type.get(suffix);
                }
                String path =resource.substring(1);
                File file = new File(path);
                if (file.exists()) {
                    if (suffix.equals("png") || suffix.equals("jpg") || suffix.equals("jpeg")) {
                        helper.readImg(file, client, contentType);
                    } else {
                        helper.readFile(file, client, contentType);
                    }
                } else {
                	System.out.println("cannot find the request file:"+path);
                	
                    PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                    out.println("HTTP/1.0 404 NOTFOUND");// ����Ӧ����Ϣ,������Ӧ��
                    out.println("Content-Type:text/html;charset=UTF-8");
                    out.println();// ���� HTTP Э��, ���н�����ͷ��Ϣ
                    out.println("sorry, the file " + path + "you found does not exist!");
                    out.close();
                    helper.closeSocket(client);
                } // file.exists
            } catch (Exception e) {
                System.out.println("HTTP����������:" + e.getLocalizedMessage());
            }
        }
    }
}
