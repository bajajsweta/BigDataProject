#
# This is a Shiny web application. You can run the application by clicking
# the 'Run App' button above.
#
# Find out more about building applications with Shiny here:
#
#    http://shiny.rstudio.com/
#

library(shiny)

# Define UI for application that draws a histogram
ui <- fluidPage( 
   
   # Application title
   titlePanel("User Based Recommendation Engine"),
   
   # Sidebar with a slider input for number of bins 
   sidebarLayout(
      sidebarPanel(
        selectInput(inputId = "var1", 
                    label = "Choose a User to display Recommendations",
                    choices = row.names(Amazon_Video_2000_matrix_scores_holder),
                    selected = "A136YD08SCJ2LV")
      ),
      
      # Show a plot of the generated distribution
      mainPanel(
        tableOutput("table1")
      )
   )
 )

# Define server logic required to draw a table
server <- function(input, output){
      output$table1 <- renderTable({ 
       ##for(var1 in rownames(Amazon_Video_2000_matrix_scores_holder))
         ##{
         Amazon_Video_2000_matrix_scores_holder[i,]
        ## }
     })
 }

# Run the application 
shinyApp(ui = ui, server = server)

