package es.fdi.ucm.xcolibri.images.xmethodselection.errorMetrics.aggregation;

import java.util.Vector;

import es.fdi.ucm.xcolibri.images.xmethodselection.Solution;

public class AverageAggr implements Aggregation {

	@Override
	public double[] aggregate(Vector<Solution> solutions) {
		double[] predictedVotes = new double[] {0.0, 0.0, 0.0, 0.0};
		double k = (double)solutions.size();
		
		//compute average votes for the k-nns
		for(Solution s: solutions)
		{
			double[] values = s.toArray();
			for(int i=0; i<4; i++)
				predictedVotes[i] += values[i];
		}
		for(int i=0; i<4; i++)
			predictedVotes[i] = predictedVotes[i]/k;
		
		return predictedVotes;
	}

}
