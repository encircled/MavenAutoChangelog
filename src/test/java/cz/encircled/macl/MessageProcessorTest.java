package cz.encircled.macl;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * @author Kisel on 27.10.2017.
 */
public class MessageProcessorTest extends AbstractTest {

    @Test
    public void testFilteredAndMergeRequestAdded() {
        ChangelogConfiguration conf = processorConf("^\\(.*$");
        NavigableSet<String> result = defaultMessageProcessor(conf)
                .getNewMessages(newMessagesUnfiltered());

        Assert.assertEquals(newMessagesFiltered(), result);
    }

    @Test
    public void testTrim() {
        NavigableSet<String> result = defaultMessageProcessor(processorConf(".*"))
                .getNewMessages(s(" test "));

        Assert.assertEquals(s("test"), result);
    }

    @Test
    public void testCommitFormat() {
        NavigableSet<String> result = defaultMessageProcessor(processorConf(".*").setCommitFormat("my-%s-format"))
                .getNewMessages(s(" test "));

        Assert.assertEquals(s("my-test-format"), result);
    }

    private NavigableSet<String> s(String s) {
        return new TreeSet<>(Collections.singletonList(s));
    }

    private ChangelogConfiguration processorConf(String applicableCommitPattern) {
        return new ChangelogConfiguration()
                .setCommitFormat("%s")
                .setMergeRequestReplacePattern("(\\))")
                .setMergeRequestReplacement(" MR#$1")
                .setApplicableCommitPattern(applicableCommitPattern);
    }


}
