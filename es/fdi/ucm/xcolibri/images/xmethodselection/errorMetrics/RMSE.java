package es.fdi.ucm.xcolibri.images.xmethodselection.errorMetrics;

import java.util.Vector;

import es.fdi.ucm.xcolibri.images.xmethodselection.Solution;
import es.fdi.ucm.xcolibri.images.xmethodselection.errorMetrics.aggregation.Aggregation;

public class RMSE implements ErrorMetric {
	
	
	
	public double compute(Solution actual, Vector<Solution> predicted, Aggregation aggr)
	{
		double[] actualVotes = actual.toArray();

		double[] predictedVotes = aggr.aggregate(predicted);
		
		//return rmse
		double rmse = rmse(actualVotes, predictedVotes);
		if(Double.isNaN(rmse))
		{
			System.out.println(rmse);
			predictedVotes = aggr.aggregate(predicted);
			
		}
		return rmse;
	}

	private static double rmse(double[] actualVotes, double[] predictedVotes) {
		double acum = 0.0;
		for(int i = 0; i< 4; i++)
		{
			double dif = actualVotes[i]-predictedVotes[i];
			acum += (dif*dif);
		}
		acum = acum/4.0;
		return Math.sqrt(acum);
	}

}
