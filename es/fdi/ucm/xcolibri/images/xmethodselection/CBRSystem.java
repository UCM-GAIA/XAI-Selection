package es.fdi.ucm.xcolibri.images.xmethodselection;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import es.ucm.fdi.gaia.jcolibri.casebase.CachedLinealCaseBase;
import es.ucm.fdi.gaia.jcolibri.cbraplications.StandardCBRApplication;
import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCaseBase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.gaia.jcolibri.evaluation.evaluators.LeaveOneOutEvaluator;
import es.ucm.fdi.gaia.jcolibri.exception.ExecutionException;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.RetrievalResult;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNConfig;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.selection.SelectCases;

public class CBRSystem implements StandardCBRApplication{

	public Vector<Image> records;
	public HashMap<String,Vector<Image>> id2img;
	public HashMap<String,Vector<Image>> class2img;
	public HashMap<String,Vector<Image>> xmethod2img;

	private class XCaseBase extends CachedLinealCaseBase{
		public void init(Collection<CBRCase> originalCases) {
			originalCases = new java.util.ArrayList<CBRCase>(originalCases);
			workingCases = new java.util.ArrayList<CBRCase>(originalCases);
		}
	}
	public XCaseBase caseBase;
		
	public void loadData() {
		records = new Vector<Image>();
		id2img = new HashMap<String,Vector<Image>>();
		class2img = new HashMap<String,Vector<Image>>();
		xmethod2img = new HashMap<String,Vector<Image>>();
		
		try (BufferedReader br = new BufferedReader(new FileReader("all.txt"))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		        String[] values = line.split(";");
		        if(values.length != 4)
		        	continue;
		        Image img = new Image(values[0],values[1],values[2],values[3]);
		        records.add(img);
		        add2Hash(id2img, values[0],img);
		        add2Hash(class2img, values[1],img);
		        add2Hash(xmethod2img, values[3],img);

		    }
		}catch (Exception e) {
			System.err.println(e);
		}
		
		//for(String key: xmethod2img.keySet())
		//	System.out.println(key+": "+ xmethod2img.get(key).size());
	}
	
	private void add2Hash(HashMap<String, Vector<Image>> table, String key, Image img) {
		Vector<Image> vector = table.get(key);
		if (vector==null) {
			vector = new Vector<Image>();
			table.put(key,vector);
		}
		vector.add(img);
		
	}

	public void createCaseBase()
	{
		caseBase = new XCaseBase();
		Vector<CBRCase> cases = new Vector<CBRCase>();
		for(String id: id2img.keySet())
		{
			Vector<Image> vector = id2img.get(id);
			Solution solution = new Solution();
			for(Image img: vector)
			{
				if(img.getXmethod().equals("lime")) solution.incLime();
				if(img.getXmethod().equals("anchor")) solution.incAnchor();
				if(img.getXmethod().equals("ig")) solution.incIG();
				if(img.getXmethod().equals("xrai")) solution.incXrai();
			}
			Image first = vector.firstElement();
			Image description = new Image(first.getId(),first.getClassification(),first.getConfidence(),null);
			CBRCase _case = new CBRCase();
			_case.setDescription(description);
			_case.setSolution(solution);
			cases.add(_case);
		}
		
		balanceCases(cases);
		filterByPercentage(cases);
		caseBase.init(cases);	
		//System.out.println(caseBase.getCases().size());
		
		Solution solution = new Solution();
		for(CBRCase c: caseBase.getCases())
		{
			Solution cSol = (Solution)c.getSolution();
			String xmethod = cSol.getMajoritary();
			if(xmethod.equals("lime")) solution.incLime();
			if(xmethod.equals("anchor")) solution.incAnchor();
			if(xmethod.equals("ig")) solution.incIG();
			if(xmethod.equals("xrai")) solution.incXrai();
			
		}
		//System.out.println(solution.toString());
	}
	
	
	// RANDOM BALANCE
	private void balanceCases(Vector<CBRCase> cases) {
		Vector<CBRCase> ig = new Vector<CBRCase>();
		for(CBRCase c: cases) {
			Solution cSol = (Solution)c.getSolution();
			String xmethod = cSol.getMajoritary();
			if(xmethod.equals("ig"))
			{
				ig.add(c);
			}
		}
		cases.removeAll(ig);
		java.util.Random rnd = new Random();
		while(ig.size()>61) {
			int index = rnd.nextInt(ig.size()-1);
			ig.remove(index);
		}
		cases.addAll(ig);
	}
	
	
