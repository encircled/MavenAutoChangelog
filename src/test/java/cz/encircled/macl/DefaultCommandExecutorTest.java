package cz.encircled.macl;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * @author Vlad on 17-Nov-17.
 */
public class DefaultCommandExecutorTest {

    @Test
    public void testDefaultCommandExecutor() throws IOException {
        InputStream exec = new DefaultCommandExecutor().exec("git");
        Scanner scanner = new Scanner(exec, "utf-8");

        Assert.assertTrue(scanner.hasNext());
        Assert.assertTrue(scanner.next().startsWith("usage:"));
    }

}
