/* AddPartyView.java
 *
 *  Version
 *  $Id$
 *
 *  Revisions:
 * 		$Log: NewPatronView.java,v $
 * 		Revision 1.3  2003/02/02 16:29:52  ???
 * 		Added ControlDeskEvent and ControlDeskObserver. Updated Queue to allow access to Vector so that contents could be viewed without destroying. Implemented observer model for most of ControlDesk.
 *
 *
 */

/**
 * Class for GUI components need to add a patron
 *
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class NewPatronView implements ActionListener {

//    private int maxSize;

    private JFrame win;

    private JButton abort;
    private JButton finished;

    private JLabel nickLabel;
    private JLabel fullLabel;
    private JLabel emailLabel;

    private JTextField nickField;
    private JTextField fullField;
    private JTextField emailField;

    private String nick;
    private String full;
    private String email;

    private boolean done;

//    private String selectedNick, selectedMember;
    private AddPartyView addParty;

    public NewPatronView(final AddPartyView view) {

        addParty=view;
        done = false;

        win = new JFrame("Add Patron");
        win.getContentPane().setLayout(new BorderLayout());
        ((JPanel) win.getContentPane()).setOpaque(false);

        final JPanel colPanel = new JPanel();
        colPanel.setLayout(new BorderLayout());

        // Patron Panel
        final JPanel patronPanel = new JPanel();
        patronPanel.setLayout(new GridLayout(3, 1));
        patronPanel.setBorder(new TitledBorder("Your Info"));

        final JPanel nickPanel = new JPanel();
        nickPanel.setLayout(new FlowLayout());
        nickLabel = new JLabel("Nick Name");
        nickField = new JTextField("", 15);
        nickPanel.add(nickLabel);
        nickPanel.add(nickField);

        final JPanel fullPanel = new JPanel();
        fullPanel.setLayout(new FlowLayout());
        fullLabel = new JLabel("Full Name");
        fullField = new JTextField("", 15);
        fullPanel.add(fullLabel);
        fullPanel.add(fullField);

        final JPanel emailPanel = new JPanel();
        emailPanel.setLayout(new FlowLayout());
        emailLabel = new JLabel("E-Mail");
        emailField = new JTextField("", 15);
        emailPanel.add(emailLabel);
        emailPanel.add(emailField);

        patronPanel.add(nickPanel);
        patronPanel.add(fullPanel);
        patronPanel.add(emailPanel);

        // Button Panel
        final JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1));

//        Insets buttonMargin = new Insets(4, 4, 4, 4);

        finished = new JButton("Add Patron");
        final JPanel finishedPanel = new JPanel();
        finishedPanel.setLayout(new FlowLayout());
        finished.addActionListener(this);
        finishedPanel.add(finished);

        abort = new JButton("Abort");
        final JPanel abortPanel = new JPanel();
        abortPanel.setLayout(new FlowLayout());
        abort.addActionListener(this);
        abortPanel.add(abort);

        buttonPanel.add(abortPanel);
        buttonPanel.add(finishedPanel);

        // Clean up main panel
        colPanel.add(patronPanel, "Center");
        colPanel.add(buttonPanel, "East");

        win.getContentPane().add("Center", colPanel);

        win.pack();

        // Center Window on Screen
        final Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
        win.setLocation(
                ((screenSize.width) / 2) - ((win.getSize().width) / 2),
                ((screenSize.height) / 2) - ((win.getSize().height) / 2));
        win.show();

    }

    public void actionPerformed(final ActionEvent actionEvent) {
        if (actionEvent.getSource().equals(abort)) {
            done = true;
            win.hide();
        }

        if (actionEvent.getSource().equals(finished)) {
            nick = nickField.getText();
            full = fullField.getText();
            email = emailField.getText();
            done = true;
            addParty.updateNewPatron( this );
            win.hide();
        }

    }

    public boolean done() {
        return done;
    }

    public String getNick() {
        return nick;
    }

    public String getFull() {
        return full;
    }

    public String getEmail() {
        return email;
    }

}
