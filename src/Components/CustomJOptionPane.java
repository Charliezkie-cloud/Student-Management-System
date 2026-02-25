package Components;

import javax.swing.*;
import java.awt.*;

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
}
