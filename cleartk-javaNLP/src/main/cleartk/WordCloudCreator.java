package main.cleartk;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.io.IOUtils;
import org.apache.uima.UIMAException;
import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.admin.CASFactory;
import org.apache.uima.cas.impl.XmiCasDeserializer;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.util.CasIOUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.apache.uima.util.CasCreationUtils;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLInputSource;
import org.mcavallo.opencloud.Cloud;
import org.xml.sax.SAXException;

public class WordCloudCreator implements Runnable {

	static final int COMMENT = 0;
	static final int CLASSNAME = 1;
	static final int METHODNAME = 2;
	static final int VARNAME = 3;
	static final int PACKAGE = 4;
	static final int IMPORT = 5;
	
	CountDownLatch cdl = null;
	JCas jcas;
	FileInputStream fisTargetFile;

	public WordCloudCreator() {
	}

	public WordCloudCreator(String f, CountDownLatch c) throws IOException,
			UIMAException {
		fisTargetFile = new FileInputStream(new File(f));
		this.cdl = c;
	}

	public Cloud updateCloud(boolean arr[], Cloud c) throws CASException {

		if (arr[COMMENT]) {
			UimaRutaAnnotator a = new UimaRutaAnnotator(
					"uima.ruta.annotators.SingleLineComment", jcas, c);
			UimaRutaAnnotator b = new UimaRutaAnnotator(
					"uima.ruta.annotators.MultiLineComment", jcas, c);
			a.addToCloud();
			b.addToCloud();
		}

		if (arr[CLASSNAME]) {
			UimaRutaAnnotator a = new UimaRutaAnnotator(
					"uima.ruta.annotators.ClassName", jcas, c);
			a.addToCloud();
		}

		if (arr[METHODNAME]) {
			UimaRutaAnnotator a = new UimaRutaAnnotator(
					"uima.ruta.annotators.MethodName", jcas, c);
			a.addToCloud();
		}

		if (arr[VARNAME]) {
			UimaRutaAnnotator a = new UimaRutaAnnotator(
					"uima.ruta.annotators.VarName", jcas, c);
			a.addToCloud();
		}

		if (arr[PACKAGE]) {
			UimaRutaAnnotator a = new UimaRutaAnnotator(
					"uima.ruta.annotators.Package", jcas, c);
			a.addToCloud();
		}

		if (arr[IMPORT]) {
			UimaRutaAnnotator a = new UimaRutaAnnotator(
					"uima.ruta.annotators.Import", jcas, c);
			a.addToCloud();
		}

		return c;
	}

	@Override
	public void run() {
		String targetFileStr;
		try {
			targetFileStr = IOUtils.toString(fisTargetFile, "UTF-8");
			ClearTKProcessor nlp = new ClearTKProcessor(targetFileStr);
			jcas = nlp.executeClearTK();
			cdl.countDown();
		} catch (UIMAException | IOException e) {
			e.printStackTrace();
		}
	}
}