package pw.misa.kk6.UserInterface;

import pw.misa.kk6.AnonymousText.Document;
import pw.misa.kk6.AnonymousText.Collection;
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
