/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package courseworkbd.utility;

/**
 *
 * @author Admin
 */
public class StaticQueries {
//queries for employee

    public static final String queryAddEmployee = "INSERT INTO employee (full_name, age, academic_degree, enroll_date) "
            + "VALUES (?, ?, ?, ?)";
    public static final String queryLastEmployeeNum = "SELECT MAX(num) FROM employee";
    public static final String queryFreeEmployees
            = "SELECT e.num, e.full_name, e.enroll_date "
            + "FROM employee e "
            + "WHERE (NOT EXISTS (SELECT * FROM employee_catalogue) "
            + "OR e.num NOT IN (SELECT ec.num FROM employee_catalogue ec WHERE ec.dismissed <> true))";

    public static final String queryPositionNames = "SELECT position_name, position_code FROM positions_catalogue";
    public static final String queryDepartmentNames = "SELECT department_name, department_code FROM departments_catalogue";
    public static final String queryUnitCodes = "SELECT unit_code, unit_num FROM units_catalogue";
    public static final String queryAddHiredEmployee = "INSERT INTO "
            + "employee_catalogue (num, department_code, unit_num, position_code, state_worker, salary_multiplyer, salary, date_of_hire, dismissed) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String queryGetSalaryByCode = "SELECT salary FROM positions_catalogue WHERE position_code = ?";
    public static final String queryGetDepartmentByCode = "SELECT department_name FROM departments_catalogue WHERE department_code = ?";
    public static final String queryGetPositionByCode = "SELECT position_name FROM position_catalogue WHERE position_code = ?";
    public static final String queryGetUnitByCode = "SELECT unit_code FROM units_catalogue WHERE unit_num = ?";
    public static final String queryGetFreePositionsByUnitPosition = "SELECT unit_num, position_code, quantity "
            + "FROM staff_catalogue";
    public static final String queryFindFreePlaceByUnitPos
            = "SELECT unit_num, position_code "
            + "FROM staff_catalogue "
            + "WHERE current_quantity < quantity AND unit_num = ? AND position_code = ?";
    public static final String queryHiredEmployees
            = "SELECT e_c.num, e.full_name, d_c.department_name, u_c.unit_code, p_c.position_name, e_c.state_worker "
            + "FROM ((((employee_catalogue e_c "
            + "LEFT JOIN employee e ON e_c.num = e.num) "
            + "LEFT JOIN positions_catalogue p_c ON e_c.position_code = p_c.position_code) "
            + "LEFT JOIN units_catalogue u_c ON e_c.unit_num = u_c.unit_num) "
            + "LEFT JOIN departments_catalogue d_c ON e_c.department_code = d_c.department_code) "
            + "WHERE e_c.dismissed <> true "
            + "ORDER BY e_c.num ASC";
    public static final String queryDismissByNum
            = "UPDATE employee_catalogue "
            + "SET dismissed = true, date_of_dismiss = ? "
            + "WHERE num = ?";
    public static final String queryChangeCurrEmployQuantByUnitPos
            = "UPDATE staff_catalogue "
            + "SET current_quantity = ? "
            + "WHERE unit_num = ? AND position_code = ?";
    public static final String queryGetCurrEmployQuantByUnitPos
            = "Select current_quantity "
            + "FROM staff_catalogue "
            + "WHERE unit_num = ? AND position_code = ?";
    public static final String queryUnitPosByNum
            = "SELECT position_code, unit_num "
            + "FROM employee_catalogue "
            + "WHERE dismissed = false AND num = ?";
//universal query

    public static String createQuerySelectFromWhere(String select, String from, String where) {
        return "SELECT " + select + " FROM " + from + " WHERE " + where;
    }

    public static String createInsertValues(String insertInto, String values) {
        return "INSERT INTO (" + insertInto + ") VALUES (" + values + ")";
    }

    public static String createUpdateWhatInWhere(String updateIn, String values, String where) {
        return "UPDATE " + updateIn + " SET " + values + " WHERE " + where;
    }

    public static String createDeleteWhatInWhere(String deleteIn, String where) {
        return "DELETE FROM " + deleteIn + " WHERE " + where;
    }
//queries for main menu
    public static final String queryBrowseEmployee = "SELECT * FROM employee";
    public static final String queryBrowseUnits = "SELECT unit_code FROM units_catalogue";
    public static final String queryBrowseDepartments = "SELECT department_name FROM departments_catalogue";
    public static final String queryBrowseStaff
            = "SELECT uc.unit_code, pc.position_name, sc.current_quantity, sc.quantity "
            + "FROM (staff_catalogue sc "
            + "LEFT JOIN positions_catalogue pc ON sc.position_code = pc.position_code) "
            + "LEFT JOIN units_catalogue uc ON sc.unit_num = uc.unit_num "
            + "ORDER BY sc.unit_num, sc.position_code ASC";
    public static final String queryBrowsePositions = "SELECT position_name, salary FROM positions_catalogue";
    public static final String queryHiredHistory
            = "SELECT e_c.num, e.full_name, d_c.department_name, u_c.unit_code, p_c.position_name, "
            + "e_c.state_worker, e_c.date_of_hire, e_c.dismissed, e_c.date_of_dismiss "
            + "FROM ((((employee_catalogue e_c "
            + "LEFT JOIN employee e ON e_c.num = e.num) "
            + "LEFT JOIN positions_catalogue p_c ON e_c.position_code = p_c.position_code) "
            + "LEFT JOIN units_catalogue u_c ON e_c.unit_num = u_c.unit_num) "
            + "LEFT JOIN departments_catalogue d_c ON e_c.department_code = d_c.department_code) "
            + "ORDER BY e_c.date_of_hire ASC";
//queries for administration menu
    public static final String upadteStaff
            = "UPDATE staff_catalogue "
            + "SET unit_num=?, position_code=?, quantity=? "
            + "WHERE unit_num=? AND position_code=?";
    public static final String queryPositions = "SELECT position_name, salary, position_code FROM positions_catalogue";
    
