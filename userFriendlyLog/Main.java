import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        List<File> csvFiles = covertEachParticipantIntoSessions("data");

        Participants t = new Participants();
        for (File file : csvFiles) {  
            t.createParticipants(loadTheFile(file));
        }
        
        csvFiles.clear();

        csvFiles = covertEachParticipantIntoSessions("output");
        for(File file : csvFiles){
            length(loadTheFile(file));
        }

        Sessions s = new Sessions();
        csvFiles = covertEachParticipantIntoSessions("output");
        for (File file : csvFiles) {  
            s.createSessions(loadTheFile(file));
        }

        csvFiles.clear();

        UserBehaviour b = new UserBehaviour();
        b.addTitles();
        csvFiles = covertEachParticipantIntoSessions("userFriendlyLogData");
        for (File file : csvFiles) {  
            b.calculate(loadTheFile(file));
        }
        b.printOneFile("output2", "statistics");
        
    }

    private static void length(List<String[]> data){
        for(String[] d: data){
            if(d.length < 5){
                System.out.println(String.join(",", d));
            }
        }
    }

    private static List<File> covertEachParticipantIntoSessions(String placement){
        final File folder = new File("./" + placement);
        final List<File> fileList = Arrays.asList(folder.listFiles());

        List<File> csvFiles = new ArrayList<>();
        for (int i = 0; i < fileList.size(); i++) {
            File file = fileList.get(i);
            if(file.isFile() && file.getName().endsWith(".csv")){
                csvFiles.add(file);
            }
        }
        return csvFiles;
    }

    private static List<String[]> loadTheFile(File file){
        List<String[]> data = new ArrayList<>();
        try(Scanner sc = new Scanner(file)) {    
            while(sc.hasNext()){
                data.add(sc.nextLine().split(","));                    
            }

        } catch (IOException e) {
            System.err.println("Execption occured:");
            e.printStackTrace();
        }
        return data;
    }
}