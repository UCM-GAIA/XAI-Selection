package es.fdi.ucm.xcolibri.images.xmethodselection.errorMetrics.aggregation;

import java.util.Vector;

import es.fdi.ucm.xcolibri.images.xmethodselection.Solution;

public class Min implements Aggregation {

	@Override
	public double[] aggregate(Vector<Solution> solutions) {
		double[] predictedVotes = new double[] {Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE};
		
		//compute average votes for the k-nns
		for(Solution s: solutions)
		{
			double[] values = s.toArray();
			for(int i=0; i<4; i++)
				predictedVotes[i] = Math.min(predictedVotes[i], values[i]);
		}	
		return predictedVotes;
	}

}
