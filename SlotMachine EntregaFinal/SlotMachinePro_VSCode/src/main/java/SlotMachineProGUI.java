import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Graphical interface for the SlotMachinePro simulator by Julian G.
 *
 * The main window offers four operation groups. The existing
 * JOptionPane submenus and their interactions are preserved.
 * The Canvas still displays the machine and specialized symbols.
 *
 * The graphical demonstration accepts at most three simultaneous
 * wheels because the Canvas has a fixed width. The domain model
 * has no corresponding limit. Wheel positioning is synchronized
 * after structural operations without replacing the domain objects.
 *
 * Initial symbols allow a jackpot after three global spins:
 * [red | yellow | blue] -> ... -> [red | red | red].
 * The existing dialog announces each newly reached jackpot.
 *
 * Dear Teacher Angie, i hope that my work, which required so much effort and time, meets with your approval.
 *
 * @author Julian Gomez Boada
 * @version Cierre y Pulido Final
 */
public class SlotMachineProGUI extends JFrame
{
    private SlotMachine machine;

    // The production UI uses JOptionPane; tests can supply scripted dialogs.
    private final DialogPort dialogs;

    // VIEW - COMPONENTS REUSED BY THE CONTROLLER
    private JLabel labelStatus;
    private JButton buttonPlay;
    private JButton buttonWheels;
    private JButton buttonSymbols;
    private JButton buttonConfiguration;
    private JButton buttonExit;

    /**
     * Creates the model, prepares the view, registers its controller
     * and starts the existing graphical machine demonstration.
     */
    private SlotMachineProGUI()
    {
        this(new SwingDialogPort());
    }

    /**
     * Allows tests to provide non-blocking dialogs. In normal execution
     * the no-argument constructor retains exactly the original Swing dialogs.
     * Package visibility intentionally restricts this overload to the project.
     *
     * @param dialogPort interaction provider used by the GUI
     */
    SlotMachineProGUI(DialogPort dialogPort)
    {
        if(dialogPort == null){
            throw new IllegalArgumentException("DialogPort cannot be null");
        }
        dialogs = dialogPort;
        machine = new SlotMachine();
        prepareModel();
        prepareView();
        prepareController();
        start();
    }

    /**
     * Preserves the validated three-wheel demonstration setup.
     * Different symbol counts make a jackpot reachable through spins.
     */
    private void prepareModel()
    {
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
    }

    // VIEW - WINDOW AND COMPONENT LAYOUT

    /**
     * Builds the main JFrame using the Swing containers studied
     * in class. The existing operation dialogs remain unchanged.
     */
    private void prepareView()
    {
        setTitle("Slot Machine Pro - Menu principal");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel panelStatus = prepareViewStatus();
        JPanel panelButtons = prepareViewButtons();

        add(panelStatus, BorderLayout.NORTH);
        add(panelButtons, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }

    /**
     * Prepares the status panel displayed above the menu buttons.
     * Its label is an attribute because actions refresh its content.
     *
     * @return the status container
     */
    private JPanel prepareViewStatus()
    {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 18, 4, 18));
        labelStatus = new JLabel("", SwingConstants.CENTER);
        panel.add(labelStatus, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Prepares the main navigation buttons. The components are
     * attributes because their action listeners are registered later.
     *
     * @return the menu container
     */
    private JPanel prepareViewButtons()
    {
        JPanel panel = new JPanel(new GridLayout(5, 1, 7, 7));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 24, 18, 24));

        buttonPlay = new JButton("Jugar");
        buttonWheels = new JButton("Administrar ruedas");
        buttonSymbols = new JButton("Administrar simbolos");
        buttonConfiguration = new JButton("Consultar y configurar");
        buttonExit = new JButton("Salir");

