package build.cross.rest.v1.beans;

import java.util.ArrayList;
import java.util.List;

public class LoadBean {
	
	private List<String> dates;
	private List<Double> values;
	public List<String> getDates() {
		return dates;
	}
	public List<Double> getValues() {
		return values;
	}
	public LoadBean() {
		super();
		this.dates = new ArrayList<String>();
		this.values = new ArrayList<Double>();
	}
}
