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
    private Symbol[] symbols;
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
        symbols = new Symbol[0];
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
     *
     * @param color visual color of the new symbol
     * @return true if the symbol was added, false otherwise
     */
    public boolean addSymbol(String color)
    {
        if(contains(color))
        {
            return false;
        }
    
        Symbol[] n = new Symbol[symbols.length + 1];
    
        for(int i = 0; i < symbols.length; i++)
        {
            n[i] = symbols[i];
        }
    
        n[symbols.length] = new Symbol(color);
    
        symbols = n;
    
        return true;
    }

    /**
     * Removes the first occurrence of a symbol from this wheel.
     *
     * @param symbol color of the symbol to remove
     * @return true if the symbol was removed, false otherwise
     */
    public boolean delSymbol(String symbol)
    {
        int pos = find(symbol);
    
        if(pos == -1){
            return false;
        }
    
        Symbol[] n = new Symbol[symbols.length - 1];
    
        for(int i = 0; i < pos; i++){
            n[i] = symbols[i];
        }
    
        for(int i = pos; i < n.length; i++){
            n[i] = symbols[i + 1];
        }
    
        symbols = n;
    
        if(currentSymbol >= symbols.length){
            currentSymbol = 0;
        }
    
        return true;
    }

    /**
     * Positions the visible symbol to the given color.
     *
     * @param symbol color to display
     * @return true if the symbol exists
     */
    public boolean placeSymbol(String symbol)
    {
        int pos = find(symbol);
        if(pos == -1){
            return false;
        }
        
        currentSymbol = pos;
        
        if(visible){
            circle.changeColor(symbols[currentSymbol].getColor());
        }
    
        return true;
    }

    // ── spin ─────────────────────────────────────────────────────────────────

    /**
     * Advances the wheel one position.
     * Returns false if locked or empty.
     *
     * @return true if the wheel moved
     */
    public boolean spin()
    {
        if(symbols.length == 0 || locked){
            return false;
        }
    
        currentSymbol++;
    
        if(currentSymbol >= symbols.length){
            currentSymbol = 0;
        }
    
        if(visible){
            circle.changeColor(symbols[currentSymbol].getColor());
            Canvas.getCanvas().wait(150);
        }
    
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
     * Unlocks the wheel.
     * Visual: restores symbol color and green lock bar.
     */
    public void unlock()
    {
        locked = false;
    
        if(visible){
            String color = (symbols.length > 0) ? symbols[currentSymbol].getColor():"white";
    
            circle.changeColor(color);
            lockBar.changeColor("green");
        }
    }

    /** Returns whether this wheel is currently locked. */
    public boolean isLocked(){ return locked; }

    // ── query ─────────────────────────────────────────────────────────────────

    /**
     * Returns the currently visible symbol color,
     * or null if the wheel is empty.
     *
     * @return current symbol color
     */
    public String configuration()
    {
        if(symbols.length == 0)
        {
            return null;
        }
    
        return symbols[currentSymbol].getColor();
    }
    
    /**
     * Returns all symbol colors contained in this wheel.
     *
     * @return array with symbol colors
     */
    public String[] symbols()
    {
        String[] result = new String[symbols.length];
    
        for(int i = 0; i < symbols.length; i++){
            result[i] = symbols[i].getColor();
        }
    
        return result;
    }

    // ── visibility ────────────────────────────────────────────────────────────

    /**
     * Shows all visual elements of this wheel.
     */
    public void makeVisible()
    {
        visible = true;
    
        slotFrame.makeVisible();
        lockBar.makeVisible();
    
        String color = (symbols.length > 0) ? symbols[currentSymbol].getColor(): "white";
    
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

    private int find(String color)
    {
        for(int i = 0; i < symbols.length; i++)
        {
            if(symbols[i].getColor().equals(color))
            {
                return i;
            }
        }
    
        return -1;
    }
}
