import orange, orngClustering

class DirtyKNNClassifier:
	def __init__(self, centroids, distFn):
		self.centroids = centroids
		self.distFn = distFn
	def label(self, example):
		best = -1
		bestDist = 100000
		for centroid in self.centroids:
			d = self.distFn(example, centroid)
			if (d < bestDist):
				bestDist = d
				best = centroid
		#print testCopy.getclass(),"vs",best
		ix = self.centroids.index(best)
		return ix
	def getDomain(self):
		return self.centroids[0].domain