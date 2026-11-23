# AIRP 模块与原生开发资料对照

| AIRP 要做的部分 | 苹果参考 | Android 原生实现 |
|---|---|---|
| AIRP 内部主屏幕 | App icons、Layout、Widgets HIG | Compose Box/Row/Lazy grids、拖拽与状态管理 |
| 真正显示在手机桌面的组件 | Widgets HIG | Android App Widgets / Glance |
| Liquid Glass 导航栏 | Materials、Navigation bars、Toolbars | Compose 绘制、graphicsLayer、Blur/RenderEffect（能力检测与降级） |
| 软件页面 | Apple 各软件 HIG 模式 | Kotlin + Compose 页面与 Navigation Compose |
| 弹窗和底部面板 | Alerts、Sheets、Menus | Compose Dialog、ModalBottomSheet、DropdownMenu |
| 灵动岛风格区域 | Live Activities HIG | AIRP 内部自绘 Compose 动态区域；Android 通知/前台服务另按系统规则实现 |
| 状态栏、刘海屏 | iOS Layout | WindowInsets、Edge-to-edge、Display Cutout |
| 日夜间与手动主题 | Dark Mode、Color | Compose 自定义 Theme + DataStore 保存用户选择 |
| 动画 | Motion、WWDC | Compose Animation、AnimatedVisibility、Shared Element |
| 安装包（只自己安装，不上架） | — | Gradle、APK Analyzer |

## 必须遵守的实现边界
- Apple 的 SwiftUI / UIKit / WidgetKit 不能直接编译进 Android APK。
- AIRP 内部仿 iPhone 桌面与真实 Android Launcher 是两种不同功能。
- 需要真实桌面小组件时，使用 App Widgets / Glance；仅在 AIRP 页面内显示时，Compose 自绘即可。
- Liquid Glass 效果需要考虑 Android 版本、GPU 和设备厂商差异，提供模糊关闭或半透明替代方案。
- 每次开发应先完整检查项目结构和已有实现，再局部修改，避免覆盖无关代码。
