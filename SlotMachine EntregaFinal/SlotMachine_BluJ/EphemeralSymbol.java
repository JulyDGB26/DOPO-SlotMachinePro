/**
 * Represents an ephemeral symbol in a slot machine wheel.
 *
 * An ephemeral symbol reduces its visual size every time it becomes
 * selected as a consequence of a wheel spin. Its size never becomes
 * smaller than the minimum size used to represent a point.
 *
 * @author Julian Gomez Boada
 * @version Ciclo4-MC3
 */
public class EphemeralSymbol extends Symbol
{
    private int size;

    private static final int INITIAL_SIZE = 70;
    private static final int SIZE_STEP = 10;
    private static final int POINT_SIZE = 2;

    /**
     * Creates an ephemeral symbol with the specified color.
     *
     * @param color visual color associated with the symbol
     */
    public EphemeralSymbol(String color)
    {
        super(color);
        size = INITIAL_SIZE;
    }

    /**
     * Reduces the size of this symbol after a spin.
     *
     * Once the minimum point size is reached, subsequent spins
     * preserve that minimum size.
     */
    @Override
    public void spin()
    {
        size = size - SIZE_STEP;

        if(size < POINT_SIZE)
        {
            size = POINT_SIZE;
        }
    }

    /**
     * Returns the current visual size of this ephemeral symbol.
     *
     * @return current symbol diameter
     */
    @Override
    public int getSize()
    {
        return size;
    }
    
    /**
     * Indicates that an ephemeral symbol requires
     * a visual identification marker.
     *
     * @return true
     */
    @Override
    public boolean hasMarker()
    {
        return true;
    }
    
    /**
     * Returns the identification color of ephemeral symbols.
     *
     * @return magenta marker color
     */
    @Override
    public String markerColor()
    {
        return "magenta";
    }
}