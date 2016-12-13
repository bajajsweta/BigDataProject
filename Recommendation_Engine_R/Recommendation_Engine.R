library(dplyr)
library(data.table)
library(reshape)

Amazon_Video_2000 <- as.data.table(ratings_Amazon_Instant_Video_2000)
Amazon_Video_2000 <- Amazon_Video_2000[Date %like% "2000"]

##Get Desired Columns
Amazon_Video_2000_matrix <- Amazon_Video_2000[,1:3]
Amazon_Video_2000_matrix
                     
##Pivot the Table
Amazon_Video_2000_matrix_1 <- cast(Amazon_Video_2000_matrix, userId ~ itemId)

#Replace NA's with 0's
Amazon_Video_2000_matrix_1[is.na(Amazon_Video_2000_matrix_1)] <- 0

# Drop any column named "user"
video_matrix <- (Amazon_Video_2000_matrix_1[,!(names(Amazon_Video_2000_matrix_1) %in% c("userId"))])

# Create a helper function to calculate the cosine between two vectors
getCosine <- function(x,y) 
{
  this.cosine <- sum(x*y) / (sqrt(sum(x*x)) * sqrt(sum(y*y)))
  return(this.cosine)
}


# Create a placeholder dataframe listing item vs. item
holder  <- matrix(NA,nrow=ncol(video_matrix),ncol=ncol(video_matrix),dimnames=list(colnames(video_matrix),colnames(video_matrix)))

video_matrix_similarity <- as.data.frame(holder)

# Lets fill in those empty spaces with cosine similarities
for(i in 1:ncol(video_matrix)) {
  for(j in 1:ncol(video_matrix)) {
    video_matrix_similarity[i,j]= getCosine(video_matrix[i],video_matrix[j])
  }
}

head.matrix(video_matrix_similarity)

# Get the top 5 neighbours for each : Place Holder
Video_matrix_neighbours <- matrix(NA,nrow=ncol(video_matrix_similarity),ncol=5,dimnames=list(colnames(video_matrix_similarity)))


head.matrix(Video_matrix_neighbours)

##Lets fill the Neighbours Matrix
for(i in 1:ncol(video_matrix)) 
{
  Video_matrix_neighbours[i,] <- (t(head(n=5,rownames(video_matrix_similarity[order(video_matrix_similarity[,i],decreasing=TRUE),][i]))))
}

head.matrix(Video_matrix_neighbours)

##############################################################################
#####################ITEM RECOMMENDATIONS#####################################
## Item         #[,1]         [,2]         [,3]         [,4]         [,5]        
#B000ID1PUI "B000ID1PUI" "B000JIJMBQ" "B000MN4YR0" "B000MN6XQK" "B000W453KQ"
#B000JIJMBQ "B000JIJMBQ" "B000MN4YR0" "B000MN6XQK" "B000ID1PUI" "B000W453KQ"
#B000MN4YR0 "B000JIJMBQ" "B000MN4YR0" "B000MN6XQK" "B000ID1PUI" "B000W453KQ"
#B000MN6XQK "B000JIJMBQ" "B000MN4YR0" "B000MN6XQK" "B000ID1PUI" "B000W453KQ"
#B000W453KQ "B000W453KQ" "B000ID1PUI" "B000JIJMBQ" "B000MN4YR0" "B000MN6XQK"
#B0032DGB5K "B0032DGB5K" "B000ID1PUI" "B000JIJMBQ" "B000MN4YR0" "B000MN6XQK"

##############################################################################

##USer Score Matrix
# Lets make a helper function to calculate the scores
getScore <- function(history, similarities)
{
  x <- sum(history*similarities)/sum(similarities)
  x
}


holder_1 <- matrix(NA, nrow=nrow(Amazon_Video_2000_matrix_1),ncol=ncol(Amazon_Video_2000_matrix_1)-1,dimnames=list((Amazon_Video_2000_matrix_1$userId),colnames(Amazon_Video_2000_matrix_1[-1])))


