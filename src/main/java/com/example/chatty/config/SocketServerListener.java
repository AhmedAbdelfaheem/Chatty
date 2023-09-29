package com.example.chatty.config;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class SocketServerListener {

    @EventListener
    public void handelWebsocketDisconnectedListener(SessionDisconnectEvent event){

        // TODO -- implement
    }
}
