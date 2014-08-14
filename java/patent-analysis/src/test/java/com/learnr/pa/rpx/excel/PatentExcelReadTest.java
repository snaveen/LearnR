package com.learnr.pa.rpx.excel;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.learnr.core.clustering.ClusterUtil;
import com.learnr.core.clustering.ClustersGenerator;
import com.learnr.core.text.Corpus;
import com.learnr.core.text.StopWords;
import com.learnr.core.text.TermFreqVector;
import com.learnr.core.text.TfIdfRealMatrix;
import com.learnr.core.visualization.ClusterPlot;
import com.learnr.pa.rpx.excel.bean.PatAbstract;
import com.learnr.util.Verify;
import com.learnr.util.excel.GenericExcelReader;

public class PatentExcelReadTest {

	private static final Logger logger = LoggerFactory.getLogger(PatentExcelReadTest.class);

	// data sets paths
	private static final String BASE_DIR = "src/main/resources/sample-files-rpx/";

	private static final String PATS = BASE_DIR + "pats.xls";
	private static final String PAT_ABSTRACTS = BASE_DIR + "pat_abstracts.xlsx";
	private static final String PAT_DESCRIPTIONS = BASE_DIR + "pat_descriptions.xlsx";
	private static final String PAT_CLAIMS = BASE_DIR + "pat_claims.xlsx";

	@Before
	public void init() {

	}

	@After
	public void destroy() {

	}

	@Test
	public void read_pats_test() throws InterruptedException {

		File f = new File(PAT_ABSTRACTS);
		GenericExcelReader<PatAbstract> ger = new GenericExcelReader<PatAbstract>(f);
		List<PatAbstract> pats = ger.read(PatAbstract.class);

		logger.info("Total no of patents found : " + pats.size());

		Map<String, String> patAbsMap = this.getIdToAbstractTextMap(pats);

		Corpus<String> abstractCorpus = new Corpus<String>(patAbsMap);
		abstractCorpus.process();

		List<String> dimenVector = abstractCorpus.getDimensionVector();
		logger.info("dimenstion vector size : " + dimenVector.size());

		List<TermFreqVector<String>> list = abstractCorpus.getTermVectors();
		List<Map<String, Integer>> listq = new ArrayList<Map<String, Integer>>();
		logger.info("Creating list of termFrequenciesVectors");
		for (int i = 0; i < list.size(); i++) {
			listq.add((Map<String, Integer>) list.get(i).getTermFrequency());
		}

		RealMatrix matrix = new Array2DRowRealMatrix();
		TfIdfRealMatrix realMatrix = new TfIdfRealMatrix();
		matrix = realMatrix.tFIdfMatrix(listq, dimenVector);
		List<Double> weights = realMatrix.finalWeightList(matrix);
		System.out.println(weights);

		List<TermFreqVector<String>> vectors = abstractCorpus.getTermVectors();
		ClustersGenerator clusterGen = new ClustersGenerator(vectors);
		List<CentroidCluster<Clusterable>> clusters = clusterGen.clusterUsingMultipleKMeansPlusPlus(3, 12);

		// calculating distances between two ids
		ClusterUtil cUtil = new ClusterUtil();
		cUtil.getDistance(cUtil.getPoints(vectors), cUtil.getIDs(vectors));

		for (double distance : cUtil.getDistance(cUtil.getPoints(vectors), cUtil.getIDs(vectors)).keySet()) {
			logger.info("distance :" + distance + " id "
					+ cUtil.getDistance(cUtil.getPoints(vectors), cUtil.getIDs(vectors)).get(distance));
		}
		// plot
		ClusterPlot.clustersPlot(clusters, 3, 4);
		Thread.sleep(150000);



		List<Clusterable> cPoints;
		Set<String> clusterVocab;

		int index = 1;
		for (CentroidCluster<Clusterable> centroidCluster : clusters) {
			cPoints = centroidCluster.getPoints();

			if (cPoints == null)
				cPoints = new ArrayList<Clusterable>();

			logger.info(" ----- Details of Cluster " + index);
			logger.info(" Size : " + cPoints.size());

			// Print cluster vocabulary
			clusterVocab = new HashSet<String>();
			for (Clusterable clusterable : cPoints) {
				TermFreqVector<String> tfv = (TermFreqVector<String>) clusterable;

				logger.info(" Patent ID : " + tfv.getId());

				clusterVocab.addAll(tfv.getTermFrequency().keySet());
			}

			clusterVocab.removeAll(StopWords.STANDARD_STOPWORDS);

			// logger.info(" Vocabulary : " + clusterVocab);
			logger.info(" Vocabulary size : " + clusterVocab.size());

			index++;
		}

	}

	private Map<String, String> getIdToAbstractTextMap(List<PatAbstract> pats) {
		Verify.notEmpty(pats, "Empty or null");

		Map<String, String> paMap = new HashMap<String, String>();
		for (PatAbstract pa : pats) {
			if (pa == null)
				continue;

			paMap.put(pa.getPatentId(), pa.getAbstractText());
		}

		return paMap;
	}

}
