# Estate Management System
**Estate Management System** is a platform designed to manage Residents, Admins, and Visitors within an estate.

## 🚀 Setup & Installation
### Prerequisites
- **Java 22** or later
- **Maven**
- **MongoDB** running locally or via cloud URL
- **SMTP credentials** (e.g., Gmail App Password)

### Environment Variables
Create a `.env` file in the root directory (copied from `.env.example`) or set these environment variables directly:
```env
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_app_password
MONGO_URL=mongodb://localhost:27017/estate_db
```

### Running the Application
```bash
mvn spring-boot:run
```
The application will start on **port 8080** by default.

## 🛡️ Security Features
- **Password Hashing**: User passwords are encrypted using **BCrypt** before storage.
- **Environment Configuration**: Sensitive credentials are not hardcoded.

## 🧪 Testing
Run unit and integration tests:
```bash
mvn test
```
