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

    public void sessionStart(){
        sb.append("Beginning of the session" + "\n");
    }

    public void addToPrinter(String nextLines){
        sb.append(nextLines + "\n");
    }

    public void test(){
        System.out.println(sb.toString());
        sb.setLength(0);
    }

    public void printItAll(String placement, String fileName){
        try {
            File file = new File("./"+ placement +"/"+ fileName +".csv");
            int i = 1;
            while(true){
                if(file.exists()){
                    file = new File("./"+ placement +"/"+ fileName + "-x-" + i + ".csv");
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
            sb.setLength(0);
        } catch (Exception e) {
            System.out.println("Execption occured:");
            e.printStackTrace();
        }
    }

}