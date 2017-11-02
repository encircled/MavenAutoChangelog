package cz.encircled.macl.transform;

import java.util.NavigableSet;

/**
 * @author Kisel on 27.10.2017.
 */
public interface MessageProcessor {

    NavigableSet<String> getNewMessages(NavigableSet<String> messages);

}
