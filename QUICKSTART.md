# Quick Start Guide

## Prerequisites Check
- [ ] Java 17+ installed (`java -version`)
- [ ] Maven installed (`mvn -version`)
- [ ] MySQL 8.0+ installed and running
- [ ] Email account (Gmail recommended) for notifications

## Step-by-Step Setup

### 1. Database Setup (5 minutes)

```bash
# Start MySQL
# Windows: Start MySQL service
# Linux/Mac: sudo systemctl start mysql

# Create database
mysql -u root -p < database/schema.sql

# Or manually:
mysql -u root -p
CREATE DATABASE event_registration_db;
USE event_registration_db;
# Then copy-paste contents of database/schema.sql
```

### 2. Configure Application (2 minutes)

Edit `src/main/resources/application.properties`:

```properties
# Update these values:
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password
spring.mail.username=your-email@gmail.com
spring.mail.password=your-gmail-app-password
```

**For Gmail App Password:**
1. Go to Google Account → Security
2. Enable 2-Factor Authentication
3. Generate App Password
4. Use that password (not your regular password)

### 3. Build and Run (2 minutes)

```bash
# Build
mvn clean install

# Run
mvn spring-boot:run
```

Wait for: `Started EventRegistrationSystemApplication`

### 4. Access Application (1 minute)

Open browser: `http://localhost:8080/index.html`

**Default Admin Login:**
- Username: `admin`
- Password: `admin123`

## First Steps

1. **Login as Admin** → Create an event
2. **Register a new user** → Register for the event
3. **Check email** → You should receive QR code ticket
4. **Mark attendance** → Admin → Attendance → Enter ticket number

## Troubleshooting

**Port 8080 in use?**
```properties
# Change in application.properties
server.port=8081
```

**Database connection error?**
- Check MySQL is running
- Verify credentials in application.properties
- Ensure database exists

**Email not sending?**
- Verify Gmail app password (not regular password)
- Check firewall settings
- Test with a simple email first

**CORS errors?**
- Update `cors.allowed-origins` in application.properties
- Ensure frontend URL matches

## Testing the System

1. **Create Event** (as admin):
   - Dashboard → Create Event
   - Fill form → Submit

2. **Register for Event** (as user):
   - Events → Select event → Register Now
   - Check email for QR code

3. **Mark Attendance** (as admin):
   - Admin → Attendance
   - Enter ticket number → Mark Attendance

## Next Steps

- Read full documentation in `README.md`
- Explore API endpoints
- Customize for your needs
- Deploy to production (see README.md production section)

---

**Total Setup Time: ~10 minutes**

