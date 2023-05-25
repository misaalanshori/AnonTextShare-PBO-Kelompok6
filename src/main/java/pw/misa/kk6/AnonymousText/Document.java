package pw.misa.kk6.AnonymousText;
import java.util.List;
import pw.misa.kk6.Database.DatabaseConnection;

public class Document {

	private DatabaseConnection DatabaseAccess;

	private String DocumentID;

	private String DocumentPass;

	public String DocumentTitle;

	public String DocumentText;
        
        private int visibility;
        
        private List<Comment> DocumentComments;

	public Document(DatabaseConnection database) {
            this.DatabaseAccess = database;
            this.DocumentID = null;
            this.DocumentPass = null;
            this.DocumentComments = null;
            this.DocumentTitle = "";
            this.DocumentText = "";
            this.visibility = 1;
	}

	public Document(DatabaseConnection database, String DocumentID) {
            if(database.checkDocument(DocumentID)){
                this.DatabaseAccess = database;
                this.DocumentID = DocumentID;
                this.DocumentPass = null;
                reload();
            }else {
                System.out.println("Document not found.");
            }
	}

	public Document(DatabaseConnection database, String DocumentID, String DocumentPass) {
            if(database.checkDocument(DocumentID, DocumentPass)){
                this.DatabaseAccess = database;
                this.DocumentID = DocumentID;
                this.DocumentPass = DocumentPass;
                reload();
            }else {
                System.out.println("Document not found.");
            }
	}

	public void reload() {
            if (this.DocumentID != null) {
                this.DocumentTitle = this.DatabaseAccess.getDocumentTitle(this.DocumentID);
                this.DocumentText = this.DatabaseAccess.getDocumentText(this.DocumentID);
                this.DocumentComments = this.DatabaseAccess.getDocumentComments(DocumentID);
                this.visibility = this.DatabaseAccess.getDocumentVisibility(DocumentID);
            }
	}

	public void save() {
            if (this.DocumentID == null) {
                if (this.DocumentPass != null && !this.DocumentPass.isEmpty()) {
                    this.DocumentID = this.DatabaseAccess.createDocument(this.DocumentTitle, this.DocumentText, this.DocumentPass, this.visibility);
                } else {
                    this.DocumentID = this.DatabaseAccess.createDocument(this.DocumentTitle, this.DocumentText, this.visibility);
                }   
            } else if (!isReadOnly()) {
                this.DatabaseAccess.updateDocumentTitle(this.DocumentID, this.DocumentPass, this.DocumentTitle);
                this.DatabaseAccess.updateDocumentText(this.DocumentID, this.DocumentPass, this.DocumentText);
                this.DatabaseAccess.updateDocumentVisibility(this.DocumentID, this.DocumentPass, this.visibility);
            }
	}

	public boolean isReadOnly() {
            if (this.DocumentID == null) {
                return false;
            } else {
                if (this.DocumentPass == null || this.DocumentPass.isEmpty()) {
                    return true;
                } else {
                    return !this.DatabaseAccess.checkDocument(this.DocumentID, this.DocumentPass);
                }
            }
	}
        
        public void addComment(String name, String text) {
            if (this.DocumentID != null) {
                this.DatabaseAccess.addDocumentComment(DocumentID, name, text);
                reload();
            }
        }
        
        public List<Comment> getComments() {
            return DocumentComments;
        }
        
        public int getViewCount() {
            return this.DatabaseAccess.getDocumentViews(DocumentID);
        }
        
        public boolean delete() {
            if (!isReadOnly()) {
                this.DatabaseAccess.deleteDocument(DocumentID, DocumentPass);
                this.DocumentID = null;
                this.DocumentPass = null;
                this.DocumentTitle = "";
                this.DocumentText = "";
                this.visibility = 0;
                return true;
            }
            return false;
        }

	public void loadPassword(String DocumentPass) {
            this.DocumentPass = DocumentPass;
	}

        public void setVisibility(int visibility) {
            this.visibility = visibility;
        }

        public int getVisibility() {
            return this.visibility;
        }

	public String getDocumentID() {
            return this.DocumentID;
	}

}
