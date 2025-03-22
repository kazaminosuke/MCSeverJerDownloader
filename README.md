# MCSeverJerDownloader 🚀

[![GitHub Actions](https://github.com/yourusername/server-manager-pro/actions/workflows/maven-build.yml/badge.svg)](https://github.com/yourusername/server-manager-pro/actions)
[![Java Version](https://img.shields.io/badge/Java-17%2B-brightgreen)](https://adoptium.net/)

ゲームサーバーやプロキシサーバーを簡単に管理できるGUIアプリケーション  
（Minecraft関連サーバーに最適化）

![UIプレビュー](docs/screenshot.png) <!-- 実際のスクリーンショット画像を追加 -->

## 🌟 主な特徴

- **マルチカテゴリ対応**
  - プロキシ/MOD/プラグインサーバーを分類管理
  - カスタムカテゴリ追加可能
- **ワンクリックダウンロード**
  - GitHubから最新版を自動取得
  - ダウンロード進捗表示
- **スマートキャッシュ機能**
  - 24時間自動更新
  - オフラインでも利用可能
- **クロスプラットフォーム**
  - Windows/macOS/Linux 対応
  - 軽量設計（~5MB JARファイル）

## 🛠️ インストール方法

### 1. 実行可能JARで利用
```bash
# 最新リリースをダウンロード
curl -L https://github.com/yourusername/server-manager-pro/releases/latest/download/server-manager-pro.jar -o server-manager-pro.jar

# 実行
java -jar server-manager-pro.jar
