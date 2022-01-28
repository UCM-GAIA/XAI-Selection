package es.fdi.ucm.xcolibri.images.xmethodselection;

import java.util.Vector;

public class ConfusionMatrix {
	public enum XMethod  {lime, anchor, ig, xrai};
	int dim = XMethod.values().length;
	public int matrix[][];
	public double precision[];
	public double recall[];
	
	public Vector<Double> rmse; 
	
	public double getPrecision() {
		return prec;
	}

	public double getRecall() {
		return rec;
	}

	public double getAccuracy() {
		return accu;
	}
	
	public double getRMSE()
	{
		return this.avgRMSE;
	}

	double prec, rec, accu, avgRMSE;
	
	public ConfusionMatrix()
	{
		matrix = new int[dim][dim];
		precision = new double[dim];
		recall = new double[dim];
		rmse = new Vector<Double>();
		reset();
	}
	
	public void reset() {
		for(int i=0; i<dim; i++)
			for(int j=0; j<dim; j++)
				matrix[i][j] =0;
	}

	public void add(String real, String predicted, double error)
	{
		XMethod r = XMethod.valueOf(real);
		XMethod p = XMethod.valueOf(predicted);
		matrix[r.ordinal()][p.ordinal()]++;
		rmse.add(error);

		updateMetrics();
	}
	
	private void updateMetrics()
	{
		//recall
		for(int r=0; r<dim; r++)
		{
			double TP = 0, FN = 0;
			for(int p=0; p<dim; p++)
			{
				if(r==p) TP = matrix[r][p];
				else FN+=matrix[r][p];
			}
			if(TP+FN==0)
				recall[r]=0;
			else
				recall[r] = TP/(TP+FN);
		}
		
		for(int p=0; p<dim; p++)
		{
			double TP = 0, FP = 0;
			for(int r=0; r<dim; r++)
			{
				if(r==p) TP = matrix[r][p];
				else FP+=matrix[r][p];
			}
			if(TP+FP==0)
				precision[p]=0;
			else
				precision[p] = TP/(TP+FP);
		}
		
		prec = 0;
		for(int i=0; i<dim; i++)
			prec+=precision[i];
		prec=prec/dim;
		
		rec = 0;
		for(int i=0; i<dim; i++)
			rec+=recall[i];
		rec=rec/dim;
		
		double total = 0;
		for(int i=0; i<dim; i++)
			for(int j=0; j<dim; j++)
				total+=matrix[i][j];
		double TP = 0;
		for(int i=0; i<dim; i++) {
			TP+=matrix[i][i];
		}
		accu = TP/total;
		
		
		//RMSE
		double acum = 0.0;
		for(Double d: rmse)
			acum+= d;
		avgRMSE = acum / (double)rmse.size();
	}
	
	public void print(boolean printMatrix)
	{
		if(printMatrix) {
		System.out.println("R\\P\t lime\t anchor\t ig\t xrai\t recall");
		for(int r=0; r<dim; r++)
		{
			String line = XMethod.values()[r].toString();
			for(int c=0; c<dim; c++)
				line+="\t "+matrix[r][c];
			line+= "\t "+String.format("%.2f",recall[r]);
			System.out.println(line);
		}
		String prec = "Prec.";
		for(int c=0; c<dim; c++) 
			prec+="\t "+String.format("%.2f",precision[c]);
		System.out.println(prec);
		System.out.println();
		}
		System.out.println("Precision: "+ String.format("%.2f",this.prec));
		System.out.println("Recall: "+ String.format("%.2f",this.rec));
		System.out.println("Accuracy: "+ String.format("%.2f",this.accu));
		System.out.println("RMSE: "+ String.format("%.2f",this.avgRMSE));
	}
}
