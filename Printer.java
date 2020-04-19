import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Printer {
    private StringBuilder sb;

    public Printer(){
        sb = new StringBuilder();
    }

    public void printer(String participant, Date time, String event, String url, String value){
        sb.append(participant + "," + timePrinter(time) + "," + event + "," + url + "," + value + "\n");
    }

    public String timePrinter(Date times){
        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        return df.format(times);
    }

    public void printTitle(){
        String titlesOfTheColoumns = "Participant,Timestamp,event,url,value";
        sb.append(titlesOfTheColoumns + "\n");
    }

    public void printEnding(String participant, String time, String event, String url, String value){
        sb.append(participant + "," + time + "," + event + "," + url + "," + value);
        System.out.println(sb);
        sb.setLength(0);
    }

}