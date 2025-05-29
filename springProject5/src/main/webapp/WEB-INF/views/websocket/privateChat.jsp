<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>개인 채팅 서버</h1>
	
	<h1>로그인 상태 : ${loginUser.userId}</h1>
	
	
	<h3>상대 회원 아이디 : <label id="otherUser">admin</label></h3>
	
	<button onclick="connect();">접속</button>
	<button onclick="disconnect();">종료</button>
	
	
	
	<hr>
	
	<input type="text" id="chat">
	<button onclick="send();">전송</button>
	
	<div id="chatArea"></div>
	
	<script>
		//웹소켓 접속 및 메시지 전달 및 종료 함수들 정의
		
		var socket;  //웹소켓 생성하여 담을 변수 준비
		
		//접속 경로를 담아서 socket 생성
		function connect(){
			
			var url = "ws://localhost:8889/spring/chat";
			//var url = "ws://192.168.150.55:8889/spring/chat";
			//접속 경로를 매개변수로 담아서 socket생성 
			if(!socket){ //소켓이 없을때만 
				socket = new WebSocket(url);
			}
			
			//연결이 되었을때 동작하는 함수정의
			socket.onopen = function(){
				console.log("연결 성공!");
			}
			
			//연결이 끊어졌을때 동작하는 함수정의
			socket.onclose = function(){
				console.log("연결 종료!");
			}
			
			//에러발생했을때 
			socket.onerror = function(e){
				console.log("에러발생");
				console.log(e); //에러 정보 출력
			}
			
			//메세지가 수신됐을때 동작하는 함수 
			socket.onmessage = function(message){
				
				console.log(message.data);
				//전달받은 json 문자열을 json화 시켜주는 파싱작업 처리하기
				var data = JSON.parse(message.data);
				
				console.log(data);
				
				//메시지 전달 데이터 : message.data
				var div = document.querySelector("#chatArea");
				
				//새로운 div만들어서 전달받은 메시지 담아주고 
				/*
				var newDiv = document.createElement("div");
				newDiv.textContent = message.data
				
				//텍스트가 담긴 div 결과 div에 추가해주기
				div.appendChild(newDiv);
				*/
				
				var msg = data.currentTime+"["+data.userId+"] : "+data.msg;
				
				//만들어준 데이터 div영역에 추가
				div.innerText = msg;
			}
		}
	
		//메시지 전송 함수 
		function send(){
			//사용자가 입력한 텍스트를 추출하여 메시지 전송하기 
			var msg = document.querySelector("#chat");//input요소 추출
			
			//상대방의 아이디 추출 
			var otherUser = document.querySelector("#otherUser").innerText;
			
			//전달하고자 하는 데이터를 객체로 만들어서
			//JSON 문자열 변환 메소드를 이용하여 
			//JSON 문자열로 전달하기 
			
			var obj = {
				userId : "${loginUser.userId}",
				otherId : otherUser,
				msg : msg.value
			};
			
			//소켓에 object 그대로 전달하면 toString 처리된 [object Object] 문자열이 전달됨
			//해당 데이터를 json 문자열로 변환하여 서버에서는 해당 문자열을 JSON 객체로 파싱할 수 있도록 
			//전달해준다. 
			//JSON.stringify(객체) - JSON문자열로 변환해주는 함수
			socket.send(JSON.stringify(obj));
			
			//입력창 비우기 
			msg.value = "";
		}
		
		//접속종료 함수
		function disconnect(){
			
			socket.close(); //웹소켓 연결 닫아주기 
			
		}
		
	
	
	</script>
	
	
	
	
	
	
	

</body>
</html>