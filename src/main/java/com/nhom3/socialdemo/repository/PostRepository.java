package com.nhom3.socialdemo.repository;

import com.nhom3.socialdemo.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {

    // Lấy bài của một người, mới nhất lên đầu (nạp luôn User để tránh lỗi lazy)
    @Query("select p from Post p join fetch p.user " +
            "where p.user.id = :userId " +
            "order by p.createdAt desc, p.id desc")
    List<Post> findMyPosts(@Param("userId") Integer userId);
}