package pw.misa.kk6.Database;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import pw.misa.kk6.AnonymousText.Comment;

/**
 * Implementasi database sederhana, menggunakan java hashmap untuk menyimpan data. Dimana key hashmap yang digunakan adalah kode id dokumen/koleksi dan valuenya adalah isi dokumen atau isi koleksinya
 */
public class SimpleDB extends DatabaseConnection implements DocumentAccesable, CollectionsAccessable {

	private HashMap<String, String> idDocPassMap;

	private HashMap<String, String> idDocTitleMap;

	private HashMap<String, String> idDocContentMap;
        
        private HashMap<String, Integer> idDocViewCountMap;

        private HashMap<String, List<Comment>> idDocCommentsMap;
        
	private HashMap<String, String> idColPassMap;

	private HashMap<String, String> idColTitleMap;

	private HashMap<String, List<String>> idColContentMap;
        
        private HashMap<String, Integer> idColViewCountMap;
        


	/**
	 * @see Database.DocumentAccesable#createDocument(String, String)
	 */
        
        
        
	public SimpleDB() {
                this.idDocPassMap = new HashMap<>();
                this.idDocTitleMap = new HashMap<>();
                this.idDocContentMap = new HashMap<>();
                this.idDocViewCountMap = new HashMap<>();
                this.idDocCommentsMap = new HashMap<>();
                this.idColPassMap = new HashMap<>();
                this.idColTitleMap = new HashMap<>();
                this.idColContentMap = new HashMap<>();
                this.idColViewCountMap = new HashMap<>();
	}

    public String createDocument(String title, String text) {
        String newID = genID();
        idDocTitleMap.put(newID, new String(title));
        idDocContentMap.put(newID, new String(text));
        idDocPassMap.put(newID, null);
        idDocViewCountMap.put(newID, 0);
        return newID;
    }


	/**
	 * @see Database.DocumentAccesable#createDocument(String, String, String)
	 */
	public String createDocument(String title, String text, String docPass) {
		String newID = genID();
                idDocTitleMap.put(newID, new String(title));
                idDocContentMap.put(newID, new String(text));
                idDocPassMap.put(newID, docPass);
                idDocViewCountMap.put(newID, 0);
                return newID;
	}


	/**
	 * @see Database.DocumentAccesable#checkDocument(String)
	 */
	public boolean checkDocument(String docID) {
		return idDocTitleMap.containsKey(docID);
	}


