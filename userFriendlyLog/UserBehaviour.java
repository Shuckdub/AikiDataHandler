import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class UserBehaviour {
    private String participant ="";
    private int totalSessions = 0;
    private int totalsessionSuccesses = 0;
    private int totalsessionSkipped = 0;
    private int totalsessionInterrupted = 0;
    private int longestSession = 0;
    private int totalSessionLength = 0;
    private int firstDay = 0;
    private int lastDay = 0;
    private int lastActiveDay = 0;
    private int activeDays = 0;
    private boolean wasItDisabled = false;
    private boolean wasItEnabled = true;
    private String disableTime = "-1";
    private long disabledif = 0;

    Printer p = new Printer();

    public void printOneFile(String placement, String fileName){
        p.printOneBigFile(placement, fileName);
    }
    public void addTitles(){
        String titles = "Participant,Total sessions,Total time spent in sessions"
                        +",Longest session,Amount of successful sessions"
                        +",Amount of skipped sessions,Amount of interrupted sessions"
                        +",Did the user disable Aiki?,Was Aiki enabled in the end?"
                        +",How many days was Aiki disabled?,Total days of activity";
        p.addToPrinter(titles);
    }

    public void calculate( List<String[]> data){
        participant = data.get(1)[0];
        for (int i = 1 ; i < data.size(); i++) {
            overallStats(data.get(i));
        }
        lastDay = Integer.parseInt(data.get(data.size()-1)[1].substring(0,2));
        if(lastDay != lastActiveDay){
            activeDays++;
        }
        summarizeTotal();
    }

    private void overallStats(String[] data){
        int date = Integer.parseInt(data[1].substring(0, 2));
        getActiveDays(date);

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
                if(session > longestSession){
                    longestSession = session;
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

    private void getActiveDays(int date){
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

    private void summarizeTotal(){
        p.addToPrinter(printAble(formattingPrinting(longestSession),formattingPrinting(totalSessionLength)));
        resetForNextParticipant();
    }

    private String formattingPrinting(int input) {
        return String.format("%02d:%02d:%02d", 
                            TimeUnit.MILLISECONDS.toHours(input),
                            TimeUnit.MILLISECONDS.toMinutes(input) -  
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(input)),
                            TimeUnit.MILLISECONDS.toSeconds(input) - 
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(input))
        ); 
    }

    private String printAble(String formatLongest, String formatTotalSession) {
        return participant 
                + "," + totalSessions
                + "," + formatTotalSession 
                + "," + formatLongest 
                + "," + totalsessionInterrupted 
                + "," + totalsessionSkipped 
                + "," + totalsessionSuccesses
                + "," + wasItDisabled 
                + "," + wasItEnabled
                + "," + disabledif 
                + "," + activeDays;
    }
    
    private void resetForNextParticipant(){
        totalSessions = 0;
        totalsessionSuccesses = 0;
        totalsessionSkipped = 0;
        totalsessionInterrupted = 0;
        longestSession = 0;
        totalSessionLength = 0;
        firstDay = 0;
        lastDay = 0;
        lastActiveDay = 0;
        activeDays = 0;
        wasItDisabled = false;
        wasItEnabled = true;
        disableTime = "";
        disabledif = 0;
    }

}