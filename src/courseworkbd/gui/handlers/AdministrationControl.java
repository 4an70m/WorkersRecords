/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package courseworkbd.gui.handlers;

import courseworkbd.gui.AdministrationMenu;
import courseworkbd.gui.CreateDepartmentForm;
import courseworkbd.gui.EditForm;
import courseworkbd.gui.FindForm;
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
import javax.swing.JTable;
import javax.swing.SwingUtilities;

/**
 *
 * @author Admin
 */
public class AdministrationControl implements Listener, FormController {

    private AdministrationMenu adminMenu;
    private EditForm eForm;
    private EditFormStaffControl eFormS;
    private int selectedTab;
    private FindForm fForm;
    private static final int tabDepartments = 0;
    private static final int tabUnits = 1;
    private static final int tabStaff = 3;
    private static final int tabPositions = 2;
    private Object[] property;
    private boolean needUpdate[];
    private String[] units_code;
    private String[] positions_code;
    private String[] departments_code;
    private String[][] staff_items;
    int selectedRow = -1;

    public AdministrationControl() {
        adminMenu = new AdministrationMenu();
        adminMenu.addListener(this);
        selectedTab = 0;
        int tabSize = 4;
        needUpdate = new boolean[tabSize];
        for (int i = 0; i < needUpdate.length; i++) {
            needUpdate[i] = true;
        }
    }

