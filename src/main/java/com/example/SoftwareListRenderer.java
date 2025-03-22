package com.example;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SoftwareListRenderer extends DefaultListCellRenderer {
    private final Map<String, ImageIcon> iconCache = new HashMap<>();
    private final ExecutorService executor = Executors.newFixedThreadPool(3);
    private final ImageIcon placeholderIcon;

    public SoftwareListRenderer() {
        this.placeholderIcon = new ImageIcon(
                new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB)
        );
    }

    @Override
    public Component getListCellRendererComponent(
            JList<?> list, Object value,
            int index, boolean isSelected,
            boolean cellHasFocus
    ) {
        JLabel label = (JLabel) super.getListCellRendererComponent(
                list, value, index, isSelected, cellHasFocus
        );

        if (value instanceof Software software) {
            configureLabelForSoftware(label, software);
        }

        return label;
    }

    private void configureLabelForSoftware(JLabel label, Software software) {
        label.setText("<html><b>" + software.getName() + "</b><br>"
                + software.getSubCategory() + "</html>");
        label.setIcon(getCachedIcon(software.getIconUrl()));
        label.setVerticalTextPosition(SwingConstants.BOTTOM);
        label.setHorizontalTextPosition(SwingConstants.CENTER);
    }

    private ImageIcon getCachedIcon(String iconUrl) {
        return iconCache.computeIfAbsent(iconUrl, url -> {
            executor.submit(() -> loadIconAsync(url));
            return placeholderIcon;
        });
    }

    private void loadIconAsync(String iconUrl) {
        try {
            URL url = new URL(iconUrl);
            ImageIcon originalIcon = new ImageIcon(url);
            Image scaledImage = originalIcon.getImage()
                    .getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);

            SwingUtilities.invokeLater(() -> {
                iconCache.put(iconUrl, scaledIcon);
                repaintParentWindow();
            });
        } catch (Exception e) {
            System.err.println("アイコンの読み込みに失敗: " + e.getMessage());
        }
    }

    private void repaintParentWindow() {
        Window parent = SwingUtilities.windowForComponent(this);
        if (parent != null) {
            parent.repaint();
        }
    }
}