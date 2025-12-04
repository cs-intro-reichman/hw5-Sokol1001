import java.util.Random;

public class Wordle {

    // Reads all words from dictionary filename into a String array.
    public static String[] readDictionary(String filename) {
        In fileIn = new In(filename);
        String[] dict = fileIn.readAllLines();
        fileIn.close();
        return dict;
    }

    // Choose a random secret word from the dictionary. 
    // Hint: Pick a random index between 0 and dict.length (not including) using Math.random()
    public static String chooseSecretWord(String[] dict) {
        // Since Random class is not imported and we are limited to basic methods, 
        // we use Math.random() directly.
        int index = (int) (Math.random() * dict.length);
        return dict[index];
    }

    // Simple helper: check if letter c appears anywhere in secret (true), otherwise
    // return false.
    public static boolean containsChar(String secret, char c) {
        int i = 0;
        while (i < secret.length()) {
            if (secret.charAt(i) == c) {
                return true;
            }
            i++;
        }
        return false;
    }

    // Compute feedback for a single guess into resultRow.
    // G for exact match, Y if letter appears anywhere else, _ otherwise.
    public static void computeFeedback(String secret, String guess, char[] resultRow) {
        int i = 0;
        while (i < secret.length()) {
            char guessChar = guess.charAt(i);
            
            // Check for Green (G) first: exact match
            if (guessChar == secret.charAt(i)) {
                resultRow[i] = 'G';
            } 
            // Check for Yellow (Y): letter appears elsewhere
            else if (containsChar(secret, guessChar)) {
                resultRow[i] = 'Y';
            } 
            // Otherwise, Gray (_)
            else {
                resultRow[i] = '_';
            }
            i++;
        }
    }

    // Store guess string (chars) into the given row of guesses 2D array.
    public static void storeGuess(String guess, char[][] guesses, int row) {
        int i = 0;
        while (i < guess.length()) {
            guesses[row][i] = guess.charAt(i);
            i++;
        }
    }

    // Prints the game board up to currentRow (inclusive).
    public static void printBoard(char[][] guesses, char[][] results, int currentRow) {
        System.out.println("Current board:");
        for (int row = 0; row <= currentRow; row++) {
            System.out.print("Guess " + (row + 1) + ": ");
            for (int col = 0; col < guesses[row].length; col++) {
                System.out.print(guesses[row][col]);
            }
            System.out.print(" Result: ");
            for (int col = 0; col < results[row].length; col++) {
                System.out.print(results[row][col]);
            }
            System.out.println();
        }
        System.out.println();
    }

    // Returns true if all entries in resultRow are 'G'.
    public static boolean isAllGreen(char[] resultRow) {
        int i = 0;
        while (i < resultRow.length) {
            if (resultRow[i] != 'G') {
                return false;
            }
            i++;
        }
        return true;
    }

    public static void main(String[] args) {

        int WORD_LENGTH = 5;
        int MAX_ATTEMPTS = 6;
        
        // Read dictionary
        String[] dict = readDictionary("dictionary.txt");
        if (dict.length == 0) {
            System.out.println("Error: Dictionary is empty or not found.");
            return;
        }

        // Choose secret word
        String secret = chooseSecretWord(dict);

        // Prepare 2D arrays for guesses and results
        char[][] guesses = new char[MAX_ATTEMPTS][WORD_LENGTH];
        char[][] results = new char[MAX_ATTEMPTS][WORD_LENGTH];

        // Prepare to read from the standard input 
        In inp = new In();

        int attempt = 0;
        boolean won = false;

        while (attempt < MAX_ATTEMPTS && !won) {

            String guess = "";
            boolean valid = false;

            // Loop until you read a valid guess
            while (!valid) {
                System.out.print("Enter your guess (5-letter word): ");
                
                // Read from the standard input using In class
                String inputLine = inp.readLine();
                
                if (inputLine == null) {
                    break; 
                }
                
                guess = inputLine.toUpperCase(); 
                
                // Check if the guess is valid (must be 5 letters)
                if (guess.length() != WORD_LENGTH) {
                    System.out.println("Invalid word. Please try again.");
                } else {
                    valid = true;
                }
            }
            
            // Check if we exited the loop due to break (input stream closure)
            if (!valid) break; 

            // Store guess and compute feedback
            storeGuess(guess, guesses, attempt);
            computeFeedback(secret, guess, results[attempt]);

            // Print board
            printBoard(guesses, results, attempt);

            // Check win
            if (isAllGreen(results[attempt])) {
                System.out.println("Congratulations! You guessed the word in " + (attempt + 1) + " attempts.");
                won = true;
            }

            attempt++;
        }

        if (!won) {
            System.out.println("Sorry, you did not guess the word.");
            // Follow the assignment example printing style
            System.out.println("The secret word was: " + secret);
        }

        inp.close();
    }
}