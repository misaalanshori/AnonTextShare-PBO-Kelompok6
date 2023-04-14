package pw.misa.kk6.Database;
import java.util.List;
public interface CollectionsAccessable {

	public abstract String createCollection(String title, List<String> contents);

	public abstract String createCollection(String title, List<String> contents, String colPass);

	public abstract boolean checkCollection(String colID);

	public abstract boolean checkCollection(String colID, String colPass);

	public abstract String getCollectionTitle(String colID);

	public abstract List<String> getCollectionContents(String colID);

	public abstract boolean updateCollectionTitle(String colID, String colPass, String newTitle);

	public abstract boolean updateCollectionContents(String colID, String colPass, List<String> newContents);

	public abstract boolean deleteCollection(String colID, String colPass);
        
        public abstract boolean incrementCollectionViews(String colID);
        
        public abstract int getCollectionViews(String colID);
}
