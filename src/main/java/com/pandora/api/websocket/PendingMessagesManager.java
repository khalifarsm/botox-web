package com.pandora.api.websocket;


import com.pandora.api.dto.MessageDTO;

import java.util.List;

public interface PendingMessagesManager {

    List<MessageDTO> getPendingMessages(String id);
    void delete(MessageDTO messageDTO);

    void sendCommand(String content,String to);
}
