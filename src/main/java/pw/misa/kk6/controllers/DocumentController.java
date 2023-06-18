package pw.misa.kk6.controllers;

import java.util.List;
import javax.swing.ButtonModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import pw.misa.kk6.dao.TextDocumentDao;
import pw.misa.kk6.models.TextDocument;
import pw.misa.kk6.views.BuatDokumen;
import pw.misa.kk6.views.MainMenu;
import pw.misa.kk6.views.MenuLoadCollection;
import pw.misa.kk6.views.MenuLoadDoc;

public class DocumentController {

    private TextDocumentDao documentDao;
    private MainMenu menuAkses;
    private MenuLoadDoc loadDoc;
    private MenuLoadCollection loadCollec;
    private BuatDokumen buatDoc;
    private List<TextDocument> listDocument;
    private TextDocument loadDocument;

    public DocumentController(MainMenu menuAkses) {
        this.menuAkses = menuAkses;
        this.buatDoc = new BuatDokumen(this);
        this.loadDoc = new MenuLoadDoc(this);
        this.documentDao = new TextDocumentDao();
        this.listDocument = documentDao.selectLatest(15);
        this.loadDocument = new TextDocument();
    }

    public void setLoadDoc(MenuLoadDoc loadDoc) {
        this.loadDoc = loadDoc;
    }

    public void setLoadDocument(TextDocument loadDocument) {
        this.loadDocument = loadDocument;
    }

    public void reset() {
        buatDoc.getIsiJudul().setText("");
        buatDoc.getIsiPassword().setText("");
        buatDoc.getIsiDok().setText("");
        buatDoc.getButtonGroup1().clearSelection();
        loadDoc.getNama().setText("Nama");
        loadDoc.getIsi().setText("Isi");
        menuAkses.getIsiKode().setText("");
        menuAkses.getIsiPasswordAkses().setText("");
        menuAkses.getButtonGroup1().clearSelection();
    }

