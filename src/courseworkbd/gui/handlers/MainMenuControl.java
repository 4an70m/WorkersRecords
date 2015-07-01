/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package courseworkbd.gui.handlers;

import courseworkbd.gui.FindForm;
import courseworkbd.gui.MainMenu;
import courseworkbd.gui.utility.FormController;
import courseworkbd.gui.utility.Listener;
import courseworkbd.gui.utility.OutputHelper;
import courseworkbd.utility.BDConnectionHandler;
import courseworkbd.utility.StaticID;
import courseworkbd.utility.StaticQueries;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.JTable;

/**
 *
 * @author Admin
 */
public class MainMenuControl implements Listener, FormController {

    private MainMenu mainMenu;
    private int selectedTab;
    private FindForm fForm;
    private EmployeeAddControl employeeAddControl;
    private EmployeeHireControl employeeHireControl;
    private EmployeeDismissControl employeeDismissControle;
    private AdministrationControl aControl;

    private static final int tabEmplyees = 0;
    private static final int tabEmplyeeCatalogue = 1;
    private static final int tabDepartments = 2;
    private static final int tabUnits = 3;
    private static final int tabStaff = 4;
    private static final int tabPositions = 5;
    private static final int tabHistory = 6;

    private boolean needUpdate[];

    public MainMenuControl() {
        mainMenu = new MainMenu();
        mainMenu.addListener(this);
        selectedTab = 0;
        int tabSize = 7;
        needUpdate = new boolean[tabSize];
        for (int i = 0; i < needUpdate.length; i++) {
            needUpdate[i] = true;
        }
    }

    @Override
    public void actionPerformed(int id) {
        switch (id) {
            case StaticID.enterForm: {
                onUpdate();
                break;
            }
            case StaticID.tabChange: {
                onUpdate();
                break;
            }
            case StaticID.buttonAdministration: {
                onAdministration();
                break;
            }
            case StaticID.buttonAdd: {
                addEmployee();
                break;
            }
            case StaticID.buttonHireEmployee: {
                hireEmployee();
                break;
            }
            case StaticID.buttonDissmissEmployee: {
                dismissEmployee();
                break;
            }
            case StaticID.buttonRefresh: {
                refresh();
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
            case StaticID.findFormEnter: {
                findFormEnter();
                break;
            }
            case StaticID.findFormFind: {
                findFormFind();
                break;
            }
            case StaticID.findFormBack: {
                findFormBack();
                break;
            }
            case StaticID.findFormRemoveFilter: {
                findFormRemoveFilter();
                break;
            }
            case StaticID.buttonLogOff: {
                System.exit(0);
                break;
            }
            case StaticID.buttonPrintMenu: {
                onPrintMenu();
                break;
            }
            case StaticID.buttonPrint: {
                onPrint();
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
                mainMenu.setVisible(true);
            }
        });
    }

