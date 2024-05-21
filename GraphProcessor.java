import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.Format;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Scanner;
// import guru.nidi.graphviz.engine.Format;
// import guru.nidi.graphviz.engine.Graphviz;
// import guru.nidi.graphviz.model.MutableGraph;
// import guru.nidi.graphviz.parse.Parser;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class GraphProcessor {

    // 随机因子
    public static Random random;
    static {
        random = new Random();
    }

    // 图的结构
    public static Map<String, Map<String, Integer>> Graph;
    public static int nums;

    // 主函数
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入文件名:");
        String filename = scanner.nextLine();
        Graph = new HashMap<>();// 边表
        // 从文件加载图
        showDirectedGraph(filename);

        System.out.println("开始操作");
        // 显示菜单并处理用户输入以调用不同功能
        while (true) {
            System.out.println("\n菜单:");
            System.out.println("1. 查询桥接词");
            System.out.println("2. 生成新文本");
            System.out.println("3. 计算最短路径");
            System.out.println("4. 随机游走");
            System.out.println("5. 退出");
            System.out.print("请输入你的选择: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // 消耗换行符
            switch (choice) {
                case 1:
                    // 调用查询桥接词函数
                    // 提示用户输入第一个单词
                    System.out.print("Enter the first word: ");
                    String word1 = scanner.nextLine().toLowerCase();
                    // 提示用户输入第二个单词
                    System.out.print("Enter the second word: ");
                    String word2 = scanner.nextLine().toLowerCase();

                    String bridgeWords = queryBridgeWords(word1, word2);
                    // 如果图中没有
                    if (bridgeWords == "f1") {
                        System.out.println("No \"" + word1 + "\" and \"" + word2 + "\" in the graph!");
                    } else if (bridgeWords == "f2") {
                        System.out.println("No \"" + word1 + "\" in the graph!");
                    } else if (bridgeWords == "f3") {
                        System.out.println("No \"" + word2 + "\" in the graph!");
                    }
                    // 如果是空的
                    else if (bridgeWords == "None") {
                        System.out.println("No bridge words from \"" + word1 + "\" to \"" + word2 + "\"!");
                    }
                    // 如果有
                    else {
                        // 使用逗号分割字符串，并将结果添加到列表中
                        String[] tokens = bridgeWords.split(",");
                        List<String> list = new ArrayList<>();
                        for (String token : tokens) {
                            list.add(token);
                        }
                        System.out.print(
                                "The bridge words from \"" + word1 + "\" to \"" + word2
                                        + ((list.size() > 1) ? "\" are: " : "\" is: "));
                        for (int i = 0; i < list.size(); i++) {
                            if (i > 0) {
                                System.out.print(", ");
                            }
                            System.out.print(list.get(i));
                        }
                        System.out.println(".");
                    }

                    break;
                case 2:
                    // 提示用户输入新文本
                    System.out.print("Enter the new text: ");
                    String Text = scanner.nextLine();
                    String text = Text.replaceAll("[^a-zA-Z]", " ").toLowerCase();
                    text = text.replaceAll("\\s+", " ");
                    String newtext = generateNewText(text);
                    System.out.print(newtext);
                    break;
                case 3:
                    // 调用计算最短路径函数
                    System.out.println("请输入起点和终点，中间用空格间隔开来");
                    String start_end_node = scanner.nextLine();
                    String[] start_end = start_end_node.split("\\s+");

                    // 若是得到起点和终点
                    if (start_end.length == 2) {
                        String start = start_end[0];
                        String end = start_end[1];
                        String shortestPath = calcShortestPath(start, end);
                        nums++;
                        if (shortestPath != null) {

                            System.out.println("-------------------------------------------------------");
                            System.out.println("找到了从" + start + " 到 " + end + " 的路径。");
                            System.out.println("最短路径为：" + shortestPath);
                            String[] String_short = shortestPath.split(", ");
                            int road_length = 0;
                            int path_size = String_short.length;
                            for (int i = 0; i < path_size - 1; i++) {
                                String pre_word = String_short[i];
                                String new_word = String_short[i + 1];
                                Map<String, Integer> innerMap = Graph.get(pre_word);
                                int edge_weight = innerMap.get(new_word);
                                road_length = road_length + edge_weight;
                            }
                            System.out.println("最短路径长度为：" + road_length);
                            System.out.println("-------------------------------------------------------");

                        } else {
                            System.out.println("找不到从 " + start + " 到 " + end + " 的路径。");
                        }
                    }
                    // 若是只得到起点
                    else if (start_end.length == 1) {
                        String start = start_end[0];
                        Set<String> visited = new HashSet<>();// 标记已经作为终点的字符串
                        visited.add(start);

                        // 遍历其他结点
                        nums = 1;
                        for (Map.Entry<String, Map<String, Integer>> entry : Graph.entrySet()) {
                            String out_word = entry.getKey();
                            Map<String, Integer> innerMap = entry.getValue();
                            if (!visited.contains(out_word)) {
                                visited.add(out_word);
                                String end = out_word;
                                String shortestPath = calcShortestPath(start, end);
                                nums++;
                                if (shortestPath != null) {

                                    System.out.println("-------------------------------------------------------");
                                    System.out.println("找到了从" + start + " 到 " + end + " 的路径。");
                                    System.out.println("最短路径为：" + shortestPath);
                                    String[] String_short = shortestPath.split(", ");
                                    int road_length = 0;
                                    int path_size = String_short.length;
                                    for (int i = 0; i < path_size - 1; i++) {
                                        String pre_word = String_short[i];
                                        String new_word = String_short[i + 1];
                                        Map<String, Integer> innerMap1 = Graph.get(pre_word);
                                        int edge_weight = innerMap1.get(new_word);
                                        road_length = road_length + edge_weight;
                                    }
                                    System.out.println("最短路径长度为：" + road_length);
                                    System.out.println("-------------------------------------------------------");

                                } else {
                                    System.out.println("找不到从 " + start + " 到 " + end + " 的路径。");
                                }
                            }
                            for (String in_word : innerMap.keySet()) {
                                if (!visited.contains(in_word)) {
                                    visited.add(in_word);
                                    String end = in_word;
                                    String shortestPath = calcShortestPath(start, end);
                                    nums++;
                                    if (shortestPath != null) {

                                        System.out.println("-------------------------------------------------------");
                                        System.out.println("找到了从" + start + " 到 " + end + " 的路径。");
                                        System.out.println("最短路径为：" + shortestPath);
                                        String[] String_short = shortestPath.split(", ");
                                        int road_length = 0;
                                        int path_size = String_short.length;
                                        for (int i = 0; i < path_size - 1; i++) {
                                            String pre_word = String_short[i];
                                            String new_word = String_short[i + 1];
                                            Map<String, Integer> innerMap2 = Graph.get(pre_word);
                                            int edge_weight = innerMap2.get(new_word);
                                            road_length = road_length + edge_weight;
                                        }
                                        System.out.println("最短路径长度为：" + road_length);
                                        System.out.println("-------------------------------------------------------");
                                    } else {
                                        System.out.println("找不到从 " + start + " 到 " + end + " 的路径。");
                                    }
                                }

                            }
                        }
                    }

                    break;
                case 4:

                    AtomicReference<String> randomWalkResult = new AtomicReference<>("");

                    Thread traversalThread = new Thread(() -> {
                        System.out.println("\nStarting random walk... ");
                        System.out.println("If you want to stop the traversal, enter stop");
                        randomWalkResult.set(randomWalk());
                    });

                    traversalThread.start();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                    while (traversalThread.isAlive()) {
                        try {
                            if (reader.ready()) {
                                String userInput = reader.readLine();
                                if (userInput.equals("stop")) {
                                    System.out.println("The user stopped the random walk");
                                    traversalThread.interrupt();
                                    break;
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    try {
                        traversalThread.join(); // 等待遍历线程完成
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Random walk finished.\nRandom walk result: " + randomWalkResult.get());

                    break;
                case 5:
                    System.out.println("正在退出...");
                    System.exit(0);
                default:
                    System.out.println("无效选择！");
            }
        }
    }

    // 函数：显示有向图
    public static void showDirectedGraph(String filename) {
        // 读取文本并处理
        StringBuilder fullText = new StringBuilder(); // 用于存储完整的文本
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                fullText.append(line).append(" "); // 将当前行文本添加到完整文本中，并加上一个空格
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("An error occurred while reading the file: " + e.getMessage());
        }

        // 将非字母字符替换为空格，并转换为小写
        String processedText = fullText.toString().replaceAll("[^a-zA-Z]", " ").toLowerCase();
        // 去除重复的空格
        processedText = processedText.replaceAll("\\s+", " ");
        // 输出处理后的文本
        System.out.println(processedText);

        // 将处理后的文本分割成单词数组
        String[] words = processedText.split("\\s+");
        // 逐个处理单词
        for (int i = 0; i < words.length - 1; i++) {
            String wordA = words[i];
            String wordB = words[i + 1];

            // 检查是否已存在 wordA -> wordB 的关系
            if (Graph.containsKey(wordA)) {
                Map<String, Integer> innerMap = Graph.get(wordA);
                // 如果已经存在 wordA -> wordB 的关系，则增加计数
                if (innerMap.containsKey(wordB)) {
                    int count = innerMap.get(wordB);
                    innerMap.put(wordB, count + 1);
                } else {
                    // 否则，将 wordA -> wordB 的关系存入内部 Map
                    innerMap.put(wordB, 1);
                }
            } else {
                // 如果不存在 wordA -> wordB 的关系，则创建新的内部 Map 并存入关系
                Map<String, Integer> innerMap = new HashMap<>();
                innerMap.put(wordB, 1);
                Graph.put(wordA, innerMap);
            }
        }
        // 将该图显示出来
        // 将图结构转换为DOT语言
        String dotContent = convertToDot(Graph, null);
        // 将DOT语言保存为.dot文件
        saveDotFile(dotContent, "./graph/graph.dot");
        // 使用Graphviz将DOT文件转换为PNG图像
        convertDotToPng("./graph/graph.dot", "./graph/graph.png");
    }

    // 将图结构转换为DOT语言
    public static String convertToDot(Map<String, Map<String, Integer>> graph, List<String> shortestPaths) {
        StringBuilder dotContent = new StringBuilder();
        dotContent.append("digraph MyGraph {\n");
        for (Map.Entry<String, Map<String, Integer>> entry : graph.entrySet()) {
            String sourceNode = entry.getKey();
            Map<String, Integer> edges = entry.getValue();
            for (Map.Entry<String, Integer> edgeEntry : edges.entrySet()) {
                String targetNode = edgeEntry.getKey();
                int weight1 = edgeEntry.getValue();
                String edgeColor = "black"; // 默认颜色为黑色
                if (shortestPaths != null) {
                    for (int i = 0; i < shortestPaths.size() - 1; i++) {
                        if ((sourceNode == shortestPaths.get(i)) && targetNode == shortestPaths.get(i + 1)) {
                            edgeColor = "red"; // 最短路径上的边颜色为红色
                        }
                    }
                }
                dotContent.append(String.format("    \"%s\" -> \"%s\" [label=\"%d\", color=\"%s\"];\n", sourceNode,
                        targetNode, weight1, edgeColor));
            }
        }
        // if(shortestPaths !=null)
        // {
        // for(int i = 0; i < shortestPaths.size() - 1; i++)
        // {
        // String sourceNode = shortestPaths.get(i);
        // String targetNode = shortestPaths.get(i+1);
        // Map<String, Integer> innerMap = Graph.get(sourceNode);
        // if (innerMap != null && innerMap.containsKey(targetNode)) {
        // int weight2 = innerMap.get(targetNode);
        // String edgeColor = "red"; // 最短路径上的边颜色为红色
        // dotContent.append(String.format(" \"%s\" -> \"%s\" [label=\"%d\",
        // color=\"%s\"];\n", sourceNode, targetNode, weight2, edgeColor));
        // }
        // }
        // }

        dotContent.append("}");
        return dotContent.toString();
    }

    // 将DOT语言保存为.dot文件
    public static void saveDotFile(String dotContent, String fileName) {
        try {
            FileWriter writer = new FileWriter(fileName);
            writer.write(dotContent);
            writer.close();
            // System.out.println("DOT文件已保存为：" + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 使用Graphviz将DOT文件转换为PNG图像
    public static void convertDotToPng(String dotFileName, String pngFileName) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("dot", "-Tpng", dotFileName, "-o", pngFileName);
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            // if (exitCode == 0) {
            // System.out.println("有向图生成成功: " + pngFileName);
            // } else {
            // System.out.println("有向图生成失败");
            // }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    // 函数：查询桥接词
    public static String queryBridgeWords(String word1, String word2) {

        // 检查图中是否包含输入的单词

        boolean word1Exists = false;
        boolean word2Exists = false;
        // 遍历 wordGraph 的键值对
        for (Map.Entry<String, Map<String, Integer>> entry : Graph.entrySet()) {
            String wordA = entry.getKey();
            Map<String, Integer> innerMap = entry.getValue();

            // 检查是否存在 word1 在键中
            if (wordA.equals(word1) || innerMap.containsKey(word1)) {
                word1Exists = true;
            }
            // 检查是否存在 word2 在键中
            if (wordA.equals(word2) || innerMap.containsKey(word2)) {
                word2Exists = true;
            }
        }

        if (!word1Exists || !word2Exists) {
            String result = "";
            // 如果二者都不存在
            if (!word1Exists && !word2Exists) {
                result = "f1";
            }
            // 如果分别只有1或者2不存在
            else if (!word1Exists) {
                result = "f2";
            } else if (!word2Exists) {
                result = "f3";
            }
            return result;
        }

        // 获取 word1 到 word2 的所有桥接词

        List<String> bridgeWords = new ArrayList<>();
        Map<String, Integer> innerMap = Graph.get(word1);

        // 检查是否存在桥接词
        for (String bridgeWord : innerMap.keySet()) {
            // 检查当前桥接词是否同时与 word2 相连
            if (Graph.containsKey(bridgeWord) && Graph.get(bridgeWord).containsKey(word2)) {
                // 如果是，则将桥接词添加到列表中
                bridgeWords.add(bridgeWord);
            }
        }

        // 返回结果
        if (bridgeWords.isEmpty()) {
            return "None";
        } else {
            StringBuilder sb = new StringBuilder();
            for (String item : bridgeWords) {
                // 添加元素到字符串，并用逗号分隔
                sb.append(item).append(",");
            }
            // 去掉末尾多余的逗号
            if (sb.length() > 0) {
                sb.setLength(sb.length() - 1);
            }
            return sb.toString();
        }
    }

    // 函数：生成新文本
    public static String generateNewText(String inputText) {
        // 将输入的文本分割成单词数组
        String[] words = inputText.split("\\s+");
        List<String> resultWords = new ArrayList<>();

        // 构建带有桥接词的新文本单词数组
        for (int i = 0; i < words.length - 1; i++) {
            // 将当前单词添加到结果数组中
            resultWords.add(words[i]);

            // 检查当前单词和下一个单词之间是否存在桥接词
            String bridgeWords = queryBridgeWords(words[i], words[i + 1]);
            if (bridgeWords != "None" && bridgeWords != "f1" && bridgeWords != "f2" && bridgeWords != "f3") {
                String[] tokens = bridgeWords.split(",");
                List<String> list = new ArrayList<>();
                for (String token : tokens) {
                    list.add(token);
                }
                // 生成一个随机索引
                int randomIndex = random.nextInt(list.size());

                // 获取对应索引处的 bridgeWord
                String bridgeWord = list.get(randomIndex);

                // 将 bridgeWord 添加到 resultWords 中
                resultWords.add(bridgeWord);
            }
        }

        // 添加输入文本中的最后一个单词
        resultWords.add(words[words.length - 1]);

        // 将结果数组转换为新文本并返回
        return String.join(" ", resultWords);
    }

    // 函数：计算两个单词之间的最短路径（Dijkatra算法）
    public static String calcShortestPath(String word1, String word2) {
        boolean word1Exists = false;
        boolean word2Exists = false;
        for (Map.Entry<String, Map<String, Integer>> entry : Graph.entrySet()) {
            String wordA = entry.getKey();
            Map<String, Integer> innerMap = entry.getValue();
            // 检查是否存在 word1 在键中
            if (wordA.equals(word1) || innerMap.containsKey(word1)) {
                word1Exists = true;

            }
            // 检查是否存在 word2 在键中
            if (wordA.equals(word2) || innerMap.containsKey(word2)) {
                word2Exists = true;

            }
        }

        if (word1Exists == false) {
            // System.out.println("找不到起点："+word1);
            return null;
        }
        if (word2Exists == false) {
            // System.out.println("找不到终点："+word2);
            return null;
        }

        // 定义三个数组，存储当前距离、前驱结点、标记是否访问过
        Map<String, Integer> distance = new HashMap<>();
        Map<String, String> predecessor = new HashMap<>();
        Set<String> visited = new HashSet<>();

        // 初始化，将起点到每个节点的距离初始化为无穷大
        for (Map.Entry<String, Map<String, Integer>> entry : Graph.entrySet()) {
            String out_word = entry.getKey();
            Map<String, Integer> innerMap = entry.getValue();
            if (!distance.containsKey(out_word)) {
                distance.put(out_word, Integer.MAX_VALUE);
                // System.out.println(out_word);
            }
            for (String in_word : innerMap.keySet()) {
                if (!distance.containsKey(in_word)) {
                    distance.put(in_word, Integer.MAX_VALUE);
                    // System.out.println(in_word);
                }

            }
        }
        distance.put(word1, 0); // 起始节点到自身的距离为0

        // 使用优先队列实现堆
        PriorityQueue<String> queue = new PriorityQueue<>(Comparator.comparingInt(distance::get));
        queue.add(word1);

        // Dijkstra 算法
        while (!queue.isEmpty()) {
            String currentNode = queue.poll();
            // System.out.println("currentNode:"+currentNode);
            visited.add(currentNode);

            // 更新当前节点的邻居节点的距离
            if (Graph.containsKey(currentNode)) {
                for (Map.Entry<String, Integer> neighborEntry : Graph.get(currentNode).entrySet()) {
                    String neighbor = neighborEntry.getKey();
                    // System.out.println("neighbor:"+neighbor);
                    int weight = neighborEntry.getValue();
                    // System.out.println("weight:"+weight);
                    if (!visited.contains(neighbor)) {
                        int newDistance = distance.get(currentNode) + weight;
                        // System.out.println("newDistance:"+newDistance);
                        if (newDistance < distance.get(neighbor)) {
                            distance.put(neighbor, newDistance);
                            predecessor.put(neighbor, currentNode);
                            queue.add(neighbor);
                        }
                    }
                }
            }

        }

        // 构建最短路径
        List<String> shortestPath = new ArrayList<>();
        String current = word2;
        while (predecessor.get(current) != null) {
            shortestPath.add(current);
            current = predecessor.get(current);
        }
        if (shortestPath.isEmpty()) {
            return null;
        }
        shortestPath.add(word1);
        Collections.reverse(shortestPath);

        // 将该图显示出来
        // 将图结构转换为DOT语言
        String dotContent = convertToDot(Graph, shortestPath);
        // 将DOT语言保存为.dot文件
        saveDotFile(dotContent, "./graph/graph_least_road_" + nums + ".dot");
        // 使用Graphviz将DOT文件转换为PNG图像
        convertDotToPng("./graph/graph_least_road_" + nums + ".dot", "./graph/graph_least_road_" + nums + ".png");

        // 转换成字符串输出
        StringBuilder pathBuilder = new StringBuilder();
        for (String word : shortestPath) {
            pathBuilder.append(word).append(", ");
        }
        // 删除最后一个逗号和空格
        if (pathBuilder.length() > 0) {
            pathBuilder.delete(pathBuilder.length() - 2, pathBuilder.length());
        }
        return pathBuilder.toString();
    }

    // 函数：随机游走
    public static String randomWalk() {
        // 随机选择一个起始节点
        List<String> nodes = new ArrayList<>(Graph.keySet());
        String currentNode = nodes.get(random.nextInt(nodes.size()));

        // 记录经过的节点
        List<String> Path = new ArrayList<>();
        Path.add(currentNode);

        // 记录遍历过的边
        Map<String, Set<String>> walkEdges = new HashMap<>();

        try {
            // 创建文件写入器
            FileWriter writer = new FileWriter("random_walk_path.txt");
            // 将当前节点写入文件
            writer.write(currentNode + " ");

            // 在每次迭代中检查是否有用户输入
            while (!Thread.currentThread().isInterrupted()) {
                // 获取当前节点的出边
                Map<String, Integer> edges = Graph.get(currentNode);

                // 如果当前节点没有出边，或者所有出边都已经访问过，结束遍历
                if (edges == null || edges.isEmpty() || edges.keySet().equals(walkEdges.get(currentNode))) {
                    break; // 停止遍历
                }

                // 从当前节点的出边中随机选择一个未遍历过的节点
                List<String> availableEdges = new ArrayList<>(edges.keySet());
                if (walkEdges.get(currentNode) != null) {
                    availableEdges.removeAll(walkEdges.get(currentNode)); // 移除已经遍历过的节点
                }
                String nextNode = availableEdges.get(random.nextInt(availableEdges.size()));

                // 记录遍历过的边
                walkEdges.putIfAbsent(currentNode, new HashSet<>());
                walkEdges.get(currentNode).add(nextNode);

                // 将当前节点添加到遍历路径中
                Path.add(nextNode);
                writer.write(" " + nextNode);
                // 更新当前节点
                currentNode = nextNode;

                // 计时器，等待1秒钟
                long startTime = System.currentTimeMillis();
                while (System.currentTimeMillis() - startTime < 500) {
                } // 延缓操作
            }
            // 关闭文件写入器
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 返回遍历路径
        return String.join(" ", Path);
    }

}
