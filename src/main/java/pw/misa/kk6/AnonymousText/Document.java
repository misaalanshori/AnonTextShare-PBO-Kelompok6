package pw.misa.kk6.AnonymousText;

import pw.misa.kk6.Database.DatabaseConnection;

public class Document {

	private DatabaseConnection DatabaseAccess;

	private String DocumentID;

	private String DocumentPass;

	public String DocumentTitle;

	public String DocumentText;

	public Document(DatabaseConnection database) {
            this.DatabaseAccess = database;
            this.DocumentID = null;
            this.DocumentPass = null;
            this.DocumentTitle = "";
            this.DocumentText = "";
	}

	public Document(DatabaseConnection database, String DocumentID) {
            if(database.checkDocument(DocumentID)){
                this.DatabaseAccess = database;
                this.DocumentID = DocumentID;
                this.DocumentPass = null;
                this.DocumentTitle = database.getDocumentTitle(DocumentID);
                this.DocumentText = database.getDocumentText(DocumentID);
            }else {
                System.out.println("Document not found.");
            }
	}

	public Document(DatabaseConnection database, String DocumentID, String DocumentPass) {
            if(database.checkDocument(DocumentID, DocumentPass)){
                this.DatabaseAccess = database;
                this.DocumentID = DocumentID;
                this.DocumentPass = DocumentPass;
                this.DocumentTitle = database.getDocumentTitle(DocumentID);
                this.DocumentText = database.getDocumentText(DocumentID);
            }else {
                System.out.println("Document not found.");
            }
	}

	public void reload() {
            if (this.DocumentID != null) {
                this.DocumentTitle = this.DatabaseAccess.getDocumentTitle(this.DocumentID);
                this.DocumentText = this.DatabaseAccess.getDocumentText(this.DocumentID);
            }
	}

	public void save() {
            if (this.DocumentID == null) {
                if (this.DocumentPass != null && !this.DocumentPass.isEmpty()) {
                    this.DocumentID = this.DatabaseAccess.createDocument(this.DocumentTitle, this.DocumentText, this.DocumentPass);
                } else {
                    this.DocumentID = this.DatabaseAccess.createDocument(this.DocumentTitle, this.DocumentText);
                }   
            } else if (!isReadOnly()) {
                this.DatabaseAccess.updateDocumentTitle(this.DocumentID, this.DocumentPass, this.DocumentTitle);
                this.DatabaseAccess.updateDocumentText(this.DocumentID, this.DocumentPass, this.DocumentText);
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
        
        public int getViewCount() {
            return this.DatabaseAccess.getDocumentViews(DocumentID);
        }
        
        public boolean delete() {
            if (!isReadOnly()) {
                this.DocumentsAccess.deleteDocument(DocumentID, DocumentPass);
                this.DocumentID = null;
                this.DocumentPass = null;
                this.DocumentTitle = "";
                this.DocumentText = "";
                return true;
            }
            return false;
        }

	public void loadPassword(String DocumentPass) {
            this.DocumentPass = DocumentPass;
	}

	public String getDocumentID() {
            return this.DocumentID;
	}

}
