package com.example;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;

public class CategoryManager {
    private static final Map<String, CategoryStyle> CATEGORY_STYLES = Map.of(
            "プロキシサーバー", new CategoryStyle("⚡", new Color(33, 150, 243)),
            "MODサーバー", new CategoryStyle("🛠️", new Color(255, 152, 0)),
            "プラグインサーバー", new CategoryStyle("🧩", new Color(76, 175, 80)),
            "ハイブリッドサーバー", new CategoryStyle("🧬", new Color(156, 39, 176)),
            "その他", new CategoryStyle("❓", new Color(158, 158, 158))
    );

    public static List<String> getMainCategories() {
        return new ArrayList<>(CATEGORY_STYLES.keySet());
    }

    public static Icon getCategoryIcon(String category) {
        return new TextIcon(CATEGORY_STYLES.getOrDefault(category,
                new CategoryStyle("", Color.BLACK)));
    }

    public static Color getCategoryColor(String category) {
        return CATEGORY_STYLES.getOrDefault(category,
                new CategoryStyle("", Color.BLACK)).color();
    }

    private record CategoryStyle(String emoji, Color color) {}

    private static class TextIcon implements Icon {
        private final CategoryStyle style;

        TextIcon(CategoryStyle style) {
            this.style = style;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );
            g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
            g2.setColor(style.color());
            g2.drawString(style.emoji(), x, y + 24);
            g2.dispose();
        }

        @Override public int getIconWidth() { return 32; }
        @Override public int getIconHeight() { return 32; }
    }
}