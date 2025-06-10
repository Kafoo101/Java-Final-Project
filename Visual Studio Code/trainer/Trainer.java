package main.trainer;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.doccat.DocumentSampleStream;
import opennlp.tools.doccat.DoccatFactory;
import opennlp.tools.util.InputStreamFactory;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;
import opennlp.tools.util.ObjectStream;
import java.io.*;
public class Trainer {
    public static void main(String[] args) {
        try {
            InputStreamFactory dataIn = () -> new FileInputStream("train_data/TrainingDataV3.1.txt");
            ObjectStream<String> lineStream = new PlainTextByLineStream(dataIn, "UTF-8");
            ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);

            TrainingParameters params = new TrainingParameters();
            params.put(TrainingParameters.ITERATIONS_PARAM, 100);
            params.put(TrainingParameters.CUTOFF_PARAM, 1);
            params.put(TrainingParameters.ALGORITHM_PARAM, "MAXENT");

            DoccatFactory factory = new DoccatFactory();
            DoccatModel model = DocumentCategorizerME.train("en", sampleStream, params, factory);

            File outFile = new File("model/newsCategorizerModelV3.1.bin");
            try (OutputStream modelOut = new BufferedOutputStream(new FileOutputStream(outFile))) {
                model.serialize(modelOut);
            }

            System.out.println("Model successfully trained and saved.");

        } catch (IOException e) {
            System.err.println("Error during training: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
