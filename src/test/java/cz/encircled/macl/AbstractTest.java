package cz.encircled.macl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.concurrent.Callable;

import cz.encircled.macl.transform.DefaultMessageProcessor;
import cz.encircled.macl.transform.GitLabMergeRequestModifier;
import cz.encircled.macl.transform.MessageProcessor;
import org.apache.maven.monitor.logging.DefaultLog;
import org.codehaus.plexus.logging.console.ConsoleLogger;
import org.junit.Assert;

/**
 * @author Kisel on 27.10.2017.
 */
public class AbstractTest {

    DefaultLog consoleLog() {
        return new DefaultLog(new ConsoleLogger(0, ""));
    }

    MessageProcessor defaultMessageProcessor(ChangelogConfiguration conf) {
        return new DefaultMessageProcessor(consoleLog(), conf, ChangelogMojo.filters, ChangelogMojo.transformers,
                Collections.singletonList(new GitLabMergeRequestModifier(conf)));
    }

    void assertException(Callable<?> callable, String message) {
        try {
            callable.call();
        } catch (Exception e) {
            Assert.assertEquals(e.getMessage(), message);
            return;
        }
        Assert.fail("Exception [" + message + "] is expected!");
    }

    ChangelogExecutor executor(ChangelogConfiguration conf) {
        return new ChangelogExecutor(conf, (log, tagFrom) -> newMessagesFiltered(), (messages) -> newMessagesFiltered());
    }

    Set<String> newMessagesFiltered() {
        return new LinkedHashSet<>(Arrays.asList("(ABC-123 123!) New message 1", "(XYZ-321 777!) New message 2"));
    }

    Set<String> newMessagesUnfiltered() {
        return new LinkedHashSet<>(Arrays.asList("(ABC-123) New message 1", "See merge request 123!",
                "(XYZ-321) New message 2", "See merge request 777!", "Noise commit"));
    }

    List<String> changelog_A() {
        return changelog_A(Collections.emptyList());
    }

    List<String> changelog_A(Collection<String> newMessages) {
        ArrayList<String> result = new ArrayList<>(Arrays.asList(
                "This is my changelog!",
                "Some description...",
                "",
                "## [Unreleased]"
        ));
        result.addAll(newMessages);
        result.addAll(Arrays.asList(
                "- (MY-777) new feature",
                "",
                "## [2017.03] - 2017.03.02",
                "- Test commit",
                "",
                "## [2017.02] - 2017.02.05",
                "- Documentation update",
                "",
                "## [2017.01] - 2017.01.01",
                "- Performance fix",
                ""
        ));

        return result;
    }


}
