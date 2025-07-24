// src/MemberDAO.java
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {
    private Connection conn;

    public MemberDAO(Connection conn) {
        this.conn = conn;
    }

 public boolean addMember(Member member) {
    try {
        // Check if member exists by email
        String checkSql = "SELECT * FROM members WHERE email = ?";
        PreparedStatement checkStmt = conn.prepareStatement(checkSql);
        checkStmt.setString(1, member.getEmail());
        ResultSet rs = checkStmt.executeQuery();

        if (rs.next()) {
            System.out.println("⚠️ Member already exists. Skipping insert.");
            return false;
        }

        // Insert if not found
        String sql = "INSERT INTO members (name, email) VALUES (?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, member.getName());
        stmt.setString(2, member.getEmail());
        stmt.executeUpdate();
        return true;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}


    public List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM members";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("member_id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                members.add(new Member(id, name, email));
            }
        } catch (SQLException e) {
            System.out.println("❌ Failed to fetch members: " + e.getMessage());
        }
        return members;
    }
    // Search members by name or email
public List<Member> searchMember(String keyword) throws SQLException {
    List<Member> members = new ArrayList<>();
    String query = "SELECT * FROM members WHERE name LIKE ? OR email LIKE ?";
    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
        pstmt.setString(1, "%" + keyword + "%");
        pstmt.setString(2, "%" + keyword + "%");
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            members.add(new Member(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("email")
            ));
        }
    }
    return members;
}
// Delete member by ID
public boolean deleteMemberById(int id) throws SQLException {
    String query = "DELETE FROM members WHERE id = ?";
    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
        pstmt.setInt(1, id);
        int rowsAffected = pstmt.executeUpdate();
        return rowsAffected > 0;
    }
}


}
