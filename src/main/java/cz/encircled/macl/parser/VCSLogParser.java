package cz.encircled.macl.parser;

import org.apache.maven.plugin.logging.Log;

import java.util.NavigableSet;

/**
 * @author Kisel on 22.6.2017.
 */
public interface VCSLogParser {

    NavigableSet<String> getNewMessages(Log log, String tagFrom);

}
