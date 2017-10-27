package cz.encircled.macl;

import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Kisel on 22.6.2017.
 */
public class ChangelogTest extends AbstractTest {

    @Test
    public void testAppendNewMessages() {
        ChangelogExecutor executor = executor(new ChangelogConfiguration().setUnreleasedRowPattern("## \\[Unreleased]"));
        List<String> result = executor.insertNewMessages(changelog_A(), newMessagesFiltered(), executor.getIndexOfUnreleasedLine(changelog_A()));
        Assert.assertEquals(changelog_A(newMessagesFiltered()), result);
    }

    @Test
    public void testAppendNewMessageWithSpaceAfterUnreleased() {
        ChangelogExecutor executor = executor(new ChangelogConfiguration().setUnreleasedRowPattern("## \\[Unreleased]"));
        int unreleased = executor.getIndexOfUnreleasedLine(changelog_A());

        List<String> result = executor.insertNewMessages(changelog_A(Collections.singleton("")), newMessagesFiltered(), unreleased);
        Assert.assertEquals(changelog_A(newMessagesFiltered()), result);
    }

}
