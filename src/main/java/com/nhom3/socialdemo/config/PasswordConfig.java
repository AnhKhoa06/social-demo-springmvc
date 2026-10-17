package com.nhom3.socialdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// [IoC] @Configuration: lớp chứa cấu hình, nơi khai báo các bean bằng @Bean.
@Configuration
public class PasswordConfig {

    // [IoC] @Bean: Spring gọi method này một lần, lấy đối tượng trả về làm bean và quản lý nó.
    //       Dùng @Bean khi lớp không phải do mình viết (BCryptPasswordEncoder của thư viện),
    //       nên không gắn @Component lên lớp đó được.
    // [DI] Nơi nào cần PasswordEncoder (ví dụ constructor của UserService) thì Spring tiêm bean này vào.
    //      Muốn đổi thuật toán mã hóa, chỉ sửa ở đây.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}