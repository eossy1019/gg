<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Document</title>
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

        table * {margin:5px;}
        table {width:100%;}
    </style>
</head>
<body>
        
    <%@ include file="/WEB-INF/views/common/header.jsp" %>

    <div class="content">
        <br><br>
        <div class="innerOuter">
            <h2>게시글 상세보기</h2>
            <br>

            <a class="btn btn-secondary" style="float:right;" href="list.bo">목록으로</a>
            <br><br>

            <table id="contentArea" algin="center" class="table">
                <tr>
                    <th width="100">제목</th>
                    <td colspan="3">${b.boardTitle }</td>
                </tr>
                <tr>
                    <th>작성자</th>
                    <td>${b.boardWriter }</td>
                    <th>작성일</th>
                    <td>${b.createDate }</td>
                </tr>
                <tr>
                    <th>첨부파일</th>
                    <td colspan="3">
                    	<c:choose>
							<c:when test="${empty b.originName }">
								첨부파일이 없습니다.
							</c:when>         
							<c:otherwise>
		                        <a href="${contextRoot }${b.changeName}" download="${b.originName}">${b.originName }</a>
							</c:otherwise>           	
                    	</c:choose>
                    </td>
                </tr>
                <tr>
                    <th>내용</th>
                    <td colspan="3"></td>
                </tr>
                <tr>
                    <td colspan="4"><p style="height:150px;">${b.boardContent }</p></td>
                </tr>
            </table>
            <br>

			<c:if test="${b.boardWriter eq loginUser.userId }">
	            <div align="center">
	                <!-- 수정하기, 삭제하기 버튼은 이 글이 본인이 작성한 글일 경우에만 보여져야 함 -->
	                <a class="btn btn-primary" href="update.bo?bno=${b.boardNo}">수정하기</a>
	                <button class="btn btn-danger" id="deleteBtn">삭제하기</button>
	            </div>
            </c:if>
            
            <script>
            	//삭제하기 버튼을 눌렀을때 삭제처리 post 방식으로 해보기 
            	//매핑주소로 쿼리스트링을 전달하는 get방식의 경우 url 요청만으로 삭제처리를 해버릴 수 있기 때문에
            	//중요작업은 post요청으로 처리하여 쿼리스트링을 이용하지 못하게 해야한다.
            	//함수를 이용하여 submit 처리 해보기
            	
            	$(function(){
            		
            		$("#deleteBtn").click(function(){
            			//form태그를 만들어 전달값을 요소로 추가한 뒤 submit처리를 통해 서버에 전송
            			
            			var check = confirm("정말로 삭제하시겠습니까?");
            			
            			if(check){
            				var form = $("<form>");
            				var inputObj = $("<input>");
            				var filePath = $("<input>");
            				
            				//form태그에 action속성과 method속성을 추가해주기 
            				form.prop("action","delete.bo").prop("method","post");
            				
            				//input 요소로 게시글 번호 전달하기 
            				inputObj.prop("type","hidden").prop("name","bno").prop("value","${b.boardNo}");
            				//서버에 업로드된 파일이 있다면 삭제하기 위해서 파일정보 전달 
            				filePath.prop("type","hidden").prop("name","filePath").prop("value","${b.changeName}");
            				
            				//form에 input 요소들 추가해주기 
            				form.append(inputObj,filePath);
            				
            				//문서에 포함시키기
            				$("body").append(form);
            				
            				//서버에 전송 요청
            				form.submit();
            			}
            		});
            		
            		
            	});
            </script>
            
            <br><br>

            <!-- 댓글 기능은 나중에 ajax 배우고 나서 구현할 예정! 우선은 화면구현만 해놓음 -->
            <table id="replyArea" class="table" align="center">
                <thead>
                	<c:choose>
                		<c:when test="${empty loginUser }">
                			<tr>
		                        <th colspan="2">
		                            <textarea class="form-control" cols="55" rows="2" style="resize:none; width:100%;" readonly>로그인 후 이용가능한 서비스입니다.</textarea>
		                        </th>
		                        <th style="vertical-align:middle"><button class="btn btn-secondary" disabled>등록하기</button></th>
		                    </tr>
                		</c:when>
                		<c:otherwise>
		                    <tr>
		                        <th colspan="2">
		                            <textarea class="form-control" id="content" cols="55" rows="2" style="resize:none; width:100%;"></textarea>
		                        </th>
		                        <th style="vertical-align:middle"><button class="btn btn-secondary">등록하기</button></th>
		                    </tr>
                		</c:otherwise>
                	</c:choose>
                    <tr>
                        <td colspan="3">댓글(<span id="rcount"></span>)</td>
                    </tr>
                </thead>
                <tbody>
                  
                </tbody>
            </table>
        </div>
        <br><br>

    </div>
    
    <script>
    	$(function(){
    		
    		//목록 조회 함수 호출
    		selectList();
    		
    		
    		//댓글 등록 기능 
    		$("#replyArea button").click(function(){
    			
    			$.ajax({
    				url : "insertReply.bo",
    				method : "post",
    				data : {
    					replyContent : $("#content").val(),
    					replyWriter : "${loginUser.userId}",
    					refBno : ${b.boardNo}
    				},
    				success : function(result){
    					//dml 구문이니 성공 실패에 따라 처리 
    					
    					if(result>0){//성공
    						alert("댓글 작성이 되었습니다.");
    						$("#content").val("");
    						//목록 갱신
    						selectList();
    					}else{
    						alert("댓글 작성 실패!");
    					}
    					
    					
    				},
    				error : function(){
    					console.log("통신 오류");
    				}
    				
    				
    			});
    			
    		});
    		
    		
    		
    	});
    	
    	function selectList(){
    		//댓글 목록을 조회하여 replyArea 테이블 tbody에 넣어주기 
    		//비동기 통신을 이용하여 처리 
    		//요청 매핑 : replyList.bo
    		//메소드명 : replyList() 
    		//mapper는 board-mapper 사용 
    		//rcount 위치에는 댓글 개수 추가
    		//페이지가 처음 띄워질때 댓글목록 조회될 수 있도록 처리하기 
    		$.ajax({
    			url : "replyList.bo",
    			data : { /*참조 게시글 번호가 필요하기 때문에 해당 게시글 번호 전달*/
    				boardNo : ${b.boardNo}
    			},
    			success : function(list){
    				
    				var str = "";
    				
    				for(var i=0; i<list.length; i++){
    					str+="<tr>"
    						+"<td>"+list[i].replyWriter+"</td>"
    						+"<td>"+list[i].replyContent+"</td>"
    						+"<td>"+list[i].createDate+"</td>"
    						+"</tr>";
    				}
    				
    				$("#replyArea tbody").html(str);
    				$("#rcount").text(list.length);
    				
    			},
    			error : function(){
    				console.log("통신 실패");
    			}
    			
    		});
    		
    	}
    	
    	
    
    </script>
    
    
    
    
    
    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
    
</body>
</html>