package main.cleartk;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.TypeSystem;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.util.CasUtil;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.Sofa;
import org.apache.uima.jcas.cas.TOP;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.jcas.tcas.DocumentAnnotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.InvalidXMLException;
import org.cleartk.classifier.CleartkAnnotator;
import org.cleartk.classifier.Feature;
import org.cleartk.classifier.Instance;
import org.cleartk.classifier.feature.extractor.CleartkExtractor;
import org.cleartk.classifier.feature.extractor.CleartkExtractor.Count;
import org.cleartk.classifier.feature.extractor.CleartkExtractor.Covered;
import org.cleartk.classifier.feature.extractor.simple.CoveredTextExtractor;
import org.cleartk.examples.type.UsenetDocument;
import org.cleartk.token.type.Token;
import org.cleartk.type.test.Lemma;





import com.google.common.hash.HashCode;

import bsh.org.objectweb.asm.Type;

public class Tester extends CleartkAnnotator<String> {
	private boolean arr[];

	private CleartkExtractor extractor;

	public void setBoleans(boolean a[]) {
		arr = a;
	}

	public void initialize(UimaContext context) throws ResourceInitializationException {
		System.out.println("ini");

		this.extractor = new CleartkExtractor(Token.class, new CoveredTextExtractor(), new Count(new Covered()));
	}

	public static AnalysisEngineDescription getDescription() throws ResourceInitializationException {
		return AnalysisEngineFactory.createPrimitiveDescription(Tester.class);
	}

	public void process(JCas jCas) throws AnalysisEngineProcessException {

		 // System.out.println("boooooooooooooooooooooooom");
		 CAS cas = jCas.getCas();
		 org.apache.uima.cas.Type type =
		 cas.getTypeSystem().getType("uima.ruta.annotators.SingleLineComment");
		 System.out.println(type.getName());
		 System.out.println(cas.getDocumentText());
		 // System.out.println("boooooooooooooooooooooooom1");
	
		 // System.out.println("boooooooooooooooooooooooom111");

			for (AnnotationFS annotation : CasUtil.select(jCas.getCas(), type)) {
				System.out.println(annotation.getCoveredText());
				for (Token token : JCasUtil.selectCovered(jCas, Token.class, annotation)) {
					System.out.println("entro al forr de los token");
					System.out.println("Entre: " + token.getLemma());
					String aux2 = token.getLemma();
					String[] r2 = aux2.split("[.]");
					for (String s : r2) {
						String[] r3 = s.split("(?=\\p{Upper})");
						if (r3.length != 1) {
							for (int i = 1; i < r3.length; i++) {
								System.out.println(r3[i]);
							}
						} else
							System.out.println(r3[0]);
					}
		
//		  System.out.println(jCas.getDocumentText());
//		  System.out.println("IMPRIMO TODOS LOS TOKEN");
//		  for (Token token : JCasUtil.select(jCas, Token.class)) {
//		  System.out.print(token.getCoveredText() + " ");
//		  }
//		  System.out.println(" ");
//		  System.out.println("IMPRIMO TODOS LOS STEM");
//		  for (Token token : JCasUtil.select(jCas, Token.class)) {
//		  System.out.print(token.getStem() + " ");
//		  }
//		  System.out.println(" ");
//		  System.out.println("IMPRIMO TODOS LOS LEMMA");
//		  for (Token token : JCasUtil.select(jCas, Token.class)) {
//		  System.out.print(token.getLemma() + " ");
//		  }
		
		

//		TypeSystem a2 = jCas.getTypeSystem();
//		Iterator<org.apache.uima.cas.Type> types2 = a2.getTypeIterator();
//		Vector<org.apache.uima.cas.Type> tipos2 = new Vector<org.apache.uima.cas.Type>();
//		while (types2.hasNext()) {
//			System.out.println(types2.next().getName());
//			// tipos.add(types.next());
		//}

	}
			}}
}