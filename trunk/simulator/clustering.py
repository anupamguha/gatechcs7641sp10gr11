import orange, orngEnsemble, orngClustering
import orngTest, orngStat
import copy
from time import clock,time

# http://www.ailab.si/orange/doc/modules/orngClustering.htm

k = 3

data = orange.ExampleTable("statistics1.csv")

km = orngClustering.KMeans(data, k)

# Labels must be strings, so convert cluster integers to strings
intValues = range(1, k+1)
stringValues = list()
for intValue in intValues:
	stringValues.append(str(intValue))
	
# Create a new domain, same as our original data plus a cluster number
cluster = orange.EnumVariable("cluster", values = stringValues)
labelledDomain = orange.Domain(data.domain.attributes+[cluster])

#print data.domain
#print labelledDomain

# Copy all of our examples and append the cluster number
labelledExamples = list()
i = 0
for example in data:
	attribs = list()
	for attrib in example:
		attribs.append(attrib)
	attribs.append(km.clusters[i]) # use this line if the original data does not have labels
	#attribs[len(attribs)-1] = km.clusters[i] # use this line if the original data already has labels which you are ignoring
	labelledExamples.append(orange.Example(labelledDomain, attribs))
	i += 1
	
# Save examples with their cluster numbers
labelledData = orange.ExampleTable(labelledDomain, labelledExamples)
labelledData.save("labelledExamples.tab")

# Save centroids... to use these as training data, manually append a unique label to the end of each row
centroidData = orange.ExampleTable(km.centroids)
centroidData.save("centroids.tab")
