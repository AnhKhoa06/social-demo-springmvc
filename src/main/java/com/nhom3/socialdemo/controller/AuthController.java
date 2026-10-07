package com.nhom3.socialdemo.controller;

import com.nhom3.socialdemo.model.User;
import com.nhom3.socialdemo.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // ---------- Đăng nhập ----------
    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username,
                          @RequestParam String password,
                          HttpSession session,
                          Model model) {
        Optional<User> user = userService.login(username.trim(), password);
        if (user.isEmpty()) {
            model.addAttribute("error", "Sai tên đăng nhập hoặc mật khẩu");
            return "login";
        }
        session.setAttribute("currentUser", user.get());
        return "redirect:/";
    }

    // ---------- Đăng ký ----------
    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(@RequestParam String username,
                             @RequestParam String password,
                             @RequestParam String confirmPassword,
                             Model model) {
        username = username.trim();
        if (username.length() < 3 || password.length() < 6) {
            model.addAttribute("error", "Tên tối thiểu 3 ký tự, mật khẩu tối thiểu 6 ký tự");
            return "register";
        }
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Mật khẩu nhập lại không khớp");
            return "register";
        }
        try {
            userService.register(username, password);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
        return "redirect:/login";
    }

    // ---------- Đăng xuất ----------
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}