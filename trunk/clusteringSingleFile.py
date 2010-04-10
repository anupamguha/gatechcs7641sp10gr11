import orange, orngEnsemble, orngClustering
import orngTest, orngStat
import copy
import pylab
import os, pickle
from time import clock,time

from DirtyKNNClassifier import *
from playerClassifier import *

# ---------------------------------------------------------------------------
#                              SET THESE

colors = ["white", "red", "blue", "yellow", "green", "orange", "purple", "pink", "firebrick", "gold", "greenyellow", "gray", "indigo", "khaki"]

kmin = 2
kmax = min(5, len(colors))

graphAttribute1 = "Check%"
graphAttribute2 = "Bet%"

filepath = "C:/Users/hartsoka/Documents/Classes/CS 7641/project/trunk/simulator2/data/aggregatedPlayerHistories.tab"

# ---------------------------------------------------------------------------

# http://www.ailab.si/orange/doc/modules/orngClustering.htm

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
phases = [PREFLOP, FLOP, TURN, RIVER]
stackLevels = [LOW, MED, HIGH]
potLevels = [LOW, MED, HIGH]

pathTokens = filepath.rsplit("/", 1)
directory = pathTokens[0] + "/"
fileTokens = pathTokens[1].rsplit(".", 1)
name = fileTokens[0]
ext = "." + fileTokens[1]

resultingClusterers = dict()
resultingClassifiers = dict()

# get all data
totalDataPlusNames = orange.ExampleTable(filepath)

# remove player names from the data
attribsWithoutNames = totalDataPlusNames.domain[1:]
domainWithoutNames = orange.Domain(attribsWithoutNames)
totalData = orange.ExampleTable(domainWithoutNames, totalDataPlusNames)

# and set up a domain which doesn't include Phase, Pot%, Stack%, since
#	we will be filtering on these and they'll all be the same
attribs = totalData.domain[3:]
reducedDomain = orange.Domain(attribs)

def makeKey(phase, potLevel, stackLevel):
	return phase.lower()[0] + potLevel.lower()[0] + stackLevel.lower()[0]
	
numClustersSoFar = 0
lowerClusterNumber = dict()

for phase in phases:
	for potLevel in potLevels:
		for stackLevel in stackLevels:
			key = makeKey(phase, potLevel, stackLevel)

			print("------------------------------------------------------------")
			print("Clustering: " + key)
			
			# get only data with the values we care about
			try:
				filteredData = totalData.filter({"Phase" : phase, "Pot%" : potLevel, "Stack%" : stackLevel})
			except:
				print ("Enum Value not included for this cross-section")
				resultingClusterers[key] = None
				resultingClassifiers[key] = None
				continue
			
			print "Number of player samples for cross section:", len(filteredData)
			if (len(filteredData) <= kmax):
				print ("Not enough data for this cross-section")
				resultingClusterers[key] = None
				resultingClassifiers[key] = None
				continue
			
			# now throw away those values, since they're all the same and will slow down results
			data = orange.ExampleTable(reducedDomain, filteredData)

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
			
			#print data.domain
			#print labelledDomain
			
			# Save image of clustering
			plot_scatter(data, bestClusterer, graphAttribute1, graphAttribute2, directory + key + "Image.png") # save clusters over dimension
			orngClustering.plot_silhouette(bestClusterer, filename=(directory + key + "Silhouette.png"), fast=True) # save silhouette calcs
			
			resultingClusterers[key] = bestClusterer
			resultingClassifiers[key] = knn
			
			lowerClusterNumber[key] = numClustersSoFar
			numClustersSoFar += len(bestClusterer.centroids)
			

print("------------------------------------------------------------")
print "Saving clusterers and classifiers to files... ",
			
clusterersFilepath = directory + "clusterers.pik"
dumpfile = open(clusterersFilepath, 'wc')
pickler = pickle.Pickler(dumpfile)
pickler.dump(resultingClusterers)
dumpfile.close()

classifiersFilepath = directory + "classifiers.pik"
dumpfile = open(classifiersFilepath, 'wc')
pickler = pickle.Pickler(dumpfile)
pickler.dump(resultingClassifiers)
dumpfile.close()

print "Done."

print("------------------------------------------------------------")
print "Classifying players based on constructed classifiers... ",

def createPlayerDataMap(playerName, totalDataPlusNames):
	playerDataMap = dict()
	playerData = totalDataPlusNames.filter({"Player" : playerName})
	
	for phase in phases:
		for potLevel in potLevels:
			for stackLevel in stackLevels:
				key = makeKey(phase, potLevel, stackLevel)
				
				try:
					filteredData = playerData.filter({"Phase" : phase, "Pot%" : potLevel, "Stack%" : stackLevel})
				except:
					playerDataMap[key] = None
					continue
					
				if (len(filteredData) == 0):
					playerDataMap[key] = None
					continue
					
				playerDataMap[key] = orange.Example(reducedDomain, filteredData[0])
				
	return playerDataMap


playersData = dict()
pc = PlayerClassifier(classifiersFilepath)

for d in totalDataPlusNames:
	name = str(d["Player"])
	if playersData.has_key(name):
		continue
	values = createPlayerDataMap(name, totalDataPlusNames)
	playersData[name] = pc.label(name, values)
	
print "Done."
playerFilepath = directory + "playerClusters.txt"
print "Writing player clusters to file",playerFilepath,"... ",

f = open(playerFilepath, "wc")
for name,d in playersData.iteritems():
	f.write(str(name) + "\t")
	for phase in phases:
		for potLevel in potLevels:
			for stackLevel in stackLevels:
				key = makeKey(phase, potLevel, stackLevel)
				val = d[key]
				if val != None and val != "?":
					val = int(val) + lowerClusterNumber[key]
				f.write(str(val) + "\t")
	f.write("\n")
f.close()

print "Done."

