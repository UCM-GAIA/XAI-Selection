package es.fdi.ucm.xcolibri.images.xmethodselection.errorMetrics;

import java.util.List;
import java.util.Vector;

import es.fdi.ucm.xcolibri.images.xmethodselection.Solution;
import es.fdi.ucm.xcolibri.images.xmethodselection.errorMetrics.aggregation.Aggregation;

public class NDCG implements ErrorMetric {

		
	@Override
	public double compute(Solution actual, Vector<Solution> predicted, Aggregation aggr) {
		
		List<XMVotes> sortedSolution = XMVotes.sortSolution(actual.toArray());
		double[] aggrSol = aggr.aggregate(predicted);
		
		
		//Normalize
		double totalActual = actual.getTotalVotes();
		double totalPred = 0.0;
		for(int i=0; i<aggrSol.length; i++) totalPred+=aggrSol[i];
		double ratio = totalActual/totalPred;
		for(int i=0; i<aggrSol.length; i++) aggrSol[i]*=ratio;
		//end normalize
		
		
		List<XMVotes> sortedRanked = XMVotes.sortSolution(aggrSol);
		
		// TODO Auto-generated method stub
		return computeNDCG(sortedRanked,sortedSolution);
	}
	  
	private double computeNDCG(
		      List<XMVotes> ranked_items,
		      List<XMVotes> correct_items) {
		    

		    double dcg   = 0;
		    double idcg  = 0;
		    
		 // compute iDCG part		
		    for (int i = 0; i < correct_items.size(); i++) {
		    	  double rel = 1+correct_items.get(i).getVotes(); 
		    	  double div = Math.log(2) / Math.log(i + 1);
		    	  if(Double.isInfinite(div))
		    		  div = 1;
			      idcg += (Math.pow(2, rel) -1) / 1;       
			    }		    

		      // compute NDCG part		    
		    for (int i = 0; i < ranked_items.size(); i++) {
		    	 double div = Math.log(2) / Math.log(i + 1);
		    	  if(Double.isInfinite(div))
		    		  div = 1;
		      dcg += ranked_items.get(i).getVotes() / div;       
		    }

		    return dcg / idcg;
		  }


	public String toString()
	{
		return "NDCG";
	}
		  
		  
		  

		  
		  public static void main(String[] args)
		  {
			  Solution sol = new Solution(10,30,12,28);
			  List<XMVotes> sorted = XMVotes.sortSolution(sol.toArray());
			  System.out.println(sorted);
		  }
	
}