/*
	private void balanceCases(Vector<CBRCase> cases) {
		Vector<CBRCase> ig = new Vector<CBRCase>();
		for(CBRCase c: cases) {
			Solution cSol = (Solution)c.getSolution();
			String xmethod = cSol.getMajoritary();
			if(xmethod.equals("ig"))
			{
				if(cSol.getIg()<8)
					ig.add(c);
			}
		}
		cases.removeAll(ig);
	}
*/	

	private void filterByPercentage(Vector<CBRCase> cases) {
		if(PERCENTAGE == 100)
			return;
		else
		{
			double size = cases.size();
			int keep =(int) (((double)PERCENTAGE)*size/100.0);
			int toremove = (int)size-keep;
			for(int i=0; i<toremove; i++)
			{
				size = cases.size();
				int index =(int) (Math.random() * size);
				cases.remove(index);
			}
		}
		
	}

	public void cycle(CBRQuery query)  throws ExecutionException 
	{
		NNConfig config = new NNConfig();
		config.setDescriptionSimFunction(new Average());
		
		config.addMapping(new Attribute("id",Image.class), similFunction);
		if(similFunction instanceof FeatureSimilarity)
		{
			((FeatureSimilarity) similFunction).setCases(caseBase.getCases());
			((FeatureSimilarity) similFunction).setQuery((Image)query.getDescription());
		}
		
		List<RetrievalResult> retCases = NNScoringMethod.evaluateSimilarity(caseBase.getCases(), query, config);
		Collection<CBRCase> selCases = SelectCases.selectTopK(retCases, k);
		
		Vector<Solution> solutions = new Vector<Solution>();
		for(CBRCase c: selCases)
			solutions.add((Solution)c.getSolution());
		
		//System.out.println("Query: "+((Image)query.getDescription()).getId());
		//for(CBRCase c: selCases)
		//System.out.println(((Image)c.getDescription()).getId() + "   "+ ((Solution)c.getSolution()).getMajoritary());
		
		
		CBRCase qCase = (CBRCase)query;
		
		String real =  ((Solution)qCase.getSolution()).getMajoritary();
		String predicted = classifier.getClass(solutions);
		
		cMatrix.add(real, predicted);
	}
		
	
	int k; LocalSimilarityFunction similFunction; Classifier classifier; ConfusionMatrix cMatrix;
	
	public CBRSystem(int k, LocalSimilarityFunction similFunction, Classifier classifier, ConfusionMatrix cMatrix)
	{
		this.k = k;
		this.similFunction = similFunction;
		this.classifier = classifier;
		this.cMatrix = cMatrix;
	}

	@Override
	public void configure() throws ExecutionException {
		// TODO Auto-generated method stub
	}

	@Override
	public CBRCaseBase preCycle() throws ExecutionException {
		loadData();
		createCaseBase();
		return this.caseBase;
	}

	@Override
	public void postCycle() throws ExecutionException {
		// TODO Auto-generated method stub
		
	}

	

	
/*
	public static void main(String[] args) {
		int[] kValues = {3};
		LocalSimilarityFunction[] similFunction = new LocalSimilarityFunction[4]; 
	    SimilMatrix smH = new SimilMatrix("histogramMatrix.csv", 1.0);
		SimilMatrix smS = new SimilMatrix("ssimMatrix.csv", 2.0);
		SimilMatrix smP = new SimilMatrix("pixelMatrix.csv", 1.0);
		similFunction[0] = new MatrixSimilarityFunction(smH);
		similFunction[1] = new MatrixSimilarityFunction(smS);
		similFunction[2] = new MatrixSimilarityFunction(smP);
		similFunction[3] = new FeatureSimilarity();
		
		Classifier[] classifier = {new ClassifierSimpleMajority()};
		
		
		for(int k : kValues)
		{
			System.out.println(k);
			for(LocalSimilarityFunction sf: similFunction) 
				for(Classifier classif : classifier)
			{
				ConfusionMatrix cMatrix = new ConfusionMatrix();
				CBRSystem cbr = new CBRSystem(k,sf,classif,cMatrix);				

				
				try {
					CBRCaseBase caseBase = cbr.preCycle();


					ArrayList<CBRCase> cases = new ArrayList<CBRCase>(caseBase.getCases());
					
					String id = "2377698";
					CBRCase query = null;
					for(CBRCase c: cases)
						if(c.getID().equals(id))
							query = c;
									
					//For each case in the case base
					ArrayList<CBRCase> aux = new ArrayList<CBRCase>();
						//Delete the case in the case base
						aux.clear();
						aux.add(query);
						caseBase.forgetCases(aux);
						
						cbr.cycle(query);
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//System.out.println(cMatrix.getAccuracy());
				cMatrix.print(true);
			}
		}
	}
*/	
	public static int PERCENTAGE =100;
