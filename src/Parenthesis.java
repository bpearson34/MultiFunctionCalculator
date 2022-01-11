public class Parenthesis extends EquationCharacter{

    boolean begin;
    boolean end;

    public Parenthesis(int positions, boolean begins, boolean ends){
        position = positions;
        begin = begins;
        end = ends;

        if (begin)
            symbol = "(";
        else if (end)
            symbol = ")";
    }

    public Parenthesis(){
        position = -1;
        begin = false;
        end = false;
    }


}
