package com.pandora.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pandora.api.dto.MessageDTO;
import com.pandora.api.dto.ResetRequestDTO;
import com.pandora.api.entity.Account;
import com.pandora.api.exceptions.rest.BadRequestRestException;
import com.pandora.api.exceptions.rest.NotFoundRestException;
import com.pandora.api.repository.AccountRepository;
import com.pandora.api.util.DateUtils;
import com.pandora.api.util.SHA256Hash;
import com.pandora.api.websocket.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final MessageService messageService;
    private final AccountRepository accountRepository;

    @SneakyThrows
    public void send(ResetRequestDTO dto) {
        if (dto.getAfter() != null && dto.getAfter() < 0) {
            throw new BadRequestRestException("after should be positive value");
        }
        Account account = accountRepository.findFirstByUserId(dto.getUserId())
                .orElseThrow(() -> new NotFoundRestException("user id or reset code not valid"));
        String hash = SHA256Hash.hash(dto.getReset());
        if (!account.getResetCode().equals(hash)) {
            throw new NotFoundRestException("user id or reset code not valid");
        }
        if (account.isWipe() && account.getAfter().longValue() == dto.getAfter().longValue()) {
            if (account.getResponseDate() != null) {
                throw new BadRequestRestException("The device is wiped on " + account.getResponseDate());
            } else {
                if (new Date().before(account.getWipeDate())) {
                    throw new BadRequestRestException("The wipe command is sent to device on " + account.getSentDate() + ", the device will be wiped on " + account.getWipeDate());
                }
                throw new BadRequestRestException("The wipe command is sent to device on " + account.getSentDate() + ", We have no confirmation of device Reset, probably because it is offline");
            }
        }
        if (!send(account.getUserId(), new ObjectMapper().writeValueAsString(dto))) {
            throw new BadRequestRestException("failed to send reset command");
        }
        account.setWipe(true);
        account.setSentDate(new Date());
        account.setWipeDate(DateUtils.afterSeconds(dto.getAfter().intValue()));
        account.setAfter(dto.getAfter());
        accountRepository.save(account);
    }

    public boolean send(String userId, String messageBody) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setBody(messageBody);
        messageDTO.setTo(userId);
        messageService.sendMessage(messageDTO);
        return true;
    }
}
