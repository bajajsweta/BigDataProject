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
public class Reducer3 extends MapReduceBase implements Reducer<Text, Text, Text, Text> 
{
	
	

	@Override
	public void reduce(Text mainProduct, Iterator<Text> productsWithCount,
			OutputCollector<Text, Text> output, Reporter arg3)
			throws IOException {
//		Iterator<Text> products1=products;
     int maxCount=0;
     int similarityCount=0;
     StringBuilder mostSuggestedProducts = null ;
    //mostSuggestedProducts;
    // StringBuilder mostSuggestedProducts = new StringBuilder();
//     List<Text> cache1 = new ArrayList<Text>();
//     List<Text> cache2 = new ArrayList<Text>();
//     System.out.println("key"+arg0);
     while (productsWithCount.hasNext()) {
    	 String[] values=productsWithCount.next().toString().split("\\s+");
    	 String currentSuggestedProduct=new String(values[0].trim());
    	 int currentScore=Integer.parseInt(values[1].trim());
    	 
    	
    	 
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
    	

     }

    		 
    			 output.collect(mainProduct,new Text(mostSuggestedProducts.toString()));  

	
	}

}
