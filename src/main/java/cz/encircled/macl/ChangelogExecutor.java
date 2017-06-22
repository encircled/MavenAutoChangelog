package cz.encircled.macl;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import org.apache.maven.plugin.logging.Log;

/**
 * @author Kisel on 22.6.2017.
 */
public class ChangelogExecutor {

    private final ChangelogConfiguration conf;

    public ChangelogExecutor(ChangelogConfiguration conf) {
        this.conf = conf;
    }

    public void run(Log log) {
        try {
            log.debug("Changelog file: " + conf.pathToChangelog);

            List<String> allLines = Files.readAllLines(conf.pathToChangelog);
            String lastTag = getLastTag(allLines);
            List<String> newMessages = new GitLogParser(conf).getNewMessages(lastTag);

            if (!newMessages.isEmpty()) {
                newMessages.set(0, "\r\n" + newMessages.get(0));
            }

            log.debug("Last tag: " + lastTag);

            int unreleasedIndex = getIndexOfUnreleasedLine(allLines);
            log.debug("Index of 'Unreleased' line: " + unreleasedIndex);

            List<String> resultLines = new ArrayList<>(allLines.size() + newMessages.size());
            resultLines.addAll(allLines.subList(0, unreleasedIndex + 1));
            resultLines.addAll(newMessages);

            resultLines.addAll(allLines.subList(unreleasedIndex + 2, allLines.size()));

            Files.write(conf.pathToChangelog, resultLines);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private String getLastTag(List<String> allLines) {
        for (String line : allLines) {
            Matcher matcher = conf.lastTagPattern.matcher(line.trim());
            if (matcher.matches()) {
                return String.format(conf.lastTagFormat, matcher.group(1));
            }
        }

        throw new IllegalStateException("Last tag not found for pattern " + conf.lastTagPattern.pattern());
    }

    private int getIndexOfUnreleasedLine(List<String> allLines) {
        for (int i = 0; i < allLines.size(); i++) {
            if (conf.unreleasedRowPattern.matcher(allLines.get(i).trim()).matches()) {
                return i;
            }
        }
        throw new IllegalStateException("Row with unreleased changes was not found for pattern " + conf.unreleasedRowPattern.pattern());
    }

}
