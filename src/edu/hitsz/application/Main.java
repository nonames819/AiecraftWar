package edu.hitsz.application;

import edu.hitsz.application.game.DifficultGame;
import edu.hitsz.application.game.EasyGame;
import edu.hitsz.application.game.Game;
import edu.hitsz.application.game.NormalGame;
import edu.hitsz.application.mode.ModeSelection;
import edu.hitsz.application.music.MusicManager;
import edu.hitsz.application.score.ScoreTable;
import edu.hitsz.database.Score;
import edu.hitsz.database.ScoreDaoImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 程序入口
 *
 * @author hitsz
 */
public class Main {

    public static final int WINDOW_WIDTH = 512;
    public static final int WINDOW_HEIGHT = 768;
    /**
     * 难度标志：1-简单，2-一般，3-困难
     */
    public static int difficulty = 0;

    /**
     * 游戏窗口
     */
    public static final JFrame FRAME = new JFrame("Aircraft War");

    /**
     * 游戏程序
     */
    public static Game game;

    /**
     * 音效是否打开
     */
    public static boolean musicActive = false;

    /**
     * 数据访问辅助对象
     */
    public static ScoreDaoImpl scoreDao = new ScoreDaoImpl("src\\table\\RankList.csv");

    /**
     * 数据对象实例
     */
    public static Score score = new Score(0, "TestPlayer", "null","TO_BE_SET");

    /**
     * 音乐控制器
     */
    public static MusicManager MUSIC_MANAGER = new MusicManager();

    /**
     * 表格列名
     */
    public static String[] columnName;

    /**
     * 表格数据
     */
    public static String[][] tableData;

    public static final Thread SELECT_THREAD = new Thread(() -> {
        ModeSelection modeSelection = new ModeSelection();
        FRAME.setContentPane(modeSelection.getMainPanel());
        FRAME.setVisible(true);
    });

    public static final Thread SCORE_THREAD = new Thread(new Runnable() {
        @Override
        public void run() {
            ScoreTable scoreTable = new ScoreTable(columnName, tableData, scoreDao);
            FRAME.setContentPane(scoreTable.getMainPanel());
            FRAME.setVisible(true);

            // 输入名称
            String player = "";
            player += JOptionPane.showInputDialog(null, "请输入玩家名称");
            // 更新排行榜
            if (player.length() > 0) {
                addScore(player);
                loadData();
                scoreTable = new ScoreTable(columnName, tableData, scoreDao);
                FRAME.setContentPane(scoreTable.getMainPanel());
                FRAME.setVisible(true);
            }
        }
    });

    /**
     * 文件操作，即从数据访问对象中获取所有分数信息
     */
    protected static void loadData() {
        // 输出分数信息
        java.util.List<Score> scoreList = scoreDao.getAllScores();
        List<String[]> scoreString = new LinkedList<>();
        int rank = 0;
        int flag = 0;
        for (Score s : scoreList) {
            if (s.getDifficulty().equals("Easy")) {
                flag = 1;
            } else if (s.getDifficulty().equals("Normal")) {
                flag = 2;
            } else if (s.getDifficulty().equals("Difficult")) {
                flag = 3;
            } else {
                flag = 0;
            }

            if (flag == Main.difficulty) {
                rank++;
                scoreString.add(new String[]{Integer.toString(rank), s.getPlayer(), Integer.toString(s.getScore()), s.getTime(), s.getDifficulty()});
            }
        }

        // 创建表格 GUI
        columnName = new String[]{"排名", "玩家", "得分", "时间"};
        tableData = scoreString.toArray(new String[0][]);
    }

    /**
     * 增加分数
     *
     * @param player 玩家名称
     */
    protected static void addScore(String player) {
        if (player.length() > 0) {
            score.setPlayer(player);
        }
        // 获取当前日期
        Date dateNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");
        // 设置分数信息
        score.setTime(ft.format(dateNow));
        switch (difficulty){
            case 1:
                score.setDifficulty("Easy");
                break;
            case 2:
                score.setDifficulty("Normal");
                break;
            case 3:
                score.setDifficulty("Difficult");
                break;
        }
        System.out.println(score.toString());
        scoreDao.doAddScore(score);
    }

    public static void main(String[] args) throws InterruptedException, IOException {

        System.out.println("Hello Aircraft War");

        // 获得屏幕的分辨率，初始化 FRAME
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        FRAME.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        FRAME.setResizable(false);
        //设置窗口的大小和位置,居中放置
        FRAME.setBounds(((int) screenSize.getWidth() - WINDOW_WIDTH) / 2, 0,
                WINDOW_WIDTH, WINDOW_HEIGHT);
        FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //模式选择界面
        SELECT_THREAD.start();
        while (difficulty == 0) {
            synchronized (SELECT_THREAD) {
                SELECT_THREAD.wait();
            }
        }

        // 游戏难度设置
        switch (Main.difficulty) {
            case 1:
                game = new EasyGame();
                break;
            case 2:
                game = new NormalGame();
                break;
            case 3:
                game = new DifficultGame();
                break;
            default:
                game = new EasyGame();
                break;
        }

        //设置音乐播放
        if (musicActive) {
            MUSIC_MANAGER.activeMusic();
        }

        //游戏启动
        FRAME.setContentPane(game);
        FRAME.setVisible(true);
        game.action();
        MUSIC_MANAGER.runForever(MusicManager.BGM);

        //游戏结束
        synchronized (game) {
            game.wait();
        }

        // 音乐结束
        new Thread(() -> {
            synchronized (MUSIC_MANAGER) {
                MUSIC_MANAGER.stopAll();
                MUSIC_MANAGER.notifyAll();
            }
        }).start();
        synchronized (MUSIC_MANAGER) {
            MUSIC_MANAGER.wait();
        }


        // 当窗口关闭时存储数据
        FRAME.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // 将分数信息存回文件
                scoreDao.writeToFile();
                System.out.println("Data is saved");
                super.windowClosing(e);
            }
        });

        //排行榜界面

        loadData();
        SCORE_THREAD.start();
    }
}
