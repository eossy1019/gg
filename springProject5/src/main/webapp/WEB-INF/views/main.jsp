<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
	 .content {
            background-color:rgb(247, 245, 245);
            width:80%;
            margin:auto;
        }
        .innerOuter {
            border:1px solid lightgray;
            width:80%;
            margin:auto;
            padding:5% 10%;
            background-color:white;
        }

        #boardList {text-align:center;}
        #boardList>tbody>tr:hover {cursor:pointer;}

</style>
</head>
<body>
	
	<%@ include file="/WEB-INF/views/common/header.jsp" %>
	
	
	
	<div class="content">
	<br> <br>
	<button onclick="location.href='websocket/basic';">기본 채팅 서버</button>
	<button onclick="location.href='websocket/group';">그룹 채팅 서버</button>
	<button onclick="location.href='websocket/member';">멤버 채팅 서버</button>
	<button onclick="location.href='websocket/chat';">개인 채팅 서버</button>
		<br><br>
		 <div class="innerOuter" style="padding:5% 10%;">
			<h4>게시글 TOP 5</h4>
			<br>
           <table id="boardList" class="table table-hover" align="center">
                <thead>
                    <tr>
                        <th>글번호</th>
                        <th>제목</th>
                        <th>작성자</th>
                        <th>조회수</th>
                        <th>작성일</th>
                        <th>첨부파일</th>
                    </tr>
                </thead>
                <tbody>
                	<!-- 현재 조회수가 가장 높은 상위 5개의 게시글 조회해서 넣어주기(AJAX) -->
                </tbody>
            </table>
          </div>
	</div>
	
	<script>
		//게시글중 조회수가 높은 순으로 상위 5개를 조회하여 목록화하기 
		//매핑주소값 : topList.bo
		//메소드명 :  topList()
		//tbody에 목록화한뒤 해당 글을 클릭했을때 상세보기 화면으로 이동될 수 있도록 추가작업까지 해보기
		
		$(function(){
			selectTopList(); //처음 페이지 띄워질때 호출 
			//지정한 초 마다 반복해서 해당 함수를 호출해주는 구문 
			//setInterval(selectTopList,1000);//1초마다 호출	
			
			//생성된 글 클릭했을때 상세보기페이지로 이동시키기
			//동적으로 추가된 요소는 기본 이벤트 동작 방식으로는 처리 불가 (이벤트 동작안함)
// 			$("#boardList tbody tr").click(function(){
// 				console.log($(this));
// 			});
			
			//동적으로 추가된 요소에도 이벤트를 적용시키고자 한다면
			//상위요소 선택자 -> 하위요소 선택자 이벤트 동작방식을 이용해야한다.
			$("#boardList").on("click","tbody tr",function(){
				//console.log($(this));
				//글번호 추출 
				var bno = $(this).children().first().text();
				
				location.href="detail.bo?bno="+bno;
			});
			
			
		});
		
		
		function selectTopList(){
			$.ajax({
				url : "topList.bo",
				success : function(list){
					
					var str = "";
					
					for(var b of list){
						str +="<tr>"
							+"<td>"+b.boardNo+"</td>"
							+"<td>"+b.boardTitle+"</td>"
							+"<td>"+b.boardWriter+"</td>"
							+"<td>"+b.count+"</td>"
							+"<td>"+b.createDate+"</td>"
							+"<td>";
						if(b.originName!=null){//첨부파일이 존재할경우
							str+="★";
						}
						str+="</td></tr>";
					}
					
					$("#boardList tbody").html(str);
					
				},
				erorr : function(){
					console.log("통신 실패");
				}
			});
		}
	
	</script>
	
	
	
	
	<%@ include file="/WEB-INF/views/common/footer.jsp" %>
	
</body>
</html>