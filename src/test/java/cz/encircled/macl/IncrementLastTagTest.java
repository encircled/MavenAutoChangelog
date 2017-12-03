package cz.encircled.macl;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Vlad on 03-Dec-17.
 */
public class IncrementLastTagTest extends AbstractTest {

    @Test
    public void testIncrementValue() {
        ChangelogExecutor executor = executor(new ChangelogConfiguration());

        Assert.assertEquals("2", executor.incrementLastTag("1"));
        Assert.assertEquals("10", executor.incrementLastTag("9"));

        Assert.assertEquals("17.2", executor.incrementLastTag("17.1"));
        Assert.assertEquals("17.10", executor.incrementLastTag("17.9"));

        Assert.assertEquals("17.1.1.1", executor.incrementLastTag("17.1.1.0"));
        Assert.assertEquals("17.1.1.10", executor.incrementLastTag("17.1.1.9"));

        Assert.assertEquals("17.1000", executor.incrementLastTag("17.999"));

        Assert.assertEquals("17.8-3m", executor.incrementLastTag("17.8-2m"));
        Assert.assertEquals("17.8-10m", executor.incrementLastTag("17.8-9m"));
    }

    @Test
    public void testIncrementIllegalFormat() {
        ChangelogExecutor executor = executor(new ChangelogConfiguration());

        assertException(() -> executor.incrementLastTag("abc"), "Can't find version number in the last tag");
    }

}
