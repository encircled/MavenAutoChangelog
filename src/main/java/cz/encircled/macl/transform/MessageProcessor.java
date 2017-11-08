package cz.encircled.macl.transform;

import java.util.Collection;
import java.util.Set;

/**
 * @author Kisel on 27.10.2017.
 */
public interface MessageProcessor {

    Set<String> getNewMessages(Collection<String> messages);

}
