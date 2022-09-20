import java.math.BigInteger;

public class Main {
    public static void main(String[] args) {
        String y = "oiu291981u39u192u3198u389u28u389u";
        BigInteger bi = new BigInteger(y, 36);
        System.out.println(bi.toString(36));

    }
}