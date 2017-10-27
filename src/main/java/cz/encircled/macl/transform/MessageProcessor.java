package cz.encircled.macl.transform;

import java.util.Collection;
import java.util.NavigableSet;
import java.util.stream.Stream;

/**
 * @author Kisel on 27.10.2017.
 */
public interface MessageProcessor {

    NavigableSet<String> getNewMessages(Stream<String> messages, Collection<MessageFilter> filters, Collection<MessageTransformer> transformers);

}
