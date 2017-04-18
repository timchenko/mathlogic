package Parser;

public class ParserContext
{
    public String text;
    public int currentPosition;

    public ParserContext(String text, int currentPosition) {
        this.text = text;
        this.currentPosition = currentPosition;
    }

    public ParserContext clone() {
        return new ParserContext(text, currentPosition);
    }
}
