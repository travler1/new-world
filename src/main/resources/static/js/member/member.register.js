$(function(){
	/*--------------------
	*		회원가입
	--------------------*/
	//아이디 중복 여부 저장 변수 : 0은 아이디 중복 또는 중복 체크 미실행, 1은 아이디 미중복
	let checkId=0;
	
	//아이디 중복 체크
	$('#confirmEmail').click(function(){
		if($('#email').val().trim()==''){
			$('#message_email').css('color','red').text('등록하실 이메일을 입력하세요');
			$('#id').val('').focus();
			return; //submit일 땐 return false, 나머지는 return
		}
		
		//서버와 통신
		$.ajax({
			url:'register/confirmEmail',
			type:'post',
			data:{email:$('#email').val()},
			dataType:'json',
			success:function(param){
				$('#email-error').val('');
				if(param.result == 'idNotFound'){
					$('#message_email').css('color','#000').text('등록 가능 이메일입니다.');
					checkId = 1;
				}else if(param.result=='idDuplicated'){
					$('#message_email').css('color','#000').text('중복된 이메일입니다.');
					$('#id').val('').focus();
					checkId = 0;
				}else if(param.result=='notMatchPattern'){
					$('#message_email').css('color','red').text('올바른 이메일 형식이 아닙니다.');
					$('#id').val('').focus();
					checkId= 0;
				}else{
					checkId=0;
					alert('이메일 중복 체크 오류');
				}
			},error:function(){
				checkId=0;
				alert('네트워크 오류 발생');
			}
		});//end of ajax
		
	});//end of click
	
	//아이디 중복 안내 메시지 초기화 및 아이디 중복 값 초기화
	$('#member_register #id').keydown(function(){
		checkId=0;
		$('#message_id').text(''); 
	});//end of keydown
	
	//submit 이벤트 발생시 아이디 중복 체크 여부 확인
	$('#member_register').submit(function(){
		if(checkId==0){
			$('#message_email').css('color','red').text('이메일 중복 체크 필수');
			if($('#email').val().trim()==''){
				$('#email').val('').focus();
			}
			return false;
		}//end of submit
	});
	
	
})