package com.nhom3.socialdemo.service;

import com.nhom3.socialdemo.model.Follow;
import com.nhom3.socialdemo.model.Post;
import com.nhom3.socialdemo.model.User;
import com.nhom3.socialdemo.repository.FollowRepository;
import com.nhom3.socialdemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FollowService {

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Post> getFeedPosts(Integer currentUserId) {
        return followRepository.findFeedPostsByUserId(currentUserId);
    }

    public List<User> getOtherUsers(Integer currentUserId) {
        return userRepository.findAll().stream()
                .filter(u -> !u.getId().equals(currentUserId))
                .collect(Collectors.toList());
    }

    public Map<Integer, Long> getFollowerCounts() {
        Map<Integer, Long> counts = new HashMap<>();
        for (User u : userRepository.findAll()) {
            counts.put(u.getId(), followRepository.countByFollowedUserId(u.getId()));
        }
        return counts;
    }

    public List<Integer> getFollowedUserIds(Integer currentUserId) {
        return followRepository.findFollowedUserIdsByUserId(currentUserId);
    }

    @Transactional
    public void followUser(Integer currentUserId, Integer targetUserId) {
        if (!currentUserId.equals(targetUserId) && 
            !followRepository.existsByFollowingUserIdAndFollowedUserId(currentUserId, targetUserId)) {
            
            // Dùng constructor có sẵn của Follow:
            Follow follow = new Follow(currentUserId, targetUserId);
            followRepository.save(follow);
        }
    }

    @Transactional
    public void unfollowUser(Integer currentUserId, Integer targetUserId) {
        followRepository.deleteByFollowingUserIdAndFollowedUserId(currentUserId, targetUserId);
    }
}