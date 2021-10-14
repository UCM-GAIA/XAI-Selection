package es.fdi.ucm.xcolibri.images.xmethodselection;

import java.util.Vector;

public class ClassifierAggregate implements Classifier {

	@Override
	public String getClass(Vector<Solution> solutions) {
		Solution sol = new Solution();
		for(Solution s: solutions) {
			sol.incAnchor(s.getAnchor());
			sol.incIG(s.getIg());
			sol.incLime(s.getLime());
			sol.incXrai(s.getXrai());
		}
		return sol.getMajoritary();
	}

}
