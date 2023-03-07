package com.myMongoTest.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mongodb.client.gridfs.model.GridFSFile;
import com.myMongoTest.DTO.SearchDB;
import com.myMongoTest.document.LoginForm;
import com.myMongoTest.document.User;
import com.myMongoTest.document.User2;
import com.myMongoTest.service.UserService;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
public class UserController {

	   private final UserService userService;
	

	  @RequestMapping("/") 
	  public String home(){
	    System.out.println("Hello Boot!! loginForm");
	    return "loginForm"; 
	  }
	  
		@ResponseBody
		@PostMapping("/login")
		public ResponseEntity<String> login(	@RequestBody LoginForm loginForm){
			
			if(loginForm.getId().equals("admin1234") && loginForm.getPassword().equals("1234qwer!!")) {
				return new ResponseEntity<String>("success",HttpStatus.OK);	
			}
			  return new ResponseEntity<String>("fail", HttpStatus.BAD_REQUEST);
		}
	
	@ResponseBody
	@PostMapping("/insertDb")
	public ResponseEntity<String> insertDb(	@RequestBody User user){
		userService.mongoUserInsert(user);
		return new ResponseEntity<String>("success",HttpStatus.OK);
	}
	
	@ResponseBody
	@PostMapping("/joinUser2")
	public ResponseEntity<String> joinUser2(	@RequestBody User2 user){
		System.out.println("user getId: "+user.getId());
		System.out.println("user getEmail : "+user.getEmail());
		System.out.println("user getPassword : "+user.getPassword());
		
		if(userService.mongoFindOneUser2Email(user) == null) {
		
			userService.mongoUser2Insert(user);
			return new ResponseEntity<String>("success",HttpStatus.OK);
		}
		
//			User2 user2 = userService.mongoFindOneUser2Email(user);
//			System.out.println("user2 : "+user2.getEmail());
//		
//			if (!user2.getEmail().equals(user.getEmail())) {
//				System.out.println("추가 작업 실행 됨.");
//			}
				
		return ResponseEntity.badRequest().build();
	}
	
	@ResponseBody
	@PostMapping("/updateDb")
	public ResponseEntity<String> updateDb(	@RequestBody User user){
		userService.mongoUserUpdate(user);
		return new ResponseEntity<String>("success",HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping("/findAll")
	public List<User> list( ){
		List<User> userList = userService.mongoFindAll();
		return userList;
	}
	
	@ResponseBody
	@PostMapping("/searchDb")
	public List<User> searchlist( @RequestBody SearchDB searchDB){
		List<User> userList = userService.mongoSearchFindAll(searchDB);
		return userList;
	}
	
//	  @RequestMapping({"/hello", "/"})
	  @RequestMapping("/hello")
	  public String hello(Model model ){
		List<User> userList = userService.mongoFindAll();
		model.addAttribute("user",  userList);
		return "hello";
	  } 
	  
	  @RequestMapping("/main")
	  public String main(Model model ){
		List<User> userList = userService.mongoFindAll();
		model.addAttribute("user",  userList);
		return "main";
	  } 
	  
	  @RequestMapping("/joinForm")
	  public String joinForm(Model model ){
		return "joinForm";
	  } 
	  
	  @RequestMapping("/updateForm/{id}")
	  public String updateForm(	Model model , @PathVariable Long id){
		User user = userService.mongoFindOne(id);
		model.addAttribute("user",  user);
		return "updateForm";
	  } 
	  
	  @ResponseBody
		@DeleteMapping("/dbDelete/{id}")
		public Long delete(@PathVariable Long id) {
			userService.deleteDb("_id", id);
			return id;
	  
}
}
