/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package courseworkbd.gui.handlers;

import courseworkbd.gui.EmployeeDismissForm;
import courseworkbd.gui.FindForm;
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
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/**
 *
 * @author Admin
 */
public class EmployeeDismissControl implements Listener, FormController {

    EmployeeDismissForm formED;
    private FindForm fForm;

    public EmployeeDismissControl() {
        formED = new EmployeeDismissForm();
        formED.addListener(this);
    }

    @Override
    public void actionPerformed(int id) {
        switch (id) {
            case StaticID.enterForm: {
                onEnter();
                break;
            }
            case StaticID.updateForm: {
                onUpdate();
                break;
            }
            case StaticID.buttonDismiss: {
                onDismiss();
                break;
            }
            case StaticID.buttonTop: {
                onTop();
                break;
            }
            case StaticID.buttonBottom: {
                onBottom();
                break;
            }
            case StaticID.buttonFind: {
                onFind();
                break;
            }
            case StaticID.buttonBack: {
                onBack();
                break;
            }
            case StaticID.findFormEnter: {
                findFormEnter();
                break;
            }
            case StaticID.findFormFind: {
                findFormFind();
                break;
            }
            case StaticID.findFormBack: {
                onFormBack();
                break;
            }
            case StaticID.findFormRemoveFilter: {
                onFormRemoveFilter();
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
                formED.setVisible(true);
            }
        });
    }

    @Override
    public void hide() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                formED.setVisible(false);
            }
        });
    }

    private void onEnter() {
        BDConnectionHandler connectionHandler = BDConnectionHandler.getInstance();
        connectionHandler.initConnection();
        try {
            Statement stmt = connectionHandler.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(StaticQueries.queryHiredEmployees);
            OutputHelper.printResultSetToTable(formED.getjTable1(), rs, "i s s i s b");
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeAddControl.class.getName()).log(Level.SEVERE, null, ex);
        }
        connectionHandler.closeConnection();
        formED.getjTable1().getColumnModel().getColumn(0).setPreferredWidth(10);
    }

    private void onUpdate() {
        BDConnectionHandler connectionHandler = BDConnectionHandler.getInstance();
        connectionHandler.initConnection();
        try {
            Statement stmt = connectionHandler.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(StaticQueries.queryHiredEmployees);
            OutputHelper.printResultSetToTable(formED.getjTable1(), rs, "i s s i s b");
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeAddControl.class.getName()).log(Level.SEVERE, null, ex);
        }
        connectionHandler.closeConnection();
        formED.getjTable1().getColumnModel().getColumn(0).setPreferredWidth(10);
    }

    private void onDismiss() {
        int row = formED.getjTable1().getSelectedRow();
        if (row == -1) {
            return;
        }

        //get position in unit of employee by num
        Integer num = Integer.valueOf((String) formED.getjTable1().getValueAt(row, 0));
        int unit_num = 0;
        int pos_code = 0;
        BDConnectionHandler connectionHandler = BDConnectionHandler.getInstance();
        connectionHandler.initConnection();
        try {
            PreparedStatement pstmt = connectionHandler.getConnection().prepareStatement(StaticQueries.queryUnitPosByNum);
            pstmt.setInt(1, num);
            pstmt.executeQuery();
            pstmt.getResultSet().next();
            pos_code = pstmt.getResultSet().getInt(1);
            unit_num = pstmt.getResultSet().getInt(2);
            pstmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeAddControl.class.getName()).log(Level.SEVERE, null, ex);
        }
        connectionHandler.closeConnection();
        Calendar c = new GregorianCalendar();
        String date = c.get(Calendar.YEAR) + "-" + c.get(Calendar.MONTH) + "-" + c.get(Calendar.DAY_OF_MONTH);
        connectionHandler.initConnection();
        try {
            PreparedStatement pstmt = connectionHandler.getConnection().prepareStatement(StaticQueries.queryDismissByNum);
            pstmt.setDate(1, Date.valueOf(date));
            pstmt.setInt(2, num);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeAddControl.class.getName()).log(Level.SEVERE, null, ex);
        }
        connectionHandler.closeConnection();

        //get current_quantitiy of position in unit
        int current_quantity = 0;
        connectionHandler.initConnection();
        try {
            PreparedStatement pstmt = connectionHandler.getConnection().prepareStatement(StaticQueries.queryGetCurrEmployQuantByUnitPos);
            pstmt.setInt(1, unit_num);
            pstmt.setInt(2, pos_code);
            pstmt.executeQuery();
            pstmt.getResultSet().next();
            current_quantity = pstmt.getResultSet().getInt(1);
            pstmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeAddControl.class.getName()).log(Level.SEVERE, null, ex);
        }
        connectionHandler.closeConnection();
        //decrease current_quantitiy of position in unit
        connectionHandler.initConnection();
        try {
            PreparedStatement pstmt = connectionHandler.getConnection().prepareStatement(StaticQueries.queryChangeCurrEmployQuantByUnitPos);
            pstmt.setInt(1, current_quantity - 1);
            pstmt.setInt(2, unit_num);
            pstmt.setInt(3, pos_code);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeAddControl.class.getName()).log(Level.SEVERE, null, ex);
        }
        connectionHandler.closeConnection();
        formED.getjTable1().getColumnModel().getColumn(0).setPreferredWidth(10);
    }

    private void onBottom() {
        formED.getjTable1().changeSelection(formED.getjTable1().getRowCount() - 1,
                formED.getjTable1().getColumnCount() - 1, false, false);
    }

    private void onFind() {
        if (fForm == null) {
            fForm = new FindForm();
            fForm.addListener(this);
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                fForm.setVisible(true);
            }
        });
    }

    private void onBack() {
        hide();
    }

    private void onTop() {
        formED.getjTable1().changeSelection(0, 0, false, false);
    }

    private void findFormEnter() {
        for (int i = 0; i < formED.getjTable1().getColumnCount(); i++) {
            fForm.getjComboBoxColumn().addItem(formED.getjTable1().getColumnName(i));
        }
    }

    private void findFormFind() {
        BDConnectionHandler connectionHandler = BDConnectionHandler.getInstance();
        connectionHandler.initConnection();
        try {
            String select = "e_c.num, e.full_name, d_c.department_name, u_c.unit_code, p_c.position_name, e_c.state_worker";
            String from = "(" + StaticQueries.queryHiredEmployees + ") ";
            String where = fForm.getjComboBoxColumn().getSelectedItem().toString()
                    + " " + fForm.getjComboBoxSign().getSelectedItem().toString() + " " + fForm.getjTextFieldStatement().getText();
            Statement stmt = connectionHandler.getConnection().createStatement();
            stmt.executeQuery(StaticQueries.createQuerySelectFromWhere(select, from, where));
            OutputHelper.printResultSetToTable(formED.getjTable1(), stmt.getResultSet(), "i s s i s b");
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeAddControl.class.getName()).log(Level.SEVERE, null, ex);
        }
        connectionHandler.closeConnection();
        onFormBack();
    }

    private void onFormBack() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                fForm.setVisible(false);
            }
        });
    }

    private void onFormRemoveFilter() {
        onUpdate();
    }

}
