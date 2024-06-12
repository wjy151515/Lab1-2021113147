import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.example.gra;

public class QueryBridgeWordsTest {
    private static Map<String, Map<String, Integer>> G;

    @Test
    public void testPath1() {
        G = new HashMap<>();
        G.put("word1", new HashMap<>(Map.of("word2", 1, "word5", 1)));
        G.put("word2", new HashMap<>(Map.of("word4", 1)));
        G.put("word3", new HashMap<>(Map.of("word1", 1)));
        G.put("word4", new HashMap<>(Map.of("word3", 1)));
        G.put("word5", new HashMap<>(Map.of("word6", 1)));
        G.put("word6", new HashMap<>());
        String result = gra.queryBridgeWords(G, "missing1", "word2");
        assertEquals("f2", result);
    }

    @Test
    public void testPath2() {
        G = new HashMap<>();
        G.put("word1", new HashMap<>(Map.of("word2", 1, "word5", 1)));
        G.put("word2", new HashMap<>(Map.of("word4", 1)));
        G.put("word3", new HashMap<>(Map.of("word1", 1)));
        G.put("word4", new HashMap<>(Map.of("word3", 1)));
        G.put("word5", new HashMap<>(Map.of("word6", 1)));
        G.put("word6", new HashMap<>());
        String result = gra.queryBridgeWords(G, "missing1", "missing2");
        assertEquals("f1", result);
    }

    @Test
    public void testPath3() {
        G = new HashMap<>();
        G.put("word1", new HashMap<>(Map.of("word2", 1, "word5", 1)));
        G.put("word2", new HashMap<>(Map.of("word4", 1)));
        G.put("word3", new HashMap<>(Map.of("word1", 1)));
        G.put("word4", new HashMap<>(Map.of("word3", 1)));
        G.put("word5", new HashMap<>(Map.of("word6", 1)));
        G.put("word6", new HashMap<>());
        String result = gra.queryBridgeWords(G, "word1", "missing2");
        assertEquals("f3", result);
    }

    @Test
    public void testPath4() {
        G = new HashMap<>();
        G.put("word1", new HashMap<>(Map.of("word2", 1, "word5", 1)));
        G.put("word2", new HashMap<>(Map.of("word4", 1)));
        G.put("word3", new HashMap<>(Map.of("word1", 1)));
        G.put("word4", new HashMap<>(Map.of("word3", 1)));
        G.put("word5", new HashMap<>(Map.of("word6", 1)));
        G.put("word6", new HashMap<>());
        String result = gra.queryBridgeWords(G, "word6", "word4");
        assertEquals("", result);
    }

    @Test
    public void testPath5() {
        G = new HashMap<>();
        G.put("word1", new HashMap<>(Map.of("word2", 1, "word5", 1)));
        G.put("word2", new HashMap<>(Map.of("word4", 1)));
        G.put("word3", new HashMap<>(Map.of("word1", 1)));
        G.put("word4", new HashMap<>(Map.of("word3", 1)));
        G.put("word5", new HashMap<>(Map.of("word6", 1)));
        G.put("word6", new HashMap<>());
        String result = gra.queryBridgeWords(G, "word1", "word4");
        assertEquals("word2", result);
    }

    @Test
    public void testPath6() {
        G = new HashMap<>();
        G.put("word1", new HashMap<>(Map.of("word2", 1)));
        G.put("word2", new HashMap<>(Map.of("word4", 1)));
        G.put("word3", new HashMap<>(Map.of("word1", 1)));
        G.put("word4", new HashMap<>());
        String result = gra.queryBridgeWords(G, "word1", "word3");
        assertEquals("", result);
    }

    @Test
    public void testPath7() {
        G = new HashMap<>();
        G.put("word1", new HashMap<>(Map.of("word2", 1, "word5", 1)));
        G.put("word2", new HashMap<>(Map.of("word4", 1)));
        G.put("word3", new HashMap<>(Map.of("word1", 1)));
        G.put("word4", new HashMap<>(Map.of("word3", 1)));
        G.put("word5", new HashMap<>(Map.of("word6", 1)));
        G.put("word6", new HashMap<>());
        String result = gra.queryBridgeWords(G, "word5", "word1");
        assertEquals("", result);
    }
}
