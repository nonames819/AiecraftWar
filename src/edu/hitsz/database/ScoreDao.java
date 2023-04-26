package edu.hitsz.database;

import java.util.List;

public interface ScoreDao {
    /**
     * 获得所有 Score 实例
     * @return Score 列表
     */
    List<Score> getAllScores();

    /**
     * 增加 Score 实例
     * @param score 新的 Score 实例
     */
    void doAddScore(Score score);

    /**
     * 根据排名删除对应 Score 实例
     * @param rank 排名数
     */
    int doDeleteScore(int rank,String difficulty);


    /**
     * 将分数信息写回文件
     */
    void writeToFile();
}
