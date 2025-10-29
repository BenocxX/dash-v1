package lang;

import lang.errors.RuntimeError;
import lang.interpreter.Interpreter;
import lang.utils.AstPrinter;
import lang.expressions.*;
import lang.lexer.Lexer;
import lang.parser.Parser;
import lang.tokens.Token;
import lang.tokens.TokenType;
import lang.utils.FileReader;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class DashLang {
    private static boolean hadError = false;
    private static boolean hadRuntimeError = false;

    public static void runFile(String path) throws IOException {
        run(FileReader.readSource(path));

        if (hadError || hadRuntimeError) {
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

    public static void error(Token token, String message) {
        if (token.type == TokenType.EOF) {
            System.err.println("[line " + token.line + " ] Error at end: " + message);
        } else {
            System.err.println("[line " + token.line + " ] Error at '" + token.lexeme + "' " + message);
        }

        hadError = true;
    }

    public static void runtimeError(RuntimeError error) {
        System.err.println(error.getMessage() + "\n[line " + error.token.line + "]");
        hadRuntimeError = true;
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

        Parser parser = new Parser(tokens);
        Expression expression = parser.parse();

        if (hadError) {
            return;
        }

        System.out.println(expression.print(new AstPrinter()));
        System.out.println();

        Interpreter interpreter = new Interpreter();
        System.out.println(interpreter.interpret(expression));
    }
}
