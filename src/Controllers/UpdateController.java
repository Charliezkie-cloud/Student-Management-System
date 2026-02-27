package Controllers;

import Components.CustomJOptionPane;
import Models.Student;
import Services.DatabaseService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class UpdateController {
    final private JFrame parent;
    final private MainController mainController;

    private JTextField uuidField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField middleNameField;
    private JComboBox<String> courseBox;
    private ButtonGroup yearButtonGroup;

    public UpdateController(JFrame parent, MainController mainController) {
        this.parent = parent;
        this.mainController = mainController;
    }

    // Setters
    public void setUuidField(JTextField uuidField) { this.uuidField = uuidField; }
    public void setFirstNameField(JTextField firstNameField) { this.firstNameField = firstNameField; }
    public void setLastNameField(JTextField lastNameField) { this.lastNameField = lastNameField; }
    public void setMiddleNameField(JTextField middleNameField) { this.middleNameField = middleNameField; }
    public void setCourseBox(JComboBox<String> courseBox) { this.courseBox = courseBox; }
    public void setYearButtonGroup(ButtonGroup yearButtonGroup) { this.yearButtonGroup = yearButtonGroup; }

    // Events
    public class updateStudentOnAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!validateForm()) return;

            UUID uuid = UUID.fromString(uuidField.getText());
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String middleName = middleNameField.getText();
            String course = Objects.requireNonNull(courseBox.getSelectedItem()).toString();
            String year = "";

            for (
                    Enumeration<AbstractButton> buttons = yearButtonGroup.getElements();
                    buttons.hasMoreElements();
            ) {
                AbstractButton button = buttons.nextElement();
                if (button.isSelected()) {
                    year = button.getText();
                    break;
                }
            }

            Optional<Student> result = DatabaseService.students
                    .stream()
                    .filter(student -> student.getUuid().equals(uuid))
                    .findAny();

            if (result.isPresent()) {
                Student student = result.get();

                student.setFirstName(firstName);
                student.setLastName(lastName);
                student.setMiddleName(middleName);
                student.setCourse(course);
                student.setYear(year);

                DatabaseService.saveToDatabase();
                mainController.refreshStudentsTable();

                CustomJOptionPane.showSuccessDialog(UpdateController.this.parent, "Student has been updated!");
                parent.dispose();
                return;
            }

            CustomJOptionPane.showErrorDialog(UpdateController.this.parent, "Can't find a student with the given ID.");
        }
    }

    public class cancelOnAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int option = CustomJOptionPane.showConfirmDialog(
                    UpdateController.this.parent,
                    "Are you sure you want to cancel?",
                    "Cancel confirmation",
                    CustomJOptionPane.YES_NO_OPTION
            );
            if (option == CustomJOptionPane.YES_OPTION) {
                parent.dispose();
            }
        }
    }

    public class deleteStudentOnAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int option = CustomJOptionPane.showConfirmDialog(
                    parent,
                    "Are you sure you want to delete this student?",
                    "Deletion confirmation",
                    CustomJOptionPane.YES_NO_OPTION
            );
            if (option == CustomJOptionPane.YES_OPTION) {
                UUID uuid = UUID.fromString(uuidField.getText());

                Optional<Student> result = DatabaseService.students
                        .stream()
                        .filter(student -> student.getUuid().equals(uuid))
                        .findAny();

                if (result.isPresent()) {
                    Student student = result.get();

                    DatabaseService.students.remove(student);
                    DatabaseService.saveToDatabase();
                    CustomJOptionPane.showSuccessDialog(parent, "Student has been removed!");
                    mainController.refreshStudentsTable();
                    parent.dispose();

                    return;
                }

                CustomJOptionPane.showErrorDialog(parent, "Can't find a student with the given ID.");
            }
        }
    }

    // Helpers
    private boolean validateForm() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String middleName = middleNameField.getText();

        if (firstName.length() > 50 || firstName.length() < 3) {
            CustomJOptionPane.showErrorDialog(parent, "First name must be between 3 and 50 characters.");
            return false;
        }
        if (lastName.length() > 50 || lastName.length() < 3) {
            CustomJOptionPane.showErrorDialog(parent, "Last name must be between 3 and 50 characters.");
            return false;
        }
        if (middleName.length() > 50 || middleName.length() < 3) {
            CustomJOptionPane.showErrorDialog(parent, "Middle name must be between 3 and 50 characters.");
            return false;
        }

        int selectedCourseIndex = courseBox.getSelectedIndex();
        if (selectedCourseIndex == -1) {
            CustomJOptionPane.showErrorDialog(parent, "Course is required.");
            return false;
        }

        boolean isSelectedYear = false;
        for (
                Enumeration<AbstractButton> buttons = yearButtonGroup.getElements();
                buttons.hasMoreElements();
        ) {
            AbstractButton button = buttons.nextElement();
            if (button.isSelected()) {
                isSelectedYear = true;
                break;
            }
        }
        if (!isSelectedYear) {
            CustomJOptionPane.showErrorDialog(parent, "Year is required.");
            return false;
        }

        return true;
    }
}
