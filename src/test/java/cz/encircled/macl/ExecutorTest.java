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

    @Before
    public void before() {
        File file = new File(path());
        if (file.exists()) {
            file.delete();
        }

        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Files.write(Paths.get(path()), changelog_A(newMessagesUnfiltered()), Charset.forName("UTF-8"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void endToEndTest() {
        Assume.assumeTrue(new File(path()).canWrite());

        executor(
                new ChangelogConfiguration()
                        .setMergeRequestReplacePattern("(\\])")
                        .setMergeRequestReplacement(" MR#$1)")
                        .setCommitFormat(".*")
                        .setLastTagPattern(".*\\[([\\d\\.]+)].*")
                        .setLastTagFormat("%s")
                        .setPathToChangelog(path())
                        .setUnreleasedRowPattern("## \\[Unreleased]")
                        .setApplicableCommitPattern(".*\\(.*")
        ).run(consoleLog());
    }

    private String path() {
        return System.getProperty("java.io.tmpdir") + "test-changelog.md";
    }

}
