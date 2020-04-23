import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

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
                participantData.add(sc.nextLine().split(","));
                                          
            }
            sc.close();  
            ArrayList<String[]> tempList = new ArrayList<>();
            for(int i = 0 ; i < participantData.size()-1; i++){
                if(participantData.get(i)[0].equals(participantData.get(i+1)[0])){
                    tempList.add(participantData.get(i));
                } else {
                    tempList.add(participantData.get(i));
                    endOfThisParticipant(tempList, placement);
                    tempList.clear();
                }
            }
            endOfThisParticipant(tempList, placement);
        } catch (IOException e) {
            System.out.println("Execption occured:");
            e.printStackTrace();
        }
    }

    private void endOfThisParticipant(ArrayList<String[]> participantData, String placement ){
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
}

// while(sc.hasNext()){
//     String lines = sc.nextLine();
//     String[] linesArray = lines.split(",");
//     if(sc.hasNext()){
//         String nextLine = sc.nextLine();
//         String[] nextLineArray = nextLine.split(",");
//         if(!(nextLineArray[0].equals(linesArray[0]))){
//             endOfThisParticipant(participantData, linesArray, placement);
//             participantData.clear();
//             if(nextLineArray[2].equals("end")){
//                 endOfThisParticipant(participantData, nextLineArray, placement);
//                 participantData.clear();
//             }
//             break;
//         } 
//         participantData.add(nextLineArray);
//     } else{
//         endOfThisParticipant(participantData, linesArray, placement);
//         participantData.clear();
//     }                        
//     participantData.add(linesArray);
// }