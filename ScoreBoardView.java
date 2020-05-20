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

import sun.invoke.empty.Empty;
import java.sql.*;

import java.util.*;

/**
 * Constructor for GUI used to Add Parties to the waiting party queue.
 *  
 */

public class ScoreBoardView implements ActionListener, ListSelectionListener {

    private int maxSize;

    private JFrame win;

    private JButton highestScore;
    private JButton lowestScore;
    private JButton Last5Score;
    private JButton finished;

    private JList scoreList;
    private  JList allBowlers;

    private Vector scores;
    private Vector bowlerdb;

//    private Integer lock;

    private ControlDeskView controlDesk;

    private String selectedNick;
    private String selectedMember;

    public static Vector query_db(int query_num,String Nick){
        String query ="Select * FROM SCOREBOARD;";
        if(Nick.equals("All")){
            switch( query_num){
                case 0: query="SELECT nick, date,max(score),score "
                        + "FROM SCOREBOARD ;";
                        break;
                case 1: query="SELECT nick, date,min(score),score "
                        + "FROM SCOREBOARD ;";
                        break;
                case 2: query="SELECT nick, date,score "
                        + "FROM SCOREBOARD ORDER BY sno DESC LIMIT 5 ;";
                        break;
            }    

        }
        else{
        switch( query_num){
            case 0: query="SELECT nick, date,max(score),score "
                    + "FROM SCOREBOARD WHERE nick = ?;";
                    break;
            case 1: query="SELECT nick, date,min(score),score "
                    + "FROM SCOREBOARD WHERE nick = ?;";
                    break;
            case 2: query="SELECT nick, date, score,score "
                    + "FROM SCOREBOARD WHERE nick = ? ORDER BY sno DESC LIMIT 5;";
                    break;
        }
        
    }
        Vector result=new Vector();
        result.add("   Nick    |     Date       |     Score");
        final Vector empty = new Vector();
        empty.add("   Nick    |     Date       |     Score");
        String url = "jdbc:sqlite:GAME_DATA.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
            ResultSet rs;
            if(Nick.equals("All")){
                Statement stmt = conn.createStatement();
                rs=stmt.executeQuery(query);
            }
            else{
            PreparedStatement stmt=conn.prepareStatement(query);
            stmt.setString(1, Nick);
            rs=stmt.executeQuery();
            }
            while (rs.next()) {
                String nick,date;
                int score;
                nick=rs.getString("nick");
                date=rs.getString("date");
                score=rs.getInt("score");
                if(nick == null || date == null )
                    return empty;
                result.add(nick + "       " +date+ "        " + score);
                System.out.println(nick +  "\t" + 
                                   date + "\t" +
                                   score);
            }
            return result;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            
            return empty;
        }

        
    }
    public ScoreBoardView(final ControlDeskView controlDesk, final int max) {

        this.controlDesk = controlDesk;
        maxSize = max;

        win = new JFrame("Score Board");
        win.getContentPane().setLayout(new BorderLayout());
        ((JPanel) win.getContentPane()).setOpaque(false);

        final JPanel colPanel = new JPanel();
        colPanel.setLayout(new GridLayout(1, 3));

        // Party Panel
        final JPanel scorePanel = new JPanel();
        scorePanel.setLayout(new FlowLayout());
        scorePanel.setBorder(new TitledBorder("Score Details"));

        scores = new Vector();
        final Vector empty = new Vector();
        empty.add("(Empty)");

        scoreList = new JList(empty);
        scoreList.setFixedCellWidth(250);
        scoreList.setVisibleRowCount(8);
        scoreList.addListSelectionListener(this);
        final JScrollPane scorePane = new JScrollPane(scoreList);
        //        partyPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scorePanel.add(scorePane);

        // Bowler Database
        final JPanel bowlerPanel = new JPanel();
        bowlerPanel.setLayout(new FlowLayout());
        bowlerPanel.setBorder(new TitledBorder("Bowler Database"));

        try {
            
            bowlerdb = new Vector(BowlerFile.getBowlers());
            bowlerdb.add(0,"All");
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

        highestScore = new JButton("Highest");
        final JPanel addPatronPanel = new JPanel();
        addPatronPanel.setLayout(new FlowLayout());
        highestScore.addActionListener(this);
        addPatronPanel.add(highestScore);

        lowestScore = new JButton("Lowest");
        final JPanel remPatronPanel = new JPanel();
        remPatronPanel.setLayout(new FlowLayout());
        lowestScore.addActionListener(this);
        remPatronPanel.add(lowestScore);

        Last5Score = new JButton("Last 5 Scores");
        final JPanel newPatronPanel = new JPanel();
        newPatronPanel.setLayout(new FlowLayout());
        Last5Score.addActionListener(this);
        newPatronPanel.add(Last5Score);

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
        colPanel.add(scorePanel);
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
        if (Event.getSource().equals(highestScore)) {
            if (selectedNick != null ) {
                if (scores.contains(selectedNick)) {
                    System.err.println("Member already in Selected");
                } else {
                    scores.clear();
                    scores=query_db(0, selectedNick);
                    // scores.add(selectedNick+"high");
                    scoreList.removeAll();
                    scoreList.setListData(scores);
                }
            }
            System.out.println(" Highest Pressed");
        }
        if (Event.getSource().equals(lowestScore)) {
            if (selectedNick != null ) {
                if (scores.contains(selectedNick)) {
                    System.err.println("Member already in Selected");
                } else {
                    scores.clear();
                    scores=query_db(1, selectedNick);
                    // scores.add(selectedNick+"low");
                    scoreList.removeAll();
                    scoreList.setListData(scores);
                }
            }
            System.out.println("Lowest Pressed");
        }
        if (Event.getSource().equals(Last5Score)) {
            if (selectedNick != null ) {
                if (scores.contains(selectedNick)) {
                    System.err.println("Member already in Selected");
                } else {
                    scores.clear();
                    scores=query_db(2, selectedNick);
                    // scores.add(selectedNick+"Last 5");
                    scoreList.removeAll();
                    scoreList.setListData(scores);
                }
            }
            // final NewPatronView newPatron = new NewPatronView( this );
            System.out.println("Last Five Pressed");

        }
        if (Event.getSource().equals(finished)) {
            // if ( party != null && !party.isEmpty()) {
            //     controlDesk.updateAddParty( this );
            // }
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
        if (Event.getSource().equals(scoreList)) {
            selectedMember =
                    (String) ((JList) Event.getSource()).getSelectedValue();
        }
    }

    // /**
    //  * Accessor for Party
    //  */

    // public Vector getNames() {
    //     return party;
    // }

    // /**
    //  * Called by NewPatronView to notify AddPartyView to update
    //  *
    //  * @param newPatron the NewPatronView that called this method
    //  */

    // public void updateNewPatron(final NewPatronView newPatron) {
    //     try {
    //         final Bowler checkBowler = BowlerFile.getBowlerInfo( newPatron.getNick() );
    //         if ( checkBowler == null ) {
    //             BowlerFile.putBowlerInfo(
    //                     newPatron.getNick(),
    //                     newPatron.getFull(),
    //                     newPatron.getEmail());
    //             bowlerdb = new Vector(BowlerFile.getBowlers());
    //             allBowlers.setListData(bowlerdb);
    //             party.add(newPatron.getNick());
    //             partyList.setListData(party);
    //         } else {
    //             System.err.println( "A Bowler with that name already exists." );
    //         }
    //     } catch (Exception e2) {
    //         System.err.println("File I/O Error");
    //     }
    // }

    // /**
    //  * Accessor for Party
    //  */

    // public Vector getParty() {
    //     return party;
    // }

}
