package com.nhom3.socialdemo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "follows")
@IdClass(FollowId.class)
public class Follow {

    @Id
    @Column(name = "following_user_id")
    private Integer followingUserId;

    @Id
    @Column(name = "followed_user_id")
    private Integer followedUserId;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    public Follow() {}
    public Follow(Integer followingUserId, Integer followedUserId) {
        this.followingUserId = followingUserId;
        this.followedUserId = followedUserId;
    }

    public Integer getFollowingUserId() { return followingUserId; }
    public void setFollowingUserId(Integer v) { this.followingUserId = v; }
    public Integer getFollowedUserId() { return followedUserId; }
    public void setFollowedUserId(Integer v) { this.followedUserId = v; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}