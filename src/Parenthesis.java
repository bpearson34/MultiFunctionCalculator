public class Parenthesis extends EquationCharacter{

    private final boolean BEGIN;
    private final boolean END;

    public Parenthesis(int positions, boolean begins, boolean ends){
        setPosition(positions);
        BEGIN = begins;
        END = ends;

        if (BEGIN)
            setSymbol("(");
        else if (END)
            setSymbol(")");
    }



}
