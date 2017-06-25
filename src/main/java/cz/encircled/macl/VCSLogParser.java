package cz.encircled.macl;

import org.apache.maven.plugin.logging.Log;

import java.util.List;

/**
 * @author Kisel on 22.6.2017.
 */
public interface VCSLogParser {

    List<String> getNewMessages(Log log, String tagFrom) throws Exception;

}
