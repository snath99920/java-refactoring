/**
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */

import java.util.*;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.DatabaseMetaData;
import java.sql.*;

public class ScoreHistoryFile {
    volatile static Connection conn; 

    public static void insert_db(String nick,String date,int score) {
        try {
            Class.forName("org.sqlite.JDBC");
            String dbURL = "jdbc:sqlite:GAME_DATA.db";
            conn = DriverManager.getConnection(dbURL);
            if (conn != null) {
                System.out.println("Connected to the database");
                DatabaseMetaData dm = (DatabaseMetaData) conn.getMetaData();
                System.out.println("Driver name: " + dm.getDriverName());
                System.out.println("Driver version: " + dm.getDriverVersion());
                System.out.println("Product name: " + dm.getDatabaseProductName());
                System.out.println("Product version: " + dm.getDatabaseProductVersion());
                String sql = "INSERT INTO SCOREBOARD (nick,date,score) VALUES(?,?,?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, nick);
                    pstmt.setString(2, date);
                    pstmt.setInt(3, score);
                    pstmt.executeUpdate();
                // conn.close();
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }



    private static String SCOREHISTORY_DAT = "SCOREHISTORY.DAT";

    
    public static void addScore(final String nick, final String date, final String score)
            throws IOException, FileNotFoundException {

        final String data = nick + "\t" + date + "\t" + score + "\n";

        final RandomAccessFile out = new RandomAccessFile(SCOREHISTORY_DAT, "rw");
        out.skipBytes((int) out.length());
        out.writeBytes(data);
        insert_db(nick,date,Integer.parseInt(score));
        out.close();
    }

    public static Vector getScores(final String nick)
            throws IOException, FileNotFoundException {
        final Vector scores = new Vector();

        final BufferedReader in_var =
                new BufferedReader(new FileReader(SCOREHISTORY_DAT));
        String data;
        while ((data = in_var.readLine()) != null) {
            // File format is nick\tfname\te-mail
            final String[] scoredata = data.split("\t");
            //"Nick: scoredata[0] Date: scoredata[1] Score: scoredata[2]
            if (nick.equals(scoredata[0])) {
                scores.add(new Score(scoredata[0], scoredata[1], scoredata[2]));
            }
        }
        return scores;
    }

}
