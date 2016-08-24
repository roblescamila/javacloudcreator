package main.cleartk;

import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.jcas.JCas;

public interface NLPAnalyzer {

	
	public abstract JCas executeNLP() throws UIMAException, IOException;
}
