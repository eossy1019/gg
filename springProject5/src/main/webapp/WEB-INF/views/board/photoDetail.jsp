<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>사진 게시글 상세보기</title>

<!-- jQuery 및 Bootstrap 불러오기 -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

<style>
    .content {
        background-color: rgb(247, 245, 245);
        width: 80%;
        margin: auto;
    }
    .innerOuter {
        border: 1px solid lightgray;
        width: 80%;
        margin: auto;
        padding: 5% 10%;
        background-color: white;
    }
    .preview-img {
        width: 250px;
        height: 170px;
        object-fit: cover;
        cursor: pointer;
        border: 1px solid #ccc;
        margin: 5px;
    }
</style>
</head>
<body>

<%@ include file="/WEB-INF/views/common/header.jsp" %>

<div class="content">
    <br><br>
    <div class="innerOuter">
        <h2 class="text-center">사진 게시글 상세보기</h2>
        <br>

        <table class="table table-bordered text-center">
            <tr>
                <th width="150">제목</th>
                <td colspan="3">${b.boardTitle}</td>
            </tr>
            <tr>
                <th>작성자</th>
                <td>${b.boardWriter}</td>
                <th>작성일</th>
                <td>${b.createDate}</td>
            </tr>
            <tr>
                <th>내용</th>
                <td colspan="3">
                    <p style="min-height:50px">${b.boardContent}</p>
                </td>
            </tr>
            <c:if test="${not empty b.atList}">
                <tr>
                    <th>대표사진</th>
                    <td colspan="3">
                        <img class="preview-img" src="${contextRoot}${b.atList[0].filePath}${b.atList[0].changeName}" alt="대표 이미지">
                    </td>
                </tr>
                <!-- atList size가 1보다 큰지 확인  -->
                <c:if test="${fn:length(b.atList) > 1}">
                    <tr>
                        <th>상세사진</th>
                        <td colspan="3">
                           <c:forEach var="at" items="${b.atList}" varStatus="vs">
                             	<c:if test="${!vs.first}">  <!-- vs.first : 첫번째 요소인지 판별 속성 -->
	                                    <img class="preview-img" src="${contextRoot}${at.filePath}${at.changeName}" alt="상세 이미지">
                             	</c:if>
                           </c:forEach>                    
                        </td>
                    </tr>
                </c:if>
            </c:if>
        </table>

        <div class="text-center mt-4">
            <a href="" class="btn btn-primary">수정하기</a>
            <button type="button" class="btn btn-secondary" onclick="history.back();">뒤로가기</button>
        </div>
    </div>
    <br><br>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />

</body>
</html>
