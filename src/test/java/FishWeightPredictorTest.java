import com.examples.flinkml.FishWeightPredictor;
import com.examples.flinkml.StreamingJob;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.InputStream;


class FishWeightPredictorTest {
    static FishWeightPredictor model;

    @BeforeAll
    static void setUp() throws JAXBException, SAXException {
        final InputStream pmml = StreamingJob.class.getResourceAsStream("/fish-weight-model.pmml");
        model = new FishWeightPredictor(pmml);
    }

    @Test
    void malformedPmmlThrows() {

    }
}