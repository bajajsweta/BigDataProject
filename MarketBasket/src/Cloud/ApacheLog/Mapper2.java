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
public class Mapper2 extends MapReduceBase 
implements Mapper<LongWritable, Text, Text, IntWritable> {
	 private static final IntWritable one = new IntWritable(1);
	@Override
	public void map(LongWritable key, Text value,
			OutputCollector<Text, IntWritable> output, Reporter arg3)
			throws IOException {
		
		try{
		
		  String[] data = value.toString().split("\\s+");
		 
		  
		  String date0=data[0];
		  String date1=data[1];
		  System.out.println(date0);
		  System.out.println(date1);
		 if(date0.trim().equals(date1.trim()))
		 {
			  System.out.println("not ok!!!");
			 
		 }
		 else
		 {
			 System.out.println("ok!!!");
			 output.collect(value, one);
		 }
		}
		catch(Exception e)
		{
			
		}
		  
		  //String matrixName = matrixData[0].trim();
 
  }

}