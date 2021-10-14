package es.fdi.ucm.xcolibri.images.xmethodselection;

import java.util.Collection;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.exception.NoApplicableSimilarityFunctionException;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;

public class FeatureSimilarity implements LocalSimilarityFunction {

	Collection<CBRCase> cases;
	Image query;
	public FeatureSimilarity()
	{
	}
	
	
	
	public void setCases(Collection<CBRCase> cases) {
		this.cases = cases;
	}
	
	public void setQuery(Image query) {
		this.query = query;
	}



	@Override
	public double compute(Object caseObject, Object queryObject) throws NoApplicableSimilarityFunctionException {
		String caseId = (String) caseObject;
		String queryId = (String) queryObject;
		
		Image caseImage = getImage(caseId);
		Image queryImage = query;
		
		String caseClass = caseImage.getClassification();
		String queryClass = queryImage.getClassification();
		
		if(!caseClass.equals(queryClass))
			return 0;
		
		double qF = Double.parseDouble(queryImage.getConfidence());
		double cF = Double.parseDouble(caseImage.getConfidence());
	
		return Math.sqrt(Math.pow(qF-cF, 2));
	}

	private Image getImage(String id) {
		for(CBRCase c: cases)
		{
			Image img = (Image)c.getDescription();
			if(img.getId().equals(id))
				return img;
		}
		return null;
	}
	@Override
	public boolean isApplicable(Object caseObject, Object queryObject) {
		// TODO Auto-generated method stub
		return true;
	}

}
