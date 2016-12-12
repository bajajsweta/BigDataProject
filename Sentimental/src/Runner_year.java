import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Runner_year {

  public static class TokenizerMapper
       extends Mapper<LongWritable, Text, Text, Text>{

	  // Reusable IntWritable for the count
	  private static final IntWritable one = new IntWritable(1);
	  

    public void map(LongWritable key, Text value, Context context
                    ) throws IOException, InterruptedException {
    	
    	
    String data[]=	value.toString().split("\t");
    	if(!data[2].equals("reviewTime"))
    			{
    String id =data[7];
    String comment=data[0];
    String overall =data[4];
    String replaced=data[2].replaceAll("\"" ," ");
    String year=replaced.substring(replaced.trim().length()-4);
   
    

 	    	context.write(new Text(id+"\t"+year), new Text(comment+"\t"+overall));

 	  
      }
    }
    }
  

  
    
       
    public static class DataReducer
    extends Reducer<Text,Text,Text,Text> {
 private IntWritable result = new IntWritable();
 private Set<String> positive = new HashSet<String>();
 private Set<String> negitive = new HashSet<String>();
 
 @Override
 protected void setup(Context context) throws IOException, InterruptedException {
 	try{
 		//System.out.println("HIIIIIIIIIIIIIIIIIIIIIII");
 		Path[] stopWordsFiles = DistributedCache.getLocalCacheFiles(context.getConfiguration());
 		if(stopWordsFiles != null && stopWordsFiles.length > 0) {
 			for(Path stopWordFile : stopWordsFiles) {
 				readFile(stopWordFile);
 			}
 		}
 	} catch(IOException ex) {
 		System.err.println("Exception in mapper setup: " + ex.getMessage());
 	}
 }

 private void readFile(Path filePath) {
		try{
			//BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath.toString()));
			
			FileSystem fs = FileSystem.get(new Configuration());
		    BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(fs.open(new Path("hdfs://localhost:54310/words/positive-words.txt"))));
		    String stopWord = null;
			while((stopWord = bufferedReader.readLine()) != null) {
			
				positive.add(stopWord.toLowerCase());
			}
			FileSystem fs1 = FileSystem.get(new Configuration());
		    BufferedReader bufferedReader1=new BufferedReader(new InputStreamReader(fs1.open(new Path("hdfs://localhost:54310/words/negative-words.txt"))));
		    String negtive = null;
			while((negtive = bufferedReader1.readLine()) != null) {
				
				negitive.add(negtive.toLowerCase());
			}
		} catch(IOException ex) {
			System.err.println("Exception while reading stop words file: " + ex.getMessage());
		}
	}
 public void reduce(Text key, Iterable<Text> values,
                    Context context
                    ) throws IOException, InterruptedException {
	 double positiveCount=0;
	 double negativeCount=0;
	 double positiveRatio=0;
	 int count=0;
	 double rating=0;
	 for(Text value1:values)
	 {
		 count++;
		 String value[]=value1.toString().split("\t");
		 if(value[1].toString().length()>1 || value[1].toString().equals("?") )
		 {
			 rating+=2;
		 }
		 else
		 {
		 rating+=Double.parseDouble(value[1].toString());
		 }
	 String data[]=	value[0].toString().split(" ");
	 for(int i=0;i<data.length;i++)
	 {
	if(positive.contains(data[i]))
	{
		positiveCount++;
		
	}
	else if (negitive.contains(data[i]))
	{
		negativeCount++;
	}
	 }
	 
	 
	 
}
	// System.out.println(positiveCount+"  -"+negativeCount);
	 if((positiveCount+negativeCount)!=0)
	 {//System.out.println("hellloooooo");
		// System.out.println(positiveCount-negativeCount);
		 //System.out.println(positiveCount+negativeCount);
	positiveRatio=(double)((positiveCount-negativeCount)/(positiveCount+negativeCount));
	//System.out.println("what!!"+(positiveCount-negativeCount));
	 //double negativeRatio=negativeCount/data.length;
	 
	 }
	 String sentiment="";
	 if(positiveRatio<=.3)
	 {
		 sentiment="Negative";
	 }
	 else if (positiveRatio>.3 && positiveRatio <=.35)
	 {
		 sentiment="Neutral";
	 }
	 else if (positiveRatio>.35 )
	 {
		 sentiment="Positive";
	 }
	
	 Double averageRating=rating/count;
	 context.write(new Text(key),new Text(sentiment+"\t"+count+"\t"+averageRating) );
    
    

  
    }
    }
    
    

  public static void main(String[] args) throws Exception {
   Configuration conf = new Configuration();
 
    
    Job job2 = Job.getInstance(conf, "word count");
    DistributedCache.addCacheFile(new Path("hdfs://localhost:54310/words/negative-words.txt").toUri(), job2.getConfiguration());
    DistributedCache.addCacheFile(new Path("hdfs://localhost:54310/words/positive-words.txt").toUri(), job2.getConfiguration());
    
    job2.setJarByClass(Runner_year.class);
    job2.setMapperClass(TokenizerMapper.class);
    //job.setCombinerClass(IntSumReducer.class);
   job2.setReducerClass(DataReducer.class);
    job2.setNumReduceTasks(1);
    job2.setOutputKeyClass(Text.class);
    job2.setOutputValueClass(Text.class);
    org.apache.hadoop.mapreduce.lib.input.TextInputFormat.addInputPath(job2, new Path(args[0]));
    FileOutputFormat.setOutputPath(job2, new Path(args[1]));
    job2.waitForCompletion(true);
  
   
  
  }
  }
