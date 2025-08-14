import java.util.ArrayList;
import java.util.List;

public class Cart {
    List<Book> items = new ArrayList<>();

    public void addBook(Book book) {
        items.add(book);
        System.out.println(book.title + " added to cart.");
    }

    public void viewCart() {
        if (items.isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }
        System.out.println("Your Cart:");
        for (Book b : items) {
            System.out.println(b);
        }
    }
}
