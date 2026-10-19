package com.nhom3.socialdemo.controller;

import com.nhom3.socialdemo.aspect.RequireLogin;
import com.nhom3.socialdemo.model.Post;
import com.nhom3.socialdemo.model.User;
import com.nhom3.socialdemo.service.FollowService;
import com.nhom3.socialdemo.service.PostService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequireLogin
public class FollowController {

    private final FollowService followService;
    private final PostService postService; // 1. Bổ sung tiêm PostService

    // 2. Tiêm cả 2 Service qua Constructor Injection (chuẩn DI)
    public FollowController(FollowService followService, PostService postService) {
        this.followService = followService;
        this.postService = postService;
    }

    @GetMapping("/feed")
    public String viewFeed(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("posts", followService.getFeedPosts(currentUser.getId()));
        return "feed";
    }

    @GetMapping("/feed/users/{userId}")
    public String viewFeedaUser(@PathVariable("userId") Integer userId, HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");

        // Lấy danh sách bài viết của đúng user có ID truyền trên URL
        List<Post> userPosts = postService.findByUser(userId);

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("posts", userPosts);
        
        return "feed"; // Trả về giao diện feed để hiển thị danh sách bài của user này
    }
    
    @GetMapping("/users")
    public String viewUsers(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("users", followService.getOtherUsers(currentUser.getId()));
        model.addAttribute("followerCounts", followService.getFollowerCounts());
        model.addAttribute("followedUserIds", followService.getFollowedUserIds(currentUser.getId()));
        return "users/list";
    }

    @PostMapping("/users/follow")
    public String followUser(@RequestParam("userId") Integer targetUserId, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");

        followService.followUser(currentUser.getId(), targetUserId);
        return "redirect:/users";
    }

    @PostMapping("/users/unfollow")
    public String unfollowUser(@RequestParam("userId") Integer targetUserId, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");

        followService.unfollowUser(currentUser.getId(), targetUserId);
        return "redirect:/users";
    }
}
