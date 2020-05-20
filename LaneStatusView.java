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

public class LaneStatusView implements ActionListener, LaneObserver, PinsetterObserver {

    private JPanel jPanel;

    private JLabel curBowler;
    private JLabel foul;
    private JLabel pinsDown;

    private JButton viewLane;
    private JButton viewPinSetter;
    private JButton maintenance;

    private PinSetterView psv;
    private LaneView laneView;
    private Lane lane;
    int laneNum;

    boolean laneShowing;
    boolean psShowing;

    public LaneStatusView(final Lane lane, final int laneNum ) {

        this.lane = lane;
        this.laneNum = laneNum;

        laneShowing=false;
        psShowing=false;

        psv = new PinSetterView( laneNum );
        final Pinsetter pinSet = lane.getPinsetter();
        pinSet.subscribe(psv);

        laneView = new LaneView( lane, laneNum );
        lane.subscribe(laneView);


        jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout());
        final JLabel cLabel = new JLabel( "Now Bowling: " );
        curBowler = new JLabel( "(no one)" );
        final JLabel fLabel = new JLabel( "Foul: " );
        foul = new JLabel( " " );
        final JLabel pdLabel = new JLabel( "Pins Down: " );
        pinsDown = new JLabel( "0" );

        // Button Panel
        final JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

//        Insets buttonMargin = new Insets(4, 4, 4, 4);

        viewLane = new JButton("View Lane");
        final JPanel viewLanePanel = new JPanel();
        viewLanePanel.setLayout(new FlowLayout());
        viewLane.addActionListener(this);
        viewLanePanel.add(viewLane);

        viewPinSetter = new JButton("Pinsetter");
        final JPanel viewPins = new JPanel();
        viewPins.setLayout(new FlowLayout());
        viewPinSetter.addActionListener(this);
        viewPins.add(viewPinSetter);

        maintenance = new JButton("     ");
        maintenance.setBackground( Color.GREEN );
        final JPanel maintenancePanel = new JPanel();
        maintenancePanel.setLayout(new FlowLayout());
        maintenance.addActionListener(this);
        maintenancePanel.add(maintenance);

        viewLane.setEnabled( false );
        viewPinSetter.setEnabled( false );


        buttonPanel.add(viewLanePanel);
        buttonPanel.add(viewPins);
        buttonPanel.add(maintenancePanel);

        jPanel.add( cLabel );
        jPanel.add( curBowler );
//		jp.add( fLabel );
//		jp.add( foul );
        jPanel.add( pdLabel );
        jPanel.add( pinsDown );

        jPanel.add(buttonPanel);

    }

    public JPanel showLane() {
        return jPanel;
    }

    public void actionPerformed( final ActionEvent actionEvent ) {
        if ( lane.isPartyAssigned() && actionEvent.getSource().equals(viewPinSetter)) {
//            if (actionEvent.getSource().equals(viewPinSetter)) {
                if ( !psShowing ) {
                    psv.show();
                    psShowing=true;
                } else if ( psShowing ) {
                    psv.hide();
                    psShowing=false;
                }
//            }
        }
        if (actionEvent.getSource().equals(viewLane) && lane.isPartyAssigned()) {
//            if ( lane.isPartyAssigned() ) {
                if ( !laneShowing ) {
                    laneView.show();
                    laneShowing=true;
                } else if ( laneShowing ) {
                    laneView.hide();
                    laneShowing=false;
                }
//            }
        }
        if (actionEvent.getSource().equals(maintenance)) {
            if ( lane.isPartyAssigned() ) {
                lane.unPauseGame();
                maintenance.setBackground( Color.GREEN );
            }
        }
    }

    public void receiveLaneEvent(final LaneEvent laneEvent) {
        curBowler.setText( ( (Bowler)laneEvent.getBowler()).getNickName() );
        if ( laneEvent.isMechanicalProblem() ) {
            maintenance.setBackground( Color.RED );
        }
        if ( !lane.isPartyAssigned() ) {
            viewLane.setEnabled( false );
            viewPinSetter.setEnabled( false );
        } else {
            viewLane.setEnabled( true );
            viewPinSetter.setEnabled( true );
        }
    }

    public void receivePinsetterEvent(final PinsetterEvent pinSet) {
        pinsDown.setText( ( new Integer(pinSet.totalPinsDown()) ).toString() );
    }

}
