package edu.jarkvin;

import edu.jarkvin.model.Rule;

import java.util.*;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static Random random = new Random();
    public static void main(String[] args) {
        init();
    }

    private static void init() {
        String initVar;
        Set<Rule> rules = new HashSet<>();

        System.out.println("Ingresa variable inicial");
        initVar = scanner.nextLine();

        do {
            Rule rule = new Rule();
            System.out.println("Ingresar variable");
            rule.setVariable(scanner.nextLine());
            System.out.println("Ingresar regla");
            rule.setString(scanner.nextLine());

            rules.add(rule);
        }while (toContinue());

        System.out.println("Ingresar el numero de cadenas generadas:");
        int n = scanner.nextInt();

        showRules(rules);
        showStrings(getStrings(rules, initVar, n));
    }

    private static void showStrings(Set<String> strings) {
        List<String> listStrings = new ArrayList<>(strings.stream().toList());
        listStrings.sort(Collections.reverseOrder());
        listStrings.forEach(System.out::println);
    }

    private static Set<String> getStrings(Set<Rule> rules, String initial, int n) {
        Set<String> generatedStrings = new HashSet<>();

        String concatString;
        do {
            //Se obtiene una cadena a partir de la regla inicial
            concatString = getInitialString(initial, rules);

            while (existsNoTerminal(concatString, rules)){
                concatString = getString(concatString, findRulesByString(concatString, rules));
            }

            concatString = replaceVoid(concatString);
            generatedStrings.add(concatString);

        }while(generatedStrings.size() < n);
        return generatedStrings;
    }

    private static String replaceVoid(String str){
        return str.replace("ε","");
    }

    private static boolean existsNoTerminal(String str, Set<Rule> rules) {
        return !findRulesByString(str, rules).isEmpty();
    }

    private static List<Rule> findRulesByString(String str, Set<Rule> rules){
        return rules.stream().filter(r -> str.contains(r.getVariable())).toList();
    }

    private static String getInitialString(String initVar, Set<Rule> rules) {
        List<Rule> initialRules = getRules(initVar, rules);
        Rule rule = initialRules.get(random.nextInt(initialRules.size()));
        return rule.getString();
    }

    private static List<Rule> getRules(String variable, Set<Rule> rules) {
        return rules.stream().filter(r -> variable.equals(r.getVariable())).toList();
    }

    private static String getString(String str, List<Rule> rules) {
        Rule rule = rules.get(random.nextInt(rules.size()));
        str = str.replace(rule.getVariable(), rule.getString());
        return str;
    }

    private static void showRules(Set<Rule> rules) {
        System.out.println("P = {");
        rules.forEach(r -> System.out.println("\t"+r.toString()));
        System.out.println("}");
    }

    static boolean toContinue() {
        System.out.println("Desea ingresar otra regla? y/n");
        String opc = scanner.nextLine();
        return opc.equals("y");
    }
}
