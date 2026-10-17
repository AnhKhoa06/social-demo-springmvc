package com.nhom3.socialdemo.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// [AOP] Annotation tự định nghĩa, chỉ dùng để "đánh dấu" nơi cần đăng nhập.
//       Bản thân nó không làm gì, LoginCheckAspect mới là nơi xử lý.
// Dùng được trên lớp (TYPE) hoặc trên từng method (METHOD).
@Target({ElementType.TYPE, ElementType.METHOD})
// RUNTIME: giữ annotation lúc chạy để Spring AOP đọc được và áp dụng aspect.
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireLogin {
}