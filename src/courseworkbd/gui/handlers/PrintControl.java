/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package courseworkbd.gui.handlers;

import courseworkbd.gui.PrintForm;
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
import javax.swing.JTable;
import javax.swing.SwingUtilities;

/**
 *
 * @author Admin
 */
public class PrintControl implements Listener, FormController {

    PrintForm enterForm;

    public PrintControl() {
        enterForm = new PrintForm();
        enterForm.addListener(this);
    }

    @Override
    public void actionPerformed(int id) {
        switch (id) {
            case StaticID.printAllDismissedEmployee: {
                onPrint(StaticID.printAllDismissedEmployee);
                break;
            }
            case StaticID.printAllEmployee: {
                onPrint(StaticID.printAllEmployee);
                break;
            }
            case StaticID.printAllHiredEmployee: {
                onPrint(StaticID.printAllHiredEmployee);
                break;
            }
            case StaticID.printAllHiredEmployeeByDate: {
                onPrint(StaticID.printAllHiredEmployeeByDate);
                break;
            }
            case StaticID.printAllHiredEmployeeByUnit: {
                onPrint(StaticID.printAllHiredEmployeeByUnit);
                break;
            }
            case StaticID.printAllPHDEmplyee: {
                onPrint(StaticID.printAllPHDEmplyee);
                break;
            }
            case StaticID.printAllRetiereEmplyee: {
                onPrint(StaticID.printAllRetiereEmplyee);
                break;
            }
            case StaticID.buttonBack: {
                hide();
                break;
            }
            default: {
                return;
            }
        }
        
    }

    @Override
    public void show() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                enterForm.setVisible(true);
            }
        });
    }

    @Override
    public void hide() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                enterForm.setVisible(false);
            }
        });
    }

    private void onPrint(int id) {

        String headline = null;
        BDConnectionHandler connectionHandler = BDConnectionHandler.getInstance();
        connectionHandler.initConnection();
        try {
            Statement stmt = connectionHandler.getConnection().createStatement();
            ResultSet rs = null;
            String output = null;
            switch (id) {
                case StaticID.printAllDismissedEmployee: {
                    headline = "All dismissed employee";
                    rs = stmt.executeQuery(StaticQueries.queryDismissedEmployee);
                    output = "i s s i s b d d";
                    break;
                }
                case StaticID.printAllEmployee: {
                    headline = "All employee";
                    rs = stmt.executeQuery(StaticQueries.queryBrowseEmployee);
                    output = "i s i b d";
                    break;
                }
                case StaticID.printAllHiredEmployee: {
                    headline = "All hiered employee";
                    rs = stmt.executeQuery(StaticQueries.queryHiredEmployee);
                    output = "i s s i s b d d";
                    break;
                }
                case StaticID.printAllPHDEmplyee: {
                    headline = "All PHD employee";
                    rs = stmt.executeQuery(StaticQueries.queryPHDEmployees);
                    output = "i s s i s b d d";
                    break;
                }
                case StaticID.printAllRetiereEmplyee: {
                    headline = "All retiere mployee";
                    rs = stmt.executeQuery(StaticQueries.queryRetiereEmployees);
                    output = "i s s i s b d d";
                    break;
                }
                default: {
                    return;
                }
            }
            JTable table = new JTable();
            OutputHelper.printResultSetToTable(table, rs, output);
            try {
                OutputHelper.OutputToFile(table, headline);
            } catch (IOException ex) {
                Logger.getLogger(PrintControl.class.getName()).log(Level.SEVERE, null, ex);
            }
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(PrintControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
