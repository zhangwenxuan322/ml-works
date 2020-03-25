from numpy import *
import operator

# 创建虚拟数据集和标签
def createDataSet():
    # 虚拟数据集
    group = array(
        [
            [1.0, 1.1],  # A
            [1.0, 1.0],  # A
            [0, 0],  # B
            [0, 0.1]  # B
        ]
    )
    # 虚拟标签
    labels = ['A', 'A', 'B', 'B']
    return group, labels

# inX用于分类
# dataSet作为训练样本集
# labels作为标签
# k用于选择近邻的个数
def classify0(inX, dataSet, labels, k):
    dataSetSize = dataSet.shape[0]
    # 距离计算
    diffMat = tile(inX, (dataSetSize, 1)) - dataSet
    sqDiffMat = diffMat**2
    sqDistances = sqDiffMat.sum(axis=1)
    distances = sqDistances**0.5
    sortedDistIndicies = distances.argsort()
    classCount = {}
    # 选择距离最小的k个点
    for i in range(k):
        voteIlabel = labels[sortedDistIndicies[i]]
        classCount[voteIlabel] = classCount.get(voteIlabel, 0) + 1
    sortedClassCount = sorted(classCount.items(),
                              # 排序
                              key=operator.itemgetter(1),
                              reverse=True
                              )
    return sortedClassCount[0][0]
