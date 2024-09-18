package http;

import java.util.HashMap;

// session: 클라이언트 접속 정보를 일정 시간(일반적으로 30분)동안 저장하기 위한 공간
// 데이터를 키-값(key-value) 형태로 유지
// 키는 클라이언트의 IP 주소와 Agent 정보로 구성
// Java에서는 간단히 HashMap<String, Object>를 이용해 구현 가능

public class Session {
	//// new Session() 형태로 생성자를 호출할 경우, 기존 방식으로 객체를 별도로 생성
	private HashMap<String, Object> table = null;
	private static Session instance = null;
	
	// new Session() 형태로 생성자를 호출할 경우, 기존 방식으로 객체를 별도로 생성
	public Session() {
		table = new HashMap<String, Object>();
	}
	
	// Session.getInstance()로 호출할 경우, 공유 객체 생성
	public static Session getInstance() { 
		if (instance == null) {
			instance = new Session();
		}
		return instance;
	}
	
	public void set(String key, Object val) {
		table.put(key, val);
	}
	
	public Object get(String key) {
		return table.get(key);
	}
	
	public Object remove(String key) {
		return table.remove(key);
	}
}
