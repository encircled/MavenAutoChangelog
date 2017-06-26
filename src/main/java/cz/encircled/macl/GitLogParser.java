package cz.encircled.macl;

import org.apache.maven.plugin.logging.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<String> getNewMessages(Log log, String tagFrom) throws Exception {
        List<String> result = new ArrayList<>();

        String command = String.format(GitLogParser.command, tagFrom);
        Process p = Runtime.getRuntime().exec(command);
        new Thread(() -> {
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            result.addAll(input.lines()
                    .map(String::trim)
                    .filter(l -> {
                        boolean matches = conf.applicableCommitPattern.matcher(l).matches();
                        if (matches) {
                            log.info("Message [" + l + "] included");
                        } else {
                            log.info("Message [" + l + "] skipped");
                        }
                        return matches;
                    })
                    .map(s -> String.format(conf.commitFormat, s))
                    .collect(Collectors.toList()));
        }).start();

        p.waitFor();

        log.info(String.format("Executed command [%s] returned [%d] rows", command, result.size()));

        return result;
    }

}
