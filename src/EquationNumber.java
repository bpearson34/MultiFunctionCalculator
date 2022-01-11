public class EquationNumber extends EquationCharacter{

    double number;
    public EquationNumber(int positions, String symbols){
        position = positions;
        symbol = symbols;
        number = Double.parseDouble(symbol);

    }


}
