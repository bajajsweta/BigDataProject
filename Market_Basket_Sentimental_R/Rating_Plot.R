# if you haven't installed plyr, delete the # and run this line:
# install.packages("plyr")

library(plyr)

# first we use plyr to calculate the mean rating and SE for each story
ratingdat <- ddply(db, c("story"), summarise,
                   M = mean(rating, na.rm=T),
                   SE = sd(rating, na.rm=T)/sqrt(length(na.omit(rating))),
                   N = length(na.omit(rating)))

# make story into an ordered factor, ordering by mean rating:
ratingdat$story <- factor(ratingdat$story)
ratingdat$story <- reorder(ratingdat$story, ratingdat$M)

# take a look at our handiwork:

# Now open a connection to a pdf file
pdf(file="plots/dotplot-story-rating.pdf", height=14, width=8.5)

ggplot(ratingdat, aes(x = M, xmin = M-SE, xmax = M+SE, y = story )) +
  geom_point() + geom_segment( aes(x = M-SE, xend = M+SE,
                                   y = story, yend=story)) +
  theme_bw() + opts(axis.title.x = theme_text(size = 12, vjust = .25))+
  xlab("Mean rating") + ylab("Story") +
  opts(title = expression("Rating article by Story, with SE"))
dev.off()