package com.nhom3.socialdemo.service;

import com.nhom3.socialdemo.model.Post;
import com.nhom3.socialdemo.model.User;
import com.nhom3.socialdemo.repository.PostRepository;
import com.nhom3.socialdemo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // Danh sách bài viết của một người dùng
    public List<Post> findByUser(Integer userId) {
        return postRepository.findMyPosts(userId);
    }

    // Tất cả bài viết, mới nhất lên đầu (dành cho ADMIN)
    public List<Post> findAll() {
        return postRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    // Lấy bài viết nếu đúng là của người này, ngược lại trả về rỗng
    public Optional<Post> findOwnedPost(Integer postId, Integer userId) {
        return postRepository.findById(postId)
                .filter(p -> p.getUser().getId().equals(userId));
    }

    // Đăng bài mới
    public void create(Integer userId, String title, String body) {
        User author = userRepository.findById(userId).orElseThrow();
        Post post = new Post();
        post.setTitle(title);
        post.setBody(body);
        post.setUser(author);
        post.setStatus("PUBLISHED");
        postRepository.save(post);
    }

    // Sửa bài, chỉ chủ bài mới sửa được. Trả về false nếu không được phép
    public boolean update(Integer postId, Integer userId, String title, String body) {
        Optional<Post> found = findOwnedPost(postId, userId);
        if (found.isEmpty()) {
            return false;
        }
        Post post = found.get();
        post.setTitle(title);
        post.setBody(body);
        postRepository.save(post);
        return true;
    }

    // Xóa bài: chủ bài hoặc ADMIN. Trả về false nếu không được phép
    public boolean delete(Integer postId, User current) {
        Optional<Post> found = postRepository.findById(postId);
        if (found.isEmpty()) {
            return false;
        }
        Post post = found.get();
        boolean laChuBai = post.getUser().getId().equals(current.getId());
        boolean laAdmin = "ADMIN".equals(current.getRole());
        if (!laChuBai && !laAdmin) {
            return false;
        }
        postRepository.delete(post);
        return true;
    }
}
