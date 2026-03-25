# 📱 Redmi K60 安装指南

> 无需开发者模式，无需 Android Studio

---

## 方案：GitHub 在线编译（推荐）

GitHub 可以免费帮你编译 APK，你只需要注册账号并上传代码。

---

## 步骤一：注册 GitHub（2分钟）

1. 打开 https://github.com
2. 点击 **Sign up**
3. 输入邮箱、密码、用户名
4. 验证邮箱
5. 完成注册

---

## 步骤二：创建仓库并上传代码

### 方法 A：使用 GitHub 网页上传（最简单）

1. 登录 GitHub
2. 点击右上角 **+** → **New repository**
3. 填写：
   - Repository name: `CalendarAlarm`
   - Description: `日历闹钟 Android App`
   - 勾选 **Add a README file**
   - 点击 **Create repository**

4. 在仓库页面，点击 **Add file** → **Upload files**

5. 把我给你的项目文件打包上传：
   ```
   CalendarAlarm/
   ├── app/
   │   └── src/
   │       └── main/
   │           ├── java/com/example/calendaralarm/
   │           │   ├── MainActivity.java
   │           │   ├── AlarmActivity.java
   │           │   ├── AlarmItem.java
   │           │   └── AlarmAdapter.java
   │           ├── res/
   │           │   ├── layout/
   │           │   │   ├── activity_main.xml
   │           │   │   ├── activity_alarm.xml
   │           │   │   └── item_alarm.xml
   │           │   ├── drawable/
   │           │   │   ├── button_primary.xml
   │           │   │   ├── button_time.xml
   │           │   │   └── button_cancel.xml
   │           │   └── values/
   │           │       └── strings.xml
   │           └── AndroidManifest.xml
   ├── .github/
   │   └── workflows/
   │       └── build.yml
   ├── gradle/
   │   └── wrapper/
   │       └── gradle-wrapper.properties
   ├── build.gradle
   ├── settings.gradle
   └── gradlew
   ```

### 方法 B：使用 Git 命令（如果你会）

```bash
git init
git add .
git commit -m "Initial commit"
git branch -M main
git remote add origin https://github.com/你的用户名/CalendarAlarm.git
git push -u origin main
```

---

## 步骤三：触发编译

1. 在 GitHub 仓库页面，点击 **Actions** 标签
2. 你会看到 **Build APK** 工作流
3. 点击 **Run workflow** → **Run workflow**
4. 等待 2-3 分钟，编译完成

---

## 步骤四：下载 APK

1. 编译完成后，点击 Actions 里的最新运行记录
2. 在最下方找到 **Artifacts** 部分
3. 点击 **app-debug** 下载 APK 文件

或者：

1. 在仓库页面点击右侧 **Releases**
2. 下载最新的 APK 文件

---

## 步骤五：安装到手机

1. 把下载的 `app-debug.apk` 传到手机（微信/QQ/蓝牙）
2. 在手机上点击 APK 文件
3. 系统会提示「允许安装未知来源应用」
4. 点击 **设置** → 允许此来源
5. 返回继续安装
6. 完成！

---

## Redmi K60 安装设置

如果安装时提示「禁止安装」：

1. 打开 **设置**
2. 进入 **隐私保护** → **特殊权限设置**
3. 点击 **安装未知应用**
4. 找到你使用的应用（如微信、文件管理器）
5. 开启 **允许来自此来源的应用**

---

## 完整文件清单

我已经为你准备好了所有文件，在文件夹里：
```
/root/.openclaw/workspace/CalendarAlarm-Android/
```

你需要把这些文件打包成 ZIP，然后上传到 GitHub。

---

## 替代方案：找朋友帮忙

如果你不想用 GitHub，可以：
1. 把项目文件发给有 Android Studio 的朋友
2. 让他帮你编译 APK
3. 你直接安装

---

## 需要帮助？

告诉我卡在哪个步骤，我帮你解决！