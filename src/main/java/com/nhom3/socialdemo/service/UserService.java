package com.nhom3.socialdemo.service;

import com.nhom3.socialdemo.model.User;
import com.nhom3.socialdemo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

// [IoC] @Service: Spring tự tạo UserService, trong code không có "new UserService()".
@Service
public class UserService {

    // [DI] Hai phụ thuộc, khai báo final: chỉ gán một lần trong constructor.
    private final UserRepository userRepository;
    // Khai báo kiểu interface PasswordEncoder, không ghi thẳng BCryptPasswordEncoder:
    // UserService không cần biết cách mã hóa cụ thể, đổi cách mã hóa không phải sửa lớp này.
    private final PasswordEncoder passwordEncoder;

    // [DI] Constructor Injection: Spring tự truyền UserRepository và PasswordEncoder vào đây.
    //      PasswordEncoder lấy từ @Bean khai báo trong PasswordConfig.
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(String username, String rawPassword) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Tên đăng nhập đã tồn tại");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole("USER");
        return userRepository.save(user);
    }

    public Optional<User> login(String username, String rawPassword) {
        return userRepository.findByUsername(username)
                .filter(u -> passwordEncoder.matches(rawPassword, u.getPassword()));
    }
}