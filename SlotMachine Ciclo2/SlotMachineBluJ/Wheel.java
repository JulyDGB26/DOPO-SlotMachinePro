/**
 * Represents a wheel in the slot machine.
 * Each wheel has a slot-frame window, a colored symbol circle,
 * and a lock indicator bar below the frame.
 *
 * @author Julian Gomez Boada - Julian Carrero Carranza
 * @version Ciclo2-visual (05/09/2026)
 */
public class Wheel
{
    private String[] symbols;
    private int currentSymbol;
    private Circle    circle;
    private Rectangle slotFrame;
    private Rectangle lockBar;
    private boolean   locked;
    private boolean   visible;

    // Horizontal slot origin and spacing (pixels)
    private static final int SLOT_ORIGIN  = 55;
    private static final int SLOT_SPACING = 130;

    /**
     * Creates a wheel at the given 0-based slot index.
     *
     * @param slotIndex 0-based position of this wheel in the machine
     */
    public Wheel(int slotIndex)
    {
        symbols = new String[0];
        currentSymbol = 0;
        locked  = false;
        visible = false;

        int sx = SLOT_ORIGIN + SLOT_SPACING * slotIndex;

        // Slot frame: white window behind the symbol
        // Rectangle default: x=70, y=15
        slotFrame = new Rectangle();
        slotFrame.changeColor("white");
        slotFrame.changeSize(145, 90);
        slotFrame.moveHorizontal(sx - 70);
        slotFrame.moveVertical(57);        // target y=72

        // Symbol circle: centered inside slotFrame (diameter 70)
        // Circle default: x=20, y=15
        circle = new Circle();
        circle.changeColor("white");
        circle.changeSize(70);
        circle.moveHorizontal(sx - 10);   // target x=sx+10; from 20: (sx+10)-20=sx-10
        circle.moveVertical(94);           // target y=109; from 15: 109-15=94

        // Lock indicator: green = unlocked, red = locked
        lockBar = new Rectangle();
        lockBar.changeColor("green");
        lockBar.changeSize(16, 90);
        lockBar.moveHorizontal(sx - 70);
        lockBar.moveVertical(207);         // target y=222; from 15: 207
    }

    // ── symbol management ────────────────────────────────────────────────────

    /**
     * Adds a new symbol (color) to this wheel.
     */
    public boolean addSymbol(String color)
    {
        if(contains(color)){ return false; }
        String[] n = new String[symbols.length + 1];
        for(int i = 0; i < symbols.length; i++){ n[i] = symbols[i]; }
        n[symbols.length] = color;
        symbols = n;
        return true;
    }

    /**
     * Removes the first occurrence of a symbol from this wheel.
     */
    public boolean delSymbol(String symbol)
    {
        int pos = find(symbol);
        if(pos == -1){ return false; }
        String[] n = new String[symbols.length - 1];
        for(int i = 0; i < pos; i++){ n[i] = symbols[i]; }
        for(int i = pos; i < n.length; i++){ n[i] = symbols[i + 1]; }
        symbols = n;
        if(currentSymbol >= symbols.length){ currentSymbol = 0; }
        return true;
    }

    /**
     * Positions the visible symbol to the given color.
     */
    public boolean placeSymbol(String symbol)
    {
        int pos = find(symbol);
        if(pos == -1){ return false; }
        currentSymbol = pos;
        if(visible) circle.changeColor(symbols[currentSymbol]);
        return true;
    }

    // ── spin ─────────────────────────────────────────────────────────────────

    /**
     * Advances the wheel one position. Returns false if locked or empty.
     */
    public boolean spin()
    {
        if(symbols.length == 0 || locked){ return false; }
        currentSymbol++;
        if(currentSymbol >= symbols.length){ currentSymbol = 0; }
        if(visible) circle.changeColor(symbols[currentSymbol]);
        return true;
    }

    // ── lock / unlock ─────────────────────────────────────────────────────────

    /**
     * Locks the wheel. Visual: circle turns gray, lock bar turns red.
     */
    public void lock()
    {
        locked = true;
        if(visible){
            circle.changeColor("gray");
            lockBar.changeColor("red");
        }
    }

    /**
     * Unlocks the wheel. Visual: circle restores symbol color, lock bar turns green.
     */
    public void unlock()
    {
        locked = false;
        if(visible){
            String color = (symbols.length > 0) ? symbols[currentSymbol] : "white";
            circle.changeColor(color);
            lockBar.changeColor("green");
        }
    }

    /** Returns whether this wheel is currently locked. */
    public boolean isLocked(){ return locked; }

    // ── query ─────────────────────────────────────────────────────────────────

    /** Returns the currently visible symbol color, or null if the wheel is empty. */
    public String configuration()
    {
        if(symbols.length == 0){ return null; }
        return symbols[currentSymbol];
    }

    /** Returns all symbols in this wheel. */
    public String[] symbols(){ return symbols; }

    // ── visibility ────────────────────────────────────────────────────────────

    /**
     * Shows all visual elements of this wheel on the canvas.
     */
    public void makeVisible()
    {
        visible = true;
        slotFrame.makeVisible();
        lockBar.makeVisible();
        String color = (symbols.length > 0) ? symbols[currentSymbol] : "white";
        circle.changeColor(color);
        circle.makeVisible();
    }

    /**
     * Hides all visual elements of this wheel from the canvas.
     */
    public void makeInvisible()
    {
        visible = false;
        circle.makeInvisible();
        lockBar.makeInvisible();
        slotFrame.makeInvisible();
    }

    // ── private helpers ───────────────────────────────────────────────────────

    private boolean contains(String color){ return find(color) != -1; }

    private int find(String symbol)
    {
        for(int i = 0; i < symbols.length; i++){
            if(symbols[i].equals(symbol)){ return i; }
        }
        return -1;
    }
}
