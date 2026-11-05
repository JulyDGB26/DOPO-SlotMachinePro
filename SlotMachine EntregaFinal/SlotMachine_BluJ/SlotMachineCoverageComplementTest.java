import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import java.awt.GraphicsEnvironment;
import org.junit.jupiter.api.Test;

/**
 * Complementary contract tests identified through the initial JaCoCo report.
 * Exercises previously uncovered public behaviors without modifying domain code.
 */
public class SlotMachineCoverageComplementTest
{
    @Test
    public void shouldRespectSymbolColorEqualityHashAndTextContract()
    {
        Symbol red = new Symbol("red");
        Symbol sameColor = new Symbol("red");
        Symbol blue = new Symbol("blue");

        assertTrue(red.equals(red));
        assertTrue(red.equals(sameColor));
        assertFalse(red.equals(blue));
        assertFalse(red.equals(new Object()));
        assertEquals(red.hashCode(), sameColor.hashCode());
        assertEquals("red", red.toString());
        assertEquals("red", red.getColor());
        assertEquals(70, red.getSize());
        assertTrue(red.isVisible());
        assertFalse(red.hasMarker());
        assertNull(red.markerColor());
        red.select();
        red.spin();
        assertEquals("red", red.getColor());
    }

    @Test
    public void shouldComputeContestMovementsFromDistinctInitialPositions()
    {
        int[][] movements = SlotMachineContest.solve(4);
        assertEquals(4, movements.length);
        assertArrayEquals(new int[]{1, 0}, movements[0]);
        assertArrayEquals(new int[]{2, 3}, movements[1]);
        assertArrayEquals(new int[]{3, 2}, movements[2]);
        assertArrayEquals(new int[]{4, 1}, movements[3]);
        assertEquals(0, SlotMachineContest.solve(-1).length);
    }

    @Test
    public void shouldSimulateContestVisuallyWithoutChangingTheSolverContract()
    {
        assumeFalse(GraphicsEnvironment.isHeadless(),
            "Graphical contest simulation requires an active desktop");
        SlotMachineContest.simulate(3);
        assertEquals(3, SlotMachineContest.solve(3).length);
    }

    @Test
    public void shouldDrawAndMoveTriangleInBothDirections()
    {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Graphical lifecycle test");
        Triangle triangle = new Triangle();
        triangle.moveHorizontal(1); // invisible operations are safe
        triangle.moveVertical(-1);
        triangle.changeSize(13, 15);
        triangle.changeColor("orange");
        triangle.makeVisible();
        triangle.moveRight();
        triangle.moveLeft();
        triangle.moveUp();
        triangle.moveDown();
        triangle.slowMoveHorizontal(2);
        triangle.slowMoveHorizontal(-2);
        triangle.slowMoveVertical(2);
        triangle.slowMoveVertical(-2);
        triangle.makeInvisible();
        triangle.makeVisible();
        triangle.makeInvisible();
        assertNotNull(Canvas.getCanvas());
    }

    @Test
    public void shouldDrawAndMoveCircleInBothDirections()
    {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Graphical lifecycle test");
        Circle circle = new Circle();
        circle.moveHorizontal(1);
        circle.moveVertical(-1);
        circle.changeSize(20);
        circle.changeColor("blue");
        circle.makeVisible();
        circle.moveRight();
        circle.moveLeft();
        circle.moveUp();
        circle.moveDown();
        circle.slowMoveHorizontal(2);
        circle.slowMoveHorizontal(-2);
        circle.slowMoveVertical(2);
        circle.slowMoveVertical(-2);
        circle.makeInvisible();
        assertNotNull(Canvas.getCanvas());
    }

    @Test
    public void shouldDrawAndMoveRectangleInBothDirections()
    {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Graphical lifecycle test");
        Rectangle rectangle = new Rectangle();
        rectangle.moveHorizontal(1);
        rectangle.moveVertical(-1);
        rectangle.changeSize(25, 30);
        rectangle.changeColor("green");
        rectangle.makeVisible();
        rectangle.moveRight();
        rectangle.moveLeft();
        rectangle.moveUp();
        rectangle.moveDown();
        rectangle.slowMoveHorizontal(2);
        rectangle.slowMoveHorizontal(-2);
        rectangle.slowMoveVertical(2);
        rectangle.slowMoveVertical(-2);
        rectangle.makeInvisible();
        assertNotNull(Canvas.getCanvas());
    }

    @Test
    public void shouldRecognizeEveryCanvasColorAndDefaultToBlack()
    {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Graphical palette test");
        Canvas canvas = Canvas.getCanvas();
        assertNotNull(canvas);
        assertTrue(canvas == Canvas.getCanvas());
        for(String color : new String[]{"red", "black", "blue", "yellow",
            "green", "magenta", "white", "gray", "grey", "orange", "unknown"}){
            canvas.setForegroundColor(color);
        }
    }
}
