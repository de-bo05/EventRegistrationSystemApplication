# Event Registration System

A full-stack event registration system built with Java Spring Boot (backend) and HTML/CSS/JavaScript with Bootstrap (frontend). This system allows users to register for events, receive QR code tickets, and enables administrators to manage events and mark attendance.

## Features

### User Features
- **User Registration & Login**: Secure authentication with JWT tokens
- **Event Browsing**: View all available and upcoming events
- **Event Registration**: Register for events with capacity management
- **QR Code Tickets**: Automatic QR code generation for each registration
- **Email Confirmation**: Automatic email notifications with QR code tickets
- **Dashboard**: View personal registrations and manage account

### Admin Features
- **Admin Dashboard**: Comprehensive admin panel
- **Event Management**: Create, update, and manage events
- **User Management**: View and manage all users
- **Attendance Marking**: Mark attendance using ticket numbers or QR codes
- **Event Analytics**: View registration statistics

### Technical Features
- **RESTful API**: Clean REST API architecture
- **JWT Authentication**: Secure token-based authentication
- **Role-Based Access Control**: Admin and User roles with proper authorization
- **Database Design**: MySQL database with proper relationships
- **Design Patterns**: 
  - Singleton Pattern for database configuration
  - Factory Pattern for user roles
- **MVC Architecture**: Clean separation of concerns
- **Input Validation**: Comprehensive validation on both frontend and backend
- **CORS Support**: Configured for cross-origin requests

## Technology Stack

### Backend
- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Security** (JWT authentication)
- **Spring Data JPA** (Hibernate)
- **MySQL** (Database)
- **JWT** (JSON Web Tokens)
- **ZXing** (QR Code generation)
- **Spring Mail** (Email notifications)

### Frontend
- **HTML5**
- **CSS3**
- **JavaScript (ES6+)**
- **Bootstrap 5.3.0**

## Project Structure

```
event-registration-system/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/auca/event_registration_system/
│   │   │       ├── config/          # Configuration classes (Singleton pattern)
│   │   │       ├── controller/      # REST Controllers (MVC)
│   │   │       ├── dto/             # Data Transfer Objects
│   │   │       ├── factory/         # Factory Pattern (User roles)
│   │   │       ├── model/           # JPA Entities
│   │   │       ├── repository/      # JPA Repositories
│   │   │       ├── security/        # Security configuration
│   │   │       ├── service/         # Business logic
│   │   │       └── util/            # Utility classes (JWT, etc.)
│   │   └── resources/
│   │       ├── static/              # Frontend files
│   │       │   ├── css/
│   │       │   ├── js/
│   │       │   └── *.html
│   │       └── application.properties
│   └── test/
├── database/
│   └── schema.sql                   # Database schema
├── pom.xml                          # Maven dependencies
└── README.md
```

## Prerequisites

Before running the application, ensure you have:

1. **Java 17** or higher
2. **Maven 3.6+**
3. **MySQL 8.0+**
4. **Email Account** (Gmail recommended) for email notifications

## Setup Instructions

### 1. Database Setup

1. Install and start MySQL server
2. Create the database and tables by running the schema file:
   ```bash
   mysql -u root -p < database/schema.sql
   ```
   Or manually execute the SQL commands in `database/schema.sql`

3. Note: The default admin credentials are:
   - **Username**: `admin`
   - **Password**: `admin123`

### 2. Backend Configuration

1. Open `src/main/resources/application.properties`

2. Update database configuration:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/event_registration_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
   spring.datasource.username=your_mysql_username
   spring.datasource.password=your_mysql_password
   ```

3. Update email configuration (for Gmail):
   ```properties
   spring.mail.username=your-email@gmail.com
   spring.mail.password=your-app-password
   ```
   
   **Note**: For Gmail, you need to:
   - Enable 2-factor authentication
   - Generate an "App Password" from your Google Account settings
   - Use the app password (not your regular password)

4. Update JWT secret (use a strong secret key in production):
   ```properties
   jwt.secret=your-secret-key-change-this-in-production-minimum-256-bits
   ```

5. Configure CORS origins (if needed):
   ```properties
   cors.allowed-origins=http://localhost:3000,http://127.0.0.1:5500,http://localhost:5500
   ```

### 3. Build and Run

1. **Build the project**:
   ```bash
   mvn clean install
   ```

2. **Run the application**:
   ```bash
   mvn spring-boot:run
   ```
   
   Or run the main class: `EventRegistrationSystemApplication`

3. The backend will start on: `http://localhost:8080`

### 4. Frontend Setup

The frontend is served as static files by Spring Boot. Simply:

1. Open your browser and navigate to:
   ```
   http://localhost:8080/index.html
   ```

2. Or use a local web server (if you prefer):
   ```bash
   # Using Python
   python -m http.server 8000
   
   # Using Node.js http-server
   npx http-server -p 8000
   ```
   
   Then update `API_BASE_URL` in `src/main/resources/static/js/api.js` accordingly.

## API Endpoints

### Authentication
- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Login and get JWT token

### Events (Public)
- `GET /api/events/public/active` - Get all active events
- `GET /api/events/public/upcoming` - Get upcoming events
- `GET /api/events/public/{id}` - Get event by ID

### Events (Authenticated)
- `GET /api/events` - Get all events (authenticated)
- `POST /api/events` - Create new event (requires authentication)
- `PUT /api/events/{id}` - Update event
- `DELETE /api/events/{id}` - Delete event
- `GET /api/events/my-events` - Get events created by current user

