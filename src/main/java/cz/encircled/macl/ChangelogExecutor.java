package cz.encircled.macl;

import cz.encircled.macl.parser.VCSLogParser;
import cz.encircled.macl.transform.MessageProcessor;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.maven.plugin.logging.Log;

import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

/**
 * @author Kisel on 22.6.2017.
 */
public class ChangelogExecutor {

    private final ChangelogConfiguration conf;

    private final VCSLogParser vcsLogParser;


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

            Pair<String, Integer> lastTag = getLastTag(allLines);
            log.info("Last tag: " + lastTag);

            Set<String> newMessages = messageProcessor.getNewMessages(vcsLogParser.getNewMessages(log, lastTag.getLeft()));
            if (newMessages.isEmpty()) {
                log.info("No new messages");
                return;
            }

            int unreleasedIndex = getIndexOfUnreleasedLine(allLines);

            log.info("Count of new messages: " + newMessages.size());
            log.debug("Index of 'Unreleased' line: " + unreleasedIndex);

            List<String> resultLines = insertNewMessages(allLines, newMessages, unreleasedIndex, lastTag.getRight());

//            try (BufferedWriter writer = Files.newBufferedWriter(conf.pathToChangelog,
//                    StandardCharsets.UTF_8, StandardOpenOption.APPEND)) {
//                for (String line : resultLines) {
//                    writer.write(line);
//                    writer.write('\n');
//                }
//                writer.flush();
//            }
            Files.write(conf.pathToChangelog, resultLines);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public List<String> insertNewMessages(List<String> allLines, Collection<String> newMessages, int unreleasedIndex, Integer lastTagIndex) {
        int afterUnreleased = unreleasedIndex + 1;
        List<String> linesToInsert = new ArrayList<>();

        // append new messages skipping duplicates
        List<String> present = new ArrayList<>(allLines.subList(afterUnreleased, lastTagIndex));
        newMessages.stream().filter(m -> !present.contains(m)).forEach(linesToInsert::add);

        // append messages which are present in 'Unresolved' already
        // Preserve leading empty line if present
        if (present.get(0).isEmpty()) {
            linesToInsert.add(0, "");
            present.remove(0);
        }
        linesToInsert.addAll(present);

        List<String> result = new ArrayList<>(allLines.subList(0, afterUnreleased));
        result.addAll(linesToInsert);
        result.addAll(allLines.subList(lastTagIndex, allLines.size()));

        return result;
    }

    public Pair<String, Integer> getLastTag(List<String> allLines) {
        if (conf.lastTag != null) {
            return Pair.of(conf.lastTag, null);
        }

        for (int i = 0; i < allLines.size(); i++) {
            Matcher matcher = conf.lastTagPattern.matcher(allLines.get(i));
            if (matcher.matches()) {
                return Pair.of(String.format(conf.lastTagFormat, matcher.group(1)), i);
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