/*
	public static void main(String[] args) {
		SimilMatrix smS = new SimilMatrix("ssimMatrix.csv", 2.0);
		//LocalSimilarityFunction similFunction = new MatrixSimilarityFunction(smS);
		LocalSimilarityFunction similFunction = new FeatureSimilarity();
		Classifier classifier = new ClassifierAggregate();//new ClassifierSimpleMajority(); 
		
		for(int cb = 5; cb<=100 ; cb+=5)
		{
			//System.out.println(cb);
			PERCENTAGE = cb;
			ConfusionMatrix cMatrix = new ConfusionMatrix();

			CBRSystem cbr = new CBRSystem(10,similFunction,classifier,cMatrix);				
				LeaveOneOutEvaluator eval = new LeaveOneOutEvaluator();
				eval.init(cbr);
				double accu = 0;
				for(int r=0; r<50; r++)
				{
				cMatrix.reset();
				eval.LeaveOneOut();
				accu+= cMatrix.getAccuracy();
				}
				System.out.println(accu/50);
		}
	}
*/
	
	 
	 //PrINT matrix
	public static void main(String[] args) {
		LocalSimilarityFunction similFunction = new FeatureSimilarity();
		Classifier classifier = new ClassifierSimpleMajority(); 
		
		for(int cb = 100; cb<=100 ; cb+=5)
		{
			//System.out.println(cb);
			PERCENTAGE = cb;
			ConfusionMatrix cMatrix = new ConfusionMatrix();

			CBRSystem cbr = new CBRSystem(10,similFunction,classifier,cMatrix);				
				LeaveOneOutEvaluator eval = new LeaveOneOutEvaluator();
				eval.init(cbr);
				eval.LeaveOneOut();
				cMatrix.print(true);
		}
	}
	
/*
	public static void main(String[] args) {
		int[] kValues = {1, 3, 5, 10, 20, 50};
		LocalSimilarityFunction[] similFunction = new LocalSimilarityFunction[4]; 
	    SimilMatrix smH = new SimilMatrix("histogramMatrix.csv", 1.0);
		SimilMatrix smS = new SimilMatrix("ssimMatrix.csv", 2.0);
		SimilMatrix smP = new SimilMatrix("pixelMatrix.csv", 1.0);
		similFunction[0] = new MatrixSimilarityFunction(smH);
		similFunction[1] = new MatrixSimilarityFunction(smS);
		similFunction[2] = new MatrixSimilarityFunction(smP);
		similFunction[3] = new FeatureSimilarity();
		
		Classifier[] classifier = {new ClassifierSimpleMajority(),new ClassifierAggregate()};
		
		
		for(int k : kValues)
		{
			System.out.println(k);
			for(LocalSimilarityFunction sf: similFunction) 
				for(Classifier classif : classifier)
			{
				ConfusionMatrix cMatrix = new ConfusionMatrix();
				CBRSystem cbr = new CBRSystem(k,sf,classif,cMatrix);				
				LeaveOneOutEvaluator eval = new LeaveOneOutEvaluator();
				eval.init(cbr);
				//eval.LeaveOneOut();
				//System.out.println(cMatrix.getAccuracy());
				double accu = 0;
				for(int r=0; r<50; r++)
				{
				cMatrix.reset();
				eval.LeaveOneOut();
				accu+=cMatrix.getAccuracy();
				}
				System.out.println(accu/50.0);
			}
		}
	}
*/	
}
