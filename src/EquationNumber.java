public class EquationNumber extends EquationCharacter{

    private final double NUMBER;
    public EquationNumber(int positions, String symbols){
        setPosition(positions);
        setSymbol(symbols);
        NUMBER = Double.parseDouble(getSymbol());

    }


}
