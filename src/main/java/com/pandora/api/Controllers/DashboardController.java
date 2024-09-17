package com.pandora.api.Controllers;

import com.pandora.api.entity.Account;
import com.pandora.api.entity.AccountStatus;
import com.pandora.api.entity.User;
import com.pandora.api.repository.AccountRepository;
import com.pandora.api.repository.SubscriptionCodeRepository;
import com.pandora.api.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

import static com.pandora.api.entity.User.ROLE_ADMIN;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final AccountRepository accountRepository;
    private final SubscriptionCodeRepository subscriptionCodeRepository;
    private final AdminService adminService;

    @GetMapping(value = "/admin/dashboard")
    public String dashboard(Model model) {
        User auth = adminService.getAuthenticatedUser();
        List<Account> accounts;
        if (auth.getRole().equalsIgnoreCase(ROLE_ADMIN)) {
            accounts = accountRepository.findByAdminId(auth.getId());
            model.addAttribute("codes", subscriptionCodeRepository.countByOwnerId(auth.getId()));
        } else {
            accounts = accountRepository.findAll();
            model.addAttribute("codes", subscriptionCodeRepository.count());
        }
        int active = 0;
        int wiped = 0;
        int expired = 0;
        for (Account account : accounts) {
            AccountStatus status = account.getStatus();
            if (status == AccountStatus.ACTIVE) {
                active++;
                continue;
            }
            if (status == AccountStatus.WIPED) {
                wiped++;
                continue;
            }
        }
        model.addAttribute("wiped", wiped);
        model.addAttribute("active", active);
        model.addAttribute("expired", expired);
        return "dashboard";
    }
}
