# 🛒 Đi Chợ Tiện Lợi (Smart Grocery) - Backend API

Backend API cho ứng dụng di động đa nền tảng giúp các gia đình quản lý danh sách mua sắm, theo dõi thực phẩm trong tủ lạnh (bao gồm hạn sử dụng), và lập kế hoạch bữa ăn.

## 📋 Mục Lục

- [Tổng Quan](#tổng-quan)
- [Công Nghệ Sử Dụng](#công-nghệ-sử-dụng)
- [Tính Năng](#tính-năng)
- [Cấu Trúc Dự Án](#cấu-trúc-dự-án)
- [Hướng Dẫn Cài Đặt](#hướng-dẫn-cài-đặt)
- [Tài Liệu API](#tài-liệu-api)
- [Sơ Đồ Database](#sơ-đồ-database)
- [Bảo Mật](#bảo-mật)
- [Xử Lý Lỗi](#xử-lý-lỗi)
- [Tác Vụ Nền](#tác-vụ-nền)
- [Danh Sách API](#danh-sách-api)

## 🎯 Tổng Quan

Đi Chợ Tiện Lợi là hệ thống quản lý thực phẩm gia đình toàn diện với các tính năng:
- **Quản lý Gia đình**: Tạo gia đình, mời thành viên bằng mã code, phân quyền
- **Danh sách Mua sắm**: Danh sách mua sắm cộng tác với xử lý xung đột đồng thời
- **Quản lý Tủ lạnh**: Theo dõi thực phẩm trong tủ lạnh và hạn sử dụng
- **Lập kế hoạch Bữa ăn**: Lên kế hoạch bữa ăn trong tuần kết hợp công thức nấu ăn
- **Quản lý Công thức**: Lưu trữ và chia sẻ công thức, gợi ý dựa trên nguyên liệu có sẵn
- **Thông báo**: Cảnh báo hàng ngày về thực phẩm sắp hết hạn

## 🛠 Công Nghệ Sử Dụng

| Thành phần | Công nghệ |
|-----------|------------|
| Ngôn ngữ | Kotlin 1.9.21 |
| Framework | Spring Boot 3.2.1 |
| Cơ sở dữ liệu | PostgreSQL |
| Migration | Flyway |
| Xác thực | JWT (Access + Refresh Token) |
| Tài liệu API | OpenAPI (Swagger) |
| Object Mapping | MapStruct |
| JSON | Jackson (Kotlin Module) |

## ✨ Tính Năng

### 1. Xác Thực & Quản Lý Người Dùng
- ✅ Đăng ký người dùng với xác thực email
- ✅ Xác thực JWT (Access + Refresh token)
- ✅ Mã hóa mật khẩu với BCrypt
- ✅ Phân quyền theo vai trò (ADMIN, USER)
- ✅ Quản lý hồ sơ cá nhân
- ✅ Lưu trữ FCM token cho push notification

### 2. Quản Lý Gia Đình
- ✅ Tạo gia đình với mã mời duy nhất
- ✅ Tham gia gia đình bằng mã mời
- ✅ Phân quyền theo vai trò (TRƯỞNG NHÓM, THÀNH VIÊN)
- ✅ Quản lý thành viên (thêm, xóa, cập nhật vai trò)
- ✅ Tạo lại mã mời
- ✅ Rời gia đình / Xóa gia đình

### 3. Danh Sách Mua Sắm
- ✅ Tạo danh sách mua sắm với các mục
- ✅ **Optimistic Locking** cho chỉnh sửa đồng thời
- ✅ Chọn sản phẩm linh hoạt (sản phẩm có sẵn HOẶC tên tùy chỉnh)
- ✅ Phân công mục cho thành viên gia đình
- ✅ Theo dõi trạng thái đã mua và người mua
- ✅ Thêm nhiều mục cùng lúc
- ✅ Lọc theo trạng thái (ĐANG LẬP, ĐANG MUA, HOÀN THÀNH)

### 4. Quản Lý Tủ Lạnh
- ✅ Thêm thực phẩm với ngày hết hạn
- ✅ Nhiều vị trí lưu trữ (NGĂN ĐÁ, NGĂN MÁT, TỦ ĐỒ KHÔ)
- ✅ Theo dõi trạng thái (TƯƠI, SẮP HẾT HẠN, HẾT HẠN, ĐÃ DÙNG, ĐÃ BỎ)
- ✅ Tiêu thụ một phần số lượng
- ✅ Lọc theo vị trí, trạng thái, hạn sử dụng
- ✅ Bảng thống kê tủ lạnh
- ✅ Tự động cập nhật trạng thái hết hạn

### 5. Công Thức Nấu Ăn
- ✅ Tạo/chỉnh sửa công thức với nguyên liệu
- ✅ Công thức công khai và riêng tư
- ✅ Mức độ khó (DỄ, TRUNG BÌNH, KHÓ)
- ✅ Theo dõi thời gian chuẩn bị và nấu
- ✅ **Gợi ý công thức** dựa trên nguyên liệu trong tủ lạnh
- ✅ Tính toán phần trăm nguyên liệu khớp

### 6. Lập Kế Hoạch Bữa Ăn
- ✅ Kế hoạch bữa ăn **Master-Detail** (Kế hoạch + Món ăn)
- ✅ Ràng buộc duy nhất: một kế hoạch cho mỗi gia đình/ngày/loại bữa
- ✅ Loại bữa ăn: SÁNG, TRƯA, TỐI, PHỤ
- ✅ Xem theo ngày và tuần
- ✅ Liên kết công thức hoặc dùng tên món tùy chỉnh

### 7. Tác Vụ Nền
- ✅ Kiểm tra hết hạn hàng ngày (8 giờ sáng)
- ✅ Cập nhật trạng thái hàng giờ cho thực phẩm hết hạn
- ✅ Push notification mô phỏng (sẵn sàng tích hợp FCM)

### 8. Dữ Liệu Danh Mục (Admin)
- ✅ Quản lý danh mục với icon
- ✅ Danh mục sản phẩm mẫu
- ✅ Quan hệ sản phẩm-danh mục
- ✅ Thông tin thời hạn sử dụng mặc định

## 📁 Cấu Trúc Dự Án

```
src/main/kotlin/com/smartgrocery/
├── SmartGroceryApplication.kt      # Điểm khởi chạy ứng dụng
├── config/                          # Các class cấu hình
│   ├── JpaConfig.kt
│   ├── JwtConfig.kt
│   ├── OpenApiConfig.kt
│   └── SecurityConfig.kt
├── controller/                      # REST Controllers
│   ├── AuthController.kt
│   ├── CategoryController.kt
│   ├── FamilyController.kt
│   ├── FridgeController.kt
│   ├── MealPlanController.kt
│   ├── ProductController.kt
│   ├── RecipeController.kt
│   └── ShoppingListController.kt
├── dto/                             # Data Transfer Objects
│   ├── auth/
│   ├── category/
│   ├── common/
│   ├── family/
│   ├── fridge/
│   ├── mealplan/
│   ├── product/
│   ├── recipe/
│   └── shopping/
├── entity/                          # JPA Entities
│   ├── BaseEntity.kt
│   ├── Category.kt
│   ├── Family.kt
│   ├── FamilyMember.kt
│   ├── FridgeItem.kt
│   ├── MasterProduct.kt
│   ├── MealItem.kt
│   ├── MealPlan.kt
│   ├── Recipe.kt
│   ├── RecipeIngredient.kt
│   ├── Role.kt
│   ├── ShoppingItem.kt
│   ├── ShoppingList.kt
│   └── User.kt
├── exception/                       # Xử lý ngoại lệ
│   ├── Exceptions.kt
│   └── GlobalExceptionHandler.kt
├── repository/                      # JPA Repositories
├── scheduler/                       # Tác vụ nền
│   ├── ExpirationNotificationScheduler.kt
│   └── NotificationService.kt
├── security/                        # Các thành phần bảo mật
│   ├── CustomUserDetails.kt
│   ├── CustomUserDetailsService.kt
│   ├── JwtAuthenticationFilter.kt
│   └── JwtTokenProvider.kt
└── service/                         # Logic nghiệp vụ
    ├── AuthService.kt
    ├── CategoryService.kt
    ├── FamilyService.kt
    ├── FridgeService.kt
    ├── MealPlanService.kt
    ├── ProductService.kt
    ├── RecipeService.kt
    └── ShoppingListService.kt

src/main/resources/
├── application.yml                  # Cấu hình ứng dụng
└── db/migration/
    └── V1__Initial_Schema.sql      # Migration database
```

## 🚀 Hướng Dẫn Cài Đặt

### Yêu Cầu Hệ Thống

- JDK 17+
- PostgreSQL 13+
- Gradle 8+

### Thiết Lập Database

```sql
CREATE DATABASE smart_grocery;
```

### Cấu Hình

Cập nhật file `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/smart_grocery
    username: tên_đăng_nhập
    password: mật_khẩu

jwt:
  secret: khóa-bí-mật-256-bit-ít-nhất-32-ký-tự
```

### Chạy Ứng Dụng

```bash
# Sử dụng Gradle
./gradlew bootRun

# Hoặc build và chạy JAR
./gradlew build
java -jar build/libs/smart-grocery-1.0.0.jar
```

### Truy Cập Swagger UI

Mở http://localhost:8080/swagger-ui.html

## 📚 Tài Liệu API

### Định Dạng Response Chuẩn

Tất cả API trả về JSON theo định dạng thống nhất:

```json
{
  "code": 1000,
  "message": "Thành công",
  "data": { ... }
}
```

### Mã Lỗi

| Mã | Mô tả |
|------|-------------|
| 1000 | Thành công |
| 1001 | Tạo mới thành công |
| 1100 | Yêu cầu không hợp lệ |
| 1101 | Lỗi xác thực dữ liệu |
| 1102 | Chưa xác thực |
| 1103 | Không có quyền |
| 1104 | Không tìm thấy |
| 1105 | Xung đột dữ liệu |
| **1106** | **Lỗi đồng thời (Optimistic Lock)** |
| 1200 | Thông tin đăng nhập không hợp lệ |
| 1300+ | Lỗi liên quan người dùng |
| 1400+ | Lỗi liên quan gia đình |
| 1500+ | Lỗi liên quan danh sách mua sắm |
| 1600+ | Lỗi liên quan tủ lạnh |
| 1700+ | Lỗi liên quan công thức |
| 1800+ | Lỗi liên quan kế hoạch bữa ăn |
| 5000 | Lỗi máy chủ nội bộ |

## 🗄 Sơ Đồ Database

### Quan Hệ Giữa Các Entity

```
User ──< UserRole >── Role

User ──< FamilyMember >── Family
         │
         └── FamilyRole (LEADER, MEMBER)

Family ──< ShoppingList ──< ShoppingItem
                           │
                           └── MasterProduct (tùy chọn)

Family ──< FridgeItem ── MasterProduct (tùy chọn)

Family ──< MealPlan ──< MealItem ── Recipe (tùy chọn)

Recipe ──< RecipeIngredient ── MasterProduct (tùy chọn)

MasterProduct ──< ProductCategory >── Category
```

### Quyết Định Thiết Kế Quan Trọng

1. **Optimistic Locking**: Sử dụng `@Version` trên ShoppingList và ShoppingItem để xử lý chỉnh sửa đồng thời
2. **Lazy Loading**: Tất cả quan hệ sử dụng `FetchType.LAZY` để tối ưu hiệu năng
3. **Soft Delete**: Sản phẩm sử dụng cờ `isActive` thay vì xóa cứng
4. **Sản phẩm Hybrid**: Các mục có thể tham chiếu MasterProduct HOẶC dùng tên tùy chỉnh
5. **Composite Key**: FamilyMember sử dụng khóa kết hợp (familyId, userId)

## 🔐 Bảo Mật

### Luồng Xác Thực

1. **Đăng ký**: `POST /api/v1/auth/register`
2. **Đăng nhập**: `POST /api/v1/auth/login` → Trả về access + refresh token
3. **Gọi API**: Thêm header `Authorization: Bearer <access_token>`
4. **Làm mới token**: `POST /api/v1/auth/refresh` với refresh token

### API Công Khai

- `/api/v1/auth/**` - Xác thực
- `/swagger-ui/**`, `/api-docs/**` - Tài liệu API
- `GET /api/v1/categories/**` - Danh sách danh mục
- `GET /api/v1/master-products/**` - Danh sách sản phẩm
- `GET /api/v1/recipes/**` - Danh sách công thức

### API Yêu Cầu Xác Thực

Tất cả các endpoint khác đều yêu cầu xác thực.

### API Chỉ Dành Cho Admin

- `POST/PUT/DELETE /api/v1/categories/**`
- `POST/PUT/DELETE /api/v1/master-products/**`

## ⚠️ Xử Lý Lỗi

`GlobalExceptionHandler` cung cấp response lỗi thống nhất:

```kotlin
@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(ApiException::class)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ExceptionHandler(ObjectOptimisticLockingFailureException::class)
    // ... các handler khác
}
```

### Ví Dụ Optimistic Locking

Khi cập nhật mục mua sắm:

```json
// Request
PATCH /api/v1/shopping-items/1
{
  "isBought": true,
  "version": 5  // Phiên bản hiện tại
}

// Response lỗi (nếu version không khớp)
{
  "code": 1106,
  "message": "Tài nguyên đã bị chỉnh sửa bởi người dùng khác. Vui lòng tải lại và thử lại."
}
```

## ⏰ Tác Vụ Nền

### Kiểm Tra Hết Hạn Hàng Ngày (8 giờ sáng)

```kotlin
@Scheduled(cron = "0 0 8 * * *")
fun checkExpiringItems() {
    // Tìm thực phẩm hết hạn trong 3 ngày tới
    // Gửi push notification cho thành viên gia đình
    // Cập nhật trạng thái thực phẩm hết hạn
}
```

### Cập Nhật Trạng Thái Hàng Giờ

```kotlin
@Scheduled(cron = "0 0 * * * *")
fun updateExpiredItemsStatus() {
    // Tự động đánh dấu thực phẩm hết hạn
}
```

## 📱 Danh Sách API

### Xác Thực
| Phương thức | Endpoint | Mô tả |
|--------|----------|-------------|
| POST | `/api/v1/auth/register` | Đăng ký người dùng mới |
| POST | `/api/v1/auth/login` | Đăng nhập |
| POST | `/api/v1/auth/refresh` | Làm mới access token |
| GET | `/api/v1/auth/me` | Lấy thông tin người dùng hiện tại |
| PATCH | `/api/v1/auth/me` | Cập nhật hồ sơ |
| POST | `/api/v1/auth/change-password` | Đổi mật khẩu |

### Gia Đình
| Phương thức | Endpoint | Mô tả |
|--------|----------|-------------|
| POST | `/api/v1/families` | Tạo gia đình |
| POST | `/api/v1/families/join` | Tham gia bằng mã mời |
| GET | `/api/v1/families` | Lấy danh sách gia đình của tôi |
| GET | `/api/v1/families/{id}` | Lấy chi tiết gia đình |
| GET | `/api/v1/families/{id}/members` | Lấy danh sách thành viên |
| PUT | `/api/v1/families/{id}` | Cập nhật gia đình |
| PATCH | `/api/v1/families/{familyId}/members/{userId}` | Cập nhật thành viên |
| DELETE | `/api/v1/families/{familyId}/members/{userId}` | Xóa thành viên |
| POST | `/api/v1/families/{id}/leave` | Rời gia đình |
| POST | `/api/v1/families/{id}/regenerate-invite-code` | Tạo mã mời mới |
| DELETE | `/api/v1/families/{id}` | Xóa gia đình |

### Danh Sách Mua Sắm
| Phương thức | Endpoint | Mô tả |
|--------|----------|-------------|
| POST | `/api/v1/shopping-lists` | Tạo danh sách |
| GET | `/api/v1/families/{familyId}/shopping-lists` | Lấy danh sách |
| GET | `/api/v1/families/{familyId}/shopping-lists/active` | Lấy danh sách đang hoạt động |
| GET | `/api/v1/shopping-lists/{id}` | Lấy danh sách với các mục |
| PATCH | `/api/v1/shopping-lists/{id}` | Cập nhật danh sách |
| DELETE | `/api/v1/shopping-lists/{id}` | Xóa danh sách |
| POST | `/api/v1/shopping-lists/{listId}/items` | Thêm mục |
| POST | `/api/v1/shopping-lists/{listId}/items/bulk` | Thêm nhiều mục |
| PATCH | `/api/v1/shopping-items/{itemId}` | Cập nhật mục |
| DELETE | `/api/v1/shopping-items/{itemId}` | Xóa mục |

### Tủ Lạnh
| Phương thức | Endpoint | Mô tả |
|--------|----------|-------------|
| POST | `/api/v1/fridge-items` | Thêm thực phẩm |
| GET | `/api/v1/families/{familyId}/fridge-items` | Lấy thực phẩm (có bộ lọc) |
| GET | `/api/v1/families/{familyId}/fridge-items/active` | Lấy thực phẩm còn dùng được |
| GET | `/api/v1/families/{familyId}/fridge-items/expiring` | Lấy thực phẩm sắp hết hạn |
| GET | `/api/v1/families/{familyId}/fridge-items/expired` | Lấy thực phẩm đã hết hạn |
| GET | `/api/v1/families/{familyId}/fridge-items/statistics` | Lấy thống kê |
| GET | `/api/v1/fridge-items/{id}` | Lấy chi tiết thực phẩm |
| PATCH | `/api/v1/fridge-items/{id}` | Cập nhật thực phẩm |
| POST | `/api/v1/fridge-items/{id}/consume` | Sử dụng một phần |
| POST | `/api/v1/fridge-items/{id}/discard` | Bỏ thực phẩm |
| DELETE | `/api/v1/fridge-items/{id}` | Xóa thực phẩm |

### Công Thức
| Phương thức | Endpoint | Mô tả |
|--------|----------|-------------|
| GET | `/api/v1/recipes` | Lấy tất cả công thức |
| GET | `/api/v1/recipes/{id}` | Lấy công thức với nguyên liệu |
| GET | `/api/v1/recipes/search?title=` | Tìm kiếm công thức |
| GET | `/api/v1/recipes/my-recipes` | Lấy công thức của tôi |
| GET | `/api/v1/recipes/suggestions/{familyId}` | Lấy gợi ý công thức |
| POST | `/api/v1/recipes` | Tạo công thức |
| PUT | `/api/v1/recipes/{id}` | Cập nhật công thức |
| DELETE | `/api/v1/recipes/{id}` | Xóa công thức |

### Kế Hoạch Bữa Ăn
| Phương thức | Endpoint | Mô tả |
|--------|----------|-------------|
| POST | `/api/v1/meal-plans` | Tạo kế hoạch bữa ăn |
| GET | `/api/v1/meal-plans/{id}` | Lấy kế hoạch bữa ăn |
| GET | `/api/v1/families/{familyId}/meal-plans?startDate=&endDate=` | Lấy theo khoảng ngày |
| GET | `/api/v1/families/{familyId}/meal-plans/daily?date=` | Lấy kế hoạch theo ngày |
| GET | `/api/v1/families/{familyId}/meal-plans/weekly?startDate=` | Lấy kế hoạch theo tuần |
| PUT | `/api/v1/meal-plans/{id}` | Cập nhật kế hoạch |
| POST | `/api/v1/meal-plans/{mealPlanId}/items` | Thêm món ăn |
| DELETE | `/api/v1/meal-items/{itemId}` | Xóa món ăn |
| DELETE | `/api/v1/meal-plans/{id}` | Xóa kế hoạch |

### Danh Mục (Admin)
| Phương thức | Endpoint | Mô tả |
|--------|----------|-------------|
| GET | `/api/v1/categories` | Lấy tất cả danh mục |
| GET | `/api/v1/categories/{id}` | Lấy danh mục |
| GET | `/api/v1/categories/search?name=` | Tìm kiếm danh mục |
| POST | `/api/v1/categories` | Tạo danh mục (Admin) |
| PUT | `/api/v1/categories/{id}` | Cập nhật danh mục (Admin) |
| DELETE | `/api/v1/categories/{id}` | Xóa danh mục (Admin) |

### Sản Phẩm (Admin)
| Phương thức | Endpoint | Mô tả |
|--------|----------|-------------|
| GET | `/api/v1/master-products` | Lấy tất cả sản phẩm |
| GET | `/api/v1/master-products/{id}` | Lấy sản phẩm |
| GET | `/api/v1/master-products/search?name=` | Tìm kiếm sản phẩm |
| GET | `/api/v1/master-products/by-category/{categoryId}` | Theo danh mục |
| POST | `/api/v1/master-products` | Tạo sản phẩm (Admin) |
| PUT | `/api/v1/master-products/{id}` | Cập nhật sản phẩm (Admin) |
| DELETE | `/api/v1/master-products/{id}` | Xóa sản phẩm (Admin) |

---

## 📄 Giấy Phép

MIT License

## 👥 Đội Ngũ Phát Triển

Smart Grocery Team
