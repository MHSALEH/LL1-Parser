import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {

    private static final String[] SYMBOLS = {
        ":=", "|=", "=<", "=>", ".", ":", ";", ",", "+", "(", ")", "+", "-", "*", "/", "%", "=", "<", ">"
    };

    private static final String[] RESERVED_WORDS = {
        "project", "const", "var", "int", "subroutine", "begin", "end", "scan", "print", "if",
        "then", "endif", "else", "while", "do"
    };

    static ArrayList<Integer> tokensInline = new ArrayList<>();
    public static String[][] getTokens(String fileName) {
        ArrayList<String> tokens = new ArrayList<>();
        int lineNumber = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] words = line.split("\\s+|(?<=[^a-zA-Z0-9])|(?=[^a-zA-Z0-9])");
                for (int i = 0; i < words.length; i++) {
                    String word = words[i];
                    if (isSymbol(word)) {
                        if (i + 1 < words.length) {
                            String compoundSymbol = word + words[i + 1];
                            if (isSymbol(compoundSymbol)) {
                                tokens.add(compoundSymbol);
                                i++;
                            } else {
                                tokens.add(word);
                            }
                        } else {
                            tokens.add(word);
                        }
                    } else if (isReservedWord(word)) {
                        tokens.add(word);
                    } else if (isName(word)) {
                        tokens.add("name");
                    } else if (isIntegerValue(word)) {
                        tokens.add("integer-value");
                    } else {
//                        System.err.printf("Unknown symbol '%s' at line %d\n", word, lineNumber);
                    }
                }
                tokensInline.add(tokens.size());
            }
        } catch (IOException e) {
            System.err.printf("Failed to read file '%s': %s\n", fileName, e.getMessage());
        }

        return tokensTo2D(tokens);
    }

    private static boolean isSymbol(String word) {
        for (String symbol : SYMBOLS) {
            if (word.equals(symbol)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isReservedWord(String word) {
        for (String reservedWord : RESERVED_WORDS) {
            if (word.equals(reservedWord)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isName(String word) {
        Pattern pattern = Pattern.compile("^[a-zA-Z]+[a-zA-Z0-9]*$");
        Matcher matcher = pattern.matcher(word);
        return matcher.matches();
    }

    private static boolean isIntegerValue(String word) {
        try {
            Integer.parseInt(word);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static String[][] tokensTo2D(ArrayList<String> tokens) {
    	int l=0;
        String[][] tokenTypes = new String[tokens.size()][2];
        for (int i = 0; i < tokens.size(); i++) {
        	if(i>=tokensInline.get(l)) {
	    		l++;
	    	}
            tokenTypes[i][0] = (l+1)+"";
            tokenTypes[i][1] = tokens.get(i);
        }
        return tokenTypes;
    }

}
