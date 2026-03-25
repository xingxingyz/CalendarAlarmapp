#!/bin/bash

# 编译脚本 - 需要在安装 Android Studio 后使用

echo "================================"
echo "日历闹钟 APK 编译脚本"
echo "================================"
echo ""

# 检查是否有 Android SDK
if [ -z "$ANDROID_HOME" ]; then
    echo "❌ 错误：未找到 ANDROID_HOME 环境变量"
    echo ""
    echo "请先安装 Android Studio 并配置 SDK"
    echo "1. 下载 Android Studio: https://developer.android.com/studio"
    echo "2. 安装后设置 ANDROID_HOME 环境变量"
    echo ""
    echo "或者在 Linux/Mac 上添加到 ~/.bashrc:"
    echo 'export ANDROID_HOME=$HOME/Android/Sdk'
    echo 'export PATH=$PATH:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools'
    exit 1
fi

echo "✓ Android SDK 路径: $ANDROID_HOME"
echo ""

# 清理并编译
echo "开始编译..."
./gradlew clean assembleDebug

if [ $? -eq 0 ]; then
    echo ""
    echo "================================"
    echo "✅ 编译成功！"
    echo "================================"
    echo ""
    echo "APK 文件位置："
    find . -name "*.apk" -path "*/build/outputs/*" | head -5
    echo ""
    echo "安装到手机："
    echo "1. 开启手机开发者选项和 USB 调试"
    echo "2. 连接手机到电脑"
    echo "3. 运行: adb install app/build/outputs/apk/debug/app-debug.apk"
else
    echo ""
    echo "❌ 编译失败，请检查错误信息"
fi