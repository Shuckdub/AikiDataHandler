import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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
        sb.append(participant + "," + time + "," + event + "," + url + "," + value + "\n");
    }

    public void printItAll(){
        try {
            File file = new File("./output/clean.csv");
            int i = 1;
            while(true){
                if(file.exists()){
                    file = new File("./output/clean" + i + ".csv");
                    i++;
                    continue;
                }
                FileWriter out = new FileWriter(file.getAbsolutePath());
                BufferedWriter bw = new BufferedWriter(out);
                bw.write(sb.toString());

                bw.close();

                sb.setLength(0);
                break;
            }
        } catch (Exception e) {
            //TODO: handle exception
        }
    }

}