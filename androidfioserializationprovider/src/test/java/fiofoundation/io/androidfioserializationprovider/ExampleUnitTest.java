package fiofoundation.io.androidfioserializationprovider;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void testAbiJson()
    {
        Map<String, String> map = AbiFIOJson.INSTANCE.getAbiFioJsonMap();

        //map.put("test","test");

        System.out.println(map.toString());
              //System.out.println(map.get("test"));
    }
}