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
    public void setName(final String name){
        this.name = name;
    }

    public String getText() {
        return text;
    }
    public void setText(final String text){
        this.text = text;
    }
    
    } 
        private DatabaseConnection Access;

	private String ID;

	private String Pass;

	public String Title;

	public String Text;
        
        private int visibility;
        
        private List<pw.misa.kk6.AnonymousText.Comment> DocumentComments;

	public TextDocument(final DatabaseConnection database) {
            this.Access = database;
            this.ID = null;
            this.Pass = null;
            this.DocumentComments = null;
            this.Title = "";
            this.Text = "";
            this.visibility = 1;
	}

	public TextDocument(final DatabaseConnection database, final String DocumentID) {
            if(database.checkDocument(DocumentID)){
                this.Access = database;
                this.ID = DocumentID;
                this.Pass = null;
                reload();
            }else {
                System.out.println("Document not found.");
            }
	}

	public TextDocument(final DatabaseConnection database, final String DocumentID, final String DocumentPass) {
            if(database.checkDocument(DocumentID, DocumentPass)){
                this.Access = database;
                this.ID = DocumentID;
                this.Pass = DocumentPass;
                reload();
            }else {
                System.out.println("Document not found.");
            }
	}

	public void reload() {
            if (this.ID != null) {
                this.Title = this.Access.getDocumentTitle(this.ID);
                this.Text = this.Access.getDocumentText(this.ID);
                this.DocumentComments = this.Access.getDocumentComments(ID);
                this.visibility = this.Access.getDocumentVisibility(ID);
            }
	}
        public DatabaseConnection getAcces(){
            return Access;
        }
        public void setAcces(final DatabaseConnection Access){
            this.Access = Access;
        }
        
        public String getText(){
            return Text;
        }
        public void setText(final String Text){
            this.Text = Text;
        }
        public String getTite(){
            return Title;
        }
        public void setTitle(final String Title){
            this.Title = Title;
        }
        
        public void setVisibility(final int visibility) {
            this.visibility = visibility;
        }
        public int getVisibility() {
            return this.visibility;
        }
        
	public String getDocumentID() {
            return this.ID;
	}
        public void SetID(final String ID){
            this.ID = ID;
        }
        
        public String getPass(){
            return Pass;
        }
        public void SetPass(final String Pass){
            this.Pass = Pass;
        }
        
        public List<pw.misa.kk6.AnonymousText.Comment> getList(){
            return DocumentComments;
        } 
        public void setList(final List<pw.misa.kk6.AnonymousText.Comment> Comments){
            this.DocumentComments = Comments;
        }
        
}
