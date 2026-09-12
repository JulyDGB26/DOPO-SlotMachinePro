/**
 * Represents a symbol used in a slot machine wheel.
 * A symbol stores its own visual representation through a color value.
 *
 * @author Julian Gomez Boada - Julian Carranza
 * @version Refactoring Symbol - 12/09/2026
 */
public class Symbol
{
    private String color;

    /**
     * Creates a new symbol with the specified color.
     *
     * @param color visual color associated with the symbol
     */
    public Symbol(String color)
    {
        this.color = color;
    }

    /**
     * Returns the color representation of this symbol.
     *
     * @return symbol color
     */
    public String getColor()
    {
        return color;
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
     * The value is generated using the symbol color because
     * equality depends on this attribute.
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