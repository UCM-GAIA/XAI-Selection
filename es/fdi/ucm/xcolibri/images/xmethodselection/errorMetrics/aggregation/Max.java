package es.fdi.ucm.xcolibri.images.xmethodselection.errorMetrics.aggregation;

import java.util.Vector;

import es.fdi.ucm.xcolibri.images.xmethodselection.Solution;

public class Max implements Aggregation {

	@Override
	public double[] aggregate(Vector<Solution> solutions) {
		double[] predictedVotes = new double[] {0.0, 0.0, 0.0, 0.0};
		
		//compute average votes for the k-nns
		for(Solution s: solutions)
		{
			double[] values = s.toArray();
			for(int i=0; i<4; i++)
				predictedVotes[i] = Math.max(predictedVotes[i], values[i]);
		}	
		return predictedVotes;
	}
	public String toString()
	{
		return "Max";
	}
}
