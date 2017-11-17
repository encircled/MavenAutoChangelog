package cz.encircled.macl;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Vlad on 01-Nov-17.
 */
public class ExecutorTest extends AbstractTest {

    private String path;

    @Before
    public void before() {
        File file;
        try {
            file = File.createTempFile("test-changelog", "md");
            path = file.getPath();
            try {
                Files.write(Paths.get(path), changelog_A(newMessagesUnfiltered()), Charset.forName("UTF-8"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void endToEndTest() {
        Assume.assumeNotNull(path);

        executor(
                new ChangelogConfiguration()
                        .setMergeRequestReplacePattern("(\\])")
                        .setMergeRequestReplacement(" MR#$1)")
                        .setCommitFormat(".*")
                        .setLastTagPattern(".*\\[([\\d\\.]+)].*")
                        .setLastTagFormat("%s")
                        .setPathToChangelog(path)
                        .setUnreleasedRowPattern("## \\[Unreleased]")
                        .setApplicableCommitPattern(".*\\(.*")
        ).run(consoleLog());
    }

}
