# 月海导航项目使用说明

## 项目简介
月海导航是一个基于 Spring Boot 4.0.1 + Thymeleaf + Bootstrap 5 + PostgreSQL + JPA 的现代化网址导航系统。

## 功能特性

### 1. 用户功能
- ✅ 用户注册和登录
- ✅ 未登录用户查看默认分类和网址
- ✅ 已登录用户管理个人分类和网址

### 2. 分类管理
- ✅ 创建、编辑、删除分类
- ✅ 拖拽排序分类
- ✅ 自定义分类图标（Bootstrap Icons）
- ✅ 侧边栏折叠功能

### 3. 网址管理
- ✅ 添加、编辑、删除网址
- ✅ 拖拽排序网址
- ✅ 网站 Logo 显示
- ✅ 网址描述

### 4. 搜索功能
- ✅ 站内搜索（实时下拉提示）
- ✅ 百度搜索
- ✅ 必应搜索
- ✅ 谷歌搜索

### 5. SEO 优化
- ✅ 语义化 HTML 结构
- ✅ Meta 标签优化
- ✅ robots.txt 配置
- ✅ sitemap.xml 生成

## 技术栈
- **后端**: Spring Boot 4.0.1, Spring Security, Spring Data JPA
- **前端**: Thymeleaf, Bootstrap 5.3.0, JavaScript ES6+
- **数据库**: PostgreSQL
- **构建工具**: Maven
- **其他**: Lombok, SortableJS

## 前置要求
1. JDK 25
2. PostgreSQL 数据库
3. Maven 3.6+

## 快速开始

### 1. 创建数据库
```sql
CREATE DATABASE moonsea_navigation;
```

### 2. 配置数据库连接
编辑 `src/main/resources/application.properties`，修改以下配置：
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/moonsea_navigation
spring.datasource.username=你的数据库用户名
spring.datasource.password=你的数据库密码
```

### 3. 运行项目
```bash
# 在项目根目录执行
mvn spring-boot:run
```

### 4. 访问应用
打开浏览器访问：http://localhost:8080

## 默认数据
项目首次启动时会自动初始化以下默认分类和网址：
- 常用工具（百度、Google、必应）
- 社交媒体（微信、微博、知乎）
- 开发工具（GitHub、Stack Overflow、MDN）
- 视频娱乐（B站、YouTube、爱奇艺）
- 电商购物（淘宝、京东、天猫）

## 主要页面
- **首页**: `/` - 展示所有分类和网址
- **登录**: `/login` - 用户登录
- **注册**: `/register` - 用户注册
- **关于**: `/about` - 关于页面
- **友链**: `/links` - 友情链接
- **Sitemap**: `/sitemap.xml` - SEO sitemap

## API 接口

### 分类管理
- `POST /api/categories` - 创建分类
- `PUT /api/categories/{id}` - 更新分类
- `DELETE /api/categories/{id}` - 删除分类
- `POST /api/categories/reorder` - 分类排序

### 网址管理
- `POST /api/websites` - 创建网址
- `PUT /api/websites/{id}` - 更新网址
- `DELETE /api/websites/{id}` - 删除网址
- `POST /api/websites/reorder` - 网址排序

### 搜索
- `GET /api/search?keyword=xxx` - 站内搜索

## 项目结构
```
moonsea-navigation/
├── src/
│   ├── main/
│   │   ├── java/cc/moonsea/navigation/
│   │   │   ├── config/          # 配置类
│   │   │   ├── controller/      # 控制器
│   │   │   ├── dto/             # 数据传输对象
│   │   │   ├── entity/          # 实体类
│   │   │   ├── repository/      # 数据访问层
│   │   │   ├── service/         # 业务逻辑层
│   │   │   └── MoonseaNavigationApplication.java
│   │   └── resources/
│   │       ├── static/
│   │       │   ├── css/         # 样式文件
│   │       │   └── js/          # JavaScript文件
│   │       ├── templates/       # Thymeleaf模板
│   │       └── application.properties
│   └── test/                    # 测试代码
└── pom.xml                      # Maven配置
```

## 使用技巧

### 1. 如何添加分类
1. 登录系统
2. 点击左侧边栏底部的"添加分类"按钮
3. 输入分类名称和图标（可选）
4. 点击保存

### 2. 如何添加网址
1. 登录系统
2. 在任意分类标题右侧点击"添加网址"按钮
3. 填写网站信息（名称、URL、描述、Logo）
4. 点击保存

### 3. 如何拖拽排序
- 登录后，直接拖动分类或网址卡片即可自动保存排序

### 4. 如何使用搜索
- **站内搜索**: 选择"站内搜索"，输入关键词，会显示匹配的网址下拉列表
- **外部搜索**: 选择搜索引擎（百度/必应/谷歌），输入关键词，点击搜索或按回车

### 5. 如何折叠侧边栏
- 点击侧边栏右上角的折叠按钮，可以折叠/展开侧边栏以节省空间

## 安全说明
- 密码使用 BCrypt 加密存储
- 已登录用户只能管理自己的分类和网址
- API 接口受 Spring Security 保护
- CSRF 保护已启用（API 接口除外）

## 浏览器兼容性
- Chrome 90+
- Firefox 88+
- Safari 14+
- Edge 90+

## 常见问题

### Q: 数据库连接失败？
A: 请检查 PostgreSQL 是否已启动，数据库是否已创建，用户名密码是否正确。

### Q: 页面样式显示异常？
A: 请检查网络连接，确保可以访问 Bootstrap CDN。

### Q: 拖拽功能不工作？
A: 请确保已登录，且浏览器支持 HTML5 拖放 API。

## 开发计划
- [ ] 导入/导出书签功能
- [ ] 多主题支持
- [ ] 移动端优化
- [ ] 分类分享功能
- [ ] 网址截图预览

## 许可证
MIT License

## 联系方式
如有问题，欢迎联系开发者。
