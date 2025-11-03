import lang.DashLang;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PrintStatementTest extends InterpreterTester {
    @Test
    public void testPrintStringShouldPrintTheProvidedString() {
        DashLang.runString("print \"hello\";");
        assertFalse(DashLang.hasErrors());
        assertEquals("hello\n", outContent.toString());
    }

    @Test
    public void testPrintNumberShouldPrintTheProvidedNumberWithoutDotZeroSuffix() {
        DashLang.runString("print 10;");
        assertFalse(DashLang.hasErrors());
        assertEquals("10\n", outContent.toString());
    }

    @Test
    public void testPrintFloatingNumberShouldPrintTheProvidedNumber() {
        DashLang.runString("print 10.24;");
        assertFalse(DashLang.hasErrors());
        assertEquals("10.24\n", outContent.toString());
    }

    @Test
    public void testPrintBooleanShouldPrintTheProvidedBoolean() {
        DashLang.runString("""
        print true;
        print false;
        """);

        assertFalse(DashLang.hasErrors());
        assertEquals("""
        true
        false
        """, outContent.toString());
    }

    @Test
    public void testPrintNilShouldPrintNil() {
        DashLang.runString("print nil;");
        assertFalse(DashLang.hasErrors());
        assertEquals("nil\n", outContent.toString());
    }

    @Test
    public void testPrintExpressionShouldInterpretTheExpressionAndPrintTheResult() {
        DashLang.runString("print !((10 + -4) > 6);");
        assertFalse(DashLang.hasErrors());
        assertEquals("true\n", outContent.toString());
    }

    @Test
    public void testPrintVariableShouldPrintTheValueContainedInTheVariable() {
        DashLang.runString("""
        var x = 10;
        print x;
        """);
        assertFalse(DashLang.hasErrors());
        assertEquals("10\n", outContent.toString());
    }

    @Test
    public void testPrintWithoutAValueShouldDisplayAnErrorMessage() {
        DashLang.runString("print;");
        assertTrue(DashLang.hasErrors());
        assertTrue(errContent.toString().contains("Error at ';' Expect expression"));
    }

    @Test
    public void testPrintWithoutASemiColonShouldDisplayAnErrorMessage() {
        DashLang.runString("print \"Hello\"");
        assertTrue(DashLang.hasErrors());
        assertTrue(errContent.toString().contains("Error at end: Expect ';' after value."));
    }
}
