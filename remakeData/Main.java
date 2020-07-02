import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args){
        final File folder = new File("./data");
        final List<File> fileList = Arrays.asList(folder.listFiles());
        boolean lastFile = false;

        for (int i = 0; i < fileList.size(); i++) {
            File file = fileList.get(i);
            DataHandler d = new DataHandler();
            if(i==fileList.size()-1){
              lastFile = true;  
            }
            if(file.isFile() && file.getName().endsWith(".json")){
                d.startTheDataHandler(file, lastFile);
            }
        }
    }
}