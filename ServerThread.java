import java.io.*;
import java.net.*;

class ServerThread extends Thread {
	Socket sock;	
	public ServerThread(Socket sock) { this.sock = sock; }
	public void run() {
		try {
			HttpRequest req = new HttpRequest();
			String msg = req.receive(sock.getInputStream());
			
			String url = req.getUrl(msg); 	// msg로부터 "/setUser.jsp?id=kim&tel=1111" 추출
			System.out.println(url);
			
			if (url != null) {
				String host = req.getHost(msg); 
				String file = req.getFile(url); // 클라이언트로부터 전달된 HTTP 요청 메시지로부터 파일 이름 검출
				String[] params = req.getParams(url); 
				
				if(params != null) {
					System.out.println("host: " + host + ", file: " + file + ",params[0]:"+ params[0]);
				}
				HttpResponse res = new HttpResponse();
				res.send(sock.getOutputStream(), host, file, params);
			}
			sock.close();
			
		} catch (IOException ex) { 
			ex.printStackTrace(); 
		}
	}
}

