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
     * Regexp pattern which should be used to match line with latest release version (tag)
     */
    @Parameter(required = true)
    protected String lastTagPattern;

    /**
     * Format (for java <code>String.format(lastTagFormat, lastTagPattern)</code>) which can be used to customize git tag if differs from <code>lastTagPattern</code>
     */
    @Parameter(defaultValue = "%s")
    protected String lastTagFormat;

    /**
     * Regexp pattern which is used to filter unwanted commits, i.e. only commits which match this regexp will be included into changelog
     */
    @Parameter(required = true)
    protected String applicableCommitPattern;

    /**
     * Additional java <code>String.format(commitMessage, commitFormat)</code> which can be used to customize changelog entry
     */
    @Parameter(defaultValue = "%s")
    protected String commitFormat;

    /**
     * Regexp pattern which should be used to match line with 'Unreleased' token
     */
    @Parameter(required = true)
    protected String unreleasedRowPattern;

    public void execute() throws MojoExecutionException, MojoFailureException {
        new ChangelogExecutor(new ChangelogConfiguration()
                .setApplicableCommitPattern(applicableCommitPattern)
                .setLastTagPattern(lastTagPattern)
                .setPathToChangelog(pathToChangelog)
                .setUnreleasedRowPattern(unreleasedRowPattern)
                .setLastTagFormat(lastTagFormat)
                .setCommitFormat(commitFormat)
        ).run(getLog());
    }

}
