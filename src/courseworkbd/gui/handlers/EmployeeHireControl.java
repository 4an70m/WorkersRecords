/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package courseworkbd.gui.handlers;

import courseworkbd.gui.utility.OutputHelper;
import courseworkbd.gui.EmployeeHireForm;
import courseworkbd.gui.FindForm;
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
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author Admin
 */
public class EmployeeHireControl implements Listener, FormController {

    private EmployeeHireForm employeeHireForm;
    private String[] departments_code;
    private String[] units_code;
    private String[] positions_code;
    FindForm fForm;

    public EmployeeHireControl() {
        employeeHireForm = new EmployeeHireForm();
        employeeHireForm.addListener(this);
    }

    @Override
    public void actionPerformed(int id) {
        switch (id) {
            case StaticID.buttonAdd: {
                add();
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
            case StaticID.buttonBack: {
                onBack();
                break;
            }
            case StaticID.buttonFind: {
                onFind();
                break;
            }
            case StaticID.enterForm: {
                onEnter();
                break;
            }
            case StaticID.updateForm: {
                onUpdate();
                break;
            }
            case StaticID.findFormFind: {
                onFormFind();
                break;
            }
            case StaticID.findFormEnter: {
                onFormEnter();
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
                employeeHireForm.setVisible(true);
            }
        });
    }

    @Override
    public void hide() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                employeeHireForm.setVisible(false);
            }
        });
    }

    public void add() {
        //get selected employee
        int row = employeeHireForm.getjTable1().getSelectedRow();
        if (row == -1) {
            return;
        }
        //num
        Integer num = Integer.valueOf((String) employeeHireForm.getjTable1().getValueAt(row, 0));
        //find department_code
        Integer dep_code = Integer.valueOf(departments_code[employeeHireForm.getjComboBoxDepratments().getSelectedIndex()]);
        //find position_code
        Integer pos_code = Integer.valueOf(positions_code[employeeHireForm.getjComboBoxPosition().getSelectedIndex()]);
        //find unit_num
        Integer unit_num = Integer.valueOf(units_code[employeeHireForm.getjComboBoxUnits().getSelectedIndex()]);
//check if there are free positions in staff_catalogue
        boolean hasAvailablePositions = true;
        BDConnectionHandler connectionHandler = BDConnectionHandler.getInstance();
        connectionHandler.initConnection();
        try {
            PreparedStatement pstmt = connectionHandler.getConnection().prepareStatement(StaticQueries.queryFindFreePlaceByUnitPos);
            pstmt.setInt(1, unit_num);
            pstmt.setInt(2, pos_code);
            pstmt.executeQuery();
            if (!pstmt.getResultSet().next()) {
                hasAvailablePositions = false;
                JOptionPane.showMessageDialog(fForm, "All positions in this unit are currently occupied.");
            }
            pstmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeAddControl.class.getName()).log(Level.SEVERE, null, ex);
        }
        connectionHandler.closeConnection();
        if (!hasAvailablePositions) {
            return;
        }
