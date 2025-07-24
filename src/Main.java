// src/Main.java
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Connection conn = DBConnection.getConnection();
        if (conn == null) {
            System.out.println("❌ Database connection failed!");
            return;
        }
        System.out.println("✅ Database connection successful!");

        BookDAO bookDao = new BookDAO(conn);
        MemberDAO memberDao = new MemberDAO(conn);
        BorrowDAO borrowDao = new BorrowDAO(conn);

        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n📚===== LIBRARY MANAGEMENT MENU =====");
            System.out.println("1. Add Book");
            System.out.println("2. List All Books");
            System.out.println("3. Add Member");
            System.out.println("4. List All Members");
            System.out.println("5. Borrow Book");
            System.out.println("6. Return Book");
            System.out.println("7. List All Borrowed Books");
            System.out.println("8. Search Book by Title");
            System.out.println("9. Delete Book by ID");
            System.out.println("10. Search Member by Name/Email");
            System.out.println("11. Delete Member by ID");
            System.out.println("12. Check Book Quantity by ID");


            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine(); // consume newline

            try {
                switch (choice) {
                    case 1:
                        System.out.print("Enter title: ");
                        String title = sc.nextLine();
                        System.out.print("Enter author: ");
                        String author = sc.nextLine();
                        System.out.print("Enter quantity: ");
                        int qty = sc.nextInt();
                        sc.nextLine();
                        Book newBook = new Book(0, title, author, qty);
                        if (bookDao.addBook(newBook))
                            System.out.println("✅ Book added!");
                        break;

                    case 2:
                        List<Book> books = bookDao.getAllBooks();
                        System.out.println("\n📚 All Books:");
                        for (Book b : books)
                            System.out.println("ID: " + b.getId() + ", Title: " + b.getTitle() + ", Author: " + b.getAuthor() + ", Quantity: " + b.getQuantity());
                        break;

                    case 3:
                        System.out.print("Enter member name: ");
                        String name = sc.nextLine();
                        System.out.print("Enter email: ");
                        String email = sc.nextLine();
                        Member newMember = new Member(0, name, email);
                        if (memberDao.addMember(newMember))
                            System.out.println("✅ Member added!");
                        break;

                    case 4:
                        List<Member> members = memberDao.getAllMembers();
                        System.out.println("\n👤 All Members:");
                        for (Member m : members)
                            System.out.println("ID: " + m.getMemberId() + ", Name: " + m.getName() + ", Email: " + m.getEmail());
                        break;

                    case 5:
                        System.out.print("Enter member ID: ");
                        int memberId = sc.nextInt();
                        System.out.print("Enter book ID: ");
                        int bookId = sc.nextInt();
                        if (borrowDao.borrowBook(bookId, memberId))
                            System.out.println("✅ Book borrowed!");
                        break;

                    case 6:
                        System.out.print("Enter member ID: ");
                        int returnMemberId = sc.nextInt();
                        System.out.print("Enter book ID: ");
                        int returnBookId = sc.nextInt();
                        if (borrowDao.returnBook(returnBookId, returnMemberId))
                            System.out.println("✅ Book returned!");
                        break;

                    case 7:
                        borrowDao.listAllBorrowedBooks();
                        break;
                        case 8:
    System.out.print("Enter keyword to search in title: ");
    String keyword = sc.nextLine();
    List<Book> foundBooks = bookDao.searchBookByTitle(keyword);
    if (foundBooks.isEmpty()) {
        System.out.println("❌ No books found with that title.");
    } else {
        for (Book b : foundBooks) {
            System.out.println("ID: " + b.getId() + ", Title: " + b.getTitle() + ", Author: " + b.getAuthor() + ", Quantity: " + b.getQuantity());
        }
    }
    break;

case 9:
    System.out.print("Enter Book ID to delete: ");
    int delId = sc.nextInt();
    if (bookDao.deleteBookById(delId)) {
        System.out.println("✅ Book deleted successfully.");
    } else {
        System.out.println("❌ Book not found.");
    }
    break;
    case 10:
    sc.nextLine(); // consume newline
    System.out.print("Enter member name/email to search: ");
    String mKeyword = sc.nextLine();
    List<Member> foundMembers = memberDao.searchMember(mKeyword);
    if (foundMembers.isEmpty()) {
        System.out.println("❌ No members found.");
    } else {
        for (Member m : foundMembers) {
            System.out.println("ID: " + m.getMemberId() + ", Name: " + m.getName() + ", Email: " + m.getEmail());
        }
    }
    break;
    case 11:
    System.out.print("Enter member ID to delete: ");
     delId = sc.nextInt();
    if (memberDao.deleteMemberById(delId)) {
        System.out.println("✅ Member deleted successfully!");
    } else {
        System.out.println("❌ Member not found or could not be deleted.");
    }
    break;
    case 12:
    System.out.print("Enter book ID to check quantity: ");
    int bookIdQty = sc.nextInt();
     qty = bookDao.getBookQuantityById(bookIdQty);
    if (qty >= 0) {
        System.out.println("📚 Available Quantity: " + qty);
    } else {
        System.out.println("❌ Book not found.");
    }
    break;





                    case 0:
                        System.out.println("👋 Exiting system. Goodbye!");
                        break;

                    default:
                        System.out.println("❌ Invalid option. Try again.");
                }
            } catch (SQLException e) {
                System.out.println("❌ SQL Error: " + e.getMessage());
            }
        } while (choice != 0);

        sc.close();
    }
}
