package com.learnr.core.text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class TermFrequency extends Configured implements Tool {
	/**
	 * class to find term frequencies of documents by using mapreduce
	 * 
	 */
	public static  String wordCountFile = "/home/cloudera/Documents/bq/part-00000";

	public static class Map extends MapReduceBase implements
			Mapper<LongWritable, Text, LongWritable, Text> {
		public void map(LongWritable key, Text value,
				OutputCollector<LongWritable, Text> output, Reporter reporter)
				throws IOException {
			String line = value.toString();
			List<String> list = Lemmatizer.lemmatize(line);
			for (int i = 0; i < list.size(); i++) {
				Text string1 = new Text(list.get(i));
				output.collect(key, string1);
			}

		}
	}

	public static class Reduce extends MapReduceBase implements
			Reducer<LongWritable, Text, LongWritable, Text> {
		public void reduce(LongWritable key, Iterator<Text> values,
				OutputCollector<LongWritable, Text> output, Reporter reporter)
				throws IOException {
			List<String> corpus = Reader.getCorpusWords(wordCountFile);
			System.out.println("Corpus list : "+corpus);
			HashMap<String, Integer> tfMap = new LinkedHashMap<String, Integer>();
			List<String> words = new ArrayList<String>();
			while(values.hasNext())
			{
				String word = values.next().toString();
				words.add(word);
			}
			for (int i = 0; i < corpus.size(); i++) {
				int sum = 0;
				for(int j=0;j<words.size();j++)
				{
					if(words.get(j).equals(corpus.get(i)))
					{
						sum =sum +1;
					}
						
				}
				tfMap.put(corpus.get(i), sum);
			}
			List<Integer> frequency = new ArrayList<Integer>(tfMap.values());

			output.collect(key, new Text(frequency.toString()));
		}
	}

	public int run(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Configuration conf = getConf();
		JobConf job = new JobConf(conf, TermFrequency.class);
		Path in = new Path("/home/cloudera/Documents/input");
		Path out = new Path("/home/cloudera/Documents/TermFrequencyq");
		FileInputFormat.setInputPaths(job, in);
		FileOutputFormat.setOutputPath(job, out);
		job.setJobName("TesrmFrequency");
		job.setMapperClass(Map.class);
		job.setReducerClass(Reduce.class);
		job.setInputFormat(TextInputFormat.class);
		job.setOutputFormat(TextOutputFormat.class);
		job.setOutputKeyClass(LongWritable.class);
		job.setOutputValueClass(Text.class);
		JobClient.runJob(job);
		return 0;

	}

	public static void main(String[] args) throws Exception {
		int res = ToolRunner.run(new Configuration(), new TermFrequency(), args);
		System.exit(res);
	}
}
