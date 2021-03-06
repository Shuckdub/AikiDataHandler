import static java.lang.System.out;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class Printer {
    private StringBuilder sb;

    public Printer() {
        sb = new StringBuilder();
    }

    public void titles() {
        sb.append("Participant,Timestamp,event,url,value" + "\n");
    }

    public void addToPrinter(String nextLines) {
        sb.append(nextLines + "\n");
    }

    public void test() {
        out.print(sb.toString());
        sb.setLength(0);
    }

    public void addLinesToPrinter(String[] lineArray, String event) {
        switch (event) {
            case "end":
                lineArray[2] = "exercise-session";
                lineArray[3] = "";
                lineArray[4] = "ended";
                addToPrinter(String.join(",", lineArray));
                break;

            case "interruption":
                lineArray[2] = "exercise-was";
                lineArray[3] = "";
                lineArray[4] = "interrupted";
                addToPrinter(String.join(",", lineArray));
                break;

            case "timeout":
                lineArray[2] = "earned-timout-until";
                addToPrinter(String.join(",", lineArray));
                break;

            case "timeout-on":
                lineArray[2] = "user-added-a-timeout";
                addToPrinter(String.join(",", lineArray));
                break;

            case "timeout-off":
                lineArray[2] = "user-removed-timeout";
                addToPrinter(String.join(",", lineArray));
                break;

            case "exercisestatus":
                lineArray[2] = "exercise-was";
                addToPrinter(String.join(",", lineArray));
                break;

            case "zeeguu":
                lineArray[2] = "length-of-session";
                addToPrinter(String.join(",", lineArray));
                break;

            case "user-went-to":
                lineArray[2] = "user-went-to";
                lineArray[4] = " ";
                addToPrinter(String.join(",", lineArray));
                break;

            case "user-went-to-first-time":
                lineArray[2] = "user-went-to";
                lineArray[4] = "firstTime";
                addToPrinter(String.join(",", lineArray));
                break;

            case "enabled":
                lineArray[2] = "extension-was";
                addToPrinter(String.join(",", lineArray));
                break;

            case "exerciseduration":
                lineArray[2] = "exercise-duration-changed-to";
                addToPrinter(String.join(",", lineArray));
                break;

            case "intercepted":
                lineArray[2] = "user-closed-interception-instantly";
                addToPrinter(String.join(",", lineArray));
                break;

            case "blockedurls":
                lineArray[2] = "this-is-a-time-wasting-site";
                addToPrinter(String.join(",", lineArray)+", ");
                break;

            case "timewastedduration":
                lineArray[2] = "wasting-time-duration-changed-to";
                addToPrinter(String.join(",", lineArray));
                break;

            default:
                addToPrinter("Something went wrong");
                break;
        }
    }

    public void printItAll(String placement, String fileName) {
        File file = createFile(placement, fileName);
        try (FileWriter output = new FileWriter(file.getAbsolutePath());
            BufferedWriter bw = new BufferedWriter(output)) {
            bw.write(sb.toString());

            sb.setLength(0);
        } catch (Exception e) {
            out.println("Execption occured:");
            e.printStackTrace();
        }
    }

    private File createFile(String placement, String fileName) {
        try {
            File file = new File("./" + placement + "/" + fileName + ".csv");
            int i = 1;
            while (true) {
                if (!file.exists()) {
                    break;
                }
                file = new File("./" + placement + "/" + fileName + "-x-" + i + ".csv");
                i++;
            }
            return file;
        } catch (Exception e) {
            out.println("Execption occured:");
            e.printStackTrace();
        }
        return new File("");
    }

    public void printOneBigFile(String placement, String fileName) {
        File file = createFile(placement, fileName);
        try(FileWriter out = new FileWriter(file.getAbsolutePath());
            BufferedWriter bw = new BufferedWriter(out)) {
            bw.write(sb.toString());

            sb.setLength(0);

        } catch (Exception e) {
            out.println("Exception happened: ");
            e.printStackTrace();
        }
    }

}