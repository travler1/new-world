$(function(){
	let message_socket;
	
	connectWebSocket();//웹소켓 생성
	
	/*-----------------------
	 * 웹소켓 연결
     *-----------------------*/
	function connectWebSocket(){
		let socketUrl;
		if (window.location.hostname === 'localhost') {
			socketUrl = "ws://localhost:9090/message-ws"
		}else{
			socketUrl = "wss://www.hello-world.world/message-ws";
		}
		message_socket = new WebSocket(socketUrl);
		//채팅페이지가 열렸을 때 호출되는 함수 지정
		message_socket.onopen=function(evt){
			console.log("채팅페이지 접속 : " + $('#talkDetail').length);
			if($('#talkDetail').length == 1){
				//WebSocket을 통해 클라이언트로부터 서버에 메시지를 전송
				message_socket.send("msg:");
				selectMsg();
			}
		};
		//서버로부터 메시지를 받으면 호출되는 함수 지정
		message_socket.onmessage=function(evt){
			console.log("Message received:", evt.data);
			let data = evt.data;
			if($('#talkDetail').length == 1 && 
			                data.substring(0,4) == 'msg:'){
				selectMsg();
			}
		};
		message_socket.onclose=function(evt){
			//소켓이 종료된 후 부과적인 작업이 있을 경우 명시
			console.log('chat close');
		}
		// 소켓 연결 중에 오류가 발생한 경우 처리
    	message_socket.onerror = function (evt) {
        console.error('WebSocket 오류 발생:', evt);
    	};
	}
	
	/*-----------------------
	 * 채팅하기
     *-----------------------*/
	//채팅 데이터 읽기
	function selectMsg(){
		console.log("selectMsg 호출")
		//서버와 통신
		$.ajax({
			url:'/chat/chatDetailAjax',
			type:'post',
			data:{chatRoom_num:$('#chatRoom_num').val()},
			dataType:'json',
			success:function(param){
				if(param.result == 'logout'){
					alert('로그인 후 사용하세요!');
					message_socket.close();
				}else if(param.result == 'success'){
					//메시지 표시 UI 초기화
					$('#chatting_message').empty();

					let login_user = param.login_user;
					let chat_date = '';
					const days = ['일', '월', '화', '수', '목', '금', '토'];
					$(param.list).each(function(index,item){
						let output = '';
						let category = 'PROFILE_IMAGE';
						// 날짜 추출
						let itemDate = new Date(item.chatRegDate);
						let formattedDate = itemDate.getFullYear() + '-' +
							String(itemDate.getMonth() + 1).padStart(2, '0') + '-' +
							String(itemDate.getDate()).padStart(2, '0') + ' (' +
							days[itemDate.getDay()] + ')';

						if(chat_date != formattedDate){
							chat_date = formattedDate;
							output += '<div class="date-position"><span>' + chat_date + '</span></div>';

						}

						//멤버 등록 메시지
						if(item.memberId == param.login_user){
							output += '<div class="from-position">';
							output += '<div class="space-photo"><div class="align-right">';
							output += item.username+'<img src="/photoView?num='+login_user+'&category='+category+'" width="40" height="40" class="my-photo">';
							output += '</div></div>';
							output += '<div>';
							//멤버등록 메시지가 아닌 일반 메시지
						}else{
							output += '<div class="to-position">';
							output += '<div class="space-photo">';
							output += '<img src="/photoView?num='+item.memberId+'&category='+category+'" width="40" height="40" class="my-photo">';
							output += '</div><div class="space-message">';
							output += item.username;
						}
						output += '<div class="item">';
						output += '<span>' + item.chatMessage.replace(/\r\n/g,'<br>').replace(/\r/g,'<br>').replace(/\n/g,'<br>') + '</span>';
						output += '</div>';

						//채팅방 메세지 읽음처리, 로그인회원 메세지에만 표시, 읽음처리(0)가 아닌 경우만 출력.
						if(item.memberId == param.login_user){
							if(item.chatReadCheck != 0){
								output += '<div class="chat_Readcount">'+item.chatReadCheck+'</div>';
							}
						}

						//시간 추출
						// 주어진 ISO 8601 형식의 문자열
						let dateString = item.chatRegDate;
						// 주어진 문자열을 Date 객체로 변환
						let chatRegDate = new Date(dateString);
						// 현재 시간을 나타내는 Date 객체 생성
						let hours = chatRegDate.getHours();
						let minutes = chatRegDate.getMinutes();
						// 시간이 오후인지 오전인지 확인하여 AM 또는 PM 추가
						let ampm = hours >= 12 ? '오후' : '오전';
						// 12시간 단위로 변환
						hours = hours % 12;
						hours = hours ? hours : 12; // 0시는 12시로 표시
						// 분이 한 자리 수인 경우 앞에 0을 추가하여 두 자리로 표시
						minutes = minutes < 10 ? '0' + minutes : minutes;
						// 현재 시간을 문자열로 출력
						let currentTimeString = ampm + ' ' + hours + ':' + minutes;
						output += '<div class="align-right">'+currentTimeString+'</div>';

						output += '</div><div class="space-clear"></div>';
						output += '</div>';


						//문서 객체에 추가
						$('#chatting_message').append(output);
						//스크롤을 하단에 위치시킴
						$('#chatting_message').scrollTop($('#chatting_message')[0].scrollHeight);
					});
				}else{
					alert('채팅 메시지 읽기 오류 발생');
					message_socket.close();

				}
			},
			error:function(){
				alert('네트워크 오류 발생 chatDetailAjax');
				message_socket.close();
			}
		});
	}
	
	//메시지 입력 후 enter 이벤트 처리
	$('#chat_message').keydown(function(event){
		if(event.keyCode == 13 && !event.shiftKey){
			$('#detail_form').trigger('submit');
		}
	});
	
	//채팅 메시지 등록
	$('#detail_form').submit(function(event){
		if($('#chat_message').val().trim()==''){
			alert('메시지를 입력하세요');
			$('#chat_message').val('').focus();
			return false;
		}
		
		if($('#chat_message').val().length>1333){
			alert('메시지를 1333자까지만 입력 가능합니다.');
			return false;
		}
		
		//form 이하의 태그에 입력한 데이터를 모두 읽어옴
		let form_data = $(this).serialize();
		
		//서버와 통신
		$.ajax({
			url:'/chat/writeChatAjax',
			type:'post',
			data:form_data,
			dataType:'json',
			success:function(param){
				if(param.result == 'logout'){
					alert('로그인해야 작성할 수 있습니다.');
					message_socket.close();
				}else if(param.result == 'success'){
					//폼 초기화
					$('#chat_message').val('').focus();
					//메시지가 저장되었다고 소켓에 신호를 보냄
					message_socket.send('msg:');
				}else{
					alert('채팅 메시지 등록 오류');
					message_socket.close();
				}
			},
			error:function(){
				alert('네트워크 오류 writeChatAjax');
		
			}
		});		
		
		//기본 이벤트 제거
		event.preventDefault();
	});
	
	
	//채팅방 나가기
	$('.chat_delete').click(function() {
		if (confirm("채팅방을 나가시겠습니까? (해당 채팅방에서의 채팅내역이 전부 사라집니다.)")) {
			let chatRoomNum = $('.chatRoomNum').val();
			$.ajax({
				url: '../chat/deleteChatRoom',
				type: 'post',
				data: { chatRoom_num: chatRoomNum },
				dataType: 'json',
				success: function(param) {
					if (param.result == 'logout') {
						alert('로그인해야 작성할 수 있습니다.');
						message_socket.close();
					} else if (param.result == 'success') {
						alert('채팅방을 나갔습니다.');
						window.location.href = "marketChatRoom";
					} else {
						alert('채팅 메시지 등록 오류');
					}
				},
				error: function() {
					alert('네트워크 오류');

				}
			});

		} else {

		}
	});
});