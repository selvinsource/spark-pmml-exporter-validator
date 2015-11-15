import org.apache.spark.mllib.classification.NaiveBayes
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.linalg.Vectors

// Load and parse the data
val data = sc.textFile("../datasets/spectheart.csv")
val parsedData = data.map { line =>
  val parts = line.split(',')
  LabeledPoint(parts.head.toDouble, Vectors.dense(parts.tail.map(_.toDouble)))
}

// Build naive bayes model
val model = NaiveBayes.train(parsedData, lambda = 1.0, modelType = "bernoulli")

// Export naive bayes model to PMML
model.toPMML("../exported_pmml_models/naivebayes_bernoulli.xml")

// Test model on training data
// First from spectheart.csv (Class: 1)
var predictedCluster = model.predict(Vectors.dense(0,0,0,1,0,0,0,1,1,0,0,0,1,1,0,0,0,0,0,0,0,0))
// Last from spectheart.csv (Class: 0)
predictedCluster = model.predict(Vectors.dense(1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0))
