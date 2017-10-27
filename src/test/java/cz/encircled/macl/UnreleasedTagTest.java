package cz.encircled.macl;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Kisel on 27.10.2017.
 */
public class UnreleasedTagTest extends AbstractTest {

    @Test
    public void testUnreleasedIndex() {
        int index = executor(new ChangelogConfiguration()
                .setUnreleasedRowPattern("## \\[Unreleased]"))
                .getIndexOfUnreleasedLine(changelog_A());
        Assert.assertEquals(3, index);
    }

    @Test
    public void testUnreleasedIndexNotExist() {
        assertException(() -> executor(
                new ChangelogConfiguration()
                        .setUnreleasedRowPattern("## \\[NotExistUnreleased]"))
                        .getIndexOfUnreleasedLine(changelog_A()),

                "Row with unreleased changes was not found for pattern ## \\[NotExistUnreleased]");
    }

}
