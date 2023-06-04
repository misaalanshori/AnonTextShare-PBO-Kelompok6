/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pw.misa.kk6.models;

import java.util.ArrayList;
import java.util.List;
import pw.misa.kk6.AnonymousText.Document;
import pw.misa.kk6.Database.DatabaseConnection;

/**
 *
 * @author Isabu
 */
public class TextCollection {
        private DatabaseConnection Access;

	private String ID;

	private String Pass;

	public String Title;

	public List<Document> Contents;
        
        public DatabaseConnection getAcces(){
            return Access;
        }
        public void setAcces(final DatabaseConnection Access){
            this.Access = Access;
        }

	public String getCollectionID() {
            return ID;
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
        public String getTittle(){
            return Title;
        }
        public void setTitle(final String Title){
            this.Title = Title;
        }
        public List<Document> getList(){
            return Contents;
        } 
        
        public void setList(final List<Document> Contents){
            this.Contents = Contents;
        }
}
