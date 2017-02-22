import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Created by Bogdan on 18.02.2017.
 */
public class Main {
    public static void main(String[] args) {
        try (BufferedReader bReader = new BufferedReader(new InputStreamReader(new FileInputStream("good1.in"), "UTF-8"));
             BufferedWriter bWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("good1.out"),  "UTF-8"))) {

            String line;
            line = bReader.readLine();
            Proof proof = new Proof(line);
            while ((line = bReader.readLine()) != null) {
                proof.addExpression(line);
            }
            ArrayList<Annotation> ans = proof.annotate();
            bWriter.write(proof.getHead());
            bWriter.write("\r\n");
            for (int i = 1; i <= ans.size(); i++) {
                bWriter.write("(" + i + ") " + proof.getExp(i) + " (" + ans.get(i-1).print() + ")\r\n");
            }
        } catch (IOException e) {
            System.err.println("Some I/O error occurred");
        }
    }
}
