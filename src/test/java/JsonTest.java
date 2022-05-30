import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.regex.Pattern;

public class JsonTest extends TestCase {
    public void testSplitString() {
        JsonObject object = new JsonObject();
        JsonArray aut = new JsonArray();
        JsonArray objects = new JsonArray();
        aut.add("PanJohnny");
        object.add("authors", aut);
        JsonObject simple = new JsonObject();
        simple.addProperty("type", "Tile");
        simple.addProperty("x", 0);
        simple.addProperty("y", 0);
        simple.addProperty("width", 1);
        simple.addProperty("height", 1);
        simple.addProperty("texture", "default");

        objects.add(simple);

        // read objects


        String[] authors = object.get("authors").getAsString().split(Pattern.quote(","));
        String[] expected = {"PanJohnny"};
        assertEquals(Arrays.toString(expected), Arrays.toString(authors));
    }
}
