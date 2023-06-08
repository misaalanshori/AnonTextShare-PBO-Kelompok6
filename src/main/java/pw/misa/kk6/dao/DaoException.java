/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pw.misa.kk6.dao;

import java.sql.SQLException;

/**
 *
 * @author Isabu
 */
public class DaoException extends RuntimeException {
    private SQLException sqlException;
    private int errorCode;

    public DaoException(String message) {
        super(message);
        errorCode = 0;
    }
    
    public DaoException(String message, SQLException sqlException) {
        super(message, sqlException);
        this.sqlException = sqlException;
        errorCode = sqlException.getErrorCode();
    }

    public SQLException getSqlException() {
        return sqlException;
    }

    public int getErrorCode() {
        return errorCode;
    }
    
    
    
    

    
}
