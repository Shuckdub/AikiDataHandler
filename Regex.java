
public class Regex {
    public static String regexIt(String theRegex){
        theRegex = theRegex.toLowerCase();
        switch (theRegex) {
            case "time":
                return "[^0-9]+";
        
            case "url":
                return "[^Sa-z.]+";

            case "event":
                return "[^Sa-z]+";

            case "participant":
                return "[^a-z0-9]+";
        }
        return "Error";
    }
}