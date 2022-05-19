package es.fdi.ucm.xcolibri.images.xmethodselection;

import java.io.FileWriter;
import java.io.IOException;

import es.fdi.ucm.xcolibri.images.xmethodselection.errorMetrics.ErrorMetric;
import es.fdi.ucm.xcolibri.images.xmethodselection.errorMetrics.MRR;
import es.fdi.ucm.xcolibri.images.xmethodselection.errorMetrics.NDCG;
import es.fdi.ucm.xcolibri.images.xmethodselection.errorMetrics.RMSE;
import es.fdi.ucm.xcolibri.images.xmethodselection.errorMetrics.aggregation.Aggregation;
import es.fdi.ucm.xcolibri.images.xmethodselection.errorMetrics.aggregation.AverageAggr;
import es.fdi.ucm.xcolibri.images.xmethodselection.errorMetrics.aggregation.Baseline;
import es.fdi.ucm.xcolibri.images.xmethodselection.errorMetrics.aggregation.Max;
import es.fdi.ucm.xcolibri.images.xmethodselection.errorMetrics.aggregation.Min;
import es.fdi.ucm.xcolibri.images.xmethodselection.errorMetrics.aggregation.WeightedAverage;
import es.ucm.fdi.gaia.jcolibri.evaluation.evaluators.LeaveOneOutEvaluator;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;

public class Experiment {


		
		public static void main(String[] args) {
			int[] kValues = {1, 3, 5, 10, 25};
			
			LocalSimilarityFunction[] similFunction = new LocalSimilarityFunction[5]; 
		    SimilMatrix smH = new SimilMatrix("histogramMatrix.csv", 1.0);
			SimilMatrix smS = new SimilMatrix("ssimMatrix.csv", 2.0);
			SimilMatrix smP = new SimilMatrix("pixelMatrix.csv", 1.0);
			SimilMatrix smL = new SimilMatrix("latent.csv", 1.0);

			similFunction[0] = new MatrixSimilarityFunction(smP, "P2P");
			similFunction[1] = new MatrixSimilarityFunction(smH, "Histogram");
			similFunction[2] = new MatrixSimilarityFunction(smS, "SSIM");
			similFunction[3] = new MatrixSimilarityFunction(smL, "Latent");
			similFunction[4] = new FeatureSimilarity();
			
			double[] weights = {.5,.10,.15,.40,.30};
			LocalSimilarityFunction comb = new CombinedMatrixSimilarityFunction(similFunction, weights);
			
			Classifier classifier = new ClassifierSimpleMajority();
			
			ErrorMetric[] errorMetrics = {new MRR(),new NDCG(),new RMSE()};
			
			Aggregation[] aggrMethods = {new AverageAggr(), new WeightedAverage(), new Max(), new Min(), new Baseline(new double[] {26,5,61,44})};
			String SEPARATOR = "\t";
			ErrorMetric errorMetric = errorMetrics[1];
			
	    	
			try {
				FileWriter output = new FileWriter(errorMetric+".csv");
				output.append("Aggr"+SEPARATOR+"simFunc");
				for(int k : kValues)
					output.append(SEPARATOR+k);
				output.append(System.lineSeparator());

				System.out.println(errorMetric);
				
				for(Aggregation aggr: aggrMethods)
					for(LocalSimilarityFunction sf: similFunction)
					{
						System.out.print(aggr+SEPARATOR+sf);
						output.append(aggr+SEPARATOR+sf);
						for(int k : kValues)
						{
								ConfusionMatrix cMatrix = new ConfusionMatrix();
								CBRSystem cbr = new CBRSystem(k,sf,classifier,cMatrix, errorMetric, aggr);				
								LeaveOneOutEvaluator eval = new LeaveOneOutEvaluator();
								eval.init(cbr);
								eval.LeaveOneOut();
								System.out.print(SEPARATOR);
								System.out.format("%.3f", cMatrix.getError());
								output.append(SEPARATOR);
								output.append(String.format("%.3f", cMatrix.getError()).replaceAll(",", "."));
						}
						System.out.println();
						output.append(System.lineSeparator());
					}
				
				
				output.flush();
				output.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		
		}
	}

