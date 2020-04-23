import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Testing {

    Printer p = new Printer();

    public void firstGetEachParticipant(){
        final File folder = new File("./data");
        final List<File> fileList = Arrays.asList(folder.listFiles());

        for (int i = 0; i < fileList.size(); i++) {
            File file = fileList.get(i);
            if(file.isFile() && file.getName().endsWith(".csv")){
                splitDataIntoParticipants(file);
            }
        }
    }

    private void splitDataIntoParticipants(File file){
        try {
            String placement = "output";
            Scanner sc = new Scanner(file);
            String title = sc.nextLine();
            ArrayList<String[]> participantData = new ArrayList<>();
            while(sc.hasNext()){
                while(sc.hasNext()){
                    String lines = sc.nextLine();
                    String[] linesArray = lines.split(",");
                    String end = linesArray[2];
                    if(end.equals("end")) {
                        if(sc.hasNext()){
                            String nextLine = sc.nextLine();
                            String[] nextLineArray = nextLine.split(",");
                            if(!(nextLineArray[0].equals(linesArray[0]))){
                                endOfThisParticipant(participantData, linesArray, placement);
                                participantData.clear();
                                if(nextLineArray[2].equals("end")){
                                    endOfThisParticipant(participantData, nextLineArray, placement);
                                    participantData.clear();
                                }
                                break;
                            } 
                            participantData.add(nextLineArray);
                        } else{
                            endOfThisParticipant(participantData, linesArray, placement);
                            participantData.clear();
                        }                        
                    }
                    participantData.add(linesArray);
                }                
            }
        } catch (IOException e) {
            System.out.println("Execption occured:");
            e.printStackTrace();
        }
    }

    private void endOfThisParticipant(ArrayList<String[]> participantData, String[] linesArray, String placement ){
        participantData.add(linesArray);
        participantData.sort((o1, o2) -> o1[1].compareTo(o2[1]));
        prepareToPrint(participantData, placement);
    }

    private void prepareToPrint(ArrayList<String[]> participantData, String placement){
        for (String[] data : participantData) {
            p.addToPrinter(String.join(",", data));
            String print = String.join(",", data);
        }
        p.printItAll(placement,participantData.get(0)[0]);
    }

    public void covertEachParticipantIntoSessions(){
        final File folder = new File("./output");
        final List<File> fileList = Arrays.asList(folder.listFiles());

        for (int i = 0; i < fileList.size(); i++) {
            File file = fileList.get(i);
            if(file.isFile() && file.getName().endsWith(".csv")){
                createSessions(file);
            }
        }
    }

    public void createSessions(File file){
        try {
            Scanner sc = new Scanner(file);
            ArrayList<String[]> lastInterception = new ArrayList<>();
            while(sc.hasNext()){
                String line = sc.nextLine();
                String[] lineArray = line.split(",");
                if(lineArray[2].equals("intercepts")){
                    if(lastInterception.isEmpty()){
                        lastInterception.add(lineArray);
                    }
                    System.out.println();
                    System.out.println("Begning of Session");
                    System.out.println(lineArray[0]);
                    System.out.println(lineArray[1]);
                    while(sc.hasNext()){
                        String nextLine = sc.nextLine();
                        String[] nextLineArray = nextLine.split(",");
                        if(nextLineArray[1].equals(lineArray[1])){
                            System.out.println(nextLineArray[1]);
                        } else{
                            break;
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
}