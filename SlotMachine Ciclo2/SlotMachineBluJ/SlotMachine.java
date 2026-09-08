/**
 * Simulates a slot machine simulator.
 *
 * @author Julian Gomez - Julian Carrero
 * 
 */
public class SlotMachine
{
    private Wheel[] wheels;
    private boolean ok;
    
    private Rectangle housing;
    private Rectangle topBar;
    private Rectangle displayBg;
    private Rectangle bottomBar;
    private boolean   visible;

    /**
     * Creates an empty slot machine.
     */
    public SlotMachine()
    {
        wheels  = new Wheel[0];
        ok      = true;
        visible = false;

        housing = new Rectangle();
        housing.changeColor("black");
        housing.changeSize(320, 480);
        housing.moveHorizontal(10 - 70);   
        housing.moveVertical(10 - 15);     

        
        topBar = new Rectangle();
        topBar.changeColor("red");
        topBar.changeSize(52, 480);
        topBar.moveHorizontal(-60);
        topBar.moveVertical(-5);

        
        displayBg = new Rectangle();
        displayBg.changeColor("black");
        displayBg.changeSize(186, 430);
        displayBg.moveHorizontal(35 - 70);  
        displayBg.moveVertical(62 - 15);    

    
        bottomBar = new Rectangle();
        bottomBar.changeColor("red");
        bottomBar.changeSize(42, 480);
        bottomBar.moveHorizontal(-60);
        bottomBar.moveVertical(288 - 15);   
    }

    /**
     * Adds a new wheel at the indicated position.
     *
     * @param pos position where the wheel will be added
     */
    public void addWheel(int pos)
    {
        int position = addPosition(pos);
        Wheel[] newWheels = new Wheel[wheels.length + 1];

        for(int i = 0; i < position - 1; i++) {
            newWheels[i] = wheels[i];
        }

        newWheels[position - 1] = new Wheel(position - 1);

        for(int i = position; i < newWheels.length; i++) {
            newWheels[i] = wheels[i - 1];
        }

        wheels = newWheels;
        ok = true;
    }

    /**
     * Deletes the wheel at the indicated position.
     *
     * @param pos position of the wheel to delete
     */
    public void delWheel(int pos)
    {
        if(wheels.length == 0) {
            ok = false;
            return;
        }

        int position = delPosition(pos);
        Wheel[] newWheels = new Wheel[wheels.length - 1];

        for(int i = 0; i < position - 1; i++) {
            newWheels[i] = wheels[i];
        }

        for(int i = position - 1; i < newWheels.length; i++) {
            newWheels[i] = wheels[i + 1];
        }

        wheels = newWheels;
        ok = true;
    }
    
    /**
     * Swaps the positions of two wheels.
     *
     * @param wheel1 position of the first wheel
     * @param wheel2 position of the second wheel
     */
    public void swap(int wheel1, int wheel2)
    {
        if(wheels.length < 2){
            ok = false;
            return;
        }

        int p1 = delPosition(wheel1);
        int p2 = delPosition(wheel2);

        Wheel temp = wheels[p1 - 1];
        wheels[p1 - 1] = wheels[p2 - 1];
        wheels[p2 - 1] = temp;

        ok = true;
    }
    
    /**
     * Locks a wheel so it cannot be rotated.
     *
     * @param wheel wheel position
     */
    public void lock(int wheel)
    {
        if(wheel < 1 || wheel > wheels.length){
            ok = false;
            return;
        }

        wheels[wheel - 1].lock();
        ok = true;
    }
    
    /**
     * Unlocks a wheel so it can be rotated again.
     *
     * @param wheel wheel position
     */
    public void unlock(int wheel)
    {
        if(wheel < 1 || wheel > wheels.length){
            ok = false;
            return;
        }

        wheels[wheel - 1].unlock();
        ok = true;
    }
    
    /**
     * Adds a symbol to a specific wheel.
     *
     * @param pos wheel position
     * @param color symbol color
     */
    public void addSymbol(int pos, String color)
    {
        if(!(pos >= 1 && pos <= wheels.length)){
            ok = false;
            return;
        }
    
        ok = wheels[pos - 1].addSymbol(color);
    }
    
    /**
     * Deletes a symbol from the machine.
     *
     * @param symbol symbol color
     */
    public void delSymbol(String symbol)
    {
        boolean deleted = false;

        for(int i = 0; i < wheels.length; i++){

            if(wheels[i].delSymbol(symbol)){
                deleted = true;
                break;
            }
        }

        ok = deleted;
    }
    
    /**
     * Places a symbol in a specific wheel.
     *
     * @param wheel wheel position
     * @param symbol symbol color
     */
    public void placeSymbol(int wheel, String symbol)
    {
        if(!(wheel >= 1 && wheel <= wheels.length)){
            ok = false;
            return;
        }

        ok = wheels[wheel - 1].placeSymbol(symbol);
    }
    
    /**
     * Rotates one wheel.
     *
     * @param wheel wheel position
     */
    public void spin(int wheel)
    {
        if(!(wheel >= 1 && wheel <= wheels.length)){
            ok = false;
            return;
        }

        ok = wheels[wheel - 1].spin();
    }
    
