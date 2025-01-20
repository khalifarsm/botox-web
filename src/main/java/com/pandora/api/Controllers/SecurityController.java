package com.pandora.api.Controllers;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.pandora.api.entity.User;
import com.pandora.api.repository.AccountRepository;
import com.pandora.api.repository.UserRepository;
import com.pandora.api.service.AccountService;
import com.pandora.api.service.AdminService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jboss.aerogear.security.otp.Totp;
import org.jboss.aerogear.security.otp.api.Base32;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SecurityController {

    private static final String APP_NAME = "PANDORA";
    public static final String QR_PREFIX =
            "/admin/security/barcode";

    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final AdminService adminService;
    private final UserRepository userRepository;

    @GetMapping(value = "/admin/security")
    public String index(Model model) {
        model.addAttribute("user",adminService.getAuthenticatedUser());
        return "security";
    }

    @PostMapping(value = "/admin/security/activation")
    public String activation(String activation, Model model) {
        User user = adminService.getAuthenticatedUser();
        if (activation.equalsIgnoreCase("enable")) {

            //user.setIsUsing2FA(true);
            user.setSecret(Base32.random());
            userRepository.save(user);
            model.addAttribute("url", generateQRUrl());
            return "mfa";
        } else {
            user.setIsUsing2FA(false);
            userRepository.save(user);
        }
        return "redirect:/admin/security";
    }

    @PostMapping(value = "/admin/security/mfa")
    public String mfa(String code, Model model) {
        User user = adminService.getAuthenticatedUser();
        Totp totp = new Totp(user.getSecret());
        if (!totp.verify(code)) {
            return "redirect:/admin/security?error=wrong MFA code";
        }
        user.setIsUsing2FA(true);
        userRepository.save(user);
        return "redirect:/admin/security";
    }

    @SneakyThrows
    public String generateQRUrl() {
        return QR_PREFIX;
    }

    @GetMapping("/admin/security/barcode")
    public void barcode( HttpServletResponse response) throws Exception {
        User user = adminService.getAuthenticatedUser();
        String code = String.format(
                "otpauth://totp/%s:%s?secret=%s&issuer=%s",
                APP_NAME, user.getEmail(), user.getSecret(), APP_NAME);
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = barcodeWriter.encode(code, BarcodeFormat.QR_CODE, 300, 300);
        BufferedImage img = MatrixToImageWriter.toBufferedImage(bitMatrix);
        response.setContentType("image/jpg");
        // Write to output stream
        ImageIO.write(img, "jpg", response.getOutputStream());
    }
}
