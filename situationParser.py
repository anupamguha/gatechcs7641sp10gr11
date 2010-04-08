import orange, orngTest, orngStat

# ---------------------------------------------------------------------------
#                              SET THESE

situationsFilepath = "C:/Users/hartsoka/Documents/Classes/CS 7641/project/trunk/simulator/stats.tab"
playerStatsFilepath = "C:/Users/hartsoka/Documents/Classes/CS 7641/project/trunk/simulator2/data/agg.tab"

outputDir = "C:/Users/hartsoka/Documents/Classes/CS 7641/project/"

# ---------------------------------------------------------------------------

print "Loading Player Stats...",
playerStatsDataFull = orange.ExampleTable(playerStatsFilepath)
print "Done"

print "Loading Situations...",
situationsDataFull = orange.ExampleTable(situationsFilepath)
print "Done"

print "Removing situations without enough stats to identify a style...",
situationsDataFiltered = situationsDataFull.filter({"Cluster" : -1}, negate=1)
situationsDataFiltered = situationsDataFull.filter({"Action" : "?"}, negate=1)
print "Done"

print "Creating classification data without player model...",
situationsDomainPlain = orange.Domain(situationsDataFiltered.domain[3:])
situationsDataPlain = orange.ExampleTable(situationsDomainPlain, situationsDataFiltered)
print "Done"

print "Creating classification data using clusters...",
situationsDomainByCluster = orange.Domain(situationsDataFiltered.domain[1:2] + situationsDataFiltered.domain[3:])
situationsDataByCluster = orange.ExampleTable(situationsDomainByCluster, situationsDataFiltered)
print "Done"

print "Creating classification data using full player stats...",
print "TODO"

"""
test = situationsDataFiltered.filter({"Action" : "FOLD"}, negate=1)
test = test.filter({"Action" : "BET"}, negate=1)
test = test.filter({"Action" : "RAISE"}, negate=1)
test = test.filter({"Action" : "CALL"}, negate=1)
test = test.filter({"Action" : "CHECK"}, negate=1)
test = test.filter({"Action" : "BLIND"}, negate=1)
test = test.filter({"Action" : "ALL_IN"}, negate=1)
print situationsDomainPlain
for d in test:
	print d
exit()
"""

plainFilepath = outputDir + "situationsPlain.tab"
print "Saving classification data without player model to",plainFilepath,"...",
situationsDataPlain.save(plainFilepath)
print "Done"

clusterFilepath = outputDir + "situationsClustered.tab"
print "Saving classification data without player model to",clusterFilepath,"...",
situationsDataByCluster.save(clusterFilepath)
print "Done"

"""
print "Creating and testing learners...",

majorityLearner = orange.MajorityLearner()
treeLearner = orange.TreeLearner()
learners = [treeLearner, majorityLearner]

trainData = orange.ExampleTable(situationsDomainPlain, situationsDataPlain[0:len(situationsDataPlain)/5])
testData = orange.ExampleTable(situationsDomainPlain, situationsDataPlain[len(situationsDataPlain)/5:len(situationsDataPlain)*2/5])

res = orngTest.learnAndTestOnTestData(learners, trainData, testData)
CAs = orngStat.CA(res, reportSE = 1)
print "Done"
"""
