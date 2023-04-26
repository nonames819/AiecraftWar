package edu.hitsz.application.music;

import java.util.LinkedList;
import java.util.List;

/**
 * @author hitsz
 */
public class MusicManager {
    private boolean active;
    //音乐线程列表
    private final List<MusicThread> musicThreadList = new LinkedList<>();
    //文件所在根目录
    private final String directory;

    public final static String BGM = "bgm.wav";
    public final static String BOSS = "bgm_boss.wav";
    public final static String BOMB = "bomb_explosion.wav";
    public final static String SHOOT = "bullet.wav";
    public final static String BULLET_HIT = "bullet_hit.wav";
    public final static String GAME_OVER = "game_over.wav";
    public final static String GET_SUPPLY = "get_supply.wav";


    public MusicManager(String directory) {
        this.directory = directory;
        this.active = false;
    }
    public MusicManager() {
        this("src/videos/");
    }

    public void activeMusic() {
        this.active = true;
    }

    public void runForever(String musicName) {
        if (active) {
            MusicThread musicThread = new MusicThread(directory + musicName, true);
            musicThread.start();
            this.musicThreadList.add(musicThread);
        }
    }

    public void runForOnce(String musicName) {
        if (active) {
            new MusicThread(directory + musicName, false).start();
        }
    }

    /**
     * 停止播放
     * @param musicName 音乐名称
     */
    public void stop(String musicName) {
        for (MusicThread musicThread : musicThreadList) {
            if (musicThread.getFilename().equals(directory + musicName)) {
                musicThread.interrupt();
                musicThreadList.remove(musicThread);
                break;
            }
        }
    }

    public void stopAll() {
        // 停止音乐
        while (musicThreadList.size() > 0) {
            for (MusicThread musicThread : musicThreadList) {
                musicThread.interrupt();
            }
            musicThreadList.removeIf(musicThread -> musicThread.needStop && musicThread.isInterrupted());
        }
    }
}
