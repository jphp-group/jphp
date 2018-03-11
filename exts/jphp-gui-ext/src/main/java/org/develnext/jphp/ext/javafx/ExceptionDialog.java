package org.develnext.jphp.ext.javafx;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * ExceptionDialog.
 * <p>
 * Displays an exception stack trace in a panel south of the main dialog area.
 *
 * @author Oliver Watkins (c)
 */
public class ExceptionDialog extends JDialog {

    private int dialogWidth = 600;
    private int dialogHeight = 240;

    private JLabel iconLabel = new JLabel();

    // is error panel opened up
    private boolean open = false;

    private JLabel errorLabel = new JLabel();
    private JTextArea errorTextArea = new JTextArea("");

    private JTextArea exceptionTextArea = new JTextArea("");
    private JScrollPane exceptionTextAreaSP = new JScrollPane();

    private JButton okButton = new JButton("OK");
    private JButton viewButton = new JButton("View Error");

    private JPanel topPanel = new JPanel(new BorderLayout());

    public ExceptionDialog(String errorLabelText, String errorDescription,
                           Throwable e) {

        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));

        setSize(dialogWidth, dialogHeight);

        setResizable(false);

        errorTextArea.setText(errorDescription);

        errorLabel.setText(errorLabelText);

        exceptionTextArea.setText(errors.toString());

        exceptionTextAreaSP = new JScrollPane(exceptionTextArea);
        exceptionTextAreaSP.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        exceptionTextAreaSP.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        iconLabel.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));

        iconLabel.setIcon(UIManager.getIcon("OptionPane.errorIcon"));
        setupUI();

        setUpListeners();
    }

    public ExceptionDialog(String errorLabelText, Throwable e) {
        this(errorLabelText, null, e);
    }

    public void setupUI() {

        this.setTitle("Error");

        errorTextArea.setLineWrap(true);
        errorTextArea.setWrapStyleWord(true);
        errorTextArea.setEditable(false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        buttonPanel.add(okButton);
        //buttonPanel.add(viewButton);

        errorTextArea.setBackground(iconLabel.getBackground());

        JScrollPane textAreaSP = new JScrollPane(errorTextArea);

        textAreaSP.setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));

        errorLabel.setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));

        exceptionTextArea.setPreferredSize(new Dimension(100, 200));
        exceptionTextAreaSP.setBorder(new EmptyBorder(new Insets(0, 10, 0, 10)));

        topPanel.add(iconLabel, BorderLayout.WEST);

        JPanel p = new JPanel(new BorderLayout());
        p.add(errorLabel, BorderLayout.NORTH);
        p.add(textAreaSP);

        topPanel.add(p);

        this.add(topPanel);

        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    public void collapse() {
        viewButton.setText("View Error");
        topPanel.remove(exceptionTextAreaSP);
        ExceptionDialog.this.setSize(dialogWidth, dialogHeight);
        topPanel.revalidate();
        open = false;
    }

    public void expand() {
        viewButton.setText("Hide Error");
        topPanel.add(exceptionTextAreaSP, BorderLayout.SOUTH);

        ExceptionDialog.this.setSize(dialogWidth,
                dialogHeight + 100);

        topPanel.revalidate();
        open = true;
    }

    private void setUpListeners() {
        okButton.addActionListener(e -> {
            ExceptionDialog.this.dispose();
            System.exit(1);
        });

        viewButton.addActionListener(e -> {
            if (open) {
                this.collapse();
            } else {
                expand();
            }
        });

    }
}