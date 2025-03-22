package com.example;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import javax.swing.event.*;

public class SoftwareManagerApp extends JFrame {
    private List<Software> allSoftware = new ArrayList<>();
    private DefaultListModel<Software> listModel = new DefaultListModel<>();
    private JList<Software> softwareList;
    private JComboBox<String> versionCombo = new JComboBox<>();
    private JTextArea descriptionArea = new JTextArea();

    public SoftwareManagerApp() {
        initializeUI();
        loadData();
    }

    private void initializeUI() {
        setTitle("Server Manager Pro 1.0");
        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // 設定ボタン
        JButton settingsBtn = new JButton("⚙ 設定");
        settingsBtn.addActionListener(e -> new SettingsDialog(this).setVisible(true));

        // リスト表示
        softwareList = new JList<>(listModel);
        softwareList.setCellRenderer(new SoftwareListRenderer());

        // 詳細パネル
        JPanel detailPanel = createDetailPanel();

        // レイアウト
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(settingsBtn);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(softwareList), BorderLayout.WEST);
        add(detailPanel, BorderLayout.CENTER);
    }

    private JPanel createDetailPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // バージョン選択
        JPanel versionPanel = new JPanel();
        versionPanel.add(new JLabel("バージョン:"));
        versionPanel.add(versionCombo);

        // 説明文
        descriptionArea.setLineWrap(true);
        descriptionArea.setEditable(false);

        // ダウンロードボタン
        JButton downloadBtn = new JButton("ダウンロード");
        downloadBtn.addActionListener(e -> startDownload());

        panel.add(versionPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(descriptionArea), BorderLayout.CENTER);
        panel.add(downloadBtn, BorderLayout.SOUTH);

        return panel;
    }

    private void loadData() {
        try {
            allSoftware = SoftwareLoader.loadSoftwareList();
            updateList();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "データ読み込みエラー: " + e.getMessage(),
                    "エラー", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateList() {
        listModel.clear();
        allSoftware.forEach(listModel::addElement);
    }

    private void startDownload() {
        Software selected = softwareList.getSelectedValue();
        String version = (String) versionCombo.getSelectedItem();

        int choice = JOptionPane.showOptionDialog(
                this,
                "ダウンロード先: " + AppConfig.getDefaultDownloadPath(),
                "ダウンロード確認",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[]{"ダウンロード", "保存先変更", "キャンセル"},
                "ダウンロード"
        );

        if (choice == JOptionPane.YES_OPTION) {
            new Thread(() -> DownloadManager.download(selected, version)).start();
        } else if (choice == JOptionPane.NO_OPTION) {
            new SettingsDialog(this).setVisible(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SoftwareManagerApp().setVisible(true));
    }
}