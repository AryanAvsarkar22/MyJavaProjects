
# 🏥 Hospital Management System (Java Swing + MySQL)

## 📌 Overview
A simple **Java Swing** desktop app to manage **Patients, Doctors, and Appointments** with a **MySQL** backend.

## ✨ Features
- Add / Update / Delete **Patients**
- Add / Update / Delete **Doctors**
- Create / Delete **Appointments**
- Searchable tables, form-to-table sync
- Clean OOP structure and JDBC access layer

## 🧰 Tech Stack
- Java SE 8+ (Swing)
- MySQL 5.7/8.0
- JDBC (MySQL Connector/J)

## 🗂 Project Structure
```
HospitalManagementSystem/
 ├── src/
 │    ├── Main.java
 │    ├── DatabaseConnection.java
 │    ├── HospitalApp.java
 │    ├── Patient.java
 │    ├── Doctor.java
 │    └── Appointment.java
 └── hospital_db.sql
```

## 🔐 Notes
- This demo stores no passwords and has minimal validation — add auth before production use.
- Add indexes/constraints as needed for larger datasets.


