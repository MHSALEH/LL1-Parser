import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class main {

	private static ArrayList<String> NTerminals;
	public static void main(String[] args) throws FileNotFoundException {
		NTerminals=readNonTerminals("src/NonTerminals.txt");
		String[][] productions= getproductionRules("src/production_rules.txt");
		Map<String, Map<String, Integer>> parserTable = getParserTable(productions, "src/LL1table.txt");
		String[][] tokens= Tokenizer.getTokens("src/code_example2.txt");
		Parser parser= new Parser(parserTable, productions);
		parser.parse(tokens);

//		traverseMap(parserTable);
//		traverseProductions(productions);
//		traverseTokens(tokens);
	}

	public static String[][] getproductionRules(String productionRulesFileName) throws FileNotFoundException {
	    List<String[]> productionsList = new ArrayList<>();

	    try (Scanner scanner = new Scanner(new File(productionRulesFileName))) {
	        while (scanner.hasNextLine()) {
	            String[] parts = scanner.nextLine().trim().split("::=");
	            String[] productions = parts[1].trim().split("\\s+");
	            for (int i = 0; i < productions.length; i++) {
	                if (productions[i].equals("''")) {
	                    productions[i] = "";
	                }
	            }
	            productionsList.add(productions);
	        }
	    }

	    String[][] productions = new String[productionsList.size()][];
	    return productionsList.toArray(productions);
	}

    public static Map<String, Map<String, Integer>> getParserTable(String[][] productions, String fileName) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(fileName));
        String[] terminals = scanner.nextLine().trim().split(" ");
        Map<String, Map<String, Integer>> parserTable = new HashMap<>();
        int lineNumber = 0;

        while (scanner.hasNextLine()) {
            lineNumber++;
            String[] values = scanner.nextLine().trim().split(" ");
            Map<String, Integer> entry = new HashMap<>();

            for (int i = 0; i < values.length; i++) {
                if (values[i].equals("ss")) {
                    continue;
                }
                entry.put(terminals[i], Integer.parseInt(values[i]));
            }

            parserTable.put(NTerminals.get(lineNumber - 1), entry);
        }

        scanner.close();
        return parserTable;
    }
    public static void traverseMap(Map<String, Map<String, Integer>> parserTable) {
        System.out.println("\n\n\n\n");
        parserTable.forEach((outerKey, innerMap) -> {
            innerMap.forEach((innerKey, value) -> {
                System.out.println("Outer Key: " + outerKey + ", Inner Key: " + innerKey + ", Value: " + value);
            });
        });
        System.out.println("\n\n\n\n");
    }
    public static void traverseProductions(String[][] productionsList) {
        System.out.println("\ntraverseProductions");
        // print the production rules
        System.out.print("Production Rules:\n::= ");
        for (String[] production : productionsList) {
            System.out.print(String.join("", production));
            System.out.print("\n::= ");
        }
        System.out.println("\n");
    }

    public static void traverseTokens(String[][] tokens) {
        System.out.println("\n\nTokens:");
        for (String[] token : tokens) {
            System.out.println(token[0] + " : " + token[1]);
        }
        System.out.println("\n\n");
    }


    private static ArrayList<String> readNonTerminals(String fileName) {
    	ArrayList<String> nt = new ArrayList<>();
        try {
            Files.lines(Paths.get(fileName))
                .forEach(nt::add);
        } catch (IOException e) {
            System.out.println("File not found: " + fileName);
        }
        return nt;
    }

}
