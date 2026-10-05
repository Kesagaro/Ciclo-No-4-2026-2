import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * Ventana gráfica compartida por todas las formas (singleton).
 * Extendida para el proyecto slotMachine con soporte de colores CSS
 * adicionales, redimensionado dinámico y cierre de ventana.
 *
 * @author BlancoS-GarzonR
 * @version 1.7
 */
public class Canvas {

    private static Canvas canvasSingleton;

    /** Colores del estándar CSS: nombre en minúsculas → valor RGB. */
    private static final HashMap<String, Integer> CSS_COLORS = new HashMap<String, Integer>();

    static {
        String[] table = {
            "aliceblue", "F0F8FF", "antiquewhite", "FAEBD7", "aqua", "00FFFF", "aquamarine", "7FFFD4",
            "azure", "F0FFFF", "beige", "F5F5DC", "bisque", "FFE4C4", "black", "000000",
            "blanchedalmond", "FFEBCD", "blue", "0000FF", "blueviolet", "8A2BE2", "brown", "A52A2A",
            "burlywood", "DEB887", "cadetblue", "5F9EA0", "chartreuse", "7FFF00", "chocolate", "D2691E",
            "coral", "FF7F50", "cornflowerblue", "6495ED", "cornsilk", "FFF8DC", "crimson", "DC143C",
            "cyan", "00FFFF", "darkblue", "00008B", "darkcyan", "008B8B", "darkgoldenrod", "B8860B",
            "darkgray", "A9A9A9", "darkgreen", "006400", "darkgrey", "A9A9A9", "darkkhaki", "BDB76B",
            "darkmagenta", "8B008B", "darkolivegreen", "556B2F", "darkorange", "FF8C00", "darkorchid", "9932CC",
            "darkred", "8B0000", "darksalmon", "E9967A", "darkseagreen", "8FBC8F", "darkslateblue", "483D8B",
            "darkslategray", "2F4F4F", "darkslategrey", "2F4F4F", "darkturquoise", "00CED1", "darkviolet", "9400D3",
            "deeppink", "FF1493", "deepskyblue", "00BFFF", "dimgray", "696969", "dimgrey", "696969",
            "dodgerblue", "1E90FF", "firebrick", "B22222", "floralwhite", "FFFAF0", "forestgreen", "228B22",
            "fuchsia", "FF00FF", "gainsboro", "DCDCDC", "ghostwhite", "F8F8FF", "gold", "FFD700",
            "goldenrod", "DAA520", "gray", "808080", "green", "008000", "greenyellow", "ADFF2F",
            "grey", "808080", "honeydew", "F0FFF0", "hotpink", "FF69B4", "indianred", "CD5C5C",
            "indigo", "4B0082", "ivory", "FFFFF0", "khaki", "F0E68C", "lavender", "E6E6FA",
            "lavenderblush", "FFF0F5", "lawngreen", "7CFC00", "lemonchiffon", "FFFACD", "lightblue", "ADD8E6",
            "lightcoral", "F08080", "lightcyan", "E0FFFF", "lightgoldenrodyellow", "FAFAD2", "lightgray", "D3D3D3",
            "lightgreen", "90EE90", "lightgrey", "D3D3D3", "lightpink", "FFB6C1", "lightsalmon", "FFA07A",
            "lightseagreen", "20B2AA", "lightskyblue", "87CEFA", "lightslategray", "778899", "lightslategrey", "778899",
            "lightsteelblue", "B0C4DE", "lightyellow", "FFFFE0", "lime", "00FF00", "limegreen", "32CD32",
            "linen", "FAF0E6", "magenta", "FF00FF", "maroon", "800000", "mediumaquamarine", "66CDAA",
            "mediumblue", "0000CD", "mediumorchid", "BA55D3", "mediumpurple", "9370DB", "mediumseagreen", "3CB371",
            "mediumslateblue", "7B68EE", "mediumspringgreen", "00FA9A", "mediumturquoise", "48D1CC", "mediumvioletred", "C71585",
            "midnightblue", "191970", "mintcream", "F5FFFA", "mistyrose", "FFE4E1", "moccasin", "FFE4B5",
            "navajowhite", "FFDEAD", "navy", "000080", "oldlace", "FDF5E6", "olive", "808000",
            "olivedrab", "6B8E23", "orange", "FFA500", "orangered", "FF4500", "orchid", "DA70D6",
            "palegoldenrod", "EEE8AA", "palegreen", "98FB98", "paleturquoise", "AFEEEE", "palevioletred", "DB7093",
            "papayawhip", "FFEFD5", "peachpuff", "FFDAB9", "peru", "CD853F", "pink", "FFC0CB",
            "plum", "DDA0DD", "powderblue", "B0E0E6", "purple", "800080", "rebeccapurple", "663399",
            "red", "FF0000", "rosybrown", "BC8F8F", "royalblue", "4169E1", "saddlebrown", "8B4513",
            "salmon", "FA8072", "sandybrown", "F4A460", "seagreen", "2E8B57", "seashell", "FFF5EE",
            "sienna", "A0522D", "silver", "C0C0C0", "skyblue", "87CEEB", "slateblue", "6A5ACD",
            "slategray", "708090", "slategrey", "708090", "snow", "FFFAFA", "springgreen", "00FF7F",
            "steelblue", "4682B4", "tan", "D2B48C", "teal", "008080", "thistle", "D8BFD8",
            "tomato", "FF6347", "turquoise", "40E0D0", "violet", "EE82EE", "wheat", "F5DEB3",
            "white", "FFFFFF", "whitesmoke", "F5F5F5", "yellow", "FFFF00", "yellowgreen", "9ACD32"
        };
        for (int i = 0; i < table.length; i += 2) {
            CSS_COLORS.put(table[i], Integer.valueOf(table[i + 1], 16));
        }
    }

