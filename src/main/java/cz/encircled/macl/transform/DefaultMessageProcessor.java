package cz.encircled.macl.transform;

import java.util.Collection;
import java.util.NavigableSet;
import java.util.TreeSet;
import java.util.stream.Stream;

import cz.encircled.macl.ChangelogConfiguration;
import cz.encircled.macl.parser.ParsingState;
import org.apache.maven.plugin.logging.Log;

/**
 * @author Kisel on 27.10.2017.
 */
public class DefaultMessageProcessor implements MessageProcessor {

    private final ChangelogConfiguration conf;

    private final Log log;

    public DefaultMessageProcessor(Log log, ChangelogConfiguration conf) {
        this.conf = conf;
        this.log = log;
    }

    @Override
    public NavigableSet<String> getNewMessages(Stream<String> messages, Collection<MessageFilter> filters, Collection<MessageTransformer> transformers) {
        final TreeSet<String> result = new TreeSet<>();
        ParsingState state = new ParsingState(conf);

        messages.forEach(m -> {
            for (MessageFilter filter : filters) {
                boolean accept = filter.accept(m, state);
                if (accept) {
                    state.isAccepted = true;
                    break;
                }
            }

            for (MessageTransformer transformer : transformers) {
                m = transformer.transform(m, state);
            }

            if (state.isAccepted) {
                result.add(m);
                state.previousMatched = m;
            } else {
                log.info("Message [" + m + "] excluded");
            }
            state.isAccepted = false;
        });

        log.info(String.format("Count of new matched messages is %d", result.size()));

        return result;
    }
}
