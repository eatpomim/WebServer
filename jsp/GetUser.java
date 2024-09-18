package jsp;
import http.JspService;
import http.Session;

public class GetUser extends JspService {
	public String getHtml(Session ses, String host, String[] params) {
		String[] prms = (String[]) ses.get(host);
		if (prms ==null){
			return "<html><meta charset='utf-8'> 회원정보를 찾을 수 없습니다.</html>";
		}
		return "<html><meta charset='utf-8'> 회원 id: " + getParamValue(prms, "id") + " </html>";
	}
	
}