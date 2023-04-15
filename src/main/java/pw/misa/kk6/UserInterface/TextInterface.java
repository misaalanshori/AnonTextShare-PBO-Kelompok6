package pw.misa.kk6.UserInterface;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import pw.misa.kk6.AnonymousText.Document;
import pw.misa.kk6.AnonymousText.Collection;
import java.util.Scanner;
import pw.misa.kk6.Database.DatabaseConnection;

public class TextInterface extends AppInterface {
        Scanner masuk = new Scanner(System.in);

    public TextInterface(DatabaseConnection DatabaseConnection) {
        super(DatabaseConnection);
    }
	public void startUI() {
            this.showMainMenu();
	}

	public void showMainMenu() {
            
            
            boolean running = true;
            while (running) {
                System.out.println("Anonymous Text Sharing!");
                System.out.println("Masukan Nomor Pilihan anda ");
                System.out.println("1. Create Document         ");
                System.out.println("2. Load Document           ");
                System.out.println("3. Create Collection       ");
                System.out.println("4. Load Collection         ");
                System.out.println("5. Exit                    ");
                System.out.print("Pilihan> ");
                int input = masuk.nextInt();
                masuk.nextLine();
                switch (input) {
                    case 1:
                        this.CreateDocument();
                        break;
                    case 2:
                        this.LoadDocument();
                        break;
                    case 3:
                        this.CreateCollection();
                        break;
                    case 4:
                        this.LoadCollection();
                        break;
                    case 5:
                        System.out.println("Selesai");
                        running = false;
                        break;
                    default:
                        break;
                }
            }
            
	}

	public void CreateDocument() {
            this.CurrentText = new Document(DatabaseConnection);        
            System.out.print("Masukan judul: ");
            String input = masuk.nextLine();
            this.CurrentText.DocumentTitle = input;
            
            System.out.print("Masukan teks: ");
            String input1 = masuk.nextLine();
            this.CurrentText.DocumentText = input1;
            
            
            System.out.print("Masukan password: ");
            String dPass = masuk.nextLine();
            if(dPass != null && !dPass.isEmpty()){
                this.CurrentText.loadPassword(dPass);
            }
            this.CurrentText.save();
            System.out.println("New document code : " + this.CurrentText.getDocumentID());
            
	}

	public void LoadDocument() {
            System.out.print("Masukan kode dokumen: ");
            String input = masuk.nextLine().trim();
            if(input == null || !this.DatabaseConnection.checkDocument(input)){
                System.out.println("kode salah");
                return;
            }
            
            this.CurrentText = new Document(DatabaseConnection, input);

            System.out.print("Masukan password dokumen: ");
            String dPass = masuk.nextLine();
            if (dPass != null && !dPass.isEmpty()) {
                this.CurrentText.loadPassword(dPass);
            }
            
            

            
            boolean running = true;
            while (running) {
                System.out.printf("Dokumen: %s (Views: %d)\n", this.CurrentText.DocumentTitle, this.CurrentText.getViewCount());
                System.out.println("1. View Document");
                System.out.println("2. Edit Document");
                System.out.println("3. Delete Document");
                System.out.println("4. Exit");
                System.out.print("Pilihan> ");
                int pilihan = masuk.nextInt();
                masuk.nextLine();
                switch (pilihan) {
                    case 1:
                        this.ViewDocument();
                        break;
                    case 2:
                        if (!this.CurrentText.isReadOnly()) {
                            this.EditDocument();
                        } else {
                            System.out.println("Document Read-Only");
                        }
                        
                        break;
                    case 3:
                        if (!this.CurrentText.isReadOnly()) {
                            System.out.print("Hapus? (y/n) ");
                            input = masuk.nextLine();
                            if (input.toLowerCase().equals("y")) {
                                DatabaseConnection.deleteDocument(this.CurrentText.getDocumentID(), dPass);
                                System.out.println("Dokumen terhapus");
                                running = false;
                            }
                        } else {
                            System.out.println("Document Read-Only");
                        }
                        break;
                    case 4:
                        System.out.println("Kembali ke Main Menu");
                        running = false;
                        break;
                    default:
                        break;
                }
            }
        }
        
 
	public void ViewDocument() {
            System.out.println("Judul\t: " + this.CurrentText.DocumentTitle);
            System.out.println("Isi\t: " + this.CurrentText.DocumentText);
	}

	public void EditDocument() {
            ViewDocument();
            System.out.print("apakah anda ingin mengganti judul? (y/n) ");
            String input = masuk.nextLine();
            if(input.toLowerCase().equals("y")){
                System.out.print("Judul Baru: ");
                String judul = masuk.nextLine();
                this.CurrentText.DocumentTitle = judul;
            }
            System.out.print("apakah anda ingin mengganti isi? (y/n) ");
            input = masuk.nextLine();
            if(input.toLowerCase().equals("y")){
                System.out.print("Isi baru: ");
                String isi = masuk.nextLine();
                this.CurrentText.DocumentText = isi;
            }
            
            System.out.print("apakah anda ingin menyimpan dokumen? (y/n) ");
            input = masuk.nextLine();
            if(input.toLowerCase().equals("y")){
                this.CurrentText.save();
            }
            this.CurrentText.reload();
            System.out.println("Dokumen akhir:");
            ViewDocument();
	}

