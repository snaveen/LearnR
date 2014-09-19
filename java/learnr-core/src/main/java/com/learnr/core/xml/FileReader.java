package com.learnr.core.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

public class FileReader {
	/**
	 * method to create a new xml file
	 * 
	 * @param Id
	 *            (name given to the new created file)
	 * @param patent
	 *            (inputs of the patent in the form of list)
	 */
	public static void newFileReader(LongWritable Id, List<Text> patent) {

		FileOutputStream fop = null;
		File file;
		String a = Id.toString() + ".xml";
		String filePath = "/home/cloudera/Documents/patentsfinal/";
		String path = filePath.concat(a);
		// String content = "This is the text content";
		// Text a = new Text("new");
		try {

			file = new File(path);
			fop = new FileOutputStream(file);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			// get the content in bytes
			for (int i = 0; i < patent.size(); i++) {
				byte[] contentInBytes = patent.get(i).getBytes();

				fop.write(contentInBytes);
			}

			fop.flush();
			fop.close();

			// System.out.println("Done");

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
