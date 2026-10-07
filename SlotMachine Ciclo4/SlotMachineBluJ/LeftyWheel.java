/**
 * Represents a lefty wheel in the slot machine.
 *
 * A lefty wheel behaves like a normal wheel when it has no wheel
 * immediately to its left. When a left wheel exists, spinning this
 * wheel copies the visible state of that neighbor, provided that
 * the same symbol belongs to this wheel.
 *
 * @author Julian Gomez Boada
 * @version Ciclo4-MC2
 */
public class LeftyWheel extends Wheel
{
    /**
     * Creates a lefty wheel at the given 0-based slot index.
     *
     * @param slotIndex 0-based position of this wheel in the machine
     */
    public LeftyWheel(int slotIndex)
    {
        super(slotIndex);
    }

    /**
     * Spins this lefty wheel considering its immediate left neighbor.
     *
     * If no left wheel exists, the inherited normal spin is performed.
     * If a left wheel exists, this wheel copies its currently selected
     * symbol as a consequence of a spin.
     *
     * @param leftWheel wheel immediately to the left, or null if none exists
     * @return true if the operation was successfully performed
     */
    @Override
    public boolean spin(Wheel leftWheel)
    {
        if(leftWheel == null){
            return super.spin();
        }
    
        if(isLocked()){
            return false;
        }
    
        String leftSymbol = leftWheel.configuration();
    
        if(leftSymbol == null){
            return false;
        }
    
        return placeSymbolBySpin(leftSymbol);
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
}