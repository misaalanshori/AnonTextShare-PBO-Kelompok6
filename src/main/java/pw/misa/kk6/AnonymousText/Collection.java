package pw.misa.kk6.AnonymousText;
import java.util.List;
import pw.misa.kk6.Database.CollectionsAccessable;
import pw.misa.kk6.Database.DatabaseConnection;
import pw.misa.kk6.Database.DocumentAccesable;
import java.util.ArrayList;

public class Collection{

	private DatabaseConnection DatabaseAccess;

	private String CollectionID;

	private String CollectionPass;

	public String CollectionTitle;

	public List<Document> CollectionContents;

	public Collection(DatabaseConnection database) {
            this.DatabaseAccess = database;
            this.CollectionID = null;
            this.CollectionPass = null;
            this.CollectionTitle = "";
            this.CollectionContents = null;
	}

	public Collection(DatabaseConnection database, String CollectionID) {
            this.DatabaseAccess = database;
            this.CollectionID = CollectionID;
            this.CollectionPass = null;
            reload();
	}

	public Collection(DatabaseConnection database, String CollectionID, String CollectionPass) {
            this.DatabaseAccess = database;
            this.CollectionID = CollectionID;
            this.CollectionPass = CollectionPass;
            reload();
	}

	public void reload() {
            if(this.CollectionID != null){
                if(DatabaseAccess.checkCollection(CollectionID)){
                    this.CollectionTitle = DatabaseAccess.getCollectionTitle(CollectionID);
                    List<String> ListBaru = DatabaseAccess.getCollectionContents(CollectionID);
                    List<Document> DocumentBaru = new ArrayList();
                    for(int i = 0 ; i < ListBaru.size(); i++){
                        DocumentBaru.add(new Document(DatabaseAccess, ListBaru.get(i)));
                    }
                    CollectionContents = DocumentBaru;
                }
            }
	}

	public void save() {
            List<String> ListBaru = new ArrayList();
            for(int i = 0 ; i < this.CollectionContents.size(); i++){
                ListBaru.add(this.CollectionContents.get(i).getDocumentID());
            }
            
            if(CollectionPass != null && CollectionID == null){
                this.CollectionID = DatabaseAccess.createCollection(CollectionTitle, ListBaru, this.CollectionPass);
            }else if(CollectionID == null){
                this.CollectionID = DatabaseAccess.createCollection(this.CollectionTitle, ListBaru); 
            }else if(!isReadOnly()){
                DatabaseAccess.updateCollectionTitle(CollectionID, CollectionPass, CollectionTitle);
                DatabaseAccess.updateCollectionContents(CollectionID, CollectionPass, ListBaru);
            }else{
                System.out.println("Collection Read Only");
            }
	}

	public boolean isReadOnly() {
            if(this.CollectionID == null){
                return false;
            }else if(CollectionPass != null){   
                return !DatabaseAccess.checkCollection(CollectionID, CollectionPass);
            }else{
                return true;
            }		
	}

        public int getViewCount() {
            return this.DatabaseAccess.getCollectionViews(CollectionID);
        }
        
        public boolean delete() {
            if (!isReadOnly()) {
                this.DatabaseAccess.deleteDocument(CollectionID, CollectionPass);
                this.CollectionID = null;
                this.CollectionPass = null;
                this.CollectionTitle = "";
                this.CollectionContents = null;
                return true;
            }
            return false;
        }
        
	public void loadPassword(String CollectionPass) {
             this.CollectionPass = CollectionPass; 
	}

	public String getCollectionID() {
            return CollectionID;
	}
}