    @Override
    public void actionPerformed(int id) {

        switch (id) {
            case StaticID.enterForm: {
                onEnter();
                break;
            }
            case StaticID.tabChange: {
                onUpdate();
                break;
            }
            case StaticID.buttonAdd: {
                onEditEnter();
                onAdd();
                break;
            }
            case StaticID.buttonTop: {
                onTop();
                break;
            }
            case StaticID.buttonEdit: {
                onEdit();
                break;
            }
            case StaticID.buttonBottom: {
                onBottom();
                break;
            }
            case StaticID.buttonDelete: {
                onDelete();
                break;
            }
            case StaticID.buttonFind: {
                onFind();
                break;
            }
            case StaticID.buttonRefresh: {
                onRefresh();
                break;
            }
            case StaticID.findFormBack: {
                onFindFormBack();
                break;
            }
            case StaticID.findFormEnter: {
                onFindFormEnter();
                break;
            }
            case StaticID.findFormFind: {
                onFindFormFind();
                break;
            }
            case StaticID.findFormRemoveFilter: {
                onFindFormFilter();
                break;
            }
            case StaticID.buttonBack: {
                onBack();
                break;
            }
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
                adminMenu.setVisible(true);
            }
        });
    }

    @Override
    public void hide() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                adminMenu.setVisible(false);
            }
        });
    }

    private void onAdd() {
        selectedTab = adminMenu.getjTabbedPaneTables().getSelectedIndex();
        switch (selectedTab) {
            case tabDepartments: {//departments
                CreateDepartmentControl cForm = new CreateDepartmentControl();
                cForm.show();
                break;
            }
            case tabUnits: {//units
                CreateUnitControl cForm = new CreateUnitControl();
                cForm.show();
                break;
            }
            case tabStaff: {//staff
                CreateStaffControl cForm = new CreateStaffControl();
                cForm.show();
                break;
            }
            case tabPositions: {//positions
                CreatePositionControl cForm = new CreatePositionControl();
                cForm.show();
                break;
            }
            default: {
                return;
            }
        }
    }

    private void onEdit() {

        selectedTab = adminMenu.getjTabbedPaneTables().getSelectedIndex();
        JTable table = null;
        switch (selectedTab) {
            case tabDepartments: {//departments
                table = adminMenu.getjTableDepartments();
                break;
            }
            case tabUnits: {//units
                table = adminMenu.getjTableUnits();
                break;
            }
            case tabStaff: {//staff
                table = adminMenu.getjTableStaff();
                break;
            }
            case tabPositions: {//positions
                table = adminMenu.getjTablePositions();
                break;
            }
            default: {
                return;
            }
        }
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(adminMenu, "Select row to edit.");
            return;
        }
        if (selectedTab == tabStaff) {
            if (eFormS == null) {
                eFormS = new EditFormStaffControl();
            }
            eFormS.show();
            eFormS.setSelectedRow(selectedRow);
            eFormS.setSelectedTable(adminMenu.getjTableStaff().getModel().getValueAt(selectedRow, 0),
                    adminMenu.getjTableStaff().getModel().getValueAt(selectedRow, 1),
                    adminMenu.getjTableStaff().getModel().getValueAt(selectedRow, 2),
                    adminMenu.getjTableStaff().getModel().getValueAt(selectedRow, 3));

            return;
        }

        if (eForm == null) {
            eForm = new EditForm();
            eForm.addListener(this);
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                eForm.setVisible(true);
            }
        });
    }

    private void onTop() {
        JTable table = null;
        selectedTab = adminMenu.getjTabbedPaneTables().getSelectedIndex();
        switch (selectedTab) {
            case tabDepartments: {//departments
                table = adminMenu.getjTableDepartments();
                break;
            }
            case tabUnits: {//units
                table = adminMenu.getjTableUnits();
                break;
            }
            case tabStaff: {//staff
                table = adminMenu.getjTableStaff();
                break;
            }
            case tabPositions: {//positions
                table = adminMenu.getjTablePositions();
                break;
            }
            default: {

            }
        }
        table.changeSelection(0, 0, false, false);
    }

    private void onBottom() {
        JTable table = null;
        selectedTab = adminMenu.getjTabbedPaneTables().getSelectedIndex();
        switch (selectedTab) {
            case tabDepartments: {//departments
                table = adminMenu.getjTableDepartments();
                break;
            }
            case tabUnits: {//units
                table = adminMenu.getjTableUnits();
                break;
            }
            case tabStaff: {//staff
                table = adminMenu.getjTableStaff();
                break;
            }
            case tabPositions: {//positions
                table = adminMenu.getjTablePositions();
                break;
            }
            default: {

            }
        }
        table.changeSelection(table.getRowCount() - 1,
                table.getColumnCount() - 1, false, false);
    }

    private void onDelete() {
        boolean allowDelete = false;
        selectedTab = adminMenu.getjTabbedPaneTables().getSelectedIndex();
        JTable table = null;
        switch (selectedTab) {
            case tabDepartments: {//departments
                table = adminMenu.getjTableDepartments();
                break;
            }
            case tabUnits: {//units
                table = adminMenu.getjTableUnits();
                break;
            }
            case tabStaff: {//staff
                table = adminMenu.getjTableStaff();
                break;
            }
            case tabPositions: {//positions
                table = adminMenu.getjTablePositions();
                break;
            }
            default: {
                return;
            }
        }
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(adminMenu, "Select row to delete.");
            return;
        }

        //check if selected item is used in any other table
        Statement stmt = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BDConnectionHandler connectionHandler = BDConnectionHandler.getInstance();
        connectionHandler.initConnection();
        try {
            switch (selectedTab) {
                case tabDepartments: {//departments
                    pstmt = connectionHandler.getConnection().prepareStatement(StaticQueries.queryCheckDepartmentForDelete);
                    pstmt.setInt(1, Integer.valueOf(departments_code[selectedRow]));
                    rs = pstmt.executeQuery();
                    break;
                }
                case tabUnits: {//units
                    pstmt = connectionHandler.getConnection().prepareStatement(StaticQueries.queryCheckUnitForDelete);
                    pstmt.setInt(1, Integer.valueOf(units_code[selectedRow]));
                    rs = pstmt.executeQuery();
                    break;
                }
                case tabStaff: {//staff
                    pstmt = connectionHandler.getConnection().prepareStatement(StaticQueries.queryCheckStaffForDelete);
                    pstmt.setInt(1, Integer.valueOf(staff_items[0][selectedRow]));
                    pstmt.setInt(2, Integer.valueOf(staff_items[1][selectedRow]));
                    rs = pstmt.executeQuery();
                    break;
                }
                case tabPositions: {//positions
                    pstmt = connectionHandler.getConnection().prepareStatement(StaticQueries.queryCheckPositionForDelete);
                    pstmt.setInt(1, Integer.valueOf(positions_code[selectedRow]));
                    rs = pstmt.executeQuery();
                    break;
                }
                default: {
                    return;
                }
            }
            rs.next();
            if (rs.getInt(1) == 0) {
                allowDelete = true;
            }
            rs.close();
            connectionHandler.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeAddControl.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (!allowDelete) {
            JOptionPane.showMessageDialog(adminMenu, "Selected item is bound with another items.");
            return;
        }
        
        //perform delete
        String deleteIn = null;
        String where = null;
        switch (selectedTab) {
            case tabDepartments: {//departments
                deleteIn = "departments_catalogue";
                where = "department_code = " + departments_code[selectedRow];
                break;
            }
            case tabUnits: {//units
                deleteIn = "units_catalogue";
                where = "unit_num = " + units_code[selectedRow];
                break;
            }
            case tabStaff: {//staff
                deleteIn = "staff_catalogue";
                where = "unit_num = " + OutputHelper.whereArg(staff_items[0][selectedRow])
                        + "AND position_code = " + OutputHelper.whereArg(staff_items[1][selectedRow]);
                break;
            }
            case tabPositions: {//positions
                deleteIn = "positions_catalogue";
                where = "position_code = " + positions_code[selectedRow];
                break;
            }
            default: {
                return;
            }
        }
        connectionHandler.initConnection();
        try {
            stmt = connectionHandler.getConnection().createStatement();
            String del = StaticQueries.createDeleteWhatInWhere(deleteIn, where);
            stmt.executeUpdate(del);
            stmt.close();
            connectionHandler.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeAddControl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void onFind() {
        needUpdate[selectedTab] = true;
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

    private void onFindFormBack() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                fForm.setVisible(false);
            }
        });
    }

    private void onFindFormEnter() {

        fForm.getjComboBoxColumn().removeAllItems();
        JTable table = null;
        selectedTab = adminMenu.getjTabbedPaneTables().getSelectedIndex();
        switch (selectedTab) {
            case tabDepartments: {//departments
                table = adminMenu.getjTableDepartments();
                break;
            }
            case tabUnits: {//units
                table = adminMenu.getjTableUnits();
                break;
            }
            case tabStaff: {//staff
                table = adminMenu.getjTableStaff();
                break;
            }
            case tabPositions: {//positions
                table = adminMenu.getjTablePositions();
                break;
            }
            default: {
                return;
            }
        }
        for (int i = 0; i < table.getColumnCount(); i++) {
            fForm.getjComboBoxColumn().addItem(table.getColumnName(i));
        }
    }

    private void onFindFormFind() {
        selectedTab = adminMenu.getjTabbedPaneTables().getSelectedIndex();
        BDConnectionHandler connectionHandler = BDConnectionHandler.getInstance();
        connectionHandler.initConnection();
        try {
            Statement stmt = connectionHandler.getConnection().createStatement();
            String where = fForm.getjComboBoxColumn().getSelectedItem().toString()
                    + " " + fForm.getjComboBoxSign().getSelectedItem().toString() + " "
                    + OutputHelper.whereArg(fForm.getjTextFieldStatement().getText());
            switch (selectedTab) {
                case tabDepartments: {//departments
                    String select = "department_name";
                    String from = "(" + StaticQueries.queryDepartmentNames + ") ";
                    stmt.executeQuery(StaticQueries.createQuerySelectFromWhere(select, from, where));
                    OutputHelper.printResultSetToTable(adminMenu.getjTableDepartments(), stmt.getResultSet(), "s i");
                    break;
                }
                case tabUnits: {//units
                    String select = "unit_code";
                    String from = "(" + StaticQueries.queryUnitCodes + ") ";
                    stmt.executeQuery(StaticQueries.createQuerySelectFromWhere(select, from, where));
                    OutputHelper.printResultSetToTable(adminMenu.getjTableUnits(), stmt.getResultSet(), "i i");
                    break;
                }
                case tabStaff: {//staff
                    String select = "uc.unit_code, pc.position_name, sc.current_quantity, sc.quantity";
                    String from = "(" + StaticQueries.queryBrowseStaff + ") ";
                    stmt.executeQuery(StaticQueries.createQuerySelectFromWhere(select, from, where));
                    OutputHelper.printResultSetToTable(adminMenu.getjTableStaff(), stmt.getResultSet(), "i s i i");
                    break;
                }
                case tabPositions: {//positions
                    String select = "position_name, salary";
                    String from = "(" + StaticQueries.queryPositions + ") ";
                    stmt.executeQuery(StaticQueries.createQuerySelectFromWhere(select, from, where));
                    OutputHelper.printResultSetToTable(adminMenu.getjTablePositions(), stmt.getResultSet(), "s f i");
                    break;
                }
                default: {
                    return;
                }
            }
            stmt.close();
            connectionHandler.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeAddControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void onFindFormFilter() {
        onUpdate();
    }

    private void onEnter() {
        onUpdate();
    }

    private void onUpdate() {

        selectedTab = adminMenu.getjTabbedPaneTables().getSelectedIndex();
        if (!needUpdate[selectedTab]) {
            return;
        }
        needUpdate[selectedTab] = false;
        BDConnectionHandler connectionHandler = BDConnectionHandler.getInstance();
        connectionHandler.initConnection();
        try {
            Statement stmt = connectionHandler.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            switch (selectedTab) {
                case tabDepartments: {//departments
                    ResultSet rs = stmt.executeQuery(StaticQueries.queryDepartmentNames);
                    OutputHelper.printResultSetToTable(adminMenu.getjTableDepartments(), rs, "s i");
                    rs.beforeFirst();
                    departments_code = new String[adminMenu.getjTableDepartments().getRowCount()];
                    OutputHelper.printResultToArray(departments_code, rs, "i", 2);
                    rs.close();
                    break;
                }
                case tabUnits: {//units
                    ResultSet rs = stmt.executeQuery(StaticQueries.queryUnitCodes);
                    OutputHelper.printResultSetToTable(adminMenu.getjTableUnits(), rs, "i i");
                    rs.beforeFirst();
                    units_code = new String[adminMenu.getjTableUnits().getRowCount()];
                    OutputHelper.printResultToArray(units_code, rs, "i", 2);
                    rs.close();
                    break;
                }
                case tabPositions: {//positions
                    ResultSet rs = stmt.executeQuery(StaticQueries.queryPositions);
                    OutputHelper.printResultSetToTable(adminMenu.getjTablePositions(), rs, "s f i");
                    rs.beforeFirst();
                    positions_code = new String[adminMenu.getjTablePositions().getRowCount()];
                    OutputHelper.printResultToArray(positions_code, rs, "i", 3);
                    rs.close();
                    break;
                }
                case tabStaff: {//staff
                    ResultSet rs = stmt.executeQuery(StaticQueries.queryBrowseStaff);
                    OutputHelper.printResultSetToTable(adminMenu.getjTableStaff(), rs, "i s i i");
                    rs.close();
                    staff_items = new String[2][adminMenu.getjTableStaff().getRowCount()];
                    rs = stmt.executeQuery(StaticQueries.queryBrowseStaffForAdminControl);
                    OutputHelper.printResultToArray2(staff_items, rs, "i i");
                    rs.close();
                    break;
                }
                default: {

                }
            }
            stmt.close();

            connectionHandler.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeAddControl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void onBack() {
        this.hide();
    }

    private void onEditBack() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                eForm.setVisible(false);
            }
        });
    }

    private void onEditSave() {

        String updateIn;
        String values;
        String where;
        switch (selectedTab) {
            case tabDepartments: {//departments
                updateIn = "departments_catalogue";
                values = "department_name = " + OutputHelper.whereArg(eForm.getjTableEditingRow().getValueAt(0, 0).toString());
                where = "department_code = " + departments_code[selectedRow];
                break;
            }
            case tabUnits: {//units
                updateIn = "units_catalogue";
                values = "unit_code = " + OutputHelper.whereArg(eForm.getjTableEditingRow().getValueAt(0, 0).toString());
                where = "unit_num = " + units_code[selectedRow];
                break;
            }
            case tabPositions: {//positions
                updateIn = "positions_catalogue";
                values = "position_name = " + OutputHelper.whereArg(eForm.getjTableEditingRow().getValueAt(0, 0).toString())
                        + ", salary = " + OutputHelper.whereArg(eForm.getjTableEditingRow().getValueAt(0, 1).toString());
                where = "position_code = " + positions_code[selectedRow];
                break;
            }
            default: {
                return;
            }
        }
        //run insert query
        BDConnectionHandler connectionHandler = BDConnectionHandler.getInstance();
        connectionHandler.initConnection();
        try {
            Statement stmt = connectionHandler.getConnection().createStatement();
            stmt.executeUpdate(StaticQueries.createUpdateWhatInWhere(updateIn, values, where));
            stmt.close();
            connectionHandler.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeAddControl.class.getName()).log(Level.SEVERE, null, ex);
        }
        JOptionPane.showMessageDialog(eForm, "Row was sucefully upadted.");
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                eForm.setVisible(false);
            }
        });
        onRefresh();
    }

    private void onEditEnter() {
        selectedTab = adminMenu.getjTabbedPaneTables().getSelectedIndex();
        JTable table = null;
        switch (selectedTab) {
            case tabDepartments: {//departments
                table = adminMenu.getjTableDepartments();
                break;
            }
            case tabUnits: {//units
                table = adminMenu.getjTableUnits();
                break;
            }
            case tabPositions: {//positions
                table = adminMenu.getjTablePositions();
                break;
            }
            default: {
                return;
            }
        }
        selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }
        eForm.getjLabelFormName().setText(table.getName());
        eForm.getjTableEditingRow()
                .setModel(OutputHelper.insertSelectedRowToTable(table, selectedRow));
    }

    private void onRefresh() {
        for (int i = 0; i < needUpdate.length; i++) {
            needUpdate[i] = true;
        }
        onUpdate();
    }
}
