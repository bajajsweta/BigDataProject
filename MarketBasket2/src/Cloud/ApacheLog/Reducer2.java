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
public class Reducer2 extends MapReduceBase implements Reducer<Text, Text, Text, Text> 
{
	
	

	@Override
	public void reduce(Text key, Iterator<Text> values,
			OutputCollector<Text, Text> output, Reporter arg3)
			throws IOException {
		  int maxCount=0;
		     int similarityCount=0;
		     StringBuilder mostSuggestedProducts = null ;
		  //String[] data = values.toString().split(":");
//		    // loop over the count and tally it up
//		    while (values.hasNext())
//		    {
//		      IntWritable count = values.next();
//		      totalCount += count.get();
//		      i++;
//		    }
//		    int avgRating=totalCount/i;
		  
		  while (values.hasNext()) {
			  String[] data = values.next().toString().split(":");
		    	 String currentSuggestedProduct=new String(data[0].trim());
		    	 int currentScore=Integer.parseInt(data[1].trim());
		    	 
		    	
		    	 
		    	 if(currentScore>maxCount)
		    	 {
		    		 mostSuggestedProducts = new StringBuilder();
		    		 mostSuggestedProducts.append(currentSuggestedProduct);
		    		 maxCount= currentScore;
		    	 }
		    	 else if(currentScore==maxCount&&similarityCount<6)
		    	 {
		    		 similarityCount++;
		    		 mostSuggestedProducts.append(","+currentSuggestedProduct);
		    		 
		    	 }
//		   

    			  
		 
	}
		  output.collect(key,new Text(mostSuggestedProducts.toString())); 
	}

}
