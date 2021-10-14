package es.fdi.ucm.xcolibri.images.xmethodselection;

import java.util.Vector;

public interface Classifier {
	public String getClass(Vector<Solution> solutions);
}
