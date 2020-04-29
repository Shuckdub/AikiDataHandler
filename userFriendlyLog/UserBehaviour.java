import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class UserBehaviour {
    private int totalSessions = 0;
    private int totalsessionSuccesses = 0;
    private int totalsessionSkipped = 0;
    private int totalsessionInterrupted = 0;
    private int longestSessionOverall = 0;
    private int totalSessionLength = 0;
    private int firstDay = 0;
    private int lastDay = 0;
    private int lastActiveDay = 0;
    private int activeDays = 0;
    private boolean wasItDisabled = false;
    private boolean wasItEnabled = true;
    private String disableTime;
    private long disabledif = 0;


    Printer p = new Printer();

    public void calculate( List<String[]> data){
        for (int i = 1 ; i < data.size(); i++) {
            overallStats(data.get(i));
        }
        lastDay = Integer.parseInt(data.get(data.size()-1)[1].substring(0,2));
        if(lastDay != lastActiveDay){
            activeDays++;
        }
        summarizeTotal(data.get(1));
    }

    private void overallStats(String[] data){
        int date = Integer.parseInt(data[1].substring(0, 2));
        if(firstDay == 0 ){
            firstDay = date;
            lastActiveDay = date;
            activeDays++;
        } else {
            if(lastActiveDay < date){
                lastActiveDay = date;
                activeDays++;
            }
        }


        String event = data[2];
        String value = data[4];
        switch (event) {
            case "intercepts":
                           
                break;
            case "extension-was":
                if(value.equals("enabled")){
                    enabled(data);
                } else{
                    disableTime = data[1];
                    wasItEnabled = false;
                    wasItDisabled = true;
                }

                break;
            case "length-of-session":
                int session = Integer.parseInt(value);
                if(session > longestSessionOverall){
                    longestSessionOverall = session;
                }
                totalSessionLength += session;
                break;
            case "exercise-was":
                exerciseWas(value);
                break;
            default:
                break;
        }
    }

    private void enabled(String[] data){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss");
        LocalDate d1 = LocalDate.parse(disableTime, formatter);
        LocalDate d2 = LocalDate.parse(data[1], formatter);
        Duration diff = Duration.between(d1.atStartOfDay(), d2.atStartOfDay());
        if(disabledif == 0 ){
            disabledif = diff.toDays();
        } else {
            disabledif += diff.toDays();
        }
        wasItEnabled = true;
    }

    private void exerciseWas(String value){
        if(value.equals("Succes")){
            totalsessionSuccesses++;
            totalSessions++;
        } else if(value.equals("Skipped")){
            totalsessionSkipped++;
            totalSessions++;
        } else {
            totalsessionInterrupted++;
            totalSessions++;
        }
    }

    public void summarizeTotal(String[] data){
        // System.out.print("Total: " + totalSessions);
        // System.out.print(data[0] + ", Total spent in sessions: " + totalSessionLength);
        System.out.print(data[0] + ", Longest session " + longestSessionOverall);
        // System.out.print(", Succes: " + totalsessionSuccesses);
        // System.out.print(", Skipped: " + totalsessionSkipped);
        // System.out.print(", Interrupted: " + totalsessionInterrupted);
        // System.out.print(", Did the user disable Aiki? " + wasItDisabled);
        // System.out.print(", Was Aiki enabled in the end? " + wasItEnabled);
        System.out.println();
        // System.out.println("participant,total sessions,total time spent in session,longest session,successful sessions,skipped sessions,interrupted sessions,did the user disable Aiki,in the end was Aiki enabled,how many days was it disabled, first active day, last active day, total days of activity");
        // System.out.println(data[0] + "," + totalSessions + "," + totalSessionLength + "," + longestSessionOverall + "," + totalsessionSuccesses + "," + totalsessionSkipped +  "," + totalsessionInterrupted + "," + wasItDisabled + "," + wasItEnabled + "," +  disabledif);
        resetForNextParticipant();
    }

    private void resetForNextParticipant(){
        totalSessions = 0;
        totalsessionSuccesses = 0;
        totalsessionSkipped = 0;
        totalsessionInterrupted = 0;
        longestSessionOverall = 0;
        totalSessionLength = 0;
        firstDay = 0;
        wasItDisabled = false;
        wasItEnabled = true;
        disableTime = "";
        disabledif = 0;
    }
}