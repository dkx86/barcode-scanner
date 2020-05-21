package ru.dkx86.scanner;


import java.util.Scanner;

public class Main {
    private static boolean EXIT = false;

    public static void main(String[] args) {
        final BarcodeScanner bScanner = new BarcodeScanner("YJHF500"); // service name from jpos.xml
        Scanner sc = new Scanner(System.in);
        printHelp();

        System.out.println("Input command:");
        while (sc.hasNextLine()) {
            processCommand(bScanner, sc.nextLine());
            if (EXIT)
                break;
        }
    }

    private static void processCommand(BarcodeScanner bScanner, String commandData) {
        if (commandData == null || commandData.isEmpty()) {
            printHelp();
            return;
        }
        final String cmd = commandData.trim().toUpperCase();
        switch (cmd) {
            case "START":
                bScanner.initialize();
                bScanner.setAutoReady(true);
                bScanner.setReadyForReading();
                break;
            case "STOP":
                bScanner.shutdown();
                break;
            case "EXIT":
                EXIT = true;
                break;
            case "HELP":
            default:
                printHelp();
        }
    }

    @SuppressWarnings("StringBufferReplaceableByString")
    private static void printHelp() {
        final StringBuffer help = new StringBuffer();
        help.append("Available commands:\n");
        help.append("  START - Open scanner, claim it and set enabled. Set scanner ready to read data. \n");
        help.append("  STOP - Disable data events, disable scanner, release claim and close it.\n");
        help.append("  HELP - Print this help text.\n");
        help.append("  EXIT - Exit the app.\n");
        System.out.println(help);
    }
}
