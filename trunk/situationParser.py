import orange, orngTest, orngStat
import pdb

# ---------------------------------------------------------------------------
#                              SET THESE

situationsFilepath = "C:/Users/hartsoka/Documents/Classes/CS 7641/project/trunk/simulator/situations.tab"
playerStatsFilepath = "C:/Users/hartsoka/Documents/Classes/CS 7641/project/trunk/simulator2/data/aggregatedPlayerHistories.tab"

outputDir = "C:/Users/hartsoka/Documents/Classes/CS 7641/project/"

# ---------------------------------------------------------------------------

def testLearners(data, trainSize, testSize):

	majorityLearner = orange.MajorityLearner()
	treeLearner = orange.TreeLearner()
	#knnLearner = orange.KNNLearner()
	learners = [treeLearner, majorityLearner]
	
	if (trainSize + testSize > len(data)):
		print "TrainSize + TestSize is bigger than data available, aborting"
		return
	
	indicesN = orange.MakeRandomIndicesN(p=[trainSize,testSize])
	ind = indicesN(data)
	trainData = data.select(ind,0)
	testData = data.select(ind,1)
	
	#pdb.set_trace()
	
	if (len(trainData) < 5 or len(testData) < 5):
		print "Insufficient amount of data to learn, aborting"
		return
	
	res = orngTest.learnAndTestOnTestData(learners, trainData, testData)
	CAs = orngStat.CA(res, reportSE = 1)
	for i in range(len(learners)):
		print learners[i]," : CA =", CAs[i]

def getPlayerStats(situationExample, playerDataStatsFull, phase, domain):
	playerDataStats = playerDataStatsFull.filter({"Player" : situationExample["Player"]})
	
	potPercent = situationExample["Pot%"]
	if (phase == "PREFLOP"):
		if (potPercent > .01):
			playerDataStats = playerDataStats.filter({"Pot%" : "HIGH"})
		elif (potPercent == 0):
			playerDataStats = playerDataStats.filter({"Pot%" : "LOW"})
		else:
			playerDataStats = playerDataStats.filter({"Pot%" : "MEDIUM"})
	elif (phase == "FLOP"):
		if (potPercent > .02):
			playerDataStats = playerDataStats.filter({"Pot%" : "HIGH"})
		elif (potPercent < .005):
			playerDataStats = playerDataStats.filter({"Pot%" : "LOW"})
		else:
			playerDataStats = playerDataStats.filter({"Pot%" : "MEDIUM"})
	elif (phase == "TURN"):
		if (potPercent > .05):
			playerDataStats = playerDataStats.filter({"Pot%" : "HIGH"})
		elif (potPercent < .02):
			playerDataStats = playerDataStats.filter({"Pot%" : "LOW"})
		else:
			playerDataStats = playerDataStats.filter({"Pot%" : "MEDIUM"})
	else:
		if (potPercent > .1):
			playerDataStats = playerDataStats.filter({"Pot%" : "HIGH"})
		elif (potPercent < .05):
			playerDataStats = playerDataStats.filter({"Pot%" : "LOW"})
		else:
			playerDataStats = playerDataStats.filter({"Pot%" : "MEDIUM"})
			
	stackPercent = situationExample["Stack%"]
	if (phase == "PREFLOP"):
		if (stackPercent < .10):
			playerDataStats = playerDataStats.filter({"Stack%" : "LOW"})
		elif (stackPercent < .15):
			playerDataStats = playerDataStats.filter({"Stack%" : "MEDIUM"})
		else:
			playerDataStats = playerDataStats.filter({"Stack%" : "HIGH"})
	elif (phase == "FLOP"):
		if (stackPercent < .12):
			playerDataStats = playerDataStats.filter({"Stack%" : "LOW"})
		elif (stackPercent < .25):
			playerDataStats = playerDataStats.filter({"Stack%" : "MEDIUM"})
		else:
			playerDataStats = playerDataStats.filter({"Stack%" : "HIGH"})
	elif (phase == "TURN"):
		if (stackPercent < .13):
			playerDataStats = playerDataStats.filter({"Stack%" : "LOW"})
		elif (stackPercent < .35):
			playerDataStats = playerDataStats.filter({"Stack%" : "MEDIUM"})
		else:
			playerDataStats = playerDataStats.filter({"Stack%" : "HIGH"})
	else:
		if (stackPercent < .04):
			playerDataStats = playerDataStats.filter({"Stack%" : "LOW"})
		elif (stackPercent < .35):
			playerDataStats = playerDataStats.filter({"Stack%" : "MEDIUM"})
		else:
			playerDataStats = playerDataStats.filter({"Stack%" : "HIGH"})
	  
	if len(playerDataStats) == 0:
		return None
	history = playerDataStats[0]
		
	#pdb.set_trace()
	
	playerStatsSlice = list()
	for i in range(4, len(history)):
		playerStatsSlice.append(history[i])
	situationSlice = list()
	for i in range(5, len(situationExample)):
		situationSlice.append(situationExample[i])
		
	ex = orange.Example(domain, playerStatsSlice + situationSlice)
	
	return ex
		
