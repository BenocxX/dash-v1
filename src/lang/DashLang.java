package lang;

import lang.utils.FileReader;

import java.io.IOException;
import java.util.Scanner;

public class DashLang {
    public static void runFile(String path) throws IOException {
        run(FileReader.readSource(path));
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
        }
    }

    private static void run(String source) {
        System.out.println(source);
        System.out.println();
    }
}
