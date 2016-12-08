import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;
import org.apache.hadoop.mapred.lib.MultipleInputs;
import org.apache.hadoop.mapred.lib.MultipleOutputs;



public class Runner {

	public static class MahoutData extends MapReduceBase implements org.apache.hadoop.mapred.Mapper<LongWritable, Text, Text, NullWritable> {

	  public Map<String, Integer> userID=new HashMap<String, Integer>(); 
	  public Map<String, Integer> productID =new HashMap<String, Integer>();
	  
	  private int userCount=0;
	  private int productCount=0;
		@Override
		public void map(LongWritable key, Text value, OutputCollector<Text, NullWritable> output, Reporter arg3)
				throws IOException {
    	String data[]=value.toString().split("\t");
    	String reviwer=data[1];
    	String product=data[7];
    	String rating =data[4];
    	int newUser;
    	int newProduct;
    	if(rating.length()==1)
    	{
    	if(userID.containsKey(reviwer))
    	{
    		newUser=userID.get(reviwer);
    	}
    	else
    	{
    		userID.put(reviwer,userCount );
    		newUser=userCount;
    		
    		userCount++;
    		
    	}
    	
    	if(productID.containsKey(product))
    	{
    		newProduct=productID.get(product);
    	}
    	else
    	{
    		productID.put(product,productCount );
    		newProduct=productCount;
    		productCount++;
    		
    	}
    	
    	String output1=newUser+"\t"+newProduct+"\t"+rating+"\t"+reviwer+"\t"+product;
    	
        output.collect(new Text(output1),NullWritable.get());
    	}
      
    }
		

  }
	
	public static class UserData extends MapReduceBase implements org.apache.hadoop.mapred.Mapper<LongWritable, Text, Text, NullWritable> {

		  public Map<String, Integer> userID=new HashMap<String, Integer>(); 
		  //public Map<String, Integer> productID =new HashMap<String, Integer>();
		  
		  private  int userCount=0;
		  private  int productCount=0;
			@Override
			public void map(LongWritable key, Text value, OutputCollector<Text, NullWritable> output, Reporter arg3)
					throws IOException {
	    	String data[]=value.toString().split("\t");
	    	String reviwer=data[1];
	    	//String product=data[7];
	    	String rating =data[4];
	    	int newUser;
	    	//int newProduct;
	    	if(rating.length()==1)
	    	{
	    	if(userID.containsKey(reviwer))
	    	{
	    		System.out.println("already");
	    	}
	    	
	    	else
	    	{
	    		
	    		userID.put(reviwer,userCount );
	    	
	    		String text=userCount+"\t"+reviwer;
	    		output.collect(new Text(text),NullWritable.get());
	    		userCount=userCount+1;
	    	}
	    		    	}
	      
	    }
			

	  }
	
	public static class ProductData extends MapReduceBase implements org.apache.hadoop.mapred.Mapper<LongWritable, Text, Text, NullWritable> {

		 // public Map<String, Integer> userID=new HashMap<String, Integer>(); 
		  public Map<String, Integer> productID =new HashMap<String, Integer>();
		  
		  private  int userCount=0;
		  private  int productCount=0;
			@Override
			public void map(LongWritable key, Text value, OutputCollector<Text, NullWritable> output, Reporter arg3)
					throws IOException {
				output.collect(new Text(value),NullWritable.get());
	    		
				
	    }
			

	  }

	
	public  static class UserReducer extends MapReduceBase implements Reducer<Text, NullWritable, Text, NullWritable> 
	 {
  public Map<String, Integer> productID =new HashMap<String, Integer>();
		  
		  private  int userCount=0;
		  private  int productCount=0;
		@Override
		public void reduce(Text value, Iterator<NullWritable> arg1, OutputCollector<Text, NullWritable> output, Reporter arg3)
				throws IOException {
			String data[]=value.toString().split("\t");
	    	String reviwer=data[1];
	    	//String product=data[7];
	    	String rating =data[4];
	    	int newUser;
	    	//int newProduct;
	    	if(rating.length()==1)
	    	{
	    	if(productID.containsKey(reviwer))
	    	{}
	    	else
	    	{

	    		productID.put(reviwer,userCount );
	    		newUser=userCount;
	    		String text=reviwer+"\t"+newUser;
	    		output.collect(new Text(text),NullWritable.get());
	    		userCount++;
	    	
	    	}
	    		    	}
	      
	    }
			
		}
	
	
	
