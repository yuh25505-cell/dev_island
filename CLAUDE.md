# CLAUDE.md · 岛屿机

## 每次开工读取
每次开始工作先读取本文件、README.md、DEVELOPMENT_PLAN.md，随后检查当前目录、相关源代码、Git 状态和最近提交。按计划一次只完成一个任务，除非用户明确指定任务。不要臆测文件状态。

## 产品
岛屿机是原生 AI-Native 角色生活模拟手机体验。角色有独立日常、朋友、兴趣、记忆和成长，不围绕用户不停触发。Android 原生 APK，不接受 WebView 套壳；iOS未来独立原生实现。包名 com.islandphone.app，当前版本 0.1.3。

## 技术选型
Kotlin、Jetpack Compose、Android SDK；Navigation Compose；Room 结构数据；DataStore 设置；文件系统媒体；Coroutines/Flow；OkHttp 网络/流；WorkManager 可延续任务。AI Provider 适配层与提示词流程分开。Cloudflare 后端。依赖版本开工时核对稳定版与兼容性，不能宣称未经构建验证。

## 代码职责
UI只显示状态并发出操作；ViewModel协调状态/用例；Repository负责数据来源；Room/文件/DataStore负责存取；Network负责协议/流解析/重试；AI Provider负责模型协议；Prompt/Memory负责上下文与记忆编排。按 feature 拆页面和业务。单文件只承担一个主要职责，避免巨型文件。共用能力集中维护，App 的玩法保留独立模块。不要为未来功能提前生成空模块。

## 提示词和记忆
统一读取用户资料、角色资料、世界书、记忆和近期聊天。每个新 App 开始前，先写明读取哪些内容、注入顺序/预算、哪些玩法数据隔离、结束后保存什么、哪些可同步。角色/会话/玩法数据必须隔离。新 App 复用流程，但独立组织UI、玩法、场景和回复格式。改共用流程时检查既有功能。

## 角色生活模拟
自主活动必须可设置频率、休息时段、全局或单角色暂停，并可查看活动记录。重要成长事件需可追溯、编辑/删除/撤销。角色关系、日程、兴趣与生活状态来自持久数据，而非仅靠一次提示词生成。

## 数据安全
新增用户内容同时支持编辑和删除。可开启功能必须有关闭、暂停或撤销。共享需要明确双方授权并可退出。API密钥不得提交Git、写入日志或导出明文。权限按需申请，拒绝后提供可用降级。账号与管理员授权在服务端校验；隐藏按钮不是安全机制。设备绑定用安装级密钥与后端验证，避免IMEI。长期登录采用可撤销令牌机制。虚拟支付/外卖玩法不接真实付款。

## UI
参考仓库中的 Apple 官方 Liquid Glass 资料；Android 用 Compose 实现相似视觉，玻璃集中用于导航、浮层、工具栏与交互控件，正文清晰。模糊效果可关闭，低性能设备降级半透明。所有页面/弹窗/功能支持系统跟随、固定日间、固定夜间。支持 Android 原生返回与边缘手势，不拦截系统手势。动效克制，性能优先。弹窗使用专属UI，不加额外小字注释说明。

## 主屏和启动
启动动画：雨滴落在中心、水波扩散，显示“请让这座孤岛因你们而拥有属于它的故事”，随后主屏。主屏图标/文件夹/内部组件/壁纸布局可编辑并持久化。模拟内部组件不等于真实 Android Widget。

## API
预设池统一管理，可编辑、删除、复制、测试、切换。聊天/总结/向量/重排/识图/语音/转写/生图分别配置，支持流式与非流式；官方与中转协议通过适配器支持。网络请求可取消，错误可读，流式中断不重复写入。

## GitHub Actions与签名
Actions 输出 APK Artifact。签名信息只存 GitHub Secrets，不提交 keystore/JKS。建议 Secrets：SIGNING_KEYSTORE_BASE64、SIGNING_KEYSTORE_PASSWORD、SIGNING_KEY_ALIAS、SIGNING_KEY_PASSWORD；配置已存在时核对实际名称再用。无签名配置时构建 Debug；Release签名需正确加载Secrets。applicationId 与证书不可随意变化，否则覆盖安装会失败。

## 每任务流程
1. 阅读规则、计划、项目和关联文件。
2. 确认当前任务与验收范围。
3. 只做该任务，保留无关代码。
4. 拆分职责，避免长文件。
5. 考虑编辑/删除、关闭/撤销、主题和返回。
6. 构建/测试并回归已有功能；不虚报通过。
7. 更新版本、计划与记录，说明修改文件数量。
8. 重复问题总结为短规则补充本文档。

## 构建规则
Compose 的 `Dp` 与动画 `Float` 混合运算时，使用 `Dp * Float` 顺序（如 `52.dp * progress`），避免 Float 乘 Dp 的类型错误。
修改 Gradle Kotlin DSL 文件时，检查整个文件的大括号成对闭合；不能只检查报错行。Android Java 与 Kotlin 编译目标需统一为 Java 17，修改后核对 compileOptions 与 kotlinOptions。每次改动构建配置后，以 GitHub Actions 的 assembleDebug 结果作为通过依据。

## 变更记录
- 0.1.3 修复 Splash 动画 Dp/Float 运算顺序导致的 Kotlin 编译失败。
- 0.1.2 修复 Java/Kotlin JVM 目标不一致，统一为 17。
- 0.1.1 修复根 build.gradle.kts 缺少闭合大括号，版本号升级。
- 0.1.0 初始工程文档与启动/主屏幕骨架。
