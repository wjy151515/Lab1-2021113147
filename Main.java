import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.Format;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;

public class Main {

    public static boolean st = false;
    // 随机因子
    public static Random random;
    static {
        random = new Random();
    }
    // 图的结构
    public static Map<String, Map<String, Integer>> Graph;
    public static int nums;

    public static void main(String[] args) {
        // 创建主窗口
        JFrame frame = new JFrame("文本处理程序");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        // 创建菜单面板
        JPanel menuPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        frame.add(menuPanel);

        // 添加标题
        JLabel titleLabel = new JLabel("欢迎使用文本处理程序");
        menuPanel.add(titleLabel);

        // 添加文本输入框和确认按钮
        JTextField inputTextField = new JTextField();
        JButton confirmButton = new JButton("确认");
        menuPanel.add(inputTextField);
        menuPanel.add(confirmButton);

        // 确认按钮的事件处理程序
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 从文本输入框中获取文件名
                String filename = inputTextField.getText();
                // 清空边表
                Graph = new HashMap<>();
                // 从文件加载图
                showDirectedGraph(filename);

                // 创建新窗口
                JFrame newFrame = new JFrame("新窗口");
                newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                newFrame.setSize(400, 300);

                // 创建新窗口的菜单面板
                JPanel newMenuPanel = new JPanel(new GridLayout(6, 1, 10, 10));
                newFrame.add(newMenuPanel);

                // 添加按钮到新窗口
                JButton graghButton = new JButton("查看有向图");
                JButton bridgeButton = new JButton("查询桥接词");
                JButton newTextButton = new JButton("生成新文本");
                JButton shortestPathButton = new JButton("计算最短路径");
                JButton randomWalkButton = new JButton("随机游走");
                JButton exitButton = new JButton("退出");

                newMenuPanel.add(graghButton);
                newMenuPanel.add(bridgeButton);
                newMenuPanel.add(newTextButton);
                newMenuPanel.add(shortestPathButton);
                newMenuPanel.add(randomWalkButton);
                newMenuPanel.add(exitButton);

                // 设置按钮的事件处理程序
                graghButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // 从某个地址读取图像
                        ImageIcon icon = new ImageIcon("example.png");

                        // 显示图像对话框
                        JOptionPane.showMessageDialog(null, icon, "查看有向图", JOptionPane.PLAIN_MESSAGE);
                    }
                });

                bridgeButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // 弹出输入框让用户输入第一个单词
                        String word1 = JOptionPane.showInputDialog(null, "Enter the first word:", "输入单词1",
                                JOptionPane.PLAIN_MESSAGE);
                        if (word1 != null && !word1.isEmpty()) {
                            word1 = word1.toLowerCase(); // 转换为小写
                            // 弹出输入框让用户输入第二个单词
                            String word2 = JOptionPane.showInputDialog(null, "Enter the second word:", "输入单词2",
                                    JOptionPane.PLAIN_MESSAGE);
                            if (word2 != null && !word2.isEmpty()) {
                                word2 = word2.toLowerCase(); // 转换为小写
                                // 查询桥接词
                                String bridgeWords = queryBridgeWords(word1, word2);
                                // 构建显示的消息
                                String message;
                                if (bridgeWords.equals("f1")) {
                                    message = "No \"" + word1 + "\" and \"" + word2 + "\" in the graph!";
                                } else if (bridgeWords.equals("f2")) {
                                    message = "No \"" + word1 + "\" in the graph!";
                                } else if (bridgeWords.equals("f3")) {
                                    message = "No \"" + word2 + "\" in the graph!";
                                } else if (bridgeWords.equals("None")) {
                                    message = "No bridge words from \"" + word1 + "\" to \"" + word2 + "\"!";
                                } else {
                                    String[] tokens = bridgeWords.split(",");
                                    StringBuilder builder = new StringBuilder();
                                    for (int i = 0; i < tokens.length; i++) {
                                        if (i > 0) {
                                            builder.append(", ");
                                        }
                                        builder.append(tokens[i]);
                                    }
                                    message = "The bridge words from \"" + word1 + "\" to \"" + word2 + "\" are: "
                                            + builder.toString();
                                }
                                // 弹出消息对话框显示结果
                                JOptionPane.showMessageDialog(null, message, "查询结果", JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                    }
                });

                newTextButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // 弹出输入框让用户输入新文本
                        String text = JOptionPane.showInputDialog(null, "Enter the new text:", "输入新文本",
                                JOptionPane.PLAIN_MESSAGE);
                        if (text != null && !text.isEmpty()) {
                            // 处理文本并生成新文本
                            text = text.replaceAll("[^a-zA-Z]", " ").toLowerCase();
                            text = text.replaceAll("\\s+", " ");
                            String newtext = generateNewText(text);
                            // 弹出消息对话框显示生成的新文本
                            JOptionPane.showMessageDialog(null, newtext, "生成的新文本", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                });

                shortestPathButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // 弹出输入框让用户输入起点和终点
                        String input = JOptionPane.showInputDialog(null, "请输入起点和终点，中间用空格间隔开来", "输入起点和终点",
                                JOptionPane.PLAIN_MESSAGE);
                        if (input != null && !input.isEmpty()) {
                            // 调用方法处理计算最短路径的逻辑，并获取结果
                            String result = calculateShortestPath(input);
                            // 弹窗显示结果
                            JOptionPane.showMessageDialog(null, result, "计算最短路径结果", JOptionPane.PLAIN_MESSAGE);
                        }
                    }
                });

                randomWalkButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // 弹出消息框，提醒用户如何停止遍历
                        JOptionPane.showMessageDialog(null, "If you want to stop the traversal, enter 'stop'",
                                "Random Walk", JOptionPane.INFORMATION_MESSAGE);

                        // 创建一个对话框以停止遍历
                        JButton stopButton = new JButton("Stop");
                        stopButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                st = true; // 中断线程
                                JOptionPane.getRootFrame().dispose(); // 关闭父对话框
                            }
                        });

                        JOptionPane.showMessageDialog(null, stopButton, "Stop Traversal",
                                JOptionPane.PLAIN_MESSAGE);

                        // 执行随机遍历
                        String result = randomWalk();

                        // 弹出结果消息框
                        JOptionPane.showMessageDialog(null,
                                "Random walk finished.\nRandom walk result: " + result,
                                "Traversal Result", JOptionPane.INFORMATION_MESSAGE);
                    }
                });

                exitButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // 在这里添加退出的逻辑
                        System.out.println("退出");
                        newFrame.dispose(); // 关闭新窗口
                    }
                });

                // 显示新窗口
                newFrame.setVisible(true);
            }
        });

        // 显示主窗口
        frame.setVisible(true);
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
            while (!st) {
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
                while (System.currentTimeMillis() - startTime < 100) {
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

    // 创建一个方法来处理计算最短路径的逻辑
    public static String calculateShortestPath(String input) {
        String[] startEnd = input.split("\\s+");
        String result;
        // 若得到起点和终点
        if (startEnd.length == 2) {
            String start = startEnd[0];
            String end = startEnd[1];
            String shortestPath = calcShortestPath(start, end);
            if (shortestPath != null) {
                result = "找到了从" + start + " 到 " + end + " 的路径。\n最短路径为：" + shortestPath;
                String[] shortest = shortestPath.split(", ");
                int roadLength = 0;
                for (int i = 0; i < shortest.length - 1; i++) {
                    String preWord = shortest[i];
                    String newWord = shortest[i + 1];
                    Map<String, Integer> innerMap = Graph.get(preWord);
                    int edgeWeight = innerMap.get(newWord);
                    roadLength += edgeWeight;
                }
                result += "\n最短路径长度为：" + roadLength;
            } else {
                result = "找不到从 " + start + " 到 " + end + " 的路径。";
            }
        } else if (startEnd.length == 1) { // 若只得到起点
            String start = startEnd[0];
            Set<String> visited = new HashSet<>(); // 标记已经作为终点的字符串
            visited.add(start);
            StringBuilder builder = new StringBuilder();
            for (Map.Entry<String, Map<String, Integer>> entry : Graph.entrySet()) {
                String outWord = entry.getKey();
                Map<String, Integer> innerMap = entry.getValue();
                if (!visited.contains(outWord)) {
                    visited.add(outWord);
                    String end = outWord;
                    String shortestPath = calcShortestPath(start, end);
                    if (shortestPath != null) {
                        builder.append("找到了从").append(start).append(" 到 ").append(end).append(" 的路径。\n最短路径为：")
                                .append(shortestPath);
                        String[] shortest = shortestPath.split(", ");
                        int roadLength = 0;
                        for (int i = 0; i < shortest.length - 1; i++) {
                            String preWord = shortest[i];
                            String newWord = shortest[i + 1];
                            Map<String, Integer> innerMap1 = Graph.get(preWord);
                            int edgeWeight = innerMap1.get(newWord);
                            roadLength += edgeWeight;
                        }
                        builder.append("\n最短路径长度为：").append(roadLength);
                    } else {
                        builder.append("找不到从 ").append(start).append(" 到 ").append(end).append(" 的路径。\n");
                    }
                }
                for (String inWord : innerMap.keySet()) {
                    if (!visited.contains(inWord)) {
                        visited.add(inWord);
                        String end = inWord;
                        String shortestPath = calcShortestPath(start, end);
                        if (shortestPath != null) {
                            builder.append("找到了从").append(start).append(" 到 ").append(end).append(" 的路径。\n最短路径为：")
                                    .append(shortestPath);
                            String[] shortest = shortestPath.split(", ");
                            int roadLength = 0;
                            for (int i = 0; i < shortest.length - 1; i++) {
                                String preWord = shortest[i];
                                String newWord = shortest[i + 1];
                                Map<String, Integer> innerMap2 = Graph.get(preWord);
                                int edgeWeight = innerMap2.get(newWord);
                                roadLength += edgeWeight;
                            }
                            builder.append("\n最短路径长度为：").append(roadLength);
                        } else {
                            builder.append("找不到从 ").append(start).append(" 到 ").append(end).append(" 的路径。\n");
                        }
                    }
                }
            }
            result = builder.toString();
        } else {
            result = "输入格式错误，请输入正确的起点和终点。";
        }
        return result;
    }

}
