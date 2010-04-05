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
#phases = [PREFLOP]
#stackLevels = [LOW]
#potLevels = [LOW]
phases = [PREFLOP, FLOP, TURN, RIVER]
stackLevels = [LOW, MED, HIGH]
potLevels = [LOW, MED, HIGH]

"""
# Change back when done testing
phases = [PREFLOP]
stackLevels = [LOW]
potLevels = [LOW]
#phases = [PREFLOP, FLOP, TURN, RIVER]
#stackLevels = [LOW, MED, HIGH]
#potLevels = [LOW, MED, HIGH]
"""

def makeKey(phase, potLevel, stackLevel):
	return phase + potLevel + stackLevel

class PlayerClassifier:

	def __init__(self, filepath):
		classifiersFile = open(filepath, 'r')
		up = pickle.Unpickler(classifiersFile)
		self.strategyClassifiers = up.load()
		classifiersFile.close()
		
	def label(self, name, playerData):
		playerStrategiesDict = dict()
		playerStrategiesList = list()
		
		for phase in phases:
			for potLevel in potLevels:
				for stackLevel in stackLevels:
				
					key = makeKey(phase, potLevel, stackLevel)
					#print name,"strategy for", key,"=",
				
					classifier = self.strategyClassifiers[key]
					playerAttribs = playerData[key]
					
					if playerAttribs != None and classifier != None:
						playerExample = orange.Example(classifier.getDomain(), playerAttribs)
						label = classifier.label(playerExample)
					else:
						label = "?"
						
					playerStrategiesDict[key] = label
					playerStrategiesList.append(label)
					#print label

		return playerStrategiesDict
					