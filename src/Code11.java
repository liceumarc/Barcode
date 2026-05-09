
// Consultar taula https://en.wikipedia.org/wiki/Barcode#Linear_barcodes
// Code11: https://en.wikipedia.org/wiki/Code_11

// Generadors de codis:
//     https://barcode.tec-it.com/en/Code11
//     https://www.free-barcode-generator.net/code-11/
//     https://products.aspose.app/barcode/generate

import java.util.HashMap;
import java.util.Map;
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
            char c = s.charAt(i);
            res.append(createpattern(c));
            if (!(i == s.length() - 1)){
                res.append(" ");
            }
        }

        return res.toString();
    }

    private static String createpattern(char c) {
        StringBuilder res = new StringBuilder();

        String mappattern = diccionary.get(c);
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
        return "";
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
