package myproject.web.matching.chat;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myproject.LoginAccount;
import myproject.domain.matching.chat.ChatService;
import myproject.domain.member.Member;
import myproject.service.member.MemberService;
import myproject.web.member.MemberDTO.SessionMemberForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

import static myproject.Util.commonResultAlert;

@Controller
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ChatController {

    private final ChatService chatService;
    private final MemberService memberService;

    /*=================================
     * 		 채팅방 조회 및 생성
     *=================================*/

    //채팅창 생성 (채팅방 번호 (생성/조회) , 채팅방 번호 넘겨주기, 채팅방이 존재하면 메세지 읽음처리)
    @GetMapping("matching/chat")
    public String chat(Model model, HttpServletRequest request,
                       RedirectAttributes redirectAttributes,
                       @RequestParam("receiverId") Long receiverId,
                       @ModelAttribute("chatDto") ChatDto chatDto,
                       @LoginAccount Member member) {

        if(member == null) {
            commonResultAlert("로그인한 회원만 이용 가능합니다.", "/loginForm", redirectAttributes, request);
            return "redirect:/common/childResultAlert";
        }

        Long chatRoomId = chatService.findChatRoomByMember(member.getId(), receiverId);

        model.addAttribute("chatRoom_num", chatRoomId);
        model.addAttribute("sender", member.getId());
        model.addAttribute("receiver", receiverId);

        return "template/matching/chat/chatDetail";
    }

    /*=================================
     * 			 채팅메시지 조회
     *=================================*/
    //채팅 메시지 페이지 호출
    @ResponseBody
    @PostMapping("/chat/chatDetailAjax")
    public Map<String, Object> talkDetail(@RequestParam("chatRoom_num") Long id,
                                          @LoginAccount Member member) {
        
        log.info("chatDetailAjax 진입, chatRoom_num={}", id);

        //ajax로 반환결과를 담을 Map
        Map<String, Object> ajaxMap = chatService.chatDetailResult(id, member.getId());
        log.info("ajaxMap={}", ajaxMap);

        ajaxMap.put("result", "success");
        return ajaxMap;
    }

    //채팅 메시지 전송
    @ResponseBody
    @PostMapping("/chat/writeChatAjax")
    public Map<String, Object> writeChatAjax(@ModelAttribute ChatDto chatDto,
                                             @RequestParam("chatRoom_num") Long chatRoom_num,
                                             @LoginAccount Member member) {

        chatService.insertChat(chatDto, chatRoom_num, member.getId());

        Map<String, Object> ajaxMap = new HashMap<>();
        ajaxMap.put("result", "success");

        return ajaxMap;
    }
}
