import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    static ArrayList<EquationCharacter> equation = new ArrayList<>();
    static boolean isNegative;
    static String userinput = "";
    static int operatorcount = 0;

    public static void main (String [] args){
        //Opening text for user to see
        System.out.println("Calculator Version 2.0");
        System.out.println("Press e to exit.");
        System.out.println("Enter your equation below");

        while (!userinput.equals("e")) {
            userinput = getUserInput();
            separateCharacters(userinput);
  //          solveParenthesis();
            solveMultiplicationAndDivision();
            solveAdditionandSubtraction();
            System.out.println(equation.get(0).symbol);
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
                if ((counter == 0 && userinput.charAt(counter) == '-') || userinput.charAt(counter) == '-' && (userinput.charAt(counter - 1) == '+' || userinput.charAt(counter - 1) == '-' || userinput.charAt(counter - 1) == '*' || userinput.charAt(counter - 1) == '/')) {
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
            if (equation.get(i).symbol.equals("*") && i < equation.size() - 1){
                combine = Double.parseDouble(equation.get(i - 1).symbol) * Double.parseDouble(equation.get(i + 1).symbol);
                EquationNumber e = new EquationNumber(i - 1, String.valueOf(combine));
                int run = 0;
                while (run != 3){
                    equation.remove(i - 1);
                    run++;
                }
                equation.add(i - 1, e);
                i = -1;
            }

            else if (equation.get(i).symbol.equals("/") && i < equation.size() - 1){
                combine = Double.parseDouble(equation.get(i - 1).symbol) / Double.parseDouble(equation.get(i + 1).symbol);
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

    public static void solveAdditionandSubtraction(){
        double combine;

        for (int i = 0; i < equation.size(); i++){
            //multiply numbers if multiplication symbol is encountered
            if (equation.get(i).symbol.equals("+") && i < equation.size() - 1){
                combine = Double.parseDouble(equation.get(i - 1).symbol) + Double.parseDouble(equation.get(i + 1).symbol);
                EquationNumber e = new EquationNumber(i - 1, String.valueOf(combine));
                int run = 0;
                while (run != 3){
                    equation.remove(i - 1);
                    run++;
                }
                equation.add(i - 1, e);
                i = -1;
            }

            else if (equation.get(i).symbol.equals("-") && i < equation.size() - 1){
                combine = Double.parseDouble(equation.get(i - 1).symbol) - Double.parseDouble(equation.get(i + 1).symbol);
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
}
