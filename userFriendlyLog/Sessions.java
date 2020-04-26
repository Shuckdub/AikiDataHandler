
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class Sessions {

    Map<String,Integer> lastInterception = new TreeMap<>();
    Map<String,Integer> currentInterception = new TreeMap<>();
    ArrayList<String[]> data;
    int lastSession = 0;

    Printer p = new Printer();

    public void covertEachParticipantIntoSessions(){
        final File folder = new File("./output");
        final List<File> fileList = Arrays.asList(folder.listFiles());
        p.titles();  

        for (int i = 0; i < fileList.size(); i++) {
            File file = fileList.get(i);
            if(file.isFile() && file.getName().endsWith(".csv")){
                createSessions(file);
            }
        }
        p.printOneBigFile();
    }

    public void createSessions(File file){
        String nextLine = "";

        loadTheFile(file);      

        int i = 0;
        boolean first = true;
        
        while(i < data.size()){
            String[] lineArray = data.get(i);
            
            if(lineArray[2].equals("intercepts")){

                p.addToPrinter(lineArray[0] + "," + lineArray[1]+ ",exercise-session" + "," + ",begun");

                i = getInterecptedSites(i, lineArray);
                
                if(currentInterception.isEmpty()){
                    p.addLinesToPrinter(lineArray, "user-went-to-first-time");
                    i++;
                } else {
                    whereIsTheUserHeaded(lineArray);
                }
                
                i = whatHappenedinTheInterception(i);
                
                resetForTheNextSession(data.get(i-1));
            
            } else if(lineArray[2].equals("blockedurls")) {
                p.addLinesToPrinter(lineArray, lineArray[2]);
                i++;

            } else if(lineArray[2].equals("enabled")) {
                if(lineArray[4].equals("true")){
                    lineArray[4] = "enabled";
                    p.addLinesToPrinter(lineArray, lineArray[2]);
                } else{
                    lineArray[4] = "disabled";
                    p.addLinesToPrinter(lineArray, lineArray[2]);
                }
                i++;

            } else{
                p.addLinesToPrinter(lineArray, lineArray[2]);
                i++;
            }
        }
        // p.test();
        resetForNextParticipant();
    }

    private void loadTheFile(File file){
        try {    
            Scanner sc = new Scanner(file);
            data = new ArrayList<>();
            while(sc.hasNext()){
                data.add(sc.nextLine().split(","));                    
            }
            sc.close();

        } catch (IOException e) {
            System.out.println("Execption occured:");
            e.printStackTrace();
        }
    }

    private void whereIsTheUserHeaded(String[] lastEntry){        
        if(lastInterception.keySet().equals(currentInterception.keySet())){
            for (Map.Entry<String,Integer> lastInter : lastInterception.entrySet()) {
                if(currentInterception.containsKey(lastInter.getKey())){
                    
                    int last = lastInter.getValue();
                    int current = currentInterception.get(lastInter.getKey());
                    if(last < current){
                        lastEntry[3] = lastInter.getKey();
                        p.addLinesToPrinter(lastEntry, "user-went-to");
                    }
                }
            }    
        } else{
            for (Map.Entry<String,Integer> lastInter : currentInterception.entrySet()) {
                if(!(lastInterception.containsKey(lastInter.getKey()))){
                    
                    lastEntry[3] = lastInter.getKey();
                    p.addLinesToPrinter(lastEntry, "user-went-to-first-time");
                }
            }
        }
    }

    private int getInterecptedSites(int i, String[] lineArray){
        if(lastInterception.isEmpty()){
            if(lineArray[4].equals("closed")) {
                p.addToPrinter(lineArray[1] + ": It happened here!!");
                return i++;
            }
            lastInterception.put(lineArray[3],Integer.parseInt(lineArray[4]));
        } else{
            currentInterception.put(lineArray[3],Integer.parseInt(lineArray[4]));
            i++;
            while(i < data.size()){
                String[] nextLineArray = data.get(i);
                if(nextLineArray[4].equals("closed")) {
                    return i;
                } else if(nextLineArray[1].equals(lineArray[1])){
                    currentInterception.put(nextLineArray[3],Integer.parseInt(nextLineArray[4]));
                } else{
                    return i;
                }
                i++;
            }
        }
        return i++;
    }

    private int whatHappenedinTheInterception(int i){
        boolean exercisestatusFound = false;

        while(i < data.size()){
            
            String[] nextLineArray = data.get(i);
            
            if(nextLineArray[2].equals("zeeguu")){
                if(lastSession==0){
                    lastSession = Integer.parseInt(nextLineArray[4]);
                    p.addLinesToPrinter(nextLineArray, "zeeguu");
                } else{
                    int currentSession = Integer.parseInt(nextLineArray[4]);
                    currentSession = currentSession-lastSession;
                    lastSession = Integer.parseInt(nextLineArray[4]);
                    nextLineArray[4] = String.valueOf(currentSession);
                    p.addLinesToPrinter(nextLineArray, "zeeguu");
                }

            } else if(nextLineArray[2].matches("timeout-on")){
                p.addLinesToPrinter(nextLineArray, "timeout");

            } else if(nextLineArray[2].equals("exercisestatus")){
                p.addLinesToPrinter(nextLineArray, "exercisestatus");
                exercisestatusFound = true;

            } else {
                if(exercisestatusFound){
                    return i;
                } else {
                    String[] interruption = data.get(i-1);
                    p.addLinesToPrinter(interruption, "interruption");
                    return i;
                }
            }
            i++;
        }
        if(i == data.size()){
            if(exercisestatusFound){
                return i;
            } else {
                String[] interruption = data.get(i-1);
                p.addLinesToPrinter(interruption, "interruption");
                return i;
            }
        }
        return i;
    }

    private void resetForTheNextSession(String[] lineArray){
        lastInterception.putAll(currentInterception);
        currentInterception.clear();
        p.addLinesToPrinter(lineArray, "end");
    }

    private void resetForNextParticipant(){
        lastInterception.clear();
        currentInterception.clear();
        data.clear();
        lastSession = 0;
    }
}