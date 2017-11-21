package cz.encircled.macl;

import org.junit.Test;

/**
 * @author Vlad on 17-Nov-17.
 */
public class ConfigurationTest extends AbstractTest {

    @Test
    public void testMergeRequestValidation() {
        assertException(() -> base().setMergeRequestReplacement("wrong").valid(),
                "mergeRequestReplacement must contain token MR#");
    }

    private ChangelogConfiguration base() {
        return new ChangelogConfiguration().setLastTag("1.0");
    }

}
