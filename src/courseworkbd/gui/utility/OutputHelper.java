/*
 * To change this license header, choose License HeaderesultSet in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package courseworkbd.gui.utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Admin
 */
public class OutputHelper {

    public static void printResultSetToTable(JTable jTable, ResultSet resultSet, String colTypes) {
        try {
            String[] types = colTypes.split(" ");
            ResultSetMetaData dbInfo = resultSet.getMetaData();
            int dbColSize = dbInfo.getColumnCount();
            String[] headline = new String[dbColSize];
            for (int i = 1; i <= dbColSize; i++) {
                headline[i - 1] = dbInfo.getColumnName(i);
            }
            ArrayList<String[]> table = new ArrayList<>();
            String[] temp = null;
            int i = 0;
            while (resultSet.next()) {
                temp = new String[dbColSize];
                for (int j = 0; j < dbColSize; j++) {
                    temp[j] = resultSetGet(resultSet, j + 1, types[j]);
                }
                i++;
                table.add(temp);
            }
            jTable.setModel(outputToTable(headline, table));
        } catch (SQLException ex) {
            Logger.getLogger(OutputHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static DefaultTableModel outputToTable(String[] headline, ArrayList<String[]> table) {
        DefaultTableModel model = new DefaultTableModel();
        String[][] temp = new String[table.size()][headline.length];
        for (int i = 0; i < table.size(); i++) {
            temp[i] = table.get(i);
        }
        model.setDataVector(temp, headline);
        return model;
    }

    private static String resultSetGet(ResultSet rs, int i, String type) throws SQLException {
        switch (type) {
            case "i": {
                return String.valueOf(rs.getInt(i));
            }
            case "s": {
                return rs.getString(i);
            }
            case "f": {
                return String.valueOf(rs.getDouble(i));
            }
            case "b": {
                return String.valueOf(rs.getBoolean(i));
            }
            case "d": {
                return String.valueOf(rs.getDate(i));
            }
            default: {
                return String.valueOf(rs.getObject(i));
            }
        }
    }

    public static void printResultToListBox(JComboBox jComboBox, ResultSet rs, String type) {
        try {
            while (rs.next()) {
                jComboBox.addItem(resultSetGet(rs, 1, type));
            }

        } catch (SQLException ex) {
            Logger.getLogger(OutputHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void printResultToListBox(JComboBox jComboBox, ResultSet rs, String type, int pos) {
        try {
            while (rs.next()) {
                jComboBox.addItem(resultSetGet(rs, pos, type));
            }

        } catch (SQLException ex) {
            Logger.getLogger(OutputHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void printResultToArrayList(ArrayList<String> list, ResultSet rs, String type, int pos) {
        try {
            int i = 0;
            while (rs.next()) {
                list.add(resultSetGet(rs, pos, type));
                i++;
            }

        } catch (SQLException ex) {
            Logger.getLogger(OutputHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void printResultToArray(String[] list, ResultSet rs, String type, int pos) {
        try {
            int i = 0;
            while (rs.next()) {
                list[i] = (resultSetGet(rs, pos, type));
                i++;
            }

        } catch (SQLException ex) {
            Logger.getLogger(OutputHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void printResultToArray2(String[][] list, ResultSet rs, String type) {
        try {
            int i = 0;
            while (rs.next()) {
                list[0][i] = (resultSetGet(rs, 1, type));
                list[1][i] = (resultSetGet(rs, 2, type));
                i++;
            }

        } catch (SQLException ex) {
            Logger.getLogger(OutputHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String whereArg(String text) {

        if (isInteger(text) || isDouble(text)) {
            return text;
        }
        return "'" + text + "'";
    }

    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException nfe) {
        }
        return false;
    }

    private static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException nfe) {
        }
        return false;
    }

    @Deprecated
    public static TableModel insertSelectedRowToTable(JTable table, int selectedRow) {
        DefaultTableModel model = new DefaultTableModel();
        String[][] temp = new String[1][table.getModel().getColumnCount()];
        String[] headline = new String[table.getModel().getColumnCount()];
        for (int i = 0; i < temp[0].length; i++) {
            temp[0][i] = table.getModel().getValueAt(selectedRow, i).toString();
            headline[i] = table.getModel().getColumnName(i);
        }
        model.setDataVector(temp, headline);
        return model;
    }

    public static File OutputToFile(JTable table, String headline) throws IOException {
        File file = new File("Output.html");
        file.setWritable(true);
        BufferedWriter br = new BufferedWriter(new FileWriter(file));
        br.write("<html>");
        br.newLine();
        br.write("<head>");
        br.write("<H1>" + headline + "</H1>");
        br.write("</head>");
        br.newLine();
        br.write("<body>");
        br.newLine();
        br.write("<style>"
                + "th {"
                + "background: grey;"
                + "color: white;"
                + "padding: 10px;"
                + "}"
                + "tr {"
                + "text-align: center;"
                + "}"
                + "table, th, tr, td {"
                + "border: 1px solid black;"
                + "}"
                + "h1{"
                + "padding: 5px;"
                + "}"
                + "</style>");
        br.write("<table>");
        br.newLine();
        for (int i = 0; i < table.getModel().getColumnCount(); i++) {
            StringBuilder line = new StringBuilder();
            line.append("<th>");
            line.append(table.getModel().getColumnName(i));
            line.append("</th>");
            br.write(line.toString());
            br.newLine();
        }
        for (int i = 0; i < table.getModel().getRowCount(); i++) {
            StringBuilder line = new StringBuilder();
            line.append("<tr>");
            for (int j = 0; j < table.getModel().getColumnCount(); j++) {
                line.append("<td>" + table.getValueAt(i, j) + "</td>");
            }
            line.append("</tr>");
            br.write(line.toString());
            br.newLine();
        }
        br.write("</table>");
        br.newLine();
        br.write("</body>");
        br.newLine();
        br.write("</html>");
        br.flush();
        br.close();
        return file;
    }
}
