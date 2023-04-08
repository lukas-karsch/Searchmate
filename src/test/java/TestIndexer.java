import jdk.jfr.Description;
import karsch.lukas.Indexer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class TestIndexer {
    Indexer indexer;

    @Before
    public void setUp() {
        indexer = new Indexer();
    }

    @Test
    public void testSimple() {
        String input = "Game game Game Design – Jack, der Bankräuber";
        List<String> tokens = List.of("game", "game", "game", "design", "jack", "der", "bankräuber");

        Assert.assertEquals(tokens, indexer.getSortedTokensFromString(input));
    }

    @Test
    @Description("If a string contains digits that follow an alphabetic character, they should be split")
    public void testNumbers() {
        String input = "killer234 is my babe!!1";
        List<String> tokens = List.of("killer", "234", "is", "my", "babe", "1");

        Assert.assertEquals(tokens, indexer.getSortedTokensFromString(input));
    }
}
