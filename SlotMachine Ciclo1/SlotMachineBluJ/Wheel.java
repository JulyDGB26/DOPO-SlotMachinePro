/**
 * Represents a wheel in the slot machine.
 *
 * @author Julian Gomez - Julian Carranza
 * @version Minicicle2
 */
public class Wheel
{
    private String[] symbols;
    private int currentSymbol;
    private static int wheelCount = 0;
    private Circle circle;

    /**
     * Creates an empty wheel.
     */
    public Wheel()
    {
        symbols = new String[0];
        currentSymbol = 0;
        
        circle = new Circle();
        circle.changeColor("white");
        
        // Por cada rueda creada se mueve 20px a la izquierda (- 20 * wheelCount)
        int initialShiftX = 115 - (40 * wheelCount);
        
        circle.moveHorizontal(initialShiftX);
        circle.moveVertical(120);
        circle.makeVisible();
        
        wheelCount++;
    }
    
    /**
     * Adds a new symbol to the wheel.
     *
     * @param color symbol color
     * @return true if the symbol was successfully added
     */
    public boolean addSymbol(String color)
    {
        if(contains(color)){
            return false;
        }
    
        String[] newSymbols = new String[symbols.length + 1];
    
        for(int i = 0; i < symbols.length; i++){
            newSymbols[i] = symbols[i];
        }
    
        newSymbols[symbols.length] = color;
    
        symbols = newSymbols;
    
        return true;
    }
    
    /**
     * Deletes a symbol.
     *
     * @param symbol symbol color
     * @return true if deleted
     */
    public boolean delSymbol(String symbol)
    {
        int position = find(symbol);

        if(position == -1){
            return false;
        }

        String[] newSymbols = new String[symbols.length - 1];

        for(int i = 0; i < position; i++){
            newSymbols[i] = symbols[i];
        }

        for(int i = position; i < newSymbols.length; i++){
            newSymbols[i] = symbols[i + 1];
        }

        symbols = newSymbols;

        if(currentSymbol >= symbols.length){
            currentSymbol = 0;
        }

        return true;
    }

    /**
     * Places a symbol as the visible symbol.
     *
     * @param symbol symbol color
     * @return true if symbol exists
     */
    public boolean placeSymbol(String symbol)
    {
        int position = find(symbol);

        if(position == -1){
            return false;
        }

        currentSymbol = position;

        return true;
    }
    
    /**
     * Rotates the wheel one position.
     *
     * @return true if the wheel was rotated
     */
    public boolean spin()
    {
        if(symbols.length == 0){
            return false;
        }


        currentSymbol++;


        if(currentSymbol >= symbols.length){
            currentSymbol = 0;
        }


        return true;
    }
    
    /**
     * Returns the currently visible symbol.
     *
     * @return visible symbol
     */
    public String configuration()
    {
        if(symbols.length == 0){
            return null;
        }

        return symbols[currentSymbol];
    }

    /**
     * Returns symbols of the wheel.
     *
     * @return symbols array
     */
    public String[] symbols()
    {
        return symbols;
    }
    
    /**
     * Makes the wheel visible on canvas.
     */
    public void makeVisible()
    {
        circle.makeVisible();
    }

    /**
     * Makes the wheel invisible on canvas.
     */
    public void makeInvisible()
    {
        circle.makeInvisible();
    }

    private boolean contains(String color)
    {
        return find(color) != -1;
    }

    private int find(String symbol)
    {
        for(int i = 0; i < symbols.length; i++)
        {
            if(symbols[i].equals(symbol))
            {
                return i;
            }
        }

        return -1;
    }
}