    public static final String queryCheckDepartmentForDelete
            = "SELECT count(*) FROM employee_catalogue WHERE dismissed = false AND department_code = ?";
    public static final String queryCheckUnitForDelete
            = "SELECT count(*) FROM employee_catalogue WHERE dismissed = false AND unit_num = ?";
    public static final String queryCheckStaffForDelete
            = "SELECT current_quantity * quantity FROM staff_catalogue WHERE unit_num = ? AND position_code = ? "
            + "ORDER BY unit_num, position_code ASC";
    public static final String queryCheckPositionForDelete
            = "SELECT count(*) FROM employee_catalogue WHERE dismissed = false AND position_code = ?";
    
    public static final String queryBrowseStaffForAdminControl
            = "SELECT unit_num, position_code FROM staff_catalogue";
    
    public static final String queryLastDepartmentNum = "SELECT MAX(department_code) FROM departments_catalogue";
    public static final String queryLastUnitNum = "SELECT MAX(unit_num) FROM units_catalogue";
    public static final String queryLastPositionNum = "SELECT MAX(position_code) FROM positions_catalogue";
    
    public static final String queryAddDepartment = "INSERT INTO departments_catalogue (department_name, department_code) "
            + "VALUES (?, ?)";
    public static final String queryAddUnit = "INSERT INTO units_catalogue (unit_code, unit_num) "
            + "VALUES (?, ?)";
    public static final String queryAddPosition = "INSERT INTO positions_catalogue (position_name, position_code, salary) "
            + "VALUES (?, ?, ?)";
    public static final String queryAddStaff = "INSERT INTO staff_catalogue (position_code, unit_num, current_quantity, quantity) "
            + "VALUES (?, ?, ?, ?)";
    //quries for printing
    public static final String queryDismissedEmployee
            = "SELECT e_c.num, e.full_name, d_c.department_name, u_c.unit_code, p_c.position_name, "
            + "e_c.state_worker, e_c.date_of_hire, e_c.date_of_dismiss "
            + "FROM ((((employee_catalogue e_c "
            + "LEFT JOIN employee e ON e_c.num = e.num) "
            + "LEFT JOIN positions_catalogue p_c ON e_c.position_code = p_c.position_code) "
            + "LEFT JOIN units_catalogue u_c ON e_c.unit_num = u_c.unit_num) "
            + "LEFT JOIN departments_catalogue d_c ON e_c.department_code = d_c.department_code) "
            + "WHERE e_c.dismissed = true "
            + "ORDER BY e_c.date_of_hire ASC";
     public static final String queryHiredEmployee
            = "SELECT e_c.num, e.full_name, d_c.department_name, u_c.unit_code, p_c.position_name, "
            + "e_c.state_worker, e_c.date_of_hire, e_c.date_of_dismiss "
            + "FROM ((((employee_catalogue e_c "
            + "LEFT JOIN employee e ON e_c.num = e.num) "
            + "LEFT JOIN positions_catalogue p_c ON e_c.position_code = p_c.position_code) "
            + "LEFT JOIN units_catalogue u_c ON e_c.unit_num = u_c.unit_num) "
            + "LEFT JOIN departments_catalogue d_c ON e_c.department_code = d_c.department_code) "
            + "WHERE e_c.dismissed = false "
            + "ORDER BY e_c.date_of_hire ASC";
     public static final String queryRetiereEmployees
            = "SELECT e_c.num, e.full_name, d_c.department_name, u_c.unit_code, p_c.position_name, "
            + "e_c.state_worker, e_c.date_of_hire, e_c.date_of_dismiss "
            + "FROM ((((employee_catalogue e_c "
            + "LEFT JOIN employee e ON e_c.num = e.num) "
            + "LEFT JOIN positions_catalogue p_c ON e_c.position_code = p_c.position_code) "
            + "LEFT JOIN units_catalogue u_c ON e_c.unit_num = u_c.unit_num) "
            + "LEFT JOIN departments_catalogue d_c ON e_c.department_code = d_c.department_code) "
            + "WHERE e_c.dismissed = false AND e.age > 65 "
            + "ORDER BY e_c.date_of_hire ASC";
      public static final String queryPHDEmployees
            = "SELECT e_c.num, e.full_name, d_c.department_name, u_c.unit_code, p_c.position_name, "
            + "e_c.state_worker, e_c.date_of_hire, e_c.date_of_dismiss "
            + "FROM ((((employee_catalogue e_c "
            + "LEFT JOIN employee e ON e_c.num = e.num) "
            + "LEFT JOIN positions_catalogue p_c ON e_c.position_code = p_c.position_code) "
            + "LEFT JOIN units_catalogue u_c ON e_c.unit_num = u_c.unit_num) "
            + "LEFT JOIN departments_catalogue d_c ON e_c.department_code = d_c.department_code) "
            + "WHERE e_c.dismissed = false AND e.academic_degree = true "
            + "ORDER BY e_c.date_of_hire ASC";
     
}
