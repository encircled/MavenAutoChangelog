package cz.encircled.macl.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.maven.plugin.logging.Log;

/**
 * @author Kisel on 22.6.2017.
 */
public class GitLogParser implements VCSLogParser {

    private final static String command = "git log %s..HEAD";

    @Override
    public Set<String> getNewMessages(Log log, String tagFrom) {
        String command = String.format(GitLogParser.command, tagFrom);
        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            log.info(String.format("GitParser executed command [%s]", command));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            return input.lines().collect(Collectors.toCollection(LinkedHashSet::new));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
