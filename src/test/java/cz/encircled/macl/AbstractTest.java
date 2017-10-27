package cz.encircled.macl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

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
        return new ChangelogExecutor(conf, (log, tagFrom) -> newMessagesFilteredStream(), (messages, filters, transformers) -> newMessagesFiltered());
    }

    NavigableSet<String> newMessagesFiltered() {
        return new TreeSet<>(Arrays.asList("(ABC-123) New message 1", "(XYZ-321) New message 2"));
    }

    Stream<String> newMessagesFilteredStream() {
        return newMessagesFiltered().stream();
    }

    Stream<String> newMessagesUnfilteredStream() {
        NavigableSet<String> strings = newMessagesFiltered();
        strings.addAll(Arrays.asList("Invalid", "Noise commit"));
        return strings.stream();
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
