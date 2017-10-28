package cz.encircled.macl;

import cz.encircled.macl.transform.GitLabMergeRequestModifier;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

/**
 * @author Kisel on 22.6.2017.
 */
public class ChangelogConfiguration {

    public Path pathToChangelog;

    public String lastTag;

    public Pattern lastTagPattern;

    public Pattern applicableCommitPattern;

    public String commitFormat;

    public Pattern unreleasedRowPattern;

    public Pattern mergeRequestReplacePattern;

    public String mergeRequestReplacement;

    public String lastTagFormat;

    public ChangelogConfiguration valid() {
        if (isEmpty(lastTagFormat) && isEmpty(lastTag)) {
            throw new IllegalStateException("LastTagFormat or lastTag must be specified");
        }
        if (!isEmpty(lastTagFormat) && !isEmpty(lastTag)) {
            throw new IllegalStateException("Only one of [lastTagFormat, lastTag] might be present");
        }
        if (mergeRequestReplacement == null || !mergeRequestReplacement.contains(GitLabMergeRequestModifier.MR_TOKEN)) {
            throw new IllegalStateException("mergeRequestReplacement must contain token " + GitLabMergeRequestModifier.MR_TOKEN);
        }
        return this;
    }

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
        if (!isEmpty(lastTagPattern)) {
            this.lastTagPattern = Pattern.compile(lastTagPattern);
        }
        return this;
    }

    public ChangelogConfiguration setMergeRequestReplacePattern(String pattern) {
        if (!isEmpty(pattern)) {
            this.mergeRequestReplacePattern = Pattern.compile(pattern);
        }
        return this;
    }

    public ChangelogConfiguration setMergeRequestReplacement(String pattern) {
        this.mergeRequestReplacement = pattern;
        return this;
    }

    public ChangelogConfiguration setLastTag(String lastTag) {
        this.lastTag = lastTag;
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

    boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }

}