    /**
     * Retorna el color indicado por un nombre CSS estándar (por ejemplo
     * {@code "salmon"}) o por un código hexadecimal {@code #rrggbb}.
     * Si el texto no se reconoce retorna negro.
     *
     * @param colorString nombre CSS o código hex del color
     * @return el color; negro si es nulo o desconocido
     */
    public static Color colorOf(String colorString) {
        if (colorString == null) return Color.black;
        if (colorString.startsWith("#")) {
            try {
                return Color.decode(colorString);
            } catch (NumberFormatException e) {
                return Color.black;
            }
        }
        Integer rgb = CSS_COLORS.get(colorString.toLowerCase());
        return rgb == null ? Color.black : new Color(rgb.intValue());
    }

    /**
     * Retorna la instancia única del canvas, creándola y mostrándola si no existe.
     * Si ya existe no cambia su visibilidad: quien quiera mostrarla de nuevo
     * debe llamar {@code setVisible(true)}.
     *
     * @return instancia singleton
     */
    public static Canvas getCanvas() {
        if (canvasSingleton == null) {
            canvasSingleton = new Canvas("DOPO-POOB — Slot Machine", 400, 220, Color.white);
            canvasSingleton.setVisible(true);
        }
        return canvasSingleton;
    }

    /**
     * Muestra u oculta la ventana sin forzar la creación del singleton.
     *
     * @param visible {@code true} para mostrar, {@code false} para ocultar
     */
    public static void setCanvasVisible(boolean visible) {
        if (canvasSingleton != null) {
            canvasSingleton.frame.setVisible(visible);
        }
    }

    /**
     * Cierra y libera la ventana solo si ya fue creada; si nunca se creó
     * no hace nada (no abre una ventana solo para cerrarla).
     */
    public static void closeCanvas() {
        if (canvasSingleton != null) {
            canvasSingleton.close();
        }
    }

    // ----- parte de instancia -----

    private JFrame frame;
    private CanvasPane canvas;
    private Graphics2D graphic;
    private Color backgroundColour;
    private Image canvasImage;
    private List<Object> objects;
    private HashMap<Object, ShapeDescription> shapes;

    /**
     * Construye el canvas con los parámetros dados.
     *
     * @param title    título de la ventana
     * @param width    ancho en píxeles
     * @param height   alto en píxeles
     * @param bgColour color de fondo
     */
    private Canvas(String title, int width, int height, Color bgColour) {
        frame = new JFrame();
        canvas = new CanvasPane();
        frame.setContentPane(canvas);
        frame.setTitle(title);
        canvas.setPreferredSize(new Dimension(width, height));
        backgroundColour = bgColour;
        frame.pack();
        objects = new ArrayList<Object>();
        shapes = new HashMap<Object, ShapeDescription>();
    }

