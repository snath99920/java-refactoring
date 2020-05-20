
import java.io.*;
import java.util.Vector;
import java.util.Iterator;
import java.net.*;
import java.awt.print.*;
import java.util.logging.Logger;
import java.util.logging.Level;

public class ScoreReport {

    Logger logger = Logger.getLogger(ScoreReport.class.getName());

    private String content;

//    Logger logger = Logger.getLogger(com.dytech.common.log4j.DailySizeRollingAppender.class);

    public ScoreReport(final Bowler bowler, final int[] scores, final int games) {
        final String nick = bowler.getNick();
        final String full = bowler.getFullName();
        Vector vector = null;
        try {
            vector = ScoreHistoryFile.getScores(nick);
        } catch (Exception e) {
            System.err.println("Error: " + e);
        }

        final Iterator scoreIt = vector.iterator();

        content = "";
        content += "--Lucky Strike Bowling Alley Score Report--\n";
        content += "\n";
        content += "Report for " + full + ", aka \"" + nick + "\":\n";
        content += "\n";
        content += "Final scores for this session: ";
        content += scores[0];
        for (int i = 1; i < games; i++) {
            content += ", " + scores[i];
        }
        content += ".\n";
        content += "\n";
        content += "\n";
        content += "Previous scores by date: \n";
        while (scoreIt.hasNext()) {
            final Score score = (Score) scoreIt.next();
            content += "  " + score.getDate() + " - " + score.getScore();
            content += "\n";
        }
        content += "\n\n";
        content += "Thank you for your continuing patronage.";

    }

    public void sendEmail(final String recipient) {
        try {
            final Socket socket = new Socket("osfmail.rit.edu", 25);
            final BufferedReader in_var = new BufferedReader(new InputStreamReader(socket.getInputStream(), "8859_1"));
            final BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "8859_1"));

//            String boundary = "DataSeparatorString";

            // here you are supposed to send your username
            sendln(in_var, out, "HELO world");
            sendln(in_var, out, "MAIL FROM: <mda2376@rit.edu>");
            sendln(in_var, out, "RCPT TO: <" + recipient + ">");
            sendln(in_var, out, "DATA");
            sendln(out, "Subject: Bowling Score Report ");
            sendln(out, "From: <Lucky Strikes Bowling Club>");

            sendln(out, "Content-Type: text/plain; charset=\"us-ascii\"\r\n");
            sendln(out, content + "\n\n");
            sendln(out, "\r\n");

            sendln(in_var, out, ".");
            sendln(in_var, out, "QUIT");
            socket.close();
        } catch (Exception e) {
//            e.printStackTrace();
            logger.log(Level.SEVERE, "This is fatal!", e);
        }
    }

    public void sendPrintout() {
        final PrinterJob job = PrinterJob.getPrinterJob();

        final PrintableText printobj = new PrintableText(content);

        job.setPrintable(printobj);

        if (job.printDialog()) {
            try {
                job.print();
            } catch (PrinterException e) {
                System.out.println(e);
            }
        }

    }

    public void sendln(final BufferedReader in_var, final BufferedWriter out_var, String str) {
        try {
            out_var.write(str + "\r\n");
            out_var.flush();
            // System.out.println(s);
            str = in_var.readLine();
            // System.out.println(s);
        } catch (Exception e) {
//            e.printStackTrace();
            logger.log(Level.SEVERE, "This is fatal!", e);
        }
    }

    public void sendln(final BufferedWriter out_var, final String str) {
        try {
            out_var.write(str + "\r\n");
            out_var.flush();
            System.out.println(str);
        } catch (Exception e) {
//            e.printStackTrace();
            logger.log(Level.SEVERE, "This is fatal!", e);
        }
    }

}
