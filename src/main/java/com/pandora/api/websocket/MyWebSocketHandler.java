package com.pandora.api.websocket;

import com.pandora.api.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Service
public class MyWebSocketHandler extends TextWebSocketHandler {
    @Autowired
    private MessageRepository messageRepository;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        System.out.println("Received: " + payload);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("connection established");
        SignalSession.addSession(session);
        super.afterConnectionEstablished(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("connection closed");
        SignalSession.deleteSession(session);
        super.afterConnectionClosed(session, status);
    }
}
