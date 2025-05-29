package com.kh.spring.member.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kh.spring.member.model.service.MemberService;
import com.kh.spring.member.model.vo.Member;

@Controller //Controller 타입의 어노테이션을 부여하면 spring이 bean scan을 통해 Controller bean으로 등록해준다.
public class MemberController {
	
	//사용할 Service객체를 선언해놓고 스프링이 관리할 수 있도록 처리하기
	//기존 방식
	//private MemberService service = new MemberService();
	/*
	 * 기존 객체 생성 방식
	 * 객체간의 결합도가 높다.(소스코드의 수정이 일어날 경우 직접 찾아서 변경해야함)
	 * 서비스가 동시에 많은 횟수가 요청될 경우 그만큼 객체 생성이 발생됨 
	 * 
	 * Spring에선 DI(Dependency Injection)을 이용한 방식으로 
	 * 객체를 생성시켜 주입한다. (객체간의 결합도를 낮춰준다)
	 * new 라는 키워드없이 선언문만 사용하고 @Autowired라는 어노테이션을 부여해서 
	 * 스프링이 직접 bean을 관리 할 수 있도록 등록한다.
	 * 
	 * @Autowired : 스프링이 bean을 자동 주입할 수 있도록 하는 어노테이션
	 * 
	 * */
	
	@Autowired
	private MemberService service;
	
	//암호처리할 Bcrypt 등록
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	
	/*
	 * Spring에서 파라미터(요청시 전달데이터)를 받아주는 방법
	 * 
	 * 1.HttpServletRequest 이용 - (기존 jsp/servlet 방식)
	 * 요청을 처리하는 메소드의 매개변수에 필요한 객체를 입력하면 spring이 메소드 호출 시 자동으로 해당 객체를 생성하여
	 * 주입해준다.
	 * */
	
	/*
	@RequestMapping("login.me")
	public String loginMember(HttpServletRequest request) {
		
		String userId = request.getParameter("userId");
		String userPwd = request.getParameter("userPwd");
		
		System.out.println(userId+" "+userPwd);
		
		
		//요청에 대한 응답 처리는 문자열로 경로를 작성한다.
		//작성된 경로에 viewResolver가 앞쪽엔 WEB-INF/views/ 를 뒤쪽엔 .jsp를 붙여서 위임 처리 한다.
		//만약 재요청(redirect)을 하고 싶다면 redirect:경로 로 작성해주면 된다.
		//return "main"; //  /WEB-INF/views/main.jsp 와 같이 요청된다
		return "redirect:/"; //재요청방식은 /는 컨텍스트루트 뒤에 붙는다
	}
	*/
	
	/*
	 * 2. @RequestParam 어노테이션을 이용하는 방법
	 * request.getParameter("키")로 값을 추출하는 역할을 대신 수행해주는 어노테이션
	 * value 속성의 값으로 jsp에서 작성했던 name 속성값을 담으면 해당 매개변수로 받아올 수 있다.
	 * 만약 전달된 값이 비어있는 상태라면 defualtValue 속성으로 기본값을 지정해줄 수 있다.
	 * */
	
	/*
	@RequestMapping("/login.me")
	public String loginMember(@RequestParam(value="userId") String userId
							 ,@RequestParam(value="userPwd") String userPwd
							 ,@RequestParam(value="currentPage",defaultValue = "1" ) String currentPage) {
		
		System.out.println(userId+" "+userPwd);
		System.out.println(currentPage);
		return "redirect:/";
	}
	*/
	
	/*
	 * 3. @RequestParam 어노테이션을 생략하는 방법
	 * 단, 매개변수명을 jsp의 name 속성값과 일치시켜야만 해당 데이터가 파싱된다.
	 * 또한 어노테이션을 생략했기 때문에 defaultValue는 사용할 수 없음 
	 * */
	
	/*
	@RequestMapping("login.me")
	public String loginMember(String userId,String userPwd,String name) {
		
		
		System.out.println(userId+" "+userPwd);
		System.out.println(name);
		
		return "redirect:/";
	}
	*/
	
