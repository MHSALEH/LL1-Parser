import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.TreeSet;

class Parser {
    private final Map<String, Map<String, Integer>> parserTable;
    private final String[][] productions;
    public String[][] table;

    public Parser(Map<String, Map<String, Integer>> parserTable, String[][] productions) {
        this.parserTable = parserTable;
        this.productions = productions;
    }

    public void parse(String[][] tokens) {
        Stack<String> stack = new Stack<>();
        stack.push("$");
        stack.push("project-declaration");

        int i = 0;
        while (!stack.empty()) {
            String top = stack.pop();
            if(top.equals("$") && i>=tokens.length-1) {
            	System.out.println("success");break;
            }
            if(top.equals("")) {
            	continue;
            }else if (top.equals(tokens[i][1])) {
                i++;
            } else if (parserTable.containsKey(top)) {
                Map<String, Integer> row = parserTable.get(top);
                if (row.containsKey(tokens[i][1])) {
                    int productionIndex = row.get(tokens[i][1]);
                    String[] production = productions[productionIndex];
                    for (int j = production.length - 1; j >= 0; j--) {
                        stack.push(production[j]);
                    }
                } else {
                    System.out.println("Error in line "+tokens[i][0]+": expected " + row.keySet() + " but got " + tokens[i][1]);
                    return;
                }
            } else {
                System.out.println("Error in line "+tokens[i][0]+": expected " + top + " but got " + tokens[i][1]);
                return;
            }
        }
        System.out.println("Successful parsing.");
    }

    public void firstSet(){
    	TreeSet<String> terminatiors;
        TreeSet<String> nonTerminatiors = null;
        HashMap<String, ArrayList<String>> productionMap = null;
        HashMap<String, TreeSet<String>> firstMap = null;

        for(String nonTermin : nonTerminatiors){
            ArrayList<String> rightArray = productionMap.get(nonTermin);
            for(String right : rightArray){
                TreeSet<String> firstSet = firstMap.get(nonTermin);
                if(firstSet == null){
                    firstSet = new TreeSet<String>();
                }
                int flag = 0;
                for(int i=0;i<right.length();i++){
                    String singleSymble = right.charAt(i)+"";
                    flag = recFirst(firstSet, nonTermin, singleSymble);
                    if(flag == 1){
                        break;
                    }
                }
            }
        }
    }
    public int recFirst(TreeSet<String>firstSet, String nonTermin, String singleSymble){
    	TreeSet<String> terminatiors = null;
        TreeSet<String> nonTerminatiors = null;
        HashMap<String, ArrayList<String>> productionMap = null;
        HashMap<String, TreeSet<String>> firstMap = null;
        if(terminatiors.contains(singleSymble) || singleSymble.equals("ε")){
            firstSet.add(singleSymble);
            firstMap.put(nonTermin, firstSet);
            return 1;
        }
        else if(nonTerminatiors.contains(singleSymble)){
            ArrayList<String> arrayList = productionMap.get(singleSymble);
            for(String s : arrayList){
                String letter = s.charAt(0)+"";
                recFirst(firstSet, nonTermin, letter);
            }
        }
        return 1;
    }



}
