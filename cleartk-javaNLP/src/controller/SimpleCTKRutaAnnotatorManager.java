package controller;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.Feature;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.fit.util.CasUtil;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.cleartk.classifier.feature.extractor.CleartkExtractor;
import org.cleartk.classifier.feature.extractor.CleartkExtractor.Count;
import org.cleartk.classifier.feature.extractor.CleartkExtractor.Covered;
import org.cleartk.classifier.feature.extractor.simple.CoveredTextExtractor;
import org.cleartk.token.type.Token;
import org.mcavallo.opencloud.Cloud;


public class SimpleCTKRutaAnnotatorManager extends CTKRutaAnnotatorManager {

	// boolean selected;
	String classPath;
	

	// path : "uima.ruta.annotators.MethodName"
	public SimpleCTKRutaAnnotatorManager(String path, JCas cas, Cloud c) {
		classPath = path;
		jcas = cas;
		cloud = c;
	}


	public void addToCloud()  {
		// TODO Auto-generated method stub
		try {
			super.addToCloud(classPath);
		} catch (CASException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}





	
}