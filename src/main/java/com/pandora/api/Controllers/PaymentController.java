package com.pandora.api.Controllers;

import com.pandora.api.dto.SessionDTO;
import com.pandora.api.entity.*;
import com.pandora.api.repository.PaymentRepository;
import com.pandora.api.service.StripeService;
import com.pandora.api.service.SubscriptionCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final StripeService stripeService;
    private final PaymentRepository paymentRepository;
    private final SubscriptionCodeService subscriptionCodeService;

    @GetMapping(value = "/payment")
    public String payment() {
        SessionDTO sessionDTO = stripeService.createPaymentSession(50);
        Payment payment = new Payment();
        payment.setUid(sessionDTO.getId());
        payment.setStatus("pending");
        payment.setSessionId(sessionDTO.getSession().getId());
        paymentRepository.save(payment);
        return "redirect:" + sessionDTO.getSession().getUrl();
    }

    @GetMapping(value = "/payment/{status}/{id}")
    public String payment(@PathVariable("status") String status, @PathVariable("id") String id, Model model) {
        Payment payment = paymentRepository.findFirstByUid(id);
        if (payment == null) {
            model.addAttribute("success", false);
            return "home/payment";
        }
        if (status.equalsIgnoreCase("success")) {
            payment.setStatus("success");
            paymentRepository.save(payment);
            SubscriptionCode code = subscriptionCodeService.create(payment);
            model.addAttribute("code", code.getCode());
        } else {
            payment.setStatus("return");
            paymentRepository.save(payment);
        }
        model.addAttribute("success", status.equals("success"));
        return "home/payment";
    }
}
