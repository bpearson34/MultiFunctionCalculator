public class EquationNumber extends EquationCharacter{

    private double number;
    public EquationNumber(int positions, String symbols){
        setPosition(positions);
        setSymbol(symbols);
        number = Double.parseDouble(getSymbol());

    }


}
