package es.fdi.ucm.xcolibri.images.xmethodselection;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;

public class Image implements CaseComponent {
	private String id;
	private String classification;
	private String confidence;
	private String xmethod;
	public Image(String id, String classification, String confidence, String xmethod) {
		super();
		this.id = id;
		this.classification = classification;
		this.confidence = confidence;
		this.xmethod = xmethod;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getClassification() {
		return classification;
	}
	public void setClassification(String classification) {
		this.classification = classification;
	}
	public String getConfidence() {
		return confidence;
	}
	public void setConfidence(String confidence) {
		this.confidence = confidence;
	}
	public String getXmethod() {
		return xmethod;
	}
	public void setXmethod(String xmethod) {
		this.xmethod = xmethod;
	}
	@Override
	public String toString() {
		return "Image [id=" + id + ", classification=" + classification + ", confidence=" + confidence + ", xmethod="
				+ xmethod + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Image other = (Image) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id", Image.class);
	}
	
	
	
	
}
