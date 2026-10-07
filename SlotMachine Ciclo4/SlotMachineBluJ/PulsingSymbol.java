/**
 * Represents a pulsing symbol in a slot machine wheel.
 *
 * A pulsing symbol alternates between an expanded and a
 * contracted visual size every time it is selected.
 *
 * The symbol preserves its color and logical identity
 * regardless of its current visual size.
 *
 * @author Julian Gomez Boada
 * @version Ciclo4-MC4
 */
public class PulsingSymbol extends Symbol
{
    private boolean expanded;

    private static final int EXPANDED_SIZE = 70;
    private static final int CONTRACTED_SIZE = 45;

    /**
     * Creates a pulsing symbol with the specified color.
     *
     * The symbol starts in its expanded state.
     *
     * @param color visual color of the symbol
     */
    public PulsingSymbol(String color)
    {
        super(color);
        expanded = true;
    }

    /**
     * Alternates the size state of this symbol whenever
     * it is selected in its wheel.
     */
    @Override
    public void select()
    {
        expanded = !expanded;
    }

    /**
     * Returns the visual diameter corresponding to the
     * current pulsing state.
     *
     * @return expanded or contracted symbol diameter
     */
    @Override
    public int getSize()
    {
        if(expanded){
            return EXPANDED_SIZE;
        }

        return CONTRACTED_SIZE;
    }

    /**
     * Indicates that a pulsing symbol has a visual marker.
     *
     * @return true
     */
    @Override
    public boolean hasMarker()
    {
        return true;
    }

    /**
     * Returns the color used to identify pulsing symbols.
     *
     * @return green marker color
     */
    @Override
    public String markerColor()
    {
        return "green";
    }
}