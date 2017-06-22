package cz.encircled.macl;

import java.util.List;

/**
 * @author Kisel on 22.6.2017.
 */
public interface VCSLogParser {

    List<String> getNewMessages(String tagFrom) throws Exception;

}
