package cz.encircled.macl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.maven.plugin.logging.Log;

/**
 * @author Kisel on 22.6.2017.
 */
public class GitLogParser implements VCSLogParser {

    private final ChangelogConfiguration conf;

    private final static String command = "git log %s..HEAD";

    public GitLogParser(ChangelogConfiguration conf) {
        this.conf = conf;
    }

    @Override
    public NavigableSet<String> getNewMessages(Log log, String tagFrom) throws Exception {
        String command = String.format(GitLogParser.command, tagFrom);
        Process p = Runtime.getRuntime().exec(command);
        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
        List<String> result = input.lines()
                .map(String::trim)
                .filter(l -> {
                    boolean matches = conf.applicableCommitPattern.matcher(l).matches();
                    if (!matches) {
                        log.info("Message [" + l + "] skipped");
                    }
                    return matches;
                })
                .map(s -> String.format(conf.commitFormat, s))
                .collect(Collectors.toList());

        if (!result.isEmpty()) {
            result.set(0, "\r\n" + result.get(0));
        }

        log.info(String.format("Executed command [%s] returned [%d] rows", command, result.size()));

        return new TreeSet<>(result);
    }

}
