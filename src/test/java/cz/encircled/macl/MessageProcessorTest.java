package cz.encircled.macl;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Kisel on 27.10.2017.
 */
public class MessageProcessorTest extends AbstractTest {

    @Test
    public void testFilteredAndMergeRequestAdded() {
        ChangelogConfiguration conf = processorConf("^\\(.*$");
        Set<String> result = defaultMessageProcessor(conf)
                .getNewMessages(newMessagesUnfiltered());

        Assert.assertEquals(newMessagesFiltered(), result);
    }

    @Test
    public void testTrim() {
        Set<String> result = defaultMessageProcessor(processorConf(".*"))
                .getNewMessages(s(" test "));

        Assert.assertEquals(s("test"), result);
    }

    @Test
    public void testCommitFormat() {
        Set<String> result = defaultMessageProcessor(processorConf(".*").setCommitFormat("my-%s-format"))
                .getNewMessages(s(" test "));

        Assert.assertEquals(s("my-test-format"), result);
    }

    private Set<String> s(String s) {
        return new LinkedHashSet<>(Collections.singletonList(s));
    }

    private ChangelogConfiguration processorConf(String applicableCommitPattern) {
        return new ChangelogConfiguration()
                .setCommitFormat("%s")
                .setMergeRequestReplacePattern("(\\))")
                .setMergeRequestReplacement(" MR#$1")
                .setApplicableCommitPattern(applicableCommitPattern);
    }


}
