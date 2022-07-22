package cz.encircled.macl;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * @author Vlad on 17-Nov-17.
 */
public class MojoTest extends AbstractTest {

    @Test
    public void mojoConfigTest() throws IOException {
        String applicableCommitPattern = "applicableCommitPattern";
        String commitFormat = "commitFormat";
        String lastTagFormat = "lastTagFormat";
        String lastTagPattern = "lastTagPattern ([\\d.])";
        String mergeRequestReplacement = "MR#";
        String mergeRequestReplacePattern = "mergeRequestReplacePattern";
        String pathToChangelog = File.createTempFile("changelog", "md").getPath();
        String unreleasedRowPattern = "unreleasedRowPattern";

        Files.write(Path.of(pathToChangelog), List.of("lastTagPattern 1"));

        ChangelogMojo mojo = new ChangelogMojo();
        mojo.applicableCommitPattern = applicableCommitPattern;
        mojo.commitFormat = commitFormat;
        mojo.lastTagFormat = lastTagFormat;
        mojo.lastTagPattern = lastTagPattern;
        mojo.mergeRequestReplacement = mergeRequestReplacement;
        mojo.mergeRequestReplacePattern = mergeRequestReplacePattern;
        mojo.pathToChangelog = pathToChangelog;
        mojo.unreleasedRowPattern = unreleasedRowPattern;

        ChangelogConfiguration result = mojo.getChangelogConfiguration();

        Assert.assertEquals(result.pathToChangelog.toString(), pathToChangelog);
        Assert.assertEquals(result.lastTagFormat, lastTagFormat);
        Assert.assertEquals(result.lastTagPattern.pattern(), lastTagPattern);
        Assert.assertEquals(result.mergeRequestReplacement, mergeRequestReplacement);
        Assert.assertEquals(result.mergeRequestReplacePattern.pattern(), mergeRequestReplacePattern);
        Assert.assertEquals(result.unreleasedRowPattern.pattern(), unreleasedRowPattern);

        // Should not fail
        mojo.execute();

        mojo.pathToChangelog = "not-exist";
        assertException(mojo::execute, "java.nio.file.NoSuchFileException: not-exist");
    }

}
