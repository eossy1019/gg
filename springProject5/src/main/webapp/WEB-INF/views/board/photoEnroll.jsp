<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>사진 게시판 작성</title>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
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

#enrollForm table {
	width: 100%;
}

#enrollForm table * {
	margin: 5px;
}

.preview img {
	width: 150px;
	height: 120px;
	object-fit: cover;
	margin: 5px;
	border: 1px solid #ccc;
}
</style>
</head>
<body>

	<%@ include file="/WEB-INF/views/common/header.jsp"%>

	<div class="content">
		<br> <br>
		<div class="innerOuter">
			<h2>사진 게시글 작성</h2>
			<br>

			<form id="enrollForm" method="post" action="${contextRoot}/insert.ph"
				enctype="multipart/form-data">
				<input type="hidden" name="boardWriter" value="${loginUser.userId}" />
				<table class="table">
					<tr>
						<th><label for="boardTitle">제목</label></th>
						<td><input type="text" id="boardTitle" class="form-control"
							name="boardTitle" required></td>
					</tr>
					<tr>
						<th><label for="boardContent">내용</label></th>
						<td><textarea id="boardContent" class="form-control" rows="5"
								name="boardContent" required></textarea></td>
					</tr>
				</table>

				<div class="form-group">
					<label>대표 이미지 (필수)</label> 
					<button type="button" id="addImg">이미지추가</button>
					<input type="file" id="thumbnail" name="uploadFiles"
						class="form-control-file" accept="image/*" required multiple>
				</div>
				<div id="fileName-area"></div>
				<div class="preview d-flex flex-wrap" id="preview-area"></div>

				<div class="text-center">
					<button type="submit" class="btn btn-primary">등록하기</button>
					<button type="reset" class="btn btn-danger">취소하기</button>
				</div>
			</form>
		</div>
		<br> <br>
	</div>

	<jsp:include page="/WEB-INF/views/common/footer.jsp" />
	
	<script>
		let fileCount = 0; //파일 개수 
		const maxFileSize = 4; //최대 개수 제어
	
		$(function(){
			//input file 태그 상태가 변경되면 동작하는 이벤트 핸들러
			$("#enrollForm").on("change","input[type=file]",function(){
				console.log("헤헤");
				let selectedFiles = this.files; //선택된 파일정보들
				//선택된 개수가 최대 설정 개수보다 크면 
				if(selectedFiles.length > maxFileSize){ //이미등록된 개수 + 방금 등록한 개수
					alert("최대 "+maxFileSize+"개 만큼만 등록 가능합니다.");
					this.value = ""; //선택되어있던거 비워주기
					return;
				}
				
				for(let i=0; i<selectedFiles.length; i++){
					let file = selectedFiles[i];
					let reader = new FileReader();
					
					//FileReader가 파일을 읽어온 시점에 동작하는 이벤트핸들러
					reader.onload = function(e){
						//console.log(e.target.result);
						//임시 url을 만드는 작업(읽어오는 작업)처리 시 해야될 동작 
						//이미지 태그 만들어서 해당 임시 url 속성에 추가하고 
						//미리보기 영역에 추가해서 화면에 출력하기 
						let imgTag = document.createElement("img");//이미지 태그생성
						imgTag.setAttribute("src",e.target.result);//임시 url src속성에 추가
						imgTag.className = "img-thumbnail"; //클래스 추가 
						document.getElementById("preview-area").appendChild(imgTag); //미리보기 영역에 이미지태그 추가
						
					}
					//파일리더로 선택된 파일 읽어서 임시 url 만들기
					reader.readAsDataURL(file);
				}
				
				
				//파일개수에 처리된 개수 추가
				fileCount = selectedFiles.length;
			});
			
			$("#addImg").click(function(){
				let inputFile = $("<input>").attr({"type":"file"
												  ,"name":"uploadFiles"
												  ,"multiple":true
												  ,"accept":"image/*"}).css("display","none");
				
				
				
				$("#enrollForm").append(inputFile);
				
				console.log(inputFile);
				
				inputFile.click();
				
			});
			
			
			
			
			
			
			
			//리셋버튼 눌렀을때 미리보기도 지워주기 
			$("button[type=reset]").on("click",function(){
				fileCount = 0; //선택된 개수 초기화
				//$("#preview-area").html(""); //미리보기 영역 지우기
				$("#preview-area").empty(); //미리보기 영역 지우기
			});
			
			
			
		});
	
	
	</script>
	
	
	
	
	
	