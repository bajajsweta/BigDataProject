package Cloud.ApacheLog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

/**
 * Counts all of the hits for an ip. Outputs all ip's
 */
public class Reducer2 extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable> 
{
	
	

	@Override
	public void reduce(Text pair, Iterator<IntWritable> values,
			OutputCollector<Text, IntWritable> output, Reporter arg3)
			throws IOException {
		 int totalCount = 0;
		    
		    // loop over the count and tally it up
		    while (values.hasNext())
		    {
		      IntWritable count = values.next();
		      totalCount += count.get();
		    }
		   
		    output.collect(pair, new IntWritable(totalCount));
		 
	}

}
