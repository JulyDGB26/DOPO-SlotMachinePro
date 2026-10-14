import javax.swing.JOptionPane;

/**
 * Interactive entry point for the SlotMachinePro simulator.
 *
 * Uses JOptionPane submenus to play, manage wheels and symbols,
 * configure the machine and consult its current state. It delegates
 * all slot-machine rules to SlotMachine and its specialized objects.
 *
 * The visual demonstration contains at most three simultaneous wheels
 * because the existing Canvas has a fixed width. This restriction is
 * exclusive to Main; the SlotMachine model has no such limit.
 * Wheel graphics are synchronized after insertions, deletions and swaps
 * without recreating wheels or resetting specialized symbol states.
 *
 * The demonstration starts with three normal wheels containing a
 * common red symbol. Their different symbol counts allow a jackpot
 * after three consecutive global spins, without initially winning.
 * The existing JOptionPane jackpot announcement is preserved; no
 * additional celebration graphics are introduced in this version.
 *
 * Supported wheel types: normal, lefty and rebel.
 * Supported symbol types: normal, ephemeral, shy and pulsing.
 * 
 * Dear Teacher Angie, i hope that my work, which required so much effort and time, meets with your approval.
 *
 * @author Julian Gomez Boada
 * @version Ciclo4 - Revision Final
 */
public class SlotMachineProGUI
{
    /**
     * Creates the three-wheel demonstration and starts the main menu.
     * The red symbol is reachable simultaneously through ordinary spins.
     * The preexisting machine exit operation ends the program.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args)
    {
        SlotMachine machine = new SlotMachine();

        // Three normal wheels fit the existing Canvas housing.
        // Different symbol counts allow their red symbols to align
        // after three global spins (starting state: red/yellow/blue).
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(3);

        machine.addSymbol(1, "red");
        machine.addSymbol(1, "blue");
        machine.addSymbol(1, "green");

        machine.addSymbol(2, "yellow");
        machine.addSymbol(2, "red");

        machine.addSymbol(3, "blue");
        machine.addSymbol(3, "green");
        machine.addSymbol(3, "yellow");
        machine.addSymbol(3, "red");

        machine.makeVisible();

        String[] options = {
            "Jugar",
            "Administrar ruedas",
            "Administrar simbolos",
            "Consultar y configurar",
            "Salir"
        };

        boolean running = true;

        while(running)
        {
            int choice = JOptionPane.showOptionDialog(
                null,
                "Estado actual: " + buildStatus(machine.configuration())
                    + "\nJackpot: " + (machine.isJackpot() ? "SI" : "NO")
                    + "\n\nSelecciona una categoria:",
                "Slot Machine Pro - Menu principal",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
            );

            switch(choice)
            {
                case 0:
                    showPlayMenu(machine);
                    break;

                case 1:
                    showWheelMenu(machine);
                    break;

                case 2:
                    showSymbolMenu(machine);
                    break;

                case 3:
                    showConfigurationMenu(machine);
                    break;

                default:
                    // Closing the dialog or selecting "Salir" ends the game.
                    running = false;
                    break;
            }
        }

        // Preserve the exit behavior of the original executable.
        machine.exit();
    }

    /**
     * Shows the existing spinning operations in the play submenu.
     *
     * @param machine machine controlled by the player
     */
    private static void showPlayMenu(SlotMachine machine)
    {
        String[] options = {
            "Girar todas",
            "Girar una rueda",
            "Volver"
        };

        boolean inMenu = true;

        while(inMenu)
        {
            int choice = JOptionPane.showOptionDialog(
                null,
                "Configuracion: " + buildStatus(machine.configuration())
                    + "\n\nSelecciona una accion:",
                "Slot Machine Pro - Jugar",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
            );

            switch(choice)
            {
                case 0:
                    boolean wasJackpot = machine.isJackpot();
                    machine.spin();
                    if(!machine.ok()){
                        showError("No fue posible girar las ruedas.");
                    }
                    else{
                        showJackpotIfNew(machine, wasJackpot);
                    }
                    break;

                case 1:
                    spinWheel(machine);
                    break;

                default:
                    inMenu = false;
                    break;
            }
        }
    }

