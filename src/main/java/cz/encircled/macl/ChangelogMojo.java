package cz.encircled.macl;

import cz.encircled.macl.parser.GitLogParser;
import cz.encircled.macl.transform.DefaultMessageProcessor;
import cz.encircled.macl.transform.GitLabMergeRequestModifier;
import cz.encircled.macl.transform.MessageFilter;
import cz.encircled.macl.transform.MessageTransformer;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Kisel on 22.6.2017.
 */
@Mojo(name = "generate-changelog", defaultPhase = LifecyclePhase.GENERATE_RESOURCES, aggregator = true)
public class ChangelogMojo extends AbstractMojo {

    private static final String NEW_LINE = System.getProperty("line.separator");

    /**
     * Path to changelog file
     */
    @Parameter(defaultValue = "CHANGELOG.md")
    protected String pathToChangelog;

    /**
     * Predefined git tag which is used for comparing. Alternatively, <code>lastTagPattern</code> may be used to parse latest tag dynamically.
     */
    @Parameter
    protected String lastTag;

    /**
     * Regex pattern which is used to match line with latest release version (tag). Alternatively, <code>lastTag</code> may be used if tag is predefined.
     */
    @Parameter
    protected String lastTagPattern;

    /**
     * Defines format (for java <code>String.format(lastTagFormat, lastTagPattern)</code>) which is used to customize git tag if differs from <code>lastTagPattern</code>
     */
    @Parameter(defaultValue = "%s")
    protected String lastTagFormat;

    /**
     * Regex pattern which is used to filter unwanted commits, i.e. only commits which match this regex will be included into changelog
     */
    @Parameter(required = true)
    protected String applicableCommitPattern;

    /**
     * Additional java <code>String.format(commitMessage, commitFormat)</code> which is used to customize changelog entry
     */
    @Parameter(defaultValue = "%s")
    protected String commitFormat;

    /**
     * Regex pattern which is used to match line with 'Unreleased' token
     */
    @Parameter(required = true)
    protected String unreleasedRowPattern;

    /**
     * Regex pattern which is used to add merge request numbers to the messages. It must match the group which will be referenced in <code>mergeRequestReplacement</code>.
     * For example "(])" to add merge requests before first "]" like <code>[ABC-123] Text  -&gt;  [ABC-123 321!] Text</code>.
     */
    @Parameter
    protected String mergeRequestReplacePattern;

    /**
     * Replacement string which will be used with <code>mergeRequestReplacePattern</code>. Must contain token "MR#", which will be replaced by merge request number
     */
    @Parameter(defaultValue = " MR#$1")
    protected String mergeRequestReplacement;

    /**
     * If true, add line with auto-incremented version after changelog generation
     */
    @Parameter(defaultValue = "false")
    protected String incrementVersionAfterRun;

    public static final List<MessageFilter> filters = Collections.singletonList(
            (MessageFilter) (needle, state) -> state.conf.applicableCommitPattern.matcher(needle).matches()
    );

    public static final List<MessageTransformer> transformers = Arrays.asList(
            (MessageTransformer) (needle, state) -> state.previousMatched == null ? NEW_LINE + needle : needle,
            (MessageTransformer) (needle, state) -> needle.trim(),
            (MessageTransformer) (needle, state) -> String.format(state.conf.commitFormat, needle)
    );

    public void execute() throws MojoExecutionException, MojoFailureException {
        ChangelogConfiguration conf = getChangelogConfiguration();

        DefaultMessageProcessor messageProcessor = new DefaultMessageProcessor(getLog(), conf, filters, transformers, Collections.singletonList(new GitLabMergeRequestModifier(conf)));
        new ChangelogExecutor(conf, new GitLogParser(new DefaultCommandExecutor()), messageProcessor).run(getLog());
    }

    public ChangelogConfiguration getChangelogConfiguration() {
        return new ChangelogConfiguration()
                .setApplicableCommitPattern(applicableCommitPattern)
                .setLastTagPattern(lastTagPattern)
                .setPathToChangelog(pathToChangelog)
                .setUnreleasedRowPattern(unreleasedRowPattern)
                .setLastTagFormat(lastTagFormat)
                .setLastTag(lastTag)
                .setCommitFormat(commitFormat)
                .setMergeRequestReplacePattern(mergeRequestReplacePattern)
                .setMergeRequestReplacement(mergeRequestReplacement)
                .setIncrementVersionAfterRun(incrementVersionAfterRun)
                .valid();
    }

}
