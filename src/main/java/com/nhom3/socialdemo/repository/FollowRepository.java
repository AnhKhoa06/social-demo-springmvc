package com.nhom3.socialdemo.repository;

import com.nhom3.socialdemo.model.Follow;
import com.nhom3.socialdemo.model.FollowId;
import com.nhom3.socialdemo.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, FollowId> {

    boolean existsByFollowingUserIdAndFollowedUserId(Integer followingUserId, Integer followedUserId);

    void deleteByFollowingUserIdAndFollowedUserId(Integer followingUserId, Integer followedUserId);

    long countByFollowedUserId(Integer followedUserId);

    @Query("SELECT f.followedUserId FROM Follow f WHERE f.followingUserId = :userId")
    List<Integer> findFollowedUserIdsByUserId(@Param("userId") Integer userId);

    @Query("SELECT p FROM Post p WHERE p.user.id IN " +
           "(SELECT f.followedUserId FROM Follow f WHERE f.followingUserId = :userId) " +
           "AND p.status = 'PUBLISHED' ORDER BY p.createdAt DESC")
    List<Post> findFeedPostsByUserId(@Param("userId") Integer userId);
}