# Loop through the users (rows)
for(i in 1:nrow(holder_1)) 
{
  # Loops through the products (columns)
  for(j in 1:ncol(holder_1)) 
  {
    # Get the user's name and th product's name
    # We do this not to conform with vectors sorted differently 
    user <- rownames(holder_1)[i]
    product <- colnames(holder_1)[j]
    
    # We do not want to recommend products you have already consumed
    # If you have already consumed it, we store an empty string
    if(as.integer(Amazon_Video_2000_matrix_1[Amazon_Video_2000_matrix_1$userId==user,product]) == 1)
    { 
      holder_1[i,j]<-""
    } else {
      
      # We first have to get a product's top 10 neighbours sorted by similarity
      topN<-((head(n=5,(video_matrix_similarity[order(video_matrix_similarity[,product],decreasing=TRUE),][product]))))
      topN.names <- as.character(rownames(topN))
      topN.similarities <- as.numeric(topN[,1])
      
      # Drop the first one because it will always be the same song
      topN.similarities<-topN.similarities[-1]
      topN.names<-topN.names[-1]
      
      # We then get the user's purchase history for those 10 items
      topN.purchases<- Amazon_Video_2000_matrix_1[,c("userId",topN.names)]
      topN.userPurchases<-topN.purchases[topN.purchases$userId==user,]
      topN.userPurchases <- as.numeric(topN.userPurchases[!(names(topN.userPurchases) %in% c("userId"))])
      
      # We then calculate the score for that product and that user
      holder_1[i,j]<-getScore(similarities=topN.similarities,history=topN.userPurchases)
      
    } # close else statement
  } # end product for loop   
} # end user for loop


Amazon_Video_2000_matrix_scores <- holder_1

# Lets make our recommendations pretty
Amazon_Video_2000_matrix_scores_holder <- matrix(NA, nrow=nrow(Amazon_Video_2000_matrix_scores),ncol=5,dimnames=list(rownames(Amazon_Video_2000_matrix_scores)))

for(i in 1:nrow(Amazon_Video_2000_matrix_scores)) 
{
  Amazon_Video_2000_matrix_scores_holder[i,] <- names(head(n=5,(Amazon_Video_2000_matrix_scores[,order(Amazon_Video_2000_matrix_scores[i,],decreasing=TRUE)])[i,]))
}

head.matrix(Amazon_Video_2000_matrix_scores_holder)

##################################################################################
#####################USER RECOMMENDATIONS#########################################
###                 [,1]         [,2]         [,3]         [,4]         [,5]        
###A136YD08SCJ2LV "B000JIJMBQ" "B000MN4YR0" "B000MN6XQK" "B000ID1PUI" "B000W453KQ"
###A1GDRNK3Y5IG9Z "B000JIJMBQ" "B000MN4YR0" "B000MN6XQK" "B000ID1PUI" "B000W453KQ"
###A1X4JO8EJ1U5BR "B000JIJMBQ" "B000MN4YR0" "B000MN6XQK" "B000ID1PUI" "B000W453KQ"
###A20EEWWSFMZ1PN "B000JIJMBQ" "B000MN4YR0" "B000MN6XQK" "B000ID1PUI" "B000W453KQ"
###A282IMYVM6GP1L "B000JIJMBQ" "B000MN4YR0" "B000MN6XQK" "B000ID1PUI" "B000W453KQ"
###A2W9J1ZCL5N1ZB "B000JIJMBQ" "B000MN4YR0" "B000MN6XQK" "B000ID1PUI" "B000W453KQ"


#################################################################################
#################################################################################

tryy <- "A136YD08SCJ2LV"
for( tryy in rownames(Amazon_Video_2000_matrix_scores_holder) )
{
  
    try <- Amazon_Video_2000_matrix_scores_holder[i,]
  
}

as.data.frame(try)
