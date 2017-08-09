package cz.encircled.macl;

import java.util.Arrays;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Kisel on 22.6.2017.
 */
public class ChangelogTest {

    @Test
    public void testInsertNewMessage() {
        List<String> result = executor(new ChangelogConfiguration()).insertNewMessages(defaultLines(), defaultNewLines(), 0);
        Assert.assertEquals(Arrays.asList("## [Unreleased]", "Test 1", "Test 2", "", "## [2017.01] - 2017.03.02"), result);
    }

    @Test
    public void testInsertAppendNewMessage() {
        List<String> lines = Arrays.asList("## [Unreleased]", "", "Some test", "", "## [2017.01] - 2017.03.02");
        List<String> result = executor(new ChangelogConfiguration()).insertNewMessages(lines, defaultNewLines(), 0);
        Assert.assertEquals(Arrays.asList("## [Unreleased]", "Test 1", "Test 2", "Some test", "", "## [2017.01] - 2017.03.02"), result);
    }

    @Test
    public void testUnreleasedIndex() {
        int index = executor(new ChangelogConfiguration()
                .setUnreleasedRowPattern("## \\[Unreleased]"))
                .getIndexOfUnreleasedLine(defaultLines());
        Assert.assertEquals(0, index);
    }

    @Test
    public void testLastTagPattern() {
        String lastTag = executor(new ChangelogConfiguration()
                .setLastTagFormat("%s")
                .setLastTagPattern("## \\[([\\d.]+)] - [\\d]{4}.[\\d]{2}.[\\d]{2}"))
                .getLastTag(defaultLines());
        Assert.assertEquals("2017.01", lastTag);
    }

    @Test
    public void testLastTag() {
        String lastTag = executor(new ChangelogConfiguration()
                .setLastTagFormat("%s")
                .setLastTagPattern("## \\[([\\d.]+)] - [\\d]{4}.[\\d]{2}.[\\d]{2}")
                .setLastTag("2017.1.1"))
                .getLastTag(defaultLines());
        Assert.assertEquals("2017.1.1", lastTag);
    }

    @Test(expected = IllegalStateException.class)
    public void testLastTagIsMissing() {
        executor(new ChangelogConfiguration().valid());
    }

    @Test(expected = IllegalStateException.class)
    public void testLastTagBothPresent() {
        executor(new ChangelogConfiguration()
                .setLastTag("123")
                .setLastTagFormat("123")
                .valid());
    }

    @Test
    public void testLastFormat() {
        String lastTag = executor(new ChangelogConfiguration()
                .setLastTagFormat("prefix-%s")
                .setLastTagPattern("## \\[([\\d.]+)] - [\\d]{4}.[\\d]{2}.[\\d]{2}"))
                .getLastTag(defaultLines());
        Assert.assertEquals("prefix-2017.01", lastTag);
    }

    private ChangelogExecutor executor(ChangelogConfiguration conf) {
        return new ChangelogExecutor(conf, (log, tagFrom) -> defaultNewLines());
    }

    private NavigableSet<String> defaultNewLines() {
        return new TreeSet<>(Arrays.asList("Test 1", "Test 2"));
    }

    private List<String> defaultLines() {
        return Arrays.asList("## [Unreleased]", "", "", "## [2017.01] - 2017.03.02");
    }

}
