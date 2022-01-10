package flashcards;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Action action = new Action();
        String importCommand = null;
        String exportCommand = null;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-import")) {
                importCommand = args[i + 1];
            }
            if (args[i].equals("-export")) {
                exportCommand = args[i + 1];
            }
        }

        action.startGame(importCommand, exportCommand);

    }

}
