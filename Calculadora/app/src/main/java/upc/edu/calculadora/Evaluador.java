package upc.edu.calculadora;

/**
 * Evaluador de expresiones matemáticas (parser descendente recursivo).
 *
 * Soporta: + − × ÷ ^, paréntesis, π, √( ), sin( ), cos( ), tan( ), atan( )
 * y multiplicación implícita, por ejemplo 2√(9) o 3(4+1).
 */
public class Evaluador {

    private final String s;
    private final boolean grados;
    private int pos = -1;
    private int ch;

    private Evaluador(String expresion, boolean grados) {
        this.s = expresion;
        this.grados = grados;
    }

    /** Evalúa la expresión. Lanza ArithmeticException si no es válida. */
    public static double evaluar(String expresion, boolean grados) {
        // Cierra automáticamente los paréntesis que falten
        int abiertos = 0;
        for (char c : expresion.toCharArray()) {
            if (c == '(') abiertos++;
            else if (c == ')') abiertos--;
        }
        StringBuilder sb = new StringBuilder(expresion);
        for (int i = 0; i < abiertos; i++) sb.append(')');

        Evaluador e = new Evaluador(sb.toString(), grados);
        e.siguiente();
        double r = e.parseExpresion();
        if (e.pos < e.s.length()) throw new ArithmeticException("Carácter inesperado");
        if (Double.isNaN(r) || Double.isInfinite(r)) throw new ArithmeticException("Error matemático");
        return r;
    }

    private void siguiente() {
        ch = (++pos < s.length()) ? s.charAt(pos) : -1;
    }

    private boolean consumir(int c) {
        while (ch == ' ') siguiente();
        if (ch == c) {
            siguiente();
            return true;
        }
        return false;
    }

    // expresion = termino { (+|−) termino }
    private double parseExpresion() {
        double x = parseTermino();
        while (true) {
            if (consumir('+')) x += parseTermino();
            else if (consumir('−') || consumir('-')) x -= parseTermino();
            else return x;
        }
    }

    // termino = factor { (×|÷|implícita) factor }
    private double parseTermino() {
        double x = parseFactor();
        while (true) {
            if (consumir('×') || consumir('*')) x *= parseFactor();
            else if (consumir('÷') || consumir('/')) {
                double d = parseFactor();
                if (d == 0) throw new ArithmeticException("División entre 0");
                x /= d;
            } else if (empiezaFactor()) x *= parseFactor(); // multiplicación implícita
            else return x;
        }
    }

    private boolean empiezaFactor() {
        return ch == '(' || ch == 'π' || ch == '√' || Character.isLetter(ch)
                || Character.isDigit(ch) || ch == '.';
    }

    // factor = (+|−) factor | base [^ factor]
    private double parseFactor() {
        if (consumir('+')) return parseFactor();
        if (consumir('−') || consumir('-')) return -parseFactor();

        double x;
        int inicio = pos;
        if (consumir('(')) {
            x = parseExpresion();
            if (!consumir(')')) throw new ArithmeticException("Falta ')'");
        } else if ((ch >= '0' && ch <= '9') || ch == '.') {
            while ((ch >= '0' && ch <= '9') || ch == '.') siguiente();
            try {
                x = Double.parseDouble(s.substring(inicio, pos));
            } catch (NumberFormatException ex) {
                throw new ArithmeticException("Número no válido");
            }
        } else if (consumir('π')) {
            x = Math.PI;
        } else if (consumir('√')) {
            double a = parseFactor();
            if (a < 0) throw new ArithmeticException("Raíz de negativo");
            x = Math.sqrt(a);
        } else if (Character.isLetter(ch)) {
            while (Character.isLetter(ch)) siguiente();
            String func = s.substring(inicio, pos);
            double a = parseFactor();
            x = aplicarFuncion(func, a);
        } else {
            throw new ArithmeticException("Expresión incompleta");
        }

        if (consumir('^')) x = Math.pow(x, parseFactor());
        return x;
    }

    private double aplicarFuncion(String func, double a) {
        double rad = grados ? Math.toRadians(a) : a;
        switch (func) {
            case "sin":
                return limpiar(Math.sin(rad));
            case "cos":
                return limpiar(Math.cos(rad));
            case "tan": {
                double c = Math.cos(rad);
                if (Math.abs(c) < 1e-12) throw new ArithmeticException("Tangente indefinida");
                return limpiar(Math.tan(rad));
            }
            case "atan": {
                double r = Math.atan(a);
                return grados ? Math.toDegrees(r) : r;
            }
            default:
                throw new ArithmeticException("Función desconocida: " + func);
        }
    }

    /** Evita resultados como 1.2246E-16 en lugar de 0 (p. ej. sin(180)). */
    private static double limpiar(double v) {
        return Math.abs(v) < 1e-12 ? 0 : v;
    }

    /** Formatea el resultado sin ceros sobrantes (máx. 10 cifras significativas). */
    public static String formatear(double v) {
        if (v == Math.rint(v) && Math.abs(v) < 1e15) {
            return String.valueOf((long) v).replace('-', '−');
        }
        java.math.BigDecimal bd = new java.math.BigDecimal(v)
                .round(new java.math.MathContext(10))
                .stripTrailingZeros();
        String txt = (Math.abs(v) >= 1e-6 && Math.abs(v) < 1e15)
                ? bd.toPlainString()
                : bd.toString();
        return txt.replace('-', '−');
    }
}
