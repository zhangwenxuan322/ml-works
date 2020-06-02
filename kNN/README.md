# k-近邻算法（kNN）

## 概述

k近邻算法采用测量不同特征值之间的距离方法进行分类

**优点**：精度高，对异常值不敏感，无数据输入假定

**缺点**：计算复杂度高，空间复杂度高。适用数据范围：数据值和标称型

## 工作原理

我们有一个样本数据集合，样本集中**每个数据都有标签**，输入没有标签的新数据之后，将该数据与样本集中的数据进行特征比对，从样本集中提取特征最相似的数据的分类标签。一般选择前k个，k通常不大于20。最后，k个相似数据中出现次数最多的分类最为新数据的分类。

## 流程

1. 收集数据
2. 准备数据：距离计算所需要的数值，最好是结构化的数据格式。
3. 分析数据
4. 训练算法：此步骤可跳过（不适用于kNN）
5. 测试算法：计算错误率
6. 使用算法