bin/hadoop fs -rmr temp/* /mahoutDatarec1 /userIDdata1 /productIDData1 /productRecm /interResult /FinalResult
bin/hadoop jar mahout1.jar Runner /review /mahoutDatarec1 /userIDdata1 /productIDData1
bin/hadoop jar mahout-core-0.7-job.jar org.apache.mahout.cf.taste.hadoop.item.RecommenderJob -Dmapred.input.dir=/mahoutDatarec1 -Dmapred.output.dir=/productRecm --usersFile /input/user.txt --booleanData true --similarityClassname SIMILARITY_COOCCURRENCE
bin/hadoop jar mahout2.jar Runner /productRecm /formatedMahout /userIDdata1 /productIDData1 /interResult /FinalResult
