package com.pandora.api.websocket;

import com.google.gson.Gson;
import com.pandora.api.dto.MessageDTO;
import com.pandora.api.exceptions.rest.NotFoundRestException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SignalSession {
    private static Map<String, List<WebSocketSession>> sessions = new HashMap<>();
    public static PendingMessagesManager pendingMessagesManager;

    public static void addSession(WebSocketSession session) {
        String username = (String) session.getAttributes().get("username");
        System.out.println("add session for " + username + " | " + session.getId());
        List<WebSocketSession> list = sessions.get(username);
        if (list == null) {
            list = new LinkedList<>();
        }
        list.add(session);
        sessions.put(username, list);
        sendPendingMessages(username);
    }

    public static void deleteSession(WebSocketSession session) {
        String username = (String) session.getAttributes().get("username");
        List<WebSocketSession> list = sessions.get(username);
        list.removeIf(s -> s.getId().equals(session.getId()));
        sessions.put(username, list);
    }

    public static void sendMessage(String message, String to) {
        List<WebSocketSession> list = sessions.get(to);
        if (list == null || list.isEmpty()) {
            throw new NotFoundRestException("receiver is not online");
        }
        for (WebSocketSession session : list) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Transactional
    private static void sendPendingMessages(String id) {
        List<MessageDTO> messages = pendingMessagesManager.getPendingMessages(id);
        for (MessageDTO message : messages) {
            try {
                String content = message.getBody();
                sendMessage(content, message.getTo());
            } catch (Exception ex) {
//                ex.printStackTrace();
            }
        }
    }
}
