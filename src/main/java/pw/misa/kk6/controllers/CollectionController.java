/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pw.misa.kk6.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import pw.misa.kk6.dao.DaoException;
import pw.misa.kk6.dao.TextCollectionDao;
import pw.misa.kk6.dao.TextDocumentDao;
import pw.misa.kk6.models.TextCollection;
import pw.misa.kk6.models.TextDocument;
import pw.misa.kk6.views.BuatKoleksi;
import pw.misa.kk6.views.MainMenu;
import pw.misa.kk6.views.MenuLoadCollection;
import pw.misa.kk6.views.MenuLoadDoc;

public class CollectionController {

    private MainMenu menuAkses;
    private MenuLoadCollection loadCollec;
    private BuatKoleksi buatCollec;
    private MenuLoadDoc loadDoc;
    private TextCollectionDao collectionDao;
    private TextDocumentDao documentDao;
    private List<TextDocument> listDocument;
    private TextCollection loadCollection;
    private TextDocument Document;
    DocumentController dc;

    public CollectionController(MainMenu menuAkses) {
        this.menuAkses = menuAkses;
        this.loadCollec = new MenuLoadCollection(this);
        this.buatCollec = new BuatKoleksi(this);
        this.dc = new DocumentController(menuAkses);
        this.loadDoc = new MenuLoadDoc(dc);
        this.dc.setLoadDoc(this.loadDoc);
        this.collectionDao = new TextCollectionDao();
        this.documentDao = new TextDocumentDao();
        this.listDocument = documentDao.selectLatest(15);
    }

    public void reset() {
        buatCollec.getIsiJudul().setText("");
        buatCollec.getIsiKodeDok().setText("");
        buatCollec.getIsiPassword().setText("");
        menuAkses.getIsiKode().setText("");
        menuAkses.getIsiPasswordAkses().setText("");
        menuAkses.getButtonGroup1().clearSelection();
        loadCollec.getKodeDok().setText("Kode Dokumen");
    }