### Registrations
- `POST /api/registrations/event/{eventId}` - Register for an event
- `GET /api/registrations/my-registrations` - Get user's registrations
- `GET /api/registrations/event/{eventId}` - Get registrations for an event
- `GET /api/registrations/ticket/{ticketNumber}` - Get registration by ticket number
- `DELETE /api/registrations/{id}` - Cancel registration

### Attendance
- `POST /api/attendance/mark/ticket/{ticketNumber}` - Mark attendance by ticket number
- `POST /api/attendance/mark/registration/{registrationId}` - Mark attendance by registration ID
- `GET /api/attendance/event/{eventId}` - Get attendance for an event
- `GET /api/attendance/all` - Get all attendance records (admin)

### Admin
- `GET /api/admin/users` - Get all users (admin only)
- `GET /api/admin/users/{id}` - Get user by ID (admin only)
- `DELETE /api/admin/users/{id}` - Delete user (admin only)

## Usage Guide

### For Users

1. **Register**: Create an account at `/register.html`
2. **Login**: Login at `/login.html`
3. **Browse Events**: View available events at `/events.html`
4. **Register for Event**: Click "Register Now" on any event
5. **View Tickets**: Check your dashboard for QR code tickets
6. **Check Email**: Confirmation email with QR code will be sent automatically

### For Administrators

1. **Login**: Use admin credentials (admin/admin123 by default)
2. **Create Events**: Go to Dashboard → Create Event tab
3. **Manage Events**: View and manage your created events
4. **Mark Attendance**: Go to Admin → Attendance tab, enter ticket number
5. **Manage Users**: View all users in Admin → Users tab

## Design Patterns Implemented

### 1. Singleton Pattern
**Location**: `src/main/java/com/auca/event_registration_system/config/DatabaseConfigSingleton.java`

Ensures a single instance of database configuration throughout the application lifecycle.

### 2. Factory Pattern
**Location**: `src/main/java/com/auca/event_registration_system/factory/RoleFactory.java`

Creates and manages user roles with appropriate authorities and permissions.

### 3. MVC Architecture
- **Model**: JPA Entities (`model/` package)
- **View**: HTML/CSS/JavaScript frontend (`static/` directory)
- **Controller**: REST Controllers (`controller/` package)

## Database Schema

The database consists of 4 main tables:

1. **users**: Stores user accounts with roles
2. **events**: Stores event information
3. **registrations**: Stores user event registrations with QR codes
4. **attendances**: Stores attendance records

See `database/schema.sql` for complete schema definition.

## Security Features

- **JWT Authentication**: Secure token-based authentication
- **Password Encryption**: BCrypt password hashing
- **Role-Based Access Control**: Admin and User roles
- **CORS Configuration**: Configured for allowed origins
- **Input Validation**: Server-side validation using Bean Validation
- **SQL Injection Protection**: Using JPA/Hibernate parameterized queries

## Email Configuration

The system sends confirmation emails with QR code tickets. To configure:

1. **Gmail Setup**:
   - Enable 2-factor authentication
   - Generate App Password: Google Account → Security → App passwords
   - Use the generated app password in `application.properties`

2. **Other Email Providers**:
   - Update `spring.mail.host` and `spring.mail.port` in `application.properties`
   - Adjust authentication settings as needed

## Troubleshooting

### Common Issues

1. **Database Connection Error**:
   - Verify MySQL is running
   - Check database credentials in `application.properties`
   - Ensure database exists

2. **Email Not Sending**:
   - Verify email credentials
   - For Gmail, ensure app password is used (not regular password)
   - Check firewall/network settings

3. **CORS Errors**:
   - Update `cors.allowed-origins` in `application.properties`
   - Ensure frontend URL matches allowed origins

4. **JWT Token Issues**:
   - Check JWT secret in `application.properties`
   - Ensure token is sent in `Authorization: Bearer <token>` header

5. **Port Already in Use**:
   - Change `server.port` in `application.properties`
   - Or stop the process using port 8080

## Production Deployment

Before deploying to production:

1. **Change JWT Secret**: Use a strong, randomly generated secret (minimum 256 bits)
2. **Update Database Credentials**: Use secure database credentials
3. **Configure HTTPS**: Set up SSL/TLS certificates
4. **Email Configuration**: Use production email service
5. **Environment Variables**: Move sensitive data to environment variables
6. **Logging**: Configure proper logging levels
7. **Database Backups**: Set up regular database backups
8. **Security Headers**: Configure security headers
9. **Rate Limiting**: Implement rate limiting for API endpoints
10. **Monitoring**: Set up application monitoring

## Testing

### Manual Testing

1. **User Registration**: Test registration with valid/invalid data
2. **Login**: Test login with correct/incorrect credentials
3. **Event Creation**: Create events as admin
4. **Event Registration**: Register for events as user
5. **QR Code Generation**: Verify QR codes are generated
6. **Email Sending**: Check email delivery
7. **Attendance Marking**: Test attendance marking with ticket numbers

### API Testing

Use tools like Postman or curl to test API endpoints:

```bash
# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# Get Events (with token)
curl -X GET http://localhost:8080/api/events/public/active \
  -H "Authorization: Bearer YOUR_TOKEN"
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## License

This project is open source and available for educational purposes.

## Support

For issues and questions:
- Check the troubleshooting section
- Review the code comments
- Check Spring Boot and Spring Security documentation

## Acknowledgments

- Spring Boot Framework
- Bootstrap for UI components
- ZXing for QR code generation
- JWT for authentication

---

**Version**: 1.0.0  
**Last Updated**: 2024

