# AIRP · Apple 视觉参考 + Android 原生 APK 开发资料包

整理日期：2026-11-23

## 这份资料包是做什么的
这份资料包专门按你的目标重新整理：**用 Android 原生技术制作 AIRP 小手机 APK，同时参考 Apple iPhone 的 Liquid Glass 视觉和交互设计。**

它不是网页开发资料，也不是 WebView 套壳教程。

## 两套资料的分工
- `01_Apple视觉参考`：只学习苹果的外观、布局、材质、图标、组件和交互规则。
- `02-10_Android原生开发`：真正用来写 Android APK 的技术文档，重点是 Kotlin、Jetpack Compose、Android SDK、App Widgets、WindowInsets、动画、权限、测试和 APK 签名。

## 推荐技术路线
- 语言：Kotlin
- 原生界面：Jetpack Compose
- 系统能力：Android SDK / Jetpack
- 桌面组件：App Widgets；Compose 风格可选 Glance
- 动画：Compose Animation
- 主题：AIRP 自己的主题系统，支持日间、夜间、跟随系统和手动切换
- 构建：Gradle / Android Gradle Plugin，输出签名 APK

## 真实桌面组件 vs AIRP 内部模拟桌面
- 如果组件要放在手机真实桌面（Launcher）上：使用 Android App Widgets / Glance。
- 如果组件只显示在 AIRP 自己的主屏幕页面内：使用 Jetpack Compose 自己绘制和管理，不需要 AppWidget。
- 如果 AIRP 想取代手机系统桌面成为默认 Launcher：还需要实现 Launcher 相关能力，并处理默认桌面授权、系统限制和兼容性。

## Liquid Glass 在 Android 上的实现提醒
Apple 的 SwiftUI `glassEffect()`、`GlassEffectContainer`、UIKit `UIGlassEffect` 都是 Apple 平台 API，**不能直接用于 Android**。
Android 端要用 Compose 绘制、图层、模糊、透明叠层、渐变高光、描边、阴影、动态背景采样和动画来重建相似视觉。不同 Android 版本和设备对背景模糊、渲染效果的支持并不完全相同，必须提供降级方案并实机测试。
不要简单给所有卡片套 blur；应把玻璃主要用于导航、工具栏、浮层和交互控件，普通内容区域保持清晰可读。

## 建议阅读顺序
1. 先看 `00_使用说明与开发路线`。
2. 看 `01_Apple视觉参考`，建立视觉规范。
3. 从 `02`、`03`、`04` 学会 Kotlin + Compose 和玻璃风格实现。
4. 根据 AIRP 功能读 `05` 到 `09`。
5. 最后按 `10` 做性能、测试和 APK 发布。

## 注意
本包是官方资料链接索引，不是 Apple/Google 官方文档全文镜像。官方页面和 SDK 会更新，打开链接时以 Apple Developer、Android Developers 当前页面为准。


## 本次核对的最新官方资料
- Apple Design Resources 当前页面列出 iOS 27 / iPadOS 27 UI Kit： https://developer.apple.com/design/resources/
- Apple Liquid Glass 官方技术概览： https://developer.apple.com/documentation/TechnologyOverviews/liquid-glass
- Android Jetpack Compose 2026 年 11 月稳定版更新（Compose 1.13）： https://developer.android.com/blog/posts/jetpack-compose-november-2026-release
- Compose Edge-to-edge 官方最新指南： https://developer.android.com/develop/ui/compose/system/setup-e2e
- Compose Blur 官方指南： https://developer.android.com/develop/ui/compose/graphics/blur/overview
- Android Material 3 in Compose 官方指南（页面更新于 2026-11-10）： https://developer.android.com/develop/ui/compose/designsystems/material3

提醒：Compose 版本号、Android Gradle Plugin、Kotlin 与各个 AndroidX 库的版本节奏不同。开始项目时应查看官方 BOM 和发行说明，不要把某一个版本号硬编码成永远最新。
