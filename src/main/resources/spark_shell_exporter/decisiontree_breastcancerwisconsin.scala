import org.apache.spark.mllib.tree.DecisionTree
import org.apache.spark.mllib.tree.model.DecisionTreeModel
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.linalg.Vectors

// Load and parse the data
val data = sc.textFile("../datasets/breastcancerwisconsin.csv")
val parsedData = data.map { line =>
  val parts = line.split(',')
  LabeledPoint(parts.last.toDouble, Vectors.dense(parts.take(9).map(_.toDouble)))
}

// Train a DecisionTree model.
//  Empty categoricalFeaturesInfo indicates all features are continuous.
val numClasses = 2
val categoricalFeaturesInfo = Map[Int, Int]()
val impurity = "gini"
val maxDepth = 5
val maxBins = 32

val model = DecisionTree.trainClassifier(parsedData, numClasses, categoricalFeaturesInfo,
  impurity, maxDepth, maxBins)


// Export decision tree model to PMML
model.toPMML("../exported_pmml_models/decisiontree_classification.xml")

// Test model on training data
// First from breastcancerwisconsin.csv (Class: 0)
var predictedValue = model.predict(Vectors.dense(5,1,1,1,2,1,3,1,1))
// Random from breastcancerwisconsin.csv (Class: 1)
predictedValue = model.predict(Vectors.dense(10,8,10,10,6,1,3,1,10))
