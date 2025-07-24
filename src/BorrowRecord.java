// src/BorrowRecord.java
public class BorrowRecord {
    private int borrowId;
    private int bookId;
    private int memberId;
    private String borrowDate;
    private String returnDate;

    public BorrowRecord(int bookId, int memberId, String borrowDate) {
        this.bookId = bookId;
        this.memberId = memberId;
        this.borrowDate = borrowDate;
    }

    // Getters and Setters
    public int getBorrowId() { return borrowId; }
    public void setBorrowId(int borrowId) { this.borrowId = borrowId; }
    public int getBookId() { return bookId; }
    public int getMemberId() { return memberId; }
    public String getBorrowDate() { return borrowDate; }
    public String getReturnDate() { return returnDate; }
    public void setReturnDate(String returnDate) { this.returnDate = returnDate; }
}
