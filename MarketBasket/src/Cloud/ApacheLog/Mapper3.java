package Cloud.ApacheLog;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

/**
 * Mapper that takes a line from an Apache access log and emits the IP with a
 * count of 1. This can be used to count the number of times that a host has
 * hit a website.
 */
public class Mapper3 extends MapReduceBase 
implements Mapper<LongWritable, Text, Text, Text> {

	@Override
	public void map(LongWritable key, Text value,
			OutputCollector<Text, Text> output, Reporter arg3)
			throws IOException {
		
		try{
		
		  String[] data = value.toString().split("\\s+");
		
		  
		  String product1=data[0];
		  String product2=data[1];
		  String count=data[2];

		  
		  output.collect(new Text(product1), new Text(product2+" "+count));
		}
		catch(Exception e)
		{
			
		}
		  
		  //String matrixName = matrixData[0].trim();
 
  }

}