// http 요청 부분
import java.io.*;

class HttpRequest {
	// 
	public String receive(InputStream is) throws IOException {
		ByteArrayOutputStream bao = new ByteArrayOutputStream(); 
		byte[] buf = new byte[1024];
		int cnt;
		while((cnt = is.read(buf)) != -1) {
			bao.write(buf);
			if (cnt < buf.length) break;
		};
		return bao.toString();
	}
	public String getUrl(String msg) {
		String[] lines = msg.trim().split("\n");
		if (lines.length == 0) return null; // (예외처리) 분리된 라인 수가 0일 경우 null 리턴
		
		System.out.println("first: " + lines[0]); // first: GET /index.html HTTP/1.1
		String[] toks = lines[0].trim().split(" ");
		if (toks.length < 2) return null;	// 분리된 토큰 수가 2개 이하일 경우 null 리턴
		if (!toks[0].equals("GET") && !toks[0].equals("POST")) return null; //첫번째 토큰이 GET이나 POST가 아닌 경우 null 리턴
		
		return toks[1];	// 'GET /index.html HTTP/1.1' http중에서 파일이름이 리턴된다.
	}
	public String getHost(String msg) {
		String[] lines = msg.trim().split("\n");
		if (lines.length <= 1) return null;	
		
		System.out.println("second: " + lines[1]); // second: Host: localhost
		String[] toks = lines[1].trim().split(":");
		if (toks.length <= 1) return null;
		if (!toks[0].equals("Host") && !toks[0].equals("host")) return null;
		
		return toks[1].trim(); 
	}
	public String getFile(String url) {
		int idx = url.indexOf("?");
		return (idx <0) ? url : url.substring(0,idx);
	}
	
	public String[] getParams(String url) {
		int idx = url.indexOf("?");
		return (idx <0) ? null : url.substring(idx+1).split("&");
	}
}