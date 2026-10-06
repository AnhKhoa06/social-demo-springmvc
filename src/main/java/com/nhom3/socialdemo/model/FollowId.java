package com.nhom3.socialdemo.model;

import java.io.Serializable;
import java.util.Objects;

public class FollowId implements Serializable {

    private Integer followingUserId;
    private Integer followedUserId;

    public FollowId() {}
    public FollowId(Integer followingUserId, Integer followedUserId) {
        this.followingUserId = followingUserId;
        this.followedUserId = followedUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FollowId that)) return false;
        return Objects.equals(followingUserId, that.followingUserId)
                && Objects.equals(followedUserId, that.followedUserId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(followingUserId, followedUserId);
    }
}