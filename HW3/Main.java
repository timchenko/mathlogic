import javafx.util.Pair;

import java.io.*;

/**
 * Created by Bogdan on 18.02.2017.
 */
public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Low arguments: required 2");
            return;
        }
        try (BufferedReader bReader = new BufferedReader(new InputStreamReader(new FileInputStream(args[0]), "UTF-8"));
             BufferedWriter bWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[1]),  "UTF-8"))) {

            String line;
            line = bReader.readLine();
            Expression exp = ExpressionParser.parse(line);
            Valuation value = Validator.validate(exp);
            if (value != null) {
                bWriter.write("Высказывание ложно при " + value.toString());
                return;
            } else {
                Proof proof = Proofer.getProof(exp);
                bWriter.write(proof.getHead() + "\r\n");
                for (int i = 0; i < proof.size(); i++) {
                    bWriter.write(proof.getExp(i) + "\r\n");
                }
            }
        } catch (IOException e) {
            System.err.println("Some I/O error occurred");
        }
    }
}
