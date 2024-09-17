package com.pandora.api.Controllers;

import com.pandora.api.entity.User;
import com.pandora.api.repository.UserRepository;
import com.pandora.api.service.AdminService;
import jakarta.validation.Valid;
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

@Controller
@RequiredArgsConstructor
@Slf4j
public class AdminsController {

    private final UserRepository userRepository;
    private final AdminService adminService;

    @GetMapping(value = "/admin/admins")
    public String index(Model model) {
        Page<User> list = userRepository.findAll(PageRequest.of(0, 1000, Sort.Direction.DESC, "id"));
        model.addAttribute("list", list);
        return "admins";
    }

    @GetMapping(value = "/admin/admins/create")
    public String create(Model model) {
        model.addAttribute("admin", new User());
        return "create-admin";
    }

    @PostMapping(value = "/admin/admins/delete")
    public String delete(@RequestParam("id") Long id) {
        adminService.delete(id);
        return "redirect:/admin/admins";
    }

    @PostMapping(value = "/admin/admins/create")
    public String create(@Valid User admin) {
        adminService.createAdmin(admin.getEmail(), admin.getPassword(), admin.getRole());
        return "redirect:/admin/admins";
    }
}
