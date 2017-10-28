package cz.encircled.macl.transform;

import cz.encircled.macl.ChangelogConfiguration;
import cz.encircled.macl.parser.ParsingState;

/**
 * @author Vlad on 27-Oct-17.
 */
public class GitLabMergeRequestModifier implements MessageModifier {

    public static final String MR_TOKEN = "MR";

    private final ChangelogConfiguration conf;

    public GitLabMergeRequestModifier(ChangelogConfiguration conf) {
        this.conf = conf;
    }

    @Override
    public boolean accept(String currentLine, ParsingState state) {
        return conf.mergeRequestReplacePattern != null && currentLine.startsWith("See merge request ");
    }

    @Override
    public String modify(String currentLine, ParsingState state) {
        String mergeRequest = currentLine.substring(currentLine.lastIndexOf(" ") + 1);
        String replacement = conf.mergeRequestReplacement.replace(MR_TOKEN, mergeRequest);
        return conf.mergeRequestReplacePattern.matcher(state.previousMatched).replaceFirst(replacement);
    }

}