	public  static class MahoutReducer extends MapReduceBase implements Reducer<Text, NullWritable, Text, NullWritable> 
	 {
 public Map<String, Integer> productID =new HashMap<String, Integer>();
 public Map<String, Integer> userID =new HashMap<String, Integer>();
		  
		  private  int userCount=0;
		  private  int productCount=0;
		@Override
		public void reduce(Text value, Iterator<NullWritable> arg1, OutputCollector<Text, NullWritable> output, Reporter arg3)
				throws IOException {
			String data[]=value.toString().split("\t");
	    	String reviwer=data[1];
	    	String product=data[7];
	    	String rating =data[4];
	    	int newUser;
	    	int newProduct;
	    	if(rating.length()==1)
	    	{
	    	if(userID.containsKey(reviwer))
	    	{
	    		newUser=userID.get(reviwer);
	    	}
	    	else
	    	{
	    		userID.put(reviwer,userCount );
	    		newUser=userCount;
	    		
	    		userCount++;
	    		
	    	}
	    	
	    	if(productID.containsKey(product))
	    	{
	    		newProduct=productID.get(product);
	    	}
	    	else
	    	{
	    		productID.put(product,productCount );
	    		newProduct=productCount;
	    		productCount++;
	    		
	    	}
	    	
	    	String output1=newUser+"\t"+newProduct+"\t"+rating+"\t"+reviwer+"\t"+product;
	    	
	        output.collect(new Text(output1),NullWritable.get());
	    	}
	      
	    }
		}
		
	 
	 
	public  static class ProductReducer extends MapReduceBase implements Reducer<Text, NullWritable, Text, NullWritable> 
	 {
 public Map<String, Integer> productID =new HashMap<String, Integer>();
		  
		  private  int userCount=0;
		  private  int productCount=0;
		@Override
		public void reduce(Text value, Iterator<NullWritable> arg1, OutputCollector<Text, NullWritable> output, Reporter arg3)
				throws IOException {
			String data[]=value.toString().split("\t");
	    	//String reviwer=data[1];
	    	String product=data[7];
	    	String rating =data[4];
	    	int newUser;
	    	//int newProduct;
	    	if(rating.length()==1)
	    	{
	    	if(productID.containsKey(product))
	    	{}
	    	else
	    	{

	    		productID.put(product,userCount );
	    		newUser=userCount;
	    		String text=product+"\t"+newUser;
	    		output.collect(new Text(text),NullWritable.get());
	    		userCount++;
	    	
	    	}
	    		    	}
	      
	    }
			
		}
		
	 
	public static class UserJoinMapper extends MapReduceBase implements org.apache.hadoop.mapred.Mapper<LongWritable, Text, Text, Text> {

		private Text outkey = new Text();
		private Text outvalue = new Text();
		
