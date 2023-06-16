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
    private List<TextCollection> listCollection;
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

    public void insertCollec() {
        try {
            TextCollection collection = new TextCollection();

            String title = buatCollec.getIsiJudul().getText();
            List<String> kodeList = Arrays.asList(buatCollec.getIsiKodeDok().getText().split("\\s*,\\s*"));
            String pass = buatCollec.getIsiPassword().getText();
            if (title.isEmpty() || kodeList.isEmpty()) {
                throw new IllegalArgumentException("Harap isi semua komponen yang diperlukan.");
            }
            collection.setTitle(title);
            collection.setPass(pass);

            List<TextDocument> documentList = new ArrayList<>();

            for (String kode : kodeList) {
                TextDocument document = new TextDocument();
                document.setID(kode);
                if (listDocument.contains(document.getID())) {
                    documentList.add(document);
                } else {
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

            buatCollec.getIsiJudul().setText("");
            buatCollec.getIsiKodeDok().setText("");
            buatCollec.getIsiPassword().setText("");

            buatCollec.setVisible(true);

            buatCollec.getIsiKodeBaru().setText(collection.getID());
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this.buatCollec, "Gagal menyimpan koleksi. " + e.getMessage());
        }
    }

    public void updateCollec() {
        try {
            String title = loadCollec.getJudul().getText();

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
        loadCollec.getKodeDok().setText("Kode Dokumen");

    }

    public void addDoc() {
        TextDocument selected = documentDao.select(this.loadCollec.getKodeDok().getText().strip());

        String pass = loadCollection.getPass();
        collectionDao.insertDocument(loadCollection, selected);
        loadCollection = collectionDao.select(loadCollection.getID());
        loadCollection.setPass(pass);

        isiList();

        loadCollec.getKodeDok().setText("Kode Dokumen");

    }

    public void isiList() {
        DefaultListModel<String> documentListModel = new DefaultListModel<>();
        for (int i = 0; i < loadCollection.getContents().size(); i++) {
            documentListModel.add(i, this.loadCollection.getContents().get(i).getID());
        }
        loadCollec.getListDokumen().setModel(documentListModel);
    }

    public void aksesCollec() {
        TextCollection selected = collectionDao.select(menuAkses.getIsiKode().getText().strip());
        selected.setPass(menuAkses.getIsiPasswordAkses().getText());

        loadCollec.getJudul().setText(selected.getTitle());

        this.loadCollection = selected;

        isiList();

        menuAkses.getIsiKode().setText("");
        menuAkses.getIsiPasswordAkses().setText("");
        menuAkses.getButtonGroup1().clearSelection();

        loadCollec.setVisible(true);
        menuAkses.setVisible(false);

    }

    public void loadDocument(int idx) {
        TextDocument selected = documentDao.select(this.loadCollection.getContents().get(idx).getID());
        selected.setPass(selected.getPass());

        loadDoc.getJudul().setText(selected.getTitle());
        loadDoc.getIsiDokumen().setText(selected.getText());
        loadDoc.getIsiTotalAkses().setText(Integer.toString(selected.getViewCount()));

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

        loadDoc.setVisible(true);
        loadCollec.setVisible(false);

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
    }

    public void buatCollec() {
        menuAkses.setVisible(false);
        buatCollec.setVisible(true);
    }
}
