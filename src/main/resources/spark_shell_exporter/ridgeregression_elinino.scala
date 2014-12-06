import org.apache.spark.mllib.regression.RidgeRegressionWithSGD
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.export.ModelExporter

// Load and parse the data
val data = sc.textFile("../datasets/elnino_linearregression.csv")
val parsedData = data.map { line =>
  val parts = line.split(',')
  LabeledPoint(parts.last.toDouble, Vectors.dense(parts.take(6).map(_.toDouble)))
}

// Build ridge regression model
var regression = new RidgeRegressionWithSGD().setIntercept(true)
regression.optimizer.setStepSize(0.0001)
val model = regression.run(parsedData)

// Export ridge regression model to PMML
ModelExporter.toPMML(model,"../exported_pmml_models/ridgeregression.xml")

// Test model on training data
// First from elnino_linearregression.csv (air temperature: 27.32)
var predictedValue = model.predict(Vectors.dense(8.96,-140.32,-6.3,-6.4,83.5,27.57))
// Random from elnino_linearregression.csv (air temperature: 25.45)
predictedValue = model.predict(Vectors.dense(-1.98,-110,-4.5,0.7,88.3,25.34))
