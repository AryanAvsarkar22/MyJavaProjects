
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

## ⚙️ Setup
1. Install **MySQL** and **Java JDK**.
2. Import `hospital_db.sql` into MySQL (creates DB + tables + sample doctors).
3. Edit DB credentials in `src/DatabaseConnection.java` (user/password).
4. Ensure **MySQL Connector/J** is on classpath when compiling/running.

### 🧪 Compile & Run (terminal)
```bash
# Go to src
cd src

# Compile (adjust mysql-connector path if needed)
javac -cp .:mysql-connector-j.jar *.java

# Run
java -cp .:mysql-connector-j.jar Main
```

> On Windows, replace `:` with `;` in classpath.

## 🔐 Notes
- This demo stores no passwords and has minimal validation — add auth before production use.
- Add indexes/constraints as needed for larger datasets.

## 📄 License
MIT
