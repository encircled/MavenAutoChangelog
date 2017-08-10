package cz.encircled.macl;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * @author Kisel on 22.6.2017.
 */
@Mojo(name = "generate-changelog", defaultPhase = LifecyclePhase.GENERATE_RESOURCES, aggregator = true)
public class ChangelogMojo extends AbstractMojo {

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
     *
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

    public void execute() throws MojoExecutionException, MojoFailureException {
        ChangelogConfiguration conf = new ChangelogConfiguration()
                .setApplicableCommitPattern(applicableCommitPattern)
                .setLastTagPattern(lastTagPattern)
                .setPathToChangelog(pathToChangelog)
                .setUnreleasedRowPattern(unreleasedRowPattern)
                .setLastTagFormat(lastTagFormat)
                .setLastTag(lastTag)
                .setCommitFormat(commitFormat)
                .valid();

        new ChangelogExecutor(conf, new GitLogParser(conf)).run(getLog());
    }

}
