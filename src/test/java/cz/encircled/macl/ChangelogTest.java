package cz.encircled.macl;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

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
    public void testLastTag() {
        String lastTag = executor(new ChangelogConfiguration()
                .setLastTagFormat("%s")
                .setLastTagPattern("## \\[([\\d.]+)] - [\\d]{4}.[\\d]{2}.[\\d]{2}"))
                .getLastTag(defaultLines());
        Assert.assertEquals("2017.01", lastTag);
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

    private List<String> defaultNewLines() {
        return Arrays.asList("Test 1", "Test 2");
    }

    private List<String> defaultLines() {
        return Arrays.asList("## [Unreleased]", "", "", "## [2017.01] - 2017.03.02");
    }

}
