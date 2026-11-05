import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.awt.Component;
import java.awt.Container;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the existing presentation/controller behavior using real Swing
 * components, with scripted responses to modal JOptionPane questions.
 *
 * The production interface is not replaced or mocked: only the dialog
 * input/output boundary is controlled. No Robot, sleeps, GUI reflection,
 * System.exit or production domain changes are required.
 *
 * These tests need a graphical desktop (as in VS Code on Windows).
 */
public class SlotMachineProGUICoverageTest
{
    private SlotMachineProGUI gui;
    private ScriptedDialogs dialogs;

    @BeforeEach
    public void prepareGui()
    {
        assumeFalse(GraphicsEnvironment.isHeadless(),
            "GUI coverage requires a graphical desktop");
        dialogs = new ScriptedDialogs();
        onEdt(new Runnable(){
            public void run(){ gui = new SlotMachineProGUI(dialogs); }
        });
    }

    @AfterEach
    public void releaseGui()
    {
        if(gui != null){
            onEdt(new Runnable(){
                public void run(){ gui.dispose(); }
            });
        }
    }

    @Test
    public void shouldPrepareViewAndControllersWithoutChangingInitialModel()
    {
        assertTrue(status().contains("[red | yellow | blue]"));
        assertTrue(status().contains("Jackpot: NO"));
        assertNotNull(findButton(gui, "Jugar"));
        assertNotNull(findButton(gui, "Administrar ruedas"));
        assertNotNull(findButton(gui, "Administrar simbolos"));
        assertNotNull(findButton(gui, "Consultar y configurar"));
        assertNotNull(findButton(gui, "Salir"));
        assertEquals(1, findButton(gui, "Jugar").getActionListeners().length);
        assertEquals(1, gui.getWindowListeners().length);
        dialogs.assertConsumed();
    }

    @Test
    public void shouldLaunchRealMainWithTheOriginalSwingDialogAdapter()
    {
        // No submenus are opened. The production main() still constructs
        // and displays a JFrame using its normal JOptionPane adapter.
        SlotMachineProGUI.main(new String[0]);
        onEdt(new Runnable(){
            public void run(){
                boolean launched = false;
                for(java.awt.Frame frame : java.awt.Frame.getFrames()){
                    if("Slot Machine Pro - Menu principal".equals(frame.getTitle())
                        && frame != gui && frame.isVisible()){
                        launched = true;
                        frame.dispose();
                    }
                }
                assertTrue(launched, "main() must open the ordinary GUI window");
            }
        });
        dialogs.assertConsumed();
    }

    @Test
    public void shouldNotifyJackpotAfterManualSymbolSelection()
    {
        dialogs.options(2, 2, -1);
        dialogs.inputs("2", "3");
        dialogs.selections("red", "red");
        click("Administrar simbolos");
        assertTrue(status().contains("[red | red | red]"));
        assertEquals(1, dialogs.countMessages("JACKPOT"));
        dialogs.assertConsumed();
    }

    @Test
    public void shouldSpinAllThreeTimesAndAnnounceNewJackpotOnlyOnce()
    {
        dialogs.options(0, 0, 0, 0, -1);
        click("Jugar");
        assertTrue(status().contains("[blue | yellow | blue]")); // fourth spin
        assertTrue(dialogs.hasMessage("JACKPOT"));
        assertEquals(1, dialogs.countMessages("JACKPOT"));
        dialogs.assertConsumed();
    }

    @Test
    public void shouldSpinIndividualWheelAndHandleInvalidInputs()
    {
        dialogs.options(1, 1, 1, 1, -1);
        dialogs.inputs("1", "1", "bad", "1", "2", "0", "1", "x");
        click("Jugar");
        assertTrue(status().contains("[blue | yellow | blue]"));
        assertTrue(dialogs.hasMessage("numeros validos"));
        assertTrue(dialogs.hasMessage("Posicion o pasos invalidos"));
        dialogs.assertConsumed();
    }

    @Test
    public void shouldCancelSingleWheelSpinWithoutMutation()
    {
        dialogs.options(1, 1, -1);
        dialogs.inputs(null, "1", null);
        click("Jugar");
        assertTrue(status().contains("[red | yellow | blue]"));
        dialogs.assertConsumed();
    }

