/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package employeemanager;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;
import employeemanager.LoginAdmin.*;
import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.lang.String;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Nhat Anh
 */
public class Employee extends javax.swing.JFrame {
    byte[] person_image;
    Connection connection = null;
    
    
    /**
     * Creates new form Employee
     */
    ArrayList<Attendance> attendanceList;
    ArrayList<Salary> salaryList;
    public Employee() {
        initComponents();
        show_infomation();
        attendanceList = attendanceList();
        show_attendance(attendanceList);
        salaryList = SalaryList();
        show_salary(salaryList);
        Toolkit toolkit = getToolkit();
        Dimension size = toolkit.getScreenSize();
        setLocation(size.width/2 - getWidth()/2, size.height/2 - getHeight()/2);
    }
    String s;
    String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    String url ="jdbc:sqlserver://localhost:1434;databaseName=EmployeeManagement";
    String user = "sa";
    String pass = "123456";
    String Username = employeemanager.LoginAdmin.txtUsername.getText();
    public void show_infomation(){
        //String Username = employeemanager.LoginAdmin.txtUsername.getText();
        try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(url,user,pass);
            String query = "Select E.Employee_ID, E.Admin_ID, E.LastName, E.FirstName, E.DoB, E.Gender, E.Address, EC.Email, EC.Phone, E.Employee_Image from Employee as E, Em_Contact as EC where E.Employee_ID = EC.Employee_ID AND E.Employee_ID IN (select Username from login where Username LIKE '%"+Username+"%')";
            PreparedStatement pst = con.prepareStatement(query);
            //pst.setString(1, employeemanager.LoginAdmin.txtUsername.getText());
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()){
            txtEmployee_ID.setText(rs.getString("Employee_ID"));
            txtAdmin_ID.setText(rs.getString("Admin_ID"));
            txtLastName.setText(rs.getString("LastName"));
            txtFirstName.setText(rs.getString("FirstName"));
            
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String dateinString = rs.getString("DoB");
            Date date = format.parse(dateinString);
            txtDob.setDate(date);
            txtGender.setSelectedItem(rs.getString("Gender"));
            txtAddress.setText(rs.getString("Address"));
            txtEmail.setText(rs.getString("Email"));
            txtContact.setText(rs.getString("Phone"));
             byte[] img = (rs.getBytes("Employee_Image")); // what Nhật Anh =)) đoạn này biểu sao ko sai, 

             ImageIcon imageIcon = new ImageIcon(new ImageIcon(img).getImage().getScaledInstance(ImageEmployee.getWidth(), ImageEmployee.getHeight(), Image.SCALE_SMOOTH));
             ImageEmployee.setIcon(imageIcon);   
            }
            //txtDob.setDate(rs.getString("Dob"));
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    public ArrayList<Attendance> attendanceList(){
        ArrayList<Attendance> attendanceList = new ArrayList();
        try{
               Class.forName(driver);
               Connection con = DriverManager.getConnection(url,user,pass);
               String query = "select a.Attend_ID, e.Employee_ID, e.LastName, e.FirstName, a.Month, a.Date from Employee as e, Attendance as a where e.Employee_ID = a.Employee_ID AND e.Employee_ID IN (select Username from login where Username LIKE '%"+Username+"%')"; //AND e.Employee_ID = '" +txtSearch1.getText()+"'"; 
               //PreparedStatement pst = con.prepareStatement(query); 
               Statement st = con.createStatement();
               ResultSet rs = st.executeQuery(query);
              // pst.setString(1, txtSearch1.getText());
               Attendance attendance;
               while(rs.next()){
                   attendance = new Attendance(rs.getString("Attend_ID"), rs.getString("Employee_ID"), rs.getString("LastName"), rs.getString("FirstName"), rs.getString("Month"), rs.getString("Date"));
                   attendanceList.add(attendance);
               }
        } catch (Exception e){
            //System.out.println("Show thanh cong");
            JOptionPane.showMessageDialog(null, e);
        }
        return attendanceList;
    } 
    public void show_attendance(ArrayList<Attendance> list){
        //ArrayList<Attendance> list = attendanceList();
        DefaultTableModel model = (DefaultTableModel)TableDisplayAttend.getModel();
        Object[] row = new Object[6];
        for(int i = 0; i < list.size(); i++){
            row[0] = list.get(i).getAttend_ID();
            row[1] = list.get(i).getEmployee_ID();
            row[4] = list.get(i).getLastName();
            row[5] = list.get(i).getFirstName();
            row[2] = list.get(i).getMonth();
            row[3] = list.get(i).getDate();
            model.addRow(row);
        }
    }
    public ArrayList<Salary> SalaryList(){
       ArrayList<Salary> SalaryList = new ArrayList();
       try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(url,user,pass);
            String query = " WITH Total AS ( SELECT COUNT(Month) as total, Employee_ID, month\n" +
"              FROM Attendance\n" +
"              GROUP BY Employee_ID, month\n" +
"			  )\n" +
"			  SELECT Salary.Employee_ID, Salary.Salary_Amount, Salary.Bonus, Salary.Allowance, (total * Salary_Amount + Bonus + Allowance) as Salary_per_month ,Salary.Month\n" +
"			  FROM Salary\n" +
"			  INNER JOIN Total\n" +
"			  ON Salary.Employee_ID = Total.Employee_ID AND Total.month = Salary.month\n" +
"			  WHERE Salary.Employee_ID = Total.Employee_ID	AND Total.Employee_ID IN (Select Username from login where Username LIKE '%"+Username+"%' )";
            //PreparedStatement pst = con.prepareStatement(query);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            //pst.setString(1, txtMonth.getText());
            Salary salary;
            while(rs.next()){
                salary = new Salary(rs.getString("Employee_ID"), rs.getString("Salary_Amount"), rs.getString("Bonus"), rs.getString("Allowance"), rs.getString("Salary_per_month"), rs.getString("Month"));
                SalaryList.add(salary);
            }
       } catch (Exception e){
            //System.out.println("Show thanh cong");
            JOptionPane.showMessageDialog(null, e);
        }
       return SalaryList;
   }
   public void show_salary(ArrayList<Salary> list){
       //ArrayList<Salary> list = SalaryList();
       DefaultTableModel model = (DefaultTableModel)TableDisplaySalary.getModel();
       Object[] row = new Object[6];
       for ( int  i = 0; i < list.size(); i++){
           row[0] = list.get(i).getEmployee_ID();
           row[1] = list.get(i).getSalary_Amount();
           row[2] = list.get(i).getBonus();
           row[3] = list.get(i).getAllowance();
           row[4] = list.get(i).getSalary_per_month();
           row[5] = list.get(i).getMonth();
           model.addRow(row);
       }
   }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        btnInfomation = new javax.swing.JButton();
        btnAttendance = new javax.swing.JButton();
        btnSalary = new javax.swing.JButton();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        Infomation = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtFirstName = new javax.swing.JTextField();
        txtLastName = new javax.swing.JTextField();
        txtGender = new javax.swing.JComboBox<>();
        txtAddress = new javax.swing.JTextField();
        txtEmployee_ID = new javax.swing.JTextField();
        txtAdmin_ID = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        txtContact = new javax.swing.JTextField();
        ImageEmployee = new javax.swing.JLabel();
        txtDob = new com.toedter.calendar.JDateChooser();
        Attendance = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TableDisplayAttend = new javax.swing.JTable();
        Salary = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TableDisplaySalary = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(245, 255, 255));

        jPanel2.setBackground(new java.awt.Color(0, 255, 255));

        jButton1.setBackground(new java.awt.Color(253, 186, 248));
        jButton1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(41, 120, 181));
        jButton1.setText("Logout");
        jButton1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnInfomation.setBackground(new java.awt.Color(253, 186, 248));
        btnInfomation.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        btnInfomation.setForeground(new java.awt.Color(41, 120, 181));
        btnInfomation.setText("Infomation");
        btnInfomation.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnInfomation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInfomationActionPerformed(evt);
            }
        });

        btnAttendance.setBackground(new java.awt.Color(253, 186, 248));
        btnAttendance.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        btnAttendance.setForeground(new java.awt.Color(41, 120, 181));
        btnAttendance.setText("Attendance");
        btnAttendance.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnAttendance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAttendanceActionPerformed(evt);
            }
        });

        btnSalary.setBackground(new java.awt.Color(253, 186, 248));
        btnSalary.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        btnSalary.setForeground(new java.awt.Color(41, 120, 181));
        btnSalary.setText("Salary");
        btnSalary.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnSalary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalaryActionPerformed(evt);
            }
        });

        jLayeredPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLayeredPane1.setLayout(new java.awt.CardLayout());

        Infomation.setBackground(new java.awt.Color(240, 255, 255));

        jLabel1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel1.setText("FirstName:");

        jLabel2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel2.setText("LastName:");

        jLabel3.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel3.setText("Gender:");

        jLabel4.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel4.setText("Date of birth:");

        jLabel5.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel5.setText("Address:");

        jLabel6.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel6.setText("Employee_ID:");

        jLabel7.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel7.setText("Admin_ID:");

        jLabel8.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel8.setText("Email:");

        jLabel9.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel9.setText("Contact:");

        txtFirstName.setEditable(false);
        txtFirstName.setBackground(new java.awt.Color(255, 255, 255));
        txtFirstName.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtFirstName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFirstNameActionPerformed(evt);
            }
        });

        txtLastName.setEditable(false);
        txtLastName.setBackground(new java.awt.Color(255, 255, 255));
        txtLastName.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        txtGender.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtGender.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Male", "Female", "Other", " " }));

        txtAddress.setEditable(false);
        txtAddress.setBackground(new java.awt.Color(255, 255, 255));
        txtAddress.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        txtEmployee_ID.setEditable(false);
        txtEmployee_ID.setBackground(new java.awt.Color(255, 255, 255));
        txtEmployee_ID.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        txtAdmin_ID.setEditable(false);
        txtAdmin_ID.setBackground(new java.awt.Color(255, 255, 255));
        txtAdmin_ID.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        txtEmail.setEditable(false);
        txtEmail.setBackground(new java.awt.Color(255, 255, 255));
        txtEmail.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        txtContact.setEditable(false);
        txtContact.setBackground(new java.awt.Color(255, 255, 255));
        txtContact.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        ImageEmployee.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout InfomationLayout = new javax.swing.GroupLayout(Infomation);
        Infomation.setLayout(InfomationLayout);
        InfomationLayout.setHorizontalGroup(
            InfomationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(InfomationLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(InfomationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(InfomationLayout.createSequentialGroup()
                        .addGroup(InfomationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, InfomationLayout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(18, 18, 18)
                                .addGroup(InfomationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtContact)
                                    .addComponent(txtEmail)
                                    .addComponent(txtAdmin_ID)
                                    .addComponent(txtEmployee_ID)))
                            .addGroup(InfomationLayout.createSequentialGroup()
                                .addGroup(InfomationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, InfomationLayout.createSequentialGroup()
                                        .addGroup(InfomationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel4)
                                            .addComponent(jLabel5))
                                        .addGap(26, 26, 26))
                                    .addGroup(InfomationLayout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGroup(InfomationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtGender, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtAddress)
                                    .addGroup(InfomationLayout.createSequentialGroup()
                                        .addGroup(InfomationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(txtDob, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(InfomationLayout.createSequentialGroup()
                                                .addComponent(txtFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(16, 16, 16)
                                                .addComponent(jLabel2)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtLastName, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(InfomationLayout.createSequentialGroup()
                        .addGroup(InfomationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 538, Short.MAX_VALUE)
                        .addComponent(ImageEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(52, 52, 52))
                    .addGroup(InfomationLayout.createSequentialGroup()
                        .addGroup(InfomationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel8))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        InfomationLayout.setVerticalGroup(
            InfomationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(InfomationLayout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addGroup(InfomationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(InfomationLayout.createSequentialGroup()
                        .addGroup(InfomationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1)
                            .addComponent(txtFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(txtLastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(InfomationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtGender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24)
                        .addGroup(InfomationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4)
                            .addComponent(txtDob, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(InfomationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(19, 19, 19)
                        .addGroup(InfomationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtEmployee_ID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(21, 21, 21)
                        .addGroup(InfomationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(txtAdmin_ID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(23, 23, 23)
                        .addGroup(InfomationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(17, 17, 17)
                        .addGroup(InfomationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(txtContact, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(ImageEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(179, Short.MAX_VALUE))
        );

        jLayeredPane1.add(Infomation, "card2");

        Attendance.setBackground(new java.awt.Color(240, 255, 255));

        jScrollPane1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        TableDisplayAttend.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Attend_ID", "Employee_ID", "LastName", "FirstName", "Month", "Date"
            }
        ));
        jScrollPane1.setViewportView(TableDisplayAttend);

        javax.swing.GroupLayout AttendanceLayout = new javax.swing.GroupLayout(Attendance);
        Attendance.setLayout(AttendanceLayout);
        AttendanceLayout.setHorizontalGroup(
            AttendanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AttendanceLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 786, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(36, Short.MAX_VALUE))
        );
        AttendanceLayout.setVerticalGroup(
            AttendanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AttendanceLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLayeredPane1.add(Attendance, "card3");

        Salary.setBackground(new java.awt.Color(240, 255, 255));

        TableDisplaySalary.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        TableDisplaySalary.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Employee_ID", "Salary_Amont", "Bonus", "Allowance", "Salary_per_month", "Month"
            }
        ));
        TableDisplaySalary.setGridColor(new java.awt.Color(255, 255, 255));
        jScrollPane2.setViewportView(TableDisplaySalary);

        javax.swing.GroupLayout SalaryLayout = new javax.swing.GroupLayout(Salary);
        Salary.setLayout(SalaryLayout);
        SalaryLayout.setHorizontalGroup(
            SalaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SalaryLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 839, Short.MAX_VALUE)
                .addContainerGap())
        );
        SalaryLayout.setVerticalGroup(
            SalaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SalaryLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLayeredPane1.add(Salary, "card4");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(btnInfomation, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnAttendance, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnSalary, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnInfomation)
                    .addComponent(btnAttendance)
                    .addComponent(btnSalary))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        LoginAdmin La = new LoginAdmin();
        La.setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnSalaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalaryActionPerformed
        // TODO add your handling code here:
        Infomation.setVisible(false);
        Salary.setVisible(true);
        Attendance.setVisible(false);
    }//GEN-LAST:event_btnSalaryActionPerformed

    private void btnInfomationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInfomationActionPerformed
        // TODO add your handling code here:
        Infomation.setVisible(true);
        Salary.setVisible(false);
        Attendance.setVisible(false);
    }//GEN-LAST:event_btnInfomationActionPerformed

    private void btnAttendanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAttendanceActionPerformed
        // TODO add your handling code here:
        Infomation.setVisible(false);
        Salary.setVisible(false);
        Attendance.setVisible(true);
    }//GEN-LAST:event_btnAttendanceActionPerformed

    private void txtFirstNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFirstNameActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_txtFirstNameActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Employee.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Employee.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Employee.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Employee.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Employee().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Attendance;
    private javax.swing.JLabel ImageEmployee;
    private javax.swing.JPanel Infomation;
    private javax.swing.JPanel Salary;
    private javax.swing.JTable TableDisplayAttend;
    private javax.swing.JTable TableDisplaySalary;
    private javax.swing.JButton btnAttendance;
    private javax.swing.JButton btnInfomation;
    private javax.swing.JButton btnSalary;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtAdmin_ID;
    private javax.swing.JTextField txtContact;
    private com.toedter.calendar.JDateChooser txtDob;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtEmployee_ID;
    private javax.swing.JTextField txtFirstName;
    private javax.swing.JComboBox<String> txtGender;
    private javax.swing.JTextField txtLastName;
    // End of variables declaration//GEN-END:variables
}
