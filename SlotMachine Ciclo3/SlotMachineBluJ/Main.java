import javax.swing.JOptionPane;

/**
 * Interactive entry point for the Slot Machine simulator.
 * The player controls the machine through a menu:
 * spin, lock/unlock, swap wheels, and set a configuration.
 *
 * @author Julian Gomez Boada - Julian Carrero Carranza
 * @version Ciclo2 (05/09/2026)
 */
public class Main
{
    public static void main(String[] args)
    {
        SlotMachine machine = new SlotMachine();

        // Default setup: 3 wheels, 3 symbols each
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(3);

        machine.addSymbol(1, "red");
        machine.addSymbol(1, "blue");
        machine.addSymbol(1, "green");

        machine.addSymbol(2, "yellow");
        machine.addSymbol(2, "magenta");
        machine.addSymbol(2, "red");

        machine.addSymbol(3, "blue");
        machine.addSymbol(3, "green");
        machine.addSymbol(3, "yellow");

        machine.makeVisible();

        String[] options = {
            "1. Girar todas las ruedas",
            "2. Girar rueda N pasos (animado)",
            "3. Fijar una rueda",
            "4. Soltar una rueda",
            "5. Intercambiar dos ruedas",
            "6. Configurar simbolos",
            "0. Salir"
        };

        boolean running = true;

        while(running)
        {
            String[] config = machine.configuration();
            String status = buildStatus(config, machine);

            int choice = JOptionPane.showOptionDialog(
                null,
                "Estado: " + status + "\n\n¿Qué deseas hacer?",
                "Slot Machine Pro",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
            );

            if(choice < 0 || choice == 6)
            {
                running = false;
                continue;
            }

            switch(choice)
            {
                case 0:
                    machine.spin();
                    if(machine.isJackpot())
                        JOptionPane.showMessageDialog(null, "¡JACKPOT! Todas las ruedas coinciden.", "Slot Machine Pro", JOptionPane.INFORMATION_MESSAGE);
                    break;

                case 1:
                    String wheelStr = JOptionPane.showInputDialog("Número de rueda (1-" + machine.wheels() + "):");
                    String stepsStr = JOptionPane.showInputDialog("Número de pasos:");
                    if(wheelStr != null && stepsStr != null)
                    {
                        try
                        {
                            int w = Integer.parseInt(wheelStr.trim());
                            int s = Integer.parseInt(stepsStr.trim());
                            machine.spin(w, s);
                            if(!machine.ok())
                                JOptionPane.showMessageDialog(null, "Posición o pasos inválidos.", "Error", JOptionPane.WARNING_MESSAGE);
                            else if(machine.isJackpot())
                                JOptionPane.showMessageDialog(null, "¡JACKPOT! Todas las ruedas coinciden.", "Slot Machine Pro", JOptionPane.INFORMATION_MESSAGE);
                        }
                        catch(NumberFormatException e)
                        {
                            JOptionPane.showMessageDialog(null, "Ingresa números válidos.", "Error", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                    break;

                case 2:
                    String lockStr = JOptionPane.showInputDialog("Número de rueda a fijar (1-" + machine.wheels() + "):");
                    if(lockStr != null)
                    {
                        try
                        {
                            machine.lock(Integer.parseInt(lockStr.trim()));
                            if(!machine.ok())
                                JOptionPane.showMessageDialog(null, "Posición inválida.", "Error", JOptionPane.WARNING_MESSAGE);
                        }
                        catch(NumberFormatException e)
                        {
                            JOptionPane.showMessageDialog(null, "Ingresa un número válido.", "Error", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                    break;

                case 3:
                    String unlockStr = JOptionPane.showInputDialog("Número de rueda a soltar (1-" + machine.wheels() + "):");
                    if(unlockStr != null)
                    {
                        try
                        {
                            machine.unlock(Integer.parseInt(unlockStr.trim()));
                            if(!machine.ok())
                                JOptionPane.showMessageDialog(null, "Posición inválida.", "Error", JOptionPane.WARNING_MESSAGE);
                        }
                        catch(NumberFormatException e)
                        {
                            JOptionPane.showMessageDialog(null, "Ingresa un número válido.", "Error", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                    break;

                case 4:
                    String w1Str = JOptionPane.showInputDialog("Primera rueda (1-" + machine.wheels() + "):");
                    String w2Str = JOptionPane.showInputDialog("Segunda rueda (1-" + machine.wheels() + "):");
                    if(w1Str != null && w2Str != null)
                    {
                        try
                        {
                            machine.swap(Integer.parseInt(w1Str.trim()), Integer.parseInt(w2Str.trim()));
                            if(!machine.ok())
                                JOptionPane.showMessageDialog(null, "No se puede intercambiar.", "Error", JOptionPane.WARNING_MESSAGE);
                        }
                        catch(NumberFormatException e)
                        {
                            JOptionPane.showMessageDialog(null, "Ingresa números válidos.", "Error", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                    break;

                case 5:
                    String configInput = JOptionPane.showInputDialog(
                        "Colores separados por coma (ej: red,blue,green)\n" +
                        "Colores válidos: red, blue, green, yellow, magenta"
                    );
                    if(configInput != null && !configInput.trim().isEmpty())
                    {
                        String[] parts = configInput.trim().split(",");
                        String[] colors = new String[parts.length];
                        for(int i = 0; i < parts.length; i++){
                            colors[i] = parts[i].trim();
                        }
                        machine.spin(colors);
                        if(!machine.ok())
                            JOptionPane.showMessageDialog(null, "Algún color no existe en esa rueda.", "Error", JOptionPane.WARNING_MESSAGE);
                    }
                    break;

                default:
                    running = false;
                    break;
            }
        }

        machine.exit();
    }

    private static String buildStatus(String[] config, SlotMachine machine)
    {
        if(config == null || config.length == 0){ return "(sin ruedas)"; }
        StringBuilder sb = new StringBuilder("[");
        for(int i = 0; i < config.length; i++)
        {
            if(i > 0) sb.append(" | ");
            sb.append(config[i] != null ? config[i] : "?");
        }
        sb.append("]");
        return sb.toString();
    }
}
