Spark PMML Exporter Validator
=============================

Using JPMML Evaluator to validate the PMML models exported from Spark

Installation
git clone https://github.com/selvinsource/spark-pmml-exporter-validator.git
cd spark-pmml-exporter-validator
sparkvalidatorpath="$PWD"
sparkshellpath="/home/myuser/git/spark/bin/spark-shell"
mvn clean compile assembly:single

Documentaion
For each machine learning algorithm there is a scala file that produces the pmml export and saves it in an xml file. 
The scala also runs the model.predict on some test instances of the training data set. 
The java evaluator (using JPMML) load the exported pmml and run the predict on the same test instances. 
The predictions made by Spark and JPMML evaluator should be produce compareble results, therefore proving the pmml export from Spark is working as expected. 
double check thesis for terminology: instances is ok?

K-Means Clustering
cd src/main/resources/spark_shell_exporter/
$sparkshellpath < kmeans_iris.scala
cd $sparkvalidatorpath 
java -jar target/spark-pmml-exporter-validator-1.0.0-SNAPSHOT-jar-with-dependencies.jar KMeansModel

Linear Regression
cd src/main/resources/spark_shell_exporter/
$sparkshellpath < linearregression_elinino.scala
cd $sparkvalidatorpath 
java -jar target/spark-pmml-exporter-validator-1.0.0-SNAPSHOT-jar-with-dependencies.jar LinearRegressionModel

Ridge Regression
cd src/main/resources/spark_shell_exporter/
$sparkshellpath < ridgeregression_elinino.scala
cd $sparkvalidatorpath 
java -jar target/spark-pmml-exporter-validator-1.0.0-SNAPSHOT-jar-with-dependencies.jar RidgeRegressionModel

Lasso Regression
cd src/main/resources/spark_shell_exporter/
$sparkshellpath < lassoregression_elinino.scala
cd $sparkvalidatorpath 
java -jar target/spark-pmml-exporter-validator-1.0.0-SNAPSHOT-jar-with-dependencies.jar LassoModel

Logistic Regression
...