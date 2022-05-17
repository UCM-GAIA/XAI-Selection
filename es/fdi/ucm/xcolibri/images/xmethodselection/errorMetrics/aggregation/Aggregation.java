package es.fdi.ucm.xcolibri.images.xmethodselection.errorMetrics.aggregation;

import java.util.Vector;

import es.fdi.ucm.xcolibri.images.xmethodselection.Solution;

public interface Aggregation {
	double[] aggregate(Vector<Solution> solutions);
}
