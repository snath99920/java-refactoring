import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.DatabaseMetaData;
import java.sql.Statement;

public class drive {

    public static boolean check_dataBase_connection(){
        try {
            Class.forName("org.sqlite.JDBC");
            String dbURL = "jdbc:sqlite:GAME_DATA.db";
            Connection conn = DriverManager.getConnection(dbURL);
            if (conn != null) {
                System.out.println("Connected to the database");
                DatabaseMetaData dm = (DatabaseMetaData) conn.getMetaData();
                System.out.println("Driver name: " + dm.getDriverName());
                System.out.println("Driver version: " + dm.getDriverVersion());
                System.out.println("Product name: " + dm.getDatabaseProductName());
                System.out.println("Product version: " + dm.getDatabaseProductVersion());
                String sql = "CREATE TABLE IF NOT EXISTS SCOREBOARD (\n"
                + "    sno integer PRIMARY KEY AUTOINCREMENT,\n"
                + "    nick text NOT NULL,\n"
                + "    date text NOT NULL,\n"
                + "    score integer NOT NULL\n"
                + ");";
                Statement stmt =conn.createStatement();
                stmt.execute(sql);
                conn.close();
                return true;
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;

    }
    public static void main(final String[] args) {

        int numLanes = 3;
        int maxPatrons = 7;

        final Alley alley = new Alley(numLanes);
        final ControlDesk controlDesk = alley.getControlDesk();
        check_dataBase_connection();
        final ControlDeskView controlDeskView = new ControlDeskView(controlDesk, maxPatrons);
        controlDesk.subscribe(controlDeskView);

    }
}
