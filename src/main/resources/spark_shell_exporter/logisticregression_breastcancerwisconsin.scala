import org.apache.spark.mllib.classification.LogisticRegressionWithLBFGS
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.linalg.Vectors

// Load and parse the data
val data = sc.textFile("../datasets/breastcancerwisconsin.csv")
val parsedData = data.map { line =>
  val parts = line.split(',')
  LabeledPoint(parts.last.toDouble, Vectors.dense(parts.take(9).map(_.toDouble)))
}

// Build logistic regression model
var logistic = new LogisticRegressionWithLBFGS().setIntercept(true)
val model = logistic.run(parsedData)
// model.clearThreshold()
// model.setThreshold(0.5)
// model.setThreshold(0.7)
// model.setThreshold(0.03)
// model.setThreshold(0.0)
// model.setThreshold(1.0)

// Export logistic regression model to PMML
model.toPMML("../exported_pmml_models/logisticregression_binary.xml")

// Test model on training data
// First from breastcancerwisconsin.csv (Class: 0)
var predictedValue = model.predict(Vectors.dense(5,1,1,1,2,1,3,1,1))
// Random from breastcancerwisconsin.csv (Class: 1)
predictedValue = model.predict(Vectors.dense(10,8,10,10,6,1,3,1,10))