    /**
     * Hace visible u oculta el canvas. En la primera llamada inicializa
     * el buffer offscreen.
     *
     * @param visible {@code true} para mostrar
     */
    public void setVisible(boolean visible) {
        if (graphic == null) {
            Dimension size = canvas.getSize();
            canvasImage = canvas.createImage(size.width, size.height);
            graphic = (Graphics2D) canvasImage.getGraphics();
            graphic.setColor(backgroundColour);
            graphic.fillRect(0, 0, size.width, size.height);
            graphic.setColor(Color.black);
        }
        frame.setVisible(visible);
    }

    /**
     * Redimensiona la ventana y recrea el buffer offscreen.
     *
     * @param width  nuevo ancho en píxeles
     * @param height nuevo alto en píxeles
     */
    public void resize(int width, int height) {
        canvas.setPreferredSize(new Dimension(width, height));
        frame.pack();
        if (graphic != null) {
            Dimension size = canvas.getSize();
            canvasImage = canvas.createImage(size.width, size.height);
            graphic = (Graphics2D) canvasImage.getGraphics();
            graphic.setColor(backgroundColour);
            graphic.fillRect(0, 0, size.width, size.height);
            graphic.setColor(Color.black);
            redraw();
        }
    }

    /**
     * Cierra y libera la ventana del canvas.
     */
    public void close() {
        frame.dispose();
    }

    /**
     * Dibuja una forma en el canvas asociándola a un objeto de referencia.
     *
     * @param referenceObject identificador de la forma
     * @param color           color de relleno
     * @param shape           forma AWT a dibujar
     */
    public void draw(Object referenceObject, String color, Shape shape) {
        objects.remove(referenceObject);
        objects.add(referenceObject);
        shapes.put(referenceObject, new ShapeDescription(shape, color));
        redraw();
    }

    /**
     * Borra la forma asociada al objeto de referencia.
     *
     * @param referenceObject identificador de la forma a borrar
     */
    public void erase(Object referenceObject) {
        objects.remove(referenceObject);
        shapes.remove(referenceObject);
        redraw();
    }

    /**
     * Establece el color de dibujo activo. Soporta los nombres CSS estándar
     * y valores hex {@code #rrggbb} (ver {@link #colorOf(String)}).
     *
     * @param colorString nombre CSS o código hex del color
     */
    public void setForegroundColor(String colorString) {
        graphic.setColor(colorOf(colorString));
    }

    /**
     * Pausa la ejecución el número de milisegundos indicado.
     *
     * @param milliseconds tiempo de espera en ms
     */
    public void wait(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (Exception e) { /* ignorado */ }
    }

    // -------- métodos privados --------

    /** Redibuja todas las formas registradas en orden de inserción. */
    private void redraw() {
        erase();
        for (Iterator i = objects.iterator(); i.hasNext();) {
            shapes.get(i.next()).draw(graphic);
        }
        canvas.repaint();
    }

    /** Limpia el fondo del canvas sin repintar. */
    private void erase() {
        Color original = graphic.getColor();
        graphic.setColor(backgroundColour);
        Dimension size = canvas.getSize();
        graphic.fill(new java.awt.Rectangle(0, 0, size.width, size.height));
        graphic.setColor(original);
    }

    /** Panel interno que renderiza el buffer offscreen. */
    private class CanvasPane extends JPanel {
        public void paint(Graphics g) {
            g.drawImage(canvasImage, 0, 0, null);
        }
    }

    /** Asocia una forma AWT con su color de relleno. */
    private class ShapeDescription {
        private Shape shape;
        private String colorString;

        public ShapeDescription(Shape shape, String color) {
            this.shape = shape;
            colorString = color;
        }

        public void draw(Graphics2D graphic) {
            setForegroundColor(colorString);
            graphic.draw(shape);
            graphic.fill(shape);
        }
    }
}
