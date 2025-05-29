package com.kh.spring.board.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.kh.spring.board.model.service.BoardService;
import com.kh.spring.board.model.vo.Attachment;
import com.kh.spring.board.model.vo.Board;
import com.kh.spring.board.model.vo.Reply;
import com.kh.spring.common.model.vo.PageInfo;
import com.kh.spring.common.template.Pagination;


@Controller
public class BoardController {
	
	
	//서비스 선언 
	@Autowired
	private BoardService service;
	
	//게시글 목록 이동 메소드
	@RequestMapping("list.bo")
	public String boardList(@RequestParam(value="currentPage",defaultValue="1")
							int currentPage
							,Model model) throws Exception {
		//자유게시판 첫 목록은 항상 1번 페이지일테니 기본값으로 1 설정해두기
		
		
		//추가적으로 필요한 값 
		int listCount = service.listCount(); //총 게시글 개수
		int boardLimit = 5; //게시글 보여줄 개수
		int pageLimit = 10; //페이징바 개수 
		
		PageInfo pi = Pagination.getPageInfo(listCount, currentPage, pageLimit, boardLimit);
		
		//게시글 목록 조회
		ArrayList<Board> list = service.boardList(pi);
		
		
		//위임시 목록데이터 전달 
		model.addAttribute("list", list);
		model.addAttribute("pi", pi);
		
		return "board/boardListView";
	}
	
	
	
	
	//메소드명 boardDetail() - SELECT
	//조회수 증가 메소드명 increaseCount() - DML  
	//조회수 증가가 성공이라면 게시글 조회해서 상세페이지로 전달 및 이동 
	//실패시 오류발생 메시지를 담고 에러페이지로 위임처리하기
	@RequestMapping("detail.bo")
	public String bardDetail(int bno
							,Model model) {
		
		//글번호를 이용해서 조회수 증가 및 게시글 조회처리하기 
		
		int result = service.increaseCount(bno);
		
		
		if(result>0) {//조회수 증가 처리가 성공이라면 
			
			Board b = service.boardDetail(bno);
			
			
			model.addAttribute("b",b);
			
			return "board/boardDetailView";
			
		}else { //실패라면
			model.addAttribute("errorMsg","오류발생!");
			return "common/errorPage";
		}
		
	}
	
	
	//글 작성 페이지로 이동 메소드
	@GetMapping("insert.bo")
	public String boardEnrollForm() {
		
		return "board/boardEnrollForm";
	}
	
	//글 작성 요청 
	@PostMapping("insert.bo")
	public String insertBoard(Board b
							 ,MultipartFile uploadFile
							 ,HttpSession session) {
		
		/*
		 * 첨부파일 처리를 하려면 form 요청 인코딩형식이 multipart/form-data 로 설정되어야한다.
		 * 이때 기존 request요청과는 형식이 달라졌기 때문에 그에 맞는 데이터형식을 사용해야한다.
		 * multipart 처리시 데이터를 전달받기 위해선 MultipartResolver가 필요하여
		 * pom.xml에 dependency를 추가하고 bean을 등록해서 사용한다.
		 * commons-fileupload / commons-io  추가하기
		 * 
		 * 업로드된 파일 정보를 담아주는 객체 MultipartFile 
		 * 요청시 input file 태그의 name값과 일치하도록 참조변수명 설정하기
		 * */
		
		//첨부파일이 전달되었는지 확인하기 
		//전달된 파일이 있을 경우 파일명이 빈문자열이 아닌 경우일테니 판별용 조건으로 사용
		//전달된 파일이 없어도 MultipartFile에는 객체가 생성되어 주입되기 때문에 null값 비교로 판별할 수 없음.
		
		//파일명 추출 메소드 : getOriginalFileName() 
		//파일명이 빈문자열이 아닌경우 (전달된 파일이 있는 경우)
		if(!uploadFile.getOriginalFilename().equals("")) {
			//저장 경로 설정 및 저장, 파일명 변경 작업 수행
			String changeName = saveFile(uploadFile,session);
			
			//Board 객체에 원본파일명과 변경된 파일명을 담아주기 
			b.setOriginName(uploadFile.getOriginalFilename());
			b.setChangeName("/resources/uploadFiles/"+changeName);
			
		}
		
		//서비스에 전달 및 요청
		int result = service.insertBoard(b);
		
		if(result>0) {
			session.setAttribute("alertMsg", "게시글 작성 성공!");
		}else {
			session.setAttribute("alertMsg", "게시글 작성 실패!");
		}

		return "redirect:/list.bo"; //게시글 목록페이지로 재요청
	}
	
	
	
	
	
	
	
