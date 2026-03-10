package Controllers;

import Components.CustomJOptionPane;
import Models.Student;
import Services.DatabaseService;
import Views.UpdateView;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Enumeration;
import java.util.Objects;

public class MainController {
    private final JFrame parent;

    private String[] tableColumns = { };

    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField middleNameField;
    private JComboBox<String> courseBox;
    private ButtonGroup yearButtonGroup;
    private JTextField searchField;
    private DefaultTableModel studentsTableModel;
    private JTable studentsTable;

    public MainController(JFrame parent, String[] tableColumns) {
        this.parent = parent;
        this.tableColumns = tableColumns;
    }

    // Setters
    public void setFirstNameField(JTextField firstNameField) { this.firstNameField = firstNameField; }
    public void setLastNameField(JTextField lastNameField) { this.lastNameField = lastNameField; }
    public void setMiddleNameField(JTextField middleNameField) { this.middleNameField = middleNameField; }
    public void setCourseBox(JComboBox<String> courseBox) { this.courseBox = courseBox; }
    public void setYearButtonGroup(ButtonGroup yearButtonGroup) { this.yearButtonGroup = yearButtonGroup; }
    public void setSearchField(JTextField searchField) { this.searchField = searchField; }
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

    public class searchStudentOnAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<DefaultTableModel>(studentsTableModel);
            studentsTable.setRowSorter(sorter);

            String search = searchField.getText();
            if (!search.isEmpty())
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + search));
            else
                sorter.setRowFilter(null);
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

    public class exportStudentsOnAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            File result = CustomJOptionPane.showSaveToXlsxDialog(parent);
            if (result != null) exportStudents(result);
        }
    }

    // Helpers
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
        studentsTableModel.fireTableDataChanged();
        loadStudentsTable();
    }

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

    private void exportStudents(File filePath) {
        Workbook workBook = new XSSFWorkbook();
        Sheet sheet = workBook.createSheet("Students");

        int rowIndex = 0;
        Row headerRow = sheet.createRow(rowIndex++);
        for (int i = 0; i < tableColumns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(tableColumns[i]);
        }

        for (Student student : DatabaseService.students) {
            Row row = sheet.createRow(rowIndex++);
            int colIndex = 0;
            row.createCell(colIndex++).setCellValue(student.getUuid().toString());
            row.createCell(colIndex++).setCellValue(student.getFirstName());
            row.createCell(colIndex++).setCellValue(student.getMiddleName());
            row.createCell(colIndex++).setCellValue(student.getLastName());
            row.createCell(colIndex++).setCellValue(student.getCourse());
            row.createCell(colIndex++).setCellValue(student.getYear());
        }

        for (int colIndex = 0; colIndex < tableColumns.length; colIndex++) {
            sheet.autoSizeColumn(colIndex);
        }

        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
            workBook.write(outputStream);
        } catch (IOException ex) {
            CustomJOptionPane.showErrorDialog(parent, ex.getMessage());
        } finally {
            try {
                workBook.close();
            } catch (IOException ex) {
                CustomJOptionPane.showErrorDialog(parent, ex.getMessage());
            }
        }
    }
}

