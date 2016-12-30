package common;

import java.math.BigInteger;

/**
 * Created by bianca on 29.12.2016.
 */
public class Fraction {
    BigInteger denominator;
    BigInteger numerator;

    public Fraction(){
        this.numerator = BigInteger.ZERO;
        this.denominator = BigInteger.ONE;
    }

    public Fraction(BigInteger numerator, BigInteger denominator) {
        this.denominator = denominator;
        this.numerator = numerator;
        this.simplify();
    }

    public Fraction(BigInteger numerator) {
        this.denominator = BigInteger.ONE;
        this.numerator = numerator;
        this.simplify().normalize();
    }

    public Fraction add(Fraction other) {
        BigInteger gcd = getGcd(this.denominator, other.denominator);
        BigInteger numerator1 = this.numerator.multiply(other.denominator.divide(gcd));
        BigInteger numerator2 = other.numerator.multiply(this.denominator.divide(gcd));
        BigInteger denominator1 = denominator.multiply(other.denominator.divide(gcd));
        return new Fraction(numerator1.add(numerator2), denominator1).simplify().normalize();
    }

    public Fraction sub(Fraction other) {
        BigInteger gcd = getGcd(this.denominator, other.denominator);
        BigInteger numerator1 = this.numerator.multiply(other.denominator.divide(gcd));
        BigInteger numerator2 = other.numerator.multiply(this.denominator.divide(gcd));
        BigInteger denominator1 = denominator.multiply(other.denominator.divide(gcd));
        return new Fraction(numerator1.subtract(numerator2), denominator1).simplify().normalize();
    }

    public Fraction mul(Fraction other) {
        return new Fraction(numerator.multiply(other.numerator), denominator.multiply(other.denominator)).simplify().normalize();
    }

    public Fraction div(Fraction other){
        return new Fraction(numerator.multiply(other.denominator), denominator.multiply(other.numerator)).simplify().normalize();
    }

    public Fraction simplify(){
        BigInteger gcd = getGcd(this.denominator, this.numerator);
        this.denominator = this.denominator.divide(gcd);
        this.numerator = this.numerator.divide(gcd);
        return this;
    }

    public Fraction normalize(){
        while (this.denominator.compareTo(BigInteger.valueOf(1000000000)) == 1){
            this.denominator = this.denominator.divide(BigInteger.TEN);
            this.numerator = this.numerator.divide(BigInteger.TEN);
        }
        return this;
    }

    private BigInteger getGcd(BigInteger b1, BigInteger b2) {
        return b1.gcd(b2);
    }

    @Override
    public String toString(){
        //return this.numerator+"/"+this.denominator;
        return this.toDouble().toString();
    }

    public Double toDouble(){
        double num = this.numerator.doubleValue();
        double den = this.denominator.doubleValue();
        return num/den;
    }

    public Fraction valueOf(double number){
        BigInteger denom = BigInteger.ONE;
        while (number != Math.floor(number)){
            denom = denom.multiply(BigInteger.TEN);
            number = number*10;
        }
        this.denominator = denom;
        this.numerator = BigInteger.valueOf((long) number);
        this.simplify();
        return this;
    }

    public Fraction valueOf(long number){
        this.denominator = BigInteger.ONE;
        this.numerator = BigInteger.valueOf(number);
        return this;
    }

    public boolean greaterThan(Fraction fraction) {
        BigInteger n1 = this.numerator;
        BigInteger n2 = fraction.numerator;
        BigInteger d1 = this.denominator;
        BigInteger d2 = fraction.numerator;
        BigInteger gcd = getGcd(d1, d2);
        n1 = n1.multiply(gcd);
        n2 = n2.multiply(gcd);
        return (n1.compareTo(n2) == 1);
    }

    public Fraction calibreaza() {
        //TODO: this does modify the entity
        this.denominator = this.denominator.multiply(BigInteger.valueOf(21000));
        return this;
    }

    public Fraction recalibreaza() {
        //TODO: this does NOT modify the entity
        return new Fraction(this.numerator.multiply(BigInteger.valueOf(21000)), this.denominator);
    }

}
