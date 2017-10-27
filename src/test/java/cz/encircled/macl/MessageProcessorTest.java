package cz.encircled.macl;

import java.util.Collection;
import java.util.Collections;
import java.util.NavigableSet;
import java.util.TreeSet;

import cz.encircled.macl.transform.DefaultMessageProcessor;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Kisel on 27.10.2017.
 */
public class MessageProcessorTest extends AbstractTest {

    @Test
    public void testFiltered() {
        NavigableSet<String> result = new DefaultMessageProcessor(consoleLog(), processorConf("^\\(.*$"))
                .getNewMessages(newMessagesUnfilteredStream(), ChangelogExecutor.filters, ChangelogExecutor.transformers);

        Assert.assertEquals(newMessagesFiltered(), result);
    }

    @Test
    public void testTrim() {
        NavigableSet<String> result = new DefaultMessageProcessor(consoleLog(), processorConf(".*"))
                .getNewMessages(s(" test ").stream(), ChangelogExecutor.filters, ChangelogExecutor.transformers);

        Assert.assertEquals(s("test"), result);
    }

    @Test
    public void testCommitFormat() {
        NavigableSet<String> result = new DefaultMessageProcessor(consoleLog(), processorConf(".*").setCommitFormat("my-%s-format"))
                .getNewMessages(s(" test ").stream(), ChangelogExecutor.filters, ChangelogExecutor.transformers);

        Assert.assertEquals(s("my-test-format"), result);
    }

    private Collection<String> s(String s) {
        return new TreeSet<>(Collections.singletonList(s));
    }

    private ChangelogConfiguration processorConf(String applicableCommitPattern) {
        return new ChangelogConfiguration()
                .setCommitFormat("%s")
                .setApplicableCommitPattern(applicableCommitPattern);
    }


}