//if there is - continue
//else return MessageBox() and exit

        //find if state worker
        Boolean state = employeeHireForm.getjCheckBox1().isSelected();
        //set salary multiplyer
        Double sal_mult = state ? 1 : 0.5;
        //get salary        
        Double salary = 0.0;
        connectionHandler = BDConnectionHandler.getInstance();
        connectionHandler.initConnection();
        try {
            PreparedStatement pstmt = connectionHandler.getConnection().prepareStatement(StaticQueries.queryGetSalaryByCode);
            pstmt.setInt(1, pos_code);
            pstmt.executeQuery();
            pstmt.getResultSet().next();
            salary = pstmt.getResultSet().getDouble(1);
            pstmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeAddControl.class.getName()).log(Level.SEVERE, null, ex);
        }
        connectionHandler.closeConnection();
        salary *= sal_mult;
        //get date of hire
        Calendar c = new GregorianCalendar();
        String date = c.get(Calendar.YEAR) + "-" + c.get(Calendar.MONTH) + "-" + c.get(Calendar.DAY_OF_MONTH);
        //insert that into employee_catalogue
        connectionHandler.initConnection();
        try {
            PreparedStatement pstmt = connectionHandler.getConnection().prepareStatement(StaticQueries.queryAddHiredEmployee);
            pstmt.setInt(1, num);
            pstmt.setInt(2, dep_code);
            pstmt.setInt(3, unit_num);
            pstmt.setInt(4, pos_code);
            pstmt.setBoolean(5, state);
            pstmt.setDouble(6, sal_mult);
            pstmt.setDouble(7, salary);
            pstmt.setDate(8, Date.valueOf(date));
            pstmt.setBoolean(9, false);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeAddControl.class.getName()).log(Level.SEVERE, null, ex);
        }
        connectionHandler.closeConnection();
        //get current_quantity of this position in this unit
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
        //increase current_quantity of this position
        connectionHandler.initConnection();
        try {
            PreparedStatement pstmt = connectionHandler.getConnection().prepareStatement(StaticQueries.queryChangeCurrEmployQuantByUnitPos);
            pstmt.setInt(1, current_quantity + 1);
            pstmt.setInt(2, unit_num);
            pstmt.setInt(3, pos_code);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeAddControl.class.getName()).log(Level.SEVERE, null, ex);
        }
        connectionHandler.closeConnection();

    }

    public void onEnter() {

        BDConnectionHandler connectionHandler = BDConnectionHandler.getInstance();
        connectionHandler.initConnection();
        try {
            employeeHireForm.getjComboBoxDepratments().removeAllItems();
            employeeHireForm.getjComboBoxPosition().removeAllItems();
            employeeHireForm.getjComboBoxUnits().removeAllItems();
            Statement stmt = connectionHandler.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(StaticQueries.queryFreeEmployees);
            OutputHelper.printResultSetToTable(employeeHireForm.getjTable1(), rs, "i s d");
            rs.close();
            stmt.close();

            stmt = connectionHandler.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(StaticQueries.queryPositionNames);
            OutputHelper.printResultToListBox(employeeHireForm.getjComboBoxPosition(), rs, "s");
            rs.beforeFirst();
            positions_code = new String[employeeHireForm.getjComboBoxPosition().getItemCount()];
            OutputHelper.printResultToArray(positions_code, rs, "i", 2);
            rs.close();
            stmt.close();

            stmt = connectionHandler.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(StaticQueries.queryDepartmentNames);
            OutputHelper.printResultToListBox(employeeHireForm.getjComboBoxDepratments(), rs, "s");
            rs.beforeFirst();
            departments_code = new String[employeeHireForm.getjComboBoxDepratments().getItemCount()];
            OutputHelper.printResultToArray(departments_code, rs, "i", 2);
            rs.close();
            stmt.close();

            stmt = connectionHandler.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(StaticQueries.queryUnitCodes);
            OutputHelper.printResultToListBox(employeeHireForm.getjComboBoxUnits(), rs, "i");
            rs.beforeFirst();
            units_code = new String[employeeHireForm.getjComboBoxUnits().getItemCount()];
            OutputHelper.printResultToArray(units_code, rs, "i", 2);
            rs.close();
            stmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(EmployeeAddControl.class.getName()).log(Level.SEVERE, null, ex);
        }

        connectionHandler.closeConnection();
    }

    public void onUpdate() {
        BDConnectionHandler connectionHandler = BDConnectionHandler.getInstance();
        connectionHandler.initConnection();
        try {
            Statement stmt = connectionHandler.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(StaticQueries.queryFreeEmployees);
            OutputHelper.printResultSetToTable(employeeHireForm.getjTable1(), rs, "i s d");
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeAddControl.class.getName()).log(Level.SEVERE, null, ex);
        }

        connectionHandler.closeConnection();
    }

    public void onBack() {
        hide();
    }

    public void onTop() {
        employeeHireForm.getjTable1().changeSelection(0, 0, false, false);
    }

    public void onBottom() {
        employeeHireForm.getjTable1().changeSelection(employeeHireForm.getjTable1().getRowCount() - 1,
                employeeHireForm.getjTable1().getColumnCount() - 1, false, false);
    }

    public void onFind() {
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

    private void onFormBack() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                fForm.setVisible(false);
            }
        });

    }

    private void onFormFind() {

        BDConnectionHandler connectionHandler = BDConnectionHandler.getInstance();
        connectionHandler.initConnection();
        try {
            String select = "num, full_name, enroll_date";
            String from = "(" + StaticQueries.queryFreeEmployees + ") ";
            String where = fForm.getjComboBoxColumn().getSelectedItem().toString()
                    + " " + fForm.getjComboBoxSign().getSelectedItem().toString() + " " + fForm.getjTextFieldStatement().getText();
            Statement stmt = connectionHandler.getConnection().createStatement();
            stmt.executeQuery(StaticQueries.createQuerySelectFromWhere(select, from, where));
            OutputHelper.printResultSetToTable(employeeHireForm.getjTable1(), stmt.getResultSet(), "i s d");
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeAddControl.class.getName()).log(Level.SEVERE, null, ex);
        }
        connectionHandler.closeConnection();
    }

    private void onFormEnter() {
        for (int i = 0; i < employeeHireForm.getjTable1().getColumnCount(); i++) {
            fForm.getjComboBoxColumn().addItem(employeeHireForm.getjTable1().getColumnName(i));
        }
    }

    private void onFormRemoveFilter() {
        onUpdate();
    }
}
