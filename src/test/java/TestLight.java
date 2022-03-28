import com.panjohnny.game.light.LightMap;
import junit.framework.TestCase;

public class TestLight extends TestCase {
    public void testLightMap() {
        LightMap map = new LightMap(-100, -100, 100,100);

        // loop through all the points in the map
        for (int x = map.getMinX(); x < map.getMaxX(); x++) {
            for (int y = map.getMinY(); y < map.getMaxY(); y++) {
                map.set(x,y,1);
            }
        }
    }
}
