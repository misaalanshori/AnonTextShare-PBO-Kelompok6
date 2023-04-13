package pw.misa.kk6.UserInterface;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import pw.misa.kk6.TextDocument.Document;
import pw.misa.kk6.TextCollection.Collection;
import java.util.Scanner;

public class TextInterface extends AppInterface {
        Scanner masuk = new Scanner(System.in);
        String pass;

    public TextInterface(pw.misa.kk6.Database.DatabaseConnection DatabaseConnection) {
        super(DatabaseConnection);
    }
	public void startUI() {
            this.showMainMenu();
	}

	public void showMainMenu() {
            System.out.println("Hello World!");
            System.out.println("Masukan Nomor Pilihan anda ");
            System.out.println("1. Create Document         ");
            System.out.println("2. Load Document           ");
            System.out.println("3. Create Collection       ");
            System.out.println("4. Load Collection         ");
            System.out.println("5. Exit                    ");
            
            int input = masuk.nextInt();
            if (input == 1){
                this.CreateDocument();
            }else if(input == 2){
                this.LoadDocument();
            }else if(input == 3){
                this.CreateCollection();
            }else if(input == 4){
                this.LoadCollection();
            }else if(input == 5){
                System.out.println("Selesai");
            }
	}

	public void CreateDocument() {
            this.CurrentText = new Document(DatabaseConnection);        
            System.out.println("Masukan judul: ");
            String input = masuk.nextLine();
            this.CurrentText.DocumentTitle = input;
            
            System.out.println("Masukan teks: ");
            String input1 = masuk.nextLine();
            this.CurrentText.DocumentText = input1;
            this.DatabaseConnection.createDocument(input, input1);
            
            System.out.println("Masukan password: ");
            pass = masuk.nextLine();
            if(" " != pass){
                this.CurrentText.loadPassword(pass);
                this.DatabaseConnection.createDocument(input, input1, pass);
            }
            System.out.println("new document code : " + this.CurrentText.getDocumentID());
            
	}

	public void LoadDocument() {
            System.out.println("Masukan kode dokumen: ");
            String input = masuk.nextLine();
            if(this.CurrentText.getDocumentID() == null ? input != null : !this.CurrentText.getDocumentID().equals(input)){
                System.out.println("kode salah");
            }
            System.out.println("Masukan password dokumen: ");
            pass = masuk.nextLine();
            if(null == pass || " " == pass)
            {
                this.ViewDocument();
            }
            if(this.CurrentText.isReadOnly() != false){
                this.EditDocument();
            }
        }
        
 
	public void ViewDocument() {
            System.out.println("judul : " + this.CurrentText.DocumentTitle);
            System.out.println("isi   : " + this.CurrentText.DocumentText);
            this.LoadDocument();
	}

	public void EditDocument() {
            this.CurrentText = new Document(DatabaseConnection);
            System.out.println("judul : " + this.CurrentText.DocumentTitle);
            System.out.println("isi   : " + this.CurrentText.DocumentText);
            System.out.println("apakah anda ingin mengganti judul? (y/n) ");
            String input = masuk.nextLine();
            if(input.toLowerCase() == "y"){
                String judul = masuk.next();
                this.CurrentText.DocumentTitle = judul;
            }
            System.out.println("apakah anda ingin mengganti isi? (y/n) ");
            String input1 = masuk.nextLine();
            if(input1.toLowerCase() == "y"){
                String isi = masuk.next();
                this.CurrentText.DocumentText = isi;
            }
            this.CurrentText.reload();
            System.out.println("apakah anda ingin menyimpan dokumen? (y/n) ");
            String input3 = masuk.nextLine();
            if(input3.toLowerCase() == "y"){
                this.CurrentText.save();
            }
            System.out.println("apakah anda ingin menghapus dokumen? (y/n) ");
            String input4 = masuk.nextLine();
            if(input4.toLowerCase() == "y"){
                this.CurrentText.loadPassword(pass);
                this.DatabaseConnection.deleteDocument(this.CurrentText.getDocumentID(), pass);
                this.CurrentText = null;
            }
            this.showMainMenu();
	}

	public void CreateCollection() {
            this.CurrentCollection = new Collection(DatabaseConnection);
            System.out.println("Masukan judul: ");
            String input = masuk.nextLine();
            this.CurrentCollection.CollectionTitle = input;
            
             System.out.println("Masukan dokumen: ");
             ArrayList<String> list = new ArrayList<String>();
             String listq = masuk.nextLine();
             while(listq != " " || listq != null){
                list.add(listq);
                if(" " != pass){
                    this.DatabaseConnection.createCollection(input, list);
                }
                this.CurrentCollection.loadPassword(pass);
                this.DatabaseConnection.createCollection(input, list, pass);
             }
             System.out.println("new collection code : " + this.CurrentCollection.getCollectionID());
             
            
	}

	public void LoadCollection() {
            System.out.println("Masukan kode dokumen: ");
            String input = masuk.nextLine();
            if(this.CurrentCollection.getCollectionID() != input){
                System.out.println("kode salah");
            }
            System.out.println("Masukan password collection: ");
            pass = masuk.nextLine();
            if(null == pass || " " == pass)
            {
                this.ViewCollection();
            }
            if(this.CurrentCollection.isReadOnly() != false){
                this.EditCollection();
            }
            
	}

	public void ViewCollection() {
            System.out.println("judul : " + this.CurrentCollection.CollectionTitle);
            System.out.println("isi   : " + this.CurrentCollection.CollectionContents);
            this.LoadDocument();
	}

	public void EditCollection() {
            this.CurrentCollection = new Collection(DatabaseConnection);
            System.out.println("judul : " + this.CurrentCollection.CollectionTitle);
            System.out.println("isi   : " + this.CurrentCollection.CollectionContents);
            System.out.println("apakah anda ingin mengganti judul? (y/n) ");
            String input = masuk.nextLine();
            if(input.toLowerCase() == "y"){
                String judul = masuk.next();
                this.CurrentCollection.CollectionTitle = judul;
            }
            System.out.println("apakah anda ingin mengganti isi? (y/n) ");
            String input1 = masuk.nextLine();
            if(input1.toLowerCase() == "y"){
                ArrayList<String> list = new ArrayList<String>();
                String listq = masuk.nextLine();
                while(listq != " " || listq != null){
                    list.add(listq);
                    this.CurrentCollection.loadPassword(pass);
                    this.DatabaseConnection.createCollection(this.CurrentCollection.CollectionTitle, list, pass);
                }
            }
            this.CurrentCollection.reload();
            System.out.println("apakah anda ingin menyimpan koleksi? (y/n) ");
            String input3 = masuk.nextLine();
            if(input3.toLowerCase() == "y"){
                this.CurrentCollection.save();
            }
            System.out.println("apakah anda ingin menghapus koleksi? (y/n) ");
            String input4 = masuk.nextLine();
            if(input4.toLowerCase() == "y"){
                this.CurrentCollection.loadPassword(pass);
                this.DatabaseConnection.deleteCollection(this.CurrentCollection.getCollectionID(), pass);
                this.CurrentCollection = null;
            }
            this.showMainMenu();
	}

}
