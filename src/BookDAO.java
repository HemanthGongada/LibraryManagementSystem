import java.sql.*;
import java.util.*;

public class BookDAO {
    private Connection conn;

    // ✅ Constructor that accepts Connection
    public BookDAO(Connection conn) {
        this.conn = conn;
    }

 public boolean addBook(Book book) {
    try {
        // Check if book already exists
        String checkSql = "SELECT * FROM books WHERE title = ? AND author = ?";
        PreparedStatement checkStmt = conn.prepareStatement(checkSql);
        checkStmt.setString(1, book.getTitle());
        checkStmt.setString(2, book.getAuthor());
        ResultSet rs = checkStmt.executeQuery();

        if (rs.next()) {
            System.out.println("⚠️ Book already exists. Skipping insert.");
            return false;
        }

        // Insert if not found
        String sql = "INSERT INTO books (title, author, quantity) VALUES (?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, book.getTitle());
        stmt.setString(2, book.getAuthor());
        stmt.setInt(3, book.getQuantity());
        stmt.executeUpdate();
        return true;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}


    public List<Book> getAllBooks() throws SQLException {
        List<Book> bookList = new ArrayList<>();
        String sql = "SELECT * FROM books";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Book book = new Book(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getInt("quantity")
                );
                bookList.add(book);
            }
        }
        return bookList;
    }
    // In src/BookDAO.java

// Search book by title
public List<Book> searchBookByTitle(String titleKeyword) throws SQLException {
    List<Book> books = new ArrayList<>();
    String query = "SELECT * FROM books WHERE title LIKE ?";
    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
        pstmt.setString(1, "%" + titleKeyword + "%");
        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                books.add(new Book(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getInt("quantity")
                ));
            }
        }
    }
    return books;
}

// Delete book by ID
public boolean deleteBookById(int id) throws SQLException {
    String query = "DELETE FROM books WHERE id = ?";
    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
        pstmt.setInt(1, id);
        int rows = pstmt.executeUpdate();
        return rows > 0;
    }
}
// Check quantity of a book by ID
public int getBookQuantityById(int id) throws SQLException {
    String query = "SELECT quantity FROM books WHERE id = ?";
    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("quantity");
        }
    }
    return -1; // Book not found
}


}
