/**
 * Presentación visual de la tragamonedas: fondo oscuro y banner dorado
 * de jackpot. Usa dos Rectangle del paquete shapes.
 *
 * @author BlancoS-GarzonR
 * @version 1.0
 */
public class SlotMachineView {

    private static final int BANNER_H    = 30;
    private static final int BANNER_Y    = 8;
    private static final int BG_X        = 0;
    private static final int BG_Y        = 0;
    private static final int RECT_INIT_X = 70;
    private static final int RECT_INIT_Y = 15;

    private final Rectangle background;
    private final Rectangle jackpotBanner;
    private boolean isVisible;
    private int bgX;
    private int bgY;
    private int bannerX;
    private int bannerY;

    /**
     * Crea la vista con las dimensiones indicadas. Inicia invisible.
     *
     * @param width  ancho total de la máquina en píxeles
     * @param height alto total de la máquina en píxeles
     */
    public SlotMachineView(int width, int height) {
        background = new Rectangle();
        background.changeSize(height, width);
        background.changeColor("black");
        bgX = RECT_INIT_X;
        bgY = RECT_INIT_Y;
        moveRect(background, BG_X, BG_Y, bgX, bgY);
        bgX = BG_X;
        bgY = BG_Y;

        jackpotBanner = new Rectangle();
        jackpotBanner.changeSize(BANNER_H, width);
        jackpotBanner.changeColor("gold");
        bannerX = RECT_INIT_X;
        bannerY = RECT_INIT_Y;
        moveRect(jackpotBanner, BG_X, BANNER_Y, bannerX, bannerY);
        bannerX = BG_X;
        bannerY = BANNER_Y;

        isVisible = false;
    }

    /**
     * Ajusta el tamaño del fondo y el banner al nuevo ancho y alto.
     *
     * @param width  nuevo ancho en píxeles
     * @param height nuevo alto en píxeles
     */
    public void resize(int width, int height) {
        background.changeSize(height, width);
        jackpotBanner.changeSize(BANNER_H, width);
    }

    /**
     * Muestra el banner de jackpot. Solo actúa si el simulador está visible.
     */
    public void showJackpot() {
        if (isVisible) jackpotBanner.makeVisible();
    }

    /** Oculta el banner de jackpot. */
    public void hideJackpot() {
        jackpotBanner.makeInvisible();
    }

    /** Hace visible el fondo en el canvas. */
    public void makeVisible() {
        background.makeVisible();
        isVisible = true;
    }

    /** Oculta el fondo y el banner del canvas. */
    public void makeInvisible() {
        background.makeInvisible();
        jackpotBanner.makeInvisible();
        isVisible = false;
    }

    // -------- métodos privados --------

    /** Mueve rect de (fromX,fromY) a (toX,toY). */
    private void moveRect(Rectangle rect, int toX, int toY, int fromX, int fromY) {
        rect.moveHorizontal(toX - fromX);
        rect.moveVertical(toY - fromY);
    }
}