	/**
	 * @see Database.DocumentAccesable#checkDocument(String, String)
	 */
	public boolean checkDocument(String docID, String docPass) {
		if (idDocPassMap.containsKey(docID)) {
                    if (idDocPassMap.get(docID) == null) {
                        return false;
                    } else if (idDocPassMap.get(docID).equals(docPass)) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
	}


	/**
	 * @see Database.DocumentAccesable#getDocumentTitle(String)
	 */
	public String getDocumentTitle(String docID) {
		return idDocTitleMap.get(docID);
	}


	/**
	 * @see Database.DocumentAccesable#getDocumentText(String)
	 */
	public String getDocumentText(String docID) {
                if (checkDocument(docID)) {
                    idDocViewCountMap.replace(docID, idDocViewCountMap.get(docID)+1);
                }
		return idDocContentMap.get(docID);
	}


	/**
	 * @see Database.DocumentAccesable#updateDocumentTitle(String, String, String)
	 */
	public boolean updateDocumentTitle(String docID, String docPass, String newTitle) {
            if (checkDocument(docID, docPass)) {
                idDocTitleMap.replace(docID, new String(newTitle) );
                return true;
            }
            return false;
	}


	/**
	 * @see Database.DocumentAccesable#updateDocumentText(String, String, String)
	 */
	public boolean updateDocumentText(String docID, String docPass, String newContents) {
            if (checkDocument(docID, docPass)) {
                idDocContentMap.replace(docID, new String(newContents));
                return true;
            }
            return false;
	}


	/**
	 * @see Database.DocumentAccesable#deleteDocument(String, String)
	 */
	public boolean deleteDocument(String docID, String docPass) {
            if (checkDocument(docID, docPass)) {
                idDocTitleMap.remove(docID);
                idDocContentMap.remove(docID);
                idDocPassMap.remove(docID);
                return true;
            }
            return false;
	}


        @Override
        public int getDocumentViews(String docID) {
            if (checkDocument(docID)) {   
                return idDocViewCountMap.get(docID);
            }
            return -1;
        }

        @Override
        public boolean addDocumentComment(String docID, String name, String text) {
            if (checkDocument(docID)) {
                idDocCommentsMap.get(docID).add(new Comment(name, text));
                return true;
            }
            return false;
        }

        @Override
        public List<Comment> getDocumentComments(String docID) {
            if (checkDocument(docID)) {
                return new ArrayList(idDocCommentsMap.get(docID));
            }
            return null;
            
        }

        
        
        
        
	/**
	 * @see Database.CollectionsAccessable#createCollection(String, )
	 */
	public String createCollection(String title, List<String> contents) {
            String newID = genID();
            idColTitleMap.put(newID, new String(title));
            idColContentMap.put(newID, new ArrayList<String>(contents));
            idColPassMap.put(newID, null);
            idColViewCountMap.put(newID, 0);
            return newID;
	}


	/**
	 * @see Database.CollectionsAccessable#createCollection(String, , String)
	 */
	public String createCollection(String title, List<String> contents, String colPass) {
		String newID = genID();
                idColTitleMap.put(newID, new String(title));
                idColContentMap.put(newID, new ArrayList<String>(contents));
                idColPassMap.put(newID, colPass);
                idColViewCountMap.put(newID, 0);
                return newID;
	}


	/**
	 * @see Database.CollectionsAccessable#checkCollection(String)
	 */
	public boolean checkCollection(String colID) {
		return idColTitleMap.containsKey(colID);
	}


	/**
	 * @see Database.CollectionsAccessable#checkCollection(String, String)
	 */
	public boolean checkCollection(String colID, String colPass) {
		if (idColPassMap.containsKey(colID)) {
                    if (idColPassMap.get(colID) == null) {
                        return false;
                    } else if (idColPassMap.get(colID).equals(colPass)) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
	}


	/**
	 * @see Database.CollectionsAccessable#getCollectionTitle(String)
	 */
	public String getCollectionTitle(String colID) {
		return idColTitleMap.get(colID);
	}


	/**
	 * @see Database.CollectionsAccessable#getCollectionContents(String)
	 */
	public List<String> getCollectionContents(String colID) {
                if (checkCollection(colID)) {
                    idColViewCountMap.replace(colID, idColViewCountMap.get(colID)+1);
                }
		return idColContentMap.get(colID);
	}


	/**
	 * @see Database.CollectionsAccessable#updateCollectionTitle(String, String, String)
	 */
	public boolean updateCollectionTitle(String colID, String colPass, String newTitle) {
            if (checkCollection(colID, colPass)) {
                idColTitleMap.replace(colID, new String(newTitle));
                return true;
            }
            return false;
	}


	/**
	 * @see Database.CollectionsAccessable#updateCollectionContents(String, String, )
	 */
	public boolean updateCollectionContents(String colID, String colPass, List<String> newContents) {
            if (checkCollection(colID, colPass)) {
                idColContentMap.replace(colID, new ArrayList<String>(newContents));
                return true;
            }
            return false;
	}


	/**
	 * @see Database.CollectionsAccessable#deleteCollection(String, String)
	 */
	public boolean deleteCollection(String colID, String colPass) {
            if (checkCollection(colID, colPass)) {
                idColTitleMap.remove(colID);
                idColContentMap.remove(colID);
                idColPassMap.remove(colID);
                return true;
            }
            return false;
	}

        @Override
        public int getCollectionViews(String colID) {
            if (checkCollection(colID)) {
                return idColViewCountMap.get(colID);
            }
            return -1;
        }

        
        
        
}
