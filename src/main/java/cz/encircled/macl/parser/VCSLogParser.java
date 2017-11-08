package cz.encircled.macl.parser;

import java.util.Set;

import org.apache.maven.plugin.logging.Log;

/**
 * @author Kisel on 22.6.2017.
 */
public interface VCSLogParser {

    Set<String> getNewMessages(Log log, String tagFrom);

}
