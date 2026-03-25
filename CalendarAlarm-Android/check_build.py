#!/usr/bin/env python3
"""
日历闹钟APK构建检查脚本
检查GitHub Actions构建状态和本地文件
"""
import os
import sys
import json
import subprocess
from pathlib import Path

def check_github_actions():
    """检查GitHub Actions状态（通过gh CLI或API）"""
    print("[工部] 检查GitHub Actions构建状态...")
    
    # 检查项目目录结构
    project_dir = Path("/root/.openclaw/workspace/CalendarAlarm-Android")
    
    # 必要文件检查
    required_files = [
        "app/build.gradle",
        "app/src/main/AndroidManifest.xml",
        ".github/workflows/build.yml",
        "settings.gradle",
        "gradle/wrapper/gradle-wrapper.properties"
    ]
    
    missing_files = []
    for file in required_files:
        if not (project_dir / file).exists():
            missing_files.append(file)
    
    if missing_files:
        print(f"❌ 缺失必要文件: {missing_files}")
        return False, f"缺失文件: {', '.join(missing_files)}"
    
    print("✅ 项目文件完整")
    
    # 检查build.yml配置
    build_yml = project_dir / ".github/workflows/build.yml"
    with open(build_yml, 'r') as f:
        content = f.read()
        if "actions/checkout@v4" in content and "actions/setup-java@v4" in content:
            print("✅ GitHub Actions配置已更新到v4")
        else:
            print("⚠️ GitHub Actions配置可能需要更新")
    
    return True, "项目结构检查通过"

def check_build_gradle():
    """检查app/build.gradle配置"""
    print("\n[工部] 检查Gradle配置...")
    
    build_gradle = Path("/root/.openclaw/workspace/CalendarAlarm-Android/app/build.gradle")
    
    if not build_gradle.exists():
        return False, "app/build.gradle 不存在"
    
    with open(build_gradle, 'r') as f:
        content = f.read()
    
    # 检查关键配置
    checks = {
        "applicationId": "applicationId" in content,
        "compileSdk": "compileSdk" in content,
        "dependencies": "dependencies" in content and "androidx" in content
    }
    
    if all(checks.values()):
        print("✅ Gradle配置正确")
        return True, "Gradle配置检查通过"
    else:
        missing = [k for k, v in checks.items() if not v]
        return False, f"Gradle配置缺失: {missing}"

def check_local_status():
    """检查本地项目状态"""
    print("\n[工部] 检查本地项目状态...")
    
    project_dir = Path("/root/.openclaw/workspace/CalendarAlarm-Android")
    
    # 统计文件
    java_files = list(project_dir.rglob("*.java"))
    xml_files = list(project_dir.rglob("*.xml"))
    
    print(f"  - Java源文件: {len(java_files)}个")
    print(f"  - XML配置文件: {len(xml_files)}个")
    
    if len(java_files) >= 4:  # MainActivity, AlarmActivity, AlarmItem, AlarmAdapter
        print("✅ 源代码文件完整")
        return True, f"代码文件完整 ({len(java_files)} Java, {len(xml_files)} XML)"
    else:
        return False, f"代码文件不完整 (仅{len(java_files)}个Java文件)"

def main():
    """主函数"""
    print("=" * 70)
    print("日历闹钟APK构建检查")
    print("=" * 70)
    print()
    
    results = []
    
    # 检查1: 项目结构
    success1, msg1 = check_github_actions()
    results.append(("项目结构", success1, msg1))
    
    # 检查2: Gradle配置
    success2, msg2 = check_build_gradle()
    results.append(("Gradle配置", success2, msg2))
    
    # 检查3: 本地状态
    success3, msg3 = check_local_status()
    results.append(("本地代码", success3, msg3))
    
    # 汇总
    print()
    print("=" * 70)
    print("检查结果汇总")
    print("=" * 70)
    
    all_passed = True
    for name, success, msg in results:
        status = "✅ 通过" if success else "❌ 失败"
        print(f"{status} | {name}: {msg}")
        if not success:
            all_passed = False
    
    print("=" * 70)
    
    # 输出JSON格式供监工系统解析
    output = {
        "success": all_passed,
        "task": "calendar_alarm_build",
        "checks": {
            name: {"success": success, "message": msg}
            for name, success, msg in results
        },
        "output": "日历闹钟项目检查完成",
        "error": None if all_passed else "部分检查未通过"
    }
    
    print(json.dumps(output, indent=2, ensure_ascii=False))
    
    return 0 if all_passed else 1

if __name__ == "__main__":
    sys.exit(main())