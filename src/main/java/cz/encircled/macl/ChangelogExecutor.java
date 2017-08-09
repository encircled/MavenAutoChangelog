package cz.encircled.macl;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NavigableSet;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import org.apache.maven.plugin.logging.Log;

/**
 * @author Kisel on 22.6.2017.
 */
public class ChangelogExecutor {

    private final ChangelogConfiguration conf;

    private final VCSLogParser vcsLogParser;

    public ChangelogExecutor(ChangelogConfiguration conf, VCSLogParser vcsLogParser) {
        this.conf = conf;
        this.vcsLogParser = vcsLogParser;
    }

    public void run(Log log) {
        try {
            log.debug("Changelog file: " + conf.pathToChangelog);
            log.info("New messages pattern: " + conf.applicableCommitPattern.pattern());

            List<String> allLines = Files.lines(conf.pathToChangelog).map(String::trim).collect(Collectors.toList());

            String lastTag = getLastTag(allLines);
            log.info("Last tag: " + lastTag);

            NavigableSet<String> newMessages = vcsLogParser.getNewMessages(log, lastTag);
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
        resultLines.addAll(allLines.subList(0, unreleasedIndex + 1));
        resultLines.addAll(newMessages);
        resultLines.addAll(allLines.subList(unreleasedIndex + 2, allLines.size()));
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
