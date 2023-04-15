package pw.misa.kk6.Database;
import java.util.List;

public class OracleDBConnector extends DatabaseConnection implements DocumentAccesable, CollectionsAccessable {


	/**
	 * @see Database.DocumentAccesable#createDocument(String, String)
	 */
	public String createDocument(String title, String text) {
		return null;
	}


	/**
	 * @see Database.DocumentAccesable#createDocument(String, String, String)
	 */
	public String createDocument(String title, String text, String docPass) {
		return null;
	}


	/**
	 * @see Database.DocumentAccesable#checkDocument(String)
	 */
	public boolean checkDocument(String docID) {
		return false;
	}


	/**
	 * @see Database.DocumentAccesable#checkDocument(String, String)
	 */
	public boolean checkDocument(String docID, String docPass) {
		return false;
	}


	/**
	 * @see Database.DocumentAccesable#getDocumentTitle(String)
	 */
	public String getDocumentTitle(String docID) {
		return null;
	}


	/**
	 * @see Database.DocumentAccesable#getDocumentText(String)
	 */
	public String getDocumentText(String docID) {
		return null;
	}


	/**
	 * @see Database.DocumentAccesable#updateDocumentTitle(String, String, String)
	 */
	public boolean updateDocumentTitle(String docID, String docPass, String newTitle) {
            return false;
	}


	/**
	 * @see Database.DocumentAccesable#updateDocumentText(String, String, String)
	 */
	public boolean updateDocumentText(String docID, String docPass, String newContents) {
            return false;
	}


	/**
	 * @see Database.DocumentAccesable#deleteDocument(String, String)
	 */
	public boolean deleteDocument(String docID, String docPass) {
            return false;
	}
        
        @Override
        public int getDocumentViews(String docID) {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }


	/**
	 * @see Database.CollectionsAccessable#createCollection(String, )
	 */
	public String createCollection(String title, List<String> contents) {
		return null;
	}


	/**
	 * @see Database.CollectionsAccessable#createCollection(String, , String)
	 */
	public String createCollection(String title, List<String> contents, String colPass) {
		return null;
	}


	/**
	 * @see Database.CollectionsAccessable#checkCollection(String)
	 */
	public boolean checkCollection(String colID) {
		return false;
	}


	/**
	 * @see Database.CollectionsAccessable#checkCollection(String, String)
	 */
	public boolean checkCollection(String colID, String colPass) {
		return false;
	}


	/**
	 * @see Database.CollectionsAccessable#getCollectionTitle(String)
	 */
	public String getCollectionTitle(String colID) {
		return null;
	}


	/**
	 * @see Database.CollectionsAccessable#getCollectionContents(String)
	 */
	public List<String> getCollectionContents(String colID) {
		return null;
	}


	/**
	 * @see Database.CollectionsAccessable#updateCollectionTitle(String, String, String)
	 */
	public boolean updateCollectionTitle(String colID, String colPass, String newTitle) {
            return false;
	}


	/**
	 * @see Database.CollectionsAccessable#updateCollectionContents(String, String, )
	 */
	public boolean updateCollectionContents(String colID, String colPass, List<String> newContents) {
            return false;
	}


	/**
	 * @see Database.CollectionsAccessable#deleteCollection(String, String)
	 */
	public boolean deleteCollection(String colID, String colPass) {
            return false;
	}

        @Override
        public int getCollectionViews(String colID) {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }





}
