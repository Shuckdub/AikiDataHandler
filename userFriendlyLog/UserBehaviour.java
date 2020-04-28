import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class UserBehaviour {
    private int totalSessions = 0;
    private int totalsessionSuccesses = 0;
    private int totalsessionSkipped = 0;
    private int totalsessionInterrupted = 0;
    private int longestSessionOverall = 0;
    private int averageLengthOverall = 0;
    private int sessionsPerDay = 0;
    private ArrayList<Integer> sessionsOnAllTheDays = new ArrayList<>();
    private int longestDisableTimeInDays = 0;
    private int day = 0;
    private int dailyTotalSessions = 0;
    private int sessionSuccesses = 0;
    private int sessionSkipped = 0;
    private int sessionInterrupted = 0;
    private int longestSession = 0;
    private boolean wasItDisabled = false;
    private boolean wasItEnabled = true;
    private String disableTime;
    private long disabledif = 0;


    Printer p = new Printer();

    public void calculate( List<String[]> data){
        for (String[] d : data) {
            overallStats(d);
        }
        summarizeTotal(data.get(1));
    }

    private void overallStats(String[] data){
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
                
                break;
            case "exercise-was":
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

    private void dailyStats(String time, String event, String value){
        int currentDay = Integer.parseInt(time.substring(0, 2));
        System.out.println(currentDay);
        if(day == 0){
            day = currentDay;
        }
        if(day < currentDay){
            while(day < currentDay){
                day++;
                summarizeDaily();
            }
        }
        switch (event) {
            case "intercepts":
                           
                break;
            case "enabled":

                break;
            case "zeeguu":
                int session = Integer.parseInt(value);
                if(session > longestSession){
                    longestSession = session;
                }
                break;
            case "exercisestatus":
                if(value.equals("Succes")){
                    sessionSuccesses++;
                    dailyTotalSessions++;
                } else if(value.equals("Skipped")){
                    sessionSkipped++;
                    dailyTotalSessions++;
                } else {
                    sessionInterrupted++;
                    dailyTotalSessions++;
                }
                break;
            default:
                break;
        }
    }
    public void summarizeDaily(){
        System.out.print("Total: " + dailyTotalSessions + ",");
        System.out.print("Succes: " + sessionSuccesses + ",");
        System.out.print("Skipped: " + sessionSkipped + ",");
        System.out.println("Interrupted: " + sessionInterrupted + ",");
        dailyTotalSessions = 0;
        sessionSuccesses = 0;
        sessionSkipped = 0;
        sessionInterrupted = 0;
    }

    public void summarizeTotal(String[] data){
        // System.out.println("Total: " + totalSessions);
        // System.out.println("Succes: " + totalsessionSuccesses);
        // System.out.println("Skipped: " + totalsessionSkipped);
        // System.out.println("Interrupted: " + totalsessionInterrupted);
        System.out.println(data[0] + ": Disabled " + disabledif + "," + wasItDisabled + "," + wasItEnabled);
        totalSessions = 0;
        totalsessionSuccesses = 0;
        totalsessionSkipped = 0;
        totalsessionInterrupted = 0;
        disabledif = 0;
        wasItDisabled = false;
    }
}