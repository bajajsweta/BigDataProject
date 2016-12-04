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

public class Runner {

  public static class TokenizerMapper
       extends Mapper<LongWritable, Text, Text, IntWritable>{

	  // Reusable IntWritable for the count
	  private static final IntWritable one = new IntWritable(1);
	  

    public void map(LongWritable key, Text value, Context context
                    ) throws IOException, InterruptedException {
    	
    String data[]=	value.toString().split("\t");
    String id =data[7];
    
    	
    	 
 	    	context.write(new Text(id), one);
 	     
 	    
 	    
 	  
      }
    }
  

  public static class IntSumReducer
       extends Reducer<Text,IntWritable,Text,IntWritable> {
    private IntWritable result = new IntWritable();
    public TreeMap<String, Integer> topValue=new TreeMap<String, Integer>();

    public void reduce(Text key, Iterable<IntWritable> values,
                       Context context
                       ) throws IOException, InterruptedException {
    	int count=0;
    
     for(IntWritable value : values)
     {
    	count++; 
     }
     
     context.write(key,new IntWritable(count));
 	
 
  }
  }
  
    
    public static class TopMapper
    extends Mapper<LongWritable, Text, NullWritable, Text>{

	  // Reusable IntWritable for the count

    	public TreeMap<Integer, Text> topValue=new TreeMap<Integer, Text>();
   

 public void map(LongWritable key, Text value, Context context
                 ) throws IOException, InterruptedException {
 	
 String data[]=	value.toString().split("\t");
 String count =data[1];

 topValue.put(Integer.parseInt(count), new Text(value));
 
 if(topValue.size()>500)
 {
	 System.out.println("firstKey"+topValue.get(topValue.firstKey()));
	  topValue.remove(topValue.firstKey());
 }
 
 
  
	  
   }
 
 public void cleanup(Context context) throws IOException, InterruptedException
 {
 	for(Text value:topValue.values())
 	{
 		System.out.println("data"+topValue.get(topValue.firstKey()));
 		context.write(NullWritable.get(),value);
 	}
 }
 }
    
    public static class DataMapper
    extends Mapper<LongWritable, Text, Text, Text>{

	  // Reusable IntWritable for the count

    	
 public void map(LongWritable key, Text value, Context context
                 ) throws IOException, InterruptedException {
	 Configuration conf=context.getConfiguration();
		String top=conf.get("top");
	 
 String data[]=	value.toString().split("\t");
 String id =data[7];
 String review=data[0];
 
if(top.contains(id))
 {
	context.write(new Text(id),new Text(review));
	
 }
 
	  
   }

 }
   
    
    
    public static class TopReducer
    extends Reducer<NullWritable,Text,NullWritable,Text> {
 private IntWritable result = new IntWritable();
 public TreeMap<Integer, Text> topValue=new TreeMap<Integer, Text>();

 public void reduce(NullWritable key, Iterable<Text> values,
                    Context context
                    ) throws IOException, InterruptedException {
	 
	 for(Text value:values)
	 {
		 System.out.println(value.toString());
	 String data[]=	value.toString().split("\t");
	 String count =data[1];
	 topValue.put(Integer.parseInt(count), new Text(value));
	 
	 if(topValue.size()>500)
	 {
		  topValue.remove(topValue.firstKey());
	 }
	 }
	 for(Text value:topValue.values())
	 	{
	 		System.out.println("r"+topValue.get(topValue.firstKey()));
	 		context.write(NullWritable.get(),value);
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
 		System.out.println("HIIIIIIIIIIIIIIIIIIIIIII");
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
	 for(Text value:values)
	 {
		 
		 
	 String data[]=	value.toString().split(" ");
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
	 System.out.println(positiveCount+"  -"+negativeCount);
	 if((positiveCount+negativeCount)!=0)
	 {System.out.println("hellloooooo");
		 System.out.println(positiveCount-negativeCount);
		 System.out.println(positiveCount+negativeCount);
	positiveRatio=(double)((positiveCount-negativeCount)/(positiveCount+negativeCount));
	System.out.println("what!!"+(positiveCount-negativeCount));
	 //double negativeRatio=negativeCount/data.length;
	 
	 }
	 
	 context.write(new Text(key),new Text(String.valueOf(positiveRatio)) );
    
    

  
    }
    }
    
    

  public static void main(String[] args) throws Exception {
   Configuration conf = new Configuration();
   Job job = Job.getInstance(conf, "word count");
    job.setJarByClass(Runner.class);
    job.setMapperClass(TokenizerMapper.class);
    //job.setCombinerClass(IntSumReducer.class);
    job.setReducerClass(IntSumReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
    org.apache.hadoop.mapreduce.lib.input.TextInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    job.waitForCompletion(true);
    
    Job job1 = Job.getInstance(conf, "word count");
    job1.setJarByClass(Runner.class);
    job1.setMapperClass(TopMapper.class);
    //job.setCombinerClass(IntSumReducer.class);
    job1.setReducerClass(TopReducer.class);
    job1.setNumReduceTasks(1);
    job1.setOutputKeyClass(NullWritable.class);
    job1.setOutputValueClass(Text.class);
    org.apache.hadoop.mapreduce.lib.input.TextInputFormat.addInputPath(job1, new Path(args[1]));
    FileOutputFormat.setOutputPath(job1, new Path(args[2]));
    job1.waitForCompletion(true);
    
  
    Path pt=new Path("hdfs://localhost:54310"+args[2]+"/part-r-00000");
    FileSystem fs = FileSystem.get(new Configuration());
    BufferedReader br=new BufferedReader(new InputStreamReader(fs.open(pt)));
    String line;
    
  String data="";
    line=br.readLine();
    while(line!=null)
    {
    	
    	
    //	System.out.println(line);
    	String temp[]=line.split("\t");
    	
   data+=temp[0]+",";
    	line=br.readLine();
    }
    //System.out.println(data);
    conf.set("top", data);
    
    Job job2 = Job.getInstance(conf, "word count");
    DistributedCache.addCacheFile(new Path("hdfs://localhost:54310/words/negative-words.txt").toUri(), job2.getConfiguration());
    DistributedCache.addCacheFile(new Path("hdfs://localhost:54310/words/positive-words.txt").toUri(), job2.getConfiguration());
    
    job2.setJarByClass(Runner.class);
    job2.setMapperClass(DataMapper.class);
    //job.setCombinerClass(IntSumReducer.class);
   job2.setReducerClass(DataReducer.class);
    job2.setNumReduceTasks(1);
    job2.setOutputKeyClass(Text.class);
    job2.setOutputValueClass(Text.class);
    org.apache.hadoop.mapreduce.lib.input.TextInputFormat.addInputPath(job2, new Path(args[0]));
    FileOutputFormat.setOutputPath(job2, new Path(args[3]));
    job2.waitForCompletion(true);
  
   
  
  }
  }
