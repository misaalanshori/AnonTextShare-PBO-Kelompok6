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
        if (document == null) {
            throw new DaoException("Dokumen Tidak Boleh Kosong!");
        }
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
                String sql = "{CALL sys.create_document_nopass(?, ?, ?, ?)}";
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
            throw new DaoException("Terjadi Kesalahan SQL", e);
        }
    }
    
    public void update(TextDocument document) {
        if (document == null || document.getID() == null || document.getID().isBlank()) {
            throw new DaoException("Dokumen Tidak Boleh Kosong!");
        }
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
            if (e.getErrorCode() == -20011) {
                throw new DaoException("Password Dokumen Salah", e);
            } else if (e.getErrorCode() == -20012) {
                throw new DaoException("Dokumen Tidak Ditemukan", e);
            }
            throw new DaoException("Terjadi Kesalahan SQL", e);
        }
    }
    
    public void delete(String id, String pass) {
        if (id == null || id.isBlank()) {
            throw new DaoException("ID Dokumen Tidak Boleh Kosong!");
        }
        if (pass == null || pass.isBlank()) {
            throw new DaoException("Password Dokumen Tidak Boleh Kosong!");
        }
         try {
            String sql = "{CALL sys.delete_document(?, ?)}";
            CallableStatement cStmt = conn.prepareCall(sql);
            cStmt.setString(1, id);
            cStmt.setString(2, pass);
            cStmt.execute();
            System.out.println("INFO: Dokumen deleted");
        } catch (SQLException e) {
            System.out.println("ERROR: Terjadi Kesalahan SQL: " + e.getMessage());
            if (e.getErrorCode() == -20011) {
                throw new DaoException("Password Dokumen Salah", e);
            } else if (e.getErrorCode() == -20012) {
                throw new DaoException("Dokumen Tidak Ditemukan", e);
            }
            throw new DaoException("Terjadi Kesalahan SQL", e);
        }
    }
    
    // Method untuk menambahkan komentar pada dokumen
    public void insertComment(String id, TextDocument.Comment comment) {
        if (id == null || id.isBlank()) {
            throw new DaoException("ID Dokumen Tidak Boleh Kosong!");
        }
        if (comment == null) {
            throw new DaoException("Komentar Tidak Boleh null!");
        }
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
            if (e.getErrorCode() == -20012) {
                throw new DaoException("Dokumen Tidak Ditemukan", e);
            }
            throw new DaoException("Terjadi Kesalahan SQL", e);
        }
    }

    public TextDocument select(String id) {
        if (id == null || id.isBlank()) {
            throw new DaoException("ID Dokumen Tidak Boleh Kosong!");
        }
        TextDocument doc = null;
        ArrayList<TextDocument.Comment> commentList = new ArrayList<>();
        PreparedStatement stmt;
        ResultSet rs;
        try {
            stmt = conn.prepareStatement("SELECT * FROM sys.documents_view WHERE id='" + id + "'");
            rs = stmt.executeQuery();
            doc = new TextDocument();
            while (rs.next()) {
                doc.setID(rs.getString("id"));
                doc.setTitle(rs.getString("title"));
                doc.setText(rs.getString("text"));
                doc.setViewCount(rs.getInt("view_count"));
                doc.setVisibility(rs.getInt("visibility"));
            }
            
            if (doc.getID() == null) {
                throw new DaoException("Dokumen Tidak Ditemukan");
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
            if (e.getErrorCode() == -20012) {
                throw new DaoException("Dokumen Tidak Ditemukan", e);
            }
            throw new DaoException("Terjadi Kesalahan SQL", e);
        }
        return doc;
    }
    
    public List<TextDocument> selectLatest(int count) {
        if (count < 1) {
            throw new DaoException("Jumlah Latest Document harus >= 1");
        }
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
            throw new DaoException("Terjadi Kesalahan SQL", e);
        }
        return docList;
    }

      public static void main(String args[]) {
        TextDocument docWithPass = new TextDocument("", "a Title", "The Text contents",1);
        
        TextDocumentDao dao = new TextDocumentDao();
        
        String docWithPassID = dao.insert(docWithPass);
        System.out.println("docWithPass ID: " + docWithPassID);
        
        dao.insertComment(docWithPassID, docWithPass.new Comment("komen", "comment"));
        dao.insertComment(docWithPassID, docWithPass.new Comment("komensss", "komment"));

        TextDocument docWithPassNew = dao.select(docWithPassID);
        docWithPassNew.print();
        
        System.out.println("latest: ");
        List<TextDocument> latestDocs = dao.selectLatest(10);
        for (TextDocument doc : latestDocs) {
            System.out.println("===");
            doc.print();
        }
        
        System.out.println("update: ");
        docWithPassNew.setTitle("Sebuah Judul");
        docWithPassNew.setText("Sebuah Teks");
        docWithPassNew.setPass("APassword");
        
        try {
            dao.update(docWithPassNew);
        } catch (DaoException e) {
            System.out.println("Terjadi kesalahan: " + e.getMessage());
            if (e.getSqlException() != null) {
                System.out.println("SQL Exception: " + e.getSqlException().getMessage());
            }
        }
        
        docWithPassNew = dao.select(docWithPassID);
        docWithPassNew.print();
        
        
        try {
            dao.delete(docWithPassID, "APassword");
            docWithPassNew = dao.select(docWithPassID);
        } catch (DaoException e) {
            System.out.println("Terjadi kesalahan: " + e.getMessage());
            if (e.getSqlException() != null) {
                System.out.println("SQL Exception: " + e.getSqlException().getMessage());
            }
        }
        
        docWithPassNew.print();
    }
}
