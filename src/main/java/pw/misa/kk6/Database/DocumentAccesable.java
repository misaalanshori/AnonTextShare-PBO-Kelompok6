package pw.misa.kk6.Database;
import java.util.List;
import pw.misa.kk6.AnonymousText.Comment;

public abstract interface DocumentAccesable {

	public abstract String createDocument(String title, String text);

	public abstract String createDocument(String title, String text, String docPass);

	public abstract boolean checkDocument(String docID);

	public abstract boolean checkDocument(String docID, String docPass);

	public abstract String getDocumentTitle(String docID);

	public abstract String getDocumentText(String docID);

	public abstract boolean updateDocumentTitle(String docID, String docPass, String newTitle);

	public abstract boolean updateDocumentText(String docID, String docPass, String newContents);

	public abstract boolean deleteDocument(String docID, String docPass);

        public abstract int getDocumentViews(String docID);
        
        public abstract boolean addDocumentComment(String docID, String name, String text);
        
        public abstract List<Comment> getDocumentComments(String docID);
}
