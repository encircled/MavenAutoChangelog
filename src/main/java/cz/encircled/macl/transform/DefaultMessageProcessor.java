package cz.encircled.macl.transform;

import cz.encircled.macl.ChangelogConfiguration;
import cz.encircled.macl.parser.ParsingState;
import org.apache.maven.plugin.logging.Log;

import java.util.*;

/**
 * @author Kisel on 27.10.2017.
 */
public class DefaultMessageProcessor implements MessageProcessor {

    private final ChangelogConfiguration conf;

    private final Log log;

    private final Collection<MessageFilter> filters;
    private final Collection<MessageTransformer> transformers;
    private final List<MessageModifier> modifiers;

    public DefaultMessageProcessor(Log log, ChangelogConfiguration conf, List<MessageFilter> filters, List<MessageTransformer> transformers,
            List<MessageModifier> modifiers) {
        this.conf = conf;
        this.log = log;
        this.filters = Collections.unmodifiableList(filters);
        this.transformers = Collections.unmodifiableList(transformers);
        this.modifiers = Collections.unmodifiableList(modifiers);
    }


    @Override
    public Set<String> getNewMessages(Collection<String> messages) {
        final List<String> result = new ArrayList<>();

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

            for (MessageModifier modifier : modifiers) {
                if (state.isPreviousAccepted && modifier.accept(m, state)) {
                    result.remove(result.size() - 1);
                    result.add(modifier.modify(m, state));
                }
            }


            if (state.isAccepted) {
                result.add(m);
                state.previousMatched = m;
            } else {
                log.info("Message [" + m + "] excluded");
            }
            state.isPreviousAccepted = state.isAccepted;
            state.isAccepted = false;
        });

        log.info(String.format("Count of new matched messages is %d", result.size()));

        return new LinkedHashSet<>(result);
    }
}
