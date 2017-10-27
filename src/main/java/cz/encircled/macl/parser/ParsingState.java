package cz.encircled.macl.parser;

import cz.encircled.macl.ChangelogConfiguration;

/**
 * @author Kisel on 27.10.2017.
 */
public class ParsingState {

    public ChangelogConfiguration conf;

    public String previousMatched;

    public Boolean isAccepted;

    public ParsingState(ChangelogConfiguration conf) {
        this.conf = conf;
        this.isAccepted = false;
    }

}
