package es.fdi.ucm.xcolibri.images.xmethodselection;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;

public class Solution implements CaseComponent {

	Integer lime;
	Integer anchor;
	Integer ig;
	Integer xrai;
	

	public Integer getLime() {
		return lime;
	}

	public void setLime(Integer lime) {
		this.lime = lime;
	}

	public Integer getAnchor() {
		return anchor;
	}

	public void setAnchor(Integer anchor) {
		this.anchor = anchor;
	}

	public Integer getIg() {
		return ig;
	}

	public void setIg(Integer ig) {
		this.ig = ig;
	}




	public Integer getXrai() {
		return xrai;
	}

	public void setXrai(Integer xrai) {
		this.xrai = xrai;
	}

	public Solution(Integer lime, Integer anchor, Integer ig, Integer xrai) {
		super();
		this.lime = lime;
		this.anchor = anchor;
		this.ig = ig;
		this.xrai = xrai;
	}
	
	public Solution() {
		super();
		this.lime =0;
		this.anchor = 0;
		this.ig = 0;
		this.xrai = 0;
	}
	
	public void incLime() {
		this.lime++;
	}
	public void incAnchor() {
		this.anchor++;
	}
	public void incIG() {
		this.ig++;
	}
	public void incXrai() {
		this.xrai++;
	}
	
	public void incLime(int votes) {
		this.lime+=votes;
	}
	public void incAnchor(int votes) {
		this.anchor+=votes;
	}
	public void incIG(int votes) {
		this.ig+=votes;
	}
	public void incXrai(int votes) {
		this.xrai+=votes;
	}

	
	public void inc(String _class)
	{
		if(_class.equals("lime"))
			this.incLime();
		if(_class.equals("anchor"))
			this.incAnchor();
		if(_class.equals("ig"))
			this.incIG();
		if(_class.equals("xrai"))
			this.incXrai();
	}

	public String getMajoritary()
	{
		String c = "lime";
		int max = this.lime;
		
		if(this.anchor>max) {max = this.anchor; c = "anchor";}
		if(this.ig>max) {max = this.ig; c = "ig";}
		if(this.xrai>max) {max = this.xrai; c = "xrai";}
		
		return c;
	}

	@Override
	public String toString() {
		return "Solution [lime=" + lime + ", anchor=" + anchor + ", ig=" + ig + ", xrai=" + xrai + "]";
	}




	@Override
	public Attribute getIdAttribute() {
		// TODO Auto-generated method stub
		return null;
	}

}
