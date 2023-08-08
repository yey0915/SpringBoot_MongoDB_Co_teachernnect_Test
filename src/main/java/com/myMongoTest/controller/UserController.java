package com.myMongoTest.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.myMongoTest.service.ImageService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.myMongoTest.DTO.SearchDB;
import com.myMongoTest.document.Memo;
import com.myMongoTest.document.User2;
import com.myMongoTest.document.Users;
import com.myMongoTest.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequiredArgsConstructor
public class UserController {

	   private final UserService userService;
	   private final PasswordEncoder passwordEncoder;

	private final ImageService imageService;

	@Autowired
	private GridFsTemplate gridFsTemplate;

	@Autowired
	private GridFsOperations gridFsOperations;
	
	    @GetMapping(value = "/login/error")
	    public String loginError(Model model){
	        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
	        return "loginForm";
	    }
	  //test
	    @GetMapping(value = "/login")
	    public String loginMember(){
	        return "loginForm";
	    }
	
	@ResponseBody
	@PostMapping("/insertDb")
	public ResponseEntity<String> insertDb(	@RequestBody Users user){
		userService.mongoUserInsert(user);
		return new ResponseEntity<String>("success",HttpStatus.OK);
	}

//	@PostMapping
//	public ResponseEntity uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
//		InputStream inputStream = file.getInputStream();
//		ObjectId objectId = gridFsTemplate.store(inputStream, file.getOriginalFilename(), file.getContentType());
//		return new ResponseEntity<>( HttpStatus.OK);
//	}
	
	@ResponseBody
	@PostMapping("/insertMemoWithImage")
	public ResponseEntity insertMemoWithImage (@RequestPart(value = "key") Memo memo,
											   @RequestPart(value = "file",required = false) MultipartFile file) throws IOException {

//		System.out.println("memo getTitle : " + memo.getTitle());
//		System.out.println("memo getMessage : " + memo.getMessage());
//		System.out.println("memo getDateField: " + memo.getDateField());
//		System.out.println("memo getFile: " + memo.getFile());
//		System.out.println("file : " + file);

		if(file != null) {
		InputStream inputStream = file.getInputStream();
		ObjectId objectId = gridFsTemplate.store(inputStream, file.getOriginalFilename(), file.getContentType());
		String objectIdToString = objectId.toString();
//		System.out.println("objectIdToString : " + objectIdToString);
		memo.setImageFileObjectId(objectIdToString);
		}

		userService.mongoMemoInsert(memo);




		return new ResponseEntity<>( HttpStatus.OK);
	}
	@ResponseBody
	@PostMapping("/insertMemo")
	public ResponseEntity<String> insertMemo(	@RequestBody Memo memo){
		userService.mongoMemoInsert(memo);
		return new ResponseEntity<String>("success",HttpStatus.OK);
	}
	
	  
	  @RequestMapping("/joinForm")
	  public String joinForm(Model model ){
		  model.addAttribute("User2", new User2());
//		  System.out.println("joinFomr");
		return "joinForm";
	  } 
	
	@PostMapping("/joinUser")
	public String joinUser(User2 user ){
		String email = user.getEmail();
		String password = passwordEncoder.encode(user.getPassword());
		user.setPassword(password);
		if(userService.mongoFindOneUser2Email(email) == null) {
		
			userService.mongoUser2Insert(user);
			return "redirect:/";
		}
		return "/joinForm";
	}
	
	@ResponseBody
	@PostMapping("/updateDb")
	public ResponseEntity<String> updateDb(	@RequestBody Users user){
		userService.mongoUserUpdate(user);
		return new ResponseEntity<String>("success",HttpStatus.OK);
	}

	@ResponseBody
	@PostMapping("/updateMemo")
	public ResponseEntity<String> updateMemo(	@RequestBody Memo memo){
		userService.mongoMemoUpdate(memo);
		return new ResponseEntity<String>("success",HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping("/findAll")
	public List<Users> list( ){
		List<Users> userList = userService.mongoFindAll();
		return userList;
	}

	@ResponseBody
	@GetMapping("/findAllMemo")
	public List<Memo> listMemo( ){
		List<Memo> memoList = userService.mongoFindAllMemo();
		return memoList;
	}
	
	@ResponseBody
	@PostMapping("/searchDb")
	public List<Memo> searchlist( @RequestBody SearchDB searchDB){
		List<Memo> memoList = userService.mongoSearchFindAll(searchDB);
		return memoList;
	}
	

	  @RequestMapping("/admin")
	  public String hello(Model model ){
		List<Users> userList = userService.mongoFindAll();
		model.addAttribute("user",  userList);
		return "admin";
	  } 
	  
	  @RequestMapping("/")
	  public String main(Model model ){
		List<Users> userList = userService.mongoFindAll();
		model.addAttribute("user",  userList);
		return "main";
	  } 

	  
	  @RequestMapping("/updateForm/{id}")
	  public String updateForm(	Model model , @PathVariable Long id){
		Users user = userService.mongoFindOne(id);
		model.addAttribute("user",  user);
		return "updateForm";
	  }

	@RequestMapping("/updateFormMemo/{id}")
	public String updateFormMemo(	Model model , @PathVariable String id){
//		System.out.println("id : "+ id);
		ObjectId objectId = new ObjectId(id);
//		System.out.println("objectId : "+ objectId);
		Memo memo = userService.mongoFindOneMemo(objectId);
		model.addAttribute("memo",  memo);
		return "updateForm";
	}

	@ResponseBody
		@DeleteMapping("/dbDelete/{id}")
		public String delete(@PathVariable String id) {
			userService.deleteDb("_id", id);
			return id;
	  
}
}
