package main.cleartk;

import java.util.Vector;

import org.apache.uima.cas.CASException;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.fit.util.CasUtil;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.cleartk.token.type.Token;
import org.mcavallo.opencloud.Cloud;

public class ComposedClTKRutaAnnotatorManager extends CTKRutaAnnotatorManager {

	Vector<String> annotators = new Vector<String>();
	
	public ComposedClTKRutaAnnotatorManager(Vector<String> an,JCas cas, Cloud c)
	{
		jcas=cas;
		cloud=c;
		annotators =an;
	}
	
	public void addToCloud()  {
		for (String a: annotators)
		{
			try {
				super.addToCloud(a);
			} catch (CASException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
}
	
}
