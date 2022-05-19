package es.fdi.ucm.xcolibri.images.xmethodselection;

import es.ucm.fdi.gaia.jcolibri.exception.NoApplicableSimilarityFunctionException;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;

public class MatrixSimilarityFunction implements LocalSimilarityFunction {

	SimilMatrix simMatrix;
	String id;
	public MatrixSimilarityFunction(SimilMatrix simMatrix, String id)
	{
		this.simMatrix = simMatrix;
		this.id = id;
	}
	
	@Override
	public double compute(Object caseObject, Object queryObject) throws NoApplicableSimilarityFunctionException {
		// TODO Auto-generated method stub
		String caseId = (String) caseObject;
		String queryId = (String) queryObject;
		
		
		
		return this.simMatrix.getSimilarity(caseId, queryId);
	}

	@Override
	public boolean isApplicable(Object caseObject, Object queryObject) {
		// TODO Auto-generated method stub
		return true;
	}

	public String toString()
	{
		return id;
	}
	
	
}
