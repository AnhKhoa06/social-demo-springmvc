package com.nhom3.socialdemo.controller;

import com.nhom3.socialdemo.model.User;
import com.nhom3.socialdemo.service.FollowService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FollowController {

    @Autowired
    private FollowService followService;

    @GetMapping("/feed")
    public String viewFeed(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) return "redirect:/login";

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("posts", followService.getFeedPosts(currentUser.getId()));
        return "feed";
    }

    @GetMapping("/users")
    public String viewUsers(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) return "redirect:/login";

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("users", followService.getOtherUsers(currentUser.getId()));
        model.addAttribute("followerCounts", followService.getFollowerCounts());
        model.addAttribute("followedUserIds", followService.getFollowedUserIds(currentUser.getId()));
        return "users/list";
    }

    @PostMapping("/users/follow")
    public String followUser(@RequestParam("userId") Integer targetUserId, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser != null) {
            followService.followUser(currentUser.getId(), targetUserId);
        }
        return "redirect:/users";
    }

    @PostMapping("/users/unfollow")
    public String unfollowUser(@RequestParam("userId") Integer targetUserId, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser != null) {
            followService.unfollowUser(currentUser.getId(), targetUserId);
        }
        return "redirect:/users";
    }
}