	//게시글 삭제
	@RequestMapping("delete.bo")
	public String deleteBoard(int bno
							 ,String filePath
							 ,HttpSession session) {
		
		//삭제 요청 및 처리  (update(status n) or delete) 
		//성공시 게시글 삭제 성공 메시지를 담고 게시글 목록페이지로 재요청 (파일삭제처리해보기)
		//실패시 게시글 삭제 실패 메시지를 담고 기존 상세페이지로 재요청
		
		int result = service.deleteBoard(bno);
		
		
		if(result>0) {
			session.setAttribute("alertMsg", "게시글 삭제 성공");
			
			//만약 파일정보가 있었다면 파일 삭제 시키기
			if(!filePath.equals("")) {//파일경로가 빈문자열이 아니라면(파일이 있다면)
				
				//파일객체로 해당 파일위치 연결하여 삭제 메소드 작성 
				new File(session.getServletContext().getRealPath(filePath)).delete(); 
			}
			
			return "redirect:/list.bo";
			
		}else {
			session.setAttribute("alertMsg", "게시글 삭제 실패");
			return "redirect:/detail.bo?bno="+bno;
		}
	}
	
	//게시글 수정페이지로 이동 메소드
	@GetMapping("update.bo")
	public String boardUpdateForm(int bno
								 ,Model model) {
		
		//글번호로 게시글 정보 조회
		Board b = service.boardDetail(bno);
		
		//위임하는 페이지에 게시글 정보 담아가기 
		model.addAttribute("b",b); 
		
		return "board/boardUpdateForm";
	}
	
	
	
