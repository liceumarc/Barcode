import java.util.ArrayList;
import java.util.List;

public class Code93 {


    public static final String[] PATRONES = {
            "131112", "111213", "111312", "111411", "121113", "121212", "121311", "111114", "131211", "141111", // 0-9
            "211113", "211212", "211311", "221112", "221211", "231111", "112113", "112212", "112311", "122112", // A-J
            "132111", "111123", "111222", "111321", "121122", "131121", "212112", "212211", "211122", "211221", // K-T
            "221121", "222111", "112122", "112221", "122121", "123111", "121131", "311112", "311211", "321111", // U-Z, -, ., Espacio, $
            "112131", "113121", "211131", "121221", "312111", "311121", "122211", "111141"                      // /, +, %, (, ), [, ], *
    };

    public static final String CARACTERES = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. $/+%()[]*";


    public static String encode(String texto) {
        List<Integer> valores = new ArrayList<>();

        for (char letra : texto.toCharArray()) {
            valores.add(CARACTERES.indexOf(letra));
        }

        int checksumC = calcularChecksum(valores, 20);
        int checksumK = calcularChecksumConC(valores, checksumC, 15);

        // Orden --> Inicio / Data / CheckSumC / CheckSumK / Fin / Barra
        String barras = dibujar(47);
        for (int valor : valores) {
            barras += dibujar(valor);
        }
        barras += dibujar(checksumC);
        barras += dibujar(checksumK);
        barras += dibujar(47);
        barras += "█";

        return barras;
    }

    private static String dibujar(int valor) {
        String patron = PATRONES[valor];
        String dibujo = "";
        boolean pintarBarra = true;

        for (int i = 0; i < patron.length(); i++) {
            int cantidad = patron.charAt(i) - '0';
            for (int j = 0; j < cantidad; j++) {
                dibujo += pintarBarra ? "█" : " ";
            }
            pintarBarra = !pintarBarra;
        }
        return dibujo;
    }



    public static String decode(String barras) {

        if (barras == null || !barras.endsWith("█")) return null;

       return "";
    }


    private static int calcularChecksum(List<Integer> datos, int pesoMaximo) {
        int suma = 0;
        int peso = 1;
        for (int i = datos.size() - 1; i >= 0; i--) {
            suma += datos.get(i) * peso;
            peso++;
            if (peso > pesoMaximo) peso = 1;
        }
        return suma % 47;
    }

    private static int calcularChecksumConC(List<Integer> datos, int valorC, int pesoMaximo) {
        List<Integer> listaConC = new ArrayList<>(datos);
        listaConC.add(valorC);
        return calcularChecksum(listaConC, pesoMaximo);
    }

    public static String decodeImage(String str) { return ""; }
    public static String generateImage(String s) { return ""; }
}