			@Override
			public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, Reporter arg3)
					throws IOException {
				
				
				String[] separatedInput = value.toString().split("\\t");
				// String[] tagTokens = separatedInput[5].split(",");
				String userId = separatedInput[1];
				if (userId == null) {
					return;
				}
				// The foreign join key is the user ID
				outkey.set(userId);
				// Flag this record for the reducer and then output
				outvalue.set("A" + value.toString());
				output.collect(outkey, outvalue);
	    		
				
	    }
			

	  }	 

	
	public static class CommentJoinMapper extends MapReduceBase implements org.apache.hadoop.mapred.Mapper<LongWritable, Text, Text, Text> {

		private Text outkey = new Text();
		private Text outvalue = new Text();
		
			@Override
			public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, Reporter arg3)
					throws IOException {
				
				
				String[] separatedInput = value.toString().split("\\t");
				// String[] tagTokens = separatedInput[5].split(",");
				String userId = separatedInput[0];
				if (userId == null) {
					return;
				}
				// The foreign join key is the user ID
				outkey.set(userId);
				// Flag this record for the reducer and then output
				outvalue.set("B" + value.toString());
				output.collect(outkey, outvalue);
	    		
				
	    }
			

	  }	 
  
	
	public  static class UserJoinReducer extends MapReduceBase implements Reducer<Text, Text, Text, Text> 
	 {
		private static final Text EMPTY_TEXT = new Text("");
		private Text tmp = new Text();
		private ArrayList<Text> listA = new ArrayList<Text>();
		private ArrayList<Text> listB = new ArrayList<Text>();
		private String joinType = null;
		@Override
		public void reduce(Text key, Iterator<Text> values, OutputCollector<Text, Text> output, Reporter arg3)
				throws IOException {
			listA.clear();
			listB.clear();
			// iterate through all our values, binning each record based on what
			// it was tagged with. Make sure to remove the tag!
			while (values.hasNext()) {
				tmp = values.next();
				System.out.println(Character.toString((char) tmp.charAt(0)));
				if (Character.toString((char) tmp.charAt(0)).equals("A")) {

					System.out.println("here4");
					listA.add(new Text(tmp.toString().substring(1)));
				}
				if (Character.toString((char) tmp.charAt(0)).equals("B")) {
					System.out.println("here5");
					listB.add(new Text(tmp.toString().substring(1)));
				}
				System.out.println(tmp);
			}
			// Execute our join logic now that the lists are filled

			System.out.println("Size is "+listB.size());
			try {
				executeJoinLogic(output);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}	
		private void executeJoinLogic(OutputCollector<Text, Text> output) throws IOException, InterruptedException {

			
				// If both lists are not empty, join A with B
				System.out.println("here3");
                                System.out.println(listA.size()+" and "+listB.size());
				if (!listA.isEmpty() && !listB.isEmpty()) {
					System.out.println("avee");
					for (Text A : listA) {
						//System.out.println("here1");
						for (Text B : listB) {
							//System.out.println("here2");
							String temp[]=A.toString().split("\t");
							output.collect(new Text(temp[0]), B);
						}
					}
				}
		
		
	 }
  
	 }
	
	
	
		 

	
	public static class CommentJoinMapper2 extends MapReduceBase implements org.apache.hadoop.mapred.Mapper<LongWritable, Text, Text, Text> {

		private Text outkey = new Text();
		private Text outvalue = new Text();
		
			@Override
			public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, Reporter arg3)
					throws IOException {
				
				
				String[] separatedInput = value.toString().split("\\t");
				// String[] tagTokens = separatedInput[5].split(",");
				String userId = separatedInput[2];
				if (userId == null) {
					return;
				}
				// The foreign join key is the user ID
				outkey.set(userId);
				// Flag this record for the reducer and then output
				outvalue.set("B" + value.toString());
				output.collect(outkey, outvalue);
	    		
				
	    }
			

	  }	 
  
	
	public  static class UserJoinReducer2 extends MapReduceBase implements Reducer<Text, Text, Text, Text> 
	 {
		private static final Text EMPTY_TEXT = new Text("");
		private Text tmp = new Text();
		private ArrayList<Text> listA = new ArrayList<Text>();
		private ArrayList<Text> listB = new ArrayList<Text>();
		private String joinType = null;
		@Override
		public void reduce(Text key, Iterator<Text> values, OutputCollector<Text, Text> output, Reporter arg3)
				throws IOException {
			listA.clear();
			listB.clear();
			// iterate through all our values, binning each record based on what
			// it was tagged with. Make sure to remove the tag!
			while (values.hasNext()) {
				tmp = values.next();
				System.out.println(Character.toString((char) tmp.charAt(0)));
				if (Character.toString((char) tmp.charAt(0)).equals("A")) {

					System.out.println("here4");
					listA.add(new Text(tmp.toString().substring(1)));
				}
				if (Character.toString((char) tmp.charAt(0)).equals("B")) {
					System.out.println("here5");
					listB.add(new Text(tmp.toString().substring(1)));
				}
				System.out.println(tmp);
			}
			// Execute our join logic now that the lists are filled

			System.out.println("Size is "+listB.size());
			try {
				executeJoinLogic(output);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}	
		private void executeJoinLogic(OutputCollector<Text, Text> output) throws IOException, InterruptedException {

			
				// If both lists are not empty, join A with B
				System.out.println("here3");
                                System.out.println(listA.size()+" and "+listB.size());
				if (!listA.isEmpty() && !listB.isEmpty()) {
					System.out.println("avee");
					for (Text A : listA) {
						//System.out.println("here1");
						for (Text B : listB) {
							//System.out.println("here2");
							String temp[]=A.toString().split("\t");
							String b[]=B.toString().split("\t");
							String outputdata=b[0]+"\t"+b[3];
							output.collect(new Text(outputdata), new Text(temp[0]));
						}
					}
				}
		
		
	 }
  
	 }

  
	public static class MahutFormaterMapper extends MapReduceBase implements org.apache.hadoop.mapred.Mapper<LongWritable, Text, Text, NullWritable> {

	
		
			@Override
			public void map(LongWritable key, Text value, OutputCollector<Text, NullWritable> output, Reporter arg3)
					throws IOException {
				
			String tsv[]=value.toString().split("\t");
			String keyData=tsv[0];
			String bracket[]=tsv[1].split("\\[");
			String data1=bracket[1];
			String data2[]=data1.split("\\]");
			String data3[]=data2[0].split(",");
			for(int i=0;i<data3.length;i++)
			{
				String data4[]=data3[i].split(":");
				String finalData=keyData+"\t"+data4[0]+"\t"+data4[1];
				//System.out.println(finalData);
				output.collect(new Text(finalData), NullWritable.get());
				
			}
			
			
				
	    		
				
	    }
			

	  }	 
	

  public static void main(String[] args) throws Exception {
	/*
	  JobConf job = new JobConf(Runner.class);
      job.setJobName("ip-count");
 
      job.setJarByClass(Runner.class);
      job.setMapperClass(ProductData.class);
      job.setNumReduceTasks(1);
      job.setReducerClass(MahoutReducer.class);
      job.setNumMapTasks(1);
   
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(NullWritable.class);
    
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1])); 
    JobClient.runJob(job);
    
    JobConf job1 = new JobConf(Runner.class);
    job1.setJobName("ip-count");

  job1.setJarByClass(Runner.class);
  job1.setMapperClass(ProductData.class);
  job1.setNumReduceTasks(1);
  job1.setReducerClass(UserReducer.class);
  job1.setNumMapTasks(1);
  
  
  job1.setOutputKeyClass(Text.class);
  job1.setOutputValueClass(NullWritable.class);
  
  FileInputFormat.addInputPath(job1, new Path(args[0]));
  FileOutputFormat.setOutputPath(job1, new Path(args[2])); 
  JobClient.runJob(job1);
  
  JobConf job2 = new JobConf(Runner.class);
  job2.setJobName("ip-count");

  job2.setJarByClass(Runner.class);
  job2.setMapperClass(ProductData.class);
  job2.setNumReduceTasks(1);
  job2.setReducerClass(ProductReducer.class);
  job2.setNumMapTasks(1);

job2.setOutputKeyClass(Text.class);
job2.setOutputValueClass(NullWritable.class);

FileInputFormat.addInputPath(job2, new Path(args[0]));
FileOutputFormat.setOutputPath(job2, new Path(args[3])); 
JobClient.runJob(job2);
*/
JobConf job5 = new JobConf(Runner.class);
job5.setJobName("Mahout Trasnform");

job5.setJarByClass(Runner.class);
job5.setMapperClass(MahutFormaterMapper.class);
job5.setNumReduceTasks(0);
//job2.setReducerClass(ProductReducer.class);
job5.setNumMapTasks(1);

job5.setOutputKeyClass(Text.class);
job5.setOutputValueClass(NullWritable.class);

FileInputFormat.addInputPath(job5, new Path(args[0]));
FileOutputFormat.setOutputPath(job5, new Path(args[1])); 
//JobClient.runJob(job5);


JobConf job3 = new JobConf(Runner.class);
job3.setJobName("ip-count");
job3.setJarByClass(Runner.class);
MultipleInputs.addInputPath(job3, new Path(args[2]), TextInputFormat.class, UserJoinMapper.class);
MultipleInputs.addInputPath(job3, new Path(args[1]), TextInputFormat.class, CommentJoinMapper.class);
job3.setReducerClass(UserJoinReducer.class);

TextOutputFormat.setOutputPath(job3, new Path(args[4]));

job3.setOutputKeyClass(Text.class);
job3.setOutputValueClass(Text.class);
JobClient.runJob(job3);

JobConf job4 = new JobConf(Runner.class);
job4.setJobName("ip-count");
job4.setJarByClass(Runner.class);
MultipleInputs.addInputPath(job4, new Path(args[2]), TextInputFormat.class, UserJoinMapper.class);
MultipleInputs.addInputPath(job4, new Path(args[4]), TextInputFormat.class, CommentJoinMapper2.class);
job4.setReducerClass(UserJoinReducer2.class);

TextOutputFormat.setOutputPath(job4, new Path(args[5]));

job4.setOutputKeyClass(Text.class);
job4.setOutputValueClass(Text.class);
JobClient.runJob(job4);
  }
}