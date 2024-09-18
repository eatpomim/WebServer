import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import http.Session;

public class WebServer {
	public static void main(String[] args) {
		try {
			ServerSocket srvsock = new ServerSocket(8080);
			
			// new Session()으로 호출할 경우, 객체가 별도로 생성
			Session ses1 = Session.getInstance(); 
			ses1.set("127.0.0.1", "test");
			
			Session ses2 = Session.getInstance(); 
			System.out.println(ses2.get("127.0.0.1"));
			
			
			System.out.println("Server started ... \n");
			
			while(true) {
				Socket sock = srvsock.accept();
				ServerThread thread = new ServerThread(sock);
				thread.start();
			}
			
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
