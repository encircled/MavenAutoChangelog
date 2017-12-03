package cz.encircled.macl;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author Kisel on 22.6.2017.
 */
public class ChangelogTest extends AbstractTest {

    @Test
    public void testAppendNewMessages() {
        ChangelogExecutor executor = appendExecutor();
        int unreleasedLine = executor.getIndexOfUnreleasedLine(changelog_A());
        Pair<String, Integer> lastTag = executor.getLastTag(changelog_A());

        List<String> result = executor.insertNewMessages(changelog_A(), newMessagesFiltered(), unreleasedLine, lastTag);
        Assert.assertEquals(changelog_A(newMessagesFiltered()), result);
    }

    @Test
    public void testAppendNewMessageWithSpaceAfterUnreleased() {
        ChangelogExecutor executor = appendExecutor();
        List<String> allLines = changelog_A(Collections.singleton(""));
        int unreleasedLine = executor.getIndexOfUnreleasedLine(allLines);
        Pair<String, Integer> lastTag = executor.getLastTag(changelog_A());

        List<String> result = executor.insertNewMessages(allLines, newMessagesFiltered(), unreleasedLine, lastTag);

        ArrayList<String> expected = new ArrayList<>(newMessagesFiltered());
        expected.add(0, "");
        Assert.assertEquals(changelog_A(expected), result);
    }

    @Test
    public void testAppendNewMessagesDuplicities() {
        ChangelogExecutor executor = appendExecutor();
        int unreleasedLine = executor.getIndexOfUnreleasedLine(changelog_A());
        Pair<String, Integer> lastTag = executor.getLastTag(changelog_A());

        Set<String> newMessages = newMessagesFiltered();
        newMessages.add("- (MY-777) new feature");

        List<String> result = executor.insertNewMessages(changelog_A(), newMessages, unreleasedLine, lastTag);
        Assert.assertEquals(changelog_A(newMessagesFiltered()), result);
    }

    private ChangelogExecutor appendExecutor() {
        return executor(new ChangelogConfiguration()
                .setLastTagPattern(".*\\[([\\d\\.]+)].*")
                .setLastTagFormat("%s")
                .setUnreleasedRowPattern("## \\[Unreleased]"));
    }

}
