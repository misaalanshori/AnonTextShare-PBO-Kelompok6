/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pw.misa.kk6.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import pw.misa.kk6.db.DBConnection;
import pw.misa.kk6.models.TextDocument;

/**
 *
 * @author Isabu
 */
public class TextDocumentDao {
    private Connection conn;

    public TextDocumentDao() {
        conn = DBConnection.getConnection();
    }
    
    
    public String insert(TextDocument document) {
        try {
            CallableStatement cStmt;
            if (document.getPass() != null && !document.getPass().isBlank()) {
                String sql = "{CALL sys.create_document(?, ?, ?, ?, ?)}";
                cStmt = conn.prepareCall(sql);
                cStmt.registerOutParameter(1, Types.VARCHAR);
                cStmt.setString(2, document.getPass());
                cStmt.setString(3, document.getTitle());
                cStmt.setString(4, document.getText());
                cStmt.setInt(5, document.getVisibility());
                cStmt.execute();
                System.out.println("INFO: Dokumen inserted dengan password");
            } else {
                String sql = "{CALL sys.create_document_nopass(?, ?, ?, ?, )}";
                cStmt = conn.prepareCall(sql);
                cStmt.registerOutParameter(1, Types.VARCHAR);
                cStmt.setString(2, document.getTitle());
                cStmt.setString(3, document.getText());
                cStmt.setInt(4, document.getVisibility());
                cStmt.execute();
                System.out.println("INFO: Dokumen inserted tanpa password");
            }
            return cStmt.getString(1);
            
        } catch (SQLException e) {
            System.out.println("ERROR: Terjadi Kesalahan SQL: " + e.getMessage());
        }
        return null;
    }
    
    public void update(TextDocument document) {
        try {
            String sql = "{CALL sys.update_document(?, ?, ?, ?, ?)}";
            CallableStatement cStmt = conn.prepareCall(sql);
            cStmt.setString(1, document.getID());
            cStmt.setString(2, document.getPass());
            cStmt.setString(3, document.getTitle());
            cStmt.setString(4, document.getText());
            cStmt.setInt(5, document.getVisibility());
            cStmt.execute();
            System.out.println("INFO: Dokumen updated");
        } catch (SQLException e) {
            System.out.println("ERROR: Terjadi Kesalahan SQL: " + e.getMessage());
        }
    }
    
    public void delete(String id, String pass) {
         try {
            String sql = "{CALL sys.delete_document(?, ?)}";
            CallableStatement cStmt = conn.prepareCall(sql);
            cStmt.setString(1, id);
            cStmt.setString(2, pass);
            cStmt.execute();
            System.out.println("INFO: Dokumen deleted");
        } catch (SQLException e) {
            System.out.println("ERROR: Terjadi Kesalahan SQL: " + e.getMessage());
        }
    }
    
    // Method untuk menambahkan komentar pada dokumen
    public void insertComment(String id, TextDocument.Comment comment) {
        try {
            String sql = "{CALL sys.insert_document_comment(?, ?, ?)}";
            CallableStatement cStmt = conn.prepareCall(sql);
            cStmt.setString(1, id);
            cStmt.setString(2, comment.getName());
            cStmt.setString(3, comment.getText());
            cStmt.execute();
            System.out.println("INFO: Comment inserted");
        } catch (SQLException e) {
            System.out.println("ERROR: Terjadi Kesalahan SQL: " + e.getMessage());
        }
    }

    public TextDocument select(String id) {
        
        TextDocument doc = null;
        ArrayList<TextDocument.Comment> commentList = new ArrayList<>();
        PreparedStatement stmt;
        ResultSet rs;
        try {
            stmt = conn.prepareStatement("SELECT * FROM sys.documents_view WHERE id='" + id + "'");
            rs = stmt.executeQuery();
            doc = new TextDocument();
            while (rs.next()) {
                doc.setID(id);
                doc.setTitle(rs.getString("title"));
                doc.setText(rs.getString("text"));
                doc.setViewCount(rs.getInt("view_count"));
                doc.setVisibility(rs.getInt("visibility"));
            }
            
            stmt = conn.prepareStatement("SELECT * FROM sys.document_comment WHERE document_id='" + id + "'");
            rs = stmt.executeQuery();
            while (rs.next()) {
                commentList.add(doc.new Comment(rs.getString("name"), rs.getString("text")));
            }
            doc.setComments(commentList);

            String sql = "{CALL sys.increment_view_count(?)}";
            CallableStatement cStmt = conn.prepareCall(sql);
            cStmt.setString(1, id);
            cStmt.execute();
            System.out.println("INFO: Document Loaded");
        } catch (SQLException e) {
            System.out.println("Terjadi Kesalahan SQL: " + e.getMessage());
        }
        return doc;
    }
    
    public List<TextDocument> selectLatest(int count) {
        TextDocument doc = null;
        ArrayList<TextDocument> docList = new ArrayList<>();
        PreparedStatement stmt;
        ResultSet rs;
        try {
            stmt = conn.prepareStatement("SELECT * FROM (SELECT id, title, view_count FROM sys.documents_view WHERE visibility = 1 ORDER BY time_created DESC) WHERE ROWNUM <= " + count);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                doc = new TextDocument();
                doc.setID(rs.getString("id"));
                doc.setTitle(rs.getString("title"));
                doc.setViewCount(rs.getInt("view_count"));
                doc.setText("");
                doc.setComments(new ArrayList<>());
                doc.setVisibility(1);
                docList.add(doc);
            }
        } catch (SQLException e) {
            System.out.println("Terjadi Kesalahan SQL: " + e.getMessage());
        }
        return docList;
    }

}
