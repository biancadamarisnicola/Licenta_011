package common;

/**
 * Created by bianca on 29.12.2016.
 */
public class Layer {
    private int noOfNeurons;
    private Neuron neurons[];

    public Layer(int noOfNeurons, int noOfInputs) {
        this.noOfNeurons = noOfNeurons;
        neurons = new Neuron[noOfNeurons];
        for (int i = 0; i < noOfNeurons; i++)
            neurons[i] = new Neuron(noOfInputs);
    }

    public Neuron[] getNeurons() {
        return neurons;
    }

    public Neuron getNeuron(int i) {
        return neurons[i];
    }

    @Override
    public String toString() {
        String result = "Layer: ";
        for (int i = 0; i < noOfNeurons; i++) {
            result += this.getNeuron(i).getOutput() + "; ";
        }
        return result;
    }

    public int getNoNeurons() {
        return this.noOfNeurons;
    }
}