	/*
	 * 4.커맨드 객체 방식
	 * 해당 메소드의 매개변수로
	 * 요청시 전달값을 담고자 하는 VO 를 세팅 후 
	 * 요청시 전달값의 키값(jsp의 name속성값) 을 VO에 각 필드명으로 작성하면
	 * 스프링 컨테이너가 해당 객체를 기본생성자로 생성 후 내부적으로 setter 메소드를 찾아
	 * 요청시 전달값을 해당 필드에 담아서 반환해준다.
	 * 
	 * 반드시 name속성값과 필드명이 같아야한다. 
	 * 
	 * 
	 * 요청 처리 후 응답데이터를 담고 응답페이지로 포워딩 또는 재요청해보기
	 * HttpSession이 필요하다면 매개변수에 작성 -> 스프링이 객체 주입해줌
	 * 
	 * 응답뷰로 포워딩(위임)시 전달 데이터 담을 수 있는 객체 
	 * 1. Model 
	 * requestScope를 담당하는 객체로 key value세트로 데이터를 담아서 전달할 수 있다.
	 * 데이터 담는 메소드는 기존에 사용하던 setAttribute()가 아닌 addAttribute()이다 
	 * */
	@RequestMapping("login.me")
	public String loginMember(Member m
							 ,HttpSession session
							 ,Model model) {
		
		//사용자가 입력한 아이디만 가지고 일치한 회원 정보 조회
		Member loginUser = service.loginMember(m);
		//해당 정보에서 비밀번호를 추출(암호문)하여 사용자가 입력한 비밀번호(평문)과 판별하는 메소드를 이용하기
		//BcryptPasswordEncoder는 암호문을 복호화 하여 평문으로 되돌려주지 않는다.
		//대신 암호문을 복호화했을때 나오는 평문과 일치하는지 비교해주는 판멸 메소드는 제공해줌
		//bcrypt.matches(평문,암호문) : 평문과 암호문을 비교해주는 메소드 : true / false 반환
//		System.out.println("사용자가 입력한 비밀번호 : "+m.getUserPwd());
//		System.out.println("평문에 대한 암호문 : "+bcrypt.encode(m.getUserPwd()));
		
		
		//성공 실패 처리 
		if(loginUser != null && bcrypt.matches(m.getUserPwd(), loginUser.getUserPwd())) { //성공시 
			session.setAttribute("alertMsg", "로그인 성공!");
			session.setAttribute("loginUser", loginUser);
			
			//메인페이지로 재요청
			return "redirect:/";
			
		}else {//실패시
			//오류메시지를 model에 담아서 에러페이지로 포워딩 해보기 
			model.addAttribute("errorMsg","로그인 실패!!");
			
			//viewResolver가 WEB-INF/views/ 와 .jsp를 붙여서 경로를 완성해준다
			return "common/errorPage";
			
		}
	}
	
	//로그아웃
	@RequestMapping("logout.me")
	public String logoutMember(HttpSession session) {
		
		//loginUser 정보 세션에서 삭제하기 
		session.removeAttribute("loginUser");
		
		//메인페이지로 보내기
		return "redirect:/";
	}
	
	
	//@RequestMapping은 get,post 요청 상관없이 처리한다. 
	//get과 post를 따로 처리하고자 한다면 
	//1.@RequestMapping에 method 속성 추가
	//2.@PostMapping,@GetMapping 어노테이션 사용 
	
	//회원가입 페이지로 이동
	//@RequestMapping(value="insert.me",method = RequestMethod.GET)
	@GetMapping("insert.me")
	public String insertMember() {
		
		
		//회원가입 페이지로 포워딩처리 
		//경로 : /WEB-INF/views/member/memberEnrollForm.jsp 
		return "member/memberEnrollForm";
	}
	
	//회원가입 등록요청
	//@RequestMapping(value="insert.me",method = RequestMethod.POST)
	@PostMapping("insert.me")
	public String insertMember(Member m
							  ,HttpSession session
							  ,Model model) {
		//나이를 입력하지 않은 경우 int 자료형인 age필드에 "" 빈문자열이 들어가려고 하기 때문에
		//오류가 발생한다. 400번 에러 (잘못된요청)
		//Member VO의 age필드를 String으로 변경한다 이때 lombok을 이용하여 손쉽게 처리하기.
		
		//비밀번호가 사용자가 입력한 그대로 저장되는것을 방지하기
		//Bcrypt 암호화 방식을 이용하여 암호문을 저장하여 보안성을 유지하기
		//1)스프링 시큐리티 모듈에서 제공하는 DI 추가하기
		//2)BCryptPasswordEncoder 클래스 bean 추가하기
		//3)web.xml에 spring-security.xml 파일을 로딩할 수 있도록 추가하기.
		
		//bcrypt.encode(평문) : 평문을 암호화한 값을 반환해준다.
		
		//비밀번호는 암호문으로 변경하여 m 에 담고 데이터베이스에 등록작업하기 
		
		String encPwd = bcrypt.encode(m.getUserPwd());//평문 암호문으로 변경 
		
		m.setUserPwd(encPwd);//객체에 암호문 비밀번호 넣기
		
		//서비스에 회원가입 메소드 호출 및 전달
		int result = service.insertMember(m);
		//mybatis 메소드는 sqlSession.insert() 를 사용하시면 됩니다.
		
		//mapper에 resultMap 또는 resultType 작성 X  parameterType만 작성
		//회원가입 성공시 - 회원가입을 환영합니다. 메시지와 함께 메인 페이지로 보내기(재요청)
		if(result>0) {
			session.setAttribute("alertMsg", "회원가입을 환영합니다.");
			return "redirect:/";
		}else {
			//회원가입 실패시 - 회원가입에 실패하였습니다. 메시지와 함께 에러페이지로 포워딩(위임) - model 객체 이용
			model.addAttribute("errorMsg","회원가입에 실패하였습니다.");
			
			return "common/errorPage";
		}
	}
	
