/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package courseworkbd.gui.handlers;

import courseworkbd.gui.EditFormStaff;
import courseworkbd.gui.utility.FormController;
import courseworkbd.gui.utility.Listener;
import courseworkbd.gui.utility.OutputHelper;
import courseworkbd.utility.BDConnectionHandler;
import courseworkbd.utility.StaticID;
import courseworkbd.utility.StaticQueries;
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
public class EditFormStaffControl implements Listener, FormController {

    private EditFormStaff eForm;
    private int selectedRow;
    private Object[] obj;
    private String[] units_code;
    private String[] positions_code;
    private int currentUnitCode = 0;
    private int currentPositionCode = 0;

    public EditFormStaffControl() {
        eForm = new EditFormStaff();
        eForm.addListener(this);
        obj = new Object[4];
    }

    @Override
    public void actionPerformed(int id) {
        switch (id) {
            case StaticID.editFormEnter: {
                onEditEnter();
                break;
            }
            case StaticID.editFormSave: {
                onEditSave();
                break;
            }
            case StaticID.editFormBack: {
                onEditBack();
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
                eForm.setVisible(true);
            }
        });
    }

    @Override
    public void hide() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                eForm.setVisible(false);
            }
        });
    }

    private void onEditEnter() {
        BDConnectionHandler connectionHandler = BDConnectionHandler.getInstance();
        connectionHandler.initConnection();
        try {
            eForm.getjComboBoxUnit().removeAllItems();
            eForm.getjComboBoxPosition().removeAllItems();

            Statement stmt = connectionHandler.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery(StaticQueries.queryUnitCodes);
            OutputHelper.printResultToListBox(eForm.getjComboBoxUnit(), rs, "i");
            rs.beforeFirst();
            //get unit code into an array, so we will be able to operate codes, which we dont get in our query
            units_code = new String[eForm.getjComboBoxUnit().getItemCount()];
            OutputHelper.printResultToArray(units_code, rs, "i", 2);
            eForm.getjComboBoxUnit().setSelectedItem(obj[0]);
            //getting the current unit code
            currentUnitCode = Integer.valueOf(units_code[eForm.getjComboBoxUnit().getSelectedIndex()]);
            rs.close();
            stmt.close();

            //same as unit, but for position
            stmt = connectionHandler.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(StaticQueries.queryPositionNames);
            OutputHelper.printResultToListBox(eForm.getjComboBoxPosition(), rs, "s");
            rs.beforeFirst();
            positions_code = new String[eForm.getjComboBoxPosition().getItemCount()];
            OutputHelper.printResultToArray(positions_code, rs, "i", 2);
            eForm.getjComboBoxPosition().setSelectedItem(obj[1]);
            currentPositionCode = Integer.valueOf(positions_code[eForm.getjComboBoxPosition().getSelectedIndex()]);
            rs.close();
            stmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(EmployeeAddControl.class.getName()).log(Level.SEVERE, null, ex);
        }
        connectionHandler.closeConnection();

        eForm.getjLabelCurQuant().setText(obj[2].toString());
        eForm.getjTextFieldQuant().setText(obj[3].toString());
    }

    private void onEditSave() {

        //additional info for inserting
        int newQuantity = Integer.valueOf(eForm.getjTextFieldQuant().getText());
        //check if new quantity is less then existing quantity
        if (newQuantity < Integer.valueOf(obj[2].toString())) {
            JOptionPane.showMessageDialog(eForm, "New quantity is less then current quantity.");
            return;
        }
        int newUnitCode = Integer.valueOf(units_code[eForm.getjComboBoxUnit().getSelectedIndex()]);
        int newPositionCode = Integer.valueOf(units_code[eForm.getjComboBoxPosition().getSelectedIndex()]);

        BDConnectionHandler connectionHandler = BDConnectionHandler.getInstance();
        connectionHandler.initConnection();
        try {
            PreparedStatement pstmt = connectionHandler.getConnection().prepareStatement(StaticQueries.upadteStaff);
            pstmt.setInt(1, newUnitCode);
            pstmt.setInt(2, newPositionCode);
            pstmt.setInt(3, newQuantity);
            pstmt.setInt(4, currentUnitCode);
            pstmt.setInt(5, currentPositionCode);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeAddControl.class.getName()).log(Level.SEVERE, null, ex);
        }
        connectionHandler.closeConnection();
        JOptionPane.showMessageDialog(eForm, "Row was sucefully updated.");
        this.hide();

    }

    private void onEditBack() {
        hide();
    }

    public void setSelectedRow(int selectedRow) {
        this.selectedRow = selectedRow;
    }

    public void setSelectedTable(Object unit, Object pos, Object curQant, Object quant) {
        obj[0] = unit;
        obj[1] = pos;
        obj[2] = curQant;
        obj[3] = quant;
    }
}
