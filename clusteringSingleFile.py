import orange, orngEnsemble, orngClustering
import orngTest, orngStat
import copy
import pylab
import os, pickle
from time import clock,time

from DirtyKNNClassifier import *

# http://www.ailab.si/orange/doc/modules/orngClustering.htm

colors = ["white", "red", "blue", "yellow", "green", "orange", "purple", "pink", "firebrick", "gold", "greenyellow", "gray", "indigo", "khaki"]

def plot_scatter(data, cls, attx, atty, filepath, title=None):
	"""plot a data scatter plot with the position of centeroids"""
	pylab.rcParams.update({'font.size': 8, 'figure.figsize': [4,3]})
	
	x = [float(d[attx]) for d in data]
	y = [float(d[atty]) for d in data]
	#cs = "".join([colors[c] for c in cls.clusters])
	#cs = "".join([cnames[colors[c]] for c in cls.clusters])
	cs = list()
	for c in cls.clusters:
		cs.append(colors[c])
	#print cs
	pylab.scatter(x, y, c=cs, s=10)
	
	xc = [float(d[attx]) for d in cls.centroids]
	yc = [float(d[atty]) for d in cls.centroids]
	pylab.scatter(xc, yc, marker="x", c="k", s=200) 
	
	pylab.xlabel(attx)
	pylab.ylabel(atty)
	if title:
		pylab.title(title)
	pylab.savefig("%s" % filepath)
	pylab.close()
	
PREFLOP = "PREFLOP"
FLOP = "FLOP"
TURN = "TURN"
RIVER = "RIVER"
LOW = "LOW"
MED = "MEDIUM"
HIGH = "HIGH"

# Change back when done testing
#phases = [PREFLOP]
#stackLevels = [LOW]
#potLevels = [LOW]
phases = [PREFLOP, FLOP, TURN, RIVER]
stackLevels = [LOW, MED, HIGH]
potLevels = [LOW, MED, HIGH]

kmin = 2
kmax = min(5, len(colors))

graphAttribute1 = "Check%"
graphAttribute2 = "Bet%"

filepath = "C:/Users/hartsoka/Documents/Classes/CS 7641/project/trunk/simulator2/data/agg.tab"
pathTokens = filepath.rsplit("/", 1)
directory = pathTokens[0] + "/"
fileTokens = pathTokens[1].rsplit(".", 1)
name = fileTokens[0]
ext = "." + fileTokens[1]

resultingClusterers = dict()
resultingClassifiers = dict()

totalData = orange.ExampleTable(filepath)

def makeKey(phase, potLevel, stackLevel):
	return phase.lower()[0] + potLevel.lower()[0] + stackLevel.lower()[0]

for phase in phases:
	for potLevel in potLevels:
		for stackLevel in stackLevels:
			key = makeKey(phase, potLevel, stackLevel)

			print("------------------------------------------------------------")
			print("Clustering: " + key)
			
			try:
				data = totalData.filter({"Phase" : phase, "Pot%" : potLevel, "Stack%" : stackLevel})
			except:
				print ("Enum Value not included for this cross-section")
				continue
			
			print "Number of player samples for cross section:", len(data)
			if (len(data) <= kmax):
				print ("Not enough data for this cross-section")
				continue
				
			attribs = data.domain[3:]
			reducedDomain = orange.Domain(attribs)
			data = orange.ExampleTable(reducedDomain, data)

			bestClusterer = None
			bestScore = -2 # worst possible silhouette score is -1, best is 1
			bestK = -1
			for curK in range(kmin, kmax+1):
				for trial in range(0, 1):
					print "k=", curK, " : sil=",
					clusterer = orngClustering.KMeans(data, curK)
					#clusterer = orngClustering.KMeans(data, curK, initialization=orngClustering.kmeans_init_diversity)
					#score = orngClustering.score_fastsilhouette(clusterer)
					silhouetteScore = orngClustering.score_silhouette(clusterer) # takes FOREVER and a day
					cohesiveScore = orngClustering.score_distance_to_centroids(clusterer)
					score = silhouetteScore
					print score
					if (score > bestScore):
						bestK = curK
						bestClusterer = clusterer
						bestScore = score
					
			print("Best K = ", bestK)
			print("Best Score = ", bestScore)
			
			# Test that each point is closest to its centroid
			print "Testing classifier built from clusterer...", 
			distFn = orange.ExamplesDistanceConstructor_Euclidean(data)  # Try dir(x) on an Example to see if a component of it should be passed to this
			knn = DirtyKNNClassifier(bestClusterer.centroids, distFn)
			correct = 0;
			pos = 0
			for example in data:
				ix = knn.label(example)
				if ix == bestClusterer.clusters[pos]:
					correct += 1
				pos += 1
			print "CA =", float(correct) / len(data)

			# Labels must be strings, so convert cluster integers to strings
			intValues = range(1, bestK+1)
			stringValues = list()
			for intValue in intValues:
				stringValues.append(str(intValue))
				
			# Create a new domain, same as our original data plus a cluster number
			cluster = orange.EnumVariable("cluster", values = stringValues)
			labelledDomain = orange.Domain(data.domain.attributes+[cluster])

			#print data.domain
			#print labelledDomain

			"""
			# Copy all of our examples and append the cluster number
			labelledExamples = list()
			i = 0
			for example in data:
				attribs = list()
				for attrib in example:
					attribs.append(attrib)
				attribs.append(bestClusterer.clusters[i]) # use this line if the original data does not have labels
				#attribs[len(attribs)-1] = bestClusterer.clusters[i] # use this line if the original data already has labels which you are ignoring
				labelledExamples.append(orange.Example(labelledDomain, attribs))
				i += 1
				
			# Copy centroids and append the cluster number
			labelledCentroids = list()
			i = 0
			for centroid in bestClusterer.centroids:
				attribs = list()
				for attrib in centroid:
					attribs.append(attrib)
				attribs.append(i) # use this line if the original data does not have labels
				#attribs[len(attribs)-1] = i # use this line if the original data already has labels which you are ignoring
				labelledCentroids.append(orange.Example(labelledDomain, attribs))
				i += 1
			"""
			
			# Save image of clustering
			plot_scatter(data, bestClusterer, graphAttribute1, graphAttribute2, directory + key + "Image.png") # save clusters over dimension
			orngClustering.plot_silhouette(bestClusterer, filename=(directory + key + "Silhouette.png"), fast=True) # save silhouette calcs
			
			"""
			# Save examples with their cluster numbers
			labelledData = orange.ExampleTable(labelledDomain, labelledExamples)
			labelledData.save(directory + key + "Labelled" + ".tab")

			# Save centroids with assigned cluster numbers
			labelledCentroidData = orange.ExampleTable(labelledDomain, labelledCentroids)
			labelledCentroidData.save(directory + key + "Centroids" + ".tab")
			"""
			
			resultingClusterers[key] = bestClusterer
			resultingClassifiers[key] = knn

dumpfile = open(directory + "clusterers.pik", 'wc')
pickler = pickle.Pickler(dumpfile)
pickler.dump(resultingClusterers)
dumpfile.close()

dumpfile = open(directory + "classifiers.pik", 'wc')
pickler = pickle.Pickler(dumpfile)
pickler.dump(resultingClassifiers)
dumpfile.close()