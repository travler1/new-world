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

    @Override
    public Long findChatRoomByMember(Long senderId, Long receiverId) {

        Member sender = memberService.findMemberById(senderId);
        Member receiver = memberService.findMemberById(receiverId);

        Long chatRoomId = null;

        Optional<Long> optionalChatRoomId = chatRoomRepository.findChatRoomByMember(senderId, receiverId);

        if (optionalChatRoomId.isPresent()) {
            return optionalChatRoomId.get();
        }

        ChatRoom saveChatRoom = new ChatRoom(memberService.findMemberById(senderId), memberService.findMemberById(receiverId));

        ChatRoom savedChatRoom = chatRoomRepository.save(saveChatRoom);

        return savedChatRoom.getId();
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
