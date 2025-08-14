import java.sql.*;
import java.util.*;

public class BookstoreApp {
    private Connection conn;
    private User currentUser;
    private Cart cart;

    public BookstoreApp() {
        conn = DatabaseConnection.getConnection();
        cart = new Cart();
    }

    public void register(String username, String password) {
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO users(username, password) VALUES(?, ?)");
            ps.setString(1, username);
            ps.setString(2, password);
            ps.executeUpdate();
            System.out.println("Registration successful!");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public boolean login(String username, String password) {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                currentUser = new User(rs.getInt("id"), rs.getString("username"), rs.getString("role"));
                System.out.println("Login successful! Welcome " + currentUser.username);
                return true;
            } else {
                System.out.println("Invalid credentials!");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public void listBooks() {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM books");
            while (rs.next()) {
                Book book = new Book(rs.getInt("id"), rs.getString("title"), rs.getString("author"), rs.getDouble("price"));
                System.out.println(book);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching books: " + e.getMessage());
        }
    }

    public void addToCart(int bookId) {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM books WHERE id=?");
            ps.setInt(1, bookId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Book book = new Book(rs.getInt("id"), rs.getString("title"), rs.getString("author"), rs.getDouble("price"));
                cart.addBook(book);
            } else {
                System.out.println("Book not found!");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void viewCart() {
        cart.viewCart();
    }
}
