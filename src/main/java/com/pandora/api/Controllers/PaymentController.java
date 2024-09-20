package com.pandora.api.Controllers;

import com.pandora.api.client.PandoraClient;
import com.pandora.api.dto.SessionDTO;
import com.pandora.api.entity.*;
import com.pandora.api.repository.AccountRepository;
import com.pandora.api.repository.PaymentRepository;
import com.pandora.api.service.AccountService;
import com.pandora.api.service.AdminService;
import com.pandora.api.service.StripeService;
import com.pandora.api.service.SubscriptionCodeService;
import com.pandora.api.util.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

import static com.pandora.api.entity.User.ROLE_OWNER;

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
