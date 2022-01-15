import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    static ArrayList<EquationCharacter> equation = new ArrayList<>();
    static boolean isNegative;
    static String userInput = "";
    static int operatorPosition = 0;

    public static void main (String [] args){
        //Opening text for user to see
        System.out.println("Calculator Version 3.0");
        System.out.println("Press e to exit.");
        System.out.println("Enter your equation below");

        while (!userInput.equals("e")) {
            userInput = getUserInput();
            separateCharacters(userInput);
            solveParenthesis();
            solveMultiplicationAndDivision();
            solveAdditionandSubtraction();
            System.out.println(equation.get(0).getSymbol());
            equation.clear();
        }
    }

    public static String getUserInput() {
        Scanner input = new Scanner(System.in);

        //Call for user input (the equation)
        return input.next();
    }

    public static void separateCharacters (String userinput){
        for (int counter = 0; counter < userinput.length(); counter++) {
            //if parenthesis is encountered, mark if open or close and position
            if (userinput.charAt(counter) == '(' ){
                Parenthesis p = new Parenthesis(counter, true, false);
                equation.add(p);

            }
            else if (userinput.charAt(counter) == ')') {
                Parenthesis p = new Parenthesis(counter, false, true);
                equation.add(p);
            }

            //if operator symbol is encountered, store character and position
            else if (userinput.charAt(counter) == '+' || userinput.charAt(counter) == '-' || userinput.charAt(counter) == '*' || userinput.charAt(counter) == '/'){
                //if the '-' is referring to a negative dumber, mark the upcoming number as negative
                if ((counter == 0 && userinput.charAt(counter) == '-') || userinput.charAt(counter) == '-' && (userinput.charAt(counter - 1) == '+' || userinput.charAt(counter - 1) == '-' || userinput.charAt(counter - 1) == '*' || userinput.charAt(counter - 1) == '/' || userinput.charAt(counter - 1) == '(')) {
                    isNegative = true;
                }
                //otherwise, store the operator as normal
                else {
                    Operator o = new Operator(counter, String.valueOf(userinput.charAt(counter)));
                    equation.add(o);
                }
            }

            //if a digit is encountered, store the number
            else if (Character.isDigit(userinput.charAt(counter))){
                StringBuilder toadd = new StringBuilder();
                //Ensures completion of full number
                for (int j = counter; j < userinput.length() && (Character.isDigit(userinput.charAt(j)) || userinput.charAt(j) == '.'); j++) {
                   toadd.append(userinput.charAt(j));
                   if (j < userinput.length() - 1 && (Character.isDigit(userinput.charAt(j + 1)) || userinput.charAt(j + 1) == '.'))
                       counter++;
                }

                //if the number was marked as negative, adjust accordingly
                if (isNegative) {
                    EquationNumber e = new EquationNumber(counter, "-" + toadd);
                    equation.add(e);
                    isNegative = false;
                }
                else {
                    EquationNumber e = new EquationNumber(counter, toadd.toString());
                    equation.add(e);
                }
            }
        }
    }

    public static void solveMultiplicationAndDivision(){
        double combine;

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

    public static void solveAdditionandSubtraction(){
        double combine;

        for (int i = 0; i < equation.size(); i++){
            //multiply numbers if multiplication symbol is encountered
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
                solveAdditionandSubtraction(start, end);
                start = -1;
                end = -1;
                i = -1;
            }
        }
    }

    //solves multiplication and divison within parenthesis with their position marked by start and end variable
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

    //solves multiplication and divison within parenthesis with their position marked by start and end variable
    public static void solveAdditionandSubtraction(int start, int end){
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

            else if (equation.get(i).getSymbol().equals("-") && equation.get(i).getPosition() < end && equation.get(i + 1).getSymbol() != "("){
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
}
