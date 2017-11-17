package cz.encircled.macl;

import cz.encircled.macl.parser.GitLogParser;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Vlad on 17-Nov-17.
 */
public class GitLogParserTest extends AbstractTest {

    @Test
    public void testGitLogParser() {
        Set<String> result = new GitLogParser(command -> {
            if (command.equals("git log 1.0..HEAD")) {
                return new ByteArrayInputStream("(XYZ-123) test commit".getBytes(StandardCharsets.UTF_8.name()));
            } else {
                return null;
            }
        }).getNewMessages(consoleLog(), "1.0");

        Assert.assertEquals(new LinkedHashSet<>(Collections.singletonList("(XYZ-123) test commit")), result);
    }

}
