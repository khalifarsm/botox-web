package com.pandora.api.Controllers;

import com.pandora.api.entity.SubscriptionCode;
import com.pandora.api.entity.User;
import com.pandora.api.service.AdminService;
import com.pandora.api.service.SubscriptionCodeService;
import com.pandora.api.util.Pagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
public class SubscriptionCodeController {

    private final SubscriptionCodeService subscriptionCodeService;
    private final AdminService adminService;

    @GetMapping(value = "/admin/codes")
    public String index(Model model, @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        User auth = adminService.getAuthenticatedUser();
        Page<SubscriptionCode> list;
        if (auth.getRole().equals(ROLE_OWNER)) {
            list = subscriptionCodeService.list(page);

        } else {
            list = subscriptionCodeService.list(page, auth.getId());
        }
        model.addAttribute("list", list);
        Pagination pagination = new Pagination(page, list.getTotalPages());
        model.addAttribute("pagination", pagination);
        return "codes";
    }

    @GetMapping(value = "/admin/codes/create")
    public String create(Model model) {
        model.addAttribute("code",new SubscriptionCode().setRedemptions(1L));
        return "create-subscription";
    }

    @PostMapping(value = "/admin/codes/save")
    public String save(Model model, @Valid SubscriptionCode code) {
        subscriptionCodeService.create(code,adminService.getAuthenticatedUser().getId());
        return "redirect:/admin/codes";
    }

    @PostMapping(value = "/admin/codes/delete")
    public String delete(@RequestParam("id") Long id) {
        subscriptionCodeService.delete(id);
        return "redirect:/admin/codes";
    }
}
