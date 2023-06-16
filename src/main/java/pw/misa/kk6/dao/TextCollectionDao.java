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
import pw.misa.kk6.db.DBConnection;
import pw.misa.kk6.models.TextCollection;
import pw.misa.kk6.models.TextDocument;

/**
 *
 * @author Isabu
 */
public class TextCollectionDao {
    private Connection conn;

    public TextCollectionDao() {
        conn = DBConnection.getConnection();
    }

    public String insert(TextCollection collection) {
        String collectionid;
        try {
            CallableStatement cStmt;
            if (collection.getPass() != null && !collection.getPass().isBlank()) {
                String sql = "{CALL sys.create_collection(?, ?, ?)}";
                cStmt = conn.prepareCall(sql);
                cStmt.registerOutParameter(1, Types.VARCHAR);
                cStmt.setString(2, collection.getPass());
                cStmt.setString(3, collection.getTitle());
                cStmt.execute();
                collectionid = cStmt.getString(1);
                for (TextDocument docs : collection.getContents()) {
                    sql = "{CALL sys.insert_collection_document(?, ?, ?)}";
                    cStmt = conn.prepareCall(sql);
                    cStmt.setString(1, collectionid);
                    cStmt.setString(2, docs.getID());
                    cStmt.setString(3, collection.getPass());
                    cStmt.execute();
                }
                System.out.println("INFO: Koleksi inserted dengan password");
            } else {
                String sql = "{CALL sys.start_create_collection_nopass(?, ?)}";
                cStmt = conn.prepareCall(sql);
                cStmt.registerOutParameter(1, Types.VARCHAR);
                cStmt.setString(2, collection.getTitle());
                cStmt.execute();
                collectionid = cStmt.getString(1);
                for (TextDocument docs : collection.getContents()) {
                    sql = "{CALL sys.insert_collection_document(?, ?, ?)}";
                    cStmt = conn.prepareCall(sql);
                    cStmt.setString(1, collectionid);
                    cStmt.setString(2, docs.getID());
                    cStmt.setNull(3, Types.VARCHAR);
                    cStmt.execute();
                }
                sql = "{CALL sys.end_create_collection_nopass(?)}";
                cStmt = conn.prepareCall(sql);
                cStmt.setString(1, collectionid);
                cStmt.execute();
                System.out.println("INFO: koleksi inserted tanpa password");
            }
            return collectionid;
            
        } catch (SQLException e) {
            System.out.println("ERROR: Terjadi Kesalahan SQL: " + e.getMessage());
            throw new DaoException("Terjadi Kesalahan SQL", e);
        }
    }

    // Method untuk mengupdate attribut collection
    public void update(TextCollection collection) {
        if (collection == null || collection.getID() == null || collection.getID().isBlank()) {
            throw new DaoException("Koleksi Tidak Boleh Kosong!");
        }
        try {
            String sql = "{CALL sys.update_collection(?, ?, ?)}";
            CallableStatement cStmt = conn.prepareCall(sql);
            cStmt.setString(1, collection.getID());
            cStmt.setString(2, collection.getPass());
            cStmt.setString(3, collection.getTitle());
            cStmt.execute();
            System.out.println("INFO: Koleksi updated");
        } catch (SQLException e) {
            System.out.println("ERROR: Terjadi Kesalahan SQL: " + e.getMessage());
            if (e.getErrorCode() == -20021) {
                throw new DaoException("Password Koleksi Salah", e);
            } else if (e.getErrorCode() == -20022) {
                throw new DaoException("Koleksi Tidak Ditemukan", e);
            }
            throw new DaoException("Terjadi Kesalahan SQL", e);
        }
    }
    
    // Method untuk menambahkan dokumen ke collection
    public void insertDocument(TextCollection collection, TextDocument document) {
        try {
            String sql = "{CALL sys.insert_collection_document(?, ?, ?)}";
            CallableStatement cStmt = conn.prepareCall(sql);
            cStmt.setString(1, collection.getID());
            cStmt.setString(2, document.getID());
            cStmt.setString(3, collection.getPass());
            cStmt.execute();
            System.out.println("INFO: Dokumen inserted ke koleksi");
        } catch (SQLException e) {
            System.out.println("ERROR: Terjadi Kesalahan SQL: " + e.getMessage());
            throw new DaoException("Terjadi Kesalahan SQL", e);
        }
    }

    // Method untuk menghapus dokumen dari collection
    public void deleteDocument(TextCollection collection, TextDocument document) {
        try {
            String sql = "{CALL sys.delete_collection_document(?, ?, ?)}";
            CallableStatement cStmt = conn.prepareCall(sql);
            cStmt.setString(1, collection.getID());
            cStmt.setString(2, document.getID());
            cStmt.setString(3, collection.getPass());
            cStmt.execute();
            System.out.println("INFO: Dokumen deleted dari koleksi");
        } catch (SQLException e) {
            System.out.println("ERROR: Terjadi Kesalahan SQL: " + e.getMessage());
            throw new DaoException("Terjadi Kesalahan SQL", e);
        }
    }

