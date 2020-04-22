import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Testing {

    public void convert(File file){
        try {
            Scanner sc = new Scanner(file);
            Printer p = new Printer();
            outer: while(sc.hasNext()){
                String[] line = sc.nextLine().split(",");
                String participant = line[0];
                if(line[2].equals("intercepts")){
                    p.printer(participant, line[1], line[2], line[3], line[4]);
                    inner : while(sc.hasNext()){
                        String[] eventlines = sc.nextLine().split(",");
                        if (!(eventlines[0].equals(participant))){
                            break inner;
                        } else if(eventlines[2].equals("intercepts")){
                            p.printer(participant, eventlines[1], eventlines[2], eventlines[3], eventlines[4]);
                        }
                    }
                }
            }
            
            sc.close();
            
        } catch (IOException e) {
            System.out.println("Execption occured:");
            e.printStackTrace();
        }

    }

    public void split(File file){
        try {
            Scanner sc = new Scanner(file);
            String title = sc.nextLine();
            String line = sc.nextLine();
            String participant = line.split(",")[0];
            Printer p = new Printer();
            outer: while(sc.hasNext()){
                while(sc.hasNext()){
                    String nextLines = sc.nextLine();
                    String sameParticipant = nextLines.split(",")[0];
                    if(!(participant.equals(sameParticipant))) {
                        line = nextLines;
                        participant = sameParticipant;
                        break;
                    }
                    p.splitPrints(nextLines);;
                }
                p.splitItAll(participant);
                
            }
        } catch (IOException e) {
            System.out.println("Execption occured:");
            e.printStackTrace();
        }
    }
}