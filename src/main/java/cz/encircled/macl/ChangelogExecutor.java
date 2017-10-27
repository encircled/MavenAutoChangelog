package cz.encircled.macl;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.NavigableSet;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import cz.encircled.macl.parser.VCSLogParser;
import cz.encircled.macl.transform.MessageFilter;
import cz.encircled.macl.transform.MessageProcessor;
import cz.encircled.macl.transform.MessageTransformer;
import org.apache.maven.plugin.logging.Log;

/**
 * @author Kisel on 22.6.2017.
 */
public class ChangelogExecutor {

    public static final List<MessageFilter> filters = Arrays.asList(
            (MessageFilter) (needle, state) -> state.conf.applicableCommitPattern.matcher(needle).matches()
    );

    private final ChangelogConfiguration conf;

    private final VCSLogParser vcsLogParser;
    private static final String NEW_LINE = "\r\n";
    public static final List<MessageTransformer> transformers = Arrays.asList(
            (MessageTransformer) (needle, state) -> state.previousMatched == null ? NEW_LINE + needle : needle,
            (MessageTransformer) (needle, state) -> needle.trim(),
            (MessageTransformer) (needle, state) -> String.format(state.conf.commitFormat, needle),
            (MessageTransformer) (needle, state) -> {
//                TODO "See merge request !1254"
                return needle;
            }
    );
    private final MessageProcessor messageProcessor;

    public ChangelogExecutor(ChangelogConfiguration conf, VCSLogParser vcsLogParser, MessageProcessor messageProcessor) {
        this.conf = conf;
        this.vcsLogParser = vcsLogParser;
        this.messageProcessor = messageProcessor;
    }

    public void run(Log log) {
        try {
            log.debug("Changelog file: " + conf.pathToChangelog);
            log.info("New messages pattern: " + conf.applicableCommitPattern.pattern());

            List<String> allLines = Files.lines(conf.pathToChangelog).map(String::trim).collect(Collectors.toList());

            String lastTag = getLastTag(allLines);
            log.info("Last tag: " + lastTag);

            NavigableSet<String> newMessages = messageProcessor.getNewMessages(vcsLogParser.getNewMessages(log, lastTag), filters, transformers);
            if (newMessages.isEmpty()) {
                log.info("No new messages");
                return;
            }

            int unreleasedIndex = getIndexOfUnreleasedLine(allLines);

            log.info("Count of new messages: " + newMessages.size());
            log.debug("Index of 'Unreleased' line: " + unreleasedIndex);

            List<String> resultLines = insertNewMessages(allLines, newMessages, unreleasedIndex);

            Files.write(conf.pathToChangelog, resultLines);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public List<String> insertNewMessages(List<String> allLines, Collection<String> newMessages, int unreleasedIndex) {
        List<String> resultLines = new ArrayList<>(allLines.size() + newMessages.size());
        int afterUnreleased = unreleasedIndex + 1;
        resultLines.addAll(allLines.subList(0, afterUnreleased));
        resultLines.addAll(newMessages);

        int fromIndex = allLines.get(afterUnreleased).isEmpty() ? unreleasedIndex + 2 : afterUnreleased;
        resultLines.addAll(allLines.subList(fromIndex, allLines.size()));
        return resultLines;
    }

    public String getLastTag(List<String> allLines) {
        if (conf.lastTag != null) {
            return conf.lastTag;
        }

        for (String line : allLines) {
            Matcher matcher = conf.lastTagPattern.matcher(line);
            if (matcher.matches()) {
                return String.format(conf.lastTagFormat, matcher.group(1));
            }
        }

        throw new IllegalStateException("Last tag not found for pattern " + conf.lastTagPattern.pattern());
    }

    public int getIndexOfUnreleasedLine(List<String> allLines) {
        for (int i = 0; i < allLines.size(); i++) {
            if (conf.unreleasedRowPattern.matcher(allLines.get(i)).matches()) {
                return i;
            }
        }
        throw new IllegalStateException("Row with unreleased changes was not found for pattern " + conf.unreleasedRowPattern.pattern());
    }

}
