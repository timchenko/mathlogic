import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Bogdan on 17.02.2017.
 */
public interface Expression {

    boolean equals(Expression e);

    HashSet<String> getVariables();

    boolean evaluate(Valuation valuation);

    String getOperation();

}
