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

	public TextCollection(DatabaseConnection database) {
            this.Access = database;
            this.ID = null;
            this.Pass = null;
            this.Title = "";
            this.Contents = null;
	}

	public TextCollection(DatabaseConnection database, String CollectionID) {
            this.Access = database;
            this.ID = CollectionID;
            this.Pass = null;
            reload();
	}

	public TextCollection(DatabaseConnection database, String CollectionID, String CollectionPass) {
            this.Access = database;
            this.ID = CollectionID;
            this.Pass = CollectionPass;
            reload();
	}

	public void reload() {
            if(this.ID != null){
                if(Access.checkCollection(ID)){
                    this.Title = Access.getCollectionTitle(ID);
                    List<String> ListBaru = Access.getCollectionContents(ID);
                    List<Document> DocumentBaru = new ArrayList();
                    for(int i = 0 ; i < ListBaru.size(); i++){
                        if (this.Access.checkDocument(ListBaru.get(i))) {
                            DocumentBaru.add(new Document(Access, ListBaru.get(i)));
                        }
                        
                    }
                    Contents = DocumentBaru;
                }
            }
	}

	public void save() {
            List<String> ListBaru = new ArrayList();
            for(int i = 0 ; i < this.Contents.size(); i++){
                ListBaru.add(this.Contents.get(i).getDocumentID());
            }
            
            if(Pass != null && ID == null){
                this.ID = Access.createCollection(Title, ListBaru, this.Pass);
            }else if(ID == null){
                this.ID = Access.createCollection(this.Title, ListBaru); 
            }else if(!isReadOnly()){
                Access.updateCollectionTitle(ID, Pass, Title);
                Access.updateCollectionContents(ID, Pass, ListBaru);
            }else{
                System.out.println("Collection Read Only");
            }
	}

	public boolean isReadOnly() {
            if(this.ID == null){
                return false;
            }else if(Pass != null){   
                return !Access.checkCollection(ID, Pass);
            }else{
                return true;
            }		
	}

        public int getViewCount() {
            return this.Access.getCollectionViews(ID);
        }
        
        public boolean delete() {
            if (!isReadOnly()) {
                this.Access.deleteDocument(ID, Pass);
                this.ID = null;
                this.Pass = null;
                this.Title = "";
                this.Contents = null;
                return true;
            }
            return false;
        }
        
	public void loadPassword(String CollectionPass) {
             this.Pass = CollectionPass; 
	}

	public String getCollectionID() {
            return ID;
	}
}
