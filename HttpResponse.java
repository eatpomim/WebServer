// http 응답부분

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

class HttpResponse {
	public void send(OutputStream os, String host, String file, String[] params) throws IOException { 
		if (isJsp(file)) {
			new JspHandler().send(os, host, file, params);
		}
		else {
			file = "web" + file; // HTML 파일이 저장되는 루트 디렉토리로 web 폴더로 지정
			System.out.println("send file: " + file);
		
			/*Session ses = Session.getInstance();
			ses.get("localhost");*/
			
			if ((new File(file)).exists() == false) {
				// 파라미터로 전달된 file에 해당하는 파일이 존재하는지 체크
				// 파일이 존재하지 않을 경우, 디폴트 메시지 리턴
				sendText(os, getMsgNotFound());
			}
			else {
				//파일이 존재할 경우, 디스크에서 읽어서 리턴
				if(isImage(file)) { 
					// isImage(file) : file로부터 확장자 분리 확장자 찾기 위해서, 뒤에서 부터'.'찾음
					sendBytes(os, getMsgImage(file));
				}else {
					sendText(os, getMsgText(file));
				}
			}
		}
	}
	// Content-Type: 에 이미지라는 것을 정확히 명시 해야함(자동인식x)
	// 텍스트는 헤더가 없어도 <html>이라는 태그가 있다면 브라우저가 자동으로 인식하고 출력함
	
	private boolean isJsp(String file) {
		int idx = file.lastIndexOf(".");
		String ext = (idx > 0) ? file.substring(idx+1) : "";
		return (ext.equals("jsp")) ? true : false;
	}
	private boolean isImage(String file) {
		// file.lastIndexOf(“.”)을 이용해 file에서 나타나는 마지막 “.”의 위치 추출
		// file.substring(idx+1)을 이용해 “.” 이후의 텍스트를 확장자로 분리
		// 분리된 확장자가 “jpg”거나 “jpeg”일 경우 true를 리턴, 그 외의 경우 false 리턴

		int idx = file.lastIndexOf(".");
		String ext = (idx > 0) ? file.substring(idx+1) : "";
		return (ext.equals("jpg") || ext.equals("jpeg")) ? true : false;
	}
	
	// body 외 헤더 추가
	private String getMsgNotFound() {
		String msg = "HTTP/1.1 404\n";
		msg += "Content-Type: text/html;charset=utf-8\n";
		msg += "Content-Language: ko\n";
		msg += "Date: " + new java.util.Date().toString()+"\n";
		msg += "\n";
		msg += "<html><meta charset='utf-8'>요청하신 파일이 존재하지 않습니다.</html>\n";
		return msg;
	}
	
	// body 외 헤더 추가
	private String getMsgText(String path) throws IOException {
		//getMsgText()는 스트링으로 반환하는데 반해, getMsgImage()는 바이트 배열 형태로 반환
		
		byte[] bytes = Files.readAllBytes(Paths.get(path));
		String msg = "HTTP/1.1 200\n";
		msg += "Content-Type: text/html;charset=utf-8\n";
		msg += "Content-Language: ko\n";
		msg += "Content-Length: " + bytes.length + "\n";
		msg += "Date: " + new java.util.Date()+"\n";
		msg += "\n";
		msg += new String(bytes, "utf-8");
		return msg;
	}
	
	private byte[] getMsgImage(String path) throws IOException{
		byte[] bytes = Files.readAllBytes(Paths.get(path));
		String msg = "HTTP/1.1 200\n";
		msg += "Content-Type: image/jpeg\n";
		msg += "Content-Length: " + bytes.length + "\n";
		msg += "\n";
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		bao.write(msg.getBytes());
		bao.write(bytes);
		return bao.toByteArray();
	}
	
	//send()의 파라미터로 전달되는 file의 확장자 체크 필요
	// 확장자가 .jpg, .jpeg 등의 이미지 파일일 경우  바이트 배열 형태로 파일을 읽어서 전송
	// 그 외의 경우  기존과 같이 텍스트 형태로 파일을 읽은 후 전송
	private void sendText(OutputStream os, String msg) throws IOException{
		PrintWriter pw = new PrintWriter(new OutputStreamWriter(os, "utf-8"));
		pw.println(msg);
		pw.flush();
	}
	private void sendBytes(OutputStream os, byte[] bytes) throws IOException{
		os.write(bytes);
		os.flush();
	}
}

