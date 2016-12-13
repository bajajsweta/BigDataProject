# if you haven't installed plyr, delete the # and run this line:
# install.packages("plyr")

library(plyr)

# first we use plyr to calculate the mean rating and SE for each product
ratingdat <- ddply(cellPhone_sentiments, c("itemid"), summarise,
                   M = mean(AverageRating, na.rm=T),
                   SE = sd(AverageRating, na.rm=T)/sqrt(length(na.omit(AverageRating))),
                   N = length(na.omit(AverageRating)))

# make story into an ordered factor, ordering by mean rating:
ratingdat$itemid <- factor(ratingdat$itemid)
ratingdat$itemid <- reorder(ratingdat$itemid, ratingdat$M)

# take a look at our handiwork:

# Now open a connection to a pdf file
pdf(file="dotplot-story-rating.pdf", height=14, width=8.5)

ggplot(ratingdat, aes(x = M, xmin = M-SE, xmax = M+SE, y = itemid )) +
  geom_point() + geom_segment( aes(x = M-SE, xend = M+SE,
                                   y = itemid, yend=itemid)) 
dev.off()
