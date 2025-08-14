CREATE DATABASE bookstore;
USE bookstore;

CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE,
    password VARCHAR(50),
    role ENUM('customer', 'admin') DEFAULT 'customer'
);

CREATE TABLE books (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100),
    author VARCHAR(100),
    price DOUBLE
);

CREATE TABLE orders (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    book_id INT,
    quantity INT,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (book_id) REFERENCES books(id)
);

INSERT INTO books (title, author, price) VALUES
('Java Programming', 'James Gosling', 500.0),
('Effective Java', 'Joshua Bloch', 650.0),
('Head First Java', 'Kathy Sierra', 400.0);
