package es.fdi.ucm.xcolibri.images.xmethodselection.errorMetrics;

import java.util.List;
import java.util.Vector;

import es.fdi.ucm.xcolibri.images.xmethodselection.Solution;
import es.fdi.ucm.xcolibri.images.xmethodselection.errorMetrics.XMVotes.XM;
import es.fdi.ucm.xcolibri.images.xmethodselection.errorMetrics.aggregation.Aggregation;

public class MRR implements ErrorMetric {

	@Override
	public double compute(Solution actual, Vector<Solution> predicted, Aggregation aggr) {
		
		List<XMVotes> sortedSolution = XMVotes.sortSolution(actual.toArray());
		double[] aggrSol = aggr.aggregate(predicted);
		List<XMVotes> sortedRanked = XMVotes.sortSolution(aggrSol);
		
		// TODO Auto-generated method stub
		return computeMRR(sortedRanked,sortedSolution);
	}

	public double computeMRR(List<XMVotes> sortedRanked, List<XMVotes> sortedSolution) {
		XM method = sortedSolution.get(0).getMethod();
		for(double i=0; i<sortedRanked.size(); i++)
			if(sortedRanked.get((int)i).getMethod().equals(method))
				return 1.0/(i+1);
		return 0;
	}

	public String toString()
	{
		return "MRR";
	}
	
	public static void main(String[] args)
	  {
		  Solution actual = new Solution(10,30,12,28);
		  Solution ranked = new Solution(10,20,30,40);
		  List<XMVotes> sortedActual = XMVotes.sortSolution(actual.toArray());
		  List<XMVotes> rankedActual = XMVotes.sortSolution(ranked.toArray());
		  MRR mrr = new MRR();
		  
		  System.out.println(mrr.computeMRR(rankedActual, sortedActual));
	  }
}
