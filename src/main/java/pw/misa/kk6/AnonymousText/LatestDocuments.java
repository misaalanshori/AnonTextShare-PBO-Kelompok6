package pw.misa.kk6.AnonymousText;
import java.util.ArrayList;
import java.util.List;
import pw.misa.kk6.Database.DatabaseConnection;
public class LatestDocuments {
    private List<Document> latesDocuments;
    public void reload(){
        DatabaseConnection Con = null; 
        List<String> docString = Con.getLatestDocuments(0);
        
        int lastIndex = docString.size() - 1; // Mendapatkan indeks terakhir

        latesDocuments = new ArrayList<>();
        
        if (lastIndex >= 0) {
            String latestDocumentString = docString.get(lastIndex);
            Document document = new Document(Con);
            latesDocuments.add(document);
        }
        //for (String documentString : docString) {
        //    Document document = new Document(Con);
        //    latesDocuments.add(document);
        //}
        
    }
    
    public List<Document> getList(){
        return latesDocuments;
    }
}
