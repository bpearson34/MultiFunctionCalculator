import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    static ArrayList<EquationCharacter> equation = new ArrayList<>();
    static boolean isNegative;
    static String userInput = "";

    public static void main (String [] args){
        //Opening text for user to see
        System.out.println("Calculator Software 3.1");
        System.out.println("Press e to exit.");
        System.out.println("Enter equation below");

        while (!userInput.equals("e")) {
            userInput = getUserInput();
            boolean willRun = verifyValidityOfUserInput();
            if (willRun && !userInput.equals("e")) {
                separateCharacters(userInput);
                solveParenthesis();
                solveMultiplicationAndDivision();
                solveAdditionAndSubtraction();
                System.out.println(equation.get(0).getSymbol());
                equation.clear();
            }
        }
        System.out.println("Closing program...");
    }

    public static String getUserInput() {
        Scanner input = new Scanner(System.in);

        //Call for user input (the equation)
        return input.next();
    }

    public static void separateCharacters (String userInput){
        for (int counter = 0; counter < userInput.length(); counter++) {
            //if parenthesis is encountered, mark if open or close and position
            if (userInput.charAt(counter) == '(' ){
                Parenthesis p = new Parenthesis(counter, true, false);
                equation.add(p);

            }
            else if (userInput.charAt(counter) == ')') {
                Parenthesis p = new Parenthesis(counter, false, true);
                equation.add(p);
            }

            //if operator symbol is encountered, store character and position
            else if (userInput.charAt(counter) == '+' || userInput.charAt(counter) == '-' || userInput.charAt(counter) == '*' || userInput.charAt(counter) == '/'){
                //if the '-' is referring to a negative number, mark the upcoming number as negative
                if ((counter == 0 && userInput.charAt(counter) == '-') || userInput.charAt(counter) == '-' && (userInput.charAt(counter - 1) == '+' || userInput.charAt(counter - 1) == '-' || userInput.charAt(counter - 1) == '*' || userInput.charAt(counter - 1) == '/' || userInput.charAt(counter - 1) == '(')) {
                    isNegative = true;
                }
                //otherwise, store the operator as normal
                else {
                    Operator o = new Operator(counter, String.valueOf(userInput.charAt(counter)));
                    equation.add(o);
                }
            }

            //if a digit is encountered, store the number
            else if (Character.isDigit(userInput.charAt(counter))){
                StringBuilder toAdd = new StringBuilder();
                //Ensures completion of full number
                for (int j = counter; j < userInput.length() && (Character.isDigit(userInput.charAt(j)) || userInput.charAt(j) == '.'); j++) {
                   toAdd.append(userInput.charAt(j));
                   if (j < userInput.length() - 1 && (Character.isDigit(userInput.charAt(j + 1)) || userInput.charAt(j + 1) == '.'))
                       counter++;
                }

                //if the number was marked as negative, adjust accordingly
                if (isNegative) {
                    EquationNumber e = new EquationNumber(counter, "-" + toAdd);
                    equation.add(e);
                    isNegative = false;
                }
                else {
                    EquationNumber e = new EquationNumber(counter, toAdd.toString());
                    equation.add(e);
                }
            }
        }
        sortPositions();
    }

    public static void solveMultiplicationAndDivision(){
        double combine;
        //multiply numbers if multiplication symbol is encountered
        for (int i = 0; i < equation.size(); i++){
            //multiply numbers if multiplication symbol is encountered
            if (equation.get(i).getSymbol().equals("*") && i < equation.size() - 1){
                combine = Double.parseDouble(equation.get(i - 1).getSymbol()) * Double.parseDouble(equation.get(i + 1).getSymbol());
                EquationNumber e = new EquationNumber(i - 1, String.valueOf(combine));
                int run = 0;
                while (run != 3){
                    equation.remove(i - 1);
                    run++;
                }
                equation.add(i - 1, e);
                i = -1;
            }

            //divide numbers if division symbol is encountered
            else if (equation.get(i).getSymbol().equals("/") && i < equation.size() - 1){
                combine = Double.parseDouble(equation.get(i - 1).getSymbol()) / Double.parseDouble(equation.get(i + 1).getSymbol());
                EquationNumber e = new EquationNumber(i - 1, String.valueOf(combine));
                int run = 0;
                while (run != 3){
                    equation.remove(i - 1);
                    run++;
                }
                equation.add(i - 1, e);
                i = -1;
            }
        }
        removeParenthesis();
    }

    public static void solveAdditionAndSubtraction(){
        double combine;

        for (int i = 0; i < equation.size(); i++){
            //add numbers if addition symbol is encountered
            if (equation.get(i).getSymbol().equals("+") && i < equation.size() - 1){
                combine = Double.parseDouble(equation.get(i - 1).getSymbol()) + Double.parseDouble(equation.get(i + 1).getSymbol());
                EquationNumber e = new EquationNumber(i - 1, String.valueOf(combine));
                int run = 0;
                while (run != 3){
                    equation.remove(i - 1);
                    run++;
                }
                equation.add(i - 1, e);
                i = -1;
            }

            //subtract numbers if subtraction symbol is encountered
            else if (equation.get(i).getSymbol().equals("-") && i < equation.size() - 1){
                combine = Double.parseDouble(equation.get(i - 1).getSymbol()) - Double.parseDouble(equation.get(i + 1).getSymbol());
                EquationNumber e = new EquationNumber(i - 1, String.valueOf(combine));
                int run = 0;
                while (run != 3){
                    equation.remove(i - 1);
                    run++;
                }
                equation.add(i - 1, e);
                i = -1;
            }
        }
    }

    public static void solveParenthesis(){
        int start = -1, end = -1;
        for (int i = 0; i < equation.size(); i++){
            if (equation.get(i).getSymbol().equals("(")){
                start = equation.get(i).getPosition();
            }
            else if (equation.get(i).getSymbol().equals(")")) {
                end = equation.get(i).getPosition();
            }

            if (start > -1 && (end > -1 && end > start)) {
                solveMultiplicationAndDivision(start, end);
                solveAdditionAndSubtraction(start, end);
                start = -1;
                end = -1;
                i = -1;
            }
        }
    }

    //solves multiplication and division within parenthesis with their position marked by start and end variable
    public static void solveMultiplicationAndDivision(int start, int end){
        double combine;

        for (int i = start; i < end && i < equation.size(); i++){
            //multiply numbers if multiplication symbol is encountered
            if (equation.get(i).getSymbol().equals("*") && equation.get(i).getPosition() < end){
                combine = Double.parseDouble(equation.get(i - 1).getSymbol()) * Double.parseDouble(equation.get(i + 1).getSymbol());
                EquationNumber e = new EquationNumber(i - 1, String.valueOf(combine));
                int run = 0;
                while (run != 3){
                    equation.remove(i - 1);
                    run++;
                }
                equation.add(i - 1, e);
                sortPositions();
                for (int j = 0; j < equation.size(); j++) {
                    if (equation.get(j).getSymbol().equals("(") && equation.get(j + 2).getSymbol().equals(")")){
                        equation.remove(j + 2);
                        equation.remove(j);
                    }
                }
            }

            else if (equation.get(i).getSymbol().equals("/") && equation.get(i).getPosition() < end){
                combine = Double.parseDouble(equation.get(i - 1).getSymbol()) / Double.parseDouble(equation.get(i + 1).getSymbol());
                EquationNumber e = new EquationNumber(i - 1, String.valueOf(combine));
                int run = 0;
                while (run != 3){
                    equation.remove(i - 1);
                    run++;
                }
                equation.add(i - 1, e);
                sortPositions();
                for (int j = 0; j < equation.size(); j++) {
                    if (equation.get(j).getSymbol().equals("(") && equation.get(j + 2).getSymbol().equals(")")){
                        equation.remove(j + 2);
                        equation.remove(j);
                    }
                }
            }
        }

    }

    //solves addition and subtraction within parenthesis with their position marked by start and end variable
    public static void solveAdditionAndSubtraction(int start, int end){
        double combine;

        for (int i = start; i < end && i < equation.size(); i++){
            //multiply numbers if multiplication symbol is encountered
            if (equation.get(i).getSymbol().equals("+") && equation.get(i).getPosition() < end){
                combine = Double.parseDouble(equation.get(i - 1).getSymbol()) + Double.parseDouble(equation.get(i + 1).getSymbol());
                EquationNumber e = new EquationNumber(i - 1, String.valueOf(combine));
                int run = 0;
                while (run != 3){
                    equation.remove(i - 1);
                    run++;
                }
                equation.add(i - 1, e);
                sortPositions();
                //clear parenthesis if there are no more applicable operations
                for (int j = 0; j < equation.size(); j++) {
                    if (equation.get(j).getSymbol().equals("(") && equation.get(j + 2).getSymbol().equals(")")){
                        equation.remove(j + 2);
                        equation.remove(j);
                    }
                }
            }

            else if (equation.get(i).getSymbol().equals("-") && equation.get(i).getPosition() < end && !equation.get(i + 1).getSymbol().equals("(")){
                combine = Double.parseDouble(equation.get(i - 1).getSymbol()) - Double.parseDouble(equation.get(i + 1).getSymbol());
                EquationNumber e = new EquationNumber(i - 1, String.valueOf(combine));
                int run = 0;
                while (run != 3){
                    equation.remove(i - 1);
                    run++;
                }
                equation.add(i - 1, e);
                for (int j = 0; j < equation.size(); j++) {
                    if (equation.get(j).getSymbol().equals("(") && equation.get(j + 2).getSymbol().equals(")")){
                        equation.remove(j + 2);
                        equation.remove(j);
                    }
                }
            }
        }


    }

    public static void sortPositions() {
        for (int i = 0; i < equation.size(); i++){
            equation.get(i).setPosition(i);
        }
    }

    //if parenthesis is no longer needed, remove them. Example: 45 + (65) = 45 + 65
    public static void removeParenthesis(){
        for (int i = 0; i < equation.size(); i++){
            if (equation.get(i).getSymbol().equals("(") && equation.get(i + 2).getSymbol().equals(")")){
                equation.remove(i + 2);
                equation.remove(i);
            }
        }
    }

    //error detection method to catch improper user inputs
    public static boolean verifyValidityOfUserInput(){
        //prevents error message when attempting to exit program
        if (userInput.charAt(0) == 'e')
            return false;
        else if (!Character.isDigit(userInput.charAt(0)) && userInput.charAt(0) != '-' && userInput.charAt(0) != '('){
            System.err.println("Invalid equation. Please enter a standard equation to solve.\nVariables and exponents are not currently supported.");
            return false;
        }
        else if (userInput.contains("++") || userInput.contains("**") || userInput.contains("//") || userInput.contains("..")){
            System.err.println("Invalid equation. Duplicate operators or decimal symbols have been detected one potential cause of the error.");
            return false;
        }
        else if (userInput.contains("(") || userInput.contains(")")){
            int beginParenthesisCount = 0, endParenthesisCount = 0;
            for (int i = 0; i < userInput.length(); i++){
                if (userInput.charAt(i) == '(')
                    beginParenthesisCount++;
                else if (userInput.charAt(i) == ')')
                    endParenthesisCount++;
            }

            if (beginParenthesisCount != endParenthesisCount){
                System.err.println("Invalid equation. The number of open and closed parenthesis are not equal.");
                return false;
            }
        }
        else if (userInput.charAt(userInput.length() - 1) == '+' || userInput.charAt(userInput.length() - 1) == '-' || userInput.charAt(userInput.length() - 1) == '*' || userInput.charAt(userInput.length() - 1) == '/'){
            System.err.println("Invalid equation. There is an operator symbol at the end of the equation.");
            return false;
        }

        return true;
    }
}
