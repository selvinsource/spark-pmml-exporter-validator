import org.apache.spark.mllib.classification.LogisticRegressionWithSGD
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.linalg.Vectors

// Load and parse the data
val data = sc.textFile("../datasets/breastcancerwisconsin_binaryclassification.csv")
val parsedData = data.map { line =>
  val parts = line.split(',')
  LabeledPoint(parts.last.toDouble, Vectors.dense(parts.take(9).map(_.toDouble)))
}

// Build logistic regression model
var logistic = new LogisticRegressionWithSGD().setIntercept(true)
val model = logistic.run(parsedData)
// model.clearThreshold()

// Export logistic regression model to PMML
model.toPMML("../exported_pmml_models/logisticregression.xml")

// Test model on training data
// First from breastcancerwisconsin_binaryclassification.csv (Class: 0)
var predictedValue = model.predict(Vectors.dense(5,1,1,1,2,1,3,1,1))
// Random from breastcancerwisconsin_binaryclassification.csv (Class: 1)
predictedValue = model.predict(Vectors.dense(10,8,10,10,6,1,3,1,10))
