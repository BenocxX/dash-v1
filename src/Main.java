import lang.DashLang;

public static void main(String[] args) throws IOException {
    if (args.length > 1) {
        System.out.println("Error: too many arguments");
        return;
    }

    if (args.length == 1) {
        DashLang.runFile(args[0]);
        return;
    }

    DashLang.runPrompt();
}