# ---------------------------------------------------------------------------

print "Loading Player Stats...",
playerStatsDataFull = orange.ExampleTable(playerStatsFilepath)
print "Done"

print "Loading Situations...",
situationsDataFull = orange.ExampleTable(situationsFilepath)
print "Done"

print "Removing situations with unknown clusters, unknown actions, and more than 2 players...",
situationsDataFiltered = situationsDataFull.filter({"Cluster" : -1}, negate=1)
situationsDataFiltered = situationsDataFull.filter({"Action" : "?"}, negate=1)
situationsDataFiltered = situationsDataFull.filter({"NumPlayers" : 2})
print "Done"

for phase in ["PREFLOP","FLOP","TURN","RIVER"]:

	print "\nORGANIZING DATA FOR PHASE:",phase
	
	situationsPhaseData = situationsDataFiltered.filter({"Phase" : phase})

	print "Creating classification data without player model...",
	situationsDomainPlain = orange.Domain(situationsPhaseData.domain[5:], True)
	situationsDataPlain = orange.ExampleTable(situationsDomainPlain, situationsPhaseData)
	print len(situationsDataPlain)," found...",
	print "Done"

	print "Creating classification data using clusters...",
	situationsDomainByCluster = orange.Domain(situationsPhaseData.domain[1:2] + situationsPhaseData.domain[5:], True)
	situationsDataByCluster = orange.ExampleTable(situationsDomainByCluster, situationsPhaseData)
	print len(situationsDataByCluster)," found...",
	print "Done"

	print "Creating classification data using full player stats...",
	situationsWithStats = list()
	situationsWithStatsDomain = orange.Domain(playerStatsDataFull.domain[4:] + situationsPhaseData.domain[5:], True)
	for situationExample in situationsPhaseData:
		statsExample = getPlayerStats(situationExample, playerStatsDataFull, phase, situationsWithStatsDomain)
		if (statsExample == None):
			continue
		situationsWithStats.append(statsExample)
	#pdb.set_trace()
	print len(situationsWithStats)," found...",
	skipFull = False
	if len(situationsWithStats) < 5:
		skipFull = True
		print "Skipping...",
	else:
		situationsWithStats = orange.ExampleTable(situationsWithStatsDomain, situationsWithStats)
	#pdb.set_trace()
	print "Done"

	plainFilepath = outputDir + "situationsPlain" + phase + ".tab"
	print "Saving classification data w/o player model to",plainFilepath,"...",
	situationsDataPlain.save(plainFilepath)
	print "Done"

	clusterFilepath = outputDir + "situationsClustered" + phase + ".tab"
	print "Saving classification data w/ clustered player model to",clusterFilepath,"...",
	situationsDataByCluster.save(clusterFilepath)
	print "Done"
	
	historyFilepath = outputDir + "situationsHistoried" + phase + ".tab"
	if not skipFull:
		print "Saving classification data w/ clustered player model to",historyFilepath,"...",
		situationsWithStats.save(historyFilepath)
		print "Done"
	else:
		print "Warning: Skipping historied situations due to lack of examples"

	print "Creating and testing learners on data w/o player model..."
	testLearners(situationsDataPlain, 5, 5)
	print "Done"

	print "Creating and testing learners on data w/ clustered player model..."
	testLearners(situationsDataByCluster, 5, 5)
	print "Done"
	
	if not skipFull:
		print "Creating and testing learners on data w/ stat-based player model..."
		testLearners(situationsWithStats, 5, 5)
		print "Done"
	else:
		print "Warning: Skipping historied situations due to lack of examples"

