package Cloud.ApacheLog;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;

public class Runner {

  /**
   * @param args
   */
  public static void main(String[] args) throws Exception
  {
	  JobConf conf = new JobConf(Runner.class);
      conf.setJobName("MapReduce1");
      
      conf.setMapperClass(Mapper1.class);
      
      conf.setMapOutputKeyClass(Text.class);
      conf.setMapOutputValueClass(Text.class);
      
      conf.setReducerClass(Reducer1.class);
      
      FileInputFormat.setInputPaths(conf, new Path("input"));
      FileOutputFormat.setOutputPath(conf, new Path("output"));
      
         JobClient.runJob(conf);
         
       JobConf conf2 = new JobConf(Runner.class);
      conf2.setJobName("MapReduce2");
      
      conf2.setMapperClass(Mapper2.class);
      
      conf2.setMapOutputKeyClass(Text.class);
      conf2.setMapOutputValueClass(IntWritable.class);
      
      conf2.setReducerClass(Reducer2.class);
      //conf2.setOutputKeyComparatorClass(SortKeyComparator.class);
     
      // take the input and output from the command line
    
      
              FileInputFormat.setInputPaths(conf2, new Path("output"));
      FileOutputFormat.setOutputPath(conf2, new Path("output2"));

   
         JobClient.runJob(conf2);
         
         JobConf conf3 = new JobConf(Runner.class);
         conf3.setJobName("MapReduce3");
         
         conf3.setMapperClass(Mapper3.class);
         
         conf3.setMapOutputKeyClass(Text.class);
         conf3.setMapOutputValueClass(Text.class);
         
         conf3.setReducerClass(Reducer3.class);
         
         FileInputFormat.setInputPaths(conf3, new Path("output2"));
         FileOutputFormat.setOutputPath(conf3, new Path("output3"));
         
            JobClient.runJob(conf3);
         
         
        
         
         

	}

}
