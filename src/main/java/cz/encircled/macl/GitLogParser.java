package cz.encircled.macl;

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
    public List<String> getNewMessages(String tagFrom) throws Exception {
        List<String> result = new ArrayList<>();

        Process p = Runtime.getRuntime().exec(String.format(command, tagFrom));
        new Thread(() -> {
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            result.addAll(input.lines()
                    .filter(l -> conf.applicableCommitPattern.matcher(l).matches())
                    .map(s -> String.format(conf.commitFormat, s.trim()))
                    .collect(Collectors.toList()));
        }).start();

        p.waitFor();

        return result;
    }

}
