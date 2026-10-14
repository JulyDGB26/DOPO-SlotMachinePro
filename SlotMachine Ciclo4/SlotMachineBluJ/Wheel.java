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
    
    private Triangle symbolMarker;
    private int circleOffset;
    private int slotIndex;

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
        circleOffset = 0;
        this.slotIndex = slotIndex;

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
        
        // Symbol type identification marker.
        symbolMarker = new Triangle();
        symbolMarker.changeColor("green");
        symbolMarker.changeSize(10, 12);
        symbolMarker.moveHorizontal((sx + 75) - 140);
        symbolMarker.moveVertical(80 - 15);
    }

    /**
     * Moves the existing graphical elements to a new 0-based slot.
     * Symbol objects, selection, size, visibility and lock state
     * are preserved. Normally called while the machine is hidden.
     *
     * @param newSlotIndex new 0-based position in the machine
     */
    public void moveToSlot(int newSlotIndex)
    {
        int movement = (newSlotIndex - slotIndex) * SLOT_SPACING;

        if(movement == 0){
            return;
        }

        slotFrame.moveHorizontal(movement);
        circle.moveHorizontal(movement);
        lockBar.moveHorizontal(movement);
        symbolMarker.moveHorizontal(movement);

        slotIndex = newSlotIndex;
    }

    // ── symbol management ────────────────────────────────────────────────────

    /**
     * Adds a normal symbol to this wheel.
     *
     * This operation preserves the original behavior
     * of the wheel from previous cycles.
     *
     * @param color visual color of the new symbol
     * @return true if the symbol was added, false otherwise
     */
    public boolean addSymbol(String color)
    {
        return addSymbol("normal", color);
    }
    
    /**
     * Adds a symbol of the indicated type to this wheel.
     *
     * Available symbol types are normal, ephemeral, shy and pulsing.
     *
     * @param type symbol type
     * @param color visual color of the new symbol
     * @return true if the symbol was added, false otherwise
     */
    public boolean addSymbol(String type, String color)
    {
        if(contains(color)){
            return false;
        }
    
        Symbol newSymbol = createSymbol(type, color);
    
        if(newSymbol == null){
            return false;
        }
    
        Symbol[] n = new Symbol[symbols.length + 1];
    
        for(int i = 0; i < symbols.length; i++){
            n[i] = symbols[i];
        }
    
        n[symbols.length] = newSymbol;
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
        
        updateSymbolAppearance();
        return true;
    }

    /**
     * Positions the indicated symbol as the current symbol.
     *
     * Selecting a symbol activates its selection behavior.
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
    
        selectSymbol(pos);
        updateSymbolAppearance();
    
        return true;
    }
    
    /**
     * Selects a symbol as a consequence of a specialized wheel spin.
     *
     * This operation is available to Wheel subclasses so specialized
     * spinning behavior can preserve symbol events without accessing
     * the internal Symbol array.
     *
     * @param color color of the symbol to select
     * @return true if the symbol exists
     */
    protected boolean placeSymbolBySpin(String color)
    {
        int pos = find(color);
    
        if(pos == -1){
            return false;
        }
    
        selectBySpin(pos);
        updateSymbolAppearance();
    
        return true;
    }

    // ── spin ─────────────────────────────────────────────────────────────────

    /**
     * Advances the wheel one position.
     *
     * The selected symbol receives the selection event and the
     * spin event. Returns false if the wheel is locked or empty.
     *
     * @return true if the wheel moved
     */
    public boolean spin()
    {
        if(symbols.length == 0 || locked){
            return false;
        }
    
        int nextSymbol = currentSymbol + 1;
    
        if(nextSymbol >= symbols.length){
            nextSymbol = 0;
        }
    
        selectBySpin(nextSymbol);
        updateSymbolAppearance();
    
        if(visible){
            Canvas.getCanvas().wait(150);
        }
    
        return true;
    }
    
    /**
     * Spins this wheel considering the wheel located immediately
     * to its left.
     *
     * A normal wheel does not require information from its neighbor,
     * therefore it preserves its regular spin behavior.
     * Specialized wheels may override this operation.
     *
     * @param leftWheel wheel immediately to the left, or null if none exists
     * @return true if the operation was successfully performed
     */
    public boolean spin(Wheel leftWheel)
    {
        return spin();
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
     * Unlocks the wheel so it can rotate again.
     *
     * The current symbol restores its own visual representation.
     */
    public void unlock()
    {
        locked = false;
    
        if(visible){
            lockBar.changeColor("green");
            updateSymbolAppearance();
        }
    }

    /** Returns whether this wheel is currently locked. */
    public boolean isLocked(){ return locked; }

    /**
     * Indicates whether this wheel can be locked.
     * Normal wheels allow this operation.
     *
     * @return true if the wheel can be locked
     */
    public boolean canBeLocked()
    {
        return true;
    }
    
    /**
     * Indicates whether this wheel can be swapped.
     * Normal wheels allow this operation.
     *
     * @return true if the wheel can be swapped
     */
    public boolean canBeSwapped()
    {
        return true;
    }
    
    /**
     * Indicates whether this wheel can be deleted.
     * Normal wheels allow this operation.
     *
     * @return true if the wheel can be deleted
     */
    public boolean canBeDeleted()
    {
        return true;
    }
    
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
     * Shows the visual elements of this wheel.
     *
     * The frame color identifies the wheel type.
     * The current symbol preserves its own visual state.
     */
    public void makeVisible()
    {
        visible = true;
    
        slotFrame.changeColor(frameColor());
        slotFrame.makeVisible();
    
        lockBar.changeColor(locked ? "red" : "green");
        lockBar.makeVisible();
    
        updateSymbolAppearance();
    }

    /**
     * Hides all graphical elements of this wheel.
     *
     * Hiding the wheel does not modify the logical
     * state of its currently selected symbol.
     */
    public void makeInvisible()
    {
        visible = false;
    
        circle.makeInvisible();
        symbolMarker.makeInvisible();
    
        lockBar.makeInvisible();
        slotFrame.makeInvisible();
    }
    
    /**
     * Returns the color used to identify this wheel type.
     *
     * Normal wheels use a white frame.
     * Specialized wheels may override this operation.
     *
     * @return frame color
     */
    protected String frameColor()
    {
        return "white";
    }
    
    /**
     * Keeps the symbol circle centered when its diameter changes.
     *
     * The original 70-pixel circle position is used as reference.
     *
     * @param size new visual diameter of the symbol
     */
    private void centerSymbol(int size)
    {
        int newOffset = (70 - size) / 2;
        int movement = newOffset - circleOffset;
    
        circle.moveHorizontal(movement);
        circle.moveVertical(movement);
    
        circleOffset = newOffset;
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
    
    /**
     * Creates a symbol according to the requested type.
     *
     * Available types are normal, ephemeral, shy and pulsing.
     *
     * @param type symbol type
     * @param color symbol color
     * @return created symbol, or null if the type is unavailable
     */
    private Symbol createSymbol(String type, String color)
    {
        if(type == null){
            return null;
        }
    
        if(type.equals("normal")){
            return new Symbol(color);
        }
    
        if(type.equals("ephemeral")){
            return new EphemeralSymbol(color);
        }
    
        if(type.equals("shy")){
            return new ShySymbol(color);
        }
    
        if(type.equals("pulsing")){
            return new PulsingSymbol(color);
        }
    
        return null;
    }
    
    /**
     * Synchronizes the visual representation of the current symbol.
     *
     * The circle represents the symbol's color, size and visibility.
     * A separate marker identifies specialized symbol types.
     */
    private void updateSymbolAppearance()
    {
        if(!visible){
            return;
        }
    
        if(symbols.length == 0){
            centerSymbol(70);
            circle.changeSize(70);
            circle.changeColor(locked ? "gray" : "white");
            circle.makeVisible();
            symbolMarker.makeInvisible();
            return;
        }
    
        Symbol current = symbols[currentSymbol];
    
        int size = current.getSize();
    
        centerSymbol(size);
        circle.changeSize(size);
        circle.changeColor(locked ? "gray" : current.getColor());
    
        if(current.isVisible()){
            circle.makeVisible();
        }
        else{
            circle.makeInvisible();
        }
    
        if(current.hasMarker()){
            symbolMarker.changeColor(current.markerColor());
            symbolMarker.makeVisible();
        }
        else{
            symbolMarker.makeInvisible();
        }
    }
    
    /**
     * Selects the symbol at the indicated position.
     *
     * Every selected symbol receives the selection event.
     *
     * @param position index of the symbol to select
     */
    private void selectSymbol(int position)
    {
        currentSymbol = position;
        symbols[currentSymbol].select();
    }
    
    /**
     * Selects a symbol as a consequence of a wheel spin.
     *
     * The symbol receives both the selection event and the spin event.
     *
     * @param position index of the symbol selected by the spin
     */
    private void selectBySpin(int position)
    {
        selectSymbol(position);
        symbols[currentSymbol].spin();
    }
}