	//게시글 수정 등록작업 메소드 
	@PostMapping("update.bo")
	public String boardUpdate(Board b
							 ,MultipartFile reUploadFile
							 ,HttpSession session) {
		
		//정보수정 성공시 성공메시지와 함께 기존에 첨부파일이 있었다면 삭제 후 디테일뷰로 재요청
		//정보수정 실패시 실패 메시지와 함께 디테일뷰로 재요청
		//새로운 첨부파일이 있는 경우 - 데이터베이스에 변경된 데이터 적용 + 서버에 업로드된 기존 첨부파일 삭제해야함 
		//새로운 첨부파일이 없는 경우 - 데이터베이스에 변경된 데이터 적용 
		//게시글 등록 기능에서 사용했던 첨부파일 처리 참고해서 진행
		
		String deleteFile = null; //삭제해야할 파일명 저장용 변수 
		
		//파일명이 있을때(새로운 업로드파일이 전달되었을때)
		if(!reUploadFile.getOriginalFilename().equals("")) {
			
			//기존에 첨부파일이 있었는지 확인작업
			if(b.getOriginName() != null) {
				deleteFile = b.getChangeName(); //서버에 업로드되어있는 파일명 저장(추후 삭제처리용)
			}
			
			//새로 업로드된 파일 정보를 데이터베이스에 등록 및 서버 업로드 작업 수행
			//파일 업로드 처리 메소드 사용
			String changeName = saveFile(reUploadFile,session);
			
			//업로드 처리 후 변경된 파일명 데이터 베이스에 등록하기 위해서 b에 세팅해주기
			
			b.setOriginName(reUploadFile.getOriginalFilename()); //원본파일명 
			b.setChangeName(changeName);
		}
		
		int result = service.updateBoard(b);
		//정보수정 성공시 성공메시지와 함께 기존에 첨부파일이 있었다면 삭제 후 디테일뷰로 재요청
		if(result>0) {
			session.setAttribute("alertMsg", "게시글 수정 성공!");
			
			//기존 파일 있었으면 삭제처리하기 
			if(deleteFile != null) {//삭제할 파일명이 담겨있다면
				//파일 객체로 기존 파일경로 잡아서 삭제 
				new File(session.getServletContext().getRealPath(deleteFile)).delete();
			}
			
		}else {
			//정보수정 실패시 실패 메시지와 함께 디테일뷰로 재요청
			session.setAttribute("alertMsg", "게시글 수정 실패!");
		}
		
		return "redirect:/detail.bo?bno="+b.getBoardNo();
		
	}
	
	
	@RequestMapping("search.bo")
	public String searchList(@RequestParam(value="currentPage",defaultValue = "1")
							int currentPage
						   ,String condition
						   ,String keyword
						   ,Model model) {
		//검색 목록도 페이징 처리될 수 있도록 
		//페이징 처리에 필요한 데이터 준비 
		HashMap<String,String> map = new HashMap<>();
		
		map.put("condition", condition);
		map.put("keyword", keyword);
		//조건에 맞는 목록에 대한 개수를 세어와야하기 때문에 검색용 조건 데이터 담아서 전달하기 
		int searchCount = service.searchCount(map);
		int boardLimit = 5;
		int pageLimit = 10;
		
		PageInfo pi = Pagination.getPageInfo(searchCount, currentPage, pageLimit, boardLimit);
		
		ArrayList<Board> searchList = service.searchList(map,pi);
		
		model.addAttribute("map", map);
		model.addAttribute("pi", pi);
		model.addAttribute("list", searchList);
		
		return "board/boardListView";
	}
	
	
	
	
	
	
	
	
	
