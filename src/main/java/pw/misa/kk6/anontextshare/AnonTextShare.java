/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package pw.misa.kk6.anontextshare;
import pw.misa.kk6.UserInterface.*;
import pw.misa.kk6.Database.SimpleDB;
/**
 *
 * @author Isabu
 */
public class AnonTextShare {

    public static void main(String[] args) {
        AppInterface AppUI = new TextInterface(new SimpleDB());
        AppUI.startUI();
    }
}
