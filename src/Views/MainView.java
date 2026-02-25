package Views;

import Controllers.MainController;
import Data.ApplicationData;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Enumeration;

public class MainView extends JFrame {
    private final String[] tableColumn = {
            "UUID",
            "First name",
            "Last name",
            "Middle name",
            "Course",
            "Year",
    };

    public MainView() {
        JPanel mainPanel = new JPanel();
        BoxLayout mainLayout = new BoxLayout(mainPanel, BoxLayout.Y_AXIS);
        Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        mainPanel.setLayout(mainLayout);
        mainPanel.setBorder(padding);

        MainController mainController = new MainController(this);

        // ========== START OF COMPONENTS ==========

        JTextField firstNameField = new JTextField();
        mainController.setFirstNameField(firstNameField);
        firstNameField.setPreferredSize(new Dimension(383, 25));
        JPanel firstNamePanel = new JPanel();
        firstNamePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        firstNamePanel.add(new JLabel("First name:"));
        firstNamePanel.add(firstNameField);

        JTextField lastNameField = new JTextField();
        mainController.setLastNameField(lastNameField);
        lastNameField.setPreferredSize(new Dimension(383, 25));
        JPanel lastNamePanel = new JPanel();
        lastNamePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        lastNamePanel.add(new JLabel("Last name:"));
        lastNamePanel.add(lastNameField);

        JTextField middleNameField = new JTextField();
        mainController.setMiddleNameField(middleNameField);
        middleNameField.setPreferredSize(new Dimension(370, 25));
        JPanel middleNamePanel = new JPanel();
        middleNamePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        middleNamePanel.add(new JLabel("Middle name:"));
        middleNamePanel.add(middleNameField);

        JComboBox<String> courseBox = new JComboBox<>(ApplicationData.courses);
        mainController.setCourseBox(courseBox);
        courseBox.setPreferredSize(new Dimension(400, 25));
        JPanel coursePanel = new JPanel();
        coursePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        coursePanel.add(new JLabel("Course:"));
        coursePanel.add(courseBox);

        ButtonGroup yearButtonGroup = new ButtonGroup();
        mainController.setYearButtonGroup(yearButtonGroup);
        for (String year : ApplicationData.years) {
            yearButtonGroup.add(new JRadioButton(year));
        }

        JPanel yearButtonPanel = new JPanel();
        yearButtonPanel.setLayout(new GridLayout(4, 2));

        for (
                Enumeration<AbstractButton> buttons = yearButtonGroup.getElements();
                buttons.hasMoreElements();
        ) {
            AbstractButton button = buttons.nextElement();
            yearButtonPanel.add(button);
        }

        JPanel yearButtonField = new JPanel();
        yearButtonField.setLayout(new FlowLayout(FlowLayout.LEFT));
        yearButtonField.add(new JLabel("Year:"));
        yearButtonField.add(yearButtonPanel);

        JButton resetFormButton = new JButton("Reset");
        resetFormButton.addActionListener(mainController.new resetFormOnAction());
        JButton addStudentButton = new JButton("Add student");
        addStudentButton.addActionListener(mainController.new addStudentOnAction());
        JPanel formButtonPanel = new JPanel();
        formButtonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        formButtonPanel.add(resetFormButton);
        formButtonPanel.add(addStudentButton);

        JPopupMenu tablePopupMenu = new JPopupMenu();
        JMenuItem deleteStudentItem = new JMenuItem("Delete");
        deleteStudentItem.addActionListener(mainController.new deleteStudentOnAction());
        JMenuItem updateStudentItem = new JMenuItem("Update");
        updateStudentItem.addActionListener(mainController.new updateStudentOnAction());
        tablePopupMenu.add(updateStudentItem);
        tablePopupMenu.add(deleteStudentItem);

        DefaultTableModel studentsTableModel = new DefaultTableModel(tableColumn, 0);
        mainController.setStudentsTableModel(studentsTableModel);
        JTable studentsTable = new JTable(studentsTableModel);
        mainController.setStudentsTable(studentsTable);
        studentsTable.setComponentPopupMenu(tablePopupMenu);
        JScrollPane studentsTablePane = new JScrollPane();
        studentsTablePane.setViewportView(studentsTable);

        // ========== END OF COMPONENTS ==========

        mainPanel.add(firstNamePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        mainPanel.add(lastNamePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        mainPanel.add(middleNamePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        mainPanel.add(coursePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        mainPanel.add(yearButtonField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        mainPanel.add(formButtonPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(studentsTablePane);

        mainController.loadStudentsTable();

        add(mainPanel);
        setTitle("Student Management System by Charles Henry M. Tinoy Jr.");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
    }
}
