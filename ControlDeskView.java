/* ControlDeskView.java
 *
 *  Version:
 *			$Id$
 *
 *  Revisions:
 * 		$Log$
 *
 */

/**
 * Class for representation of the control desk
 *
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;

public class ControlDeskView implements ActionListener, ControlDeskObserver {

    private JButton addParty;
    private JButton finished;
    private JButton assign;
    private JButton leaderboard;

    private JFrame win;

    private JList partyList;

    /** The maximum  number of members in a party */
    private int maxMembers;

    private ControlDesk controlDesk;

    /**
     * Displays a GUI representation of the ControlDesk
     *
     */

    public ControlDeskView(final ControlDesk controlDesk, final int maxMembers) {

        this.controlDesk = controlDesk;
        this.maxMembers = maxMembers;
        final int numLanes = controlDesk.getNumLanes();

        win = new JFrame("Control Desk");
        win.getContentPane().setLayout(new BorderLayout());
        ((JPanel) win.getContentPane()).setOpaque(false);

        final JPanel colPanel = new JPanel();
        colPanel.setLayout(new BorderLayout());

        // Controls Panel
        final JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new GridLayout(3, 1));
        controlsPanel.setBorder(new TitledBorder("Controls"));

        addParty = new JButton("Add Party");
        final JPanel addPartyPanel = new JPanel();
        addPartyPanel.setLayout(new FlowLayout());
        addParty.addActionListener(this);
        addPartyPanel.add(addParty);
        controlsPanel.add(addPartyPanel);

        assign = new JButton("Assign Lanes");
        final JPanel assignPanel = new JPanel();
        assignPanel.setLayout(new FlowLayout());
        assign.addActionListener(this);
        assignPanel.add(assign);
//		controlsPanel.add(assignPanel);

        leaderboard = new JButton("ScoreBoard");
        final JPanel scoresPanel = new JPanel();
        scoresPanel.setLayout(new FlowLayout());
        leaderboard.addActionListener(this);
        scoresPanel.add(leaderboard);
        controlsPanel.add(scoresPanel);

        finished = new JButton("Finished");
        final JPanel finishedPanel = new JPanel();
        finishedPanel.setLayout(new FlowLayout());
        finished.addActionListener(this);
        finishedPanel.add(finished);
        controlsPanel.add(finishedPanel);

        // Lane Status Panel
        final JPanel laneStatusPanel = new JPanel();
        laneStatusPanel.setLayout(new GridLayout(numLanes, 1));
        laneStatusPanel.setBorder(new TitledBorder("Lane Status"));

        final HashSet lanes=controlDesk.getLanes();
        final Iterator run = lanes.iterator();
        int laneCount=0;

        while (run.hasNext()) {
            final Lane curLane = (Lane) run.next();
            final LaneStatusView laneStat = new LaneStatusView(curLane,laneCount+1);
            curLane.subscribe(laneStat);
            ((Pinsetter)curLane.getPinsetter()).subscribe(laneStat);
            final JPanel lanePanel = laneStat.showLane();
            lanePanel.setBorder(new TitledBorder("Lane" + ++laneCount ));
            laneStatusPanel.add(lanePanel);
        }

        // Party Queue Panel
        final JPanel partyPanel = new JPanel();
        partyPanel.setLayout(new FlowLayout());
        partyPanel.setBorder(new TitledBorder("Party Queue"));

        final Vector empty = new Vector();
        empty.add("(Empty)");

        partyList = new JList(empty);
        partyList.setFixedCellWidth(120);
        partyList.setVisibleRowCount(10);
        final JScrollPane partyPane = new JScrollPane(partyList);
        partyPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        partyPanel.add(partyPane);
        //		partyPanel.add(partyList);

        // Clean up main panel
        colPanel.add(controlsPanel, "East");
        colPanel.add(laneStatusPanel, "Center");
        colPanel.add(partyPanel, "West");

        win.getContentPane().add("Center", colPanel);

        win.pack();

        /* Close program when this window closes */
        win.addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        // Center Window on Screen
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        win.setLocation(
                screenSize.width / 2 - ((win.getSize().width) / 2),
                screenSize.height / 2 - ((win.getSize().height) / 2));
        win.show();

    }

    /**
     * Handler for actionEvents
     *
     * @param actionEvent the ActionEvent that triggered the handler
     *
     */

    public void actionPerformed(final ActionEvent actionEvent) {
        if (actionEvent.getSource().equals(addParty)) {
            final AddPartyView addPartyWin = new AddPartyView(this, maxMembers);
        }
        if (actionEvent.getSource().equals(assign)) {
            controlDesk.assignLane();
        }
        if (actionEvent.getSource().equals(leaderboard)) {
            final ScoreBoardView ScoreBoardWin = new ScoreBoardView(this, maxMembers);
        }
        if (actionEvent.getSource().equals(finished)) {
            win.hide();
            System.exit(0);
        }
    }

    /**
     * Receive a new party from andPartyView.
     *
     * @param addPartyView	the AddPartyView that is providing a new party
     *
     */

    public void updateAddParty(final AddPartyView addPartyView) {
        controlDesk.addPartyQueue(addPartyView.getParty());
    }

    /**
     * Receive a broadcast from a ControlDesk
     *
     * @param deskEvent	the ControlDeskEvent that triggered the handler
     *
     */

    public void receiveControlDeskEvent(final ControlDeskEvent deskEvent) {
        partyList.setListData((Vector) deskEvent.getPartyQueue());
    }
}
