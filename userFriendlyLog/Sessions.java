
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class Sessions {

    Map<String,Integer> lastInterception = new TreeMap<>();
    Map<String,Integer> currentInterception = new TreeMap<>();
    Scanner sc;

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
            sc = new Scanner(file);
            String nextLine = "";
            while(sc.hasNext()){
                String line = sc.nextLine();
                String[] lineArray = line.split(",");
                if(lineArray[2].equals("intercepts")){
                    System.out.println();
                    System.out.println("Begning of Session");
                    System.out.println(lineArray[0]);
                    System.out.println(lineArray[1]);
                    //Where is the user headed?
                    nextLine = workTheData(nextLine, lineArray);

                    if(currentInterception.isEmpty()){
                        System.out.println("The user is going to " + lineArray[3] + " first visit.");
                        continue;
                    } else {
                        whereIsTheUserHeaded(nextLine,lineArray);
                    }

                    resetForTheNextSession(nextLine);
                    //Found the answer

                    System.out.println("The end of the session");
                }
            }
            
            sc.close();
            
        } catch (IOException e) {
            System.out.println("Execption occured:");
            e.printStackTrace();
        }

    }

    private void whereIsTheUserHeaded(String nextLine, String[] lineArray ){        
        if(lastInterception.keySet().equals(currentInterception.keySet())){
            for (Map.Entry<String,Integer> lastInter : lastInterception.entrySet()) {
                if(currentInterception.containsKey(lastInter.getKey())){
                    int last = lastInter.getValue();
                    int current = currentInterception.get(lastInter.getKey());
                    if(last < current){
                        System.out.println("The user is going to " + lastInter.getKey());
                    }
                }
            }    
        } else{
            for (Map.Entry<String,Integer> lastInter : currentInterception.entrySet()) {
                if(!(lastInterception.containsKey(lastInter.getKey()))){
                    System.out.println("The user is going to " + lastInter.getKey() + " first visit.");
                }
            }
        }
    }

    private String workTheData(String nextLine, String[] lineArray){
        if(lastInterception.isEmpty()){
            lastInterception.put(lineArray[3],Integer.parseInt(lineArray[4]));
        } else{
            currentInterception.put(lineArray[3],Integer.parseInt(lineArray[4]));
            while(sc.hasNext()){
                nextLine = sc.nextLine();
                String[] nextLineArray = nextLine.split(",");
                if(nextLineArray[1].equals(lineArray[1])){
                    currentInterception.put(nextLineArray[3],Integer.parseInt(nextLineArray[4]));
                    System.out.println(nextLineArray[1]);
                } else{
                    return nextLine;
                }
            }
        }
        return nextLine;
    }

    private void resetForTheNextSession(String nextLine){
        lastInterception.clear();
        lastInterception.putAll(currentInterception);
        currentInterception.clear();
        
        if(nextLine.split(",")[2].equals("intercepts")){
            currentInterception.put(nextLine.split(",")[3],Integer.parseInt(nextLine.split(",")[4]));
        }
    }
}