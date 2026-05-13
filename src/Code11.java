
// Consultar taula https://en.wikipedia.org/wiki/Barcode#Linear_barcodes
// Code11: https://en.wikipedia.org/wiki/Code_11

// Generadors de codis:
//     https://barcode.tec-it.com/en/Code11
//     https://www.free-barcode-generator.net/code-11/
//     https://products.aspose.app/barcode/generate

import java.util.*;

import static java.util.Map.entry;

public class Code11 {

    private static Map<Character, String> diccionary = Map.ofEntries(
            entry('0', "00001"),
            entry('1', "10001"),
            entry('2', "01001"),
            entry('3', "11000"),
            entry('4', "00101"),
            entry('5', "10100"),
            entry('6', "01100"),
            entry('7', "00011"),
            entry('8', "10010"),
            entry('9', "10000"),
            entry('-', "00100"),
            entry('*', "00110")
    );

    private static String wideBar = "██";
    private static String narrowBar = "█";
    private static String narrowSpace = " ";
    private static String wideSpace = "  ";

    // Codifica un String amb Code11
    static String encode(String s) {
        StringBuilder res = new StringBuilder();

        for (int i = 0; i < s.length(); i++) {
            char key = s.charAt(i);
            res.append(createpattern(key));
            if (!(i == s.length() - 1)){
                res.append(narrowSpace);
            }
        }

        return res.toString();
    }

    private static String createpattern(char key) {
        StringBuilder res = new StringBuilder();

        String mappattern = diccionary.get(key);
        boolean isBar = true;

        for (int i = 0; i < mappattern.length(); i++) {
            char bit = mappattern.charAt(i);

            if (isBar){
                res.append(bit == '0' ? narrowBar : wideBar);
            } else {
                res.append(bit == '0' ? narrowSpace : wideSpace);
            }

            isBar = !isBar;
        }

        return res.toString();
    }

    // Decodifica amb Code11
    static String decode(String s) {
        s = s.trim();
        if (!isValidWord(s)){
            return null;
        }

        ArrayList<Integer> numeros = countValues(s);

        System.out.println(numeros);

        for (int i = 1; i < numeros.size(); i++) {
            if (i % 6 == 0){
                numeros.remove(i);
            }
        }

        int averageNarrow = (numeros.get(0) + numeros.get(1) + numeros.get(4)) / 3;
        int averageWide = (numeros.get(2) + numeros.get(3)) / 2;
        
        
        Map<Integer, List<Integer>> GroupBits = new HashMap<>();

        List<Integer> groupOfFive = new ArrayList<>();
        
        int count = 0;
        for (int i = 0; i < numeros.size(); i++) {
            groupOfFive.add(numeros.get(i));
            count++;
            if (count == 4){
                GroupBits.put()
                count = 0;
            }
        }
        
        System.out.println(numeros);
        System.out.println(averageNarrow + " " + averageWide);
        return "";
    }

    private static ArrayList<Integer> countValues(String s) {

        ArrayList<Integer> numeros = new ArrayList<>();

        int count = 1;
        char actual = s.charAt(0);

        for (int i = 1; i < s.length(); i++) {
            char index = s.charAt(i);

            if (actual == index){
                count++;
            } else {
                numeros.add(count);
                count = 1;
                actual = index;
            }

        }

        numeros.add(count);

        return numeros;
    }

    private static boolean isValidWord(String s) {
        if (!s.matches("[█ ]+")){
            return false;
        }
        return true;
    }


    // Decodifica una imatge. La imatge ha d'estar en format "ppm"
    public static String decodeImage(String str) {
        return "";
    }

    // Genera imatge a partir de codi de barres
    // Alçada: 100px
    // Marges: vertical 4px, horizontal 8px
    public static String generateImage(String s) {
        return "";
    }
}
