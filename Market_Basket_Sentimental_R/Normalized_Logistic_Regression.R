###########Normalize#################################################

normalize <- function(x) {
  return ((x - min(x)) / (max(x) - min(x)))
}

dfNorm <- as.data.frame(lapply(X6_categories[2:5], normalize))

getwd()
write.csv(dfNorm,file ="normalized_logistic.csv")


######################################################################

