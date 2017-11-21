package cz.encircled.macl.parser;

import cz.encircled.macl.CommandExecutor;
import org.apache.maven.plugin.logging.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Kisel on 22.6.2017.
 */
public class GitLogParser implements VCSLogParser {

    private final CommandExecutor commandExecutor;

    private final static String command = "git log %s..HEAD";

    public GitLogParser(CommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
    }

    @Override
    public Set<String> getNewMessages(Log log, String tagFrom) {
        String command = String.format(GitLogParser.command, tagFrom);
        log.info(String.format("GitParser executing command [%s]", command));

        try (BufferedReader input = new BufferedReader(new InputStreamReader(commandExecutor.exec(command)))) {
            return input.lines().collect(Collectors.toCollection(LinkedHashSet::new));
        } catch (IOException e) {
            throw new RuntimeException("Unable to read changelog file", e);
        }
    }

}
