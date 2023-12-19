package com.ahdy.chatservice.config;

import com.ahdy.chatservice.models.ChatMessage;
import com.ahdy.chatservice.models.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketEventListenner {
    private final SimpMessageSendingOperations simpMessageSendingOperations;
    @EventListener
    public void handleWebSocketDisconnectListenner(SessionDisconnectEvent sessionDisconnectEvent){
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(sessionDisconnectEvent.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if(username != null){
            log.info("User disconnected: {}",username);
            var chatMessage = ChatMessage.builder().sender(username)
                    .type(MessageType.LEAVE)
                    .build();
            simpMessageSendingOperations.convertAndSend("/topic/public",chatMessage);

        }
    }
}
