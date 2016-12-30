package clasification.utils;

import common.Fraction;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by bianca on 29.12.2016.
 */
public class ProcessDataClasif {
    String inputFile;
    private int noOfExample;
    private int noOfFeatures;
    private int noOfOuputs;
    private Fraction[][] input;
    private Fraction[][] output;
    private Double biggestNumber;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public ProcessDataClasif(String inputFile) throws IOException, ParseException {
        this.inputFile = inputFile;
        this.biggestNumber = getBiggestData();
    }

    public void readInputData() throws IOException, ParseException {
        FileReader fr = new FileReader(inputFile);
        BufferedReader br = new BufferedReader(fr);
        noOfExample = Integer.parseInt(br.readLine());
        noOfFeatures = Integer.parseInt(br.readLine());
        Integer.parseInt(br.readLine());
        noOfOuputs = 3;
        input = new Fraction[noOfExample][noOfFeatures];
        output = new Fraction[noOfExample][noOfOuputs];
        String day1[] = br.readLine().split("\t");
        for (int i = 0; i < noOfExample; i++) {
            String day2[] = br.readLine().split("\t");
            int j;
            input[i][0] = dayOfWeek(day1[0]).div(new Fraction(BigInteger.TEN));
            input[i][1] = dayOfMonth(day1[0]).div(new Fraction(BigInteger.valueOf(30)));
            input[i][2] = monthOfYear(day1[0]).div(new Fraction(BigInteger.valueOf(12)));
            //output should be the next day open price
            //System.out.println("Yesterday close: "+day1[4]+" Today open: "+day2[1]);
            if (Double.valueOf(day2[1]) - Double.valueOf(day1[4]) > 0.1){
                //UP
                output[i][0] = new Fraction(BigInteger.ONE);
                output[i][1] = new Fraction();
                output[i][2] = new Fraction();
            }else if (Double.valueOf(day2[1]) - Double.valueOf(day1[4]) > -0.1 && Double.valueOf(day2[1]) - Double.valueOf(day1[4]) < 0.1){
                //ZERO
                output[i][1] = new Fraction(BigInteger.ONE);
                output[i][0] = new Fraction();
                output[i][2] = new Fraction();
            }else{
                //DOWN
                output[i][2] = new Fraction(BigInteger.ONE);
                output[i][1] = new Fraction();
                output[i][0] = new Fraction();
            }
            for (j = 3; j < noOfFeatures; j++) {
                input[i][j] = new Fraction(BigInteger.ONE).valueOf(Double.valueOf(day1[j - 2])).calibreaza();
            }
            day1 = day2;
        }
        br.close();
        fr.close();
    }

    private Fraction monthOfYear(String prop) {
        String[] tok = prop.split("-");
        return new Fraction().valueOf(Double.valueOf(tok[1]));
    }

    private Fraction dayOfMonth(String prop) {
        String[] tok = prop.split("-");
        return new Fraction().valueOf(Double.valueOf(tok[2]));
    }

    private Fraction dayOfWeek(String date) throws ParseException {
        Calendar c = Calendar.getInstance();
        c.setTime(formatter.parse(date));
        double dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        return new Fraction().valueOf(dayOfWeek);
    }

    public Double getBiggestNumber() {
        return biggestNumber;
    }

    public String getInputFile() {
        return inputFile;
    }

    public int getNoOfExample() {
        return noOfExample;
    }

    public int getNoOfFeatures() {
        return noOfFeatures;
    }

    public int getNoOfOuputs() {
        return noOfOuputs;
    }

    public Fraction[][] getInput() {
        return input;
    }

    public Fraction[][] getOutput() {
        return output;
    }

    public Double getBiggestData() throws IOException, ParseException {
        FileReader fr = new FileReader(inputFile);
        BufferedReader br = new BufferedReader(fr);
        noOfExample = Integer.parseInt(br.readLine());
        noOfFeatures = Integer.parseInt(br.readLine());
        noOfOuputs = Integer.parseInt(br.readLine());
        Double max = 0.0;
        for (int i = 0; i < noOfExample; i++) {
            String props[] = br.readLine().split("\t");
            for (int j = 1; j < noOfFeatures; j++) {
                if (j != 5 && j < noOfFeatures-2) {
                    max = (max < Double.valueOf(props[j])) ? Double.valueOf(props[j]) : max;
                }
            }
        }
        return max;
    }

    public Double getAverageOutput() throws IOException, ParseException {
        Double average = 0.0;
        for (int i = 0; i < noOfExample; i++) {
            average += input[i][5].recalibreaza().toDouble();
        }
        return average/noOfExample;
    }
}
