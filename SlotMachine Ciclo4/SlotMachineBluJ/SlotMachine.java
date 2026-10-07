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
        ok = true;
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
     * Creates a slot machine with n wheels and n symbols per wheel.
     * Each wheel receives the same symbols in the same order.
     *
     * @param n number of wheels and symbols
     */
    public SlotMachine(int n)
    {
        wheels = new Wheel[0];
    
        housing = new Rectangle();
        topBar = new Rectangle();
        displayBg = new Rectangle();
        bottomBar = new Rectangle();
    
        ok = true;
        visible = false;
    
        if(n <= 0){
            ok = false;
            return;
        }
    
        String[] symbols = generateSymbols(n);
    
        for(int i = 1; i <= n; i++){
            addWheel(i);
    
            for(int j = 0; j < n; j++){
                addSymbol(i, symbols[j]);
            }
        }
    }

    /**
     * Adds a normal wheel at the indicated position.
     *
     * This operation preserves the original behavior
     * of the slot machine from previous cycles.
     *
     * @param pos position where the wheel will be added
     */
    public void addWheel(int pos)
    {
        addWheel("normal", pos);
    }
    
    /**
     * Adds a wheel of the indicated type at the requested position.
     *
     * Available wheel types are normal, lefty and rebel.
     *
     * @param type wheel type
     * @param pos position where the wheel will be added
     */
    public void addWheel(String type, int pos)
    {
        int position = addPosition(pos);
        Wheel newWheel = createWheel(type, position - 1);
    
        if(newWheel == null){
            ok = false;
            return;
        }
    
        Wheel[] newWheels = new Wheel[wheels.length + 1];
    
        for(int i = 0; i < position - 1; i++){
            newWheels[i] = wheels[i];
        }
    
        newWheels[position - 1] = newWheel;
    
        for(int i = position; i < newWheels.length; i++){
            newWheels[i] = wheels[i - 1];
        }
    
        wheels = newWheels;
        ok = true;
    }

    /**
     * Deletes the wheel at the indicated position.
     *
     * A wheel that does not allow deletion keeps the machine unchanged.
     *
     * @param pos position of the wheel to delete
     */
    public void delWheel(int pos)
    {
        if(wheels.length == 0){
            ok = false;
            return;
        }
    
        int position = delPosition(pos);
    
        if(!wheels[position - 1].canBeDeleted()){
            ok = false;
            return;
        }
    
        Wheel[] newWheels = new Wheel[wheels.length - 1];
    
        for(int i = 0; i < position - 1; i++){
            newWheels[i] = wheels[i];
        }
    
        for(int i = position - 1; i < newWheels.length; i++){
            newWheels[i] = wheels[i + 1];
        }
    
        wheels = newWheels;
        ok = true;
    }
    
    /**
     * Swaps the positions of two wheels.
     *
     * The operation is rejected if either wheel does not allow swapping.
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
    
        if(!wheels[p1 - 1].canBeSwapped() || !wheels[p2 - 1].canBeSwapped()){
            ok = false;
            return;
        }
    
        Wheel temp = wheels[p1 - 1];
        wheels[p1 - 1] = wheels[p2 - 1];
        wheels[p2 - 1] = temp;
    
        ok = true;
    }
    
    /**
     * Locks a wheel so it cannot be rotated.
     *
     * The operation fails when the selected wheel does not allow locking.
     *
     * @param wheel wheel position
     */
    public void lock(int wheel)
    {
        if(wheel < 1 || wheel > wheels.length){
            ok = false;
            return;
        }
    
        if(!wheels[wheel - 1].canBeLocked()){
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
     * Adds a normal symbol to a specific wheel.
     *
     * This operation preserves the original behavior
     * of the slot machine from previous cycles.
     *
     * @param pos wheel position
     * @param color symbol color
     */
    public void addSymbol(int pos, String color)
    {
        addSymbol("normal", color, pos);
    }
    
    /**
     * Adds a symbol of the indicated type to a specific wheel.
     *
     * @param type symbol type
     * @param color symbol color
     * @param pos wheel position
     */
    public void addSymbol(String type, String color, int pos)
    {
        if(!(pos >= 1 && pos <= wheels.length)){
            ok = false;
            return;
        }
    
        ok = wheels[pos - 1].addSymbol(type, color);
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
     * The wheel receives its immediate left neighbor as context.
     * The first wheel receives null because no left neighbor exists.
     *
     * @param wheel wheel position
     */
    public void spin(int wheel)
    {
        if(!(wheel >= 1 && wheel <= wheels.length)){
            ok = false;
            return;
        }
    
        Wheel leftWheel = null;
    
        if(wheel > 1){
            leftWheel = wheels[wheel - 2];
        }
    
        ok = wheels[wheel - 1].spin(leftWheel);
    }
    
    /**
     * Rotates a wheel a given number of steps.
     *
     * Each step is executed using the contextual behavior of the wheel.
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
    
        Wheel leftWheel = null;
    
        if(wheel > 1){
            leftWheel = wheels[wheel - 2];
        }
    
        boolean result = false;
    
        for(int i = 0; i < steps; i++){
            
            if(wheels[wheel - 1].spin(leftWheel)){
                result = true;
            }
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
     * Rotates all wheels from left to right.
     *
     * Each wheel receives the state of its immediate left neighbor
     * as contextual information when applicable.
     * ok is true if at least one wheel performs its operation.
     */
    public void spin()
    {
        if(wheels.length == 0){
            ok = false;
            return;
        }
    
        boolean result = false;
    
        for(int i = 0; i < wheels.length; i++){
            Wheel leftWheel = null;
    
            if(i > 0){
                leftWheel = wheels[i - 1];
            }
    
            if(wheels[i].spin(leftWheel)){
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

    
    // ── private helpers ───────────────────────────────────────────────────────

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

    /**
     * Generates the initial symbols of the machine.
     *
     * @param n number of symbols required
     * @return generated symbols
     */
    private String[] generateSymbols(int n)
    {
        String[] colors = {"red","blue","green","yellow","orange","purple","pink",
            "white","black","brown"};
    
        String[] result = new String[n];
    
        for(int i = 0; i < n; i++)
        {
            if(i < colors.length){
                result[i] = colors[i];
            }
            else{
                result[i] = "color" + i;
            }
        }
    
        return result;
    }
    
    /**
     * Creates a wheel according to the requested type.
     *
     * @param type wheel type
     * @param slotIndex 0-based visual position
     * @return the created wheel, or null if the type is unavailable
     */
    private Wheel createWheel(String type, int slotIndex)
    {
        if(type == null){
            return null;
        }
    
        if(type.equals("normal")){
            return new Wheel(slotIndex);
        }
    
        if(type.equals("lefty")){
            return new LeftyWheel(slotIndex);
        }
    
        if(type.equals("rebel")){
            return new RebelWheel(slotIndex);
        }
    
        return null;
    }
}