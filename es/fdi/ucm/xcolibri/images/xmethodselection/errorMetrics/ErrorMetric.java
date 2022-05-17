package es.fdi.ucm.xcolibri.images.xmethodselection.errorMetrics;

import java.util.Vector;

import es.fdi.ucm.xcolibri.images.xmethodselection.Solution;
import es.fdi.ucm.xcolibri.images.xmethodselection.errorMetrics.aggregation.Aggregation;

public interface ErrorMetric {
	
	public double compute(Solution actual, Vector<Solution> predicted, Aggregation aggr);
}
