package controller;

import org.apache.uima.cas.CASException;
import org.apache.uima.jcas.JCas;
import org.mcavallo.opencloud.Cloud;

public class SimpleCTKRutaAnnotatorManager extends CTKRutaAnnotatorManager {

	// boolean selected;
	String classPath;
	
	public SimpleCTKRutaAnnotatorManager(String path, JCas cas, Cloud c) {
		classPath = path;
		jcas = cas;
		cloud = c;
	}

	public void addToCloud()  {
		try {
			super.addToCloud(classPath);
		} catch (CASException e) {
			e.printStackTrace();
		}
	}
}