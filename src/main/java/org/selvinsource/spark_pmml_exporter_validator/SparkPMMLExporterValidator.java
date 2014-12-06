package org.selvinsource.spark_pmml_exporter_validator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.transform.Source;

import org.dmg.pmml.Cluster;
import org.dmg.pmml.FieldName;
import org.dmg.pmml.PMML;
import org.jpmml.evaluator.ClassificationMap;
import org.jpmml.evaluator.ClusteringModelEvaluator;
import org.jpmml.evaluator.Evaluator;
import org.jpmml.evaluator.FieldValue;
import org.jpmml.evaluator.ModelEvaluatorFactory;
import org.jpmml.manager.PMMLManager;
import org.jpmml.model.ImportFilter;
import org.jpmml.model.JAXBUtil;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class SparkPMMLExporterValidator {
	
	private final static String exportedModelsPath = "src/main/resources/exported_pmml_models/";
	
	public enum ModelType {
		KMeansModel,DecisionTreeModel,LogisticRegressionModel,NaiveBayesModel,SVMModel,LinearRegressionModel,RidgeRegressionModel,LassoModel;
    }
	
	public static void main( String[] args ) throws JAXBException, DatatypeConfigurationException, SAXException, IOException
	{
		
		if(args.length != 1){
			System.out.println("Please select a model: KMeansModel, DecisionTreeModel, LogisticRegressionModel, NaiveBayesModel, SVMModel, LinearRegressionModel, RidgeRegressionModel, LassoModel");
			return;
		}
				
	    switch(ModelType.valueOf(args[0])) {
	      case KMeansModel:
	         System.out.println(ModelType.KMeansModel + " selected");
	         evaluateKMeansModel(createEvaluator(exportedModelsPath + "kmeans.xml"));
	         break;
	     case LinearRegressionModel:
	    	 System.out.println(ModelType.LinearRegressionModel + " selected");
	    	 evaluateLinearRegressionModel(createEvaluator(exportedModelsPath + "linearregression.xml"));
	         break;
	     case RidgeRegressionModel:
	    	 System.out.println(ModelType.RidgeRegressionModel + " selected");
	    	 evaluateLinearRegressionModel(createEvaluator(exportedModelsPath + "ridgeregression.xml"));
	         break;
	     case LassoModel:
	    	 System.out.println(ModelType.LassoModel + " selected");
	    	 evaluateLinearRegressionModel(createEvaluator(exportedModelsPath + "lassoregression.xml"));
	         break;
	     default:
	    	 System.out.println("Model selected not implemented");
	    	 return;
	    }

		
	}
	
	@SuppressWarnings("rawtypes")
	private static void evaluateKMeansModel(Evaluator evaluator){
		
		ClusteringModelEvaluator cluster = (ClusteringModelEvaluator)evaluator;
		
		System.out.println("Cluster centers: ");
	    Iterator it = cluster.getEntityRegistry().entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        System.out.println(pairs.getKey() + " = " + ((Cluster)pairs.getValue()).getArray().getValue());
	    }
	    
	    ClassificationMap map;
		
		map = SparkPMMLExporterValidator.<ClassificationMap>evaluate(new Double[]{5.1,3.5,1.4,0.2}, evaluator);
		System.out.println("Cluster assigned to new Double[]{5.1,3.5,1.4,0.2}: " + map.getResult());
		map = SparkPMMLExporterValidator.<ClassificationMap>evaluate(new Double[]{5.9,3.0,5.1,1.8}, evaluator);
		System.out.println("Cluster assigned to new Double[]{5.9,3.0,5.1,1.8}: " + map.getResult());
		
	}
	
	private static void evaluateLinearRegressionModel(Evaluator evaluator){
		
		Number num;

		num = SparkPMMLExporterValidator.<Number>evaluate(new Double[]{8.96,-140.32,-6.3,-6.4,83.5,27.57}, evaluator);
		System.out.println("Predicted value for new Double[]{8.96,-140.32,-6.3,-6.4,83.5,27.57}: " + num);
		num = SparkPMMLExporterValidator.<Number>evaluate(new Double[]{-1.98,-110.0,-4.5,0.7,88.3,25.34}, evaluator);
		System.out.println("Predicted value for new Double[]{-1.98,-110.0,-4.5,0.7,88.3,25.34}: " + num);
		
	}	
	
	private static Evaluator createEvaluator(String filePath) throws SAXException, JAXBException, IOException{
		InputStream is = new FileInputStream(new File(filePath));
		PMML pmml;
		try {
			Source source = ImportFilter.apply(new InputSource(is));
			pmml = JAXBUtil.unmarshalPMML(source);
		} finally {
			is.close();
		}
		PMMLManager pmmlManager = new PMMLManager(pmml);
		return (Evaluator)pmmlManager.getModelManager(ModelEvaluatorFactory.getInstance());		
	} 
	
	@SuppressWarnings("unchecked")
	private static <T> T evaluate(Double[] values, Evaluator evaluator){		
		
		List<FieldName> activeFields = evaluator.getActiveFields();
		
		Map<FieldName, FieldValue> arguments = new LinkedHashMap<FieldName, FieldValue>();
		
		int i = 0;
		
		for(FieldName activeField : activeFields){
		    // The raw (ie. user-supplied) value could be any Java primitive value
		    Object rawValue = values[i];

		    // The raw value is passed through: 1) outlier treatment, 2) missing value treatment, 3) invalid value treatment and 4) type conversion
		    FieldValue activeValue = evaluator.prepare(activeField, rawValue);

		    arguments.put(activeField, activeValue);
		    i++;
		}		

		Map<FieldName, ?> results = evaluator.evaluate(arguments);
		
		FieldName targetName = evaluator.getTargetField();
		return (T)results.get(targetName);	
		
	}	

}
