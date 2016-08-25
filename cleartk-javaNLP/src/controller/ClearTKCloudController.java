package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;

import model.ClearTKProcessor;
import model.NLPAnalyzer;

import org.apache.commons.io.IOUtils;
import org.apache.uima.UIMAException;
import org.apache.uima.cas.CASException;
import org.apache.uima.jcas.JCas;
import org.mcavallo.opencloud.Cloud;

public class ClearTKCloudController implements CloudController {

	CountDownLatch cdl = null;
	JCas jcas;
	FileInputStream fisTargetFile;

	public ClearTKCloudController() {}

	public ClearTKCloudController(String f, CountDownLatch c)
			throws IOException, UIMAException {
		fisTargetFile = new FileInputStream(new File(f));
		this.cdl = c;
	}

	public Cloud updateCloud(Hashtable<String, Boolean> selected, Cloud c)
			throws CASException {
		if (selected.get("Comments")) {
			Vector<String> aux = new Vector<String>();
			aux.add("uima.ruta.annotators.SingleLineComment");
			aux.add("uima.ruta.annotators.MultiLineComment");
			AnnotatorManager a = new ComposedCTKRutaAnnotatorManager(aux, jcas, c);
			a.addToCloud();
		}

		if (selected.get("Classes")) {
			AnnotatorManager a = new SimpleCTKRutaAnnotatorManager(
					"uima.ruta.annotators.ClassType", jcas, c);
			a.addToCloud();
		}

		if (selected.get("Methods")) {
			AnnotatorManager a = new SimpleCTKRutaAnnotatorManager(
					"uima.ruta.annotators.MethodName", jcas, c);
			a.addToCloud();
		}

		if (selected.get("Variables")) {
			AnnotatorManager a = new SimpleCTKRutaAnnotatorManager(
					"uima.ruta.annotators.VarName", jcas, c);
			a.addToCloud();
		}

		if (selected.get("Packages")) {
			AnnotatorManager a = new SimpleCTKRutaAnnotatorManager(
					"uima.ruta.annotators.Package", jcas, c);
			a.addToCloud();
		}

		if (selected.get("Imports")) {
			AnnotatorManager a = new SimpleCTKRutaAnnotatorManager(
					"uima.ruta.annotators.Import", jcas, c);
			a.addToCloud();
		}
		return c;
	}

	public void executeNLP() throws IOException, UIMAException {
		String targetFileStr;
		targetFileStr = IOUtils.toString(fisTargetFile, "UTF-8");
		NLPAnalyzer nlp = new ClearTKProcessor(targetFileStr);
		jcas = nlp.executeNLP();
	}

	@Override
	public void run() {

		try {
			this.executeNLP();
		} catch (UIMAException | IOException e) {
			e.printStackTrace();
		}
		cdl.countDown();
	}
}