    public void insertCollec() {
        try {
            TextCollection collection = new TextCollection();

            String title = buatCollec.getIsiJudul().getText().strip();
            List<String> kodeList = Arrays.asList(buatCollec.getIsiKodeDok().getText().split("\\s*,\\s*"));
            String pass = buatCollec.getIsiPassword().getText().strip();
            if (title.isEmpty() || kodeList.isEmpty()) {
                throw new IllegalArgumentException("Harap isi semua komponen yang diperlukan.");
            }
            collection.setTitle(title);
            collection.setPass(pass);

            List<TextDocument> documentList = new ArrayList<>();

            for (String kode : kodeList) {
                TextDocument document = documentDao.select(kode);
                boolean found = false;
                for (TextDocument doc : listDocument) {
                    if (doc.getID().equals(kode)) {
                        found = true;
                        documentList.add(document);
                        break;
                    }
                }
                if (!found) {
                    JOptionPane.showMessageDialog(null, "Dokumen dengan kode " + kode + " tidak ditemukan");
                }
            }

            collection.setContents(documentList);

            if (!documentList.isEmpty()) {
                String generatedID = collectionDao.insert(collection);
                collection.setID(generatedID);
            } else {
                JOptionPane.showMessageDialog(this.buatCollec, "Dokumen tidak ditemukan");
            }

            buatCollec.setVisible(true);

            buatCollec.getIsiKodeBaru().setText(collection.getID());
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this.buatCollec, "Gagal menyimpan koleksi. " + e.getMessage());
        }
    }

    public void updateCollec() {
        try {
            String title = loadCollec.getJudul().getText().strip();

            if (title.isEmpty()) {
                throw new IllegalArgumentException("Harap isi semua komponen yang diperlukan.");
            }

            loadCollection.setTitle(title);
            List<TextDocument> document = loadCollection.getContents();
            String pass = loadCollection.getPass();
            collectionDao.update(loadCollection);
            loadCollection = collectionDao.select(loadCollection.getID());
            if (pass != null) {
                loadCollection.setPass(pass);
            } else {
                JOptionPane.showMessageDialog(this.loadCollec, "Password Kosong!!");
            }
            loadCollection.setContents(document);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(loadCollec, "Gagal memperbarui koleksi. " + e.getMessage());
        }
        loadCollec.setVisible(true);
    }

    public void deleteCollec() {
        int confirm = JOptionPane.showConfirmDialog(this.loadCollec, "Apakah Anda yakin ingin menghapus dokumen?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            collectionDao.delete(this.loadCollection.getID(), this.loadCollection.getPass());
            this.menuAkses.setVisible(true);
            this.loadCollec.setVisible(false);
            JOptionPane.showMessageDialog(this.menuAkses, "Koleksi berhasil dihapus.");
        }

    }

    public void deleteDoc() {
        TextDocument selected = documentDao.select(this.loadCollec.getKodeDok().getText().strip());

        int confirm = JOptionPane.showConfirmDialog(this.loadCollec, "Apakah Anda yakin ingin menghapus dokumen?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String pass = loadCollection.getPass();
            collectionDao.deleteDocument(loadCollection, selected);
            JOptionPane.showMessageDialog(this.loadCollec, "Dokumen berhasil dihapus.");
            loadCollection = collectionDao.select(loadCollection.getID());
            loadCollection.setPass(pass);

            isiList();
        }

    }

    public void addDoc() {
        TextDocument selected = documentDao.select(this.loadCollec.getKodeDok().getText().strip());

        String pass = loadCollection.getPass();
        collectionDao.insertDocument(loadCollection, selected);
        loadCollection = collectionDao.select(loadCollection.getID());
        loadCollection.setPass(pass);

        isiList();
    }

    public void isiList() {
        DefaultListModel<String> documentListModel = new DefaultListModel<>();
        for (int i = 0; i < loadCollection.getContents().size(); i++) {
            documentListModel.add(i, this.loadCollection.getContents().get(i).getTitle());
        }
        loadCollec.getListDokumen().setModel(documentListModel);
    }

    public void aksesCollec() {
        try{
            TextCollection selected = collectionDao.select(menuAkses.getIsiKode().getText().strip());
        
        if (selected == null) {
            JOptionPane.showMessageDialog(this.menuAkses, "Koleksi dengan kode " + menuAkses.getIsiKode().getText().strip() + " tidak ditemukan");
            return;
        }
        
        selected.setPass(selected.getPass());
        System.out.println(selected.getPass());
        String password = menuAkses.getIsiPasswordAkses().getText().strip();
        System.out.println(selected.getID());
        String collectionPassword = selected.getPass();
        if (collectionPassword == null || !collectionPassword.equals(password)) {
            JOptionPane.showMessageDialog(this.menuAkses, "Password salah");
            return;
        }

        if ("".equals(selected.getPass())) {
            loadCollec.getPerbarui().setEnabled(false);
            loadCollec.getHapus().setEnabled(false);
            loadCollec.getHapusKoleksi().setEnabled(false);
            loadCollec.getTambahkan().setEnabled(false);
        } else {
            loadCollec.getPerbarui().setEnabled(true);
            loadCollec.getHapus().setEnabled(true);
            loadCollec.getHapusKoleksi().setEnabled(true);
            loadCollec.getTambahkan().setEnabled(true);
        }

        loadCollec.getJudul().setText(selected.getTitle());
        loadCollec.getIsiKodeKoleksi().setText(selected.getID());

        this.loadCollection = selected;

        isiList();

        loadCollec.setVisible(true);
        menuAkses.setVisible(false);
        }catch(DaoException de){
            JOptionPane.showMessageDialog(menuAkses, "Koleksi tidak ditemukan");
        }
    }

    public void loadDocument(int idx) {
        TextDocument selected = documentDao.select(this.loadCollection.getContents().get(idx).getID());
        selected.setPass(selected.getPass());

        loadDoc.getJudul().setText(selected.getTitle());
        loadDoc.getIsiDok().setText(selected.getText());
        loadDoc.getIsiTotalAkses().setText(Integer.toString(selected.getViewCount()));
        loadDoc.getIsiKodeDok().setText(selected.getID());

        loadDoc.getPerbarui().setEnabled(false);
        loadDoc.getHapus().setEnabled(false);

        if (selected.getVisibility() == 1) {
            loadDoc.getButtonGroup1().setSelected(loadDoc.getPublic().getModel(), true);
        } else {
            if (selected.getVisibility() == 0) {
                loadDoc.getButtonGroup1().setSelected(loadDoc.getUnlisted().getModel(), true);
            }
        }

        this.Document = selected;
        DefaultListModel<String> commentListModel = new DefaultListModel<>();
        for (int i = 0; i < this.Document.getComments().size(); i++) {
            commentListModel.add(i, this.Document.getComments().get(i).getName() + ": " + this.Document.getComments().get(i).getText());
        }
        this.loadDoc.getjList1().setModel(commentListModel);
        this.dc.setLoadDocument(selected);
        loadDoc.setVisible(true);
        loadCollec.setVisible(false);

    }

    public void isiLatesDocument() {
        DefaultTableModel tabelDocument = new DefaultTableModel();
        List<TextDocument> tempListDoc = this.listDocument;
        this.listDocument = documentDao.selectLatest(tempListDoc.size());
        tabelDocument.addColumn("ID");
        tabelDocument.addColumn("Title");
        tabelDocument.addColumn("View Count");
        for (TextDocument document : listDocument) {
            if (document.getVisibility() == 1) {
                Object[] rowData = {document.getID(), document.getTitle(), document.getViewCount()};
                tabelDocument.addRow(rowData);
            }
        }

        buatCollec.getTabelDokumenTerbaru1().setModel(tabelDocument);
    }

    public void addDocLatesDocument(int row) {
        TextDocument document = documentDao.select(listDocument.get(row).getID());

        String currentText = buatCollec.getIsiKodeDok().getText().strip();
        String updateText = currentText + ", " + document.getID();

        if ("".equals(currentText)) {
            buatCollec.getIsiKodeDok().setText(document.getID());
        } else {
            buatCollec.getIsiKodeDok().setText(updateText);
        }
    }

    public void kembali() {
        if (loadCollec.isVisible() == true) {
            loadCollec.setVisible(false);
            menuAkses.setVisible(true);
        } else if (loadDoc.isVisible() == true) {
            loadDoc.setVisible(false);
            loadCollec.setVisible(true);
        } else if (buatCollec.isVisible() == true) {
            buatCollec.setVisible(false);
            menuAkses.setVisible(true);
        }
        reset();
        buatCollec.getIsiKodeBaru().setText("Tidak Perlu Diisi!!");
    }

    public void buatCollec() {
        menuAkses.setVisible(false);
        buatCollec.setVisible(true);
        reset();
        buatCollec.getIsiKodeBaru().setText("Tidak Perlu Diisi!!");
    }
}
