package Utils;

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
public class ProcessData {
    String inputFile;
    private int noOfExample;
    private int noOfFeatures;
    private int noOfOuputs;
    private Fraction[][] input;
    private Fraction[][] output;
    private Double biggestNumber;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public ProcessData(String inputFile) throws IOException, ParseException {
        this.inputFile = inputFile;
        this.biggestNumber = getBiggestData();
    }

    public void readInputData() throws IOException, ParseException {
        FileReader fr = new FileReader(inputFile);
        BufferedReader br = new BufferedReader(fr);
        noOfExample = Integer.parseInt(br.readLine());
        noOfFeatures = Integer.parseInt(br.readLine());
        noOfOuputs = Integer.parseInt(br.readLine());
        input = new Fraction[noOfExample][noOfFeatures];
        output = new Fraction[noOfExample][noOfOuputs];
        for (int i = 0; i < noOfExample; i++) {
            String props[] = br.readLine().split("\t");
            int j;
            input[i][0] = dayOfWeek(props[0]).div(new Fraction(BigInteger.TEN));
            input[i][1] = dayOfMonth(props[0]).div(new Fraction(BigInteger.valueOf(30)));
            input[i][2] = monthOfYear(props[0]).div(new Fraction(BigInteger.valueOf(12)));
            output[i][0] = new Fraction(BigInteger.ONE).valueOf(Double.valueOf(props[6])).calibreaza();
            for (j = 3; j < noOfFeatures; j++) {
                input[i][j] = new Fraction(BigInteger.ONE).valueOf(Double.valueOf(props[j - 2])).calibreaza();
            }
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
                if (j != 5 && j < 7) {
                    max = (max < Double.valueOf(props[j])) ? Double.valueOf(props[j]) : max;
                }
            }
        }
        return max;
    }
}
