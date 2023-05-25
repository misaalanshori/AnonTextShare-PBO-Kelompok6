package pw.misa.kk6.AnonymousText;
import java.util.ArrayList;
import java.util.List;
import pw.misa.kk6.Database.DatabaseConnection;
public class LatestDocuments {
    private List<Document> latesDocuments;
    private DatabaseConnection database;
    public LatestDocuments(DatabaseConnection database){
        this.database = database;
    }
    public void reload(){
        List<String> docString = database.getLatestDocuments(10);
        latesDocuments = new ArrayList<>();
        for (String documentString : docString) {
            Document document = new Document(database, documentString);
            latesDocuments.add(document);
        }
        
    }
    
    public List<Document> getList(){
        return latesDocuments;
    }
}
