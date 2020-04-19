import java.util.Date;
import java.util.Scanner;

/**
 * 
 * Takes a txt or json file and turns it into a csv file.
 * This is done by utilize the Regex class to 
 * find specific patterns in the files.
 * The Datahandler will then provide the Printer class 
 * with the information needed to generate the new csv file.
 * This class only works with datasets collected from Aiki
 * @author Gabriel Agger-Nielsen
 * 
 */
public class DataHandler{
    String participant = "";
    String event = "";
    Date time = null;
    String url = "";
    static Scanner sc = new Scanner(System.in);
    static Printer p = new Printer();
    public static void main(String[] args) {
        DataHandler d = new DataHandler();
        while(sc.hasNext()){
            String lines = sc.nextLine();
            if(lines.length() > 60) {
                p.printTitle();
                d.changeParticipant(lines);
                while(sc.hasNext()){
                    String newParticipant = lines.replaceAll(Regex.regexIt("participant"), "");
                    if(newParticipant.length() > 59) {
                        d.changeParticipant(newParticipant);
                    }
                    lines = sc.nextLine();
                    if(!lines.matches(".*collections.*")){
                        d.theParticipantsActivity(lines);
                    } else if (lines.matches(".*collections.*")){
                        d.ending();
                    } else {
                        continue;
                    }
                }
            }
        }
        sc.close();
    }
    /**
     * This method is used to sort out what the participant did
     * at the given time. The method does this by scanning the lines
     * given to the DataHandler for clues.
     * @param lines
     */
    private void theParticipantsActivity(String lines){
        String mightBeTime = lines.replaceAll(Regex.regexIt("time"), "");
        if(mightBeTime.length() == 13){
            time = timeConverter(mightBeTime);
            if(lines.matches(".*\\{\\}.*")){
                closedTheInterception();
            } else if(lines.matches(".*[Sa-z]+.*")){
                exerciseSuccessOrFailure(lines);
            } else {
                lines = sc.nextLine().toLowerCase();
                if(lines.matches(".*zeeguu.*") || lines.matches(".*exerciseduration.*") ){
                    exerciseStats(lines);             
                } else if(lines.replaceAll(Regex.regexIt("time"), "").length() == 13) {
                    timeOut(lines);
                } else if (lines.matches(".*enabled.*")){
                    enabled(lines);
                }else {
                    event = lines.replaceAll(Regex.regexIt("event"), "");
                    eventHandling(lines);
                }
            }
        }
    }

    /**
     * This method works when one participant's 
     * activity returns an array of tasks.
     * @param lines
     */
    private void eventHandling(String lines){
        while(sc.hasNext()){
            lines = sc.nextLine();
            if(lines.matches(".*},.*") || lines.matches(".*].*")){
                return;
            } else if(lines.matches(".*}.*")){
                if(lines.matches(".*},.*")){
                    return;
                }
                continue;
            }else if(event.equals("blockedurls")){
                blockedurls(lines);
            } else {
                prepIntercepts(lines);
            }
        }
    }

    /**
     * This handles everytime the participants
     * adds or removes blocked urls in the dataset.
     * @param lines
     */
    private void blockedurls(String lines){
        while(sc.hasNext()){
            if(lines.matches(".*\\{.*")){
                for(int i = 0 ; i < 8 ; i++){
                    lines = sc.nextLine();
                        if(lines.matches(".*hostname.*")){
                            prepBlockedUrls(lines);
                        }
                }
            }
            lines = sc.nextLine();
            if(lines.matches(".*\\].*")){
                return;
            }
        }
    }

    /**
     * Changes the participant
     */
    private void changeParticipant(String lines){
        participant = lines.replaceAll(Regex.regexIt("participant"), "");
    }

    /**
     * This marks the end of the dataset for the participant.
     */
    private void ending(){
        p.printEnding(participant, "", "end", "", "");
    }

    /**
     * This method prepares the data for the Printer class,
     * when the user somehow turned the timeout on or off.
     * @param lines
     */
    private void timeOut(String lines){
        url = lines.replaceAll(Regex.regexIt("url"), "");
        Date timeout = timeConverter(lines.replaceAll(Regex.regexIt("time"), ""));
        if(time.compareTo(timeout) < 0) {
            event = "timeout-on";
            p.printer(participant, time, event, url, p.timePrinter(timeout));
        } else {
            event = "timeout-off";
            p.printer(participant, time, event, url, p.timePrinter(timeout));
        }
    }

    private Date timeConverter(String times){
        Long convertedTime = Long.parseLong(times);
        Date currentDate = new Date(convertedTime);
        return currentDate;
    }

    /**
     * This method prepares the data for the Printer class,
     * when the user used the toggle to enable or disable
     * the extension.
     * @param lines
     */
    private void enabled(String lines){
        event = lines.replaceAll(Regex.regexIt("event"), "").substring(0, 7);
        String value = lines.replaceAll(Regex.regexIt("event"), "").substring(7);
        p.printer(participant, time, event, url, value);
    }

    /**
     * This method prepares the data for the Printer class,
     * when the user used has ended an exercise session.
     * @param lines
     */
    private void exerciseSuccessOrFailure(String lines) {
        String value = lines.replaceAll(Regex.regexIt("event"), "");
        event = "exercisestatus";
        url = "";
        p.printer(participant,time,event,url,value);
    }

    /**
     * This method prepares the data for the Printer class,
     * when the user do exercises or changes the exercise duration.
     * @param lines
     */
    private void exerciseStats(String lines){
        event = lines.replaceAll(Regex.regexIt("event"), "");
        url = "";
        String value = lines.replaceAll(Regex.regexIt("time"), "");
        p.printer(participant, time, event, url, value);
    }

    /**
     * This method prepares the data for the Printer class,
     * when the user was intercepted.
     * @param lines
     */
    private void prepIntercepts(String lines){
        url = lines.replaceAll(Regex.regexIt("url"), "");
        String value = lines.replaceAll(Regex.regexIt("time"), "");
        p.printer(participant, time, event, url, value);
    }

    /**
     * This method prepares the data for the Printer class,
     * when the user was added or removed blocked sites.
     * @param lines
     */
    private void prepBlockedUrls(String lines){
        url = lines.replaceAll(Regex.regexIt("url"), "");
        url = url.substring(8);
        p.printer(participant, time, event, url, "");
    }
    
    /**
     * This method prepares the data for the Printer class,
     * when the user was intercepted.
     * @param lines
     */
    private void closedTheInterception(){
        event = "intercepted";
        url = "";
        p.printer(participant, time, event, url, "closed");
    }
}