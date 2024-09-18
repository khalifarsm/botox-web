package com.pandora.api.Controllers;

import com.pandora.api.dto.ResetRequestDTO;
import com.pandora.api.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Controller
@RequiredArgsConstructor
public class ResetController {

    private final NotificationService notificationService;

    @GetMapping(value = "/reset")
    public String reset() {
        return "reset/index";
    }

    @PostMapping("/api/reset")
    @ResponseBody
    public void reset(@RequestBody ResetRequestDTO dto) {
        System.out.println(dto);
        notificationService.send(dto);
    }
}
