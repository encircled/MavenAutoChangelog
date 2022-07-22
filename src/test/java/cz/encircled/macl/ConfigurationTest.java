package cz.encircled.macl;

import org.junit.Assert;
import org.junit.Test;

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
        Assert.assertNull(conf.mergeRequestReplacePattern);
        Assert.assertNull(conf.lastTagPattern);
    }

    private ChangelogConfiguration base() {
        return new ChangelogConfiguration().setLastTag("1.0");
    }

}
