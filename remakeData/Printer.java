import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Printer {
    private ArrayList<String[]> sb;

    public Printer(){
        sb = new ArrayList<>();
    }

    public void printer(String[] data){
        sb.add(data);
    }

    public String timePrinter(Date times){
        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        return df.format(times);
    }

    public void printEnding(String[] data){
        sb.add(data);
    }

    public void printItAll(){
        try {
            File file = new File("./output/clean.csv");
            int i = 1;
            sortItAll();
            StringBuilder s = new StringBuilder();
            s.append("Participant,Timestamp,event,url,value" + "\n");
            for (String[] strings : sb) {
                s.append(String.join(",",strings) + "\n");
            }
            while(true){
                if(file.exists()){
                    file = new File("./output/clean" + i + ".csv");
                    i++;
                    continue;
                }
                FileWriter out = new FileWriter(file.getAbsolutePath());
                BufferedWriter bw = new BufferedWriter(out);
                bw.write(s.toString());

                bw.close();

                sb.clear();
                break;
            }
        } catch (Exception e) {
            System.out.println("Exception happened: ");
            e.printStackTrace();
        }
    }

    private void sortItAll(){
        sb.sort((o1, o2) -> o1[0].compareTo(o2[0]));
    }
}