import lang.DashLang;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class InterpreterTester {
    protected final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    protected final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @BeforeAll
    public static void setupAll() {
        DashLang.debug = false;
        DashLang.exitOnFailure = false;
    }

    @BeforeEach
    public void setup() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    public void teardown() {
        DashLang.resetErrors();
        System.setOut(originalOut);
        System.setErr(originalErr);
    }
}
