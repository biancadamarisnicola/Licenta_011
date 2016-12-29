import ANN.Controller;
import ANN.Layer;
import Utils.Fraction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;

/**
 * Created by bianca on 19.08.2016.
 */
public class Main {
    private JFrame frame;
    private Controller ctrl;
    private JTextField textField;
    private int noEpoch;
    private Fraction epsilon;

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
        initialize();
        this.noEpoch = 300;
        this.epsilon = new Fraction(BigInteger.ONE).valueOf(0.001);
        this.ctrl = new Controller(noEpoch, epsilon);
    }

    private void initialize() {
        //setBounds(x,y,w,h)
        frame = new JFrame();
        frame.setBounds(100, 100, 600, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblTitle = new JLabel("Aproximarea valorii de close");
        lblTitle.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        lblTitle.setBounds(80, 11, 400, 50);
        frame.getContentPane().add(lblTitle);

        textField = new JTextField();
        textField.setBounds(90, 84, 500, 20);
        frame.getContentPane().add(textField);
        textField.setColumns(10);

        JButton btnCalculate = new JButton("Calculeaza");
        btnCalculate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                String s = null;
                try {
                    Layer out = ctrl.getOutput();
                    s = String.valueOf(out.getNeuron(0).getOutput().recalibreaza());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                textField.setText(s);

            }
        });

        btnCalculate.setBounds(150, 125, 130, 23);
        frame.getContentPane().add(btnCalculate);

        JLabel lblRezultat = new JLabel("Rezultat");
        lblRezultat.setBounds(10, 87, 70, 14);
        frame.getContentPane().add(lblRezultat);
    }
}
