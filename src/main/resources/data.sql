INSERT IGNORE INTO users (id, username, password, role) VALUES
(1, 'khoa',  '$2a$10$MtNlo9iC3QwNM14dkobguezlJj3nRq53l7VcgRYNMlzWL12FlOpv.', 'ADMIN'),
(2, 'kiet',  '$2a$10$MtNlo9iC3QwNM14dkobguezlJj3nRq53l7VcgRYNMlzWL12FlOpv.', 'USER'),
(3, 'cuong', '$2a$10$MtNlo9iC3QwNM14dkobguezlJj3nRq53l7VcgRYNMlzWL12FlOpv.', 'USER');

INSERT IGNORE INTO posts (id, title, body, user_id, status) VALUES
(1, 'Xin chào mọi người', 'Đây là bài viết đầu tiên của Khoa.', 1, 'PUBLISHED'),
(2, 'Học Spring MVC', 'Spring MVC chạy rất mượt với Thymeleaf.', 2, 'PUBLISHED'),
(3, 'Hôm nay trời đẹp', 'Chia sẻ một chút cảm xúc cuối tuần.', 3, 'PUBLISHED');

INSERT IGNORE INTO follows (following_user_id, followed_user_id) VALUES
(1, 2),
(1, 3),
(2, 1);