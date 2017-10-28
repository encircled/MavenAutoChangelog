package cz.encircled.macl.transform;

import cz.encircled.macl.parser.ParsingState;

/**
 * @author Vlad on 27-Oct-17.
 */
public interface MessageModifier {

    boolean accept(String currentLine, ParsingState state);

    String modify(String currentLine, ParsingState state);

}