    @Test
    public void shouldRestrictAddWheelToThreeAndRejectInvalidDeletion()
    {
        dialogs.options(0, 1, 1, 1, -1);
        dialogs.inputs("bad", "0", "4");
        click("Administrar ruedas");
        assertTrue(dialogs.hasMessage("maximo de 3 ruedas"));
        assertTrue(dialogs.hasMessage("numero entero valido"));
        assertTrue(dialogs.hasMessage("posicion debe estar entre"));
        assertTrue(status().contains("[red | yellow | blue]"));
        dialogs.assertConsumed();
    }

    @Test
    public void shouldDeleteThenInsertLeftyAndSynchronizeWheelPositions()
    {
        // Delete 2: old wheel 3 becomes wheel 2; insert lefty at position 2.
        dialogs.options(1, -1);
        dialogs.inputs("2");
        click("Administrar ruedas");
        assertTrue(status().contains("[red | blue]"));

        dialogs.options(0, 1, -1);  // add, choose Lefty, return
        dialogs.inputs("2");
        click("Administrar ruedas");
        assertTrue(status().contains("[red | ? | blue]"));
        assertTrue(dialogs.hasMessage("Lefty"));
        dialogs.assertConsumed();
    }

    @Test
    public void shouldInsertNormalWheelAndRejectBadInsertionInput()
    {
        dialogs.options(1, -1);
        dialogs.inputs("3");
        click("Administrar ruedas");

        dialogs.options(0, -1, 0, 0, 0, 0, 0, 0, -1);
        dialogs.inputs("nope", "8", "2");
        click("Administrar ruedas");
        assertTrue(status().contains("[red | ? | yellow]"));
        assertTrue(dialogs.hasMessage("numero entero valido"));
        assertTrue(dialogs.hasMessage("posicion debe estar entre"));
        dialogs.assertConsumed();
    }

    @Test
    public void shouldInsertRebelAndEnforceItsRestrictions()
    {
        dialogs.options(1, -1);
        dialogs.inputs("3");
        click("Administrar ruedas");
        dialogs.options(0, 2, -1);
        dialogs.inputs("2");
        click("Administrar ruedas");

        dialogs.options(2, 4, 1, -1);
        dialogs.inputs("2", "1", "2", "2");
        click("Administrar ruedas");
        assertTrue(dialogs.hasMessage("fijar"));
        assertTrue(dialogs.hasMessage("intercambiar"));
        assertTrue(dialogs.hasMessage("Rebel no permiten eliminacion"));
        assertTrue(status().contains("[red | ? | yellow]"));
        dialogs.assertConsumed();
    }

    @Test
    public void shouldLockUnlockAndSwapNormalWheels()
    {
        dialogs.options(2, 3, 4, -1);
        dialogs.inputs("1", "1", "1", "3");
        click("Administrar ruedas");
        assertTrue(status().contains("[blue | yellow | red]"));
        dialogs.assertConsumed();
    }

    @Test
    public void shouldReportInvalidLockUnlockSwapInputs()
    {
        dialogs.options(2, 2, 3, 3, 4, 4, -1);
        dialogs.inputs("bad", "8", "bad", "8", "no", "1", "1", "n");
        click("Administrar ruedas");
        assertTrue(dialogs.countMessages("Error") >= 5);
        assertTrue(status().contains("[red | yellow | blue]"));
        dialogs.assertConsumed();
    }

    @Test
    public void shouldCancelWheelOperationsWithoutMutation()
    {
        dialogs.options(1, 2, 3, 4, 4, -1);
        dialogs.inputs(null, null, null, null, "1", null);
        click("Administrar ruedas");
        assertTrue(status().contains("[red | yellow | blue]"));
        dialogs.assertConsumed();
    }

    @Test
    public void shouldAddEverySymbolTypeAndRejectDuplicateColor()
    {
        // Add Ephemeral orange, Shy black, Pulsing gray, Normal white to wheel 1.
        dialogs.options(0, 1, 0, 2, 0, 3, 0, 0, 0, 0, -1);
        dialogs.inputs("1", "1", "1", "1", "1");
        dialogs.selections("orange", "black", "gray", "white", "red");
        click("Administrar simbolos");
        assertTrue(dialogs.hasMessage("Ephemeral (orange)"));
        assertTrue(dialogs.hasMessage("Shy (black)"));
        assertTrue(dialogs.hasMessage("Pulsing (gray)"));
        assertTrue(dialogs.hasMessage("Normal (white)"));
        assertTrue(dialogs.hasMessage("un solo simbolo de cada color"));
        dialogs.assertConsumed();
    }

