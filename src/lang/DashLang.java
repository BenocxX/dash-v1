package lang;

import lang.ast.AstPrinter;
import lang.expressions.*;
import lang.lexer.Lexer;
import lang.tokens.Token;
import lang.tokens.TokenType;
import lang.utils.FileReader;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class DashLang {
    private static boolean hadError = false;

    public static void runFile(String path) throws IOException {
        run(FileReader.readSource(path));

        if (hadError) {
            System.exit(1);
        }
    }

    public static void runPrompt() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("> ");

            String line = scanner.nextLine();
            if (line.equals("\n")) {
                break;
            }

            run(line);
            hadError = false;
        }
    }

    public static void error(int line, String message) {
        System.err.println("[line " + line + "] Error: " + message);
        hadError = true;
    }

    private static void run(String source) {
        System.out.println("Source:");
        System.out.println(source);
        System.out.println();

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();
        for (Token token : tokens) {
            System.out.println("Token " + token);
        }
        System.out.println();

        // -123 * (45.67)
        Expression expression = new BinaryExpression(
                new UnaryExpression(
                        new Token(TokenType.MINUS, "-", null, 1),
                        new LiteralExpression(123)
                ),
                new Token(TokenType.STAR, "*", null, 1),
                new GroupingExpression(
                        new LiteralExpression(45.67)
                )
        );
        System.out.println(expression.print(new AstPrinter()));
    }
}
