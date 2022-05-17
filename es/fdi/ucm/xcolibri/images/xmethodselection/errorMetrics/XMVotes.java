package es.fdi.ucm.xcolibri.images.xmethodselection.errorMetrics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import es.fdi.ucm.xcolibri.images.xmethodselection.errorMetrics.XMVotes.XM;

public class XMVotes{
	
	public enum XM  {LIME, ANCHOR, IG, XRAI}

	
	XM method;
	double votes;
	
	public XMVotes() {
		
	}
	
	public XMVotes(XM method, double votes) {
		super();
		this.method = method;
		this.votes = votes;
	}
	public XM getMethod() {
		return method;
	}
	public void setMethod(XM method) {
		this.method = method;
	}
	public double getVotes() {
		return votes;
	}
	public void setVotes(double votes) {
		this.votes = votes;
	}
	
	
	  public static  List<XMVotes> sortSolution(double[] actual)
	  {
			List<XMVotes> votes = new ArrayList<XMVotes>();
			XMVotes lime = new XMVotes(XM.LIME,actual[0]);
			XMVotes anchors = new XMVotes(XM.ANCHOR,actual[1]);
			XMVotes ig = new XMVotes(XM.IG,actual[2]);
			XMVotes xrai = new XMVotes(XM.XRAI, actual[3]);
			votes.add(lime);
			votes.add(anchors);
			votes.add(ig);
			votes.add(xrai);
			
			Collections.sort(votes, Comparator.comparing(XMVotes::getVotes));
			Collections.reverse(votes);
			return votes;
	  }
	
}