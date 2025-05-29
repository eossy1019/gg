<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>그룹 채팅 서버</h1>
	
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
			
			var url = "ws://localhost:8889/spring/group";
			//var url = "ws://192.168.150.55:8889/spring/group";
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
				
				//메시지 전달 데이터 : message.data
				var div = document.querySelector("#chatArea");
				
				//새로운 div만들어서 전달받은 메시지 담아주고 
				var newDiv = document.createElement("div");
				newDiv.textContent = message.data
				
				//텍스트가 담긴 div 결과 div에 추가해주기
				div.appendChild(newDiv);
				
			}
		}
	
		//메시지 전송 함수 
		function send(){
			//사용자가 입력한 텍스트를 추출하여 메시지 전송하기 
			var msg = document.querySelector("#chat");//input요소 추출
			
			socket.send(msg.value);
			
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