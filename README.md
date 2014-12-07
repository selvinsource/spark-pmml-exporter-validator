##Spark PMML Exporter Validator

Using [JPMML Evaluator] to validate the PMML models exported from [Apache Spark].

##Installation
```sh
git clone https://github.com/selvinsource/spark-pmml-exporter-validator.git
cd spark-pmml-exporter-validator
sparkvalidatorpath="$PWD"
sparkshellpath="/home/myuser/git/spark/bin/spark-shell"
mvn clean compile assembly:single
```
Note: 
* Ensure the variable `sparkshellpath` is pointing to your spark-shell 

##Documentaion
For each supported [Apache Spark] MLLib algorithm there is a scala file that generates a simple model and exports it to an xml file in PMML format.   
The scala also runs `model.predict` on some test instances of the training data set.   
The java evaluator (using [JPMML Evaluator] and acting as a decoupled application to [Apache Spark]) loads the exported PMML and run the prediction on the same test instances used for `model.predict`.   
The prediction made by [Apache Spark] and [JPMML Evaluator] produces comparable results, therefore proving the PMML export from [Apache Spark] works as expected.

##K-Means Clustering
```sh
cd src/main/resources/spark_shell_exporter/
$sparkshellpath < kmeans_iris.scala
cd $sparkvalidatorpath 
java -jar target/spark-pmml-exporter-validator-1.0.0-SNAPSHOT-jar-with-dependencies.jar KMeansModel
```

##Linear Regression
```sh
cd src/main/resources/spark_shell_exporter/
$sparkshellpath < linearregression_elinino.scala
cd $sparkvalidatorpath 
java -jar target/spark-pmml-exporter-validator-1.0.0-SNAPSHOT-jar-with-dependencies.jar LinearRegressionModel
```

##Ridge Regression
```sh
cd src/main/resources/spark_shell_exporter/
$sparkshellpath < ridgeregression_elinino.scala
cd $sparkvalidatorpath 
java -jar target/spark-pmml-exporter-validator-1.0.0-SNAPSHOT-jar-with-dependencies.jar RidgeRegressionModel
```

##Lasso Regression
```sh
cd src/main/resources/spark_shell_exporter/
$sparkshellpath < lassoregression_elinino.scala
cd $sparkvalidatorpath 
java -jar target/spark-pmml-exporter-validator-1.0.0-SNAPSHOT-jar-with-dependencies.jar LassoModel
```

##Logistic Regression
...

[JPMML Evaluator]:https://github.com/jpmml/jpmml-evaluator
[Apache Spark]:https://github.com/apache/spark