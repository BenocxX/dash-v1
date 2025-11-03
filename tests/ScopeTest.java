import lang.DashLang;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ScopeTest extends InterpreterTester {
    @Test
    public void testVariableDeclaredOutsideScopeShouldBeAccessable() {
        DashLang.runString("""
        var x = 10;
        {
            print x;
        }
        """);
        assertFalse(DashLang.hasErrors());
        assertEquals("10\n", outContent.toString());
    }

    @Test
    public void testVariableDeclaredInsideScopeShouldNotBeAccessableOutside() {
        DashLang.runString("""
        {
            var x = 10;
        }
        print x;
        """);
        assertTrue(DashLang.hasErrors());
        assertTrue(errContent.toString().contains("Undefined variable 'x'."));
    }

    @Test
    public void testVariableRedeclaredInsideScopeShouldShadowVariableOutsideScope() {
        DashLang.runString("""
        var x = 10;
        {
            var x = 20;
            print x;
        }
        print x;
        """);
        assertFalse(DashLang.hasErrors());
        assertEquals("20\n10\n", outContent.toString());
    }

    @Test
    public void testVariableExpressionInsideScopeShouldBeAppliedToTheRightVariable() {
        DashLang.runString("""
        var x = 10;
        var y = -10;
        {
            x = 20;
            var y = -20;
        
            print x;
            print y;
        }
        print x;
        print y;
        """);
        assertFalse(DashLang.hasErrors());
        assertEquals("""
        20
        -20
        20
        -10
        """, outContent.toString());
    }

    @Test
    public void testComplexScopeNestingAndVariableShadowingShouldNotCauseError() {
        DashLang.runString("""
        var x = "global x";
        var y = "global y";
        var z = "global z";

        {
            var x = "outer x";
            var y = "outer y";

            {
                var x = "inner x";
                print x;
                print y;
                print z;
            }

            print x;
            print y;
            print z;
        }

        print x;
        print y;
        print z;
        """);
        assertFalse(DashLang.hasErrors());
        assertEquals("""
        inner x
        outer y
        global z
        outer x
        outer y
        global z
        global x
        global y
        global z
        """, outContent.toString());
    }
}
