
// Consultar taula https://en.wikipedia.org/wiki/Barcode#Linear_barcodes
// Code11: https://en.wikipedia.org/wiki/Code_11

// Generadors de codis:
//     https://barcode.tec-it.com/en/Code11
//     https://www.free-barcode-generator.net/code-11/
//     https://products.aspose.app/barcode/generate

import java.util.Map;
import static java.util.Map.entry;

public class Code11 {

    private static Map<Character, String> diccionaryKeyToBits = Map.ofEntries(
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

    private static Map<String, Character> diccionaryBitsToKey = Map.ofEntries(
            entry("00001", '0'),
            entry("10001", '1'),
            entry("01001", '2'),
            entry("11000", '3'),
            entry("00101", '4'),
            entry("10100", '5'),
            entry("01100", '6'),
            entry("00011", '7'),
            entry("10010", '8'),
            entry("10000", '9'),
            entry("00100", '-'),
            entry("00110", '*')
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

        String mappattern = diccionaryKeyToBits.get(key);
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
        int unitWidth = calcularLongitud(s);
        return "";
    }

    private static int calcularLongitud(String s) {
        int res = 0;
        int index = 0;
        char[] code = s.toCharArray();

        while (code[index] == '█'){
            res++;
            index++;
        }

        return res;
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