    @Override
    public void hide() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                mainMenu.setVisible(false);
            }
        });
    }

    public void addEmployee() {
        if (employeeAddControl == null) {
            employeeAddControl = new EmployeeAddControl();
        }
        employeeAddControl.show();
    }

    public void hireEmployee() {
        if (employeeHireControl == null) {
            employeeHireControl = new EmployeeHireControl();
        }
        employeeHireControl.show();
    }

    private void dismissEmployee() {
        if (employeeDismissControle == null) {
            employeeDismissControle = new EmployeeDismissControl();
        }
        employeeDismissControle.show();
    }

    private void onUpdate() {
        if (!StaticID.admin) {
            mainMenu.getjButtonAdministration().setEnabled(false);
        }
        selectedTab = mainMenu.getjTabbedPane1().getSelectedIndex();
        if (!needUpdate[selectedTab]) {
            return;
        }
        needUpdate[selectedTab] = false;
        BDConnectionHandler connectionHandler = BDConnectionHandler.getInstance();
        connectionHandler.initConnection();
        try {
            Statement stmt = connectionHandler.getConnection().createStatement();
            switch (selectedTab) {
                case tabEmplyees: {//employees
                    ResultSet rs = stmt.executeQuery(StaticQueries.queryBrowseEmployee);
                    OutputHelper.printResultSetToTable(mainMenu.getjTableEmployees(), rs, "i s i b d");
                    rs.close();
                    break;
                }
                case tabEmplyeeCatalogue: {//employee_catalogue
                    ResultSet rs = stmt.executeQuery(StaticQueries.queryHiredEmployees);
                    OutputHelper.printResultSetToTable(mainMenu.getjTableEmployeeCatalogue(), rs, "i s s i s i");
                    rs.close();
                    break;
                }
                case tabDepartments: {//departments
                    ResultSet rs = stmt.executeQuery(StaticQueries.queryBrowseDepartments);
                    OutputHelper.printResultSetToTable(mainMenu.getjTableDepartments(), rs, "s");
                    rs.close();

                    break;
                }
                case tabUnits: {//units
                    ResultSet rs = stmt.executeQuery(StaticQueries.queryBrowseUnits);
                    OutputHelper.printResultSetToTable(mainMenu.getjTableUnits(), rs, "i");
                    rs.close();
                    break;
                }
                case tabStaff: {//staff
                    ResultSet rs = stmt.executeQuery(StaticQueries.queryBrowseStaff);
                    OutputHelper.printResultSetToTable(mainMenu.getjTableStaffCatalogue(), rs, "i s i i");
                    rs.close();
                    break;
                }
                case tabPositions: {//positions
                    ResultSet rs = stmt.executeQuery(StaticQueries.queryBrowsePositions);
                    OutputHelper.printResultSetToTable(mainMenu.getjTablePositions(), rs, "s f");
                    rs.close();
                    break;
                }
                case tabHistory: {//history
                    ResultSet rs = stmt.executeQuery(StaticQueries.queryHiredHistory);
                    OutputHelper.printResultSetToTable(mainMenu.getjTableEmplymentHistory(), rs, "i s s i s b d b d");
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

    private void refresh() {
        for (int i = 0; i < needUpdate.length; i++) {
            needUpdate[i] = true;
        }
        onUpdate();
    }

    private void onTop() {
        JTable table = null;
        selectedTab = mainMenu.getjTabbedPane1().getSelectedIndex();
        switch (selectedTab) {
            case tabEmplyees: {//employees
                table = mainMenu.getjTableEmployees();
                break;
            }
            case tabEmplyeeCatalogue: {//employee_catalogue
                table = mainMenu.getjTableEmployeeCatalogue();
                break;
            }
            case tabDepartments: {//departments
                table = mainMenu.getjTableDepartments();
                break;
            }
            case tabUnits: {//units
                table = mainMenu.getjTableUnits();
                break;
            }
            case tabStaff: {//staff
                table = mainMenu.getjTableStaffCatalogue();
                break;
            }
            case tabPositions: {//positions
                table = mainMenu.getjTablePositions();
                break;
            }
            case tabHistory: {//history
                table = mainMenu.getjTableEmplymentHistory();
                break;
            }
            default: {

            }
        }
        table.changeSelection(0, 0, false, false);
    }

    private void onBottom() {
        JTable table = null;
        selectedTab = mainMenu.getjTabbedPane1().getSelectedIndex();
        switch (selectedTab) {
            case tabEmplyees: {//employees
                table = mainMenu.getjTableEmployees();
                break;
            }
            case tabEmplyeeCatalogue: {//employee_catalogue
                table = mainMenu.getjTableEmployeeCatalogue();
                break;
            }
            case tabDepartments: {//departments
                table = mainMenu.getjTableDepartments();
                break;
            }
            case tabUnits: {//units
                table = mainMenu.getjTableUnits();
                break;
            }
            case tabStaff: {//staff
                table = mainMenu.getjTableStaffCatalogue();
                break;
            }
            case tabPositions: {//positions
                table = mainMenu.getjTablePositions();
                break;
            }
            case tabHistory: {//history
                table = mainMenu.getjTableEmplymentHistory();
                break;
            }
            default: {

            }
        }
        table.changeSelection(table.getRowCount() - 1,
                table.getColumnCount() - 1, false, false);
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

    private void findFormEnter() {
        fForm.getjComboBoxColumn().removeAllItems();
        JTable table = null;
        selectedTab = mainMenu.getjTabbedPane1().getSelectedIndex();
        switch (selectedTab) {
            case tabEmplyees: {//employees
                table = mainMenu.getjTableEmployees();
                break;
            }
            case tabEmplyeeCatalogue: {//employee_catalogue
                table = mainMenu.getjTableEmployeeCatalogue();
                break;
            }
            case tabDepartments: {//departments
                table = mainMenu.getjTableDepartments();
                break;
            }
            case tabUnits: {//units
                table = mainMenu.getjTableUnits();
                break;
            }
            case tabStaff: {//staff
                table = mainMenu.getjTableStaffCatalogue();
                break;
            }
            case tabPositions: {//positions
                table = mainMenu.getjTablePositions();
                break;
            }
            case tabHistory: {//history
                table = mainMenu.getjTableEmplymentHistory();
                break;
            }
            default: {

            }
        }
        for (int i = 0; i < table.getColumnCount(); i++) {
            fForm.getjComboBoxColumn().addItem(table.getColumnName(i));
        }
    }

    private void findFormFind() {
        selectedTab = mainMenu.getjTabbedPane1().getSelectedIndex();
        BDConnectionHandler connectionHandler = BDConnectionHandler.getInstance();
        connectionHandler.initConnection();
        try {
            Statement stmt = connectionHandler.getConnection().createStatement();
            String where = fForm.getjComboBoxColumn().getSelectedItem().toString()
                    + " " + fForm.getjComboBoxSign().getSelectedItem().toString() + " "
                    + OutputHelper.whereArg(fForm.getjTextFieldStatement().getText());
            switch (selectedTab) {
                case tabEmplyees: {//employees
                    String select = "*";
                    String from = "(" + StaticQueries.queryBrowseEmployee + ") ";
                    stmt.executeQuery(StaticQueries.createQuerySelectFromWhere(select, from, where));
                    OutputHelper.printResultSetToTable(mainMenu.getjTableEmployees(), stmt.getResultSet(), "i s i b d");
                    break;
                }
                case tabEmplyeeCatalogue: {//employee_catalogue
                    String select = "e_c.num, e.full_name, d_c.department_name, u_c.unit_code, p_c.position_name, e_c.state_worker";
                    String from = "(" + StaticQueries.queryHiredEmployees + ") ";
                    stmt.executeQuery(StaticQueries.createQuerySelectFromWhere(select, from, where));
                    OutputHelper.printResultSetToTable(mainMenu.getjTableEmployeeCatalogue(), stmt.getResultSet(), "i s s i s i");
                    break;
                }
                case tabDepartments: {//departments
                    String select = "department_name";
                    String from = "(" + StaticQueries.queryBrowseDepartments + ") ";
                    stmt.executeQuery(StaticQueries.createQuerySelectFromWhere(select, from, where));
                    OutputHelper.printResultSetToTable(mainMenu.getjTableDepartments(), stmt.getResultSet(), "s");
                    break;
                }
                case tabUnits: {//units
                    String select = "unit_code";
                    String from = "(" + StaticQueries.queryBrowseUnits + ") ";
                    stmt.executeQuery(StaticQueries.createQuerySelectFromWhere(select, from, where));
                    OutputHelper.printResultSetToTable(mainMenu.getjTableUnits(), stmt.getResultSet(), "i");
                    break;
                }
                case tabStaff: {//staff
                    String select = "uc.unit_code, pc.position_name, sc.current_quantity, sc.quantity";
                    String from = "(" + StaticQueries.queryBrowseStaff + ") ";
                    stmt.executeQuery(StaticQueries.createQuerySelectFromWhere(select, from, where));
                    OutputHelper.printResultSetToTable(mainMenu.getjTableStaffCatalogue(), stmt.getResultSet(), "i s i i");
                    break;
                }
                case tabPositions: {//positions
                    String select = "position_name, salary";
                    String from = "(" + StaticQueries.queryBrowsePositions + ") ";
                    stmt.executeQuery(StaticQueries.createQuerySelectFromWhere(select, from, where));
                    OutputHelper.printResultSetToTable(mainMenu.getjTablePositions(), stmt.getResultSet(), "s f");
                    break;
                }
                case tabHistory: {//history
                    String select = "e_c.num, e.full_name, d_c.department_name, u_c.unit_code, p_c.position_name, "
                            + "e_c.state_worker, e_c.date_of_hire, e_c.dismissed, e_c.date_of_dismiss";
                    String from = "(" + StaticQueries.queryHiredHistory + ") ";
                    stmt.executeQuery(StaticQueries.createQuerySelectFromWhere(select, from, where));
                    OutputHelper.printResultSetToTable(mainMenu.getjTableEmplymentHistory(), stmt.getResultSet(), "i s s i s b d b d");
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

    private void findFormBack() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                fForm.setVisible(false);
            }
        });
    }

    private void findFormRemoveFilter() {
        onUpdate();
    }

    private void onAdministration() {
        if (aControl == null) {
            aControl = new AdministrationControl();
        }
        aControl.show();
    }

    private void onPrintMenu() {
        PrintControl pControl = new PrintControl();
        pControl.show();
    }

    private void onPrint() {
        String headline = null;
        JTable table = null;
        selectedTab = mainMenu.getjTabbedPane1().getSelectedIndex();
        switch (selectedTab) {
            case tabEmplyees: {//employees
                headline = "Emplyee table.";
                table = mainMenu.getjTableEmployees();
                break;
            }
            case tabEmplyeeCatalogue: {//employee_catalogue
                headline = "Emplyee catalogue.";
                table = mainMenu.getjTableEmployeeCatalogue();
                break;
            }
            case tabDepartments: {//departments
                headline = "Departments table.";
                table = mainMenu.getjTableDepartments();
                break;
            }
            case tabUnits: {//units
                headline = "Units table.";
                table = mainMenu.getjTableUnits();
                break;
            }
            case tabStaff: {//staff
                headline = "Staff catalogue.";
                table = mainMenu.getjTableStaffCatalogue();
                break;
            }
            case tabPositions: {//positions
                headline = "Positions catalogue.";
                table = mainMenu.getjTablePositions();
                break;
            }
            case tabHistory: {//history
                headline = "History of employment.";
                table = mainMenu.getjTableEmplymentHistory();
                break;
            }
            default: {

            }
        }
        try {
            OutputHelper.OutputToFile(table, headline);
        } catch (IOException ex) {
            Logger.getLogger(MainMenuControl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
