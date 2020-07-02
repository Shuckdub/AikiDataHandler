import java.util.ArrayList;
import java.util.List;

public class Participants {

    Printer p = new Printer();

    public void createParticipants(List<String[]> participantData){
        String placement = "output";
        ArrayList<String[]> tempList = new ArrayList<>();
        for(int i = 1 ; i < participantData.size()-1; i++){
            if(participantData.get(i)[0].equals(participantData.get(i+1)[0])){
                tempList.add(participantData.get(i));
            } else {
                tempList.add(participantData.get(i));
                endOfThisParticipant(tempList, placement);
                tempList.clear();
            }
        }
        tempList.add(participantData.get(participantData.size()-1));
        endOfThisParticipant(tempList, placement);

    }

    private void endOfThisParticipant(ArrayList<String[]> participantData, String placement ){
        participantData.sort((o1, o2) -> o1[1].compareTo(o2[1]));
        prepareToPrint(participantData, placement);
    }

    private void prepareToPrint(ArrayList<String[]> participantData, String placement){
        p.titles();
        for (String[] data : participantData) {
            if(data[2].equals("blockedurls")){
                p.addToPrinter(String.join(",", data) + ", ");
            } else {
                p.addToPrinter(String.join(",", data));
            }
        }
        p.printItAll(placement,participantData.get(0)[0]);
    }
}