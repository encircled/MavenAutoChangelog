package cz.encircled.macl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

/**
 * @author Kisel on 22.6.2017.
 */
public class ChangelogConfiguration {

    public Path pathToChangelog;

    public Pattern lastTagPattern;

    public Pattern applicableCommitPattern;

    public String commitFormat;

    public Pattern unreleasedRowPattern;

    public String lastTagFormat;

    public ChangelogConfiguration setCommitFormat(String commitFormat) {
        this.commitFormat = commitFormat;
        return this;
    }

    public ChangelogConfiguration setLastTagFormat(String lastTagFormat) {
        this.lastTagFormat = lastTagFormat;
        return this;
    }

    public ChangelogConfiguration setPathToChangelog(String pathToChangelog) {
        this.pathToChangelog = Paths.get(pathToChangelog);
        return this;
    }

    public ChangelogConfiguration setLastTagPattern(String lastTagPattern) {
        this.lastTagPattern = Pattern.compile(lastTagPattern);
        return this;
    }

    public ChangelogConfiguration setApplicableCommitPattern(String applicableCommitPattern) {
        this.applicableCommitPattern = Pattern.compile(applicableCommitPattern);
        return this;
    }

    public ChangelogConfiguration setUnreleasedRowPattern(String unreleasedRowPattern) {
        this.unreleasedRowPattern = Pattern.compile(unreleasedRowPattern);
        return this;
    }

}
