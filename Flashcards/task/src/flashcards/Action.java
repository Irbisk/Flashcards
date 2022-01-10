package flashcards;

import java.io.*;
import java.util.*;

public class Action {
    Scanner scanner = new Scanner(System.in);
    private List<String> cardsFront = new LinkedList<>();
    private Map<String, String> cardMap = new LinkedHashMap<>();
    private List<String> log = new LinkedList<>();
    private Map<String, Integer> hardestCards = new HashMap<>();

    public void startGame(String importFile, String exportFile) {
        if (importFile != null) {
            importCard(importFile);

        }


        Scanner scanner = new Scanner(System.in);
        boolean isPlaying = true;
        while (isPlaying) {
            logOut("\nInput the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):");
            String command = logIn();
            switch (command) {
                case "add":
                    add();
                    break;
                case "remove":
                    remove();
                    break;
                case "import":
                    importCard();
                    break;
                case "export":
                    exportCard();
                    break;
                case "ask":
                    ask();
                    break;
                case "log":
                    log();
                    break;
                case "hardest card":
                    hardestCard();
                    break;
                case "reset stats":
                    reset();
                    break;
                default:
                    exit();
                    isPlaying = false;
            }
        }

        if (exportFile != null) {
            exportCard(exportFile);
        }
    }

    public void logOut(String msg) {
        System.out.printf(msg);
        log.add(msg);
    }

    public String logIn() {
        System.out.println();
        String logIn = scanner.nextLine();
        log.add(logIn);
        return logIn;
    }

