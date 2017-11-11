package cz.encircled.macl;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Kisel on 27.10.2017.
 */
public class LastTagTest extends AbstractTest {

    @Test
    public void testLastTagPattern() {
        Pair<String, Integer> lastTag = executor(new ChangelogConfiguration()
                .setLastTagFormat("%s")
                .setLastTagPattern("## \\[([\\d.]+)] - [\\d]{4}.[\\d]{2}.[\\d]{2}"))
                .getLastTag(changelog_A());
        Assert.assertEquals(Pair.of("2017.03", 6), lastTag);
    }

    @Test
    public void testLastTag() {
        Pair<String, Integer> lastTag = executor(new ChangelogConfiguration()
                .setLastTagFormat("%s")
                .setLastTagPattern("## \\[([\\d.]+)] - [\\d]{4}.[\\d]{2}.[\\d]{2}")
                .setLastTag("my_2017.1.1"))
                .getLastTag(changelog_A());
        Assert.assertEquals(Pair.of("my_2017.1.1", null), lastTag);
    }

    @Test
    public void testLastTagIsMissing() {
        assertException(() -> executor(new ChangelogConfiguration().valid()),

                "LastTagFormat or lastTag must be specified");
    }

    @Test
    public void testLastTagBothPresent() {
        assertException(() ->
                        executor(new ChangelogConfiguration()
                                .setLastTag("123")
                                .setLastTagFormat("123")
                                .valid()),

                "Only one of [lastTagFormat, lastTag] might be present");
    }

    @Test
    public void testLastFormat() {
        Pair<String, Integer> lastTag = executor(new ChangelogConfiguration()
                .setLastTagFormat("prefix-%s")
                .setLastTagPattern("## \\[([\\d.]+)] - [\\d]{4}.[\\d]{2}.[\\d]{2}"))
                .getLastTag(changelog_A());
        Assert.assertEquals(Pair.of("prefix-2017.03", 6), lastTag);
    }

}