    /**
     * Provides wheel creation, deletion, locking, unlocking and swapping.
     * The menu limits the visual demonstration to three wheels,
     * without imposing that restriction on the machine model.
     *
     * @param machine machine controlled by the player
     */
    private static void showWheelMenu(SlotMachine machine)
    {
        String[] options = {
            "Agregar rueda",
            "Eliminar rueda",
            "Bloquear",
            "Desbloquear",
            "Intercambiar",
            "Volver"
        };

        boolean inMenu = true;

        while(inMenu)
        {
            int choice = JOptionPane.showOptionDialog(
                null,
                "Ruedas actuales: " + machine.wheels()
                    + "\nConfiguracion: " + buildStatus(machine.configuration())
                    + "\n\nSelecciona una accion:",
                "Slot Machine Pro - Administrar ruedas",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
            );

            switch(choice)
            {
                case 0:
                    addWheel(machine);
                    break;

                case 1:
                    deleteWheel(machine);
                    break;

                case 2:
                    lockWheel(machine);
                    break;

                case 3:
                    unlockWheel(machine);
                    break;

                case 4:
                    swapWheels(machine);
                    break;

                default:
                    inMenu = false;
                    break;
            }
        }
    }

    /**
     * Adds a selected wheel type at a valid position. The three-wheel
     * restriction applies only to this fixed-width demonstration.
     *
     * The hide/synchronize/show cycle removes stale graphics and
     * moves all surviving wheels to their correct visual slots.
     *
     * @param machine machine controlled by the player
     */
    private static void addWheel(SlotMachine machine)
    {
        final int maxVisualWheels = 3;
        int currentWheels = machine.wheels();

        if(currentWheels >= maxVisualWheels){
            JOptionPane.showMessageDialog(
                null,
                "Limite de representacion grafica: esta demostracion "
                    + "permite un maximo de 3 ruedas al mismo tiempo."
                    + "\nElimina una rueda antes de agregar otra.",
                "Slot Machine Pro - Limite visual",
                JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        String[] types = {"Normal", "Lefty", "Rebel", "Cancelar"};
        int selection = JOptionPane.showOptionDialog(
            null,
            "Selecciona el tipo de rueda que deseas agregar:",
            "Slot Machine Pro - Agregar rueda",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            types,
            types[0]
        );

        if(selection < 0 || selection >= 3){
            return;
        }

        String type;
        switch(selection)
        {
            case 0:
                type = "normal";
                break;
            case 1:
                type = "lefty";
                break;
            default:
                type = "rebel";
                break;
        }

        String positionInput = JOptionPane.showInputDialog(
            "Posicion de la nueva rueda (1-" + (currentWheels + 1) + "):"
                + "\nLas ruedas siguientes se desplazan una posicion."
        );
        if(positionInput == null){
            return;
        }

        int position;
        try{
            position = Integer.parseInt(positionInput.trim());
        }
        catch(NumberFormatException e){
            showError("Ingresa un numero entero valido para la posicion.");
            return;
        }

        // SlotMachine normalizes out-of-range positions. The interactive
        // menu validates them explicitly so that the user is not surprised.
        if(position < 1 || position > currentWheels + 1){
            showError("La posicion debe estar entre 1 y " + (currentWheels + 1) + ".");
            return;
        }

        // The model preserves the existing wheels and their symbols.
        // Hiding first prevents obsolete paint objects from remaining.
        machine.makeInvisible();
        machine.addWheel(type, position);
        boolean success = machine.ok();
        if(success){
            machine.synchronizeWheelPositions();
        }
        machine.makeVisible();

        if(!success){
            showError("No se pudo agregar la rueda solicitada.");
            return;
        }

        JOptionPane.showMessageDialog(
            null,
            "Se agrego la rueda " + types[selection]
                + " en la posicion " + position + "."
                + "\nRuedas actuales: " + machine.wheels()
                + "\n\nLa nueva rueda no contiene simbolos todavia."
                + "\nPuedes agregarlos desde Administrar simbolos.",
            "Slot Machine Pro - Rueda agregada",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Removes the wheel at the position selected by the player.
     * Rebel wheels retain their model-defined deletion restriction.
     * A hide/synchronize/show cycle removes the deleted wheel graphics
     * and closes any gaps between the surviving wheels.
     *
     * @param machine machine controlled by the player
     */
    private static void deleteWheel(SlotMachine machine)
    {
        int currentWheels = machine.wheels();

        if(currentWheels == 0){
            showError("No hay ruedas para eliminar.");
            return;
        }

        String positionInput = JOptionPane.showInputDialog(
            "Numero de rueda a eliminar (1-" + currentWheels + "):"
                + "\nLas ruedas Rebel no pueden eliminarse."
        );
        if(positionInput == null){
            return;
        }

        int position;
        try{
            position = Integer.parseInt(positionInput.trim());
        }
        catch(NumberFormatException e){
            showError("Ingresa un numero entero valido para la posicion.");
            return;
        }

        if(position < 1 || position > currentWheels){
            showError("La posicion debe estar entre 1 y " + currentWheels + ".");
            return;
        }

        // Hide every existing wheel before removal so its graphical
        // elements do not remain drawn after the model drops it.
        machine.makeInvisible();
        machine.delWheel(position);
        boolean success = machine.ok();
        if(success){
            machine.synchronizeWheelPositions();
        }
        machine.makeVisible();

        if(!success){
            showError(
                "No se pudo eliminar la rueda " + position + "."
                    + "\nLas ruedas Rebel no permiten eliminacion."
            );
            return;
        }

        JOptionPane.showMessageDialog(
            null,
            "Se elimino la rueda de la posicion " + position + "."
                + "\nRuedas restantes: " + machine.wheels(),
            "Slot Machine Pro - Rueda eliminada",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Provides operations for adding, deleting and selecting symbols.
     * All operations delegate their rules to SlotMachine.
     *
     * @param machine machine controlled by the player
     */
    private static void showSymbolMenu(SlotMachine machine)
    {
        String[] options = {
            "Agregar simbolo",
            "Eliminar simbolo",
            "Seleccionar simbolo",
            "Volver"
        };

        boolean inMenu = true;

        while(inMenu)
        {
            int choice = JOptionPane.showOptionDialog(
                null,
                "Ruedas actuales: " + machine.wheels()
                    + "\nSimbolos en la maquina: " + machine.symbols().length
                    + "\nConfiguracion: " + buildStatus(machine.configuration())
                    + "\n\nSelecciona una accion:",
                "Slot Machine Pro - Administrar simbolos",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
            );

            switch(choice)
            {
                case 0:
                    addSymbol(machine);
                    break;

                case 1:
                    deleteSymbol(machine);
                    break;

                case 2:
                    selectSymbol(machine);
                    break;

                default:
                    inMenu = false;
                    break;
            }
        }
    }

    /**
     * Adds one of the four supported symbol types to a selected wheel.
     * The color palette is limited to colors recognized by Canvas.
     *
     * @param machine machine controlled by the player
     */
    private static void addSymbol(SlotMachine machine)
    {
        int wheel = requestWheelPosition(machine, "agregar un simbolo");
        if(wheel == -1){
            return;
        }

        String[] types = {"Normal", "Ephemeral", "Shy", "Pulsing", "Cancelar"};
        int selection = JOptionPane.showOptionDialog(
            null,
            "Selecciona el tipo de simbolo para la rueda " + wheel + ":",
            "Slot Machine Pro - Tipo de simbolo",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            types,
            types[0]
        );

        if(selection < 0 || selection >= 4){
            return;
        }

        String[] typeNames = {"normal", "ephemeral", "shy", "pulsing"};
        String type = typeNames[selection];

        // Canvas recognizes these names as actual graphical colors.
        String[] palette = {
            "red", "blue", "green", "yellow", "magenta",
            "orange", "black", "gray", "white"
        };

        String color = chooseColor(
            palette,
            "Selecciona el color del simbolo " + types[selection]
                + " para la rueda " + wheel + ":",
            "Slot Machine Pro - Color del simbolo"
        );
        if(color == null){
            return;
        }

        machine.addSymbol(type, color, wheel);
        if(!machine.ok()){
            showError(
                "No se pudo agregar el simbolo."
                    + "\nCada rueda admite un solo simbolo de cada color."
            );
            return;
        }

        // Updating visibility refreshes an empty wheel that has just
        // received its first symbol, including its specialized marker.
        machine.makeVisible();

        JOptionPane.showMessageDialog(
            null,
            "Simbolo " + types[selection] + " (" + color + ") agregado"
                + " a la rueda " + wheel + ".",
            "Slot Machine Pro - Simbolo agregado",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Deletes the first occurrence of a color across the machine.
     * The model does not offer deletion by specific wheel position;
     * the interface therefore explains this behavior explicitly.
     *
     * @param machine machine controlled by the player
     */
    private static void deleteSymbol(SlotMachine machine)
    {
        String[] colors = existingColors(machine);
        if(colors.length == 0){
            showError("No hay simbolos disponibles para eliminar.");
            return;
        }

        String color = chooseColor(
            colors,
            "Selecciona el color que deseas eliminar."
                + "\nSe eliminara su primera aparicion, buscando"
                + " de la rueda 1 hacia la ultima.",
            "Slot Machine Pro - Eliminar simbolo"
        );
        if(color == null){
            return;
        }

        machine.delSymbol(color);
        if(!machine.ok()){
            showError("No se pudo eliminar el simbolo " + color + ".");
            return;
        }

        JOptionPane.showMessageDialog(
            null,
            "Se elimino la primera aparicion del simbolo " + color + "."
                + "\nLas demas apariciones de ese color no se modificaron.",
            "Slot Machine Pro - Simbolo eliminado",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Selects a symbol in a particular wheel through placeSymbol.
     * Selecting may also change the visual state of shy and pulsing
     * symbols, as defined by their own specialized classes.
     *
     * @param machine machine controlled by the player
     */
    private static void selectSymbol(SlotMachine machine)
    {
        int wheel = requestWheelPosition(machine, "seleccionar un simbolo");
        if(wheel == -1){
            return;
        }

        String[] colors = existingColors(machine);
        if(colors.length == 0){
            showError("No hay simbolos disponibles para seleccionar.");
            return;
        }

        String color = chooseColor(
            colors,
            "Selecciona el color para la rueda " + wheel + "."
                + "\nLa lista contiene los colores existentes en la maquina;"
                + " no todos tienen que estar en esta rueda.",
            "Slot Machine Pro - Seleccionar simbolo"
        );
        if(color == null){
            return;
        }

        boolean wasJackpot = machine.isJackpot();
        machine.placeSymbol(wheel, color);
        if(!machine.ok()){
            showError(
                "La rueda " + wheel + " no contiene el simbolo " + color + "."
                + "\nElige un color que pertenezca a esa rueda."
            );
            return;
        }

        showJackpotIfNew(machine, wasJackpot);

        JOptionPane.showMessageDialog(
            null,
            "Simbolo " + color + " seleccionado en la rueda " + wheel + "."
                + "\nObserva su apariencia en la ventana grafica.",
            "Slot Machine Pro - Simbolo seleccionado",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Requests and validates a wheel position. A canceled request
     * or an invalid value returns -1 without modifying the machine.
     *
     * @param machine machine controlled by the player
     * @param action description of the requested action
     * @return selected wheel position, or -1 when unavailable
     */
    private static int requestWheelPosition(SlotMachine machine, String action)
    {
        int total = machine.wheels();
        if(total == 0){
            showError("No hay ruedas en la maquina. Agrega una rueda primero.");
            return -1;
        }

        String input = JOptionPane.showInputDialog(
            "Numero de rueda para " + action + " (1-" + total + "):"
        );
        if(input == null){
            return -1;
        }

        int position;
        try{
            position = Integer.parseInt(input.trim());
        }
        catch(NumberFormatException e){
            showError("Ingresa un numero entero valido para la rueda.");
            return -1;
        }

        if(position < 1 || position > total){
            showError("La rueda debe estar entre 1 y " + total + ".");
            return -1;
        }

        return position;
    }

    /**
     * Shows a color selection list. Returns null when canceled.
     *
     * @param colors colors offered to the player
     * @param message explanation shown in the dialog
     * @param title dialog title
     * @return selected color, or null on cancellation
     */
    private static String chooseColor(String[] colors, String message, String title)
    {
        if(colors.length == 0){
            return null;
        }

        Object selected = JOptionPane.showInputDialog(
            null,
            message,
            title,
            JOptionPane.QUESTION_MESSAGE,
            null,
            colors,
            colors[0]
        );

        if(selected == null){
            return null;
        }
        return selected.toString();
    }

    /**
     * Returns distinct colors present anywhere in the machine.
     * The public model API exposes the aggregated symbol list,
     * not the individual symbol lists for each wheel.
     *
     * @param machine machine controlled by the player
     * @return available distinct colors, preserving their order
     */
    private static String[] existingColors(SlotMachine machine)
    {
        String[] all = machine.symbols();
        String[] unique = new String[all.length];
        int count = 0;

        for(int i = 0; i < all.length; i++){
            boolean found = false;
            for(int j = 0; j < count; j++){
                if(unique[j].equals(all[i])){
                    found = true;
                    break;
                }
            }
            if(!found){
                unique[count] = all[i];
                count++;
            }
        }

        String[] result = new String[count];
        for(int i = 0; i < count; i++){
            result[i] = unique[i];
        }
        return result;
    }

    /**
     * Groups the existing configuration action and a read-only
     * display of the current machine state.
     *
     * @param machine machine controlled by the player
     */
    private static void showConfigurationMenu(SlotMachine machine)
    {
        String[] options = {
            "Configurar simbolos",
            "Consultar estado",
            "Volver"
        };

        boolean inMenu = true;

        while(inMenu)
        {
            int choice = JOptionPane.showOptionDialog(
                null,
                "Configuracion actual: " + buildStatus(machine.configuration())
                    + "\n\nSelecciona una accion:",
                "Slot Machine Pro - Consultar y configurar",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
            );

            switch(choice)
            {
                case 0:
                    configureSymbols(machine);
                    break;

                case 1:
                    showMachineStatus(machine);
                    break;

                default:
                    inMenu = false;
                    break;
            }
        }
    }

    /**
     * Spins one wheel by the requested number of steps and announces
     * a newly achieved jackpot, preserving the original spin behavior.
     *
     * @param machine machine controlled by the player
     */
    private static void spinWheel(SlotMachine machine)
    {
        String wheelStr = JOptionPane.showInputDialog(
            "Numero de rueda (1-" + machine.wheels() + "):"
        );
        if(wheelStr == null){ return; }

        String stepsStr = JOptionPane.showInputDialog("Numero de pasos:");
        if(stepsStr == null){ return; }

        try
        {
            int wheel = Integer.parseInt(wheelStr.trim());
            int steps = Integer.parseInt(stepsStr.trim());
            boolean wasJackpot = machine.isJackpot();
            machine.spin(wheel, steps);

            if(!machine.ok()){
                showError("Posicion o pasos invalidos.");
            }
            else{
                showJackpotIfNew(machine, wasJackpot);
            }
        }
        catch(NumberFormatException e)
        {
            showError("Ingresa numeros validos.");
        }
    }

    /**
     * Locks a wheel using the operation from the original menu.
     *
     * @param machine machine controlled by the player
     */
    private static void lockWheel(SlotMachine machine)
    {
        String wheelStr = JOptionPane.showInputDialog(
            "Numero de rueda a fijar (1-" + machine.wheels() + "):"
        );
        if(wheelStr == null){ return; }

        try
        {
            machine.lock(Integer.parseInt(wheelStr.trim()));
            if(!machine.ok()){
                showError("No se pudo fijar la rueda seleccionada.");
            }
        }
        catch(NumberFormatException e)
        {
            showError("Ingresa un numero valido.");
        }
    }

    /**
     * Unlocks a wheel using the operation from the original menu.
     *
     * @param machine machine controlled by the player
     */
    private static void unlockWheel(SlotMachine machine)
    {
        String wheelStr = JOptionPane.showInputDialog(
            "Numero de rueda a soltar (1-" + machine.wheels() + "):"
        );
        if(wheelStr == null){ return; }

        try
        {
            machine.unlock(Integer.parseInt(wheelStr.trim()));
            if(!machine.ok()){
                showError("No se pudo soltar la rueda seleccionada.");
            }
        }
        catch(NumberFormatException e)
        {
            showError("Ingresa un numero valido.");
        }
    }

    /**
     * Exchanges two wheels using the operation from the original menu.
     * After a successful swap, moves the graphical elements of the
     * existing wheels to match their new logical positions.
     *
     * @param machine machine controlled by the player
     */
    private static void swapWheels(SlotMachine machine)
    {
        String firstStr = JOptionPane.showInputDialog(
            "Primera rueda (1-" + machine.wheels() + "):"
        );
        if(firstStr == null){ return; }

        String secondStr = JOptionPane.showInputDialog(
            "Segunda rueda (1-" + machine.wheels() + "):"
        );
        if(secondStr == null){ return; }

        try
        {
            machine.swap(
                Integer.parseInt(firstStr.trim()),
                Integer.parseInt(secondStr.trim())
            );
            if(!machine.ok()){
                showError("No se puede intercambiar.");
            }
            else{
                machine.makeInvisible();
                machine.synchronizeWheelPositions();
                machine.makeVisible();
            }
        }
        catch(NumberFormatException e)
        {
            showError("Ingresa numeros validos.");
        }
    }

    /**
     * Configures one visible symbol per wheel, using the existing
     * spin(String[]) operation. The menu requires exactly as many
     * nonempty colors as wheels so that an incomplete user input is
     * not silently interpreted as a partial configuration.
     *
     * SlotMachine decides which colors belong to each wheel. If a
     * requested color is unavailable, that operation may already have
     * updated other wheels; Main does not duplicate the model state.
     *
     * @param machine machine controlled by the player
     */
    private static void configureSymbols(SlotMachine machine)
    {
        int total = machine.wheels();
        if(total == 0){
            showError("No hay ruedas para configurar. Agrega una rueda primero.");
            return;
        }

        String configInput = JOptionPane.showInputDialog(
            "CONFIGURAR SIMBOLOS\n"
                + "\nConfiguracion actual: " + buildStatus(machine.configuration())
                + "\nRuedas instaladas: " + total
                + "\nColores existentes en la maquina: "
                + String.join(", ", existingColors(machine))
                + "\n\nIngresa exactamente " + total
                + " colores separados por comas, uno por rueda."
                + "\nEjemplo: red,red,red"
                + "\nCada color debe existir en su rueda correspondiente."
        );
        if(configInput == null){
            return;
        }

        String[] parts = configInput.split(",", -1);
        if(parts.length != total){
            showError("Debes ingresar exactamente " + total
                + " colores, uno por cada rueda.");
            return;
        }

        String[] colors = new String[total];
        for(int i = 0; i < total; i++){
            colors[i] = parts[i].trim();
            if(colors[i].isEmpty()){
                showError("No puedes dejar colores vacios. Revisa la rueda "
                    + (i + 1) + ".");
                return;
            }
        }

        boolean wasJackpot = machine.isJackpot();
        machine.spin(colors);
        if(!machine.ok()){
            showError("Alguno de los colores no existe en la rueda indicada."
                + "\nEs posible que otras ruedas si hayan cambiado."
                + "\nConfiguracion actual: " + buildStatus(machine.configuration()));
        }
        else{
            showJackpotIfNew(machine, wasJackpot);
        }
    }

    /**
     * Announces a jackpot only when a successful operation changes
     * the machine from a nonwinning to a winning configuration.
     * This prevents duplicate dialogs when an existing jackpot has
     * not changed or when a symbol is reselected without new victory.
     *
     * @param machine machine after the completed operation
     * @param wasJackpot true if the previous configuration was winning
     */
    private static void showJackpotIfNew(SlotMachine machine, boolean wasJackpot)
    {
        if(!wasJackpot && machine.isJackpot()){
            showJackpot();
        }
    }

    /**
     * Shows the original jackpot message without extra animations.
     */
    private static void showJackpot()
    {
        JOptionPane.showMessageDialog(
            null,
            "¡JACKPOT! Todas las ruedas coinciden.",
            "Slot Machine Pro",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Shows an operation or input error.
     *
     * @param message explanation of the error
     */
    private static void showError(String message)
    {
        JOptionPane.showMessageDialog(
            null,
            message,
            "Error",
            JOptionPane.WARNING_MESSAGE
        );
    }

    /**
     * Shows a detailed, read-only summary of the model state and a
     * legend for the graphical indicators of specialized objects.
     * No wheel type is inferred from its color or stored in Main.
     *
     * @param machine machine being consulted
     */
    private static void showMachineStatus(SlotMachine machine)
    {
        StringBuilder status = new StringBuilder();
        status.append("SLOT MACHINE PRO - ESTADO\n\n");
        status.append("Ruedas instaladas: ").append(machine.wheels());
        status.append(" / 3 (limite exclusivo del menu visual)\n");
        status.append("Simbolos totales: ").append(machine.symbols().length);
        status.append("\nConfiguracion: ").append(buildStatus(machine.configuration()));
        status.append("\nJackpot: ").append(machine.isJackpot() ? "SI" : "NO");
        status.append("\n\nTIPOS DE RUEDA (color de marco)");
        status.append("\nNormal: blanco");
        status.append("\nLefty: amarillo (copia su vecino izquierdo)");
        status.append("\nRebel: naranja (restricciones de operaciones)");
        status.append("\n\nTIPOS DE SIMBOLO (marcador)");
        status.append("\nNormal: sin marcador");
        status.append("\nEphemeral: magenta (disminuye de tamano al girar)");
        status.append("\nShy: azul (alterna visibilidad al seleccionarse)");
        status.append("\nPulsing: verde (alterna tamano al seleccionarse)");
        status.append("\n\nColores presentes: ");
        String[] colors = existingColors(machine);
        status.append(colors.length == 0 ? "ninguno" : String.join(", ", colors));

        JOptionPane.showMessageDialog(
            null,
            status.toString(),
            "Slot Machine Pro - Estado",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Formats the current symbol colors in wheel order.
     * Empty wheels are shown as '?' in the summary.
     *
     * @param config current symbol configuration
     * @return a readable list of the current symbols
     */
    private static String buildStatus(String[] config)
    {
        if(config == null || config.length == 0){
            return "(sin ruedas)";
        }

        StringBuilder sb = new StringBuilder("[");
        for(int i = 0; i < config.length; i++)
        {
            if(i > 0){
                sb.append(" | ");
            }
            sb.append(config[i] != null ? config[i] : "?");
        }
        sb.append("]");
        return sb.toString();
    }
}