    @Test
    public void shouldDeleteFirstMatchingSymbolAndSelectAvailableColor()
    {
        dialogs.options(1, 2, -1);
        dialogs.inputs("1");
        dialogs.selections("red", "blue");
        click("Administrar simbolos");
        assertTrue(status().contains("[blue | yellow | blue]"));
        assertTrue(dialogs.hasMessage("primera aparicion"));
        assertTrue(dialogs.hasMessage("seleccionado"));
        dialogs.assertConsumed();
    }

    @Test
    public void shouldRejectUnavailableSymbolSelection()
    {
        dialogs.options(2, -1);
        dialogs.inputs("2");
        dialogs.selections("green");
        click("Administrar simbolos");
        assertTrue(dialogs.hasMessage("no contiene el simbolo"));
        dialogs.assertConsumed();
    }

    @Test
    public void shouldValidateWheelPositionAndCancelSymbolActions()
    {
        dialogs.options(0, 0, 0, 2, -1);
        dialogs.inputs("oops", "0", null, "7");
        click("Administrar simbolos");
        assertTrue(dialogs.hasMessage("numero entero valido"));
        assertTrue(dialogs.hasMessage("rueda debe estar entre"));
        dialogs.assertConsumed();
    }

    @Test
    public void shouldCancelSymbolTypeAndColorSelection()
    {
        dialogs.options(0, -1, 0, 1, -1);
        dialogs.inputs("1", "1");
        dialogs.selections((String)null);
        click("Administrar simbolos");
        assertTrue(status().contains("[red | yellow | blue]"));
        dialogs.assertConsumed();
    }

    @Test
    public void shouldCancelDeletingAndSelectingColors()
    {
        dialogs.options(1, 2, -1);
        dialogs.inputs("1");
        dialogs.selections(null, null);
        click("Administrar simbolos");
        assertTrue(status().contains("[red | yellow | blue]"));
        dialogs.assertConsumed();
    }

    @Test
    public void shouldConfigureJackpotAndConsultFullState()
    {
        dialogs.options(0, 1, -1);
        dialogs.inputs("red,red,red");
        click("Consultar y configurar");
        assertTrue(status().contains("Jackpot: SI"));
        assertTrue(dialogs.hasMessage("JACKPOT"));
        assertTrue(dialogs.hasMessage("TIPOS DE SIMBOLO"));
        assertTrue(dialogs.hasMessage("Ruedas instaladas: 3"));
        dialogs.assertConsumed();
    }

    @Test
    public void shouldRejectMalformedConfigurationsAndUnavailableColor()
    {
        dialogs.options(0, 0, 0, 0, -1);
        dialogs.inputs("red,blue", "red,,blue", "red,red,orange", "red,red,green");
        click("Consultar y configurar");
        assertTrue(dialogs.hasMessage("exactamente 3 colores"));
        assertTrue(dialogs.hasMessage("colores vacios"));
        assertTrue(dialogs.hasMessage("no existe en la rueda"));
        dialogs.assertConsumed();
    }

    @Test
    public void shouldCancelConfigurationAndAllowStatusConsultation()
    {
        dialogs.options(0, 1, -1);
        dialogs.inputs((String)null);
        click("Consultar y configurar");
        assertTrue(status().contains("[red | yellow | blue]"));
        assertTrue(dialogs.hasMessage("Colores presentes"));
        dialogs.assertConsumed();
    }

    @Test
    public void shouldHandleEmptyMachineStateInAllOperationMenus()
    {
        // Delete all normal wheels; later operations are rejected without errors.
        for(int count = 3; count >= 1; count--){
            dialogs.options(1, -1);
            dialogs.inputs("1");
            click("Administrar ruedas");
        }
        assertTrue(status().contains("(sin ruedas)"));

        dialogs.options(1, -1); // Attempt to delete from the empty machine
        click("Administrar ruedas");
        assertTrue(dialogs.hasMessage("No hay ruedas para eliminar"));

        dialogs.options(0, 1, 2, -1); // Add, delete, select symbols (all invalid)
        click("Administrar simbolos");
        assertTrue(dialogs.hasMessage("No hay ruedas en la maquina"));
        assertTrue(dialogs.hasMessage("No hay simbolos disponibles para eliminar"));

        dialogs.options(0, 1, -1);
        click("Consultar y configurar");
        assertTrue(dialogs.hasMessage("No hay ruedas para configurar"));
        assertTrue(dialogs.hasMessage("ninguno"));
        dialogs.assertConsumed();
    }

