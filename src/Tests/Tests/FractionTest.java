package Tests;

import common.Fraction;

import java.math.BigInteger;

/**
 * Created by bianca on 29.12.2016.
 */
public class FractionTest {
    public static void main(String[] args){
        Fraction fraction=new Fraction(BigInteger.valueOf(12), BigInteger.valueOf(15));
        System.out.println(fraction.toString());
        Fraction fraction2=new Fraction(BigInteger.valueOf(5), BigInteger.valueOf(24));
        Fraction result = fraction.mul(fraction2);
        System.out.println(result.toString());
        fraction = new Fraction(BigInteger.ONE).valueOf(0.01);
        System.out.println(fraction);
        fraction = new Fraction(BigInteger.ONE).valueOf(0.04);
        System.out.println(fraction);
        System.out.println(new Fraction().valueOf(0.18303908182786338));
    }
}
