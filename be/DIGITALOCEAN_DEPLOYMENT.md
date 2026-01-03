# DigitalOcean Deployment Guide

## 🚀 Hướng dẫn triển khai Backend lên DigitalOcean

### 1. Chuẩn bị Database
Bạn cần có:
- **DigitalOcean Managed Database (PostgreSQL)**
- **Host**: `db-postgresql-xxx-do-user-xxx.ondigitalocean.com`
- **Port**: `25060` (mặc định)
- **Database Name**: `smart_grocery` (hoặc tên bạn chọn)
- **Username**: `doadmin` (mặc định)
- **Password**: Lấy từ DigitalOcean console

### 2. Lấy Database Connection String

Vào [DigitalOcean Console](https://cloud.digitalocean.com/databases):
1. Click vào database của bạn
2. Tab "Connection details"
3. Chọn "Flags: All flags" hoặc "Show URI"
4. Copy connection string có dạng:
```
postgresql://doadmin:PASSWORD@HOST:25060/smart_grocery
```

### 3. Set Environment Variables trên DigitalOcean

Vào [App Platform](https://cloud.digitalocean.com/apps):

1. Select App → **Settings** → **Environment**

2. Add các biến này:

```
SPRING_DATASOURCE_URL=postgresql://doadmin:YOUR_PASSWORD@YOUR_HOST:25060/smart_grocery
SPRING_DATASOURCE_USERNAME=doadmin
SPRING_DATASOURCE_PASSWORD=YOUR_PASSWORD
FIREWORKS_API_KEY=fw_xxxxxxxxxxxxx
JWT_SECRET=your_secret_key_here
PORT=8080
```

**Các giá trị bạn cần thay thế:**
- `YOUR_PASSWORD`: Mật khẩu database từ DigitalOcean
- `YOUR_HOST`: Host database (ví dụ: `db-postgresql-nyc3-do-user-123456-0.ondigitalocean.com`)
- `fw_xxxxxxxxxxxxx`: API key từ Fireworks AI
- `your_secret_key_here`: JWT secret (random string)

### 4. Deploy

Chỉ cần push code lên `main` branch:
```bash
git push origin main
```

DigitalOcean sẽ tự động:
1. Build Docker image
2. Deploy container
3. Start ứng dụng

### 5. Kiểm tra Logs

```bash
# View real-time logs
doctl app logs <app-id>

# Or view in DigitalOcean console
# App → Runtime logs
```

### 6. Troubleshooting

**Database connection refused:**
- Kiểm tra `SPRING_DATASOURCE_URL` format chính xác
- Kiểm tra username, password
- Kiểm tra network access (VPC/firewall)

**Migration failed:**
- Kiểm tra database có migrate scripts không
- Check logs: `doctl app logs <app-id>`

**Schema validation error:**
- Chạy migration lần đầu: `hibernate.ddl-auto: update`
- Sau đó thay thành: `hibernate.ddl-auto: validate`

## 📋 Các Biến Môi Trường Yêu Cầu

| Biến | Mô Tả | Ví Dụ |
|------|-------|-------|
| `SPRING_DATASOURCE_URL` | Connection string PostgreSQL | `postgresql://doadmin:pass@host:25060/db` |
| `SPRING_DATASOURCE_USERNAME` | Database user | `doadmin` |
| `SPRING_DATASOURCE_PASSWORD` | Database password | `your_secure_password` |
| `FIREWORKS_API_KEY` | Fireworks AI API key | `fw_xxxxxxxxxxxxx` |
| `JWT_SECRET` | JWT signing secret | `any_random_string` |
| `PORT` | Server port | `8080` |

## 🔗 Liên kết Hữu Ích

- [DigitalOcean Database](https://cloud.digitalocean.com/databases)
- [DigitalOcean App Platform](https://cloud.digitalocean.com/apps)
- [Fireworks AI API Keys](https://app.fireworks.ai/settings/users/api-keys)
- [doctl CLI Documentation](https://docs.digitalocean.com/reference/doctl/)
