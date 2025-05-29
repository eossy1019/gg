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
    </style>
</head>
<body>
    
    <!-- 메뉴바 -->
    <%@ include file="/WEB-INF/views/common/header.jsp" %>

    <div class="content">
        <br><br>
        <div class="innerOuter">
            <h2>회원가입</h2>
            <br>

            <form action="${contextRoot}/insert.me" method="post">
                <div class="form-group">
                    <label for="userId">* ID : </label>
                    <input type="text" class="form-control" id="insertUserId" placeholder="Please Enter ID" name="userId" required> <br>
					<div id="resultDiv" style="font-size:0.8em; display:none"></div> 
					
                    <label for="userPwd">* Password : </label>
                    <input type="password" class="form-control" id="insertUserPwd" placeholder="Please Enter Password" name="userPwd" required> <br>

                    <label for="checkPwd">* Password Check : </label>
                    <input type="password" class="form-control" id="checkPwd" placeholder="Please Enter Password" required> <br>

                    <label for="userName">* Name : </label>
                    <input type="text" class="form-control" id="userName" placeholder="Please Enter Name" name="userName" required> <br>

                    <label for="email"> &nbsp; Email : </label>
                    <input type="text" class="form-control" id="email" placeholder="Please Enter Email" name="email"> <br>

                    <label for="age"> &nbsp; Age : </label>
                    <input type="number" class="form-control" id="age" placeholder="Please Enter Age" name="age"> <br>

                    <label for="phone"> &nbsp; Phone : </label>
                    <input type="tel" class="form-control" id="phone" placeholder="Please Enter Phone (-없이)" name="phone"> <br>
                    
                    <label for="address"> &nbsp; Address : </label>
                    <input type="text" class="form-control" id="address" placeholder="Please Enter Address" name="address"> <br>
                    
                    <label for=""> &nbsp; Gender : </label> &nbsp;&nbsp;
                    <input type="radio" id="Male" value="M" name="gender" checked>
                    <label for="Male">남자</label> &nbsp;&nbsp;
                    <input type="radio" id="Female" value="F" name="gender">
                    <label for="Female">여자</label> &nbsp;&nbsp;
                </div> 
                <br>
                <div class="btns" align="center">
                    <button type="submit" class="btn btn-primary" disabled>회원가입</button>
                    <button type="reset" class="btn btn-danger">초기화</button>
                </div>
            </form>
        </div>
        <br><br>

    </div>
    
    
    <script>
    	$(function(){
    		/*
    			사용자가 입력한 아이디가 중복인지 확인하는 작업 비동기식으로 처리하기
    			아이디입력값이 5글자 이상 부터 사용자가 키를 입력후 떼어질때 이벤트 발생(또는 버튼을 만들어서 해도 됨)
    			중복인지 아닌지 확인하여 네이버에서 봤던 응답데이터 처럼 NNNNN(사용불가) 또는 NNNNY(사용가능)
    			으로 응답 데이터를 받아 응답데이터에 따라 result div에 사용가능한 아이디 입니다. 또는 
    			사용 불가능한 아이디입니다 라는 텍스트를 출력하기.
    			이때 사용 가능한 아이디라면 회원가입 버튼이 활성화 되도록 처리
    			메소드명 : idCheck() 
    			요청 매핑주소 : idCheck.me
    		*/
			
    		//입력 요소 추출
    		var inputId = $("#insertUserId");
    		
    		inputId.keyup(function(){
    			
				//값이 5글자 이상인지 체크 
				
				if(inputId.val().length > 4){
					$.ajax({
						url : "idCheck.me",
						data : {
							userId : inputId.val()
						},
						success : function(result){
							
							if(result=="NNNNN"){ //중복
								$("#resultDiv").show();
								$("#resultDiv").css("color","red").text("사용 불가능한 아이디 입니다.");
								//사용불가능이면 버튼 비활성화
								$(".btns>button[type=submit]").attr("disabled",true);
							}else{//사용가능 
								$("#resultDiv").show();
								$("#resultDiv").css("color","green").text("사용 가능한 아이디 입니다.");
								//사용불가능이면 버튼 비활성화
								$(".btns>button[type=submit]").attr("disabled",false);
							}
						}, 
						error : function(){
							console.log("통신 실패");
						}
					});
				}else{
					//길이 제한 미달이면 안보이게 숨기기
					$("#resultDiv").hide();
					$(".btns>button[type=submit]").attr("disabled",true);
				}
				
				
    		
    		});
    		
    		
    		
    	});
    
    
    </script>
    
    
    
    

    <!-- 푸터바 -->
    <jsp:include page="/WEB-INF/views/common/footer.jsp" />

</body>
</html>