    public void insertDocument() {
        try {
            TextDocument document = new TextDocument();

            String title = this.buatDoc.getIsiJudul().getText().strip();
            String text = this.buatDoc.getIsiDok().getText().strip();
            ButtonModel selectedButton = this.buatDoc.getButtonGroup1().getSelection();
            String pass = this.buatDoc.getIsiPassword().getText().strip();

            // Periksa apakah ada komponen yang kosong
            if (title.isEmpty() || text.isEmpty() || selectedButton == null) {
                throw new IllegalArgumentException("Harap isi semua komponen yang diperlukan.");
            }

            // Set nilai dokumen berdasarkan komponen yang ada
            document.setTitle(title);
            document.setText(text);

            if (this.buatDoc.getUnlisted().isSelected()) {
                document.setVisibility(0);
            } else if (this.buatDoc.getPublic().isSelected()) {
                document.setVisibility(1);
            }
            document.setPass(pass);
            String generatedID = documentDao.insert(document);
            document.setID(generatedID);
            listDocument.add(document);

            this.loadDocument = document;
            buatDoc.setVisible(true);
            menuAkses.setVisible(false);

            buatDoc.getIsiKodeBaru().setText(document.getID());
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this.buatDoc, "Gagal menyimpan dokumen. " + e.getMessage());
        }
    }

    public void updateDocument() {
        try {

            String title = loadDoc.getJudul().getText().strip();
            String text = loadDoc.getIsiDok().getText().strip();
            ButtonModel selectedButton = loadDoc.getButtonGroup1().getSelection();

            // Periksa apakah ada komponen yang kosong atau input tidak valid
            if (title.isEmpty() || text.isEmpty() || selectedButton == null) {
                throw new IllegalArgumentException("Harap isi semua komponen yang diperlukan.");
            }

            // Set nilai dokumen berdasarkan komponen yang ada
            loadDocument.setTitle(title);
            loadDocument.setText(text);

            if (this.loadDoc.getPublic().isSelected()) {
                loadDocument.setVisibility(1);
            } else if (this.loadDoc.getUnlisted().isSelected()) {
                loadDocument.setVisibility(0);
            }

            List<TextDocument.Comment> comment = loadDocument.getComments();
            String pass = loadDocument.getPass();
            documentDao.update(loadDocument);
            loadDocument = documentDao.select(loadDocument.getID());
            loadDoc.getIsiTotalAkses().setText(Integer.toString(loadDocument.getViewCount()));
            if (pass != null) {
                loadDocument.setPass(pass);
            } else {
                JOptionPane.showMessageDialog(this.loadDoc, "Password Kosong!!");
            }
            loadDocument.setComments(comment);

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this.loadDoc, "Gagal memperbarui dokumen. " + e.getMessage());
        }
        loadDoc.setVisible(true);
    }

    public void deleteDocument() {
        int confirm = JOptionPane.showConfirmDialog(this.loadDoc, "Apakah Anda yakin ingin menghapus dokumen?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            documentDao.delete(loadDocument.getID(), loadDocument.getPass());
            this.menuAkses.setVisible(true);
            this.loadDoc.setVisible(false);
            JOptionPane.showMessageDialog(this.menuAkses, "Dokumen berhasil dihapus.");
        }
    }

    public void insertComment() {
        try {
            String nama = loadDoc.getNama().getText().strip();
            String isiComment = loadDoc.getIsi().getText().strip();

            // Periksa apakah ada komponen yang kosong atau input tidak valid
            if (nama.isEmpty() || isiComment.isEmpty()) {
                throw new IllegalArgumentException("Harap isi semua komponen yang diperlukan.");
            }

            TextDocument.Comment comment = loadDocument.new Comment(nama, isiComment);
            String pass = loadDocument.getPass();
            documentDao.insertComment(loadDocument.getID(), comment);
            loadDocument = documentDao.select(loadDocument.getID());
            loadDocument.setPass(pass);

            isiComment();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this.loadDoc, "Gagal memperbarui comment. " + e.getMessage());
        }
        loadDoc.setVisible(true);
    }

    public void isiComment() {
        DefaultListModel<String> commentListModel = new DefaultListModel<>();
        for (TextDocument.Comment comment : loadDocument.getComments()) {
            String commentText = comment.getName() + ": " + comment.getText();
            commentListModel.addElement(commentText);
        }
        loadDoc.getjList1().setModel(commentListModel);
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

        menuAkses.getTabelDokumenTerbaru().setModel(tabelDocument);
    }

    public void aksesDoc() {
        TextDocument selected = documentDao.select(menuAkses.getIsiKode().getText().strip());
        selected.setPass(menuAkses.getIsiPasswordAkses().getText().strip());

        loadDoc.getJudul().setText(selected.getTitle());
        loadDoc.getIsiDok().setText(selected.getText());
        loadDoc.getIsiTotalAkses().setText(Integer.toString(selected.getViewCount()));
        loadDoc.getIsiKodeDok().setText(selected.getID());

        if (selected.getVisibility() == 1) {
            loadDoc.getButtonGroup1().setSelected(loadDoc.getPublic().getModel(), true);
        } else {
            if (selected.getVisibility() == 0) {
                loadDoc.getButtonGroup1().setSelected(loadDoc.getUnlisted().getModel(), true);
            }
        }

        if ("".equals(selected.getPass())) {
            loadDoc.getPerbarui().setEnabled(false);
            loadDoc.getHapus().setEnabled(false);
        } else {
            loadDoc.getPerbarui().setEnabled(true);
            loadDoc.getHapus().setEnabled(true);
        }

        this.loadDocument = selected;

        isiComment();

        loadDoc.setVisible(true);
        menuAkses.setVisible(false);

    }

    public void isiField(int row) {
        TextDocument document = documentDao.select(listDocument.get(row).getID());

        loadDoc.getJudul().setText(document.getTitle());
        loadDoc.getIsiDok().setText(document.getText());
        loadDoc.getIsiTotalAkses().setText(Integer.toString(document.getViewCount()));
        loadDoc.getIsiKodeDok().setText(document.getID());

        loadDoc.getPerbarui().setEnabled(false);
        loadDoc.getHapus().setEnabled(false);

        if (document.getVisibility() == 1) {
            loadDoc.getButtonGroup1().setSelected(loadDoc.getPublic().getModel(), true);
        } else {
            if (document.getVisibility() == 0) {
                loadDoc.getButtonGroup1().setSelected(loadDoc.getUnlisted().getModel(), true);
            }
        }

        this.loadDocument = document;

        isiComment();

        loadDoc.setVisible(true);
        menuAkses.setVisible(false);
    }

    public void kembali() {
        if (loadDoc.isVisible() == true) {
            loadDoc.setVisible(false);
            menuAkses.setVisible(true);
        } else if (buatDoc.isVisible() == true) {
            buatDoc.setVisible(false);
            menuAkses.setVisible(true);
        }
        reset();
        buatDoc.getIsiKodeBaru().setText("Tidak Perlu Diisi!!");
    }

    public void buatDoc() {
        menuAkses.setVisible(false);
        buatDoc.setVisible(true);
        reset();
        buatDoc.getIsiKodeBaru().setText("Tidak Perlu Diisi!!");
    }
}
