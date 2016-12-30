import ANN.Controller;
import ANN.Layer;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bianca on 19.08.2016.
 */
public class Main {
    private JFrame frame;
    private Controller ctrl;
    private JTextField textField;
    private int noEpoch;
    private Double epsilon;
    private Double learningRate;
    private Double limitSEE;
    private boolean adaptiveLR;
    private Double alpha;
    private static Map<String, String> Stocks = new HashMap<>();

    static {
        Stocks.put("Nikkei-225", "NIKKEI225");
        Stocks.put("SCF-CME_NK1_FN", "SCF-CME_NK1_FN");
        Stocks.put("XTSE-MEQ_UADJ", "XTSE-MEQ_UADJ");
    }

    private int noHidden;
    private int noNeurPerHidden;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Main window = new Main();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Main() throws Exception {
        this.noHidden = 2;
        this.noNeurPerHidden = 3;
        this.alpha = 0.05;
        this.noEpoch = 100;
        this.learningRate = 0.9;
        //this.limitSEE = 400.0;
        this.adaptiveLR = true;
        this.epsilon = 100.0;
        initialize();
    }

    private void initialize() {
        //setBounds(x,y,w,h)
        frame = new JFrame();
        frame.setBounds(100, 100, 500, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblEpoch = new JLabel("Number of epochs");
        lblEpoch.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        lblEpoch.setBounds(20, 10, 130, 20);
        frame.getContentPane().add(lblEpoch);

        JTextField textFieldEpoch = new JTextField();
        textFieldEpoch.setBounds(180, 10, 130, 20);
        textFieldEpoch.setText(String.valueOf(noEpoch));
        frame.getContentPane().add(textFieldEpoch);
        textFieldEpoch.setColumns(10);

        JLabel lblLearningRate = new JLabel("Learning rate");
        lblLearningRate.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        lblLearningRate.setBounds(20, 40, 130, 20);
        frame.getContentPane().add(lblLearningRate);

        JTextField textFieldLearningRate = new JTextField();
        textFieldLearningRate.setBounds(180, 40, 130, 20);
        textFieldLearningRate.setText(String.valueOf(learningRate));
        frame.getContentPane().add(textFieldLearningRate);
        textFieldLearningRate.setColumns(10);

//        JLabel lblLimitSEE = new JLabel("Limit SEE");
//        lblLimitSEE.setFont(new Font("Times New Roman", Font.PLAIN, 16));
//        lblLimitSEE.setBounds(20, 70, 130, 20);
//        frame.getContentPane().add(lblLimitSEE);
//
//        JTextField textFieldLimitSEE = new JTextField();
//        textFieldLimitSEE.setBounds(180, 70, 130, 20);
//        textFieldLimitSEE.setText(String.valueOf(limitSEE));
//        frame.getContentPane().add(textFieldLimitSEE);
//        textFieldLimitSEE.setColumns(10);

        JLabel lblAdaptiveLearningRate = new JLabel("Adaptive Learning Rate");
        lblAdaptiveLearningRate.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        lblAdaptiveLearningRate.setBounds(20, 70, 160, 20);
        frame.getContentPane().add(lblAdaptiveLearningRate);

        JCheckBox checkBoxAdaptiveLearningRate = new JCheckBox();
        checkBoxAdaptiveLearningRate.setBounds(180, 70, 130, 20);
        checkBoxAdaptiveLearningRate.setSelected(adaptiveLR);
        frame.getContentPane().add(checkBoxAdaptiveLearningRate);

        JLabel lblMomentum = new JLabel("Momentum alpha");
        lblMomentum.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        lblMomentum.setBounds(20, 100, 130, 20);
        frame.getContentPane().add(lblMomentum);

        JTextField textFieldAlpha = new JTextField();
        textFieldAlpha.setBounds(180, 100, 130, 20);
        textFieldAlpha.setText(String.valueOf(alpha));
        frame.getContentPane().add(textFieldAlpha);
        textFieldAlpha.setColumns(10);

        JLabel lblEpsilon = new JLabel("Epsilon");
        lblEpsilon.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        lblEpsilon.setBounds(20, 130, 130, 20);
        frame.getContentPane().add(lblEpsilon);

        JTextField textFieldEpsilon = new JTextField();
        textFieldEpsilon.setBounds(180, 130, 130, 20);
        textFieldEpsilon.setText(String.valueOf(epsilon));
        frame.getContentPane().add(textFieldEpsilon);
        textFieldEpsilon.setColumns(10);

        JLabel lblNoHiddenLayer = new JLabel("No of Hidden Layer");
        lblNoHiddenLayer.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        lblNoHiddenLayer.setBounds(20, 160, 130, 20);
        frame.getContentPane().add(lblNoHiddenLayer);

        JTextField textFieldNoHiddenLayer = new JTextField();
        textFieldNoHiddenLayer.setBounds(180, 160, 130, 20);
        textFieldNoHiddenLayer.setText(String.valueOf(noHidden));
        frame.getContentPane().add(textFieldNoHiddenLayer);
        textFieldNoHiddenLayer.setColumns(10);

        JLabel lblNeurPerHidden = new JLabel("No of Neurons per Hidden Layer");
        lblNeurPerHidden.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        lblNeurPerHidden.setBounds(20, 190, 130, 20);
        frame.getContentPane().add(lblNeurPerHidden);

        JTextField textFieldNeurPerHidden = new JTextField();
        textFieldNeurPerHidden.setBounds(180, 190, 130, 20);
        textFieldNeurPerHidden.setText(String.valueOf(noNeurPerHidden));
        frame.getContentPane().add(textFieldNeurPerHidden);
        textFieldNeurPerHidden.setColumns(10);

        int pos = 0;
        for (Map.Entry stock : Stocks.entrySet()) {
            JButton btnCalculate = new JButton(stock.getKey().toString());
            btnCalculate.setBounds(185, 290 + pos, 130, 23);
            pos += 30;
            frame.getContentPane().add(btnCalculate);
            btnCalculate.addActionListener(arg0 -> {
                String s = null;
                noEpoch = Integer.valueOf(textFieldEpoch.getText());
                learningRate = Double.valueOf(textFieldLearningRate.getText());
                adaptiveLR = checkBoxAdaptiveLearningRate.isSelected();
                alpha = Double.valueOf(textFieldAlpha.getText());
                epsilon = Double.valueOf(textFieldEpsilon.getText());
                noHidden = Integer.valueOf(textFieldNoHiddenLayer.getText());
                noNeurPerHidden = Integer.valueOf(textFieldNeurPerHidden.getText());
                try {
                    ctrl = new Controller(stock.getValue().toString(), noEpoch, epsilon, learningRate, limitSEE,
                            adaptiveLR, alpha, noHidden, noNeurPerHidden);
                    Layer out = ctrl.getOutput();
                    s = String.valueOf(out.getNeuron(0).getOutput().recalibreaza());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                textField.setText(s);

            });
        }

        JLabel lblRezultat = new JLabel("Tomorrow's open price");
        lblRezultat.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        lblRezultat.setBounds(10, 250, 200, 20);
        frame.getContentPane().add(lblRezultat);

        textField = new JTextField();
        textField.setBounds(210, 250, 200, 20);
        frame.getContentPane().add(textField);
        textField.setColumns(10);

    }
}
