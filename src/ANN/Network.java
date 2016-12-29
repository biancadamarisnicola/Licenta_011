package ANN;

import Utils.Fraction;

/**
 * Created by bianca on 29.12.2016.
 */
public class Network {
    private int noOfInputs;
    private int noOfOutput;
    private int noOfHiddenLayers;
    private Layer layers[];
    private Fraction epsilon;
    private int noEpoch;
    private Fraction learningRate = new Fraction().valueOf(0.9);

    public Network(int noOfFeatures, int noOfOuputs, int noOfHidden, int noOfNeuronsPerLayer, Fraction epsilon, int noEpoch) {
        noOfInputs = noOfFeatures;
        noOfOutput = noOfOuputs;
        noOfHiddenLayers = noOfHidden;
        this.epsilon = epsilon;
        this.noEpoch = noEpoch;
        int noOfNeuronLayer = noOfNeuronsPerLayer;
        layers = new Layer[noOfNeuronLayer + 2];
        layers[0] = new Layer(noOfInputs, 0);
        layers[1] = new Layer(noOfNeuronLayer, noOfInputs);
        for (int i = 2; i < noOfHiddenLayers + 1; i++) {
            layers[i] = new Layer(noOfNeuronLayer, noOfNeuronLayer);
        }
        layers[noOfHiddenLayers + 1] = new Layer(noOfOutput, noOfNeuronLayer);
        //printNetwork();
    }

    private void printNetwork() {
        System.out.println("Print network");
        for (int i = 0; i < noOfHiddenLayers + 2; i++) {
            System.out.println("LAYER " + i + " :");
            if (i < 4) {
                for (Neuron n : layers[i].getNeurons()) {
                    System.out.println("Output: " + n.getOutput());
                    System.out.print("Weights: ");
                    for (Fraction w : n.getWeights()) {
                        System.out.print(w.toDouble() + ", ");
                    }
                    System.out.println();
                }
            }
        }
    }

    public void learn(Fraction[][] input, Fraction[][] output) {
        boolean stopCondition = false;
        int epoch = 0;
        Fraction[] globalError = new Fraction[input.length];
        while ((!stopCondition) && (epoch < this.noEpoch)){
            System.out.print("EPOCH: "+epoch);
            for (int inputNo = 0; inputNo < input.length; inputNo++){
                activate(input[inputNo]);
                Fraction[] err = new Fraction[output.length];
                globalError[inputNo] = errorComputationRegression(output[inputNo], err);
                //System.out.println(inputNo+" err "+globalError[inputNo].recalibreaza().recalibreaza());
                errorsBackpropagation(err);
            }
            Double SEE = computeSEE(globalError);
            System.out.println(" SEE: "+SEE+" Learning Rate: "+learningRate);
            stopCondition = checkGlobarErr(SEE);
            epoch++;
        }
    }

    private Double computeSEE(Fraction[] globalError) {
        Fraction see = new Fraction();
//        System.out.print(" "+globalError[0].recalibreaza().recalibreaza().div(new Fraction().valueOf(noOfInputs))
//                +" "+globalError[1].recalibreaza().recalibreaza().div(new Fraction().valueOf(noOfInputs))
//                +" "+globalError[2].recalibreaza().recalibreaza().div(new Fraction().valueOf(noOfInputs))+" ");
        for (Fraction err: globalError){
            see = see.add(err.recalibreaza().recalibreaza());
        }
        see = see.div(new Fraction().valueOf(globalError.length));
        double err = Math.sqrt(see.toDouble());
        adjustLearningRate(err);
        return err;
    }

    private void adjustLearningRate(Double err) {
        if(err<400) {
            learningRate = new Fraction().valueOf(Math.log(420-err)).div(new Fraction().valueOf(Math.exp(1)*1.1));
        }
    }

    private boolean checkGlobarErr(Double SEE) {
        if (SEE > 0.225){
            return false;
        }
        return true;
    }

    private void errorsBackpropagation(Fraction[] err) {
        //de la sfarsit spre inceput
        for (int currentLayerNo = noOfHiddenLayers+1; currentLayerNo >=1; currentLayerNo--){
            int i =0;
            for( Neuron n1: layers[currentLayerNo].getNeurons()){
                if (currentLayerNo == noOfHiddenLayers+1){
                    n1.setErrorSoftPlus(err[i]);
                }else{
                    Fraction sumError = new Fraction();
                    for(Neuron n2: layers[currentLayerNo+1].getNeurons()){
                        sumError = sumError.add(n2.getWeight(i).mul(n2.getError()));
                    }
                    n1.setErrorSigmoidal(sumError);
                }
                for (int j =0; j < n1.getNoInputs(); j++){
                    Fraction netWeight = new Fraction();
                    Fraction increment = learningRate.mul(n1.getError().mul(layers[currentLayerNo-1].getNeuron(j).getOutput()));
                    netWeight = n1.getWeight(j).add(increment);
                    //System.out.println("WEIGHTS "+n1.getWeight(j)+" "+netWeight);
                    n1.setWeight(j, netWeight);
                }
                i++;
            }
        }
    }

    private Fraction errorComputationRegression(Fraction[] target, Fraction[] err) {
        Fraction globalError = new Fraction();
        for (int i=0; i< layers[noOfHiddenLayers+1].getNoNeurons();i++){
            err[i] = target[i].sub(layers[noOfHiddenLayers+1].getNeuron(i).getOutput());
//            System.out.println("Target "+target[i].recalibreaza()+" output "+
//                    layers[noOfHiddenLayers+1].getNeuron(i).getOutput().recalibreaza()+ " error "+err[i].recalibreaza());
            globalError = globalError.add(err[i].mul(err[i]));
        }
        return globalError;
    }

    public void activate(Fraction[] input) {
        int i = 0;
        //layer intrare
        for (Neuron neuron : layers[0].getNeurons()) {
            neuron.setOutput(input[i]);
        }
        //restul layerelor
        for (int noCurrentLayer = 1; noCurrentLayer <= noOfHiddenLayers + 1; noCurrentLayer++) {
            for (Neuron neuron: layers[noCurrentLayer].getNeurons()){
                Fraction[] info = new Fraction[neuron.getNoOfInputs()];
                for (int inputNo=0; inputNo < neuron.getNoOfInputs(); inputNo++){
                    info[inputNo] = layers[noCurrentLayer-1].getNeuron(inputNo).getOutput();
                }
                neuron.fireSigmoidal(info);
            }
        }
    }

    public Layer getLayer(int i) {
        return layers[i];
    }
}
