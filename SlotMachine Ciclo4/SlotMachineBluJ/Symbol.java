/**
 * Represents a symbol used in a slot machine wheel.
 *
 * A normal symbol keeps its color, size and visibility unchanged
 * when it is selected or when its wheel spins.
 * Specialized symbols may override those reactions.
 *
 * @author Julian Gomez Boada
 * @version Ciclo4-MC3
 */
public class Symbol
{
    private String color;

    /**
     * Default visual size of a symbol.
     */
    private static final int NORMAL_SIZE = 70;

    /**
     * Creates a normal symbol with the specified color.
     *
     * @param color visual color associated with the symbol
     */
    public Symbol(String color)
    {
        this.color = color;
    }

    /**
     * Returns the color of this symbol.
     *
     * @return symbol color
     */
    public String getColor()
    {
        return color;
    }

    /**
     * Reacts when this symbol is selected in its wheel.
     *
     * A normal symbol requires no special behavior.
     * Specialized symbols may override this operation.
     */
    public void select()
    {
        // Normal symbols do not react to selection.
    }

    /**
     * Reacts when this symbol becomes selected as a consequence
     * of a wheel spin.
     *
     * A normal symbol requires no special behavior.
     * Specialized symbols may override this operation.
     */
    public void spin()
    {
        // Normal symbols do not react to a spin.
    }

    /**
     * Returns the size that must be used to represent this symbol.
     *
     * @return visual diameter of the symbol
     */
    public int getSize()
    {
        return NORMAL_SIZE;
    }

    /**
     * Indicates whether this symbol should currently be represented
     * as visible.
     *
     * @return true for a normal symbol
     */
    public boolean isVisible()
    {
        return true;
    }
    
    /**
     * Indicates whether this symbol requires a visual marker.
     *
     * Normal symbols do not require an additional marker.
     * Specialized symbols may override this operation.
     *
     * @return false for a normal symbol
     */
    public boolean hasMarker()
    {
        return false;
    }
    
    /**
     * Returns the color of the visual marker associated
     * with this symbol.
     *
     * Normal symbols do not have a marker.
     *
     * @return marker color, or null when no marker is required
     */
    public String markerColor()
    {
        return null;
    }

    /**
     * Compares this symbol with another object.
     * Two symbols are considered equal when they have the same color.
     *
     * @param obj object to compare with this symbol
     * @return true if both objects represent the same symbol
     */
    @Override
    public boolean equals(Object obj)
    {
        if(this == obj){
            return true;
        }

        if(!(obj instanceof Symbol)){
            return false;
        }

        Symbol other = (Symbol)obj;

        return color.equals(other.color);
    }

    /**
     * Returns the hash code of this symbol.
     *
     * @return hash code associated with this symbol
     */
    @Override
    public int hashCode()
    {
        return color.hashCode();
    }

    /**
     * Returns a textual representation of this symbol.
     *
     * @return symbol color
     */
    @Override
    public String toString()
    {
        return color;
    }
}