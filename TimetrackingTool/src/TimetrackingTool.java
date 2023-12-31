import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimetrackingTool {
    private static Map<String, List<String>> vacationRequests = new HashMap<>();
    private static Map<String, List<String>> worktimeSheets = new HashMap<>();
    private static Map<String, String> sicknessRecords = new HashMap<>();
    private static Map<String, String> worktimeApprovals = new HashMap<>();

    public static void main(String[] args) {
        UserDataHandler userDataHandler = new UserDataHandler();

        JFrame frame = new JFrame("User Registration & Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(720, 480);

        JPanel registrationPanel = new JPanel();
        JPanel loginPanel = new JPanel();

        JTextField regUsernameField = new JTextField(20);
        JPasswordField regPasswordField = new JPasswordField(20);
        JComboBox<String> regRoleComboBox = new JComboBox<>(new String[] { "HR", "Supervisor", "Employee" });
        JButton registerButton = new JButton("Register");

        registrationPanel.add(new JLabel("Username:"));
        registrationPanel.add(regUsernameField);
        registrationPanel.add(new JLabel("Password:"));
        registrationPanel.add(regPasswordField);
        registrationPanel.add(new JLabel("Role:"));
        registrationPanel.add(regRoleComboBox);
        registrationPanel.add(registerButton);

        JTextField loginUsernameField = new JTextField(20);
        JPasswordField loginPasswordField = new JPasswordField(20);
        JButton loginButton = new JButton("Login");

        loginPanel.add(new JLabel("Username: "));
        loginPanel.add(loginUsernameField);
        loginPanel.add(new JLabel("Password: "));
        loginPanel.add(loginPasswordField);
        loginPanel.add(loginButton);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Registration", registrationPanel);
        tabbedPane.addTab("Login", loginPanel);

        frame.add(tabbedPane);

        // Color definitions
        Color buttonColor = new Color(81, 114, 92);
        Color panelColor = new Color(220, 235, 216);

        registrationPanel.setBackground(panelColor);
        loginPanel.setBackground(panelColor);

        int buttonWidth = 200;
        int buttonHeight = 40;
        Dimension buttonDimension = new Dimension(buttonWidth, buttonHeight);

        registerButton.setBackground(buttonColor);
        loginButton.setBackground(buttonColor);

        registerButton.setForeground(Color.WHITE);
        loginButton.setForeground(Color.WHITE);

        registerButton.setPreferredSize(buttonDimension);
        loginButton.setPreferredSize(buttonDimension);

        frame.setVisible(true);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String regRole = (String) regRoleComboBox.getSelectedItem();
                UserData userData = new UserData(regRole);

                String regUsername = regUsernameField.getText();
                String regPassword = new String(regPasswordField.getPassword());

                if (userDataHandler.checkUsernameExists(regUsername)) {
                    JOptionPane.showMessageDialog(null, "Username already exists. Please choose another one.");
                    regUsernameField.setText("");
                    regPasswordField.setText("");
                    return;
                }

                String registrationError = UserData.validateRegistration(regUsername, regPassword);
                if (registrationError != null) {
                    JOptionPane.showMessageDialog(null, registrationError);
                    regUsernameField.setText("");
                    regPasswordField.setText("");
                    return;
                }

                userData.setUsername(regUsername);
                userData.setPassword(regPassword);
                userDataHandler.saveUser(userData);
                JOptionPane.showMessageDialog(null, "Registration successful.");

            }
        });

        // TODO: User deletion logic, password reset logic

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String loginUsername = loginUsernameField.getText();
                String loginPassword = new String(loginPasswordField.getPassword());

                if (userDataHandler.checkUsernameExists(loginUsername) && userDataHandler.checkPassword(loginUsername,
                        loginPassword)) {
                    String userRole = userDataHandler.getUserData(loginUsername).getRole();
                    openRoleSpecificMenu(userRole, loginUsername);
                } else {
                    JOptionPane.showMessageDialog(null, "Login failed. Please check your credentials.");
                }

                loginUsernameField.setText("");
                loginPasswordField.setText("");
            }
        });
    }

    private static void openRoleSpecificMenu(String userRole, String username) {
        if (userRole.equals("HR")) {
            JFrame roleSpecificFrame = new JFrame(userRole + " Menu");
            roleSpecificFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            roleSpecificFrame.setSize(600, 400);

            JPanel menuPanel = new JPanel();

            JButton registerSicknessButton = new JButton("Register Sickness");
            JButton viewVacationRequestsButton = new JButton("View Vacation Requests");

            styleButton(registerSicknessButton);
            styleButton(viewVacationRequestsButton);

            registerSicknessButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    registerSickness(username);
                }
            });

            viewVacationRequestsButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    viewVacationRequests();
                }
            });

            menuPanel.add(new JLabel("Welcome, " + userRole + " " + username + "!"));
            menuPanel.add(registerSicknessButton);
            menuPanel.add(viewVacationRequestsButton);

            roleSpecificFrame.add(menuPanel);
            roleSpecificFrame.setVisible(true);
        } else if (userRole.equals("Supervisor")) {
            JFrame roleSpecificFrame = new JFrame(userRole + " Menu");
            roleSpecificFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            roleSpecificFrame.setSize(600, 400);

            JPanel menuPanel = new JPanel();

            JButton approveWorktimeButton = new JButton("Approve Worktime");

            styleButton(approveWorktimeButton);

            approveWorktimeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    approveWorktime(username);
                }
            });

            menuPanel.add(new JLabel("Welcome, " + " " + username + "!"));
            menuPanel.add(approveWorktimeButton);

            roleSpecificFrame.add(menuPanel);
            roleSpecificFrame.setVisible(true);
        } else if (userRole.equals("Employee")) {
            JFrame roleSpecificFrame = new JFrame(userRole + " Menu");
            roleSpecificFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            roleSpecificFrame.setSize(600, 400);

            JPanel menuPanel = new JPanel();

            JButton requestVacationButton = new JButton("Request Vacation");
            JButton deleteVacationButton = new JButton("Delete Vacation");
            JButton registerWorktimeButton = new JButton("Register Worktime");
            JButton viewWorktimeButton = new JButton("View Worktime");

            styleButton(requestVacationButton);
            styleButton(deleteVacationButton);
            styleButton(registerWorktimeButton);
            styleButton(viewWorktimeButton);

            requestVacationButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    requestVacation(username);
                }
            });

            deleteVacationButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    deleteVacation(username);
                }
            });

            registerWorktimeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    registerWorktime(username);
                }
            });

            viewWorktimeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    viewWorktime(username);
                }
            });

            menuPanel.add(new JLabel("Welcome, " + userRole + " " + username + "!"));
            menuPanel.add(requestVacationButton);
            menuPanel.add(deleteVacationButton);
            menuPanel.add(registerWorktimeButton);
            menuPanel.add(viewWorktimeButton);

            roleSpecificFrame.add(menuPanel);
            roleSpecificFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Unknown role.");
        }
    }

    private static void registerSickness(String username) {
        // TODO: Object class for sick days and handling logic
        
        String employeeUsername = JOptionPane.showInputDialog("Enter the employee's username:");
        String sicknessDescription = JOptionPane.showInputDialog("Enter the sickness description:");

        sicknessRecords.put(employeeUsername, sicknessDescription);
        JOptionPane.showMessageDialog(null, "Sickness recorded for " + employeeUsername + ".");
    }

    private static void viewVacationRequests() {
        // TODO: Object class for vacation requests & proper handling

        StringBuilder vacationRequestList = new StringBuilder("Vacation Requests:\n");

        for (Map.Entry<String, List<String>> entry : vacationRequests.entrySet()) {
            String username = entry.getKey();
            List<String> requests = entry.getValue();

            if (requests != null && !requests.isEmpty()) {
                vacationRequestList.append("User: ").append(username).append("\n");
                for (String request : requests) {
                    vacationRequestList.append("- ").append(request).append("\n");
                }
            }
        }

        if (vacationRequestList.toString().equals("Vacation Requests:\n")) {
            vacationRequestList.append("No vacation requests at the moment.");
        }

        JOptionPane.showMessageDialog(null, vacationRequestList.toString());
    }

    private static void approveWorktime(String username) {
        String employeeUsername = JOptionPane.showInputDialog("Enter the employee's username:");
        String worktimeSheet = JOptionPane.showInputDialog("Enter the approved worktime sheet:");

        // TODO: Supervisor's worktime approval logic
        if (worktimeSheet != null && !worktimeSheet.isEmpty()) {
            worktimeApprovals.put(employeeUsername, worktimeSheet);
            JOptionPane.showMessageDialog(null, "Worktime sheet approved for " + employeeUsername + ".");
        } else {
            JOptionPane.showMessageDialog(null, "Please enter an approved worktime sheet.");
        }
    }

    private static void requestVacation(String username) {
        // TODO: Employee's vacation request code

        VacationRequestForm requestForm = new VacationRequestForm(username);
        requestForm.setVisible(true);
    }

    private static void deleteVacation(String username) {
        // TODO: Employee's vacation deletion code
    }

    private static void registerWorktime(String username) {
        // TODO Employee's worktime registration code

        EmployeeWorktimeRegistrationForm worktimeForm = new EmployeeWorktimeRegistrationForm();
        worktimeForm.setVisible(true);
    }

    

    private static void viewWorktime(String username) {

        EmployeeWorktimeRegistrationForm worktimeForm = new EmployeeWorktimeRegistrationForm();
        worktimeForm.displayDataFromFile();

        worktimeForm.setVisible(true);

        List<String> worktimeSheet = worktimeSheets.get(username);
        if (worktimeSheet != null && !worktimeSheet.isEmpty()) {
            StringBuilder worktimeContent = new StringBuilder();
            for (String entry : worktimeSheet) {
                worktimeContent.append(entry).append("\n");
            }

            JTextArea textArea = new JTextArea(20, 40);
            textArea.setText(worktimeContent.toString());
            textArea.setWrapStyleWord(true);
            textArea.setLineWrap(true);
            textArea.setCaretPosition(0);
            textArea.setEditable(false);

            JScrollPane scrollPane = new JScrollPane(textArea);
            JFrame frame = new JFrame(username + "'s Worktime Sheet");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.add(scrollPane);
            frame.pack();
            frame.setVisible(true);
        } else {
            // JOptionPane.showMessageDialog(null, "No worktime records available.");
        }
    }

    private static void styleButton(JButton button) {
        button.setPreferredSize(new Dimension(200, 40));
        button.setBackground(new Color(81, 114, 92));
        button.setForeground(Color.WHITE);
    }
}
