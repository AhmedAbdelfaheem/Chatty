package com.example.chatty.config;

import com.example.chatty.ChatMessage;
import com.example.chatty.MessageType;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.messaging.simp.user.SimpUser;


@Component
public class SocketServerListener {

    private final SimpUserRegistry simpUserRegistry;

    private final SimpMessageSendingOperations messageTemplate;

    public SocketServerListener(SimpUserRegistry simpUserRegistry, SimpMessageSendingOperations messageTemplate ) {
        this.simpUserRegistry = simpUserRegistry;
        this.messageTemplate = messageTemplate;
    }

    @EventListener
    public void handelWebsocketDisconnectedListener(SessionDisconnectEvent event) {

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        // Check if the user was authenticated
        if (headerAccessor.getUser() != null) {

            // SimpMessageHeaderAccessor.USER_HEADER
            SimpUser disconnectedUser = simpUserRegistry.getUser(headerAccessor.getUser().getName());

            if (disconnectedUser != null) {
                String username = disconnectedUser.getName();

                // Perform any necessary actions when a user disconnects, e.g., remove user from active users list
                System.out.println("User disconnected: " + username);

                ChatMessage chatMessage = ChatMessage.builder()
                        .sender(username)
                        .type(MessageType.LEAVE)
                        .build();

                // Send message to all users in the chat
                messageTemplate.convertAndSend(chatMessage);

            }

        }

    }
}
