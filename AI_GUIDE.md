# AI_GUIDE: Quy ước chung của project Social Demo

> File này dành cho AI. Hãy đọc kỹ và **tuân thủ tuyệt đối** khi viết code cho project này. Mục tiêu: code và giao diện của nhiều người phải đồng bộ với nhau.

## 1. Tổng quan project

Demo mạng xã hội đơn giản, bài tập giữa kỳ môn Phát triển phần mềm hướng đối tượng (topic Spring MVC). Giao diện hiển thị bằng **tiếng Việt**.

- Java 17, Spring Boot 4.1.1, Spring MVC, Spring Data JPA, Thymeleaf, MySQL 8, Maven
- Package gốc: `com.nhom3.socialdemo`
- Dùng `jakarta.*` (không dùng `javax.*`)
- **Không** dùng Spring Security (chỉ có `spring-security-crypto` để mã hóa mật khẩu BCrypt)
- **Không** dùng framework JS (React, Vue, jQuery...), không dùng thư viện CSS ngoài (Bootstrap, Tailwind...), không dùng CDN. Chỉ dùng HTML, Thymeleaf và `style.css` có sẵn
- Dùng constructor injection, không dùng `@Autowired` trên field

## 2. Cấu trúc thư mục

```
src/main/java/com/nhom3/socialdemo/
├── config/          PasswordConfig
├── controller/      HomeController, AuthController, (controller mới đặt ở đây)
├── model/           User, Post, Follow, FollowId
├── repository/      UserRepository, (repository mới đặt ở đây)
└── service/         UserService, (service mới đặt ở đây)

src/main/resources/
├── application.properties, schema.sql, data.sql
├── static/css/style.css
└── templates/
    ├── fragments/layout.html
    ├── index.html, login.html, register.html
    ├── posts/       list.html, form.html
    └── users/       list.html     (và feed.html ở thư mục gốc templates)
```

Luồng xử lý: **Controller -> Service -> Repository -> CSDL**. Controller không gọi Repository trực tiếp, phải đi qua Service.

## 3. Cơ sở dữ liệu và Entity có sẵn (không được sửa)

```
users   (id PK, username UNIQUE, password, role, created_at)
posts   (id PK, title, body, user_id FK -> users.id, status, created_at)
follows (following_user_id PK/FK, followed_user_id PK/FK, created_at)
```

- `follows.following_user_id` là người đi theo dõi, `followed_user_id` là người được theo dõi.
- `created_at` do MySQL tự điền (`insertable = false`), ngay sau `save()` giá trị này có thể là `null`.

Entity có sẵn trong `model/`:

```java
User   { Integer id; String username; String password; String role; LocalDateTime createdAt; }
Post   { Integer id; String title; String body; User user (@ManyToOne LAZY); String status; LocalDateTime createdAt; }
Follow { Integer followingUserId; Integer followedUserId; LocalDateTime createdAt; }   // @IdClass(FollowId.class)
         // constructor: new Follow(Integer followingUserId, Integer followedUserId)
```

Có đầy đủ getter/setter (riêng `createdAt` chỉ có getter).

`UserRepository` có sẵn: `findByUsername(String)`, `existsByUsername(String)`, và các hàm của `JpaRepository` như `findAll()`, `findById()`.

## 4. Người đang đăng nhập và bảo vệ trang

Người đăng nhập được lưu trong session với tên `currentUser` (đối tượng `User`). Mọi method controller của trang cần đăng nhập phải mở đầu như sau:

```java
User current = (User) session.getAttribute("currentUser");
if (current == null) {
    return "redirect:/login";
}
```

(`HttpSession session` là tham số của method.)

## 5. Quy ước code

- Controller dùng `@Controller` (trả về tên view), không dùng `@RestController`.
- Form dùng `POST`; sau khi xử lý xong **luôn `redirect:`** (mẫu Post/Redirect/Get), không trả view trực tiếp từ method `POST` (trừ khi trả lại form kèm lỗi).
- Nhận dữ liệu form bằng `@RequestParam`.
- Thông báo sau redirect dùng `RedirectAttributes.addFlashAttribute("success", "...")` hoặc `"error"`.
- Tên: class PascalCase, method và biến camelCase. Comment bằng tiếng Việt, ngắn gọn.
- Ví dụ thông báo trong template:

```html
<div class="alert-success" th:if="${success}" th:text="${success}"></div>
<div class="alert-error" th:if="${error}" th:text="${error}"></div>
```

- Định dạng ngày giờ: `th:text="${#temporals.format(p.createdAt, 'dd/MM/yyyy HH:mm')}"`
- Chữ cái đầu của tên (cho avatar): `th:text="${#strings.toUpperCase(#strings.substring(u.username,0,1))}"`

## 6. Giao diện

### Khung trang bắt buộc

Mọi trang mới **phải** dùng khung này (navbar, sidebar, footer lấy từ `fragments/layout.html`), chỉ thay tên trang và nội dung trong `<main>`:

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head th:replace="~{fragments/layout :: head('Tên trang')}"></head>
<body>
<nav th:replace="~{fragments/layout :: navbar}"></nav>

