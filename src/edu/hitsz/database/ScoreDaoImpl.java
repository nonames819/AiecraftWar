package edu.hitsz.database;

import java.io.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class ScoreDaoImpl implements ScoreDao{
    private final List<Score> scoreList;
    private final String tableName;

    public ScoreDaoImpl(String tableName) {
        this.scoreList = new LinkedList<>();
        this.tableName = tableName;
        readFromFile();
    }

    private void readFromFile() {
        try{
            FileReader fileReader = new FileReader(tableName);
            Scanner sc = new Scanner(fileReader);
            while(sc.hasNext()){
                String s = sc.nextLine();
                String[] items = s.split("\t");
                //分割文本信息，创建Score实例并存入列表
                //0-得分 1-玩家 2-时间 3-难度
                Score score = new Score(Integer.parseInt(items[0]),items[1],items[2],items[3]);
                this.scoreList.add(score);
            }
            fileReader.close();

        }catch  (FileNotFoundException e) {
            System.err.println("Warning: File " + tableName + " didn't exist.");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error: IOException when read file " + tableName);
            e.printStackTrace();
        }
    }

    @Override
    public List<Score> getAllScores() {
        Collections.sort(this.scoreList);
        return this.scoreList;
    }

    @Override
    public void doAddScore(Score score) {
        this.scoreList.add(score);
    }

    @Override
    public int doDeleteScore(int rank,String difficulty) {

        if(this.scoreList.size()>=rank){
            Collections.sort(this.scoreList);
            int index = -1;
            int number = 0;
            while(index < scoreList.size()){
                index++;
                if(scoreList.get(index).getDifficulty().equals(difficulty)){
                    number++;
                }
                if(number == rank){
                    this.scoreList.remove(index);
                    return index;
                }
            }


        }
        return -1;
    }

    /**
     * 将排行榜写回文件
     */
    @Override
    public void writeToFile() {
        FileWriter writer;
        try {
            writer = new FileWriter(tableName);
            for (Score score : scoreList) {
                writer.write(score.toString() + '\n');
            }
            writer.close();
        } catch (IOException e) {
            System.err.println("Error: File " + tableName + " can't find!");
            e.printStackTrace();
        }
    }
}
