public class Parenthesis extends EquationCharacter{

    boolean begin;
    boolean end;

    public Parenthesis(int positions, boolean begins, boolean ends){
        position = positions;
        begin = begins;
        end = ends;
    }

    public Parenthesis(){
        position = -1;
        begin = false;
        end = false;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
