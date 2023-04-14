package pw.misa.kk6.TextDocument;

import pw.misa.kk6.Database.DocumentAccesable;

public class Document {

	private DocumentAccesable DocumentsAccess;

	private String DocumentID;

	private String DocumentPass;

	public String DocumentTitle;

	public String DocumentText;

	public Document(DocumentAccesable database) {
            this.DocumentsAccess = database;
            this.DocumentID = null;
            this.DocumentPass = null;
            this.DocumentTitle = "";
            this.DocumentText = "";
	}

	public Document(DocumentAccesable database, String DocumentID) {
            if(database.checkDocument(DocumentID)){
                this.DocumentsAccess = database;
                this.DocumentID = DocumentID;
                this.DocumentPass = null;
                this.DocumentTitle = database.getDocumentTitle(DocumentID);
                this.DocumentText = database.getDocumentText(DocumentID);
            }else {
                System.out.println("Document not found.");
            }
	}

	public Document(DocumentAccesable database, String DocumentID, String DocumentPass) {
            if(database.checkDocument(DocumentID, DocumentPass)){
                this.DocumentsAccess = database;
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
                this.DocumentTitle = this.DocumentsAccess.getDocumentTitle(this.DocumentID);
                this.DocumentText = this.DocumentsAccess.getDocumentText(this.DocumentID);
            }
	}

	public void save() {
            if (this.DocumentID == null) {
                if (this.DocumentPass != null && !this.DocumentPass.isEmpty()) {
                    this.DocumentID = this.DocumentsAccess.createDocument(this.DocumentTitle, this.DocumentText, this.DocumentPass);
                } else {
                    this.DocumentID = this.DocumentsAccess.createDocument(this.DocumentTitle, this.DocumentText);
                }   
            } else if (!isReadOnly()) {
                this.DocumentsAccess.updateDocumentTitle(this.DocumentID, this.DocumentPass, this.DocumentTitle);
                this.DocumentsAccess.updateDocumentText(this.DocumentID, this.DocumentPass, this.DocumentText);
            }
	}

	public boolean isReadOnly() {
            if (this.DocumentID == null) {
                return false;
            } else {
                if (this.DocumentPass == null || this.DocumentPass.isEmpty()) {
                    return true;
                } else {
                    return !this.DocumentsAccess.checkDocument(this.DocumentID, this.DocumentPass);
                }
            }
	}
        
        public int getViewCount() {
            return this.DocumentsAccess.getDocumentViews(DocumentID);
        }

	public void loadPassword(String DocumentPass) {
            this.DocumentPass = DocumentPass;
	}

	public String getDocumentID() {
            return this.DocumentID;
	}

}
