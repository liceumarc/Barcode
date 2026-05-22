// https://en.wikipedia.org/wiki/Code_93

import java.util.*;
import static java.util.Map.entry;

public class Code93 {

    public static final String[] widths = {
            "131112", // 0 - 0
            "111213", // 1 - 1
            "111312", // 2 - 2
            "111411", // 3 - 3
            "121113", // 4 - 4
            "121212", // 5 - 5
            "121311", // 6 - 6
            "111114", // 7 - 7
            "131211", // 8 - 8
            "141111", // 9 - 9
            "211113", // 10 - A
            "211212", // 11 - B
            "211311", // 12 - C
            "221112", // 13 - D
            "221211", // 14 - E
            "231111", // 15 - F
            "112113", // 16 - G
            "112212", // 17 - H
            "112311", // 18 - I
            "122112", // 19 - J
            "132111", // 20 - K
            "111123", // 21 - L
            "111222", // 22 - M
            "111321", // 23 - N
            "121122", // 24 - O
            "131121", // 25 - P
            "212112", // 26 - Q
            "212211", // 27 - R
            "211122", // 28 - S
            "211221", // 29 - T
            "221121", // 30 - U
            "222111", // 31 - V
            "112122", // 32 - W
            "112221", // 33 - X
            "122121", // 34 - Y
            "123111", // 35 - Z
            "121131", // 36 - -
            "311112", // 37 - .
            "311211", // 38 - SPACE
            "321111", // 39 - $
            "112131", // 40 - /
            "113121", // 41 - +
            "211131", // 42 - %
            "121221", // 43 - ($)
            "312111", // 44 - (%)
            "311121", // 45 - (/)
            "122211", // 46 - (+)
            "111141", // Start/Stop
            //"114111", // Reverse stop
            //"411111", // Unused
            //"111132", // Unused
            //"111231", // Unused
            //"113112", // Unused
            //"113211", // Unused
            //"213111", // Unused
            //"212121" // Unused
    };

    public static final Map<Integer, String> diccionaryValueToBits = Map.ofEntries(
            Map.entry(0, "131112"),
            Map.entry(1, "111213"),
            Map.entry(2, "111312"),
            Map.entry(3, "111411"),
            Map.entry(4, "121113"),
            Map.entry(5, "121212"),
            Map.entry(6, "121311"),
            Map.entry(7, "111114"),
            Map.entry(8, "131211"),
            Map.entry(9, "141111"),
            Map.entry(10, "211113"),
            Map.entry(11, "211212"),
            Map.entry(12, "211311"),
            Map.entry(13, "221112"),
            Map.entry(14, "221211"),
            Map.entry(15, "231111"),
            Map.entry(16, "112113"),
            Map.entry(17, "112212"),
            Map.entry(18, "112311"),
            Map.entry(19, "122112"),
            Map.entry(20, "132111"),
            Map.entry(21, "111123"),
            Map.entry(22, "111222"),
            Map.entry(23, "111321"),
            Map.entry(24, "121122"),
            Map.entry(25, "131121"),
            Map.entry(26, "212112"),
            Map.entry(27, "212211"),
            Map.entry(28, "211122"),
            Map.entry(29, "211221"),
            Map.entry(30, "221121"),
            Map.entry(31, "222111"),
            Map.entry(32, "112122"),
            Map.entry(33, "112221"),
            Map.entry(34, "122121"),
            Map.entry(35, "123111"),
            Map.entry(36, "121131"),
            Map.entry(37, "311112"),
            Map.entry(38, "311211"),
            Map.entry(39, "321111"),
            Map.entry(40, "112131"),
            Map.entry(41, "113121"),
            Map.entry(42, "211131"),
            Map.entry(43, "121221"),
            Map.entry(44, "312111"),
            Map.entry(45, "311121"),
            Map.entry(46, "122211"),
            Map.entry(47, "111141")
    );

