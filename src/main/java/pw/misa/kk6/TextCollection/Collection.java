package pw.misa.kk6.TextCollection;
import java.util.List;
import pw.misa.kk6.Database.CollectionsAccessable;
import pw.misa.kk6.TextDocument.Document;
import pw.misa.kk6.Database.DatabaseConnection;
import pw.misa.kk6.Database.DocumentAccesable;
import java.util.ArrayList;

public class Collection{

	private CollectionsAccessable CollectionsAccess;

	private String CollectionID;

	private String CollectionPass;

	public String CollectionTitle;

	public List<Document> CollectionContents;

        private DocumentAccesable DocumentAccess;
        
	public Collection(DatabaseConnection database) {
            this.DocumentAccess = database;
            this.CollectionsAccess = database;
            this.CollectionID = null;
            this.CollectionPass = null;
            this.CollectionTitle = "";
            this.CollectionContents = null;
	}

	public Collection(DatabaseConnection database, String CollectionID) {
            this.DocumentAccess = database;
            this.CollectionsAccess = database;
            this.CollectionID = CollectionID;
            this.CollectionPass = null;
            this.CollectionsAccess.incrementCollectionViews(CollectionID);
            reload();
	}

	public Collection(DatabaseConnection database, String CollectionID, String CollectionPass) {
            this.DocumentAccess = database;
            this.CollectionsAccess = database;
            this.CollectionID = CollectionID;
            this.CollectionPass = CollectionPass;
            this.CollectionsAccess.incrementCollectionViews(CollectionID);
            reload();
	}

	public void reload() {
            if(this.CollectionID != null){
                if(CollectionsAccess.checkCollection(CollectionID)){
                    this.CollectionTitle = CollectionsAccess.getCollectionTitle(CollectionID);
                    List<String> ListBaru = CollectionsAccess.getCollectionContents(CollectionID);
                    List<Document> DocumentBaru = new ArrayList();
                    for(int i = 0 ; i < ListBaru.size(); i++){
                        DocumentBaru.add(new Document(DocumentAccess, ListBaru.get(i)));
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
                this.CollectionID = CollectionsAccess.createCollection(CollectionTitle, ListBaru, this.CollectionPass);
            }else if(CollectionID == null){
                this.CollectionID = CollectionsAccess.createCollection(this.CollectionTitle, ListBaru); 
            }else if(!isReadOnly()){
                CollectionsAccess.updateCollectionTitle(CollectionID, CollectionPass, CollectionTitle);
                CollectionsAccess.updateCollectionContents(CollectionID, CollectionPass, ListBaru);
            }else{
                System.out.println("Collection Read Only");
            }
	}

	public boolean isReadOnly() {
            if(this.CollectionID == null){
                return false;
            }else if(CollectionPass != null){   
                return !CollectionsAccess.checkCollection(CollectionID, CollectionPass);
            }else{
                return true;
            }		
	}

        public int getViewCount() {
            return this.CollectionsAccess.getCollectionViews(CollectionID);
        }
        
	public void loadPassword(String CollectionPass) {
             this.CollectionPass = CollectionPass; 
	}

	public String getCollectionID() {
            return CollectionID;
	}
}
