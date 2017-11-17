package cz.encircled.macl;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Vlad on 17-Nov-17.
 */
public class DefaultCommandExecutor implements CommandExecutor {

    @Override
    public InputStream exec(String command) throws IOException {
        return Runtime.getRuntime().exec(command).getInputStream();
    }

}
