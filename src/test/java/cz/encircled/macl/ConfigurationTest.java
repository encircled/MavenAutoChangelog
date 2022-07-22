package cz.encircled.macl;

import org.junit.Test;

import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Vlad on 17-Nov-17.
 */
public class ConfigurationTest extends AbstractTest {

    @Test
    public void testMergeRequestValidation() {
        assertException(() -> base().setMergeRequestReplacement("wrong").valid(),
                "mergeRequestReplacement must contain token MR#");

        assertException(() -> base().setMergeRequestReplacement(null).valid(),
                "mergeRequestReplacement must contain token MR#");
    }

    @Test
    public void testEmptyPatterns() {
        ChangelogConfiguration conf = base().setMergeRequestReplacePattern(null).setLastTagPattern("");
        assertNull(conf.mergeRequestReplacePattern);
        assertNull(conf.lastTagPattern);
    }

    @Test
    public void testNonEmptyPatterns() {
        ChangelogConfiguration conf = base().setMergeRequestReplacePattern(".*").setLastTagPattern(".*");
        assertEquals(Pattern.compile(".*").pattern(), conf.mergeRequestReplacePattern.pattern());
        assertEquals(Pattern.compile(".*").pattern(), conf.lastTagPattern.pattern());
    }

    private ChangelogConfiguration base() {
        return new ChangelogConfiguration().setLastTag("1.0");
    }

}
