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
    public class Comment {
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
    private String ID;

    private String Pass;

    public String Title;

    public String Text;

    private int visibility;

    private List<Comment> comments;

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

    public String getText() {
        return Text;
    }

    public void setText(String Text) {
        this.Text = Text;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }



        
}
