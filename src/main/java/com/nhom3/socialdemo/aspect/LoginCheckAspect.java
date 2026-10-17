package com.nhom3.socialdemo.aspect;

import jakarta.servlet.http.HttpSession;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

// [AOP] Aspect: module gom việc "kiểm tra đăng nhập" vào một chỗ duy nhất,
//       thay vì chép đoạn if (current == null) vào từng method của Controller.
// [IoC] @Component: Spring tự tạo bean này và tự áp dụng nó, không ai gọi new LoginCheckAspect().
@Aspect
@Component
public class LoginCheckAspect {

    // [AOP] Pointcut: chọn nơi cần áp dụng.
    //       @within     -> mọi method của lớp có gắn @RequireLogin (gắn ở cấp lớp, như PostController)
    //       @annotation -> method có gắn @RequireLogin (gắn ở cấp method)
    // [AOP] @Around: advice bao quanh method của Controller, tự quyết định có cho method đó chạy hay không.
    @Around("@within(com.nhom3.socialdemo.aspect.RequireLogin) "
            + "|| @annotation(com.nhom3.socialdemo.aspect.RequireLogin)")
    public Object checkLogin(ProceedingJoinPoint joinPoint) throws Throwable {
        // Lấy HttpSession của request hiện tại (aspect không có tham số session như Controller)
        ServletRequestAttributes attrs =
                (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attrs.getRequest().getSession(false);

        if (session == null || session.getAttribute("currentUser") == null) {
            return "redirect:/login";      // chưa đăng nhập: chuyển về trang đăng nhập, method Controller KHÔNG chạy
        }
        return joinPoint.proceed();         // đã đăng nhập: cho method của Controller chạy tiếp
    }
}