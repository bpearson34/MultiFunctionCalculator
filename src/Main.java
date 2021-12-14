import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    static ArrayList<Double> equationNumbers = new ArrayList<>();
    static ArrayList<Character> operators = new ArrayList<>();
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
            separateNumbers(userinput);
            solveMultiplicationAndDivision();
            solveAdditionAndSubtraction();
            System.out.println(equationNumbers.get(0));
            equationNumbers.clear();
            operators.clear();
        }
    }

    public static String getUserInput() {
        Scanner input = new Scanner(System.in);

        //Call for user input (the equation)
        return input.next();
    }


    public static void separateNumbers(String userinput){
        String number = "";


        for (int counter = 0; counter < userinput.length(); counter++){
            //if parser encounters negative symbol and the following character. if any, is a digit, mark the upcoming number as negative
            if (userinput.charAt(counter) == '-' && counter == 0)
                isNegative = true;

            //add number to variable to be converted to double format
            if (userinput.charAt(counter) != '+' && userinput.charAt(counter) != '-' && userinput.charAt(counter) != '*' && userinput.charAt(counter) != '/') {
                number = number + userinput.charAt(counter);
            }

            //if negative has not been detected and the variable number is not empty
            else if (!isNegative && !number.equals("")) {
                operators.add(userinput.charAt(counter));
                equationNumbers.add(Double.parseDouble(number));
                number = "";
            }

            //if negative has been detected and variable number is not empty
            else if (isNegative && !number.equals("")){
                operators.add(userinput.charAt(counter));
                equationNumbers.add((Double.parseDouble(number)) * -1);
                number = "";
                isNegative = false;
            }

            else if (userinput.charAt(counter) == '-')
                isNegative = true;

            //special case if the number is the last character in the string
            if (counter == userinput.length() - 1){
                if(isNegative) {
                    equationNumbers.add(Double.parseDouble(number) * -1);
                    isNegative = false;
                }
                else {
                    equationNumbers.add(Double.parseDouble(number));
                }
                number = "";
            }
        }
    }

    public static void solveAdditionAndSubtraction(){
        double combine;

        //compare numbers against operators and solve accordingly
        for (int i = 0; i < equationNumbers.size() - 1; i++){
            if (operatorcount < operators.size() && operators.get(operatorcount) == '+'){
                combine = equationNumbers.get(i) + equationNumbers.get(i + 1);
                equationNumbers.set(i, combine);
                equationNumbers.remove(i + 1);
                i = -1;
                operatorcount++;
            }
            else if (operatorcount < operators.size() && operators.get(operatorcount) == '-'){
                combine = equationNumbers.get(i) - equationNumbers.get(i + 1);
                equationNumbers.set(i, combine);
                equationNumbers.remove(i + 1);
                i = -1;
                operatorcount++;
            }
        }
        operatorcount = 0;
    }

    public static void solveMultiplicationAndDivision(){
        double combine;

        //compare numbers against operators and solve accordingly
        for (int i = 0; i < equationNumbers.size() - 1; i++){
            if (operatorcount < operators.size() && operators.get(operatorcount) == '*'){
                combine = equationNumbers.get(i) * equationNumbers.get(i + 1);
                equationNumbers.set(i, combine);
                equationNumbers.remove(i + 1);
                i = -1;
                operatorcount++;
            }
            else if (operatorcount < operators.size() && operators.get(operatorcount) == '/'){
                combine = equationNumbers.get(i) / equationNumbers.get(i + 1);
                equationNumbers.set(i, combine);
                equationNumbers.remove(i + 1);
                i = -1;
                operatorcount++;
            }
            else
                operatorcount++;
        }

         //clear used operators since multiplication and division runs first
        for (int j = 0; j <= operatorcount && j < operators.size(); j++){
            if (operators.get(j) == '*' || operators.get(j) == '/')
                operators.remove(j);
        }
        operatorcount = 0;
    }

    //parse for parenthesis, store values of parenthesis location in string, solve within parenthesis first
}