    public void delete(String id, String pass) {
        if (id == null || id.isBlank()) {
            throw new DaoException("ID Koleksi Tidak Boleh Kosong!");
        }
        if (pass == null || pass.isBlank()) {
            throw new DaoException("Password Koleksi Tidak Boleh Kosong!");
        }
        try {
            String sql = "{CALL sys.delete_collection(?, ?)}";
            CallableStatement cStmt = conn.prepareCall(sql);
            cStmt.setString(1, id);
            cStmt.setString(2, pass);
            cStmt.execute();
            System.out.println("INFO: Koleksi updated");
        } catch (SQLException e) {
            System.out.println("ERROR: Terjadi Kesalahan SQL: " + e.getMessage());
            if (e.getErrorCode() == -20021) {
                throw new DaoException("Password Koleksi Salah", e);
            } else if (e.getErrorCode() == -20022) {
                throw new DaoException("Koleksi Tidak Ditemukan", e);
            }
            throw new DaoException("Terjadi Kesalahan SQL", e);
        }
    }
    
    public TextCollection select(String id) {
        if (id == null || id.isBlank()) {
            throw new DaoException("ID Koleksi Tidak Boleh Kosong!");
        }
        TextCollection collection = null;
        PreparedStatement stmt;
        ResultSet rs;
        ArrayList<TextDocument> docs = new ArrayList<>();
        
        try {
            collection = new TextCollection();
            
            stmt = conn.prepareStatement("SELECT * FROM sys.collections_view WHERE id = '" + id + "'");
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                collection.setID(rs.getString("id"));
                collection.setTitle(rs.getString("title"));
            }
            
            if (collection.getID() == null) {
                throw new DaoException("Koleksi Tidak Ditemukan");
            }

            stmt = conn.prepareStatement("SELECT id, view_count, title, visibility FROM sys.collection_document " +
                                        "JOIN sys.documents_view " +
                                        "ON (sys.documents_view.id = sys.collection_document.document_id) " +
                                        "WHERE sys.collection_document.collection_id = '" + id + "'");
            rs = stmt.executeQuery();
            while (rs.next()) {
                TextDocument doc = new TextDocument();
                doc.setID(rs.getString("id"));
                doc.setViewCount(rs.getInt("view_count"));
                doc.setTitle(rs.getString("title"));
                doc.setText("");
                doc.setVisibility(rs.getInt("visibility"));
                doc.setComments(new ArrayList<>());
                docs.add(doc);
            }
            collection.setContents(docs);
            System.out.println("INFO: Collection Loaded");
        } catch (SQLException e) {
            System.out.println("Terjadi Kesalahan SQL: " + e.getMessage());
            throw new DaoException("Terjadi Kesalahan SQL", e);
        }
        return collection;
    }
    
    public static void main(String args[]) {
        TextDocumentDao tdao = new TextDocumentDao();
        TextCollectionDao cdao = new TextCollectionDao();
        
        ArrayList<TextDocument> docs = new ArrayList<>();
        docs.add(tdao.select(tdao.insert(new TextDocument("apass", "a title 1", "a content 1"))));
        docs.add(tdao.select(tdao.insert(new TextDocument("apass", "a title 2", "a content 2"))));
        docs.add(tdao.select(tdao.insert(new TextDocument("a title 3", "a content 3"))));
        
        TextCollection colwithpass = cdao.select(cdao.insert(new TextCollection("", "apassw", "col titles", docs)));
        System.out.println("new collection:");
        colwithpass.print();
        
        
        try {
            colwithpass.setTitle("AAAAAAAAAAAAAAAAcollection");
            colwithpass.setPass("apassw");
            cdao.update(colwithpass);
            colwithpass = cdao.select(colwithpass.getID());
            System.out.println("update collection");
            colwithpass.print();
        } catch (DaoException e) {
            System.out.println("Terjadi kesalahan: " + e.getMessage());
            if (e.getSqlException() != null) {
                System.out.println("SQL Exception: " + e.getSqlException().getMessage());
            }
        }
        
        
        try {
            colwithpass.setPass("apassw");
            cdao.insertDocument(colwithpass, new TextDocument("a title 4", "a content 4"));
            colwithpass = cdao.select(colwithpass.getID());
            System.out.println("insert to collection");
            colwithpass.print();
        } catch (DaoException e) {
            System.out.println("Terjadi kesalahan: " + e.getMessage());
            if (e.getSqlException() != null) {
                System.out.println("SQL Exception: " + e.getSqlException().getMessage());
            }
        }
        
        
        try {
            colwithpass.setPass("apassw");
            cdao.deleteDocument(colwithpass, docs.get(0));
            colwithpass = cdao.select(colwithpass.getID());
            System.out.println("delete from collection");
            colwithpass.print();
        } catch (DaoException e) {
            System.out.println("Terjadi kesalahan: " + e.getMessage());
            if (e.getSqlException() != null) {
                System.out.println("SQL Exception: " + e.getSqlException().getMessage());
            }
        }
        
        
        try {
            cdao.delete(colwithpass.getID(), "apassw");
            colwithpass = cdao.select(colwithpass.getID());
            System.out.println("deleted collection");
            colwithpass.print();
        } catch (DaoException e) {
            System.out.println("Terjadi kesalahan: " + e.getMessage());
            if (e.getSqlException() != null) {
                System.out.println("SQL Exception: " + e.getSqlException().getMessage());
            }
        }
    }
}
