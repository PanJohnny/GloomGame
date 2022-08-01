import com.panjohnny.game.GloomGame;
import com.panjohnny.game.Options;
import com.panjohnny.game.data.plf.PLFTools;
import com.panjohnny.game.render.Colors;
import com.panjohnny.game.tile.Tile;
import com.panjohnny.game.widgets.TextWidget;
import junit.framework.TestCase;

import java.lang.reflect.InvocationTargetException;

public class LevelLoadingTest extends TestCase {
    public void testStringify() {
        GloomGame.main(new String[]{"-only-init"});
        Tile tile = new Tile(0, 0, 10, 10);
        String string = PLFTools.stringifyObject(tile);

        assertEquals("com.panjohnny.game.tile.Tile(0,0,10,10)", string);
    }

    public void testObjectify() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        GloomGame.main(new String[]{"-only-init"});
        String string = "com.panjohnny.game.tile.Tile(0,0,10,10)";
        Tile tile = (Tile) PLFTools.objectifyString(string);

        assertEquals(0, tile.getX());
        assertEquals(0, tile.getY());
        assertEquals(10, tile.getWidth());
        assertEquals(10, tile.getHeight());
    }

    public void testAdvancedConvert() throws InvocationTargetException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        GloomGame.main(new String[]{"-only-init"});
        String widgetString = "com.panjohnny.game.widgets.TextWidget(0,0,Hello\\u2c} World!,%s,10)".formatted(Colors.RED.getRGB());
        TextWidget widget = (TextWidget) PLFTools.convertString(widgetString);

        assertEquals(0, widget.getX());
        assertEquals(0, widget.getY());
        assertEquals("Hello, World!", widget.getText());
        assertEquals(Colors.RED.getRGB(), widget.getColor().getRGB());
        assertEquals(10, widget.getSize());
    }

    public void testStringifyAdvanced() throws InvocationTargetException, IllegalAccessException {
        GloomGame.main(new String[]{"-only-init"});
        TextWidget widget = new TextWidget(0, 0, "Hello, World!", Colors.RED, 10);
        String string = PLFTools.stringify(widget);

        System.out.println(string);

        assertEquals(string, "com.panjohnny.game.widgets.TextWidget(0,0,Hello, World!,-6133911,10)");
    }

    public void testAccessGetterString() {
        assertEquals(PLFTools.applyAccessGetterLogic("rgb", "get↑"), "getRGB");
        assertEquals(PLFTools.applyAccessGetterLogic("something", "get⌃"), "getSomething");
    }

    public void testIllegalString() {
        String string = "Hello, World! (I am a string) This is backslash: \\";
        String plfed = PLFTools.makeStringSuitable(string);
        System.out.println(plfed);
        assertEquals("Hello\\u2c} World! \\u28}I am a string\\u29} This is backslash: \\u5c}", plfed);
    }
}
