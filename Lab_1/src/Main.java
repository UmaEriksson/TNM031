import java.math.BigInteger;

public class Main {
    public static void main(String[] args) {

        String mes = "cat";
        BigInteger m = new BigInteger(mes.getBytes());



        System.out.println(new String(m.toByteArray()));

    }
}