	//마이페이지 이동 메소드
	@RequestMapping("mypage.me")
	public String myPage() {
		
		return "member/mypage";
	}
	
	
	//정보수정 메소드 
	@RequestMapping("update.me")
	public String updateMember(Member m
							  ,HttpSession session
							  ,Model model) {
//		updateMember() 메소드를 작성하여 정보수정 처리해보기 
//    	성공시 : 정보수정 성공! 메시지와 함께 마이페이지로 되돌아오기 (변경된 정보 갱신)
//    	실패시 : 에러페이지로 정보수정 실패! 메시지와 함께 위임시키기(model 이용)
		
		int result = service.updateMember(m);
		
		if(result>0) { //성공시
			
			session.setAttribute("alertMsg", "정보수정 성공!");
			
			//변경된 정보 갱신시키기 위해서 회원정보 조회해오기(로그인메소드 재사용)
			Member updateMember = service.loginMember(m);
			
			session.setAttribute("loginUser", updateMember);//로그인정보 갱신
			
			return "redirect:/mypage.me"; //마이페이지 재요청
		}else {//실패
			
			model.addAttribute("errorMsg","정보수정 실패!");
			
			return "common/errorPage";//에러페이지로 위임
			
		}
		
		
	}
	
	
	@RequestMapping("delete.me")
	public String deleteMember(Member m 
							  ,HttpSession session) {
//		메소드명 : deleteMember()
//    	매핑주소 : delete.me
//    	성공시 회원 탈퇴 성공 메시지와 함께 메인페이지로 이동(재요청) - 세션에 담긴 로그인정보 삭제하기 
//    	실패시 회원 탈퇴 실패 메시지와 함께 마이페이지로 이동(재요청)
		
		//사용자 아이디가 있어야 해당 회원정보의 비밀번호를 확인할 수 있으니 
		//아이디 추출하기
		//전달받은 아이디를 이용하여 데이터베이스에서 회원정보 조회 후 
		//조회된 비밀번호(암호문) 와 전달받은 비밀번호(평문) 판별 후 
		//통과하면 성공 아니면 실패 처리하기 
		
		Member loginUser = service.loginMember(m);
		
		//평문과 암호문을 비교하여 판별하는 mathches 메소드 이용하기.
		
		if(bcrypt.matches(m.getUserPwd(), loginUser.getUserPwd())) {
			//비밀번호를 제대로 입력한 경우
			
			int result = service.deleteMember(m);
			
			if(result>0) {//회원탈퇴처리 성공시
				session.setAttribute("alertMsg", "회원 탈퇴 성공");
				
				//로그인된 정보 삭제
				session.removeAttribute("loginUser");
				
				return "redirect:/";//메인페이지로
				
			}else {//실패시 
				session.setAttribute("alertMsg", "회원 탈퇴 실패");
				
				return "redirect:/mypage.me";//마이페이지로
			}
			
		}else {//입력한 비밀번호가 등록된 비밀번호와 다를때 
			
			session.setAttribute("alertMsg", "비밀번호를 잘못입력하셨습니다.");
			return "redirect:/mypage.me";
		}
		
		
		
	}
	
	//문자열 데이터로 응답처리
	@ResponseBody
	@RequestMapping(value="idCheck.me",produces = "text/html;charset=UTF-8")
	public String idCheck(String userId) {
		
		//전달받은 userId와 같은 회원정보가 있는지 조회후 
		//있다면 NNNNN을 없다면 NNNNY를 반환하기 
		
		int count = service.idCheck(userId);
		
		if(count>0) {//중복 
			return "NNNNN";
		}else {//사용가능		
			return "NNNNY";
		}
	}
	
	
	
	
	
	
	
	

}
