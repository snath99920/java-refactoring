/**
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */

public class Score {

    private String nick;
    private String date;
    private String score;

    public Score( final String nick, final String date, final String score ) {
        this.nick=nick;
        this.date=date;
        this.score=score;
    }

    public String getNickName() {
        return nick;
    }

    public String getDate() {
        return date;
    }

    public String getScore() {
        return score;
    }

    public String toString() {
        return nick + "\t" + date + "\t" + score;
    }

}
