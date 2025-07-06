# 📱 ComPass – Navigate Your Company

**ComPass** is a mobile application designed to improve how companies manage employee leave requests. Developed as part of the course *"Mobile Device Application Development"*, this app enables streamlined, asynchronous communication between employees, department heads, and employers.

The app offers tailored functionality based on user role, making the process of handling leave requests transparent, organized, and mobile-friendly.

---

## 🎯 Core Purpose

In many companies, the management of leave requests can become chaotic, especially with large teams. **ComPass** allows:
- Employees to easily request leave
- Department heads to review and manage those requests
- Employers to oversee all departments, managers, and staff

---

## 👤 User Roles & Capabilities

### 🏢 Employer
- Creates company departments during registration  
- Views all departments, employees, and department heads  
- Adds departments dynamically

### 👔 Leave Manager (Head of Department)
- Registers under a specific department  
- Accepts or rejects employee leave requests  
- Views a calendar of leave dates  
- Sees which employees are currently on leave or actively working

### 👨‍💼 Employee
- Registers under a specific department  
- Submits leave requests via calendar UI  
- Attaches a doctor’s note for sick leave  
- Views the status of all requests (pending / approved / rejected)

### 🔐 All Users
- Choose role at signup (employer / manager / employee)  
- Have editable profile information  
- Login/logout functionality

---

## 🧠 Key Features

✔ Role-based registration and dashboard  
✔ Leave request creation with reason and optional document upload  
✔ Calendar integration for leave planning  
✔ Admin view of company hierarchy  
✔ Persistent login and session handling  
✔ Profile editing and user data management

---

## 🛠 Technologies Used

- **Android Studio**  
- **Java**
- **Firebase**
- **Calendar UI Component**  
- **File picker for uploading documents**  

---

## 🚀 How to Run

1. Clone the repository  
2. Open the project in Android Studio  
3. Set up Firebase/DB if required (see `/config` folder)  
4. Build & run on emulator or device
