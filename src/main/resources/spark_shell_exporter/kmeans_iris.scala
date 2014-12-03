import org.apache.spark.mllib.clustering.KMeans
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.export.ModelExporter

// Load and parse the data
val data = sc.textFile("../datasets/iris_kmeans.csv")
val parsedData = data.map(s => Vectors.dense(s.split(',').map(_.toDouble)))

// Cluster the data into three classes using KMeans
val numIterations = 20
val numClusters = 3
val kmeansModel = KMeans.train(parsedData, numClusters, numIterations)

// Export clustering model to PMML
ModelExporter.toPMML(kmeansModel,"../exported_pmml_models/kmeans.xml")

// Test model on training data
// First from iris_kmeans.csv associated to Iris-setosa (expected cluster = 0)
kmeansModel.predict(Vectors.dense(5.1,3.5,1.4,0.2))
// Last from iris_kmeans.csv associated to Iris-virginica (expected cluster = 2)
kmeansModel.predict(Vectors.dense(5.9,3.0,5.1,1.8))