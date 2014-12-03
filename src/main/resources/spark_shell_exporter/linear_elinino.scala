import org.apache.spark.mllib.regression.LinearRegressionWithSGD
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.export.ModelExporter

// Load and parse the data
val data = sc.textFile("../datasets/elnino_linearregression.csv")
val parsedData = data.map { line =>
  val parts = line.split(',')
  LabeledPoint(parts.last.toDouble, Vectors.dense(parts.take(6).map(_.toDouble)))
}

// Build linear regression model
var regression = new LinearRegressionWithSGD().setIntercept(true)
regression.optimizer.setStepSize(0.0001)
val model = regression.run(parsedData)

// Export linear regression model to PMML
ModelExporter.toPMML(model,"../exported_pmml_models/linearregression.xml")

// Test model on training data
// First from elnino_linearregression.csv (expected value 27.32)
model.predict(Vectors.dense(8.96,-140.32,-6.3,-6.4,83.5,27.57))
// Random from elnino_linearregression.csv (expected value 25.45)
model.predict(Vectors.dense(-1.98,-110,-4.5,0.7,88.3,25.34))
