package es.fdi.ucm.xcolibri.images.xmethodselection;

import java.io.FileWriter;
import java.io.IOException;

import es.fdi.ucm.xcolibri.images.xmethodselection.errorMetrics.ErrorMetric;
import es.fdi.ucm.xcolibri.images.xmethodselection.errorMetrics.MRR;
import es.fdi.ucm.xcolibri.images.xmethodselection.errorMetrics.aggregation.Aggregation;
import es.fdi.ucm.xcolibri.images.xmethodselection.errorMetrics.aggregation.AverageAggr;
import es.ucm.fdi.gaia.jcolibri.evaluation.evaluators.LeaveOneOutEvaluator;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;

public class ExperimentCaseBaseGrow {


		
		public static void main(String[] args) {
			int k = 10;
			
			SimilMatrix smL = new SimilMatrix("latent.csv", 1.0);
			LocalSimilarityFunction similFunction  = new MatrixSimilarityFunction(smL, "Latent");
			
			Classifier classifier = new ClassifierSimpleMajority();
			
			ErrorMetric errorMetric = new MRR();
			
			Aggregation aggrMethod = new AverageAggr();
		
			
			String SEPARATOR = ",";
			
	    	
			try {
				FileWriter output = new FileWriter(errorMetric+"-grow.csv");
				
				System.out.println(errorMetric);
				
				for(int cb = 5; cb<=100 ; cb+=5)
				{
					//System.out.println(cb);
					CBRSystem.PERCENTAGE = cb;
					ConfusionMatrix cMatrix = new ConfusionMatrix();
					CBRSystem cbr = new CBRSystem(k,similFunction,classifier,cMatrix, errorMetric, aggrMethod);				
					LeaveOneOutEvaluator eval = new LeaveOneOutEvaluator();
					eval.init(cbr);
					eval.LeaveOneOut();
					System.out.print(String.format("%.3f", cMatrix.getError()).replaceAll(",", "."));
					System.out.print(SEPARATOR);
					output.append(String.format("%.3f", cMatrix.getError()).replaceAll(",", "."));
					output.append(SEPARATOR);						
				}
				output.flush();
				output.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		
		}
	}