		//파일 업로드 처리 메소드 
		public String saveFile(MultipartFile uploadFile
						      ,HttpSession session) {
			//1.원본 파일명 추출
			String originName = uploadFile.getOriginalFilename();
			
			//2.시간 형식 문자열로 추출 
			String currentTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			
			//3.랜덤값 5자리 추출 
			int ranNum = (int)(Math.random()*90000+10000);
			
			//4.원본파일에서 확장자 추출 
			String ext = originName.substring(originName.lastIndexOf("."));
			
			//5. 합치기
			String changeName = currentTime+ranNum+ext;
			
			//6. 서버에 업로드 처리할때 물리적인 경로 추출하기
			String savePath = session.getServletContext().getRealPath("/resources/uploadFiles/"); 
			
			
			//7.경로와 변경된 이름을 이용하여 파일 업로드 처리 메소드 수행
			//MultipartFile 의 transferTo() 메소드 이용
			try {
				uploadFile.transferTo(new File(savePath+changeName));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return changeName; //서버에 업로드된 파일명 반환
		}
		
		/*
		//댓글 목록 조회
		@RequestMapping("replyList.bo")
		public void replyList(int boardNo,HttpServletResponse response) throws IOException {
			
			ArrayList<Reply> rList = service.replyList(boardNo);
			
			//response 객체 이용하여 반환해보기 
			
			//JSON형태로 변환시키기
			JSONArray jArr = new JSONArray();
			
			for(Reply r : rList) {
				
				JSONObject jObj = new JSONObject();
				jObj.put("replyNo", r.getReplyNo());
				jObj.put("replyContent", r.getReplyContent());
				jObj.put("replyWriter", r.getReplyWriter());
				jObj.put("createDate", r.getCreateDate().toString()); //sql.Date를 String으로 처리
				jArr.add(jObj);
			}
			//reponse객체를 이용하여 출력 
			response.setContentType("application/json; charset=UTF-8");
			//System.out.println(jArr.toJSONString());
			//System.out.println(new Gson().toJson(rList));
			response.getWriter().print(jArr.toJSONString());
		}
		*/
		/*
		//댓글 목록 조회
		@ResponseBody //데이터 자체 리턴 
		@RequestMapping(value="replyList.bo",produces = "application/json;charset=UTF-8")
		public String replyList(int boardNo) {
			
			ArrayList<Reply> rList = service.replyList(boardNo);
			
			//GSON의 toJson 메소드를 이용하여 전달 데이터 넣어주기
			return new Gson().toJson(rList);
		}
		*/
		
		//댓글 목록 조회
		@ResponseBody //데이터 자체 리턴 
		@RequestMapping(value="replyList.bo",produces = "application/json;charset=UTF-8")
		public ArrayList<Reply> replyList(int boardNo) {
			
			ArrayList<Reply> rList = service.replyList(boardNo);
			
			return rList;
		}
		
		//댓글 작성
		@ResponseBody
		@RequestMapping("insertReply.bo")
		public int insertReply(Reply r) {
			
			int result = service.insertReply(r);
			
			return result;
		}
		
		/*
		//게시글 조회수 top5 
		@ResponseBody //값자체 반환
		@RequestMapping(value="topList.bo",produces="application/json;charset=UTF-8")
		public String topList() {
			
			ArrayList<Board> list = service.topList();
			
			
			//return list;
			return new Gson().toJson(list); //json형태의 문자열로 변환
		}
		*/
		//게시글 조회수 top5 
		@ResponseBody //값자체 반환
		@RequestMapping(value="topList.bo",produces="application/json;charset=UTF-8")
		public ArrayList<Board> topList() {
			
			ArrayList<Board> list = service.topList();
			
			
			return list;
		}
		
		
		@RequestMapping("list.ph")
		public String photoList(Model model) {
			
			//사진게시글 목록 조회 후 페이지 이동
			ArrayList<Board> list = service.photoList();
			
			model.addAttribute("list", list);
			
			return "board/photoList";
		}
		
		@GetMapping("insert.ph")
		public String photoEnroll() {
			
			return "board/photoEnroll";
		}
		
		@PostMapping("insert.ph")
		public String insertPhoto(Board b
								,ArrayList<MultipartFile> uploadFiles
								,HttpSession session) {
			//첨부파일이 여러개일땐 배열또는 리스트 형식으로 전달받으면 된다.
			ArrayList<Attachment> atList = new ArrayList<>(); //첨부파일 정보들 등록할 리스트
			
			//반복문을 이용하여 전달받은 첨부파일 정보들 처리하기 
			int count = 1;
			for(MultipartFile m : uploadFiles) {
				String changeName = saveFile(m,session);//업로드 및 파일명 반환받기 
				String originName = m.getOriginalFilename(); //원본 파일명 추출
				
				//파일정보 객체 생성하여 리스트에 추가하기
				Attachment at = new Attachment();
				at.setChangeName(changeName);
				at.setOriginName(originName);
				at.setFilePath("/resources/uploadFiles/");
				if(count==1) {
					at.setFileLevel(count++); //1번 대표사진 설정 
				}else {
					at.setFileLevel(2);//나머지
				}
				
				atList.add(at); //리스트에 추가
			}
			
			//서비스에 요청
			int result = service.insertPhoto(b,atList);
			
			if(result>0) {//등록성공
				session.setAttribute("alertMsg", "등록 성공");
			}else {
				session.setAttribute("alertMsg", "등록 실패");
			}
			
			return "redirect:/list.ph";
		}
		
		
		@RequestMapping("detail.ph")
		public String photoDetail(int bno	
								 ,Model model) {
			
			//조회수증가
			int result = service.increaseCount(bno);
			
			if(result>0) {//조회수 증가 잘 되었다면 
				
				//게시글 정보 조회
				Board b = service.photoDetail(bno);
				
				System.out.println(b);
				
				model.addAttribute("b", b);
				
				return "board/photoDetail";
			}else {
				return "redirect:/list.ph";
			}
			
		}
		
		
		
		
	
}
