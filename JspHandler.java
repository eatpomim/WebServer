import java.io.*;

import http.JspService;
import http.Session;

class JspHandler {
	public void send(OutputStream os, String host, String file, String[] params) throws IOException {
		/* If 문으로 미리 정의된 jsp 파일에 대한 응답만 지원하는 것이 아닌, 
		사용자가 정의한 임의의 jsp 클래스를 메모리에 로드하고 실행할 수 있어야 함 */
		 
		file = "jsp" + getClassName(file); 
		try {
			Class c = Class.forName(file); 
			// Class.forName(String path): path에 위치한 클래스를 찾아 메모리로 로드

			JspService svc = (JspService) c.newInstance();
			sendText(os, getMsg(200, svc.getHtml(Session.getInstance(), host, params)));
		} catch(Exception ex) {
			ex.printStackTrace();
			sendText(os, getMsg(500, getHtml("서버 오류가 발생하였습니다.")));
		}
	}
	
	private String getMsg(int code, String html) {
		String msg = "HTTP/1.1" + code + "\n";
		msg += "Content-Type: text/html;charset=utf-8\n";
		msg += "Content-Language: ko\n";
		msg += "Date: " + new java.util.Date().toString()+"\n";
		msg += "\n";
		msg += html;
		return msg;	
	}
	private String getHtml(String desc) {
		return "<html><meta charset='utf-8'>" + desc + "</html>\n";
	}
	
	private String getClassName(String file) {
		// file: "/SetUser.jsp" --> ".SetUser"
		int idx = file.lastIndexOf(".jsp");
		return (idx <0) ? "": file.substring(0,idx).replaceAll("/",".");
	}
	
	private void sendText(OutputStream os, String msg) throws IOException{
		PrintWriter pw = new PrintWriter(new OutputStreamWriter(os, "utf-8"));
		pw.println(msg);
		pw.flush();
	}
	

}