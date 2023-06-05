/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pw.misa.kk6.models;

import java.util.List;
import pw.misa.kk6.AnonymousText.Document;

/**
 *
 * @author Isabu
 */
public class TextCollection {
	private String ID;

	private String Pass;

	private String Title;

	private List<Document> Contents;

        public TextCollection() {
        }

        public TextCollection(String ID, String Pass, String Title, List<Document> Contents) {
            this.ID = ID;
            this.Pass = Pass;
            this.Title = Title;
            this.Contents = Contents;
        }
        
        public TextCollection(String ID, String Title, List<Document> Contents) {
            this.ID = ID;
            this.Title = Title;
            this.Contents = Contents;
        }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPass() {
        return Pass;
    }

    public void setPass(String Pass) {
        this.Pass = Pass;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public List<Document> getContents() {
        return Contents;
    }

    public void setContents(List<Document> Contents) {
        this.Contents = Contents;
    }

        
        
	
}
