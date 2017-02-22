/**
 * Created by Bogdan on 18.02.2017.
 */
public class Annotation {
    public static final String AXIOM = "Сх. акс ";
    public static final String ASSUM = "Предп. ";
    public static final String MODUS = "M. P. ";
    public static final String WRONG = "Не доказано";

    public String annot;
    public int index1;
    public int index2;

    public Annotation(String str, int ind1, int ind2) {
        annot = str;
        index1 = ind1;
        index2 = ind2;
    }

    public Annotation(String str, int ind1) {
        this(str, ind1, 0);
    }

    public Annotation(String str) {
        this(str, 0, 0);
    }

    public String print() {
        if (annot.equals(Annotation.MODUS)) return annot + index1 + ", " + index2;
        else if (annot.equals(Annotation.AXIOM)) return annot + index1;
        else if (annot.equals(Annotation.ASSUM)) return annot + index1;
        else  return annot;
    }
}
