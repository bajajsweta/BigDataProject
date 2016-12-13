library(ggplot2)

colnames(cellPhone_sentiments) <- c("itemid","sentiment","reviewNumber","AverageRating")

sentiment_set1 <- cellPhone_sentiments %>%
                   group_by(sentiment) %>%
                   select(sentiment) %>%
                   summarise(count = n())

sentiment_set1 <- as.matrix(sentiment_set1)
colours <- c("red", "yellow", "green")
barplot(sentiment_set1$count,main="Sentiment Analysis", xlab="Sentiments", ylab="Count", names.arg=c("Negative","Neutral","Positive"))

sentiment_set2 <- cellPhone_sentiments %>%
                  group_by(reviewNumber,sentiment) %>%+
                  summarise(count = n())

hist(cellPhone_sentiments$AverageRating, col = "green")


sentiment_set2 <- cellPhone_sentiments %>%
  group_by(reviewNumber,sentiment) %>%
  summarise(count = n())

Sentiment_set3 <- semtimental_byYear_ %>%
  select(X1,X2)

Sentiment_set4 <- Sentiment_set3 %>%
                  filter(X1 > "2005") %>%
                  group_by(X1,X2) %>%
                  dplyr::summarise(count = n()) %>%
                  arrange(desc(X1),desc(count))


#Unstacked Bars
p1 <- Sentiment_set4 %>%
  ggplot() + 
  aes(x = as.factor(X1),y = count, fill = as.factor(X2)) +
  labs(title ="Sentiments over the years", x = "Year", y = "Sentiment Count") +
  geom_bar(stat="identity",position = 'dodge') +
  scale_fill_discrete(name="Sentiments")

install.packages("gridExtra")
require("gridExtra")
grid.arrange(arrangeGrob(p1))


install.packages("corrplot")
library(corrplot)
set1 <- as.matrix(semtimental_byYear_)
corrplot(set1,adis.corr = FALSE, method = "square")

M <- matrix(rnorm(64), nrow=8, ncol=8) 
M
