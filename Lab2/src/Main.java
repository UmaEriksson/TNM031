import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;

public class Main {
    public static void main(String args[]) {
        // Two large prime numbers p and q
        String stringMessage = "Hello cat!";
        BigInteger p, q, n, e, phi, d, m = new BigInteger(stringMessage.getBytes()), c;
        //n, z, d = 0, e, i;

        int bitLength = 512;
        p = largePrime(bitLength);
        q = largePrime(bitLength);

        n = p.multiply(q);

        phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));

        // 4. Find an int e such that 1 < e < Phi(n) 	and gcd(e,Phi) = 1
        e = genE(phi);

        d = e.modInverse(phi);

        c = m.modPow(e, n);


        //Decrypt

        m = c.modPow(d, n);

        System.out.println(new String(m.toByteArray()));
    }


    //this function exists in a java lib, i think
    static int gcd(int e, int z) {
        if (e == 0)
            return z;
        else
            return gcd(z % e, e);

    }

    // function returns a BigInteger which is a prime number that is bitLength large.
    private static BigInteger largePrime(int bitLength) {
        Random randomInt = new Random();
        return BigInteger.probablePrime(bitLength, randomInt);
    }

    public static BigInteger genE(BigInteger phi) {
        Random rand = new Random();
        BigInteger e = new BigInteger(1024, rand);
        do {
            e = new BigInteger(1024, rand);
            while (e.min(phi).equals(phi)) { // while phi is smaller than e, look for a new e
                e = new BigInteger(1024, rand);
            }
        } while (!e.gcd(phi).equals(BigInteger.ONE)); // if gcd(e,phi) isnt 1 then stay in loop
        return e;

    }

}