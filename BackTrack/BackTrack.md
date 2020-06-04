# 全排列
三个核心：
1. 路径：已做出的选择
2. 选择列表：当前可以做的选择
3. 结束条件：到达树的底层
代码框架：
```
result = []
def backtrack(路径, 选择列表):
    if 满足结束条件:
        result.add(路径)
        return

    for 选择 in 选择列表:
        做选择
        backtrack(路径, 选择列表)
        撤销选择
```