/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package courseworkbd.gui.handlers;

import courseworkbd.gui.CreateStaffForm;
import courseworkbd.gui.EmployeeAddForm;
import courseworkbd.gui.utility.FormController;
import courseworkbd.gui.utility.Listener;
import courseworkbd.gui.utility.OutputHelper;
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
public class CreateStaffControl implements Listener, FormController {

    private CreateStaffForm staffForm;
    private String[] units_code;
    private String[] positions_code;
    private int currentUnitCode = 0;
    private int currentPositionCode = 0;

    public CreateStaffControl() {
        staffForm = new CreateStaffForm();
        staffForm.addListener(this);

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
                staffForm.setVisible(true);
            }
        });
    }

    @Override
    public void hide() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                staffForm.setVisible(false);
            }
        });
    }

    public void add() {
        BDConnectionHandler connectionHandler = BDConnectionHandler.getInstance();
        connectionHandler.initConnection();
        try {
            PreparedStatement pstmt = connectionHandler.getConnection().prepareStatement(StaticQueries.queryAddStaff);
            pstmt.setInt(1, Integer.valueOf(positions_code[staffForm.getjComboBoxPosition().getSelectedIndex()]));
            pstmt.setInt(2, Integer.parseInt(units_code[staffForm.getjComboBoxUnit().getSelectedIndex()]));
            pstmt.setInt(3,0); 
            pstmt.setInt(4,Integer.parseInt(staffForm.getjTextFieldQuant().getText()));
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(CreateStaffControl.class.getName()).log(Level.SEVERE, null, ex);
        }

        connectionHandler.closeConnection();
        JOptionPane.showMessageDialog(staffForm, "Employee was added.");
    }

    public void back() {
        hide();
    }

    public void onEnter() {
        BDConnectionHandler connectionHandler = BDConnectionHandler.getInstance();
        connectionHandler.initConnection();
        try {
            staffForm.getjComboBoxUnit().removeAllItems();
            staffForm.getjComboBoxPosition().removeAllItems();

            Statement stmt = connectionHandler.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery(StaticQueries.queryUnitCodes);
            OutputHelper.printResultToListBox(staffForm.getjComboBoxUnit(), rs, "i");
            rs.beforeFirst();
            //get unit code into an array, so we will be able to operate codes, which we dont get in our query
            units_code = new String[staffForm.getjComboBoxUnit().getItemCount()];
            OutputHelper.printResultToArray(units_code, rs, "i", 2);
            //getting the current unit code
            currentUnitCode = Integer.valueOf(units_code[staffForm.getjComboBoxUnit().getSelectedIndex()]);
            rs.close();
            stmt.close();

            //same as unit, but for position
            stmt = connectionHandler.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(StaticQueries.queryPositionNames);
            OutputHelper.printResultToListBox(staffForm.getjComboBoxPosition(), rs, "s");
            rs.beforeFirst();
            positions_code = new String[staffForm.getjComboBoxPosition().getItemCount()];
            OutputHelper.printResultToArray(positions_code, rs, "i", 2);
            currentPositionCode = Integer.valueOf(positions_code[staffForm.getjComboBoxPosition().getSelectedIndex()]);
            rs.close();
            stmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(EmployeeAddControl.class.getName()).log(Level.SEVERE, null, ex);
        }
        connectionHandler.closeConnection();
        
        staffForm.getjLabelCurQuant().setText("0");
        staffForm.getjTextFieldQuant().setText("0");

    }
}
