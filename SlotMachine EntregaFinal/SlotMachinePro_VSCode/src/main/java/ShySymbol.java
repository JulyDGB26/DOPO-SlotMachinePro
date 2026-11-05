/**
 * Represents a shy symbol in a slot machine wheel.
 *
 * A shy symbol alternates its visual state between visible and
 * invisible every time it is selected in the wheel.
 *
 * Its logical identity and color are preserved even when its
 * visual representation is hidden.
 *
 * @author Julian Gomez Boada
 * @version Ciclo4-MC3
 */
public class ShySymbol extends Symbol
{
    private boolean symbolVisible;

    /**
     * Creates a shy symbol with the specified color.
     * A shy symbol starts visible.
     *
     * @param color visual color associated with the symbol
     */
    public ShySymbol(String color)
    {
        super(color);
        symbolVisible = true;
    }

    /**
     * Alternates the visual state of this symbol whenever
     * it is selected.
     */
    @Override
    public void select()
    {
        symbolVisible = !symbolVisible;
    }

    /**
     * Indicates whether this shy symbol should currently
     * be represented as visible.
     *
     * @return current visual state
     */
    @Override
    public boolean isVisible()
    {
        return symbolVisible;
    }
    
    /**
     * Indicates that a shy symbol requires
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
     * Returns the identification color of shy symbols.
     *
     * @return blue marker color
     */
    @Override
    public String markerColor()
    {
        return "blue";
    }
}