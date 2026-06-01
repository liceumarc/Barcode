
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

    public static String decodeImage(String str) {
        // 1. Convertir la imagen PPM a una matriz binaria (0 = Negro/Barra, 1 = Blanco/Espacio)
        int[][] matrix = parsePpmToMatrix(str);

        // 2. Generar líneas de escaneo en múltiples ángulos (0º, 90º, 45º, 135º)
        List<int[]> scanlines = generateScanlines(matrix);

        String bestBarcode = "";
        int maxLength = 0;

        // 3. Procesar cada línea buscando la lectura más completa
        for (int[] line : scanlines) {
            String decoded = processScanline(line);

            if (decoded != null && decoded.length() > maxLength) {
                maxLength = decoded.length();
                bestBarcode = decoded;
            }
        }

        return bestBarcode;
    }

    private static int[][] parsePpmToMatrix(String str) {
        str = str.replaceAll("#.*", "");
        Matcher matcher = Pattern.compile("\\d+").matcher(str);
        List<Integer> numeros = new ArrayList<>();

        while (matcher.find()) {
            numeros.add(Integer.parseInt(matcher.group()));
        }

        int ancho = numeros.get(1);
        int altura = numeros.get(2);
        int[][] matrix = new int[altura][ancho];

        int index = 4;
        for (int y = 0; y < altura; y++) {
            for (int x = 0; x < ancho; x++) {
                if (index + 2 < numeros.size()) {
                    int r = numeros.get(index++);
                    int g = numeros.get(index++);
                    int b = numeros.get(index++);
                    PixelRGB pixel = new PixelRGB(r, g, b);
                    matrix[y][x] = pixel.es0or1();
                }
            }
        }
        return matrix;
    }

    private static List<int[]> generateScanlines(int[][] matrix) {
        List<int[]> lines = new ArrayList<>();
        int h = matrix.length;
        int w = matrix[0].length;

        // Escaneos Horizontales (Filas)
        for (int i = 0; i < h; i++) {
            lines.add(matrix[i]);
        }

        // Escaneos Verticales (Columnas) - Para resolver verticalTest()
        for (int x = 0; x < w; x++) {
            int[] col = new int[h];
            for (int y = 0; y < h; y++) col[y] = matrix[y][x];
            lines.add(col);
        }

        // Escaneos Diagonales (\) - Para resolver rotatedImageTest()
        for (int k = -h + 1; k < w; k++) {
            int startX = Math.max(0, k);
            int endX = Math.min(w - 1, h - 1 + k);

            // Calculamos cuántos píxeles tendrá esta diagonal
            int size = endX - startX + 1;

            if (size > 20) {
                int[] diagArray = new int[size];

                // Rellenamos el array clásico con un bucle for
                for (int i = 0; i < size; i++) {
                    int x = startX + i;
                    diagArray[i] = matrix[x - k][x];
                }
                lines.add(diagArray);
            }
        }

        // Escaneos Anti-Diagonales (/) - Para resolver rotatedImageTest()
        for (int k = 0; k < w + h - 1; k++) {
            int startX = Math.max(0, k - h + 1);
            int endX = Math.min(w - 1, k);

            // 1. Calculamos el tamaño exacto de esta anti-diagonal
            int size = endX - startX + 1;

            // 2. Solo creamos el array y lo procesamos si tiene longitud suficiente
            if (size > 20) {
                int[] aDiagArray = new int[size];

                // 3. Rellenamos el array primitivo con un bucle clásico
                for (int i = 0; i < size; i++) {
                    int x = startX + i;
                    aDiagArray[i] = matrix[k - x][x];
                }

                lines.add(aDiagArray);
            }
        }

        return lines;
    }

    private static String processScanline(int[] line) {
        // Encontrar el inicio y fin reales del código de barras (Ignorar márgenes blancos exteriores)
        int firstZero = -1;
        int lastZero = -1;
        for (int i = 0; i < line.length; i++) {
            if (line[i] == 0) {
                if (firstZero == -1) firstZero = i;
                lastZero = i;
            }
        }

        // Si no hay barras o el fragmento es muy pequeño, descartar línea
        if (firstZero == -1 || lastZero - firstZero < 10) return null;

        // Agrupar los píxeles consecutivos en grosores
        List<Integer> widths = new ArrayList<>();
        int current = 0;
        int count = 0;
        for (int i = firstZero; i <= lastZero; i++) {
            if (line[i] == current) {
                count++;
            } else {
                widths.add(count);
                current = line[i];
                count = 1;
            }
        }
        widths.add(count);

        if (widths.size() < 11) return null;

        // Calcular el umbral dinámicamente ignorando las manchas de ruido atípicas
        double threshold = calculateThreshold(widths);

        // Convertir el grosor físico a secuencia lógica ("0" estrecho, "1" ancho)
        StringBuilder logicalStr = new StringBuilder();
        for (int w : widths) {
            logicalStr.append(w <= threshold ? "0" : "1");
        }

        // Intentar leer de Izquierda a Derecha
        String decodedForward = findValidBarcode(logicalStr.toString());

        // Intentar leer de Derecha a Izquierda (Resuelve reverseImageTest)
        String decodedReverse = findValidBarcode(logicalStr.reverse().toString());

        if (decodedForward != null && decodedReverse != null) {
            return decodedForward.length() > decodedReverse.length() ? decodedForward : decodedReverse;
        }
        return decodedForward != null ? decodedForward : decodedReverse;
    }

    private static double calculateThreshold(List<Integer> widths) {
        if (widths.isEmpty()) return 1.0;

        List<Integer> sorted = new ArrayList<>(widths);
        Collections.sort(sorted);

        int narrow = sorted.get(sorted.size() / 2);

        int wideIndex = (int) (sorted.size() * 0.85);
        if (wideIndex >= sorted.size()) {
            wideIndex = sorted.size() - 1;
        }

        int wide = sorted.get(wideIndex);

        if (wide <= narrow) {
            return narrow * 1.85;
        }

        return (narrow + wide) / 2.0;
    }

    private static String findValidBarcode(String logicalStr) {
        String startChar = diccionaryKeyToBit.get('*');
        String bestMatch = null;

        // Saltamos de 2 en 2 porque los caracteres de inicio SIEMPRE empiezan en una barra (índice par)
        for (int i = 0; i <= logicalStr.length() - 5; i += 2) {
            if (logicalStr.substring(i, i + 5).equals(startChar)) {
                StringBuilder res = new StringBuilder();
                boolean valid = true;

                // Leemos los caracteres con saltos de 6 (5 bits + 1 bit de espacio entre caracteres)
                for (int j = i; j <= logicalStr.length() - 5; j += 6) {
                    String chunk = logicalStr.substring(j, j + 5);
                    String ch = diccionaryBitToKey.get(chunk);

                    if (ch == null) {
                        valid = false;
                        break; // Secuencia inválida encontrada por ruido
                    }

                    res.append(ch);

                    // Si encontramos el cierre y el código es coherente
                    if (ch.equals("*") && res.length() >= 2) {
                        if (bestMatch == null || res.length() > bestMatch.length()) {
                            bestMatch = res.toString();
                        }
                        break;
                    }
                }
            }
        }

        return bestMatch;
    }

    // Genera imatge a partir de codi de barres
    // Alçada: 100px
    // Marges: vertical 4px, horizontal 8px
    public static String generateImage(String s) {
        return "";
    }
}
