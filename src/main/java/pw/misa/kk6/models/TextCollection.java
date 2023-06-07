/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pw.misa.kk6.models;

import java.util.List;

/**
 *
 * @author Isabu
 */
public class TextCollection {
	private String ID;

	private String Pass;

	private String Title;

	private List<TextDocument> Contents;

        public TextCollection() {
        }

        public TextCollection(String ID, String Pass, String Title, List<TextDocument> Contents) {
            this.ID = ID;
            this.Pass = Pass;
            this.Title = Title;
            this.Contents = Contents;
        }
        
        public TextCollection(String ID, String Title, List<TextDocument> Contents) {
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

    public List<TextDocument> getContents() {
        return Contents;
    }

    public void setContents(List<TextDocument> Contents) {
        this.Contents = Contents;
    }

        
    public void print() {
        System.out.println("id: " + ID);
        System.out.println("title: " + Title);
        for (TextDocument doc : Contents) {
            System.out.println("Documents: " + doc);
        }
    }     
    
    @Override
    public String toString() {
        return ID + " - " + Title;
    }
	
}
