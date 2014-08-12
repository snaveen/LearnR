package com.learnr.pa.rpx.excel;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.learnr.core.clustering.ClustersGenerator;
import com.learnr.core.clusters_storage.DistanceCalculator;
import com.learnr.core.text.Corpus;
import com.learnr.core.text.StopWords;
import com.learnr.core.text.TermFreqVector;
import com.learnr.core.visualization.ClusterPlot;
import com.learnr.core.visualization.Matrices;
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

		List<TermFreqVector<String>> vectors = abstractCorpus.getTermVectors();
		ClustersGenerator clusterGen = new ClustersGenerator(vectors);
		List<CentroidCluster<Clusterable>> clusters = clusterGen.clusterUsingMultipleKMeansPlusPlus(3, 12);

		// calculating distances between two ids
		DistanceCalculator calc = new DistanceCalculator();
		calc.getDistance(calc.getPoints(vectors), calc.getIDs(vectors));

		for (double distance : calc.getDistance(calc.getPoints(vectors), calc.getIDs(vectors)).keySet()) {
			logger.info("distance :" + distance + " id "
					+ calc.getDistance(calc.getPoints(vectors), calc.getIDs(vectors)).get(distance));
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
