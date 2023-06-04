/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pw.misa.kk6.models;

import java.util.List;
import pw.misa.kk6.Database.DatabaseConnection;

/**
 *
 * @author Isabu
 */
public class TextDocument {
    private class Comment {
        private String name;
        private String text;

    public Comment(String name, String text) {
        this.name = name;
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }
    
    } 
    private DatabaseConnection DatabaseAccess;

	private String ID;

	private String Pass;

	public String Title;

	public String Text;
        
        private int visibility;
        
        private List<pw.misa.kk6.AnonymousText.Comment> DocumentComments;

	public TextDocument(DatabaseConnection database) {
            this.DatabaseAccess = database;
            this.ID = null;
            this.Pass = null;
            this.DocumentComments = null;
            this.Title = "";
            this.Text = "";
            this.visibility = 1;
	}

	public TextDocument(DatabaseConnection database, String DocumentID) {
            if(database.checkDocument(DocumentID)){
                this.DatabaseAccess = database;
                this.ID = DocumentID;
                this.Pass = null;
                reload();
            }else {
                System.out.println("Document not found.");
            }
	}

	public TextDocument(DatabaseConnection database, String DocumentID, String DocumentPass) {
            if(database.checkDocument(DocumentID, DocumentPass)){
                this.DatabaseAccess = database;
                this.ID = DocumentID;
                this.Pass = DocumentPass;
                reload();
            }else {
                System.out.println("Document not found.");
            }
	}

	public void reload() {
            if (this.ID != null) {
                this.Title = this.DatabaseAccess.getDocumentTitle(this.ID);
                this.Text = this.DatabaseAccess.getDocumentText(this.ID);
                this.DocumentComments = this.DatabaseAccess.getDocumentComments(ID);
                this.visibility = this.DatabaseAccess.getDocumentVisibility(ID);
            }
	}

	public void save() {
            if (this.ID == null) {
                if (this.Pass != null && !this.Pass.isEmpty()) {
                    this.ID = this.DatabaseAccess.createDocument(this.Title, this.Text, this.Pass, this.visibility);
                } else {
                    this.ID = this.DatabaseAccess.createDocument(this.Title, this.Text, this.visibility);
                }   
            } else if (!isReadOnly()) {
                this.DatabaseAccess.updateDocumentTitle(this.ID, this.Pass, this.Title);
                this.DatabaseAccess.updateDocumentText(this.ID, this.Pass, this.Text);
                this.DatabaseAccess.updateDocumentVisibility(this.ID, this.Pass, this.visibility);
            }
	}

	public boolean isReadOnly() {
            if (this.ID == null) {
                return false;
            } else {
                if (this.Pass == null || this.Pass.isEmpty()) {
                    return true;
                } else {
                    return !this.DatabaseAccess.checkDocument(this.ID, this.Pass);
                }
            }
	}
        
        public void addComment(String name, String text) {
            if (this.ID != null) {
                this.DatabaseAccess.addDocumentComment(ID, name, text);
                reload();
            }
        }
        
        public List<pw.misa.kk6.AnonymousText.Comment> getComments() {
            return DocumentComments;
        }
        
        public int getViewCount() {
            return this.DatabaseAccess.getDocumentViews(ID);
        }
        
        public boolean delete() {
            if (!isReadOnly()) {
                this.DatabaseAccess.deleteDocument(ID, Pass);
                this.ID = null;
                this.Pass = null;
                this.Title = "";
                this.Text = "";
                this.visibility = 0;
                return true;
            }
            return false;
        }

	public void loadPassword(String DocumentPass) {
            this.Pass = DocumentPass;
	}

        public void setVisibility(int visibility) {
            this.visibility = visibility;
        }

        public int getVisibility() {
            return this.visibility;
        }

	public String getDocumentID() {
            return this.ID;
	}
}
