
// Consultar taula https://en.wikipedia.org/wiki/Barcode#Linear_barcodes
// Code11: https://en.wikipedia.org/wiki/Code_11

// Generadors de codis:
//     https://barcode.tec-it.com/en/Code11
//     https://www.free-barcode-generator.net/code-11/
//     https://products.aspose.app/barcode/generate

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Map.entry;

public class Code11 {

    private static final Map<Character, String> diccionaryKeyToBit = Map.ofEntries(
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

    private static Map<String, String> diccionaryBitToKey = Map.ofEntries(
            entry("00001", "0"),
            entry("10001", "1"),
            entry("01001", "2"),
            entry("11000", "3"),
            entry("00101", "4"),
            entry("10100", "5"),
            entry("01100", "6"),
            entry("00011", "7"),
            entry("10010", "8"),
            entry("10000", "9"),
            entry("00100", "-"),
            entry("00110", "*")
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

        String mappattern = diccionaryKeyToBit.get(key);
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

        ArrayList<Integer> numeros = countValuesWithString(s);

        numeros = eliminarGap(numeros);

        if (numeros.size() % 5 != 0) {
            return null;
        }

        // He creado una lista con los números, la he ordenado, luego he cogido el número mayor y menor los he sumado dividido entre 2 y lo he usado como punto medio
        ArrayList<Integer> sorted = new ArrayList<>(numeros);
        Collections.sort(sorted);

        int narrow = sorted.get(0);
        int wide = sorted.get(sorted.size() - 1);

        int threshold = (narrow + wide) / 2;

        // Hacer un cadena de tipo string en formato 0 y 1
        String numListToString = "";

        for (int i = 0; i < numeros.size(); i++) {
            int numActual = numeros.get(i);

            if (numActual <= threshold){
                numListToString += "0";
            } else {
                numListToString += "1";
            }
        }

        // Hace grupos de 5 y busca en el mapa el grupo de 5 para traducirlo
        String res = "";
        String groupFiveBits = "";

        for (int i = 0; i < numListToString.length(); i++) {
            char bit = numListToString.charAt(i);

            groupFiveBits += bit;

            if ((i + 1) % 5 == 0) {

                if (diccionaryBitToKey.get(groupFiveBits) == null){
                    return null;
                } else {
                    res = res + diccionaryBitToKey.get(groupFiveBits);
                }

                groupFiveBits = "";
                }
            }

        return res;
    }

    private static ArrayList<Integer> eliminarGap(ArrayList<Integer> numeros) {

        ArrayList<Integer> filtered = new ArrayList<>();

        for (int i = 0; i < numeros.size(); i++) {
            if ((i + 1) % 6 != 0) {
                filtered.add(numeros.get(i));
            }
        }

        return filtered;
    }

    //
    private static ArrayList<Integer> countValuesWithString(String s) {

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
        str = str.replaceAll("#.*", "");

        Matcher matcher = Pattern.compile("\\d+").matcher(str);
        List<Integer> numeros = new ArrayList<>();

        while(matcher.find()){
            numeros.add(Integer.parseInt(matcher.group()));
        }

        int ancho = numeros.get(1);
        int altura = numeros.get(2);

        List<Integer> pixeles = new ArrayList<>();

        for (int i = 4; i < numeros.size(); i += 3) {
            if (i + 2 >= numeros.size()) {
                break;
            }
            int r = numeros.get(i);
            int g = numeros.get(i + 1);
            int b = numeros.get(i + 2);

            PixelRGB pixel = new PixelRGB(r, g, b);
            pixeles.add(pixel.es0or1());
        }

        int[][] barcode = new int[altura][ancho];
        int indice = 0;

        for (int i = 0; i < altura; i++) {
            for (int j = 0; j < ancho; j++) {
                if (indice < pixeles.size()) {
                    barcode[i][j] = pixeles.get(indice);
                    indice++;
                }
            }
        }

        for (int f = 0; f < altura; f++) {
            int filaActual = (altura / 2) + ((f % 2 == 0 ? 1 : -1) * ((f + 1) / 2));

            if (filaActual < 0 || filaActual >= altura) {
                continue;
            }

            ArrayList<Integer> filaBarcode = new ArrayList<>();
            for (int i = 0; i < ancho; i++) {
                filaBarcode.add(barcode[filaActual][i]);
            }

            int start = 0;
            while (start < filaBarcode.size() && filaBarcode.get(start) == 1) {
                start++;
            }

            int end = filaBarcode.size() - 1;
            while (end >= 0 && filaBarcode.get(end) == 1) {
                end--;
            }

            ArrayList<Integer> filaRecortada = new ArrayList<>();
            if (start <= end) {
                for (int i = start; i <= end; i++) {
                    filaRecortada.add(filaBarcode.get(i));
                }
            } else {
                continue;
            }

            ArrayList<Integer> valoresBarSpace = countValuesWithInt(filaRecortada);

            if (valoresBarSpace.size() < 20) {
                continue;
            }

            ArrayList<Integer> sorted = new ArrayList<>(valoresBarSpace);
            Collections.sort(sorted);

            double threshold = sorted.get(sorted.size() / 2) * 1.85;

            StringBuilder numListToString = new StringBuilder();
            for (int numActual : valoresBarSpace) {
                if (numActual < threshold) {
                    numListToString.append("0");
                } else {
                    numListToString.append("1");
                }
            }

            StringBuilder res = new StringBuilder();
            StringBuilder groupFiveBits = new StringBuilder();
            boolean valid = true;

            for (int i = 0; i < numListToString.length(); i++) {
                char bit = numListToString.charAt(i);

                if ((i + 1) % 6 == 0) {
                    continue;
                }

                groupFiveBits.append(bit);

                if (groupFiveBits.length() == 5){
                    String value = diccionaryBitToKey.get(groupFiveBits.toString());
                    if (value != null) {
                        res.append(value);
                    } else {
                        valid = false;
                        break;
                    }
                    groupFiveBits.setLength(0);
                }
            }

            String decodedBarcode = res.toString();

            boolean hasMinimumLength = decodedBarcode.length() >= 2;
            boolean hasValidStartAndStop = decodedBarcode.startsWith("*") && decodedBarcode.endsWith("*");
            boolean isCompleteBarcode = hasMinimumLength && hasValidStartAndStop;

            if (valid && isCompleteBarcode) {
                return decodedBarcode;
            }
        }

        return null;
    }

    private static ArrayList<Integer> countValuesWithInt(ArrayList<Integer> pixeles) {
        ArrayList<Integer> res = new ArrayList<>();

        int contador = 1;

        for (int i = 0; i < pixeles.size() - 1; i++) {
            if (pixeles.get(i).equals(pixeles.get(i + 1))){
                contador++;
            } else {
                res.add(contador);
                contador = 1;
            }
        }

        res.add(contador);

        return res;
    }

    // Genera imatge a partir de codi de barres
    // Alçada: 100px
    // Marges: vertical 4px, horizontal 8px
    public static String generateImage(String s) {
        return "";
    }
}
