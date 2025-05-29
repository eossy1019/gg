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

        #boardList {text-align:center;}
        #boardList>tbody>tr:hover {cursor:pointer;}

        #pagingArea {width:fit-content; margin:auto;}
        
        #searchForm {
            width:80%;
            margin:auto;
        }
        #searchForm>* {
            float:left;
            margin:5px;
        }
        .select {width:20%;}
        .text {width:53%;}
        .searchBtn {width:20%;}
    </style>
</head>
<body>
    
    <%@ include file="/WEB-INF/views/common/header.jsp" %>

    <div class="content">
        <br><br>
        <div class="innerOuter" style="padding:5% 10%;">
            <h2>게시판</h2>
            <br>
            <!-- 로그인 후 상태일 경우만 보여지는 글쓰기 버튼 -->
            <c:if test="${not empty loginUser }">
	            <a class="btn btn-secondary" style="float:right;" href="${contextRoot}/insert.bo">글쓰기</a>
            </c:if>
            <br>
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
                
                	<c:choose>
                		<c:when test="${empty list }">
                			<tr>
                				<td colspan="6">조회된 게시글이 없습니다.</td>
                			</tr>
                		</c:when>
                		<c:otherwise>
               				<c:forEach var="b" items="${list}">
			                    <tr>
			                        <td>${b.boardNo }</td>
			                        <td>${b.boardTitle }</td>
			                        <td>${b.boardWriter }</td>
			                        <td>${b.count }</td>
			                        <td>${b.createDate }</td>
			                        <td>
			                        	<c:if test="${not empty b.originName }">
			                        		★
			                        	</c:if>
			                        </td>
			                    </tr>
		                	</c:forEach>
                		</c:otherwise>
                	</c:choose>
                
                  
                </tbody>
            </table>
            <br>

			<script>
				//게시글을 클릭했을때 상세보기 할 수 있도록 처리하는 함수 준비 
				//상세보기 요청 매핑주소 : detail.bo
				//메소드명 boardDetail() - SELECT
				//조회수 증가 메소드명 increaseCount() - DML  
				//조회수 증가가 성공이라면 게시글 조회해서 상세페이지로 전달 및 이동 
				//실패시 오류발생 메시지를 담고 에러페이지로 위임처리하기
				$(function(){
					
					$("#boardList>tbody>tr").click(function(){
						//글번호 추출
						var bno = $(this).children().first().text();
						
						//console.log(bno);
						location.href="${contextRoot}/detail.bo?bno="+bno;
					});
					
					//검색 condition 유지
					$("option[value=${map.condition}]").attr("selected",true);
				});
			</script>
			

			<!-- 
				검색시 검색어와 선택상자가 작성한것으로 남아있도록 처리 하기 
				페이징바 클릭했을때 페이징처리된 결과가 보여지도록 요청처리 
			 -->
			<!-- list.bo 또는 search.bo  -->
			<c:url var="url" value="${empty map?'list.bo':'search.bo'}">
				<c:if test="${not empty map }"> <!-- 검색 -->
					<c:param name="condition">${map.condition }</c:param>
					<c:param name="keyword" value="${map.keyword }" />
				</c:if>
				<c:param name="currentPage"></c:param>
			</c:url>
			
			
            <div id="pagingArea">
                <ul class="pagination">
                 	
                 	<c:choose>
                 		<c:when test="${pi.currentPage eq 1 }">
                 			<li class="page-item disabled"><a class="page-link" href="${url }${pi.currentPage-1}">Previous</a></li>
                 		</c:when>
                 		<c:otherwise>
                 			<li class="page-item "><a class="page-link" href="${url }${pi.currentPage-1}">Previous</a></li>
                 		</c:otherwise>
                 	</c:choose>
                 	
                	
                	<c:forEach var="i" begin="${pi.startPage}"  end="${pi.endPage }">
	                    <li class="page-item"><a class="page-link" href="${url}${i}">${i}</a></li>
                	</c:forEach>
                    
                    <c:choose>
                 		<c:when test="${pi.currentPage eq pi.maxPage }">
                 			<li class="page-item disabled"><a class="page-link" href="${url }${pi.currentPage-1}">Next</a></li>
                 		</c:when>
                 		<c:otherwise>
                 			<li class="page-item "><a class="page-link" href="${url }${pi.currentPage+1}">Next</a></li>
                 		</c:otherwise>
                 	</c:choose>
                
                </ul>
            </div>

            <br clear="both"><br>

            <form id="searchForm" action="${contextRoot }/search.bo" method="get" align="center">
                <div class="select">
                    <select class="custom-select" name="condition">
                        <option value="writer">작성자</option>
                        <option value="title">제목</option>
                        <option value="content">내용</option>
                    </select>
                </div>
                <div class="text">
                    <input type="text" class="form-control" name="keyword" value="${map.keyword }">
                </div>
                <button type="submit" class="searchBtn btn btn-secondary">검색</button>
            </form>
            <br><br>
        </div>
        <br><br>

    </div>

    <jsp:include page="/WEB-INF/views/common/footer.jsp" />

</body>
</html>