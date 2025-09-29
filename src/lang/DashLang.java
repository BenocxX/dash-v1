package lang;

import lang.utils.FileReader;

import java.io.IOException;

public class DashLang {
    public static void runFile(String path) throws IOException {
        run(FileReader.readSource(path));
    }

    private static void run(String source) {
        System.out.println(source);
        System.out.println();
    }
}
