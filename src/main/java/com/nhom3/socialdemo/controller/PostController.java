package com.nhom3.socialdemo.controller;

import com.nhom3.socialdemo.model.Post;
import com.nhom3.socialdemo.model.User;
import com.nhom3.socialdemo.service.PostService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.nhom3.socialdemo.aspect.RequireLogin;

import java.util.Optional;

// [IoC] @Controller: Spring tự tạo PostController, DispatcherServlet tự gọi các method bên dưới.
// [AOP] @RequireLogin gắn ở cấp lớp: mọi method của controller này đều bị LoginCheckAspect kiểm tra
//       đăng nhập trước. Vì vậy trong từng method không cần viết lại đoạn if (current == null).
@Controller
@RequestMapping("/posts")
@RequireLogin
public class PostController {

    // [DI] Phụ thuộc khai báo final, gán một lần trong constructor.
    private final PostService postService;

    // [DI] Constructor Injection: Spring tự truyền PostService (bean) vào đây, không cần @Autowired.
    public PostController(PostService postService) {
        this.postService = postService;
    }

    // USER: bài của mình. ADMIN: tất cả bài viết
    @GetMapping
    public String list(HttpSession session, Model model) {
        // [AOP] Nhờ @RequireLogin, tới đây chắc chắn đã đăng nhập nên currentUser không null.
        User current = (User) session.getAttribute("currentUser");

        if ("ADMIN".equals(current.getRole())) {
            model.addAttribute("posts", postService.findAll());
            model.addAttribute("pageTitle", "Tất cả bài viết");
        } else {
            model.addAttribute("posts", postService.findByUser(current.getId()));
            model.addAttribute("pageTitle", "Bài viết của tôi");
        }
        return "posts/list";
    }

    // Form đăng bài mới
    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("postId", null);
        model.addAttribute("title", "");
        model.addAttribute("body", "");
        return "posts/form";
    }

    // Xử lý đăng bài mới
    @PostMapping
    public String create(@RequestParam String title,
                         @RequestParam(defaultValue = "") String body,
                         HttpSession session,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        User current = (User) session.getAttribute("currentUser");

        String error = validate(title);
        if (error != null) {
            // Trả lại form kèm lỗi và giữ nguyên dữ liệu đã nhập
            model.addAttribute("postId", null);
            model.addAttribute("title", title);
            model.addAttribute("body", body);
            model.addAttribute("error", error);
            return "posts/form";
        }
        // Gọi Service đã được tiêm vào. Service chạy trong transaction nhờ @Transactional (AOP).
        postService.create(current.getId(), title.trim(), body.trim());
        redirectAttributes.addFlashAttribute("success", "Đăng bài thành công.");
        return "redirect:/posts";
    }

    // Form sửa bài
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Integer id,
                           HttpSession session,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        User current = (User) session.getAttribute("currentUser");

        Optional<Post> found = postService.findOwnedPost(id, current.getId());
        if (found.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Bạn không có quyền sửa bài viết này.");
            return "redirect:/posts";
        }
        Post post = found.get();
        model.addAttribute("postId", post.getId());
        model.addAttribute("title", post.getTitle());
        model.addAttribute("body", post.getBody());
        return "posts/form";
    }

    // Xử lý sửa bài
    @PostMapping("/{id}/edit")
    public String update(@PathVariable Integer id,
                         @RequestParam String title,
                         @RequestParam(defaultValue = "") String body,
                         HttpSession session,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        User current = (User) session.getAttribute("currentUser");

        String error = validate(title);
        if (error != null) {
            model.addAttribute("postId", id);
            model.addAttribute("title", title);
            model.addAttribute("body", body);
            model.addAttribute("error", error);
            return "posts/form";
        }
        if (postService.update(id, current.getId(), title.trim(), body.trim())) {
            redirectAttributes.addFlashAttribute("success", "Cập nhật bài viết thành công.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Bạn không có quyền sửa bài viết này.");
        }
        return "redirect:/posts";
    }

    // Xóa bài
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id,
                         HttpSession session,
                         RedirectAttributes redirectAttributes) {
        User current = (User) session.getAttribute("currentUser");

        if (postService.delete(id, current)) {
            redirectAttributes.addFlashAttribute("success", "Đã xóa bài viết.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Bạn không có quyền xóa bài viết này.");
        }
        return "redirect:/posts";
    }

    // Kiểm tra tiêu đề, trả về nội dung lỗi hoặc null nếu hợp lệ
    private String validate(String title) {
        if (title == null || title.isBlank()) {
            return "Tiêu đề không được để trống.";
        }
        if (title.trim().length() > 255) {
            return "Tiêu đề tối đa 255 ký tự.";
        }
        return null;
    }
}