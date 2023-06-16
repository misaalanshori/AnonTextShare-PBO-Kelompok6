/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pw.misa.kk6.models;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Isabu
 */
public class TextDocument {
    public class Comment {
        private String name;
        private String text;

        public Comment() {
        }
        
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

    private String Title;

    private String Text;

    private int visibility;
    
    private int viewCount;

    private List<Comment> comments;

    public TextDocument() {
    }

    public TextDocument(String Pass, String Title, String Text) {
        this.Pass = Pass;
        this.Title = Title;
        this.Text = Text;
        this.viewCount = 0;
        this.visibility = 1;
        this.comments = new ArrayList<>();
    }
    
    public TextDocument(String Title, String Text) {
        this.Title = Title;
        this.Text = Text;
        this.viewCount = 0;
        this.visibility = 1;
        this.comments = new ArrayList<>();
    }

    public TextDocument(String Pass, String Title, String Text, int visibility) {
        this.Pass = Pass;
        this.Title = Title;
        this.Text = Text;
        this.visibility = visibility;
        this.viewCount = 0;
        this.comments = new ArrayList<>();
    }
    
    public TextDocument(String Title, String Text, int visibility) {
        this.Title = Title;
        this.Text = Text;
        this.visibility = visibility;
        this.viewCount = 0;
        this.comments = new ArrayList<>();
    }

    public TextDocument(String ID, String Pass, String Title, String Text, int visibility, int viewCount, List<Comment> comments) {
        this.ID = ID;
        this.Pass = Pass;
        this.Title = Title;
        this.Text = Text;
        this.visibility = visibility;
        this.viewCount = viewCount;
        this.comments = comments;
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

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public void print() {
        System.out.println("id: " + ID);
        System.out.println("title: " + Title);
        System.out.println("text: " + Text);
        System.out.println("view count: " + viewCount);
        System.out.println("visibility: " + visibility);
        for (TextDocument.Comment comment : comments) {
            System.out.println("Comment: " + comment.getName() + " - " + comment.getText());
        }
    }

    @Override
    public String toString() {
        return ID + " - " + Title  + " - " + viewCount + " - " + visibility;
    }
    
    
}