<div class="container">
    <div class="layout">
        <aside th:replace="~{fragments/layout :: sidebar}"></aside>
        <main class="main">
            <div class="card">
                <div class="card-title">Tiêu đề</div>
                <!-- nội dung riêng của trang -->
            </div>
        </main>
    </div>
</div>

<footer th:replace="~{fragments/layout :: footer}"></footer>
</body>
</html>
```

Trang dạng form nhỏ ở giữa màn hình (như đăng nhập) dùng `<div class="container narrow">` thay cho `container` + `layout`.

### Icon

Icon là SVG đã khai báo sẵn trong `layout.html`. Dùng bằng cú pháp:

```html
<svg class="icon"><use href="#i-trash"/></svg>
```

| Dùng cho | Mã icon |
|---|---|
| Trang chủ | `#i-home` |
| Bảng tin | `#i-newspaper` |
| Bài viết | `#i-pen` |
| Danh sách người dùng | `#i-users` |
| Một người | `#i-user` |
| Theo dõi | `#i-user-plus` |
| Bỏ theo dõi | `#i-user-minus` |
| Thêm mới | `#i-plus` |
| Gửi / Lưu | `#i-send` |
| Sửa | `#i-edit` |
| Xóa | `#i-trash` |
| Đăng nhập / Đăng xuất | `#i-login` / `#i-logout` |
| Mũi tên | `#i-arrow-right` |

**Chỉ dùng các icon trong bảng này.** Không dùng emoji, không thêm thư viện icon. Cần icon khác thì báo cho người dùng nhắn Khoa thêm vào `layout.html`.

### Class CSS có sẵn (chỉ dùng các class này)

| Nhóm | Class |
|---|---|
| Khung | `container`, `container narrow`, `layout`, `main`, `card`, `card-title` |
| Nút | `btn` kèm `btn-primary` / `btn-outline` / `btn-danger` / `btn-ghost`, và `btn-sm` / `btn-lg` / `btn-block` |
| Avatar | `avatar` kèm `avatar-sm` / `avatar-md` / `avatar-lg` |
| Bài viết | `post-header`, `post-author`, `post-time`, `post-title`, `post-body`, `post-actions` |
| Người dùng | `user-row`, `user-info`, `user-name`, `user-meta` |
| Thông báo | `alert-success`, `alert-error`, `error` |
| Khác | `badge`, `table`, `empty-state` |

Form dùng thẻ `<label>`, `<input type="text">`, `<textarea>` (đã được style sẵn). Nút gửi form:

```html
<button class="btn btn-primary" type="submit">
    <svg class="icon"><use href="#i-send"/></svg> Lưu
</button>
```

Danh sách rỗng:

```html
<div class="empty-state" th:if="${#lists.isEmpty(posts)}">Chưa có bài viết nào.</div>
```

Mẫu một bài viết (dùng ở danh sách bài và bảng tin):

```html
<div class="card" th:each="p : ${posts}">
    <div class="post-header">
        <span class="avatar avatar-md" th:text="${#strings.toUpperCase(#strings.substring(p.user.username,0,1))}">K</span>
        <div>
            <div class="post-author" th:text="${p.user.username}">kiet</div>
            <div class="post-time" th:text="${#temporals.format(p.createdAt, 'dd/MM/yyyy HH:mm')}">01/01/2026 10:00</div>
        </div>
    </div>
    <div class="post-title" th:text="${p.title}">Tiêu đề</div>
    <div class="post-body" th:text="${p.body}">Nội dung</div>
</div>
```

Mẫu một dòng người dùng:

```html
<div class="user-row" th:each="u : ${users}">
    <span class="avatar avatar-md" th:text="${#strings.toUpperCase(#strings.substring(u.username,0,1))}">K</span>
    <div class="user-info">
        <div class="user-name" th:text="${u.username}">kiet</div>
        <div class="user-meta">...</div>
    </div>
    <!-- nút hành động -->
</div>
```

## 7. Những việc KHÔNG được làm

1. **Không sửa** các file chung: `layout.html`, `style.css`, `pom.xml`, `application.properties`, `schema.sql`, `data.sql`, và các entity trong `model/`.
2. Không thêm dependency mới vào `pom.xml`.
3. Không tự thêm CSS riêng làm đổi diện mạo chung. Cần style bổ sung thì viết trong thẻ `<style>` ở đầu trang của mình, ngắn gọn.
4. Không đổi tên bảng, tên cột, không tạo bảng mới.
5. Không dùng emoji, CDN, JS framework, thư viện ngoài.

## 8. Cách trả lời mong muốn

- Chỉ viết code cho **phần việc được giao**, không viết lại những phần đã có.
- Với mỗi file: ghi rõ **đường dẫn đầy đủ**, rồi đưa **toàn bộ nội dung file** (không viết tắt, không bỏ dòng "...giữ nguyên").
- Sau khi đưa code, liệt kê ngắn gọn các URL đã làm và cách chạy thử.
- Nếu yêu cầu cần sửa file chung hoặc thêm cột/bảng, **dừng lại và nói rõ** để người dùng báo Khoa, không tự sửa.