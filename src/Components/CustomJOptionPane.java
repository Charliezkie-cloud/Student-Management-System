package Components;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class CustomJOptionPane extends JOptionPane {
    public static void showErrorDialog(Component parent, String message) {
        JOptionPane.showMessageDialog(
                parent,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public static void showSuccessDialog(Component parent, String message) {
        JOptionPane.showMessageDialog(
                parent,
                message,
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static File showSaveToXlsxDialog(JFrame parent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save a xlsx file");
        fileChooser.setCurrentDirectory(fileChooser.getCurrentDirectory());
        fileChooser.setFileFilter(new FileNameExtensionFilter("Excel files (*.xlsx)", "xlsx"));
        fileChooser.setAcceptAllFileFilterUsed(false);

        int selected = fileChooser.showSaveDialog(parent);
        if (selected != JFileChooser.APPROVE_OPTION) return null;

        File selectedFile = fileChooser.getSelectedFile();
        String path = selectedFile.getAbsolutePath();
        if (!path.toLowerCase().endsWith(".xlsx")) selectedFile = new File(path + ".xlsx");
        return selectedFile;
    }

    public static File showOpenXlsxDialog(JFrame parent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Open a xlsx file");
        fileChooser.setCurrentDirectory(fileChooser.getCurrentDirectory());
        fileChooser.setFileFilter(new FileNameExtensionFilter("Excel files (*.xlsx)", "xlsx"));
        fileChooser.setAcceptAllFileFilterUsed(false);

        int selected = fileChooser.showOpenDialog(parent);
        if (selected != JFileChooser.APPROVE_OPTION) return null;

        File selectedFile = fileChooser.getSelectedFile();
        String path = selectedFile.getAbsolutePath();
        return new File(path);
    }
}
