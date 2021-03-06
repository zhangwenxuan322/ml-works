## 关于重构

基于《重构》的记录

首先是一些基础的要点：

1. 微小的修改也要进行测试，使用Git对部分修改的过程进行版本控制，以便回溯
2. 函数返回值用`result`命名
3. 减少临时变量的使用，用内联变量替代
4. 独立出计算模块
5. 减少顶层调用函数的代码行数，尽量按逻辑分离函数
6. 营地法则：离开时的代码库一定要比来时更健康
7. 小步走反而可以走的更快，切记保持代码处于可工作状态

### 重构的原则

