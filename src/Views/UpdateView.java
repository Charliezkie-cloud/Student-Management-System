package Views;

import Controllers.MainController;
import Controllers.UpdateController;
import Data.ApplicationData;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.Enumeration;
import java.util.UUID;

public class UpdateView extends JFrame {
    public UpdateView(MainController mainController, UUID uuid, String firstName, String lastName, String middleName, String course, String selectedYear) {
        JPanel mainPanel = new JPanel();
        BoxLayout mainLayout = new BoxLayout(mainPanel, BoxLayout.Y_AXIS);
        Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        mainPanel.setLayout(mainLayout);
        mainPanel.setBorder(padding);

        UpdateController updateController = new UpdateController(this, mainController);

        // ========== START OF COMPONENTS ==========

        // UUID text field
        JTextField uuidField = new JTextField();
        updateController.setUuidField(uuidField);
        uuidField.setText(uuid.toString());
        uuidField.setEditable(false);
        uuidField.setPreferredSize(new Dimension(new Dimension(383, 25)));
        JPanel uuidPanel = new JPanel();
        uuidPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        uuidPanel.add(new JLabel("UUID:"));
        uuidPanel.add(uuidField);

        // First name text field
        JTextField firstNameField = new JTextField();
        updateController.setFirstNameField(firstNameField);
        firstNameField.setText(firstName);
        firstNameField.setPreferredSize(new Dimension(383, 25));
        JPanel firstNamePanel = new JPanel();
        firstNamePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        firstNamePanel.add(new JLabel("First name:"));
        firstNamePanel.add(firstNameField);

        // Last name text field
        JTextField lastNameField = new JTextField();
        updateController.setLastNameField(lastNameField);
        lastNameField.setText(lastName);
        lastNameField.setPreferredSize(new Dimension(383, 25));
        JPanel lastNamePanel = new JPanel();
        lastNamePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        lastNamePanel.add(new JLabel("Last name:"));
        lastNamePanel.add(lastNameField);

        // Middle name text field
        JTextField middleNameField = new JTextField();
        updateController.setMiddleNameField(middleNameField);
        middleNameField.setText(middleName);
        middleNameField.setPreferredSize(new Dimension(370, 25));
        JPanel middleNamePanel = new JPanel();
        middleNamePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        middleNamePanel.add(new JLabel("Middle name:"));
        middleNamePanel.add(middleNameField);

        // Course combo box
        JComboBox<String> courseBox = new JComboBox<>(ApplicationData.courses);
        updateController.setCourseBox(courseBox);
        courseBox.setSelectedItem(course);
        courseBox.setPreferredSize(new Dimension(400, 25));
        JPanel courseBoxPanel = new JPanel();
        courseBoxPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        courseBoxPanel.add(new JLabel("Course:"));
        courseBoxPanel.add(courseBox);

        // Year button group
        ButtonGroup yearButtonGroup = new ButtonGroup();
        updateController.setYearButtonGroup(yearButtonGroup);
        for (String year : ApplicationData.years) {
            yearButtonGroup.add(new JRadioButton(year));
        }

        JPanel yearButtonField = new JPanel();
        yearButtonField.setLayout(new GridLayout(4, 2));
        for (
                Enumeration<AbstractButton> buttons = yearButtonGroup.getElements();
                buttons.hasMoreElements();
        ) {
            AbstractButton button = buttons.nextElement();
            yearButtonField.add(button);
            if (button.getText().equalsIgnoreCase(selectedYear)) button.setSelected(true);
        }

        JPanel yearButtonPanel = new JPanel();
        yearButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        yearButtonPanel.add(new JLabel("Year:"));
        yearButtonPanel.add(yearButtonField);

        // ========== BUTTONS ==========
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(updateController.new deleteStudentOnAction());

        // Cancel button
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(updateController.new cancelOnAction());

        // Save button
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(updateController.new updateStudentOnAction());

        JPanel actionButtonsPanel = new JPanel();
        actionButtonsPanel.setLayout(new GridLayout(1, 2));
        JPanel deleteButtonPanel = new JPanel();
        deleteButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        deleteButtonPanel.add(deleteButton);
        JPanel mainButtonPanel = new JPanel();
        mainButtonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        mainButtonPanel.add(cancelButton);
        mainButtonPanel.add(saveButton);
        actionButtonsPanel.add(deleteButtonPanel);
        actionButtonsPanel.add(mainButtonPanel);

        // ========== END OF COMPONENTS ==========

        mainPanel.add(uuidField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        mainPanel.add(firstNamePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        mainPanel.add(lastNamePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        mainPanel.add(middleNamePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        mainPanel.add(courseBoxPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        mainPanel.add(yearButtonPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        mainPanel.add(actionButtonsPanel);

        add(mainPanel);
        setTitle("Update Student");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
    }
}