    /**
     * Rotates a wheel a given number of steps, visualizing each step.
     *
     * @param wheel wheel position
     * @param steps number of steps to rotate
     */
    public void spin(int wheel, int steps)
    {
        if(wheel < 1 || wheel > wheels.length){
            ok = false;
            return;
        }

        if(steps <= 0){
            ok = false;
            return;
        }

        boolean result = false;

        for(int i = 0; i < steps; i++){
            if(wheels[wheel - 1].spin()) result = true;
            if(visible) Canvas.getCanvas().wait(150);
        }

        ok = result;
    }

    /**
     * Sets the visible symbol of each wheel to the corresponding color.
     * Applies to the first min(setSymbols.length, wheels.length) wheels.
     *
     * @param setSymbols array of symbol colors to display
     */
    public void spin(String[] setSymbols)
    {
        if(setSymbols == null || wheels.length == 0){
            ok = true;
            return;
        }

        int limit = Math.min(setSymbols.length, wheels.length);
        boolean result = true;

        for(int i = 0; i < limit; i++){
            if(!wheels[i].placeSymbol(setSymbols[i])) result = false;
        }

        ok = result;
    }

    /**
     * Rotates all wheels. Locked wheels are skipped.
     * ok is true if at least one wheel rotated successfully.
     */
    public void spin()
    {
        if(wheels.length == 0){
            ok = false;
            return;
        }

        boolean result = false;

        for(int i = 0; i < wheels.length; i++){
            if(wheels[i].spin()){
                result = true;
            }
        }

        ok = result;
    }

    /**
     * Returns current visible configuration.
     *
     * @return visible symbols
     */
    public String[] configuration()
    {
        String[] result = new String[wheels.length];

        for(int i = 0; i < wheels.length; i++){

            result[i] = wheels[i].configuration();

        }

        return result;
    }
    
    /**
     * Returns the amount of different visible symbols.
     *
     * @return number of different symbols
     */
    public int distinctSymbols()
    {
        String[] current = configuration();
        String[] different = new String[current.length];

        int count = 0;

        for(int i = 0; i < current.length; i++)
        {
            if(current[i] == null)
            {
                continue;
            }

            boolean exists = false;

            for(int j = 0; j < count; j++)
            {
                if(different[j].equals(current[i]))
                {
                    exists = true;
                    break;
                }
            }

            if(!exists)
            {
                different[count] = current[i];
                count++;
            }
        }

        return count;
    }

    /**
     * Checks if the current configuration is a jackpot.
     *
     * @return true if all wheels show the same symbol
     */
    public boolean isJackpot()
    {
        String[] current = configuration();

        if(current.length == 0)
        {
            return false;
        }

        if(current[0] == null)
        {
            return false;
        }

        for(int i = 1; i < current.length; i++)
        {
            if(current[i] == null)
            {
                return false;
            }


            if(!current[0].equals(current[i]))
            {
                return false;
            }
        }

        return true;
    }
     
    /**
     * Returns all symbols of the slot machine.
     *
     * The symbols are returned wheel by wheel,
     * starting from wheel 1.
     *
     * @return all symbols in the machine
     */
    public String[] symbols()
    {
        int total = 0;
    
        for(int i = 0; i < wheels.length; i++)
        {
            total += wheels[i].symbols().length;
        }
    
    
        String[] result = new String[total];
    
        int index = 0;
    
    
        for(int i = 0; i < wheels.length; i++)
        {
            String[] wheelSymbols = wheels[i].symbols();
    
            for(int j = 0; j < wheelSymbols.length; j++)
            {
                result[index] = wheelSymbols[j];
                index++;
            }
        }
    
    
        return result;
    }

    /**
     * Indicates whether the last operation was successfully completed.
     *
     * @return true if the last operation was successful
     */
    public boolean ok()
    {
        return ok;
    }
    
    /**
     * Returns the total number of wheels in the slot machine.
     *
     * @return the number of wheels currently installed
     */
    public int wheels()
    {
        return wheels.length;
    }
    
    /**
     * Makes the slot machine (housing) and all its wheels visible on canvas.
     */
    public void makeVisible()
    {
        visible = true;
        housing.makeVisible();
        topBar.makeVisible();
        displayBg.makeVisible();
        bottomBar.makeVisible();

        for(int i = 0; i < wheels.length; i++) {
            wheels[i].makeVisible();
        }
    }

    /**
     * Makes the slot machine (housing) and all its wheels invisible on canvas.
     */
    public void makeInvisible()
    {
        visible = false;

        for(int i = 0; i < wheels.length; i++) {
            wheels[i].makeInvisible();
        }

        bottomBar.makeInvisible();
        displayBg.makeInvisible();
        topBar.makeInvisible();
        housing.makeInvisible();
    }
    
    public void exit()
    {
        System.exit(0);
    }

    /**
     * Determines a valid position for adding a wheel.
     */
    private int addPosition(int pos)
    {
        if(pos < 1) {
            return 1;
        }

        if(pos > wheels.length + 1) {
            return wheels.length + 1;
        }

        return pos;
    }

    /**
     * Determines a valid position for deleting a wheel.
     */
    private int delPosition(int pos)
    {
        if(pos < 1) {
            return 1;
        }

        if(pos > wheels.length) {
            return wheels.length;
        }

        return pos;
    }
    
}