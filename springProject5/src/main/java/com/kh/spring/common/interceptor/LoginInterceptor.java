package com.kh.spring.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


//인터셉터를 사용하기 위해 HandlerInterceptor를 구현하기
/*
 * 요청을 가로채서 특정 권한을 체크하거나,로그를 기록하거나, 공통적으로 사용해야하는 코드를 처리한다
 * 
 * **필터와의 차이점 - 간섭시점
 * 필터 : 디스패처 서블릿 전  (인코딩 작업)
 * 인터셉터 : 디스패처 서블릿 후 컨트롤러 전 (권한 및 추가작업)
 * 
 * */
public class LoginInterceptor implements HandlerInterceptor{

	
	
	//간섭 시점 1 : 요청 처리 전
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//request : 사용자는 무엇을 요청했는가
		//response : 사용자에게 보낼 정보가 있는가
		//handler : 이 요청은 누가 처리할 것인가
		/*
		System.out.println("테스트 인터셉터 요청 처리 전 ");
		System.out.println(request.getSession().getAttribute("loginUser"));
		System.out.println(response);
		System.out.println(handler);
		*/
		
		//로그인 정보를 확인하여 로그인이 되어있지 않다면 요청 막아주기
		//회원 권한 설정 
		//로그인정보가 null이라면 기존 요청 막아주기 
		HttpSession session = request.getSession();
		if(session.getAttribute("loginUser")==null) {
			
			session.setAttribute("alertMsg", "로그인 후 이용 가능한 서비스입니다.");
			//응답뷰에 대한 설정 처리 
			response.sendRedirect(request.getContextPath()); //메인페이지로 보내기 
			
			return false;//기존 요청흐름 방지
		}
		
		//return true여야 요청흐름 유지
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}
	
	//간섭시점 2 : 요청 처리 후 (view가 만들어지기전)
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		//request : 사용자는 무엇을 요청했는가
		//response  : 사용자에게 보낼 정보가 있는가
		//handler : 이 요청은 누가 처리할 것인가
		//modelAndView : model - 전달데이터 / view - 이동할 뷰 페이지 정보
		/*
		System.out.println("테스트 인터셉터 요청 처리 후 ");
		System.out.println(request.getSession().getAttribute("loginUser"));
		System.out.println(handler);
		System.out.println(modelAndView);
		
		modelAndView.addObject("list", new ArrayList<Board>());
		*/
		
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}
	
	//간섭시점 3 : 사용자에게 출력되기 직전
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

		//request : 사용자는 무엇을 요청했는가
		//response : 사용자에게 보낼 정보가 있는가
		//handler : 이 요청은 누가 처리할 것인가
		//exception : 처리과정중 예외가 발생했는가 
		
		
		//System.out.println("예외 : "+ex);
		
		
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
	
	
	
}
