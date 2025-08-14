import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BookstoreApp app = new BookstoreApp();

        System.out.println("=== Welcome to Online Bookstore ===");
        while (true) {
            System.out.println("\n1. Register\n2. Login\n3. Exit");
            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                System.out.print("Username: ");
                String u = sc.nextLine();
                System.out.print("Password: ");
                String p = sc.nextLine();
                app.register(u, p);
            } else if (choice == 2) {
                System.out.print("Username: ");
                String u = sc.nextLine();
                System.out.print("Password: ");
                String p = sc.nextLine();
                if (app.login(u, p)) {
                    while (true) {
                        System.out.println("\n1. View Books\n2. Add to Cart\n3. View Cart\n4. Logout");
                        int ch = sc.nextInt();
                        if (ch == 1) app.listBooks();
                        else if (ch == 2) {
                            System.out.print("Enter book ID: ");
                            int id = sc.nextInt();
                            app.addToCart(id);
                        } else if (ch == 3) app.viewCart();
                        else break;
                    }
                }
            } else break;
        }
        sc.close();
    }
}
