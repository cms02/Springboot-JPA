package com.cms.blog.test;

import java.util.List;
import java.util.function.Supplier;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cms.blog.model.RoleType;
import com.cms.blog.model.User;
import com.cms.blog.repository.UserRepository;

//html파일이 아니라 data를 리턴해주는 controller 
@RestController
public class DummyControllerTest {
	
	@Autowired //의존성 주입 (DI) 
	private UserRepository userRepository;
	
	//save함수는 id를 전달하지 않으면 insert를 해주고
	//save함수는 id를 전달하고 해당 id에 대한 데이터가 있으면 update를 해주고
	//save함수는 id를 전달하고 해당 id에 대한 데이터가 없으면 insert를 한다
	//email, password
	
	@DeleteMapping("/dummy/user/{id}")
	public String delete(@PathVariable int id) {
		try {
			userRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			return "삭제에 실패하였습니다. 해당 id는 DB에 없습니다.";
		}
		return "삭제되었습니다.id: " + id;
	}
	
	@Transactional	//함수 종료시에 자동 commit이 됨.
	@PutMapping("/dummy/user/{id}")
	public User updateUser(@PathVariable int id, @RequestBody User requestUser) { //json 데이터를 요청 -> Java Object(MessageConverter의 Jackson 라이브러리가 변환해서 받아줌 @RequestBody 필요!)
		System.out.println("id: " 		+ id);
		System.out.println("password: " + requestUser.getPassword());
		System.out.println("email: " 	+ requestUser.getEmail());
		
		User user = userRepository.findById(id).orElseThrow(()->{
			return new IllegalArgumentException("수정에 실패하였습니다.");
		});
		
		user.setPassword(requestUser.getPassword());
		user.setEmail(requestUser.getEmail());
		
		//userRepository.save(requestUser);
		//더티 체킹
		
		return user;
	}
	
	@GetMapping("/dummy/users")
	public List<User> list(){
		return userRepository.findAll();
	}
	
	// 한페이지당 2건의 데이터를 리턴받도록
	@GetMapping("/dummy/user")
	public List<User> pageList(@PageableDefault(size=2,sort="id",direction = Sort.Direction.DESC) Pageable pageable){
		Page<User> pagingUser = userRepository.findAll(pageable);
		
		List<User> users = pagingUser.getContent();
		return users;
	}
	
	//http://localhost:8787/blog/dummy/join(요청)
	//http의 body에 username, password, email 데이터를 가지고(요청)
//	@PostMapping("/dummy/join")
//	public String join(String username, String password, String email) { //key = value (약속된 규칙)
//		System.out.println("username: " + username);
//		System.out.println("password: " + password);
//		System.out.println("email: " 	+ email);
//		
//		return "회원가입이 완료되었습니다.";
//	}
	@PostMapping("/dummy/join")
	public String join(User user) { //key = value (약속된 규칙)
		System.out.println("id: "			+ user.getId());
		System.out.println("username: " 	+ user.getUsername());
		System.out.println("password: " 	+ user.getPassword());
		System.out.println("email: " 		+ user.getEmail());
		System.out.println("role: " 		+ user.getRole());
		System.out.println("createDate: " 	+ user.getCreateDate());
		
		user.setRole(RoleType.USER);
		userRepository.save(user);
		return "회원가입이 완료되었습니다.";
	}
	
	
	//{id} 주소로 파라미터를 전달 받을 수 있음
	// http://localhost:8787/blog/dummy/user/3
	@GetMapping("/dummy/user/{id}")
	public User detail(@PathVariable int id) {
		//user/4를 찾았을 때, 데이터베이스에서 못찾았을시 user는 null
		//return null -> 문제가됨..
		//Optional로 User객체를 감싸서 가져옴 -> null인지 아닌지 판단해서 return !
		
//		User user = userRepository.findById(id).get(); //null이 나올 수가 없어! -> 위
		
//		User user = userRepository.findById(id).orElseGet(new Supplier<User>() {  //null 일 때 객체하나 만들어서 넣기!
//
//			@Override
//			public User get() {
//				// TODO Auto-generated method stub
//				return new User();
//			}
//		});
		
//		User user = userRepository.findById(id).orElseGet(new Supplier<User>() {	// 비어있는 User 객체 생성 후 리턴
//
//			@Override
//			public User get() {
//				// TODO Auto-generated method stub
//				return new User();
//			}
//		});
		
//		//람다식
//		User user = userRepository.findById(id).orElseThrow(()->{
//			return new IllegalArgumentException("해당 유저는 없습니다. id: " + id);
//		});
		
		User user = userRepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {

			@Override
			public IllegalArgumentException get() {
				return new IllegalArgumentException("해당 유저는 없습니다.id: " + id);
			}
		});
		
		//요청 : 웹 브라우저
		//user 객체 = 자바 오브젝트
		//변환 (웹브라우저가 이해할 수 있는 데이터) -> json(Gson 라이브러리)
		//스프링부트 = MessageConverter라는 애가 응답시에 자동 작동
		//만약에 자바 오브젝트를 리턴하게 되면 MessageConverter가 Jackson 라이브러리를 호출해서
		//user 오브젝트를 json으로 변환해서 브라우저에게 던져줌
		
		return user;
		
	}

}
