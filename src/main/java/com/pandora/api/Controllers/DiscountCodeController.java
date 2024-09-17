package com.pandora.api.Controllers;

import com.pandora.api.entity.DiscountCode;
import com.pandora.api.service.DiscountService;
import com.pandora.api.util.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;

@Controller
@RequiredArgsConstructor
@Slf4j
public class DiscountCodeController {

    private final DiscountService discountService;

    @GetMapping(value = "/admin/discounts")
    public String index(Model model, @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        Page<DiscountCode> list = discountService.list(page);
        model.addAttribute("list", list);
        Pagination pagination = new Pagination(page, list.getTotalPages());
        model.addAttribute("pagination", pagination);
        return "discounts";
    }

    @GetMapping(value = "/admin/discounts/create")
    public String delete(Model model) {
        model.addAttribute("discount", new DiscountCode()
                .setDiscount(10)
                .setMinAccounts(1));
        return "create-discount";
    }

    @GetMapping(value = "/admin/discounts/update/{id}")
    public String delete(Model model, @PathVariable("id") Long id) {
        model.addAttribute("discount", discountService.get(id));
        return "create-discount";
    }

    @PostMapping(value = "/admin/discounts/upload")
    public String upload(@Valid DiscountCode discount) {
        discountService.createOrUpdate(discount);
        return "redirect:/admin/discounts";
    }

    @PostMapping(value = "/admin/discounts/delete")
    public String delete(@RequestParam(value = "id") Long id) {
        discountService.delete(id);
        return "redirect:/admin/discounts";
    }
}
