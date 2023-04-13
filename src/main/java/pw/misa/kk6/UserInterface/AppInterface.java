package pw.misa.kk6.UserInterface;

import pw.misa.kk6.TextDocument.Document;
import pw.misa.kk6.TextCollection.Collection;
import pw.misa.kk6.Database.DatabaseConnection;

abstract public class AppInterface {

	protected Document CurrentText;

	protected Collection CurrentCollection;

	protected DatabaseConnection DatabaseConnection;

	public AppInterface(DatabaseConnection DatabaseConnection) {
            this.DatabaseConnection = DatabaseConnection;
	}

	public abstract void startUI();

}
