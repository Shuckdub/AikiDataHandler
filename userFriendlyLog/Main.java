import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args){
        Participants t = new Participants();
        //t.firstGetEachParticipant();

        Sessions s = new Sessions();
        s.covertEachParticipantIntoSessions();
    }
}