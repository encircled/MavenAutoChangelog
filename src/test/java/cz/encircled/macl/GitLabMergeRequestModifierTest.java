package cz.encircled.macl;

import cz.encircled.macl.parser.ParsingState;
import cz.encircled.macl.transform.GitLabMergeRequestModifier;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Vlad on 27-Oct-17.
 */
public class GitLabMergeRequestModifierTest {

    @Test
    public void testAcceptNull() {
        GitLabMergeRequestModifier modifier = new GitLabMergeRequestModifier(new ChangelogConfiguration());
        Assert.assertFalse(modifier.accept("- See merge request 123!", state()));
    }

    @Test
    public void testAccept() {
        GitLabMergeRequestModifier modifier = new GitLabMergeRequestModifier(conf());

        Assert.assertTrue(modifier.accept("- See merge request 123!", state()));
        Assert.assertTrue(modifier.accept("See merge request 1!", state()));
        Assert.assertFalse(modifier.accept("(123) Commit", state()));
        Assert.assertFalse(modifier.accept("Test commit", state()));
    }

    @Test
    public void testModify() {
        GitLabMergeRequestModifier modifier = new GitLabMergeRequestModifier(conf());
        String result = modifier.modify("- See merge request 125!", state());

        Assert.assertEquals("[Test 125!] commit", result);
    }

    @Test
    public void testReplacePatternMissing() {
        GitLabMergeRequestModifier modifier = new GitLabMergeRequestModifier(conf());
        ParsingState state = state();
        state.previousMatched = "(Test) commit";
        String result = modifier.modify("See merge request 123!", state);

        Assert.assertEquals("(Test) commit", result);
    }

    public ParsingState state() {
        ParsingState state = new ParsingState(conf());
        state.previousMatched = "[Test] commit";
        return state;
    }

    private ChangelogConfiguration conf() {
        return new ChangelogConfiguration()
                .setMergeRequestReplacement(" MR#$1")
                .setMergeRequestReplacePattern("(])");
    }

}
