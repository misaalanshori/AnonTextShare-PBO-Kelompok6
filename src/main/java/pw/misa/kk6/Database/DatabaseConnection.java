package pw.misa.kk6.Database;
import java.util.List;
import java.util.ArrayList;

abstract public class DatabaseConnection implements DocumentAccesable, CollectionsAccessable {
        public static void main(String[] args) {
            DatabaseConnection db = new SimpleDB();
            
            // Demo DocumentAccesable
            System.out.println("==DEMO DOCUMENTACCESSABLE==");
            // Membuat dokumen tanpa password
            System.out.println("Membuat dokumen tanpa password");
            String docNoPass = db.createDocument("Hello World", "Hello World is a common test program usually used by programmers to test that their software is working", 1);
            System.out.println("docNoPass ID: " + docNoPass);
            
            // Cek Dokumen dan isinya
            System.out.println("\nCek Dokumen dan isinya");
            System.out.println("docNoPass Exists?: " + db.checkDocument(docNoPass));
            System.out.println("random document Exists?: " + db.checkDocument("1233312"));
            System.out.println("docNoPass password Check?: " + db.checkDocument(docNoPass, null));
            System.out.println("docNoPass Title: " + db.getDocumentTitle(docNoPass));
            System.out.println("docNoPass Text: " + db.getDocumentText(docNoPass));
            
            // Mencoba mengupdate dokumen tanpa password
            System.out.println("\nMencoba mengupdate dokumen tanpa password");
            System.out.println("docNoPass update Title: " + db.updateDocumentTitle(docNoPass, "password", "Goodbye world"));
            System.out.println("docNoPass update Text: " + db.updateDocumentText(docNoPass, "", "Goodbye cruel world"));
            System.out.println("docNoPass Title: " + db.getDocumentTitle(docNoPass));
            System.out.println("docNoPass Text: " + db.getDocumentText(docNoPass));
            
            // Mencoba menghapus dokumen tanpa password
            System.out.println("\nMencoba menghapus dokumen tanpa password");
            System.out.println("docNoPass delete: " + db.deleteDocument(docNoPass, null));
            System.out.println("docNoPass Exists?: " + db.checkDocument(docNoPass));
            System.out.println("docNoPass Title: " + db.getDocumentTitle(docNoPass));
            System.out.println("docNoPass Text: " + db.getDocumentText(docNoPass));
            
            System.out.println("============================================================");

            // Membuat dokumen dengan password
            System.out.println("Membuat dokumen dengan password");
            String docWithPass = db.createDocument("A New Document", "This is a new document with a password", "goodPassword", 1);
            System.out.println("docWithPass ID: " + docWithPass);
            
            // Cek Dokumen dan isinya
            System.out.println("\nCek Dokumen dan isinya");
            System.out.println("docWithPass Exists?: " + db.checkDocument(docWithPass));
            System.out.println("docWithPass password Check?: " + db.checkDocument(docWithPass, "goodPassword"));
            System.out.println("docWithPass wrong password Check?: " + db.checkDocument(docWithPass, "veryBadPassword123"));   
            System.out.println("docWithPass null password Check?: " + db.checkDocument(docWithPass, null));
            System.out.println("docWithPass Title: " + db.getDocumentTitle(docWithPass));
            System.out.println("docWithPass Text: " + db.getDocumentText(docWithPass));
            
            // Mencoba mengupdate dokumen
            System.out.println("\nMencoba mengupdate dokumen");
            System.out.println("dengan password yang salah");
            System.out.println("docWithPass update Title: " + db.updateDocumentTitle(docWithPass, "password", "kena hack"));
            System.out.println("docWithPass update Text: " + db.updateDocumentText(docWithPass, "", "ceritanya ada yang coba hack gitu"));
            System.out.println("docWithPass Title: " + db.getDocumentTitle(docWithPass));
            System.out.println("docWithPass Text: " + db.getDocumentText(docWithPass));
            System.out.println("dengan password yang benar");
            System.out.println("docWithPass update Title: " + db.updateDocumentTitle(docWithPass, "goodPassword", "Halo Dunia"));
            System.out.println("docWithPass update Text: " + db.updateDocumentText(docWithPass, "goodPassword", "Ini adalah isi baru teks dokumen ini"));
            System.out.println("docWithPass Title: " + db.getDocumentTitle(docWithPass));
            System.out.println("docWithPass Text: " + db.getDocumentText(docWithPass));
            
            // Mencoba menghapus dokumen tanpa password
            System.out.println("\nMencoba menghapus dokumen yang dipassword");
            System.out.println("docWithPass delete wrong password: " + db.deleteDocument(docWithPass, null));
            System.out.println("docWithPass Exists?: " + db.checkDocument(docWithPass));
            System.out.println("docWithPass Title: " + db.getDocumentTitle(docWithPass));
            System.out.println("docWithPass Text: " + db.getDocumentText(docWithPass));
            System.out.println("docWithPass delete: " + db.deleteDocument(docWithPass, "goodPassword"));
            System.out.println("docWithPass Exists?: " + db.checkDocument(docWithPass));
            
            // ==============Demo CollectionsAccessable=================
            System.out.println("\n\n==DEMO COLLECTIONSACCESSSABLE==");
            
            // buat ulang docWithPass
            docWithPass = db.createDocument("A New Document", "This is a new document with a password", "goodPassword", 1);
            
            List<String> listDokumen = new ArrayList<String>();
            List<String> isiCollection;
            listDokumen.add(docNoPass);
            listDokumen.add(docWithPass);
            
            // Membuat Collection tanpa password
            System.out.println("Membuat Collection tanpa password baru dengan isi docNoPass dan docWithPass");
            String colNoPass = db.createCollection("2 documents", listDokumen);
            System.out.println("colNoPass ID: " + colNoPass);
            
            // Cek Collections dan Isinya
            System.out.println("\nCek Collections dan Isinya");
            System.out.println("colNoPass exists? " + db.checkCollection(colNoPass));
            System.out.println("random collection exists? " + db.checkCollection("124214312"));
            System.out.println("colNoPass exists with password? " + db.checkCollection(colNoPass, "a"));
            System.out.println("colNoPass title: " + db.getCollectionTitle(colNoPass));
            System.out.println("colNoPass contents: " + db.getCollectionTitle(colNoPass));
            isiCollection = db.getCollectionContents(colNoPass);
            for (int i = 0; i < isiCollection.size(); i++) {
                System.out.printf("%d. %s (%s)\n", i+1, isiCollection.get(i), db.getDocumentTitle(isiCollection.get(i)));
            }
            
            // Mencoba Mengupdate Collections Tanpa Password
            System.out.println("\nMencoba Mengupdate Collections Tanpa Password");
            System.out.println("colNoPass update title: " + db.updateCollectionTitle(colNoPass, null, "3 documents in here"));
            listDokumen.add(db.createDocument("Sebuah Dokumen", "Ini adalah sebuah dokumen", "passwordBagus", 1));
            System.out.println("colNoPass update contents: " + db.updateCollectionContents(colNoPass, null, listDokumen));
            System.out.println("colNoPass contents: " + db.getCollectionTitle(colNoPass));
            isiCollection = db.getCollectionContents(colNoPass);
            for (int i = 0; i < isiCollection.size(); i++) {
                System.out.printf("%d. %s (%s)\n", i+1, isiCollection.get(i), db.getDocumentTitle(isiCollection.get(i)));
            }
            
            // Mencoba Menghapus Collections Tanpa Password
            System.out.println("\nMencoba Menghapus Collections Tanpa Password");
            System.out.println("colNoPass delete: " + db.deleteCollection(colNoPass, ""));
            System.out.println("colNoPass contents: ");
            isiCollection = db.getCollectionContents(colNoPass);
            for (int i = 0; i < isiCollection.size(); i++) {
                System.out.printf("%d. %s (%s)\n", i+1, isiCollection.get(i), db.getDocumentTitle(isiCollection.get(i)));
            }
            
            System.out.println("============================================================");
            
            // Membuat Collections dengan password
            System.out.println("\nMembuat Collections dengan Password");
            String colWithPass = db.createCollection("3 documents", listDokumen, "goodPassword");
            System.out.println("colWithPass ID: " + colWithPass);
            
            // Cek Collections dan Isinya
            System.out.println("\nCek Collections dan Isinya");
            System.out.println("colWithPass exists? " + db.checkCollection(colWithPass));
            System.out.println("colWithPass exists with password? " + db.checkCollection(colWithPass, "goodPassword"));
            System.out.println("colWithPass exists with wrong password? " + db.checkCollection(colWithPass, "a"));
            System.out.println("colWithPass exists with null password? " + db.checkCollection(colWithPass, null));
            System.out.println("colWithPass title: " + db.getCollectionTitle(colWithPass));
            System.out.println("colWithPass contents: " + db.getCollectionTitle(colWithPass));
            isiCollection = db.getCollectionContents(colWithPass);
            for (int i = 0; i < isiCollection.size(); i++) {
                System.out.printf("%d. %s (%s)\n", i+1, isiCollection.get(i), db.getDocumentTitle(isiCollection.get(i)));
            }
            
            // Mencoba Mengupdate Collections
            System.out.println("\nMencoba Mengupdate Collections");
            System.out.println("colWithPass update title null password: " + db.updateCollectionTitle(colWithPass, null, "4 documents in here"));
            System.out.println("colWithPass update title: " + db.updateCollectionTitle(colWithPass, "goodPassword", "4 documents in here"));
            listDokumen.add(db.createDocument("Sebuah Dokumen e2", "Ini adalah sebuah dokumen", "passwordBagus", 1));
            System.out.println("colWithPass update contents: " + db.updateCollectionContents(colWithPass, "goodPassword", listDokumen));
            System.out.println("colWithPass contents: " + db.getCollectionTitle(colWithPass));
            isiCollection = db.getCollectionContents(colWithPass);
            for (int i = 0; i < isiCollection.size(); i++) {
                System.out.printf("%d. %s (%s)\n", i+1, isiCollection.get(i), db.getDocumentTitle(isiCollection.get(i)));
            }
            
            // Mencoba Menghapus Collection
            System.out.println("\nMencoba Menghapus Collections");
            System.out.println("colWithPass delete wrong password: " + db.deleteCollection(colWithPass, "wrongpass"));
            System.out.println("colWithPass exists? " + db.checkCollection(colWithPass));
            System.out.println("colWithPass delete: " + db.deleteCollection(colWithPass, "goodPassword"));
            System.out.println("colWithPass exists? " + db.checkCollection(colWithPass));
        }
        
        protected static String genID() {
            return String.valueOf((long) (Math.random() * 1e16));
        }
        
}
