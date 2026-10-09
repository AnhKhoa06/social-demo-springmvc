# Social Demo - Spring MVC (Nhóm 3)

Demo mạng xã hội thu nhỏ cho bài tập giữa kỳ môn Phát triển phần mềm hướng đối tượng, topic **Spring MVC**.

**Công nghệ:** JDK 17, Spring Boot 4.1.1 (Spring MVC + Spring Data JPA), Thymeleaf, MySQL 8, Maven, IntelliJ IDEA.

> Demo xây dựng bằng Spring MVC, chạy trên nền Spring Boot để đơn giản hóa cấu hình.

## 1. Tính năng

| Tính năng | URL | Bảng CSDL |
|---|---|---|
| Đăng ký, đăng nhập, đăng xuất (mật khẩu mã hóa BCrypt, lưu phiên bằng session) | `/register`, `/login`, `/logout` | `users` |
| Bài viết: đăng, xem, sửa, xóa | `/posts`, `/posts/new`, `/posts/{id}/edit`, `/posts/{id}/delete` | `posts`, `users` |
| Danh sách người dùng, theo dõi, bỏ theo dõi | `/users`, `/users/follow`, `/users/unfollow` | `users`, `follows` |
| Bảng tin: bài viết của những người mình theo dõi | `/feed` | `posts`, `follows`, `users` |

## 2. Vai trò và phân quyền

| Vai trò | Quyền |
|---|---|
| Khách | Xem trang chủ, đăng ký, đăng nhập |
| USER | Đăng bài; xem, sửa, xóa bài của mình; theo dõi, bỏ theo dõi; xem bảng tin |
| ADMIN | Như USER, ngoài ra xem **tất cả** bài viết và **xóa** được mọi bài (kiểm duyệt). Không sửa được bài của người khác |

Đăng ký mới luôn là USER.

**Tài khoản mẫu** (mật khẩu đều là `123456`):

| Tài khoản | Vai trò |
|---|---|
| `khoa` | ADMIN |
| `kiet` | USER |
| `cuong` | USER |

## 3. Cài đặt và chạy

**Yêu cầu:** JDK 17, IntelliJ IDEA, Git, MySQL Server 8 và MySQL Workbench. Đặt password của `root` là **`123456`** (nếu máy bạn dùng password khác thì sửa `spring.datasource.password` trong `application.properties` trên máy mình, không commit phần sửa đó).

```bash
git clone https://github.com/AnhKhoa06/social-demo-springmvc.git
```

1. IntelliJ: **File -> Open**, chọn thư mục project (có `pom.xml`), chờ Maven tải thư viện xong.
2. MySQL Workbench: tạo database rỗng (bảng và dữ liệu mẫu sẽ tự tạo khi chạy app):

```sql
CREATE DATABASE IF NOT EXISTS social_demo CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

3. Mở `SocialDemoApplication.java`, bấm **Run**. Thấy dòng `Started SocialDemoApplication` là thành công.
4. Mở http://localhost:8080 và đăng nhập bằng một trong các tài khoản mẫu ở mục 2.

## 4. Cơ sở dữ liệu

Ba bảng theo đề: `users` (có thêm cột `password`), `posts`, `follows`.

| File | Vai trò |
|---|---|
| `src/main/resources/schema.sql` | Script tạo 3 bảng, tự chạy khi khởi động app |
| `src/main/resources/data.sql` | Dữ liệu mẫu (3 tài khoản, 3 bài viết, vài quan hệ theo dõi) |
| `database/backup_social_demo.sql` | File sao lưu toàn bộ CSDL |

**Khôi phục từ file backup (không bắt buộc):** trong MySQL Workbench vào **Server -> Data Import**, chọn **Import from Self-Contained File**, trỏ tới `database/backup_social_demo.sql`, bấm **Start Import**.

**Đưa CSDL về trạng thái ban đầu** khi dữ liệu bị lộn xộn:

```sql
DROP DATABASE social_demo;
CREATE DATABASE social_demo CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Rồi chạy lại app, bảng và dữ liệu mẫu tự tạo lại.

Xem dữ liệu trong Workbench:

```sql
USE social_demo;
SELECT * FROM users;
SELECT * FROM posts;
SELECT * FROM follows;
```

## 5. Cấu trúc project

```
src/main/java/com/nhom3/socialdemo/
├── config/        PasswordConfig (bean mã hóa mật khẩu)
├── controller/    AuthController, PostController, FollowController, HomeController
├── service/       UserService, PostService, FollowService
├── repository/    UserRepository, PostRepository, FollowRepository
├── model/         User, Post, Follow, FollowId
└── SocialDemoApplication.java

src/main/resources/
├── static/css/style.css      CSS dùng chung
├── templates/                Giao diện Thymeleaf (fragments/layout, posts, users, feed, login, register, index)
├── schema.sql, data.sql
└── application.properties
```

Mô hình: **Controller -> Service -> Repository -> MySQL**, kết quả đưa vào **Model** rồi **View** (Thymeleaf) hiển thị.

## 6. Thành viên phụ trách phần Code - demo

| Người | Phần việc |
|---|---|
| Anh Khoa | Khung project, CSDL, đăng ký / đăng nhập / đăng xuất, giao diện chung, phân quyền, tích hợp, backup, tài liệu |
| Gia Kiệt | Chức năng bài viết |
| Long Cường | Chức năng theo dõi và bảng tin |