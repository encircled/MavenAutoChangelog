package cz.encircled.macl.transform;

import cz.encircled.macl.parser.ParsingState;

/**
 * @author Kisel on 27.10.2017.
 */
public interface MessageTransformer {

    String transform(String needle, ParsingState state);

}
