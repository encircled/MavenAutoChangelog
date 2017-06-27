package cz.encircled.macl;

import java.util.NavigableSet;

import org.apache.maven.plugin.logging.Log;

/**
 * @author Kisel on 22.6.2017.
 */
public interface VCSLogParser {

    NavigableSet<String> getNewMessages(Log log, String tagFrom) throws Exception;

}
