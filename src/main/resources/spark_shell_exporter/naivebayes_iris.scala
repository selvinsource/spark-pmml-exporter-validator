import org.apache.spark.mllib.classification.NaiveBayes
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.linalg.Vectors

// Load and parse the data
val data = sc.textFile("../datasets/iris.csv")
val parsedData = data.map { line =>
  val parts = line.split(',')
  LabeledPoint(parts.last.toDouble, Vectors.dense(parts.take(4).map(_.toDouble)))
}

// Build naive bayes model
val model = NaiveBayes.train(parsedData, lambda = 1.0, modelType = "multinomial")

// Export naive bayes model to PMML
model.toPMML("../exported_pmml_models/naivebayes_multinomial.xml")

// Test model on training data
// First from iris.csv associated to Iris-setosa (0)
var predictedCluster = model.predict(Vectors.dense(5.1,3.5,1.4,0.2))
// Random from iris.csv associated to Iris-Versicolour (1)
var predictedCluster = model.predict(Vectors.dense(6.2,2.9,4.3,1.3))
// Last from iris.csv associated to Iris-virginica (2)
predictedCluster = model.predict(Vectors.dense(5.9,3.0,5.1,1.8))
