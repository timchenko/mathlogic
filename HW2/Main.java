import java.io.*;

/**
 * Created by Bogdan on 18.02.2017.
 */
public class Main {
    public static void main(String[] args) {
        try (BufferedReader bReader = new BufferedReader(new InputStreamReader(new FileInputStream("contra.in"), "UTF-8"));
             BufferedWriter bWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("good1.out"),  "UTF-8"))) {

            String line;
            line = bReader.readLine();
            Proof proof = new Proof(line);
            while ((line = bReader.readLine()) != null) {
                proof.addExpression(line);
            }
            Proof ans = proof.deduction();
            bWriter.write(ans.getHead() + "\r\n");
            for (int i = 0; i < ans.size(); i++) {
                bWriter.write(ans.getExp(i) + "\r\n");
            }
        } catch (IOException e) {
            System.err.println("Some I/O error occurred");
        }
    }
}
