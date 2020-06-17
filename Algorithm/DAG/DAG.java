package DAG;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

class DAG {
    // 邻接矩阵
	static int[][] graph = new int[200][200];
	// 结点个数和边的个数
	static int vNum,eNum;
	// 记录每个结点的入度，初始化为0
	static int[] count = new int[200];
	// 用队列保存拓扑序列
	static Queue<Integer> queue = new LinkedList<>();
	
	// 拓扑排序
	void topoSort(){
		// 入度为0的结点的个数，也就是入队个数
		int number = 0;
		// 暂时存放拓扑序列
		Queue<Integer> temp = new LinkedList<Integer>();
		// 遍历图中所有结点，找入度为0的结点删除（放进队列）
		for(int i=1;i<=vNum;i++){
			if(count[i] == 0){
				queue.offer(i);
			}
		}
		// 删除这些被删除结点的出边（即对应结点入度减一）
		while(!queue.isEmpty()){
			int i = queue.peek();
			temp.offer(queue.poll());
			number++;
			for(int j=1;j<=vNum;j++){
				if(graph[i][j] == 1){
					count[j] -= 1;
					//出现了新的入度为0的结点，删除
					if(count[j] == 0){
						queue.offer(j);
					}
				}
			}
		}
		if(number != vNum){
			System.out.println("最后存在入度为1的结点，这个有向图是有回路的。");
		}else{
			System.out.println("这个有向图不存在回路，拓扑序列为：" + temp.toString());
		}
	}
	
	// 创建图,以邻接矩阵表示
	void create(){
		Scanner sc = new Scanner(System.in);
		System.out.println("正在创建图，请输入顶点个数vNum：");
		vNum = sc.nextInt();
		System.out.println("请输入边个数eNum：");
        eNum = sc.nextInt();
		// 初始化邻接矩阵为0（如果3个顶点，顶点分别是1，2，3）
		for(int i=1;i<=vNum;i++){
			for(int j=1;j<=vNum;j++){
				graph[i][j] = 0;
			}
		}
		// 输入边的情况
		System.out.println("请输入边的头和尾:");
		for(int k=1;k<=eNum;k++){
			int i = sc.nextInt();
			int j = sc.nextInt();
			graph[i][j] = 1;
		}
		// 计算每个结点的入度
		Arrays.fill(count, 0);// 先初始化为0
		for(int i=1;i<=vNum;i++){
			for(int j=1;j<=vNum;j++){
				if(graph[i][j] == 1){
					count[j] = count[j] + 1;
				}
			}
        }
        sc.close();
	}
	
	public static void main(String[] args) {
		DAG dag = new DAG();
		dag.create();
		dag.topoSort();
	}
}