        panel.add(buttonPlay);
        panel.add(buttonWheels);
        panel.add(buttonSymbols);
        panel.add(buttonConfiguration);
        panel.add(buttonExit);
        return panel;
    }

    // CONTROLLER - EVENT REGISTRATION

    /**
     * Registers a listener for each active component.
     * Anonymous listeners delegate behavior to dedicated actions,
     * following the course's event/condition/action scheme.
     */
    private void prepareController()
    {
        buttonPlay.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){buttonPlayAction();}
        });

        buttonWheels.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){buttonWheelsAction();}
        });

        buttonSymbols.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){buttonSymbolsAction();}
        });

        buttonConfiguration.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event){buttonConfigurationAction();}
        });

        buttonExit.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){buttonExitAction();}
        });

        // Closing the window is equivalent to selecting "Salir".
        addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent event){buttonExitAction();}
        });
    }

    // CONTROLLER - ACTION METHODS

    /** Opens the original play submenu, then refreshes the status. */
    private void buttonPlayAction()
    {
        showPlayMenu();
        refreshView();
    }

    /** Opens wheel administration without changing its operations. */
    private void buttonWheelsAction()
    {
        showWheelMenu();
        refreshView();
    }

    /** Opens symbol administration without changing its operations. */
    private void buttonSymbolsAction()
    {
        showSymbolMenu();
        refreshView();
    }

    /** Opens configuration and read-only consultation. */
    private void buttonConfigurationAction()
    {
        showConfigurationMenu();
        refreshView();
    }

    /** Preserves the original machine-exit behavior. */
    private void buttonExitAction()
    {
        machine.exit();
    }

    /**
     * Makes the existing Canvas visible and initializes the GUI status.
     */
    private void start()
    {
        machine.makeVisible();
        refreshView();
    }

    /**
     * Reads the domain state without storing duplicate model data.
     * Called when a submenu returns to the main interface.
     */
    private void refreshView()
    {
        labelStatus.setText("<html><div style='text-align:center;'>" + "SLOT MACHINE PRO<br>" + "Estado actual: " + buildStatus(machine.configuration()) + "<br>" + "Jackpot: " + (machine.isJackpot() ? "SI" : "NO") + "</div></html>");
    }

    /**
     * Starts the Swing interface on its event-dispatch thread.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable(){
            public void run()
            {
                SlotMachineProGUI gui = new SlotMachineProGUI();
                gui.setVisible(true);
            }
        });
    }

    // EXISTING OPERATIONS - JOPTIONPANE SUBMENUS

    /**
     * Shows the existing spinning operations in the play submenu.
     */
    private void showPlayMenu()
    {
        String[] options = {"Girar todas", "Girar una rueda", "Volver"};

        boolean inMenu = true;

        while(inMenu){
            int choice = dialogs.showOptionDialog(null, "Configuracion: " + buildStatus(machine.configuration()) + "\n\nSelecciona una accion:", "Slot Machine Pro - Jugar", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

            switch(choice){
                case 0:
                    boolean wasJackpot = machine.isJackpot();
                    machine.spin();
                    if(!machine.ok()){
                        showError("No fue posible girar las ruedas.");
                    }
                    else{
                        showJackpotIfNew(wasJackpot);
                    }
                    break;

                case 1:
                    spinWheel();
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
     */
    private void showWheelMenu()
    {
        String[] options = {"Agregar rueda", "Eliminar rueda", "Bloquear", "Desbloquear", "Intercambiar", "Volver"};

        boolean inMenu = true;

        while(inMenu){
            int choice = dialogs.showOptionDialog(null, "Ruedas actuales: " + machine.wheels() + "\nConfiguracion: " + buildStatus(machine.configuration()) + "\n\nSelecciona una accion:", "Slot Machine Pro - Administrar ruedas", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

            switch(choice){
                case 0:
                    addWheel();
                    break;

                case 1:
                    deleteWheel();
                    break;

                case 2:
                    lockWheel();
                    break;

                case 3:
                    unlockWheel();
                    break;

                case 4:
                    swapWheels();
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
     */
    private void addWheel()
    {
        final int maxVisualWheels = 3;
        int currentWheels = machine.wheels();

        if(currentWheels >= maxVisualWheels){
            dialogs.showMessageDialog(null, "Limite de representacion grafica: esta demostracion " + "permite un maximo de 3 ruedas al mismo tiempo." + "\nElimina una rueda antes de agregar otra.", "Slot Machine Pro - Limite visual", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] types = {"Normal", "Lefty", "Rebel", "Cancelar"};
        
        int selection = dialogs.showOptionDialog(null, "Selecciona el tipo de rueda que deseas agregar:", "Slot Machine Pro - Agregar rueda", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, types, types[0]);

        if(selection < 0 || selection >= 3){
            return;
        }

        String type;
        switch(selection){
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

        String positionInput = dialogs.showInputDialog("Posicion de la nueva rueda (1-" + (currentWheels + 1) + "):" + "\nLas ruedas siguientes se desplazan una posicion.");
        
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

        dialogs.showMessageDialog(null, "Se agrego la rueda " + types[selection] + " en la posicion " + position + "." + "\nRuedas actuales: " + machine.wheels() + "\n\nLa nueva rueda no contiene simbolos todavia." + "\nPuedes agregarlos desde Administrar simbolos.", "Slot Machine Pro - Rueda agregada", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Removes the wheel at the position selected by the player.
     * Rebel wheels retain their model-defined deletion restriction.
     * A hide/synchronize/show cycle removes the deleted wheel graphics
     * and closes any gaps between the surviving wheels.
     */
    private void deleteWheel()
    {
        int currentWheels = machine.wheels();

        if(currentWheels == 0){
            showError("No hay ruedas para eliminar.");
            return;
        }

        String positionInput = dialogs.showInputDialog("Numero de rueda a eliminar (1-" + currentWheels + "):" + "\nLas ruedas Rebel no pueden eliminarse.");
        
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
            showError("No se pudo eliminar la rueda " + position + "." + "\nLas ruedas Rebel no permiten eliminacion.");
            return;
        }

        dialogs.showMessageDialog(null, "Se elimino la rueda de la posicion " + position + "." + "\nRuedas restantes: " + machine.wheels(), "Slot Machine Pro - Rueda eliminada", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Provides operations for adding, deleting and selecting symbols.
     * All operations delegate their rules to SlotMachine.
     */
    private void showSymbolMenu()
    {
        String[] options = {"Agregar simbolo", "Eliminar simbolo", "Seleccionar simbolo", "Volver"};

        boolean inMenu = true;

        while(inMenu){
            int choice = dialogs.showOptionDialog(null, "Ruedas actuales: " + machine.wheels() + "\nSimbolos en la maquina: " + machine.symbols().length + "\nConfiguracion: " + buildStatus(machine.configuration()) + "\n\nSelecciona una accion:", "Slot Machine Pro - Administrar simbolos", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

            switch(choice){
                case 0:
                    addSymbol();
                    break;

                case 1:
                    deleteSymbol();
                    break;

                case 2:
                    selectSymbol();
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
     */
    private void addSymbol()
    {
        int wheel = requestWheelPosition("agregar un simbolo");
        
        if(wheel == -1){
            return;
        }

        String[] types = {"Normal", "Ephemeral", "Shy", "Pulsing", "Cancelar"};
        
        int selection = dialogs.showOptionDialog(null, "Selecciona el tipo de simbolo para la rueda " + wheel + ":", "Slot Machine Pro - Tipo de simbolo", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, types, types[0]);

        if(selection < 0 || selection >= 4){
            return;
        }

        String[] typeNames = {"normal", "ephemeral", "shy", "pulsing"};
        String type = typeNames[selection];

        // Canvas recognizes these names as actual graphical colors.
        String[] palette = {"red", "blue", "green", "yellow", "magenta", "orange", "black", "gray", "white"};

        String color = chooseColor(palette, "Selecciona el color del simbolo " + types[selection] + " para la rueda " + wheel + ":", "Slot Machine Pro - Color del simbolo");
        
        if(color == null){
            return;
        }

        machine.addSymbol(type, color, wheel);
        if(!machine.ok()){
            showError("No se pudo agregar el simbolo." + "\nCada rueda admite un solo simbolo de cada color.");
            return;
        }

        // Updating visibility refreshes an empty wheel that has just
        // received its first symbol, including its specialized marker.
        machine.makeVisible();

        dialogs.showMessageDialog(null, "Simbolo " + types[selection] + " (" + color + ") agregado" + " a la rueda " + wheel + ".", "Slot Machine Pro - Simbolo agregado", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Deletes the first occurrence of a color across the machine.
     * The model does not offer deletion by specific wheel position;
     * the interface therefore explains this behavior explicitly.
     */
    private void deleteSymbol()
    {
        String[] colors = existingColors();
        if(colors.length == 0){
            showError("No hay simbolos disponibles para eliminar.");
            return;
        }

        String color = chooseColor(colors, "Selecciona el color que deseas eliminar." + "\nSe eliminara su primera aparicion, buscando" + " de la rueda 1 hacia la ultima.", "Slot Machine Pro - Eliminar simbolo");

        if(color == null){
            return;
        }

        machine.delSymbol(color);
        if(!machine.ok()){
            showError("No se pudo eliminar el simbolo " + color + ".");
            return;
        }

        dialogs.showMessageDialog(null, "Se elimino la primera aparicion del simbolo " + color + "." + "\nLas demas apariciones de ese color no se modificaron.", "Slot Machine Pro - Simbolo eliminado", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Selects a symbol in a particular wheel through placeSymbol.
     * Selecting may also change the visual state of shy and pulsing
     * symbols, as defined by their own specialized classes.
     */
    private void selectSymbol()
    {
        int wheel = requestWheelPosition("seleccionar un simbolo");
        if(wheel == -1){
            return;
        }

        String[] colors = existingColors();
        if(colors.length == 0){
            showError("No hay simbolos disponibles para seleccionar.");
            return;
        }

        String color = chooseColor(colors, "Selecciona el color para la rueda " + wheel + "." + "\nLa lista contiene los colores existentes en la maquina;" + " no todos tienen que estar en esta rueda.", "Slot Machine Pro - Seleccionar simbolo");
        
        if(color == null){
            return;
        }

        boolean wasJackpot = machine.isJackpot();
        machine.placeSymbol(wheel, color);
        if(!machine.ok()){
            showError("La rueda " + wheel + " no contiene el simbolo " + color + "." + "\nElige un color que pertenezca a esa rueda.");
            return;
        }

        showJackpotIfNew(wasJackpot);

        dialogs.showMessageDialog(null, "Simbolo " + color + " seleccionado en la rueda " + wheel + "." + "\nObserva su apariencia en la ventana grafica.", "Slot Machine Pro - Simbolo seleccionado", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Requests and validates a wheel position. A canceled request
     * or an invalid value returns -1 without modifying the machine.
     *

     * @param action description of the requested action
     * @return selected wheel position, or -1 when unavailable
     */
    private int requestWheelPosition(String action)
    {
        int total = machine.wheels();
        if(total == 0){
            showError("No hay ruedas en la maquina. Agrega una rueda primero.");
            return -1;
        }

        String input = dialogs.showInputDialog("Numero de rueda para " + action + " (1-" + total + "):");
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
    private String chooseColor(String[] colors, String message, String title)
    {
        if(colors.length == 0){
            return null;
        }

        Object selected = dialogs.showInputDialog(null, message, title, JOptionPane.QUESTION_MESSAGE, null, colors, colors[0]);

        if(selected == null){
            return null;
        }
        return selected.toString();
    }

    /**
     * Returns distinct colors present anywhere in the machine.
     * The public model exposes the aggregated symbol list,
     * not the individual symbol lists for each wheel.
     *

     * @return available distinct colors, preserving their order
     */
    private String[] existingColors()
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
     */
    private void showConfigurationMenu()
    {
        String[] options = {"Configurar simbolos", "Consultar estado", "Volver"};

        boolean inMenu = true;

        while(inMenu){
            int choice = dialogs.showOptionDialog(null, "Configuracion actual: " + buildStatus(machine.configuration()) + "\n\nSelecciona una accion:", "Slot Machine Pro - Consultar y configurar", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

            switch(choice){
                case 0:
                    configureSymbols();
                    break;

                case 1:
                    showMachineStatus();
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
     */
    private void spinWheel()
    {
        String wheelStr = dialogs.showInputDialog("Numero de rueda (1-" + machine.wheels() + "):");
        
        if(wheelStr == null){ return; }

        String stepsStr = dialogs.showInputDialog("Numero de pasos:");
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
                showJackpotIfNew(wasJackpot);
            }
        }
        catch(NumberFormatException e)
        {
            showError("Ingresa numeros validos.");
        }
    }

    /**
     * Locks a wheel using the operation from the original menu.
     */
    private void lockWheel()
    {
        String wheelStr = dialogs.showInputDialog("Numero de rueda a fijar (1-" + machine.wheels() + "):");
        
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
     */
    private void unlockWheel()
    {
        String wheelStr = dialogs.showInputDialog("Numero de rueda a soltar (1-" + machine.wheels() + "):");
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
     */
    private void swapWheels()
    {
        String firstStr = dialogs.showInputDialog("Primera rueda (1-" + machine.wheels() + "):");
        
        if(firstStr == null){ return; }

        String secondStr = dialogs.showInputDialog("Segunda rueda (1-" + machine.wheels() + "):");
        
        if(secondStr == null){ return; }

        try
        {
            machine.swap(Integer.parseInt(firstStr.trim()), Integer.parseInt(secondStr.trim()));
            
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
     */
    private void configureSymbols()
    {
        int total = machine.wheels();
        if(total == 0){
            showError("No hay ruedas para configurar. Agrega una rueda primero.");
            return;
        }

        String configInput = dialogs.showInputDialog("CONFIGURAR SIMBOLOS\n" + "\nConfiguracion actual: " + buildStatus(machine.configuration()) + "\nRuedas instaladas: " + total + "\nColores existentes en la maquina: " + String.join(", ", existingColors()) + "\n\nIngresa exactamente " + total + " colores separados por comas, uno por rueda." + "\nEjemplo: red,red,red" + "\nCada color debe existir en su rueda correspondiente.");
        
        if(configInput == null){
            return;
        }

        String[] parts = configInput.split(",", -1);
        if(parts.length != total){
            showError("Debes ingresar exactamente " + total + " colores, uno por cada rueda.");
            return;
        }

        String[] colors = new String[total];
        for(int i = 0; i < total; i++){
            colors[i] = parts[i].trim();
            if(colors[i].isEmpty()){
                showError("No puedes dejar colores vacios. Revisa la rueda " + (i + 1) + ".");
                return;
            }
        }

        boolean wasJackpot = machine.isJackpot();
        machine.spin(colors);
        if(!machine.ok()){
            showError("Alguno de los colores no existe en la rueda indicada." + "\nEs posible que otras ruedas si hayan cambiado." + "\nConfiguracion actual: " + buildStatus(machine.configuration()));
        }
        else{
            showJackpotIfNew(wasJackpot);
        }
    }

    /**
     * Announces a jackpot only when a successful operation changes
     * the machine from a nonwinning to a winning configuration.
     * This prevents duplicate dialogs when an existing jackpot has
     * not changed or when a symbol is reselected without new victory.
     *

     * @param wasJackpot true if the previous configuration was winning
     */
    private void showJackpotIfNew(boolean wasJackpot)
    {
        if(!wasJackpot && machine.isJackpot()){
            showJackpot();
        }
    }

    /**
     * Shows the original jackpot message without extra animations.
     */
    private void showJackpot()
    {
        dialogs.showMessageDialog(null, "¡JACKPOT! Todas las ruedas coinciden.", "Slot Machine Pro", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Shows an operation or input error.
     *
     * @param message explanation of the error
     */
    private void showError(String message)
    {
        dialogs.showMessageDialog(null, message, "Error", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Shows a detailed, read-only summary of the model state and a
     * legend for the graphical indicators of specialized objects.
     * No wheel type is inferred from its color or stored in Main.
     */
    private void showMachineStatus()
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
        String[] colors = existingColors();
        status.append(colors.length == 0 ? "ninguno" : String.join(", ", colors));

        dialogs.showMessageDialog(null, status.toString(), "Slot Machine Pro - Estado", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Formats the current symbol colors in wheel order.
     * Empty wheels are shown as '?' in the summary.
     *
     * @param config current symbol configuration
     * @return a readable list of the current symbols
     */
    private String buildStatus(String[] config)
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

    // ----------------------------------------------------------
    // DIALOG PORT - TESTABILITY WITHOUT CHANGING INTERACTIONS
    // ----------------------------------------------------------

    /**
     * Only separates user input/output from the menu's decisions.
     * In production, SwingDialogPort calls the same JOptionPane methods.
     * Tests can supply deterministic answers without blocking on modal dialogs.
     */
    interface DialogPort
    {
        int showOptionDialog(java.awt.Component parent, Object message,
            String title, int optionType, int messageType, javax.swing.Icon icon,
            Object[] options, Object initialValue);

        String showInputDialog(Object message);

        Object showInputDialog(java.awt.Component parent, Object message,
            String title, int messageType, javax.swing.Icon icon,
            Object[] selectionValues, Object initialSelectionValue);

        void showMessageDialog(java.awt.Component parent, Object message,
            String title, int messageType);
    }

    /** Default adapter: delegates unchanged parameters to JOptionPane. */
    private static class SwingDialogPort implements DialogPort
    {
        public int showOptionDialog(java.awt.Component parent, Object message,
            String title, int optionType, int messageType, javax.swing.Icon icon,
            Object[] options, Object initialValue)
        {
            return JOptionPane.showOptionDialog(parent, message, title,
                optionType, messageType, icon, options, initialValue);
        }

        public String showInputDialog(Object message)
        {
            return JOptionPane.showInputDialog(message);
        }

        public Object showInputDialog(java.awt.Component parent, Object message,
            String title, int messageType, javax.swing.Icon icon,
            Object[] selectionValues, Object initialSelectionValue)
        {
            return JOptionPane.showInputDialog(parent, message, title,
                messageType, icon, selectionValues, initialSelectionValue);
        }

        public void showMessageDialog(java.awt.Component parent, Object message,
            String title, int messageType)
        {
            JOptionPane.showMessageDialog(parent, message, title, messageType);
        }
    }
}
