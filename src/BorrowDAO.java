// src/BorrowDAO.java
import java.sql.*;

public class BorrowDAO {
    private Connection conn;

    public BorrowDAO(Connection conn) {
        this.conn = conn;
    }

    public boolean borrowBook(int bookId, int memberId) {
        try {
            // Check if book is available
            String check = "SELECT quantity FROM books WHERE id=?";
            PreparedStatement psCheck = conn.prepareStatement(check);
            psCheck.setInt(1, bookId);
            ResultSet rs = psCheck.executeQuery();
            if (rs.next() && rs.getInt("quantity") > 0) {
                // Update book quantity
                String update = "UPDATE books SET quantity = quantity - 1 WHERE id=?";
                PreparedStatement psUpdate = conn.prepareStatement(update);
                psUpdate.setInt(1, bookId);
                psUpdate.executeUpdate();

                // Insert into borrowed_books
                String insert = "INSERT INTO borrowed_books (book_id, member_id, borrow_date) VALUES (?, ?, CURDATE())";
                PreparedStatement psInsert = conn.prepareStatement(insert);
                psInsert.setInt(1, bookId);
                psInsert.setInt(2, memberId);
                psInsert.executeUpdate();

                return true;
            } else {
                System.out.println("❌ Book not available!");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean returnBook(int memberId, int bookId) {
    String deleteSql = "DELETE FROM borrowed_books WHERE member_id = ? AND book_id = ? LIMIT 1";
    String updateSql = "UPDATE books SET quantity = quantity + 1 WHERE id = ?";

    try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
         PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {

        deleteStmt.setInt(1, memberId);
        deleteStmt.setInt(2, bookId);
        int deleted = deleteStmt.executeUpdate();

        if (deleted > 0) {
            updateStmt.setInt(1, bookId);
            updateStmt.executeUpdate();
            return true;
        } else {
            System.out.println("❌ No borrowed book found to return.");
            return false;
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
public void listAllBorrowedBooks() {
    String sql = "SELECT m.name AS member_name, b.title AS book_title, bb.borrow_date " +
                 "FROM borrowed_books bb " +
                 "JOIN members m ON bb.member_id = m.member_id " +
                 "JOIN books b ON bb.book_id = b.id";

    try (Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
        System.out.println("\n📋 Borrowed Books:");
        while (rs.next()) {
            String memberName = rs.getString("member_name");
            String bookTitle = rs.getString("book_title");
            String borrowDate = rs.getString("borrow_date");
            System.out.println("Member: " + memberName + ", Book: " + bookTitle + ", Date: " + borrowDate);
        }
    } catch (SQLException e) {
        System.out.println("❌ Error retrieving borrowed books.");
        e.printStackTrace();
    }
}



}
