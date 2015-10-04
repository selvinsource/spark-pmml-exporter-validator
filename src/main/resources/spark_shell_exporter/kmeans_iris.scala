import org.apache.spark.mllib.clustering.KMeans
import org.apache.spark.mllib.linalg.Vectors

// Load and parse the data
val data = sc.textFile("../datasets/iris.csv")
val parsedData = data.map(s => Vectors.dense(s.split(',').take(4).map(_.toDouble)))

// Cluster the data into three classes using KMeans
val numIterations = 20
val numClusters = 3
val kmeansModel = KMeans.train(parsedData, numClusters, numIterations)

// Export clustering model to PMML
kmeansModel.toPMML("../exported_pmml_models/kmeans.xml")

// Test model on training data
// Show cluster centers
val centers = kmeansModel.clusterCenters
// First from iris.csv associated to Iris-setosa
var predictedCluster = kmeansModel.predict(Vectors.dense(5.1,3.5,1.4,0.2))
// Last from iris.csv associated to Iris-virginica
predictedCluster = kmeansModel.predict(Vectors.dense(5.9,3.0,5.1,1.8))
