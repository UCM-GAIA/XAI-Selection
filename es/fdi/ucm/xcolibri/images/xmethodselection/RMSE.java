package es.fdi.ucm.xcolibri.images.xmethodselection;

import java.util.Vector;

public class RMSE {
	public static double computeRMSE(Solution actual, Vector<Solution> predicted)
	{
		double[] actualVotes = actual.toArray();
		double[] predictedVotes = new double[] {0.0, 0.0, 0.0, 0.0};
		
		//compute average votes for the k-nns
		for(Solution s: predicted)
		{
			double[] values = s.toArray();
			for(int i=0; i<4; i++)
				predictedVotes[i] += values[i];
		}
		for(int i=0; i<4; i++)
			predictedVotes[i] = predictedVotes[i]/(double)predicted.size();
		
		//return rmse
		double rmse = rmse(actualVotes, predictedVotes);
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
