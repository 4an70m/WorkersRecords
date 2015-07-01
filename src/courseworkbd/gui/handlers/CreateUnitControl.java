/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package courseworkbd.gui.handlers;

import courseworkbd.gui.CreateUnitForm;
import courseworkbd.gui.EmployeeAddForm;
import courseworkbd.gui.utility.FormController;
import courseworkbd.gui.utility.Listener;
import courseworkbd.utility.BDConnectionHandler;
import courseworkbd.utility.StaticID;
import courseworkbd.utility.StaticQueries;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author Admin
 */
public class CreateUnitControl implements Listener, FormController {

    CreateUnitForm unitForm;

    public CreateUnitControl() {
        unitForm = new CreateUnitForm();
        unitForm.addListener(this);
    }

    @Override
    public void actionPerformed(int id) {
        switch (id) {
            case StaticID.enterForm: {
                onEnter();
                break;
            }
            case StaticID.buttonAdd: {
                add();
                break;
            }
            case StaticID.buttonBack: {
                back();
                break;
            }
            default: {
            }
        }
    }

    @Override
    public void show() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                unitForm.setVisible(true);
            }
        });
    }

    @Override
    public void hide() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                unitForm.setVisible(false);
            }
        });
    }

    public void add() {
        BDConnectionHandler connectionHandler = BDConnectionHandler.getInstance();
        connectionHandler.initConnection();
        try {
            PreparedStatement pstmt = connectionHandler.getConnection().prepareStatement(StaticQueries.queryAddUnit);
            pstmt.setInt(1, Integer.parseInt(unitForm.getjTextFieldFullName().getText()));
            pstmt.setInt(2, Integer.parseInt(unitForm.getjTextFieldId().getText()));
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(CreateUnitControl.class.getName()).log(Level.SEVERE, null, ex);
        }

        connectionHandler.closeConnection();
    }

    public void back() {
        hide();
    }

    public void onEnter() {
        BDConnectionHandler connectionHandler = BDConnectionHandler.getInstance();
        connectionHandler.initConnection();
        try {
            Statement stmt = connectionHandler.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(StaticQueries.queryLastUnitNum);
            Integer lastNum = 0;
            if (rs.next()) {
                lastNum = rs.getInt(1);
            }
            lastNum++;
            unitForm.getjTextFieldId().setText(lastNum.toString());
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(CreateUnitControl.class.getName()).log(Level.SEVERE, null, ex);
        }

        connectionHandler.closeConnection();
    }
}
