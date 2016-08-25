package controller;

import org.apache.uima.cas.CASException;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.fit.util.CasUtil;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.cleartk.token.type.Token;
import org.mcavallo.opencloud.Cloud;

public abstract class CTKRutaAnnotatorManager implements AnnotatorManager {

	JCas jcas;
	Cloud cloud;

	public abstract void addToCloud();

	protected void addToCloud(String classPath) throws CASException {
		Type type = jcas.getTypeSystem().getType(classPath);
		for (AnnotationFS annotation : CasUtil.select(jcas.getCas(), type)) {
			for (Token token : JCasUtil.selectCovered(jcas, Token.class,
					annotation)) {
				String aux2 = token.getLemma();
				String[] r2 = aux2.split("[.]");
				for (String s : r2) {
					String[] r3 = s.split("(?=\\p{Upper})");
					if (r3.length != 1) {
						for (int i = 1; i < r3.length; i++) {
							cloud.addTag(r3[i]);
						}
					} else
						cloud.addTag(r3[0]);
				}
			}
		}
	}
}
