bin/hadoop fs -rmr temp/* /formatedMahout /mahoutDatarec1 /userIDdata1 /productIDData1 /productRecm /interResult /FinalResult
bin/hadoop jar mahout1.jar Runner /review /mahoutDatarec1 /userIDdata1 /productIDData1
bin/hadoop jar mahout-core-0.7-job.jar org.apache.mahout.cf.taste.hadoop.item.RecommenderJob -s SIMILARITY_COOCCURRENCE --input /mahoutDatarec1 --output /productRecm
bin/hadoop jar mahout2.jar Runner /productRecm /formatedMahout /userIDdata1 /productIDData1 /interResult /FinalResult

