     1→# 月海导航项目使用说明
     2→
     3→## 项目简介
     4→月海导航是一个基于 Spring Boot 4 + Thymeleaf + daisyUI v5 + TailwindCSS + PostgreSQL/SQLite + JPA 的现代化网址导航系统。
     5→
     6→## 功能特性
     7→
     8→### 1. 用户功能
     9→- ✅ 用户注册和登录
    10→- ✅ 未登录用户查看默认分类和网址
    11→- ✅ 已登录用户管理个人分类和网址
    12→
    13→### 2. 分类管理
    14→- ✅ 创建、编辑、删除分类
    15→- ✅ 拖拽排序分类
    16→- ✅ 自定义分类图标（支持SVG文本或路径）
    17→- ✅ 侧边栏折叠功能
    18→
    19→### 3. 网址管理
    20→- ✅ 添加、编辑、删除网址
    21→- ✅ 拖拽排序网址
    22→- ✅ 网站 Logo 显示
    23→- ✅ 网址描述
    24→
    25→### 4. 搜索功能
    26→- ✅ 站内搜索（实时下拉提示）
    27→- ✅ 百度搜索
    28→- ✅ 必应搜索
    29→- ✅ 谷歌搜索
    30→
    31→### 5. 网站设置
    32→- ✅ 网站名称设置
    33→- ✅ 网站LOGO上传/设置
    34→- ✅ 网站头像上传/设置
    35→- ✅ 网站域名配置
    36→- ✅ 联系方式设置（邮箱、微信）
    37→- ✅ 关于页面内容编辑
    38→- ✅ SEO设置（页面描述、关键词）
    39→
    40→### 6. SEO 优化
    41→- ✅ 语义化 HTML 结构
    42→- ✅ Meta 标签优化
    43→- ✅ robots.txt 配置
    44→- ✅ sitemap.xml 生成
    45→
    46→## 技术栈
    47→- **后端**: Spring Boot 4.0.1, Spring Security, Spring Data JPA
    48→- **前端**: Thymeleaf, daisyUI v5, TailwindCSS, JavaScript ES6+
    49→- **数据库**: PostgreSQL / SQLite
    50→- **构建工具**: Maven
    51→- **其他**: Lombok, SortableJS
    52→
    53→## 前置要求
    54→1. JDK 25
    55→2. PostgreSQL 数据库 或 使用 SQLite（默认）
    56→3. Maven 3.6+
    57→
    58→## 快速开始
    59→
    60→### 1. 使用 SQLite（默认配置）
    61→项目默认使用 SQLite 数据库，无需额外配置，直接运行即可。
    62→
    63→### 2. 使用 PostgreSQL（可选）
    64→如果需要使用 PostgreSQL 数据库，编辑 `src/main/resources/application.properties`，修改以下配置：
    65→```properties
    66→spring.profiles.active=dev
    67→```
    68→然后编辑 `src/main/resources/application-dev.properties`，修改数据库连接配置：
    69→```properties
    70→spring.datasource.url=jdbc:postgresql://localhost:5432/moonsea_navigation
    71→spring.datasource.username=你的数据库用户名
    72→spring.datasource.password=你的数据库密码
    73→```
    74→
    75→### 3. 运行项目
    76→```bash
    77→# 在项目根目录执行
    78→mvn spring-boot:run
    79→```
    80→或使用项目提供的启动脚本：
    81→```bash
    82→start.bat
    83→```
    84→
    85→### 4. 访问应用
    86→打开浏览器访问：http://localhost:8080
    87→
    88→## 默认数据
    89→项目首次启动时会自动初始化以下默认分类和网址：
    90→- 常用工具（百度、Google、必应）
    91→- 社交媒体（微信、微博、知乎）
    92→- 开发工具（GitHub、Stack Overflow、MDN）
    93→- 视频娱乐（B站、YouTube、爱奇艺）
    94→- 电商购物（淘宝、京东、天猫）
    95→
    96→## 主要页面
    97→- **首页**: `/` - 展示所有分类和网址
    98→- **登录**: `/login` - 用户登录
    99→- **注册**: `/register` - 用户注册
   100→- **关于**: `/about` - 关于页面
   101→- **友链**: `/links` - 友情链接
   102→- **网站设置**: `/admin/setting` - 网站全局配置
   103→- **Sitemap**: `/sitemap.xml` - SEO sitemap
   104→
   105→## API 接口
   106→
   107→### 分类管理
   108→- `POST /api/categories` - 创建分类
   109→- `PUT /api/categories/{id}` - 更新分类
   110→- `DELETE /api/categories/{id}` - 删除分类
   111→- `POST /api/categories/reorder` - 分类排序
   112→
   113→### 网址管理
   114→- `POST /api/websites` - 创建网址
   115→- `PUT /api/websites/{id}` - 更新网址
   116→- `DELETE /api/websites/{id}` - 删除网址
   117→- `POST /api/websites/reorder` - 网址排序
   118→
   119→### 搜索
   120→- `GET /api/search?keyword=xxx` - 站内搜索
   121→
   122→### 网站设置
   123→- `GET /admin/api/setting` - 获取网站设置
   124→- `POST /admin/api/setting` - 保存网站设置
   125→- `POST /admin/api/upload` - 上传文件
   126→- `DELETE /admin/api/file?url=xxx` - 删除文件
   127→
   128→## 项目结构
   129→```
   130→moonsea-navigation/
   131→├── src/
   132→│   ├── main/
   133→│   │   ├── java/cc/moonsea/navigation/
   134→│   │   │   ├── config/          # 配置类
   135→│   │   │   ├── controller/      # 控制器
   136→│   │   │   ├── dto/             # 数据传输对象
   137→│   │   │   ├── entity/          # 实体类
   138→│   │   │   ├── repository/      # 数据访问层
   139→│   │   │   ├── service/         # 业务逻辑层
   140→│   │   │   └── MoonseaNavigationApplication.java
   141→│   │   └── resources/
   142→│   │       ├── static/
   143→│   │       │   ├── css/         # 样式文件
   144→│   │       │   ├── images/      # 图片资源
   145→│   │       │   ├── js/          # JavaScript文件
   146→│   │       │   └── uploads/     # 上传文件目录
   147→│   │       ├── templates/       # Thymeleaf模板
   148→│   │       │   ├── admin/       # 管理后台页面
   149→│   │       │   ├── fragments/   # 模板片段
   150→│   │       │   └── layout/      # 布局模板
   151→│   │       └── application.properties
   152→│   └── test/                    # 测试代码
   153→├── uploads/                     # 上传文件目录（运行时创建）
   154→└── pom.xml                      # Maven配置
   155→```
   156→
   157→## 使用技巧
   158→
   159→### 1. 如何添加分类
   160→1. 登录系统
   161→2. 点击左侧边栏底部的"添加分类"按钮
   162→3. 输入分类名称和图标（可选，支持SVG文本或路径）
   163→4. 点击保存
   164→
   165→### 2. 如何添加网址
   166→1. 登录系统
   167→2. 在任意分类标题右侧点击"添加网址"按钮
   168→3. 填写网站信息（名称、URL、描述、Logo）
   169→4. 点击保存
   170→
   171→### 3. 如何拖拽排序
   172→- 登录后，直接拖动分类或网址卡片即可自动保存排序
   173→
   174→### 4. 如何使用搜索
   175→- **站内搜索**: 选择"站内搜索"，输入关键词，会显示匹配的网址下拉列表
   176→- **外部搜索**: 选择搜索引擎（百度/必应/谷歌），输入关键词，点击搜索或按回车
   177→
   178→### 5. 如何配置网站设置
   179→1. 登录系统
   180→2. 访问 `/admin/setting` 页面
   181→3. 可配置网站名称、LOGO、头像、域名、联系方式、SEO信息等
   182→4. 点击保存按钮保存设置
   183→
   184→### 6. 如何上传文件
   185→- 在网站设置页面可以上传网站LOGO和头像
   186→- 文件会保存到 `uploads/` 目录
   187→- 支持 JPG、PNG、SVG 等图片格式
   188→
   189→### 7. 如何折叠侧边栏
   190→- 点击侧边栏右上角的折叠按钮，可以折叠/展开侧边栏以节省空间
   191→
   192→## 安全说明
   193→- 密码使用 BCrypt 加密存储
   194→- 已登录用户只能管理自己的分类和网址
   195→- API 接口受 Spring Security 保护
   196→- CSRF 保护已启用（API 接口除外）
   197→- 文件上传功能已进行安全验证，防止路径遍历攻击
   198→
   199→## 浏览器兼容性
   200→- Chrome 90+
   201→- Firefox 88+
   202→- Safari 14+
   203→- Edge 90+
   204→
   205→## 常见问题
   206→
   207→### Q: 数据库连接失败？
   208→A: 请检查 PostgreSQL 是否已启动，数据库是否已创建，用户名密码是否正确。如需使用默认SQLite，确保 [application.properties](file:///D:/Codes/IdeaProjects2026/moonsea-navigation/src/main/resources/application.properties) 中 `spring.profiles.active=sqllitedev`。
   209→
   210→### Q: 页面样式显示异常？
   211→A: 请检查网络连接，确保可以访问 daisyUI 和 TailwindCSS 资源。
   212→
   213→### Q: 拖拽功能不工作？
   214→A: 请确保已登录，且浏览器支持 HTML5 拖放 API。
   215→
   216→### Q: 文件上传失败？
   217→A: 请确保上传目录有写入权限，文件类型为图片格式（JPG、PNG、SVG）。
   218→
   219→## 开发计划
   220→- [ ] 导入/导出书签功能
   221→- [ ] 多主题支持
   222→- [ ] 移动端优化
   223→- [ ] 分类分享功能
   224→- [ ] 网址截图预览
   225→
   226→## 许可证
   227→MIT License
   228→
   229→## 联系方式
   230→如有问题，欢迎联系开发者。