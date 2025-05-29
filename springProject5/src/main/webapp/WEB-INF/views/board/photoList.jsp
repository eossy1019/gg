<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>사진 게시판</title>

<!-- jQuery 및 Bootstrap 불러오기 -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

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

    .list-area {
        width:100%;
        margin:auto;
        text-align:center;
    }

    .thumbnail {
        border:1px solid lightgray;
        width:220px;
        display:inline-block;
        margin:14px;
        background:white;
        box-shadow: 0 0 8px rgba(0,0,0,0.1);
        transition: 0.3s ease-in-out;
        padding: 10px;
    }
	.thumbnail p {
	    white-space: nowrap;       /* 줄 바꿈 안 함 */
	    overflow: hidden;          /* 넘친 내용 숨김 */
	    text-overflow: ellipsis;   /* 넘치면 ... 표시 */
	    max-width: 200px;          /* 이미지 너비에 맞게 제한 (또는 부모 요소 기준으로 설정) */
	    margin: auto;
	}

    .thumbnail:hover {
        cursor:pointer;
        opacity:0.85;
        transform: scale(1.02);
    }

    .thumbnail img {
        width:200px;
        height:150px;
        object-fit:cover;
    }
</style>
</head>
<body>

    <%@ include file="/WEB-INF/views/common/header.jsp" %>
    <c:set var="contextPath" value="${pageContext.request.contextPath}" />

    <div class="content">
        <br><br>
        <div class="innerOuter">
            <h2 class="text-center">사진게시판</h2>
            <br>
	
<%--             <c:if test="${not empty loginUser}"> --%>
                <div class="text-right mb-3">
                    <a href="${contextPath}/insert.ph" class="btn btn-secondary">글작성</a>
                </div>
<%--             </c:if> --%>

            <div class="list-area">
                <c:choose>
                    <c:when test="${not empty list}">
                        <c:forEach var="b" items="${list}">
                            <div class="thumbnail" align="center">
                                <input type="hidden" value="${b.boardNo}">
                                <img src="${contextPath}${b.filePath}" alt="thumbnail">
                                <p class="mt-2">
                                    No.${b.boardNo}<br>
                                    <strong>${b.boardTitle}</strong><br>
                                    조회수: ${b.count}
                                </p>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <p class="text-center">등록된 게시글이 없습니다.</p>
                    </c:otherwise>
                </c:choose>
            </div>

            <script>
                $(function(){
                    $(".thumbnail").click(function(){
                        location.href = "${contextPath}/detail.ph?bno=" + $(this).find("input[type=hidden]").val();
                    });
                });
            </script>
        </div>
        <br><br>
    </div>

    <jsp:include page="/WEB-INF/views/common/footer.jsp" />

</body>
</html>
