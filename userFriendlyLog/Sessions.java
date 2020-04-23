
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
            String nextLine = "";

            data = new ArrayList<>();
            while(sc.hasNext()){
                data.add(sc.nextLine().split(","));                    
            }
            sc.close();
            
            int i = 0;
            boolean first = true;
            while(i < data.size()){
                String[] lineArray = data.get(i);
                
                if(lineArray[2].equals("intercepts")){

                    p.sessionStart();

                    i = whatAreTheIntercepts(i, lineArray);
                    
                    if(currentInterception.isEmpty()){
                        p.addToPrinter("The user went to " + lineArray[3] + " for the first time.");
                        i++;
                    } else {
                        whereIsTheUserHeaded();
                    }
                    
                    whatHappenedInTheSession(i);
                    
                    resetForTheNextSession();

                    p.test();
                
                } else{
                    i++;
                }
            }
            
        } catch (IOException e) {
            System.out.println("Execption occured:");
            e.printStackTrace();
        }

    }

    private void whereIsTheUserHeaded(){        
        if(lastInterception.keySet().equals(currentInterception.keySet())){
            for (Map.Entry<String,Integer> lastInter : lastInterception.entrySet()) {
                if(currentInterception.containsKey(lastInter.getKey())){
                    int last = lastInter.getValue();
                    int current = currentInterception.get(lastInter.getKey());
                    if(last < current){
                        p.addToPrinter("The user went to  " + lastInter.getKey());
                    }
                }
            }    
        } else{
            for (Map.Entry<String,Integer> lastInter : currentInterception.entrySet()) {
                if(!(lastInterception.containsKey(lastInter.getKey()))){
                    p.addToPrinter("The user went to " + lastInter.getKey() + " first visit.");
                }
            }
        }
    }

    private int whatAreTheIntercepts(int i, String[] lineArray){
        if(lastInterception.isEmpty()){
            lastInterception.put(lineArray[3],Integer.parseInt(lineArray[4]));
        } else{
            currentInterception.put(lineArray[3],Integer.parseInt(lineArray[4]));
            while(i < data.size()){
                i++;
                String[] nextLineArray = data.get(i);
                if(nextLineArray[1].equals(lineArray[1])){
                    currentInterception.put(nextLineArray[3],Integer.parseInt(nextLineArray[4]));
                    p.addToPrinter(nextLineArray[1]);
                } else{
                    return i;
                }
            }
        }
        return i++;
    }

    private void whatHappenedInTheSession(int i){
        boolean exercisestatusFound = false;

        while(i < data.size()){
            
            String[] nextLineArray = data.get(i);
            
            if(nextLineArray[2].equals("zeeguu")){
                if(lastSession==0){
                    lastSession = Integer.parseInt(nextLineArray[4]);
                    p.addToPrinter("Length of session " + lastSession);
                } else{
                    int currentSession = Integer.parseInt(nextLineArray[4]);
                    currentSession = currentSession-lastSession;
                    p.addToPrinter("Length of session " + currentSession);
                    lastSession = Integer.parseInt(nextLineArray[4]);
                    
                }

            } else if(nextLineArray[2].matches("timeout.*")){
                p.addToPrinter("timeout " + nextLineArray[4]);

            } else if(nextLineArray[2].equals("exercisestatus")){
                p.addToPrinter(nextLineArray[4]);
                exercisestatusFound = true;

            } else {
                if(exercisestatusFound){
                    break;
                } else {
                    String[] interruption = data.get(i-1);
                    interruption[2] = "Exercise-Interrupted"; 
                    p.addToPrinter(interruption[2]);
                    break;
                }
            }
            i++;
        }
    }

    private void resetForTheNextSession(){
        lastInterception.putAll(currentInterception);
        currentInterception.clear();
        p.addToPrinter("The end of the session");
    }
}