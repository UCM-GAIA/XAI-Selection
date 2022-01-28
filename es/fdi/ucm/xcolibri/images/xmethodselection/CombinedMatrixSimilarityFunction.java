package es.fdi.ucm.xcolibri.images.xmethodselection;

import es.ucm.fdi.gaia.jcolibri.exception.NoApplicableSimilarityFunctionException;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;

public class CombinedMatrixSimilarityFunction implements LocalSimilarityFunction {

	LocalSimilarityFunction[] simFunction;
	private double[] weights;


	
	public CombinedMatrixSimilarityFunction(LocalSimilarityFunction[] simFunction, double[] weights) {
		super();
		this.simFunction = simFunction;
		this.weights = weights;
	}

	@Override
	public double compute(Object caseObject, Object queryObject) throws NoApplicableSimilarityFunctionException {
		
		double acum = 0.0;
		for(int s=0; s<weights.length; s++)
		{
			double sim= simFunction[s].compute(caseObject, queryObject);
			acum+= sim* weights[s];
		}
		
		return acum;
		
	}

	@Override
	public boolean isApplicable(Object caseObject, Object queryObject) {
		// TODO Auto-generated method stub
		return true;
	}

}
