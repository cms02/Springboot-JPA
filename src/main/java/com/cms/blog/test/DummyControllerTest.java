package com.cms.blog.test;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DummyControllerTest {
	
	//http://localhost:8787/blog/dummy/join(요청)
	//http의 body에 username, password, email 데이터를 가지고(요청)
	@PostMapping("/dummy/join")
	public String join(String username, String password, String email) { //key = value (약속된 규칙)
		System.out.println("username: " + username);
		System.out.println("password: " + password);
		System.out.println("email: " 	+ email);
		
		return "회원가입이 완료되었습니다.";
	}

}
