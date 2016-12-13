library(dplyr)

colnames(ratings_Cell_Phones_and_Accessories) = c("userId","itemid","rating","unix")

ratings_Cell_Phones_and_Accessories$unix <- as.POSIXct(as.numeric(ratings_Cell_Phones_and_Accessories$unix), origin = '1970-01-01', tz = 'EST')
class()
ratings_Cell_Phones_and_Accessories$year <- as.numeric(format(ratings_Cell_Phones_and_Accessories$Date,"%Y")) 
## Product popularity by user
set1 <- ratings_Cell_Phones_and_Accessories %>%
        select(Date,itemId) %>%
        group_by(Date) %>%
        summarise(cnt = n())

str(ratings_Cell_Phones_and_Accessories)

a <- aggregate(as.numeric(as.factor(ratings_Cell_Phones_and_Accessories$itemid)), list(ratings_Cell_Phones_and_Accessories$unix), count)

table(ratings_Cell_Phones_and_Accessories)


foo <- function(data,column, x1){
    data %>%
    group_by_(.dots = column) %>%
    summarise_each_(funs(count), c(x1))
}

foo(ratings_Cell_Phones_and_Accessories,"unix","itemid")
