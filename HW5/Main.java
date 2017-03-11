import java.io.*;

/**
 * Created by Bogdan on 11.03.2017.
 */
public class Main {

    public static void main(String[] args) {
        if (args == null || args.length < 2) {
            System.err.println("Not enough arguments: required 2");
        } else {
            try (BufferedReader bReader = new BufferedReader(new InputStreamReader(new FileInputStream("part.txt"), "UTF-8"));
                 BufferedWriter bWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("proof.txt"), "UTF-8"))) {
                final String TWOHUNDRED = intToNum(200);

                int a_int = Integer.parseInt(args[0]);
                int b_int = Integer.parseInt(args[1]);
                int c_int = a_int + b_int;

                String a = TWOHUNDRED.substring(0, a_int + 1);
                String b = TWOHUNDRED.substring(0, b_int + 1);
                String c = TWOHUNDRED.substring(0, c_int + 1);

                bWriter.write("|-" + a + "+" + b + "=" + c + "\n");

                String line;
                while ((line = bReader.readLine()) != null) { //|-@d@e@f(d+e=f->d+e'=f')
                    bWriter.write(line);
                    bWriter.write("\n");
                }

                //bWriter.write("TEMP!!" + "\n");

                bWriter.write("@d@e@f(d+e=f->d+e'=f')->@e@f("+a+"+e=f->"+a+"+e'=f')" + "\n");
                bWriter.write("@e@f("+a+"+e=f->"+a+"+e'=f')" + "\n");

                bWriter.write("a+0=a" + "\n");
                bWriter.write("a+0=a->(0=0->0=0->0=0)->a+0=a" + "\n");
                bWriter.write("(0=0->0=0->0=0)->a+0=a" + "\n");
                bWriter.write("(0=0->0=0->0=0)->@a(a+0=a)" + "\n");
                bWriter.write("@a(a+0=a)" + "\n");
                bWriter.write("@a(a+0=a)->"+a+"+0="+a + "\n");
                bWriter.write(a+"+0="+a + "\n");

                for (int d_int = 0; d_int < b_int; d_int++) {
                    String d = TWOHUNDRED.substring(0, d_int + 1);
                    String e = TWOHUNDRED.substring(0, a_int + d_int + 1);

                    bWriter.write("@e@f("+a+"+e=f->"+a+"+e'=f')->@f("+a+"+"+d+"=f->"+a+"+"+d+"'=f')" + "\n");
                    bWriter.write("@f("+a+"+"+d+"=f->"+a+"+"+d+"'=f')" + "\n");
                    bWriter.write("@f("+a+"+"+d+"=f->"+a+"+"+d+"'=f')->"+a+"+"+d+"="+e+"->"+a+"+"+d+"'="+e+"'" + "\n");
                    bWriter.write(a+"+"+d+"="+e+"->"+a+"+"+d+"'="+e+"'" + "\n");
                    bWriter.write(a+"+"+d+"'="+e+"'" + "\n");
                }

            } catch (IOException e) {
                System.err.println("Some I/O error occurred");
            }
        }
    }

    private static String intToNum(int i) {
        StringBuilder ans = new StringBuilder("0");
        for (; i > 0; --i) {
            ans.append('\'');
        }
        return ans.toString();
    }
}
