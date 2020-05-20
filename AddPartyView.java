/* AddPartyView.java
 *
 *  Version:
 * 		 $Id$
 *
 *  Revisions:
 * 		$Log: AddPartyView.java,v $
 * 		Revision 1.7  2003/02/20 02:05:53  ???
 * 		Fixed addPatron so that duplicates won't be created.
 *
 * 		Revision 1.6  2003/02/09 20:52:46  ???
 * 		Added comments.
 *
 * 		Revision 1.5  2003/02/02 17:42:09  ???
 * 		Made updates to migrate to observer model.
 *
 * 		Revision 1.4  2003/02/02 16:29:52  ???
 * 		Added ControlDeskEvent and ControlDeskObserver. Updated Queue to allow access to Vector so that contents could be viewed without destroying. Implemented observer model for most of ControlDesk.
 *
 *
 */

/**
 * Class for GUI components need to add a party
 *
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import java.util.*;

/**
 * Constructor for GUI used to Add Parties to the waiting party queue.
 *
 */

public class AddPartyView implements ActionListener, ListSelectionListener {

    private int maxSize;

    private JFrame win;

    private JButton addPatron;
    private JButton newPatron;
    private JButton remPatron;
    private JButton finished;

    private JList partyList;
    private  JList allBowlers;

    private Vector party;
    private Vector bowlerdb;

//    private Integer lock;

    private ControlDeskView controlDesk;

    private String selectedNick;
    private String selectedMember;

    public AddPartyView(final ControlDeskView controlDesk, final int max) {

        this.controlDesk = controlDesk;
        maxSize = max;

        win = new JFrame("Add Party");
        win.getContentPane().setLayout(new BorderLayout());
        ((JPanel) win.getContentPane()).setOpaque(false);

        final JPanel colPanel = new JPanel();
        colPanel.setLayout(new GridLayout(1, 3));

        // Party Panel
        final JPanel partyPanel = new JPanel();
        partyPanel.setLayout(new FlowLayout());
        partyPanel.setBorder(new TitledBorder("Your Party"));

        party = new Vector();
        final Vector empty = new Vector();
        empty.add("(Empty)");

        partyList = new JList(empty);
        partyList.setFixedCellWidth(120);
        partyList.setVisibleRowCount(5);
        partyList.addListSelectionListener(this);
        final JScrollPane partyPane = new JScrollPane(partyList);
        //        partyPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        partyPanel.add(partyPane);

        // Bowler Database
        final JPanel bowlerPanel = new JPanel();
        bowlerPanel.setLayout(new FlowLayout());
        bowlerPanel.setBorder(new TitledBorder("Bowler Database"));

        try {
            bowlerdb = new Vector(BowlerFile.getBowlers());
        } catch (Exception e) {
            System.err.println("File Error");
            bowlerdb = new Vector();
        }
        allBowlers = new JList(bowlerdb);
        allBowlers.setVisibleRowCount(8);
        allBowlers.setFixedCellWidth(120);
        final JScrollPane bowlerPane = new JScrollPane(allBowlers);
        bowlerPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        allBowlers.addListSelectionListener(this);
        bowlerPanel.add(bowlerPane);

        // Button Panel
        final JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1));

//        Insets buttonMargin = new Insets(4, 4, 4, 4);

        addPatron = new JButton("Add to Party");
        final JPanel addPatronPanel = new JPanel();
        addPatronPanel.setLayout(new FlowLayout());
        addPatron.addActionListener(this);
        addPatronPanel.add(addPatron);

        remPatron = new JButton("Remove Member");
        final JPanel remPatronPanel = new JPanel();
        remPatronPanel.setLayout(new FlowLayout());
        remPatron.addActionListener(this);
        remPatronPanel.add(remPatron);

        newPatron = new JButton("New Patron");
        final JPanel newPatronPanel = new JPanel();
        newPatronPanel.setLayout(new FlowLayout());
        newPatron.addActionListener(this);
        newPatronPanel.add(newPatron);

        finished = new JButton("Finished");
        final JPanel finishedPanel = new JPanel();
        finishedPanel.setLayout(new FlowLayout());
        finished.addActionListener(this);
        finishedPanel.add(finished);

        buttonPanel.add(addPatronPanel);
        buttonPanel.add(remPatronPanel);
        buttonPanel.add(newPatronPanel);
        buttonPanel.add(finishedPanel);

        // Clean up main panel
        colPanel.add(partyPanel);
        colPanel.add(bowlerPanel);
        colPanel.add(buttonPanel);

        win.getContentPane().add("Center", colPanel);

        win.pack();

        // Center Window on Screen
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        win.setLocation(
                screenSize.width / 2 - (win.getSize().width) / 2,
                screenSize.height / 2 - (win.getSize().height) / 2);
        win.show();

    }

    public void actionPerformed(final ActionEvent Event) {
        if (Event.getSource().equals(addPatron)) {
            if (selectedNick != null && party.size() < maxSize) {
                if (party.contains(selectedNick)) {
                    System.err.println("Member already in Party");
                } else {
                    party.add(selectedNick);
                    partyList.setListData(party);
                }
            }
        }
        if (Event.getSource().equals(remPatron)) {
            if (selectedMember != null) {
                party.removeElement(selectedMember);
                partyList.setListData(party);
            }
        }
        if (Event.getSource().equals(newPatron)) {
            final NewPatronView newPatron = new NewPatronView( this );
        }
        if (Event.getSource().equals(finished)) {
            if ( party != null && !party.isEmpty()) {
                controlDesk.updateAddParty( this );
            }
            win.hide();
        }

    }

    /**
     * Handler for List actions
     * @param Event the ListActionEvent that triggered the handler
     */

    public void valueChanged(final ListSelectionEvent Event) {
        if (Event.getSource().equals(allBowlers)) {
            selectedNick =
                    (String) ((JList) Event.getSource()).getSelectedValue();
        }
        if (Event.getSource().equals(partyList)) {
            selectedMember =
                    (String) ((JList) Event.getSource()).getSelectedValue();
        }
    }

    /**
     * Accessor for Party
     */

    public Vector getNames() {
        return party;
    }

    /**
     * Called by NewPatronView to notify AddPartyView to update
     *
     * @param newPatron the NewPatronView that called this method
     */

    public void updateNewPatron(final NewPatronView newPatron) {
        try {
            final Bowler checkBowler = BowlerFile.getBowlerInfo( newPatron.getNick() );
            if ( checkBowler == null ) {
                BowlerFile.putBowlerInfo(
                        newPatron.getNick(),
                        newPatron.getFull(),
                        newPatron.getEmail());
                bowlerdb = new Vector(BowlerFile.getBowlers());
                allBowlers.setListData(bowlerdb);
                party.add(newPatron.getNick());
                partyList.setListData(party);
            } else {
                System.err.println( "A Bowler with that name already exists." );
            }
        } catch (Exception e2) {
            System.err.println("File I/O Error");
        }
    }

    /**
     * Accessor for Party
     */

    public Vector getParty() {
        return party;
    }

}
