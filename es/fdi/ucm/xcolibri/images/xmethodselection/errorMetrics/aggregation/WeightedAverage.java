package es.fdi.ucm.xcolibri.images.xmethodselection.errorMetrics.aggregation;

import java.util.Vector;

import es.fdi.ucm.xcolibri.images.xmethodselection.Solution;

public class WeightedAverage implements Aggregation {

	Vector<Double> similarities;
	
	public void setSimilarities(Vector<Double> sims)
	{
		this.similarities = sims;
	}
	
	@Override
	public double[] aggregate(Vector<Solution> solutions) {
		double[] predictedVotes = new double[] {0.0, 0.0, 0.0, 0.0};
		double k = (double)solutions.size();
		double accumSim = 0.0;
		for(double d: similarities)
			accumSim+=d;
		if(accumSim==0)
			accumSim = 1;
		double normRatio = k/accumSim;
		
		//compute average votes for the k-nns
		for(int i=0; i<4; i++)
		{
			for(int s=0; s<solutions.size(); s++)
			{
				double[] values = solutions.get(s).toArray();
				predictedVotes[i] += (values[i]*similarities.get(s));
			}
		}
		
		for(int i=0; i<4; i++)
			predictedVotes[i] = normRatio * predictedVotes[i] / k;
		
		return predictedVotes;
	}

}
