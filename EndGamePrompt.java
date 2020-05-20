/**
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class EndGamePrompt implements ActionListener {

    private JFrame win;

    private JButton yesButton;
    private JButton noButton;

    private int result;

//    private String selectedNick;
//    private String selectedMember;

    public EndGamePrompt( final String partyName ) {

        result =0;

        win = new JFrame("Another Game for " + partyName + "?" );
        win.getContentPane().setLayout(new BorderLayout());
        ((JPanel) win.getContentPane()).setOpaque(false);

        final JPanel colPanel = new JPanel();
        colPanel.setLayout(new GridLayout( 2, 1 ));

        // Label Panel
        final JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new FlowLayout());

        final JLabel message = new JLabel( "Party " + partyName
                + " has finished bowling.\nWould they like to bowl another game?" );

        labelPanel.add( message );

        // Button Panel
        final JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2));

//        Insets buttonMargin = new Insets(4, 4, 4, 4);

        yesButton = new JButton("Yes");
        final JPanel yesButtonPanel = new JPanel();
        yesButtonPanel.setLayout(new FlowLayout());
        yesButton.addActionListener(this);
        yesButtonPanel.add(yesButton);

        noButton = new JButton("No");
        final JPanel noButtonPanel = new JPanel();
        noButtonPanel.setLayout(new FlowLayout());
        noButton.addActionListener(this);
        noButtonPanel.add(noButton);

        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);

        // Clean up main panel
        colPanel.add(labelPanel);
        colPanel.add(buttonPanel);

        win.getContentPane().add("Center", colPanel);

        win.pack();

        // Center Window on Screen
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        win.setLocation(
                (screenSize.width) / 2 - ((win.getSize().width) / 2),
                (screenSize.height) / 2 - ((win.getSize().height) / 2));
        win.show();

    }

    public void actionPerformed(final ActionEvent actionEvent) {
        if (actionEvent.getSource().equals(yesButton)) {
            result=1;
        }
        if (actionEvent.getSource().equals(noButton)) {
            result=2;
        }

    }

    public int getResult() {
        while ( result == 0 ) {
            try {
                Thread.sleep(10);
            } catch ( InterruptedException e ) {
                System.err.println( "Interrupted" );
            }
        }
        return result;
    }

    public void distroy() {
        win.hide();
    }

}

