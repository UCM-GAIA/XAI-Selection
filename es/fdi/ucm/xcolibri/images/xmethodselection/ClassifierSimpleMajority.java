package es.fdi.ucm.xcolibri.images.xmethodselection;

import java.util.Vector;

public class ClassifierSimpleMajority implements Classifier {

	@Override
	public String getClass(Vector<Solution> solutions) {
		Solution sol = new Solution();
		for(Solution s: solutions)
			sol.inc(s.getMajoritary());
		return sol.getMajoritary();
	}

}
