# Social Demo - Spring MVC (Nhóm 3)

Demo mạng xã hội đơn giản: đăng ký / đăng nhập, bài viết, follow, bảng tin.

**Công nghệ:** JDK 17, Spring Boot 4.1.1 (Spring MVC + JPA), Thymeleaf, MySQL 8, Maven, IntelliJ IDEA.

## 1. Cài đặt (một lần)

- JDK 17, IntelliJ IDEA, Git
- MySQL Server 8 + MySQL Workbench, đặt password của `root` là **`123456`** (cả nhóm dùng chung)
- Chấp nhận lời mời Collaborator trên GitHub từ Khoa

## 2. Kéo code về và chạy

```bash
git clone https://github.com/AnhKhoa06/social-demo-springmvc.git
```

1. IntelliJ: **File -> Open** -> chọn thư mục project (có `pom.xml`), chờ Maven tải xong.
2. MySQL Workbench: tạo database rỗng (bảng và dữ liệu mẫu sẽ tự tạo khi chạy app):

```sql
CREATE DATABASE IF NOT EXISTS social_demo CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

3. Mở `SocialDemoApplication.java`, bấm **Run**. Thấy dòng `Started SocialDemoApplication` là thành công.
4. Vào http://localhost:8080 và đăng nhập thử bằng `khoa`, `kiet` hoặc `cuong`, mật khẩu `123456`.

## 3. Làm việc với Git

**Không push lên `main`.** Mỗi người làm trên nhánh riêng, xong thì tạo Pull Request cho Khoa merge.

```bash
# Bắt đầu (một lần)
git checkout main
git pull origin main
git checkout -b feature/ten-nhanh      # Kiệt: feature/posts, Cường: feature/follow

# Mỗi khi xong một phần nhỏ
git add .
git commit -m "Mô tả ngắn việc vừa làm"
git push -u origin feature/ten-nhanh   # các lần sau chỉ cần: git push
```

**Trước khi tạo Pull Request**, lấy code mới nhất từ `main` về nhánh mình:

```bash
git fetch origin
git merge origin/main
```

Nếu có conflict, IntelliJ hiện hộp thoại **Conflicts** -> bấm **Merge** -> thường giữ cả hai phần -> **Apply**. Không chắc thì hỏi Khoa.

**Tạo Pull Request:** vào GitHub, bấm **Compare & pull request**, chọn `base: main` <- nhánh của bạn, đặt tiêu đề rồi **Create pull request**.

Sau khi được merge: `git checkout main` rồi `git pull origin main`.

**Không sửa** các file chung: `layout.html`, `style.css`, `pom.xml`, `application.properties`, `schema.sql`, `data.sql`. Cần gì nhắn Khoa.

## 4. SQL

Ba bảng: `users`, `posts`, `follows` (cấu trúc xem `src/main/resources/schema.sql`).

Xem dữ liệu trong Workbench:

```sql
USE social_demo;
SELECT * FROM users;
SELECT * FROM posts;
SELECT * FROM follows;
```

Đưa DB về trạng thái ban đầu khi dữ liệu bị lộn xộn:

```sql
DROP DATABASE social_demo;
CREATE DATABASE social_demo CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Rồi chạy lại app, bảng và dữ liệu mẫu tự tạo lại. **Không tự sửa cấu trúc bảng**, cần đổi thì nhắn Khoa. File backup `database/backup_social_demo.sql` do Khoa tạo sau khi merge xong.

## 5. Nhiệm vụ

| Người | Phần việc | Nhánh | Trang cần làm |
|---|---|---|---|
| Gia Kiệt | **Bài viết** (bảng `posts`): đăng bài, xem danh sách bài của mình, sửa, xóa (chỉ chủ bài mới sửa/xóa được) | `feature/posts` | `posts/list.html`, `posts/form.html` |
| Long Cường | **Follow và Bảng tin** (bảng `follows`): danh sách người dùng + Theo dõi / Bỏ theo dõi (không tự follow mình, không follow trùng), bảng tin `/feed` hiện bài của người mình theo dõi | `feature/follow` | `users/list.html`, `feed.html` |
| Khoa | Khung project, CSDL, đăng ký / đăng nhập, giao diện chung, merge, backup, tài liệu | `main` | |

Làm xong tạo Pull Request vào `main` (xem mục 3). Làm xong gửi Khoa: **ảnh chụp màn hình** từng chức năng, **2 đến 3 đoạn code tiêu biểu** kèm giải thích ngắn, và **các lỗi đã gặp** để làm Word/PPT.

## 6. Nhờ AI viết code (đồng bộ giao diện)

File **`AI_GUIDE.md`** trong project chứa quy ước chung (khung trang, icon, class CSS, cách lấy người đăng nhập...). Khi nhờ AI viết code, **đính kèm file này** rồi dán câu lệnh của mình:

**Kiệt:**

```
Mình đính kèm file AI_GUIDE.md là quy ước chung của project Spring MVC. Hãy đọc kỹ và làm đúng theo đó.
Nhiệm vụ của mình: chức năng BÀI VIẾT (bảng posts) gồm: đăng bài mới, xem danh sách bài của người đang đăng nhập, sửa bài, xóa bài (chỉ chủ bài mới được sửa và xóa, có hộp xác nhận khi xóa, có thông báo thành công/lỗi). Cần tạo: PostRepository, PostService, PostController, templates/posts/list.html, templates/posts/form.html.
Chỉ viết phần của mình, không sửa file chung. Cho mình đường dẫn và toàn bộ code từng file.
```

**Cường:**

```
Mình đính kèm file AI_GUIDE.md là quy ước chung của project Spring MVC. Hãy đọc kỹ và làm đúng theo đó.
Nhiệm vụ của mình: chức năng FOLLOW và BẢNG TIN (bảng follows) gồm: trang /users liệt kê người dùng khác (không gồm bản thân) kèm số người theo dõi và nút Theo dõi / Bỏ theo dõi (không tự theo dõi mình, không theo dõi trùng); trang /feed hiện các bài viết (bảng posts) của những người mình đang theo dõi, mới nhất lên đầu, trống thì hiện thông báo. Cần tạo: FollowRepository (kể cả query bảng tin, đặt trong FollowRepository bằng @Query), FollowService, FollowController, templates/users/list.html, templates/feed.html.
Chỉ viết phần của mình, không sửa file chung. Cho mình đường dẫn và toàn bộ code từng file.
```

Sau khi AI đưa code: tạo đúng file theo đường dẫn AI ghi, chạy thử, có lỗi thì dán nguyên thông báo lỗi lại cho AI.
