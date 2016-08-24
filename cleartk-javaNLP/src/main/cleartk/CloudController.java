package main.cleartk;

import java.io.IOException;
import java.util.Hashtable;

import org.apache.uima.UIMAException;
import org.apache.uima.cas.CASException;
import org.mcavallo.opencloud.Cloud;

public interface CloudController extends Runnable {
	
	public abstract Cloud updateCloud(Hashtable<String, Boolean> selected, Cloud c) throws CASException;
	public abstract  void executeNLP() throws IOException, UIMAException;
}