    private static final Map<Character, String> diccionaryKeyToBits = Map.ofEntries(
            entry('0', "131112"),
            entry('1', "111213"),
            entry('2', "111312"),
            entry('3', "111411"),
            entry('4', "121113"),
            entry('5', "121212"),
            entry('6', "121311"),
            entry('7', "111114"),
            entry('8', "131211"),
            entry('9', "141111"),

            entry('A', "211113"),
            entry('B', "211212"),
            entry('C', "211311"),
            entry('D', "221112"),
            entry('E', "221211"),
            entry('F', "231111"),
            entry('G', "112113"),
            entry('H', "112212"),
            entry('I', "112311"),
            entry('J', "122112"),
            entry('K', "132111"),
            entry('L', "111123"),
            entry('M', "111222"),
            entry('N', "111321"),
            entry('O', "121122"),
            entry('P', "131121"),
            entry('Q', "212112"),
            entry('R', "212211"),
            entry('S', "211122"),
            entry('T', "211221"),
            entry('U', "221121"),
            entry('V', "222111"),
            entry('W', "112122"),
            entry('X', "112221"),
            entry('Y', "122121"),
            entry('Z', "123111"),

            entry('-', "121131"),
            entry('.', "311112"),
            entry(' ', "311211"),
            entry('$', "321111"),
            entry('/', "112131"),
            entry('+', "113121"),
            entry('%', "211131"),

            // Caracteres especiales extendidos
            entry('(', "121221"), // ($)
            entry(')', "312111"), // (%)
            entry('[', "311121"), // (/)
            entry(']', "122211"), // (+)

            // Start/Stop
            entry('*', "111141")
    );

    public static final char[] characters = {
            '0','1','2','3','4','5','6','7','8','9',
            'A','B','C','D','E','F','G','H','I','J',
            'K','L','M','N','O','P','Q','R','S','T',
            'U','V','W','X','Y','Z','-','.',' ','$',
            '/','+','%','(',')','/', '+'
    };

    private static final String SPACE = " ";
    private static final String BAR = "█";


    // Codifica emprant Code93
    static String encode(String str) {
        String cadenaBits = "";

        for (int i = 0; i < str.length(); i++) {
            char letraActual = str.charAt(i);
            String cadenaActual = diccionaryKeyToBits.get(letraActual);

            if (!(cadenaActual == null)){
                cadenaBits = cadenaBits + cadenaActual;
            } else {
                return null;
            };
        }

        String res = createPattern(cadenaBits, str);

        return res;
    }

    private static String createPattern(String cadenaBits, String str) {

        String res = "";
        int checksumC = calculateChecksumC(str);
        int checksumK = calculateChecksumK(str, characters[checksumC]);


        String startStop = diccionaryKeyToBits.get('*');
        String sumC = diccionaryValueToBits.get(checksumC);
        String sumK = diccionaryValueToBits.get(checksumK);

        String pattern = startStop + cadenaBits + sumC + sumK + startStop + 1;
        boolean isBar = true;

        System.out.println("inicio: " + startStop + " pattern: " + cadenaBits + " checksumC: " + sumC + " chekcsumK: " + sumK + " final: " + startStop);

        for (int i = 0; i < pattern.length(); i++) {
            char num = pattern.charAt(i);
            int numValue = Character.getNumericValue(num);

            if (isBar){
                res = res + BAR.repeat(numValue);
            } else {
                res = res + SPACE.repeat(numValue);
            }

            isBar = !isBar;
        }
        return res;
    }

    private static int calculateChecksumK(String str, char character) {
        String joinStrChecksumC = str + character;

        ArrayList<Integer> valoresReales = new ArrayList<>();
        char[] charArray = joinStrChecksumC.toCharArray();

        for (int i = 0; i < charArray.length; i++) {
            char letra = charArray[i];
            for (int j = 0; j < characters.length; j++) {
                if (characters[j] == letra){
                    valoresReales.add(j);
                }
            }
        }

        int res = 0;
        int peso = 1;

        for (int i = valoresReales.size() - 1; i >= 0 ; i--) {
            res += valoresReales.get(i) * peso;
            peso++;
            if (peso > 15 ) peso = 1;
        }

        res = res % 47;

        return res;
    }

    private static int calculateChecksumC(String str) {

        ArrayList<Integer> valoresReales = new ArrayList<>();
        char[] charArray = str.toCharArray();

        for (int i = 0; i < charArray.length; i++) {
            char letra = charArray[i];
            for (int j = 0; j < characters.length; j++) {
                if (characters[j] == letra){
                    valoresReales.add(j);
                }
            }
        }

        int res = 0;
        int peso = 1;

        for (int i = valoresReales.size() - 1; i >= 0 ; i--) {
            res += valoresReales.get(i) * peso;
            peso++;
            if (peso > 20 ) peso = 1;
        }

        res = res % 47;

        return res;
    }

    // Decodifica emprant Code93
    static String decode(String str) {
        return "";
    }

    // Decodifica una imatge. La imatge ha d'estar en format "ppm"
    public static String decodeImage(String str) {
        return "";
    }

    // Genera imatge a partir de barcode code93
    // Unitat barra mínima: 3 pixels
    // Alçada: 100px
    // Marges: vertical: 5px, horizontal: 15px
    public static String generateImage(String s) {
        return "";
    }
}
