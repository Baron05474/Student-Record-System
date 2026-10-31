import java.awt.*;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class StudentManagementSystem extends JFrame {

    private final CardLayout cardLayout;
    private final JPanel mainPanel;

    public StudentManagementSystem() {
        setTitle("Student Management System");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Screens
        mainPanel.add(createHomeScreen(), "Home");
        mainPanel.add(createStudentLoginScreen(), "StudentLogin");
        mainPanel.add(createAdminLoginScreen(), "AdminLogin");

        add(mainPanel);
    }

    // HOME SCREEN 
    private JPanel createHomeScreen() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 244, 248));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);

        JLabel titleLabel = new JLabel("Student Record Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));

        JButton btnStudentLogin = new JButton("Student Login");
        JButton btnAdminLogin = new JButton("Admin Login");

        btnStudentLogin.setPreferredSize(new Dimension(180, 40));
        btnAdminLogin.setPreferredSize(new Dimension(180, 40));

        btnStudentLogin.addActionListener(e -> cardLayout.show(mainPanel, "StudentLogin"));
        btnAdminLogin.addActionListener(e -> cardLayout.show(mainPanel, "AdminLogin"));

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridy = 1; gbc.gridwidth = 1;
        panel.add(btnStudentLogin, gbc);

        gbc.gridx = 1;
        panel.add(btnAdminLogin, gbc);

        return panel;
    }

    //STUDENT LOGIN SCREEN 
    private JPanel createStudentLoginScreen() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel title = new JLabel("Student Portal Login");
        title.setFont(new Font("Arial", Font.BOLD, 18));

        JTextField txtId = new JTextField(15);
        JPasswordField txtPass = new JPasswordField(15);
        JButton btnLogin = new JButton("Login");
        JButton btnBack = new JButton("Back");

        btnLogin.addActionListener(e -> {
            String id = txtId.getText().trim();
            String pass = new String(txtPass.getPassword());
            Student student = Database.getInstance().authenticateStudent(id, pass);

            if (student != null) {
                mainPanel.add(createStudentDashboard(student), "StudentDashboard");
                cardLayout.show(mainPanel, "StudentDashboard");
                txtId.setText("");
                txtPass.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Student ID or Password!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnBack.addActionListener(e -> cardLayout.show(mainPanel, "Home"));

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridwidth = 1; gbc.gridy = 1; panel.add(new JLabel("Student ID:"), gbc);
        gbc.gridx = 1; panel.add(txtId, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; panel.add(txtPass, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panel.add(btnLogin, gbc);
        gbc.gridx = 1; panel.add(btnBack, gbc);

        return panel;
    }

    // ADMIN LOGIN SCREEN 
    private JPanel createAdminLoginScreen() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel title = new JLabel("Admin Control Login");
        title.setFont(new Font("Arial", Font.BOLD, 18));

        JTextField txtUser = new JTextField(15);
        JPasswordField txtPass = new JPasswordField(15);
        JButton btnLogin = new JButton("Login");
        JButton btnBack = new JButton("Back");

        btnLogin.addActionListener(e -> {
            String user = txtUser.getText().trim();
            String pass = new String(txtPass.getPassword());

            if (Database.getInstance().validateAdmin(user, pass)) {
                mainPanel.add(createAdminDashboard(), "AdminDashboard");
                cardLayout.show(mainPanel, "AdminDashboard");
                txtUser.setText("");
                txtPass.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Admin Credentials!\n(Default: admin / adminpassword)", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnBack.addActionListener(e -> cardLayout.show(mainPanel, "Home"));

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridwidth = 1; gbc.gridy = 1; panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; panel.add(txtUser, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; panel.add(txtPass, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panel.add(btnLogin, gbc);
        gbc.gridx = 1; panel.add(btnBack, gbc);

        return panel;
    }

    // STUDENT DASHBOARD (VIEW ONLY) 
    private JPanel createStudentDashboard(Student student) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Top info header
        JPanel infoPanel = new JPanel(new GridLayout(3, 2, 10, 5));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Student Details"));
        infoPanel.add(new JLabel("ID: " + student.getId()));
        infoPanel.add(new JLabel("Name: " + student.getName()));
        infoPanel.add(new JLabel("Age: " + student.getAge()));
        infoPanel.add(new JLabel("Mobile: " + student.getMobileNumber()));
        infoPanel.add(new JLabel("Attendance: " + student.getAttendancePercentage() + "%"));
        infoPanel.add(new JLabel(String.format("Overall CGPA: %.2f", student.calculateCGPA())));

        // Academic history table
        String[] cols = {"Course Name", "Grade Point (10.0)"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        for (Map.Entry<String, Double> entry : student.getCourseGrades().entrySet()) {
            model.addRow(new Object[]{entry.getKey(), entry.getValue()});
        }
        JTable table = new JTable(model);
        table.setEnabled(false); // Make read only

        JButton btnLogout = new JButton("Logout");
        btnLogout.addActionListener(e -> cardLayout.show(mainPanel, "Home"));

        panel.add(infoPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(btnLogout, BorderLayout.SOUTH);

        return panel;
    }

    // ADMIN DASHBOARD (FULL CONTROL) 
    private JPanel createAdminDashboard() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Student Table
        String[] cols = {"ID", "Name", "Age", "Mobile", "Attendance (%)", "CGPA"};
        DefaultTableModel tableModel = new DefaultTableModel(cols, 0);
        JTable table = new JTable(tableModel);

        Runnable refreshTable = () -> {
            tableModel.setRowCount(0);
            for (Student s : Database.getInstance().getAllStudents()) {
                tableModel.addRow(new Object[]{
                        s.getId(), s.getName(), s.getAge(), s.getMobileNumber(),
                        s.getAttendancePercentage(), String.format("%.2f", s.calculateCGPA())
                });
            }
        };
        refreshTable.run();

        // Control Panel
        JPanel controls = new JPanel(new FlowLayout());

        JButton btnAdd = new JButton("Add Student");
        JButton btnRemove = new JButton("Remove Selected");
        JButton btnUpdateAcademic = new JButton("Update Grades/Attendance");
        JButton btnLogout = new JButton("Logout");

        // Add Student Dialog
        btnAdd.addActionListener(e -> {
            JTextField idField = new JTextField();
            JTextField passField = new JTextField();
            JTextField nameField = new JTextField();
            JTextField ageField = new JTextField();
            JTextField phoneField = new JTextField();

            Object[] fields = {
                    "Student ID:", idField,
                    "Password:", passField,
                    "Name:", nameField,
                    "Age:", ageField,
                    "Mobile:", phoneField
            };

            int option = JOptionPane.showConfirmDialog(this, fields, "Add New Student", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    Student ns = new Student(
                            idField.getText().trim(),
                            passField.getText().trim(),
                            nameField.getText().trim(),
                            Integer.parseInt(ageField.getText().trim()),
                            phoneField.getText().trim()
                    );
                    Database.getInstance().addStudent(ns);
                    refreshTable.run();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid input formats!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Remove Selected
        btnRemove.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String id = (String) tableModel.getValueAt(selectedRow, 0);
                Database.getInstance().removeStudent(id);
                refreshTable.run();
            } else {
                JOptionPane.showMessageDialog(this, "Please select a student to remove.");
            }
        });

        // Update Academic/Details Dialog
        btnUpdateAcademic.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String id = (String) tableModel.getValueAt(selectedRow, 0);
                Student s = Database.getInstance().getStudent(id);

                JTextField courseField = new JTextField();
                JTextField gradeField = new JTextField();
                JTextField attendanceField = new JTextField(String.valueOf(s.getAttendancePercentage()));

                Object[] fields = {
                        "Attendance (%):", attendanceField,
                        "Add/Update Course Name:", courseField,
                        "Grade Point (0.0 - 4.0):", gradeField
                };

                int option = JOptionPane.showConfirmDialog(this, fields, "Update Academic Details: " + s.getName(), JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    try {
                        s.setAttendancePercentage(Double.parseDouble(attendanceField.getText().trim()));
                        if (!courseField.getText().trim().isEmpty() && !gradeField.getText().trim().isEmpty()) {
                            s.addOrUpdateCourse(courseField.getText().trim(), Double.parseDouble(gradeField.getText().trim()));
                        }
                        refreshTable.run();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Invalid numeric input!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a student from the table.");
            }
        });

        btnLogout.addActionListener(e -> cardLayout.show(mainPanel, "Home"));

        controls.add(btnAdd);
        controls.add(btnUpdateAcademic);
        controls.add(btnRemove);
        controls.add(btnLogout);

        panel.add(new JLabel("Admin Control Dashboard - Manage All Records", SwingConstants.CENTER), BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(controls, BorderLayout.SOUTH);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StudentManagementSystem().setVisible(true);
        });
    }
}