    private void click(String name)
    {
        JButton button = findButton(gui, name);
        assertNotNull(button, "Missing Swing button: " + name);
        onEdt(new Runnable(){
            public void run(){ button.doClick(0); }
        });
    }

    private String status()
    {
        JLabel label = findLabel(gui);
        assertNotNull(label, "GUI status JLabel is missing");
        return label.getText();
    }

    private static JButton findButton(Container root, String text)
    {
        for(Component component : root.getComponents()){
            if(component.getClass() == JButton.class && text.equals(((JButton)component).getText())){
                return (JButton)component;
            }
            if(Container.class.isAssignableFrom(component.getClass())){
                JButton found = findButton((Container)component, text);
                if(found != null) return found;
            }
        }
        return null;
    }

    private static JLabel findLabel(Container root)
    {
        for(Component component : root.getComponents()){
            if(component.getClass() == JLabel.class){
                return (JLabel)component;
            }
            if(Container.class.isAssignableFrom(component.getClass())){
                JLabel found = findLabel((Container)component);
                if(found != null) return found;
            }
        }
        return null;
    }

    private static void onEdt(Runnable action)
    {
        if(SwingUtilities.isEventDispatchThread()){
            action.run();
            return;
        }
        try{
            SwingUtilities.invokeAndWait(action);
        }
        catch(Exception e){
            throw new AssertionError("Swing action failed", e);
        }
    }

    /** Records I/O and fails if an unanticipated modal dialog occurs. */
    private static class ScriptedDialogs implements SlotMachineProGUI.DialogPort
    {
        private final Queue<Integer> menuChoices = new LinkedList<Integer>();
        private final Queue<String> textInputs = new LinkedList<String>();
        private final Queue<String> colorChoices = new LinkedList<String>();
        private final List<String> output = new ArrayList<String>();

        ScriptedDialogs options(Integer... values){
            menuChoices.addAll(Arrays.asList(values));
            return this;
        }

        ScriptedDialogs inputs(String... values){
            textInputs.addAll(Arrays.asList(values));
            return this;
        }

        ScriptedDialogs selections(String... values){
            colorChoices.addAll(Arrays.asList(values));
            return this;
        }

        public int showOptionDialog(Component parent, Object message, String title,
            int optionType, int messageType, javax.swing.Icon icon,
            Object[] options, Object initialValue)
        {
            assertFalse(menuChoices.isEmpty(), "Unscripted menu: " + title);
            int value = menuChoices.remove();
            assertTrue(value < options.length && value >= -1,
                "Invalid scripted option for " + title + ": " + value);
            return value;
        }

        public String showInputDialog(Object message)
        {
            assertFalse(textInputs.isEmpty(), "Unscripted input: " + message);
            return textInputs.remove();
        }

        public Object showInputDialog(Component parent, Object message,
            String title, int messageType, javax.swing.Icon icon,
            Object[] choices, Object initialChoice)
        {
            assertFalse(colorChoices.isEmpty(), "Unscripted selection: " + title);
            String selected = colorChoices.remove();
            if(selected != null){
                assertTrue(Arrays.asList(choices).contains(selected),
                    "Unavailable scripted color " + selected + " in " + title);
            }
            return selected;
        }

        public void showMessageDialog(Component parent, Object message,
            String title, int messageType)
        {
            output.add(title + ": " + String.valueOf(message));
        }

        boolean hasMessage(String fragment)
        {
            for(String message : output){
                if(message.contains(fragment)) return true;
            }
            return false;
        }

        int countMessages(String fragment)
        {
            int count = 0;
            for(String message : output){
                if(message.contains(fragment)) count++;
            }
            return count;
        }

        void assertConsumed()
        {
            assertTrue(menuChoices.isEmpty(), "Unused menu choices: " + menuChoices);
            assertTrue(textInputs.isEmpty(), "Unused text inputs: " + textInputs);
            assertTrue(colorChoices.isEmpty(), "Unused color choices: " + colorChoices);
        }
    }
}
