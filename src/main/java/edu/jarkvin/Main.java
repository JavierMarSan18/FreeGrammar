package edu.jarkvin;

import edu.jarkvin.model.Rule;

import java.util.*;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static Random random = new Random();
    public static void main(String[] args) {
        init();
    }

    //Método de inicio
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

    //Muestra todas las cadenas generadas por las reglas.
    private static void showStrings(Set<String> strings) {
        List<String> listStrings = new ArrayList<>(strings.stream().toList());
        listStrings.sort(Collections.reverseOrder());
        listStrings.forEach(System.out::println);
    }

    //Genera cadenas a partir de las reglas de la gramática.
    private static Set<String> getStrings(Set<Rule> rules, String initial, int n) {
        Set<String> generatedStrings = new HashSet<>();
        String concatString;
        do {
            //Se obtiene una cadena a partir de la regla inicial.
            concatString = getInitialString(initial, rules);

            //Verifica si existe un no terminal en la cadena.
            //Si existe lo reemplaza por una regla.
            while (existsNoTerminal(concatString, rules)){
                concatString = getString(concatString, findRulesByString(concatString, rules));
            }

            //Se cambia el elemento vacío 'ε' y se agrega la cadena al set.
            concatString = replaceVoid(concatString);
            generatedStrings.add(concatString);

        }while(generatedStrings.size() < n);
        return generatedStrings;
    }

    //Devuelve una regla aleatoria asociada a una variable inicial.
    private static String getInitialString(String initVar, Set<Rule> rules) {
        List<Rule> initialRules = getRules(initVar, rules);
        Rule rule = initialRules.get(random.nextInt(initialRules.size()));
        return rule.getString();
    }

    //Devuelve un regla asociada a una variable.
    private static String getString(String str, List<Rule> rules) {
        Rule rule = rules.get(random.nextInt(rules.size()));
        str = str.replace(rule.getVariable(), rule.getString());
        return str;
    }

    //Muestra todas las reglas del set
    private static void showRules(Set<Rule> rules) {
        System.out.println("P = {");
        rules.forEach(r -> System.out.println("\t"+r.toString()));
        System.out.println("}");
    }

    //Devuelve todas las reglas asociadas a una variable no terminal
    // que se encuentra en una cadena.
    private static List<Rule> findRulesByString(String str, Set<Rule> rules){
        return rules.stream().filter(r -> str.contains(r.getVariable())).toList();
    }

    //Devuelve todas las reglas asociadas a una variable no terminal.
    private static List<Rule> getRules(String variable, Set<Rule> rules) {
        return rules.stream().filter(r -> variable.equals(r.getVariable())).toList();
    }

    //Elimina el elemento vacío 'ε' de la cadena.
    private static String replaceVoid(String str){
        return str.replace("ε","");
    }

    //Verifica si existe una variable no terminal en una cadena.
    private static boolean existsNoTerminal(String str, Set<Rule> rules) {
        return !findRulesByString(str, rules).isEmpty();
    }

    //Pregunta si se desea ingresar una nueva regla.
    static boolean toContinue() {
        System.out.println("Desea ingresar otra regla? y/n");
        String opc = scanner.nextLine();
        return opc.equals("y");
    }
}
