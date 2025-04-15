# 💼 IN6225

> The enterprise management system is used to manage employee information and project allocation within the enterprise, providing an efficient and convenient management method to improve enterprise Operational Efficiency. 

## 📁 Structure
```
├── backend/       # Spring Boot
│   └── src/       # source code
├── frontend/      # Vite + Vue 3
│   └── src/       # source code
├── docker/        # Docker scripts
```

## ⚙️ Tech Stack
- Backend: Spring Boot, Spring Security, JWT
- Frontend: Vite, Vue 3, TypeScript, Pinia, Vue Router, Tailwind CSS
- Database: MySQL
- Deployment: Docker, Docker Compose

## 🚀 Getting Started
### Option 1: Run Locally
- Backend
```bash
cd backend
java -jar in6225.jar
```
- Frontend
```bash
cd frontend
npm install
npm run dev
```

### Option 2: Docker Deployment(Recommend)
```bash
cd docker
docker compose up
```
> 🕐 MySQL may take 2–3 minutes to initialize on first launch — please be patient.

### 🧪 Default Accounts

| Role       | EID            | Password |
|------------|----------------|----------|
| Admin      | e.g. EID_99999 | 123456   |
| Test Users | e.g. EID_00000 | 123456   |
