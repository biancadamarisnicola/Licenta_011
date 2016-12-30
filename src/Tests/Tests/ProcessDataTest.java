package Tests;

import common.Fraction;
import regression.utils.ProcessDataRegr;

import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;

/**
 * Created by bianca on 29.12.2016.
 */
public class ProcessDataTest {
    public static void main(String [] args) throws IOException, ParseException {
        ProcessDataRegr processData = new ProcessDataRegr("src/resources/XTSE-MEQ_UADJ.txt");
        processData.readInputData();
        Fraction[][] input = processData.getInput();
        Fraction[][] output = processData.getOutput();
        int noOfExample = processData.getNoOfExample();
        int noOfFeatures = processData.getNoOfFeatures();
        int noOfOuputs = processData.getNoOfOuputs();
        Double normalizationRate = processData.getBiggestNumber();
        System.out.println("No of example "+noOfExample);
        System.out.println("No of features "+noOfFeatures);
        System.out.println("No of Outputs "+noOfOuputs);
        for (int i =0; i< 10; i++){
            System.out.println("input["+i+"] = "+input[i][0].mul(new Fraction(BigInteger.TEN))+", "
                    +input[i][1].mul(new Fraction(BigInteger.valueOf(30)))+", "
                    +input[i][2].mul(new Fraction(BigInteger.valueOf(12)))+", "+input[i][3]+", "
                    +input[i][4]+", "+input[i][5]+", "+input[i][6]+", "+input[i][7]+";");
        }
        for (int i =0; i< 10; i++){
            System.out.println("output["+i+"] = "+output[i][0].recalibreaza());
        }
        System.out.println(processData.getBiggestData());
        System.out.println(processData.getAverageOutput());
    }
}