	public void CreateCollection() {
            this.CurrentCollection = new Collection(DatabaseConnection);
            System.out.print("Masukan judul: ");
            String input = masuk.nextLine();
            this.CurrentCollection.CollectionTitle = input;
            
            
            ArrayList<Document> list = new ArrayList<Document>();
            System.out.print("Masukan dokumen: ");
            String listq = masuk.nextLine().trim();
            while(listq != null && !listq.isEmpty()){
                if (this.DatabaseConnection.checkDocument(listq)) {
                    list.add(new Document(DatabaseConnection, listq));
                } else {
                    System.out.println("Document Code Invalid");
                }
                System.out.print("Masukan dokumen: ");
                listq = masuk.nextLine().trim();
            }
            this.CurrentCollection.CollectionContents = list;
            System.out.print("Masukan password: ");
            String cPass = masuk.nextLine();
            if (cPass != null && ! cPass.isEmpty()) {
                    this.CurrentCollection.loadPassword(cPass);
            }
            this.CurrentCollection.save();
            System.out.println("new collection code : " + this.CurrentCollection.getCollectionID());
             
            
	}

	public void LoadCollection() {
            System.out.print("Masukan kode collection: ");
            String input = masuk.nextLine().trim();
            if(input == null || !this.DatabaseConnection.checkCollection(input)){
                System.out.println("kode salah");
                return;
            }
            this.CurrentCollection = new Collection(DatabaseConnection, input);
            
            System.out.print("Masukan password collection: ");
            String cPass = masuk.nextLine();
            if (cPass != null && ! cPass.isEmpty()) {
                    this.CurrentCollection.loadPassword(cPass);
            }
            
            boolean running = true;
            
            while (running) {
                System.out.printf("Collection: %s (Views: %d)\n", this.CurrentCollection.CollectionTitle, this.CurrentCollection.getViewCount());
                System.out.println("1. View Collection");
                System.out.println("2. Edit Collection");
                System.out.println("3. Delete Collection");
                System.out.println("4. Exit");
                System.out.print("Pilihan> ");
                int pilihan = masuk.nextInt();
                masuk.nextLine();
                switch (pilihan) {
                    case 1:
                        this.ViewCollection();
                        break;
                    case 2:
                        if (!this.CurrentCollection.isReadOnly()) {
                            this.EditCollection();
                        } else {
                            System.out.println("Collection Read-Only");
                        }
                        
                        break;
                    case 3:
                        if (!this.CurrentCollection.isReadOnly()) {
                            System.out.print("Hapus? (y/n) ");
                            input = masuk.nextLine().trim();
                            if (input.toLowerCase().equals("y")) {
                                DatabaseConnection.deleteCollection(this.CurrentCollection.getCollectionID(), cPass);
                                System.out.println("Collection terhapus");
                                running = false;
                            }
                        } else {
                            System.out.println("Collection Read-Only");
                        }
                        break;
                    case 4:
                        System.out.println("Kembali ke Main Menu");
                        running = false;
                        break;
                    default:
                        break;
                }
            }
            
	}

	public void ViewCollection() {
            System.out.println("judul : " + this.CurrentCollection.CollectionTitle);
            System.out.println("isi   : ");
            for (int i = 0; i < this.CurrentCollection.CollectionContents.size(); i++) {
                Document doc = this.CurrentCollection.CollectionContents.get(i);
                System.out.printf("%d. %s (%s) (Views: %d)\n", i+1, doc.DocumentTitle, doc.getDocumentID(), doc.getViewCount());
            }
	}

	public void EditCollection() {
            String input;
            ViewCollection();
            boolean running = true;
            
            while (running) {
                System.out.println("Collection: " + this.CurrentCollection.CollectionTitle);
                System.out.println("1. Edit Title");
                System.out.println("2. Add Document");
                System.out.println("3. Remove Document");
                System.out.println("4. Exit");
                System.out.print("Pilihan> ");
                int pilihan = masuk.nextInt();
                masuk.nextLine();
                switch (pilihan) {
                    case 1:
                        System.out.print("Input New Title: ");
                        input = masuk.nextLine();
                        this.CurrentCollection.CollectionTitle = input;
                        this.CurrentCollection.save();
                        
                        break;
                    case 2:
                        System.out.print("Input New Document: ");
                        input = masuk.nextLine().trim();
                        if (this.DatabaseConnection.checkDocument(input)) {
                            Document doc = new Document(DatabaseConnection, input);
                            this.CurrentCollection.CollectionContents.add(doc);
                            this.CurrentCollection.save();
                        }
                        
                        break;
                    case 3:
                        System.out.print("Delete Document: ");
                        input = masuk.nextLine().trim();
                        for (int i = 0; i < this.CurrentCollection.CollectionContents.size() && i >= 0; i++) {
                            if (this.CurrentCollection.CollectionContents.get(i).getDocumentID().equals(input)) {
                                System.out.println("Delete " + this.CurrentCollection.CollectionContents.get(i).DocumentTitle);
                                this.CurrentCollection.CollectionContents.remove(i);
                                this.CurrentCollection.save();
                                i = -1;
                            }
                        }
                        
                        break;
                    case 4:
                        System.out.println("Kembali ke Main Menu");
                        running = false;
                        break;
                    default:
                        break;
                }
            }
	}

}
