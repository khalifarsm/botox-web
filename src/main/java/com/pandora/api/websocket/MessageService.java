package com.pandora.api.websocket;

import com.pandora.api.dto.MessageDTO;
import com.pandora.api.entity.Message;
import com.pandora.api.repository.MessageRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    @PostConstruct
    public void setup() {
        SignalSession.pendingMessagesManager = new PendingMessagesManager() {
            @Override
            @Transactional(propagation = Propagation.REQUIRES_NEW)
            public List<MessageDTO> getPendingMessages(String id) {
                return messageRepository.findByToAddress(id)
                        .stream()
                        .map(m -> m.toDTO())
                        .collect(Collectors.toList());
            }

            @Override
            @Transactional(propagation = Propagation.REQUIRES_NEW)
            public void delete(MessageDTO messageDTO) {
                Message message = messageRepository.findById(messageDTO.getId()).orElse(null);
                if (message != null) {
                    messageRepository.delete(message);
                }
            }

            @Override
            public void sendCommand(String content, String to) {
                //no used
            }
        };
    }

    public void sendMessage(MessageDTO message) {
        Message entity = new Message(message);
        messageRepository.save(entity);
        String content = message.getBody();
        try {
            SignalSession.sendMessage(content, message.getTo());
            messageRepository.delete(entity);
        } catch (Exception ex) {
            log.info("failed to send the message " + message.getId() + " to: " + message.getTo());
        }
    }
}
