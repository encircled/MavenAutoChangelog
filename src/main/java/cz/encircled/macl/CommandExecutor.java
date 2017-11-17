package cz.encircled.macl;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Vlad on 17-Nov-17.
 */
public interface CommandExecutor {

    InputStream exec(String command) throws IOException;

}
