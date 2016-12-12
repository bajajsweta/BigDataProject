import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class SimplePartitioner extends Partitioner<Text, Text>{

    @Override
    public int getPartition(Text key, Text value, int numReducerTask) {
        
int s= value.toString().length();
        
        if(s<50)
        {
        return (1 % numReducerTask);
        }
        else if(s>50 && s<100 )
        {
        return (2 % numReducerTask);
        }
        else if(s>100 && s<150 )
        {
        return (3 % numReducerTask);
        }
        else
        	 return (4 % numReducerTask);
        	
    }
    
    
}