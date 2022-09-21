import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String args[]) {

        Scanner input = new Scanner(System.in);

        System.out.println("Enter message to be encrypted:");

        String stringMessage = input.nextLine();
        // Declare BigInteger variables
        BigInteger p, q, n, e, phi, d, c;
        //n, z, d = 0, e, i;
        //Convert string to bytes
        BigInteger  m = new BigInteger(stringMessage.getBytes());

        // bit length for large prime numbers
        int bitLength = 512;
        //create 2 large prime numbers with length bitLength
        p = largePrime(bitLength);
        q = largePrime(bitLength);

        //compute n
        n = p.multiply(q);

        // Compute phi = (p - 1)(q - 1)
        phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));

        // Find the encryption exponent e such that 1 < e < Phi(n) and gcd(e,Phi) = 1
        e = genE(phi);

        // calculate the decryption exponent d
        d = e.modInverse(phi);

        // c = the encrypted message
        c = m.modPow(e, n);

        //Decrypt the message using d
        m = c.modPow(d, n);

        //Output
        outputVariables(p, q, n, phi, e, d, c, m, new String(m.toByteArray()));
    }

    private static void outputVariables(BigInteger p, BigInteger q, BigInteger n, BigInteger phi, BigInteger e, BigInteger d, BigInteger c, BigInteger m, String s) {
        System.out.println("Prime number p: " + p.toString());
        System.out.println("Prime number q: " + q.toString());
        System.out.println("n (p * q): " + n.toString());
        System.out.println("Phi: " + phi.toString());
        System.out.println("Public key e: " + e.toString());
        System.out.println("Private key d: " + d.toString());
        System.out.println("Encrypted message c: " + c.toString());
        System.out.println("Decrypted message m (BigInteger) " + m.toString());
        System.out.println("Decrypted message (String): " + s);
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