package edu.hitsz.application.score;

import edu.hitsz.application.Main;
import edu.hitsz.database.ScoreDao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ScoreTable {
    private JPanel MainPanel;
    private JPanel topPanel;
    private JPanel bottomPanel;
    private JLabel scoreLabel;
    private JTable scoreTable;
    private JScrollPane scoreScrollPane;
    private JButton deleteButton;
    private final ScoreDao scoreDao;

    public ScoreTable(String[] columnName, String[][] tableData, ScoreDao scoreDao) {
        this.scoreDao = scoreDao;


        DefaultTableModel model = new DefaultTableModel(tableData,columnName){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };


        scoreTable.setModel(model);
        scoreScrollPane.setViewportView(scoreTable);
        switch (Main.difficulty){
            case 1:
                this.scoreLabel.setText("排行榜(简单模式)");
                break;
            case 2:
                this.scoreLabel.setText("排行榜(一般模式)");
                break;
            case 3:
                this.scoreLabel.setText("排行榜(困难模式)");
                break;
            default:
                this.scoreLabel.setText("排行榜");
                break;
        }



        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (scoreTable.getSelectedRow() == -1) {
                    return;
                }

                int res = JOptionPane.showConfirmDialog(null, "删除记录", "确认要删除该项记录吗？",
                        JOptionPane.YES_NO_OPTION);
                // 确认删除
                if (res == JOptionPane.OK_OPTION) {
                    int row = scoreTable.getSelectedRow();
                    if (row != -1) {
                        int index = scoreDao.doDeleteScore(row+1,Main.score.getDifficulty());
                        model.removeRow(row);

                        //刷新排名序号
                        for(int i = row;i<=model.getRowCount()-1;i++){
                            model.setValueAt(i+1,i,0);
                        }
                    }
                }
            }
        });
    }

    public JPanel getMainPanel() {
        return MainPanel;
    }
}
