import static org.junit.Assert.assertEquals;
import org.junit.Test;

import com.example.gra;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ShortestPathTest {

    @Test
    public void testShortestPath_ValidPathExists() {
        Map<String, Map<String, Integer>> graph = new HashMap<>();
        graph.put("the", new HashMap<>());
        graph.get("the").put("room", 3);
        graph.get("the").put("beautiful", 1);
        graph.put("room", new HashMap<>());
        graph.get("room").put("a", 1);
        graph.get("room").put("is", 2);
        graph.put("is", Collections.singletonMap("the", 2));
        graph.put("a", Collections.singletonMap("the", 1));
        graph.put("beautiful", Collections.singletonMap("room", 1));

        String word1 = "the";
        String word2 = "room";
        String expected = "the, beautiful, room";
        String actual = gra.calcShortestPath(graph, word1, word2);
        assertEquals(expected, actual);
    }

    @Test
    public void testShortestPath_Word1NotInGraph() {
        Map<String, Map<String, Integer>> graph = new HashMap<>();
        graph.put("room", new HashMap<>());
        graph.get("room").put("sound", 1);

        String word1 = "apple";
        String word2 = "sound";
        String expected = null;
        String actual = gra.calcShortestPath(graph, word1, word2);
        assertEquals(expected, actual);
    }

    @Test
    public void testShortestPath_Word2NotInGraph() {
        Map<String, Map<String, Integer>> graph = new HashMap<>();
        graph.put("the", new HashMap<>());
        graph.put("room", new HashMap<>());
        graph.get("the").put("room", 1);
        graph.get("room").put("sound", 1);

        String word1 = "the";
        String word2 = "apple";
        String expected = null;
        String actual = gra.calcShortestPath(graph, word1, word2);
        assertEquals(expected, actual);
    }

    @Test
    public void testShortestPath_OnlyfirstWordInput() {
        Map<String, Map<String, Integer>> graph = new HashMap<>();
        graph.put("the", new HashMap<>());
        graph.put("room", new HashMap<>());
        graph.get("the").put("room", 1);
        graph.get("room").put("sound", 1);

        String word2 = "the";
        String expected = null;
        String actual = gra.calcShortestPath(graph, "", word2);
        assertEquals(expected, actual);
    }

    @Test
    public void testShortestPath_OnlysecondWordInput() {
        Map<String, Map<String, Integer>> graph = new HashMap<>();
        graph.put("the", new HashMap<>());
        graph.put("room", new HashMap<>());
        graph.get("the").put("room", 1);
        graph.get("room").put("sound", 1);

        String word1 = "the";
        String expected = null;
        String actual = gra.calcShortestPath(graph, word1, "");
        assertEquals(expected, actual);
    }
}
