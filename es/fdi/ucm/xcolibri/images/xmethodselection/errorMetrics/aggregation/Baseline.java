package es.fdi.ucm.xcolibri.images.xmethodselection.errorMetrics.aggregation;

import java.util.Vector;

import es.fdi.ucm.xcolibri.images.xmethodselection.Solution;

public class Baseline implements Aggregation {

	double[] vector;
	
	public Baseline(double[] vector) {
		this.vector = vector;
	}
	
	@Override
	public double[] aggregate(Vector<Solution> solutions) {
		return vector;
	}

	public String toString()
	{
		return "Baseline";
	}
}
