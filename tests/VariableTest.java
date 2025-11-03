import lang.DashLang;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VariableTest extends InterpreterTester {
    @Test
    public void testVariableDeclarationShouldNotCauseAnyError() {
        DashLang.runString("var x = 10;");
        assertFalse(DashLang.hasErrors());
    }

    @Test
    public void testVariableDeclarationWithNoValueShouldNotCauseAnyError() {
        DashLang.runString("var x;");
        assertFalse(DashLang.hasErrors());
    }

    @Test
    public void testVariableDeclarationWithAComplexExpressionShouldNotCauseAnyError() {
        DashLang.runString("var x = (--2 + 4) * (10) / 2 <= 10;");
        assertFalse(DashLang.hasErrors());
    }

    @Test
    public void testVariableAssignationShouldNotCauseAnyError() {
        DashLang.runString("""
        var x = 10;
        x = 20;
        """);
        assertFalse(DashLang.hasErrors());
    }

    @Test
    public void testVariableWithNoValueAssignationShouldNotCauseAnyError() {
        DashLang.runString("""
        var x;
        x = 20;
        """);
        assertFalse(DashLang.hasErrors());
    }

    @Test
    public void testVariableAssignationShouldBeChainable() {
        DashLang.runString("""
        var x;
        var y = 10;
        x = y = 20;
        print x;
        print y;
        """);
        assertFalse(DashLang.hasErrors());
        assertEquals("20\n20\n", outContent.toString());
    }

    @Test
    public void testVariableExpressionShouldReturnTheAssociatedValue() {
        DashLang.runString("""
        var x = 10;
        print x;
        """);
        assertFalse(DashLang.hasErrors());
        assertEquals("10\n", outContent.toString());
    }

    @Test
    public void testVariableExpressionAfterAssignationShouldReturnTheAssociatedValue() {
        DashLang.runString("""
        var x = 10;
        x = 20;
        print x;
        """);
        assertFalse(DashLang.hasErrors());
        assertEquals("20\n", outContent.toString());
    }

    @Test
    public void testVariableExpressionWithNoValueShouldReturnNil() {
        DashLang.runString("""
        var x;
        print x;
        """);
        assertFalse(DashLang.hasErrors());
        assertEquals("nil\n", outContent.toString());
    }

    @Test
    public void testVariableDeclarationWithNoIdentifierShouldCauseError() {
        DashLang.runString("var;");
        assertTrue(DashLang.hasErrors());
        assertTrue(errContent.toString().contains("Error at ';' Expect variable name."));
    }

    @Test
    public void testVariableDeclarationWithAssignationWithNoIdentifierShouldCauseError() {
        DashLang.runString("var = 10;");
        assertTrue(DashLang.hasErrors());
        assertTrue(errContent.toString().contains("Error at '=' Expect variable name."));
    }

    @Test
    public void testVariableDeclarationShouldEndWithASemiColon() {
        DashLang.runString("var x = 10");
        assertTrue(DashLang.hasErrors());
        assertTrue(errContent.toString().contains("Error at end: Expect ';' after variable declaration."));
    }

    @Test
    public void testVariableAssignationToNonVariableExpressionShouldCauseError() {
        DashLang.runString("10 = 20");
        assertTrue(DashLang.hasErrors());
        assertTrue(errContent.toString().contains("Error at '=' Invalid assignment target."));
    }
}
