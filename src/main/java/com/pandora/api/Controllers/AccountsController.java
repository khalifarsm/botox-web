package com.pandora.api.Controllers;

import com.pandora.api.entity.Account;
import com.pandora.api.entity.AccountStatus;
import com.pandora.api.entity.User;
import com.pandora.api.repository.AccountRepository;
import com.pandora.api.service.AccountService;
import com.pandora.api.service.AdminService;
import com.pandora.api.util.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

import static com.pandora.api.entity.User.ROLE_OWNER;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AccountsController {

    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final AdminService adminService;

    @GetMapping(value = "/admin/accounts")
    public String index(Model model, @RequestParam(value = "page", required = false, defaultValue = "1") int page
            , @RequestParam(value = "userId", required = false, defaultValue = "") String userId
            , @RequestParam(value = "status", required = false, defaultValue = "") String status) {
        if (userId == null) userId = "";
        AccountStatus accountStatus = AccountStatus.ALL;
        if (status != null && !status.isEmpty()) {
            try {
                accountStatus = AccountStatus.valueOf(status);
            } catch (Exception ex) {
                log.warn(ex.getMessage());
            }
        }
        userId = "%" + userId + "%";
        Page<Account> list = null;
        User auth = adminService.getAuthenticatedUser();
        if (auth.getRole().equals(ROLE_OWNER)) {
            if (accountStatus == AccountStatus.ACTIVE) {
                list = accountRepository.searchActive(userId, new Date(), PageRequest.of(page - 1, 30, Sort.Direction.DESC, "id"));
            } else if (accountStatus == AccountStatus.WIPED) {
                list = accountRepository.searchWiped(userId, PageRequest.of(page - 1, 30, Sort.Direction.DESC, "id"));
                model.addAttribute("list", list);
            } else if (accountStatus == AccountStatus.ALL) {
                list = accountRepository.search(userId, PageRequest.of(page - 1, 30, Sort.Direction.DESC, "id"));
            }
        } else {
            if (accountStatus == AccountStatus.ACTIVE) {
                list = accountRepository.searchActiveByAdminId(userId, auth.getId(), PageRequest.of(page - 1, 30, Sort.Direction.DESC, "id"));
            } else if (accountStatus == AccountStatus.WIPED) {
                list = accountRepository.searchWipedByAdminId(userId, auth.getId(), PageRequest.of(page - 1, 30, Sort.Direction.DESC, "id"));
                model.addAttribute("list", list);
            } else if (accountStatus == AccountStatus.ALL) {
                list = accountRepository.searchByAdminId(userId, auth.getId(), PageRequest.of(page - 1, 30, Sort.Direction.DESC, "id"));
            }
        }
        model.addAttribute("list", list);
        Pagination pagination = new Pagination(page, list.getTotalPages());
        model.addAttribute("pagination", pagination);
        model.addAttribute("userId", userId.replace("%", ""));
        model.addAttribute("status", status);
        return "accounts";
    }

    @PostMapping(value = "/admin/accounts/wipe")
    public String wipe(@RequestParam(value = "id") String pandoraId) {
//        Account account = accountRepository.findFirstByPandoraId(pandoraId)
//                .orElseThrow(() -> new NotFoundRestException("account not found"));
//        User auth = adminService.getAuthenticatedUser();
//        if (auth.getRole().equals(ROLE_ADMIN) && !auth.getId().equals(account.getAdminId())) {
//            throw new UnauthorizedRestException("Unauthorized");
//        }
////        pandoraClient.wipeAccount(account.getPandoraId(), account.getWipeCode());
        return "redirect:/admin/accounts";
    }

    @PostMapping(value = "/admin/accounts/extend")
    public String extend(@RequestParam(value = "id") String pandoraId, @RequestParam(value = "duration") int days) {
        accountService.extend(pandoraId, days);
        return "redirect:/admin/accounts";
    }
}
