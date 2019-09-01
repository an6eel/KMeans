data1 <- read.table("scala_functional",skip=1,sep=" ")[,1:2]
data2 <- read.table("scala_parallel",skip=1,sep=" ")[,1:2]
data3 <- read.table("java",skip=1,sep=" ")[,1:2]
data4 <- read.table("java_functional", skip=1,sep=" ")[,1:2]

print(data4)
plot(data1[,1], data1[,2], type = "b", frame = FALSE, pch = 19,
     col = "red", xlab = "Iteraciones", ylab = "ms")

# Add a second line
lines(data1[,1], data2[,2], pch = 18, col = "blue", type = "b", lty = 2)
lines(data1[,1], data3[,2], pch = 18, col = "orange", type = "b", lty = 2)
lines(data1[,1], data4[,2], pch = 18, col = "green", type = "b", lty = 2)
# Add a legend to the plot
legend("topleft", legend=c("Scala", "Parallel","Java","Java Funcional"),
       col=c("green", "blue","orange","red"), lty = 1:2, cex=0.8)
write.csv(data, file = "table.csv")