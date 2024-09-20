package com.pandora.api.entity;

import com.pandora.api.dto.MessageDTO;
import com.pandora.api.service.StorageService;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "botox_message")
@Accessors(chain = true)
@Data
@NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String toAddress;
    private String body;
    private Date created = new Date();

    public Message(MessageDTO that) {
        this.toAddress = that.getTo();
        this.body = that.getBody();
    }

    public MessageDTO toDTO() {
        MessageDTO dto = new MessageDTO();
        dto.setId(this.id);
        dto.setTo(this.getToAddress());
        dto.setBody(this.body);
        return dto;
    }
}
