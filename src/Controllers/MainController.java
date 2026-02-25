package Controllers;

import Components.CustomJOptionPane;
import Models.Student;
import Services.DatabaseService;
import Views.UpdateView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Objects;

public class MainController {
    final private JFrame parent;

    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField middleNameField;
    private JComboBox<String> courseBox;
    private ButtonGroup yearButtonGroup;
    private DefaultTableModel studentsTableModel;
    private JTable studentsTable;

    public MainController(JFrame parent) {
        this.parent = parent;
    }

    // Setters
    public void setFirstNameField(JTextField firstNameField) { this.firstNameField = firstNameField; }
    public void setLastNameField(JTextField lastNameField) { this.lastNameField = lastNameField; }
    public void setMiddleNameField(JTextField middleNameField) { this.middleNameField = middleNameField; }
    public void setCourseBox(JComboBox<String> courseBox) { this.courseBox = courseBox; }
    public void setYearButtonGroup(ButtonGroup yearButtonGroup) { this.yearButtonGroup = yearButtonGroup; }
    public void setStudentsTableModel(DefaultTableModel studentsTableModel) { this.studentsTableModel = studentsTableModel; }
    public void setStudentsTable(JTable studentsTable) { this.studentsTable = studentsTable; }

    // Events
    public class resetFormOnAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            firstNameField.setText("");
            lastNameField.setText("");
            middleNameField.setText("");
            courseBox.setSelectedIndex(0);

            for (
                    Enumeration<AbstractButton> buttons = yearButtonGroup.getElements();
                    buttons.hasMoreElements();
            ) {
                AbstractButton button = buttons.nextElement();
                if (button.isSelected()) button.setSelected(false);
            }
        }
    }

    public class addStudentOnAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!validateForm()) return;

            String selectedYear = "";
            for (
                    Enumeration<AbstractButton> buttons = yearButtonGroup.getElements();
                    buttons.hasMoreElements();
            ) {
                AbstractButton button = buttons.nextElement();
                if (button.isSelected()) {
                    selectedYear = button.getText();
                    break;
                }
            }

            Student newStudent = new Student(
                    firstNameField.getText(),
                    lastNameField.getText(),
                    middleNameField.getText(),
                    Objects.requireNonNull(courseBox.getSelectedItem()).toString(),
                    selectedYear
            );
            DatabaseService.students.add(newStudent);
            DatabaseService.saveToDatabase();
            addStudentToTable(newStudent);
            CustomJOptionPane.showSuccessDialog(parent, "Student added successfully!");
        }
    }

    public class updateStudentOnAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedStudentIndex = studentsTable.getSelectedRow();

            if (selectedStudentIndex != -1) {
                Student selectedStudent = DatabaseService.students.get(selectedStudentIndex);
                new UpdateView(
                        MainController.this,
                        selectedStudent.getUuid(),
                        selectedStudent.getFirstName(),
                        selectedStudent.getMiddleName(),
                        selectedStudent.getLastName(),
                        selectedStudent.getCourse(),
                        selectedStudent.getYear()
                );
                return;
            }

            CustomJOptionPane.showErrorDialog(parent, "Please select a student first.");
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
                int selectedRowIndex = studentsTable.getSelectedRow();

                if (selectedRowIndex != -1) {
                    Student selectedStudent = DatabaseService.students.get(selectedRowIndex);
                    DatabaseService.students.remove(selectedStudent);
                    DatabaseService.saveToDatabase();
                    studentsTableModel.removeRow(selectedRowIndex);
                    CustomJOptionPane.showSuccessDialog(parent, "Student has been removed!");
                    return;
                }

                CustomJOptionPane.showErrorDialog(parent, "Select a student first.");
            }
        }
    }

    // Helpers
    private boolean validateForm() {
        if (firstNameField.getText().length() > 50 || firstNameField.getText().length() < 3) {
            CustomJOptionPane.showErrorDialog(parent, "First name must be between 3 and 50 characters.");
            return false;
        }
        if (lastNameField.getText().length() > 50 || lastNameField.getText().length() < 3) {
            CustomJOptionPane.showErrorDialog(parent, "Last name must be between 3 and 50 characters.");
            return false;
        }
        if (middleNameField.getText().length() > 50 || middleNameField.getText().length() < 3) {
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

    public void loadStudentsTable() {
        DatabaseService.readFromDatabase();
        for (Student student : DatabaseService.students) {
            studentsTableModel.addRow(new Object[] {
                    student.getUuid().toString(),
                    student.getFirstName(),
                    student.getLastName(),
                    student.getMiddleName(),
                    student.getCourse(),
                    student.getYear()
            });
        }
    }

    public void refreshStudentsTable() {
        studentsTableModel.getDataVector().removeAllElements();
        loadStudentsTable();
    }

    private void addStudentToTable(Student student) {
        studentsTableModel.addRow(new Object[] {
                student.getUuid().toString(),
                student.getFirstName(),
                student.getLastName(),
                student.getMiddleName(),
                student.getCourse(),
                student.getYear()
        });
    }
}

