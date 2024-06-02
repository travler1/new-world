package myproject.domain.matching.chat;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myproject.domain.member.Member;
import myproject.domain.member.MemberService;
import myproject.web.matching.chat.ChatDto;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberService memberService;

    //채팅방 조회, 없을 시 채팅방 생성
    @Override
    public Long findChatRoomByMember(Long senderId, Long receiverId) {

        Member sender = memberService.findMemberById(senderId);
        Member receiver = memberService.findMemberById(receiverId);

        Long chatRoomId = null;

        Long optionalChatRoomId = chatRoomRepository.findChatRoomByMember(senderId, receiverId);

        //채팅방이 존재하지 않을 경우 채팅방 생성
        if(optionalChatRoomId == null){
            ChatRoom saveChatRoom = new ChatRoom(memberService.findMemberById(senderId), memberService.findMemberById(receiverId));
            ChatRoom savedChatRoom = chatRoomRepository.save(saveChatRoom);
            return savedChatRoom.getId();
        }
        //채팅방이 존재할 경우 메세지 읽음처리 후 채팅방 아이디 반환
        //senderId = sessionForm.getId() , 로그인한 회원Id
        //receiverId = 채팅방의 상대방Id, 채팅방 상대방Id가 보낸 chat 엔티티의 chat_readcheck 값 1->0
        chatRepository.updateReadMessage(optionalChatRoomId, receiverId);
        return optionalChatRoomId;
    }


    @Override
    public Map<String, Object> chatDetailResult(Long chatRoomId, Long loginMemberId) {

        Map<String, Object> ajaxMap = new HashMap<>();
        ajaxMap.put("result", "success");
        ajaxMap.put("login_user", loginMemberId);
        ajaxMap.put("chatRoom_num", chatRoomId);

        List<ChatDto> chatDtoListByChatRoomId = chatRepository.findChatListByChatRoomId(chatRoomId);
        ajaxMap.put("list", chatDtoListByChatRoomId);
        return ajaxMap;
    }

    @Override
    public void insertChat(ChatDto chatDto, Long chatRoom_num, Long memberId) {

        ChatRoom findChatRoom = chatRoomRepository.findChatRoomById(chatRoom_num);
        Member findMember = memberService.findMemberById(memberId);

        Chat chat = new Chat().builder()
                .chatReadCheck(1)
                .chatRoom(findChatRoom)
                .member(findMember)
                .chatMessage(chatDto.getChatMessage())
                .chatRegDate(new Date())
                .chatReadCheck(1)
                .build();

        chatRepository.save(chat);
    }
}
