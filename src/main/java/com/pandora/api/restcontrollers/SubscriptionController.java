package com.pandora.api.restcontrollers;

import com.pandora.api.dto.RegisterRequestDTO;
import com.pandora.api.dto.RegisterResponseDTO;
import com.pandora.api.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class SubscriptionController {

    private final RegistrationService registrationService;

    @PostMapping("/register")
    public RegisterResponseDTO register(@RequestBody RegisterRequestDTO dto) {
        return registrationService.register(dto);
    }
}
