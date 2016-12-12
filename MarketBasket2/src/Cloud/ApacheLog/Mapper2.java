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
		 
		  
		  String product1=data[0];
		  String product2=data[1];
		  String rating2=data[2];
		  //System.out.println(data0);
		  //System.out.println(data1);
		  //String[] data3=product2AndRating.split(":");
		  //String product2=data3[0].trim();
		 // String rating=data3[1].trim();
		  
		  String finalValue=product1+" "+product2;
		// IntWritable rating1 = new IntWritable(Integer.parseInt(rating));
		  
		 if(!product1.trim().equals(product2.trim()))
		 {
			 output.collect(new Text(product1+" "+product2), new IntWritable(Integer.parseInt(rating2)));
		 }
		}
		catch(Exception e)
		{
			System.out.print("Exception"+e.getMessage());
		}
		  
		  //String matrixName = matrixData[0].trim();
 
  }

}