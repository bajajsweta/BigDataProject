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
public class Reducer1 extends MapReduceBase implements Reducer<Text, Text, Text, Text> 
{
	
	

	@Override
	public void reduce(Text arg0, Iterator<Text> products,
			OutputCollector<Text, Text> output, Reporter arg3)
			throws IOException {
		Iterator<Text> products1=products;
     int i=0;
     List<Text> cache1 = new ArrayList<Text>();
     List<Text> cache2 = new ArrayList<Text>();
     System.out.println("key"+arg0);
     while (products.hasNext()) {
    	 String value12=new String(products.next().toString());
    	 String value22=new String(value12);
    	// System.out.println(products.next().toString());
    	 
         cache1.add(new Text(value12));
         cache2.add(new Text(value22));

     }

    for(Text p1:cache1)
    {
    	 for(Text p2:cache2)
    	    {
    		System.out.println(p1.toString());
    		System.out.println(p2.toString());
    		 
    			 output.collect(p1, p2);  
 }
	}
	}

}
