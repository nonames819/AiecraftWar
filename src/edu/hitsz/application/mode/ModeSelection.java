package edu.hitsz.application.mode;

import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ModeSelection {
    private JPanel topPanel;
    private JPanel bottomPanel;
    private JLabel selectionLabel;
    private JButton easyButton;
    private JButton ordinaryButton;
    private JButton difficultButton;
    private JLabel musicLabel;
    private JCheckBox musicCheckBox;
    private JPanel MainPanel;
    private JButton clarificationButton;
    private JLabel topLabel;

    public ModeSelection() {

        easyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.musicActive = getMusicStatus();
                Main.difficulty = 1;
                synchronized (Main.SELECT_THREAD){
                    Main.SELECT_THREAD.notifyAll();
                }
            }
        });

        ordinaryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.musicActive = getMusicStatus();
                Main.difficulty = 2;
                synchronized (Main.SELECT_THREAD){
                    Main.SELECT_THREAD.notifyAll();
                }
            }
        });

        difficultButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.musicActive = getMusicStatus();
                Main.difficulty = 3;
                synchronized (Main.SELECT_THREAD){
                    Main.SELECT_THREAD.notifyAll();
                }
            }
        });

        clarificationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,"修生养性（简单模式）：只有两种飞机，且缓慢直行，放松心情的不二选择~\n" +
                        "小试身手（一般模式）：当分数到达300时会出现Boss敌机，试着去挑战它吧！\n" +
                        "挑战极限（困难模式）：当分数到达100时会出现Boss敌机，且随着Level升高它的各项属性会随之上升，准备好挑战自己吧！\n" +
                        "友情提示：碰撞子弹只扣减血量，但撞到敌机就会直接结束游戏哦，有时选择性地接子弹也是一种智慧~","难度说明",1);
            }
        });
    }

    /**
     * 根据checkbox确定音效是否打开
     * @return 音效开关状态
     */
    public boolean getMusicStatus(){
        return this.musicCheckBox.isSelected();
    }

    public JPanel getMainPanel() {
        return MainPanel;
    }
}
