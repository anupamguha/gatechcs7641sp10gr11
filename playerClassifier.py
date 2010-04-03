import orange, orngTest, orngStat
import pickle

from DirtyKNNClassifier import *

PREFLOP = "p"
FLOP = "f"
TURN = "t"
RIVER = "r"
LOW = "l"
MED = "m"
HIGH = "h"

# Change back when done testing
phases = [PREFLOP]
stackLevels = [LOW]
potLevels = [LOW]
#phases = [PREFLOP, FLOP, TURN, RIVER]
#stackLevels = [LOW, MED, HIGH]
#potLevels = [LOW, MED, HIGH]

classifiersFilepath = "C:/Users/hartsoka/Documents/Classes/CS 7641/project/trunk/simulator/data/classifiers.pik"
classifiersFile = open(classifiersFilepath, 'r')
up = pickle.Unpickler(classifiersFile)
strategyClassifiers = up.load()
classifiersFile.close()

#playerData = { "pll" : [0.0,0.375,0.75,0.0,452.3333333333333,952.1120187752574,0.0,0.25,1200.0]} # input
playerData = { "pll" : [0.05555555555555555,0.16666666666666666,0.1111111111111111,0.0,0.002515540114986834,0.0028491289710054985,0.0,0.7777777777777778,0.003467371925791251]}
playerStrategiesDict = dict() # output
playerStrategiesList = list() #output

def makeKey(phase, potLevel, stackLevel):
	#return phase + "," + stackLevel + "," + potLevel
	return phase + stackLevel + potLevel

for phase in phases:
	for potLevel in potLevels:
		for stackLevel in stackLevels:
		
			key = makeKey(phase, potLevel, stackLevel)
			print "Player strategy for", key,"=",
		
			classifier = strategyClassifiers[key]
			playerAttribs = playerData[key]
			playerExample = orange.Example(classifier.getDomain(), playerAttribs)
			label = classifier.label(playerExample)
			playerStrategiesDict[key] = label
			playerStrategiesList.append(label)
			print label