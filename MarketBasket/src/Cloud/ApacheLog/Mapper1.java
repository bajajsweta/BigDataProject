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
public class Mapper1 extends MapReduceBase 
implements Mapper<LongWritable, Text, Text, Text> {

	@Override
	public void map(LongWritable key, Text value,
			OutputCollector<Text, Text> output, Reporter arg3)
			throws IOException {
		
		try{
		
		  String[] data = value.toString().split("(?=((\\S*\\s){2})*\\S*$)[\\s,]+");
		
		  
		  String date1=data[1];
		  //System.out.println(date1);
		  String[] splittedDates = date1.toString().split(",");
		  String[] monthday=splittedDates[0].toString().split("\\s+");
		  String[] part3=data[2].toString().split("\\s+");

		  String month=monthday[0].replace("\"", "");
		  String year=part3[0].replace("\"", "");

		  //System.out.println(monthday[0]+data[0]);
		  String key1=month+year+data[0];
		  String value1=part3[1];
		  
		  output.collect(new Text(key1), new Text(value1));
		}
		catch(Exception e)
		{
			
		}
		  
		  //String matrixName = matrixData[0].trim();
 
  }

}