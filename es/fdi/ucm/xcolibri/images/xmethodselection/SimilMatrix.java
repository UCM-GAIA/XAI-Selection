package es.fdi.ucm.xcolibri.images.xmethodselection;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Vector;

public class SimilMatrix {

	HashMap<String, HashMap<String, Double>> table;
	Vector<String> ids;
	public SimilMatrix(String file, double normalize) {
		table = new HashMap<String, HashMap<String, Double>>();
		ids = new Vector<String>();
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
		    String line = br.readLine();
		    String[] images = line.split(";");
		    for(int i =1; i<images.length; i++)
		    {
		    	String id = images[i].split("__")[0];
		    	ids.add(id);
		    	table.put(id, new HashMap<String, Double>());
		    }
		   int row = 0;
		   while ((line = br.readLine()) != null) {
		        String rowId = ids.elementAt(row);
			    String[] values = line.split(";");
		        for(int col=1; col<values.length; col++) {
		        	String colId = ids.elementAt(col-1);
		        	Double val = Double.parseDouble(values[col]);
		        	table.get(rowId).put(colId, val/normalize);
		        }
		        row++;
		   } 
		   br.close();
	    }
		catch (Exception e) {
			System.err.println(e);
		}
	}
	
	public double getSimilarity(String id1, String id2)
	{
		return this.table.get(id1).get(id2);
	}
	
	
	public static void main(String[] args) {
		SimilMatrix sm = new SimilMatrix("histogramMatrix.csv", 1.0);

	}

}