    public void log() {
        logOut("File name:");
        File file = new File(logIn());
        try {
            PrintWriter printWriter = new PrintWriter(file);
            for (String s: log) {
                printWriter.println(s);
            }
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logOut("The log has been saved.");
    }

    public void reset() {
        hardestCards.clear();
        logOut("Card statistics have been reset.");
    }

    public void hardestCard() {
        List<String> cards = new ArrayList<>();
        int numberOfMistakes = 0;
        for (var entry: hardestCards.entrySet()) {
            if (entry.getValue() > numberOfMistakes) {
                cards.clear();
                numberOfMistakes = entry.getValue();
                cards.add(entry.getKey());
            } else if (entry.getValue() == numberOfMistakes) {
                cards.add(entry.getKey());
            }
        }
        if (cards.size() == 1) {
            logOut("The hardest card is \"" + cards.get(0) + "\". You have " + numberOfMistakes + " errors answering it.\n");
        } else if (cards.size() > 1) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < cards.size(); i++) {
                stringBuilder.append("\"" + cards.get(i) + "\"");
                if (i < cards.size() - 1) {
                    stringBuilder.append(", ");
                } else {
                    stringBuilder.append(".");
                }
            }
            logOut("The hardest cards are " + stringBuilder.toString() + ". You have \"" + numberOfMistakes + "\" errors answering them.\n");
        } else {
            logOut("There are no cards with errors.");
        }
    }

    public void add() {
        Scanner scanner = new Scanner(System.in);
        Card card = new Card();
        logOut("The card:");
        String front = logIn();
        if (cardMap.containsKey(front)) {
            logOut("The card \"" + front + "\" already exists.");
            return;
        }
        card.setFront(front);
        logOut("The definition of the card:");
        String back = logIn();
        if (cardMap.containsValue(back)) {
            logOut("The definition \"" + back + "\" already exists. Try again:");
            return;
        }
        card.setBack(back);
        cardsFront.add(front);
        cardMap.put(front, back);

        logOut("The pair (\""+ card.getFront() + "\":\"" + card.getBack() + "\") has been added.\n");

    }



    public void remove() {
        Scanner scanner = new Scanner(System.in);
        logOut("Which card?\n");
        String front = logIn();
        if (!cardMap.containsKey(front)) {
            logOut("Can't remove \"" + front + "\": there is no such card.\n");
        } else {
            cardMap.remove(front);
            cardsFront.remove(front);
            logOut("The card has been removed.\n");
        }
    }



    public void importCard(String importFile) {
        File file = new File(importFile);
        int n = 0;
        try {
            Scanner scannerReadTheFile = new Scanner(file);
            while (scannerReadTheFile.hasNextLine()) {
                String card = scannerReadTheFile.nextLine();
                System.out.println(card);
                String[] cardArray = card.split(":");
                cardMap.put(cardArray[0], cardArray[1]);
                cardsFront.add(cardArray[0]);
                if (cardArray.length == 3) {
                    hardestCards.put(cardArray[0], Integer.valueOf(cardArray[2]));
                }
                n++;
            }
        } catch (FileNotFoundException e) {
            logOut("File not found.\n");
            return;
        }
        logOut(n + " cards have been loaded.\n");
    }

    public void importCard() {
        Scanner scanner = new Scanner(System.in);
        logOut("File name:");
        File file = new File(logIn());
        int n = 0;
        try {
            Scanner scannerReadTheFile = new Scanner(file);
            while (scannerReadTheFile.hasNextLine()) {
                String card = scannerReadTheFile.nextLine();
                System.out.println(card);
                String[] cardArray = card.split(":");
                cardMap.put(cardArray[0], cardArray[1]);
                cardsFront.add(cardArray[0]);
                if (cardArray.length == 3) {
                    hardestCards.put(cardArray[0], Integer.valueOf(cardArray[2]));
                }
                n++;
            }
        } catch (FileNotFoundException e) {
            logOut("File not found.\n");
            return;
        }
        logOut(n + " cards have been loaded.\n");
    }

    public void exportCard() {
        Scanner scanner = new Scanner(System.in);
        logOut("File name:");
        File file = new File(logIn());
        try {
            PrintWriter printWriter = new PrintWriter(file);
            for (var entry: cardMap.entrySet()) {
                printWriter.print(entry.getKey() + ":");
                printWriter.print(entry.getValue());
                if (hardestCards.containsKey(entry.getKey())) {
                    printWriter.println(":" + hardestCards.get(entry.getKey()));
                } else {
                    printWriter.println();
                }
            }
            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        logOut(cardMap.size() + " cards have been saved.\n");
    }

    public void exportCard(String exportFile) {
        File file = new File(exportFile);
        try {
            PrintWriter printWriter = new PrintWriter(file);
            for (var entry: cardMap.entrySet()) {
                printWriter.print(entry.getKey() + ":");
                printWriter.print(entry.getValue());
                if (hardestCards.containsKey(entry.getKey())) {
                    printWriter.println(":" + hardestCards.get(entry.getKey()));
                } else {
                    printWriter.println();
                }
            }
            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        logOut(cardMap.size() + " cards have been saved.\n");
    }

    public void ask() {
        Scanner scanner = new Scanner(System.in);
        logOut("How many times to ask?\n");
        int n = scanner.nextInt();
        log.add(n + "\n");
        scanner.nextLine();
        for (int i = 0; i < n; i++) {
            Random random = new Random();
            int cardNumber = random.nextInt(cardMap.size());
            checkCards(cardNumber);
        }
    }

    public void checkCards(int cardNumber) {
        Scanner scanner = new Scanner(System.in);
        Card card = new Card(cardsFront.get(cardNumber), cardMap.get(cardsFront.get(cardNumber)));
        logOut("Print the definition of \"" + card.getFront() + "\"\n");
        String definition = logIn();
        checkCardDefinition(definition, card);
    }

    private void checkCardDefinition(String definition, Card card) {
        if (definition.equals(card.getBack())) {
            logOut("Correct!\n");
        } else if (cardMap.containsValue(definition)) {
            String key = "";
            for (var entry: cardMap.entrySet()) {
                if (entry.getValue().equals(definition)) {
                    key = entry.getKey();
                    break;
                }
            }
            logOut("Wrong. The right answer is \"" + card.getBack() + "\", but your definition is correct for \"" + key + "\"\n");
            if (hardestCards.containsKey(card.getFront())) {
                int n = hardestCards.get(card.getFront());
                n++;
                hardestCards.replace(card.getFront(), n);
            } else {
                hardestCards.put(card.getFront(), 0);
            }

        } else {
            logOut("Wrong. The right answer is \"" + card.getBack() + "\"\n");
            if (hardestCards.containsKey(card.getFront())) {
                int n = hardestCards.get(card.getFront());
                n++;
                hardestCards.replace(card.getFront(), n);
            } else {
                hardestCards.put(card.getFront(), 1);
            }
        }
    }


    public void addCards() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input the number of cards:");
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; i++) {
            Card card = new Card();
            System.out.println("Card #" + (i + 1));
            String front = scanner.nextLine();
            while (cardMap.containsKey(front)) {
                System.out.println("The term \"" + front + "\" already exists. Try again:");
                front = scanner.nextLine();
            }
            card.setFront(front);
            System.out.println("The definition for card #" + (i + 1));
            String back = scanner.nextLine();
            while (cardMap.containsValue(back)) {
                System.out.println("The definition \"" + back + "\" already exists. Try again:");
                back = scanner.nextLine();
            }
            card.setBack(back);
            cardsFront.add(front);
            cardMap.put(front, back);
        }
    }


    public void exit() {
        logOut("Bye bye!");
    }




}
