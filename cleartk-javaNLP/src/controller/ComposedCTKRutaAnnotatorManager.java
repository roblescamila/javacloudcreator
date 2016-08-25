package controller;

import java.util.Vector;

import org.apache.uima.cas.CASException;
import org.apache.uima.jcas.JCas;
import org.mcavallo.opencloud.Cloud;

public class ComposedCTKRutaAnnotatorManager extends CTKRutaAnnotatorManager {

	Vector<String> annotators = new Vector<String>();

	public ComposedCTKRutaAnnotatorManager(Vector<String> an, JCas cas, Cloud c) {
		jcas = cas;
		cloud = c;
		annotators = an;
	}

	public void addToCloud() {
		for (String a : annotators) {
			try {
				super.addToCloud(a);
			} catch (CASException e) {
				e.printStackTrace();
			}
		}
	}
}
