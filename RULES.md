## ACA的所有规则
### 铁砧不会因掉落而损坏(anvilNeverDamageByFalling)

- 类型：`布尔值`
- 默认值：`false`
- 参考选项：`true`，`false`
- 分类：`ACA`，`SURVIVAL`

### 信标卡顿优化(beaconLagOptimization)
优化信标逻辑以减小卡顿
- 类型：`布尔值`
- 默认值：`false`
- 参考选项：`true`，`false`
- 分类：`ACA`，`OPTIMIZATION`

### 展示框永远附着(ItemFrameAlwaysStayAttach)
展示框会一直附着在方块上(包括空气)
- 类型：`布尔值`
- 默认值：`false`
- 参考选项：`true`，`false`
- 分类：`ACA`，`OPTIMIZATION`

### 掉落物永不消失(itemNeverDespawn)
掉落物永远都不会消失
- 类型：`布尔值`
- 默认值：`false`
- 参考选项：`true`，`false`
- 分类：`ACA`，`COMMAND`

### 掉落物立即消失(itemdispawnimmediately)
⚠️ 该功能会导致重要掉落物损失！！！！！⚠️
- 类型：`布尔值`
- 默认值：`false`
- 参考选项：`true`，`false`
- 分类：`ACA`，`COMMAND`

### 不死图腾扳手(flippinToTemOfUndying)
实现类似仙人掌扳手的效果（PCA移植功能）
- 类型：`布尔值`
- 默认值：`false`
- 参考选项：`true`，`false`
- 分类：`ACA`，`SURVIVAL`

### 实体搜索命令(enableEntitySearchCommand)
可用于搜索实体（支持目标选择器）
- 类型：`布尔值`
- 默认值：`false`
- 参考选项：`true`，`false`
- 分类：`ACA`，`SURVIVAL`

### 实体搜索命令启用小地图支持(entitySearchCommandEnableXaeroMapSupport)
实体搜索命令启用Xaero小地图支持
- 类型：`布尔值`
- 默认值：`false`
- 参考选项：`true`，`false`
- 分类：`ACA`，`SURVIVAL`

### 启用命令拦截器(enableCommandPreventer)
可以拦截玩家执行的指令\
**命令 /preventcmd**
- 类型：`布尔值`
- 默认值：`false`
- 参考选项：`true`，`false`
- 分类：`ACA`，`COMMAND`

### 启用命令拦截器白名单(enableCommandPreventerWhiteList)
**需要开启命令拦截器**\
**格式 /preventcmd whitelist (list,add,remove) 命令**\
**list 列出已添加的命令列表**\
**add xxx 添加命令**\
**add xxx 移除命令**

- 类型：`布尔值`
- 默认值：`false`
- 参考选项：`true`，`false`
- 分类：`ACA`，`COMMAND`

### 启用命令拦截器黑名单(enableCommandPreventerBlackList)
**需要开启命令拦截器**\
**格式 /preventcmd whitelist (list,add,remove) 命令**\
**list 列出已添加的命令列表**\
**add xxx 添加命令**\
**add xxx 移除命令**
- 类型：`布尔值`
- 默认值：`false`
- 参考选项：`true`，`false`
- 分类：`ACA`，`COMMAND`

### 启用命令拦截器前缀(enableCommandPreventerPrefix)
**需要开启命令拦截器**\
**格式 /preventcmd whitelist (list,add,remove) 命令**\
**list 列出已添加的命令列表**\
**add xxx 添加命令**\
**add xxx 移除命令**
- 类型：`布尔值`
- 默认值：`false`
- 参考选项：`true`，`false`
- 分类：`ACA`，`COMMAND`

### 末影人不会被玩家激怒(endermanNeverGetAngryByPlayer)
末影人不会被玩家激怒
- 类型：`布尔值`
- 默认值：`false`
- 参考选项：`true`，`false`
- 分类：`ACA`，`SURVIVAL`

### 自定义信标范围(beaconRange)
自定义信标效果范围
- 类型：`整数`
- 默认值：0，100, 200, 500, 1000
- 参考选项：`true`，`false`
- 分类：`ACA`，`SURVIVAL`

### 自定义物品拾取范围(itemPickUpRange)
自定义信标效果范围
- 类型：`整数`
- 默认值：0，100, 200, 500, 1000
- 参考选项：`true`，`false`
- 分类：`ACA`，`SURVIVAL`

### 伪和平优化(fakePeaceOptimization)
优化强加在伪和平时的卡顿
- 类型：`布尔值`
- 默认值：`false`
- 参考选项：`true`，`false`
- 分类：`ACA`，`OPTIMIZATION`

### 村民优化(villagerOptimization)
优化刷铁机中村民的卡顿
- 类型：`布尔值`
- 默认值：`false`
- 参考选项：`true`，`false`
- 分类：`ACA`，`OPTIMIZATION`

### 船吸优化(boatOptimization)
- 类型：`布尔值`
- 默认值：`false`
- 参考选项：`true`，`false`
- 分类：`ACA`，`OPTIMIZATION`

### 蜜蜂优化(BeeOptimization)
- 类型：`布尔值`
- 默认值：`false`
- 参考选项：`true`，`false`
- 分类：`ACA`，`OPTIMIZATION`

### 潜影贝优化 (实验性) (shulkerOptimization)
- 类型：`布尔值`
- 默认值：`false`
- 参考选项：`true`，`false`
- 分类：`ACA`，`OPTIMIZATION`

### MCDR前缀兼容(mcdrPrefixCompatible)
可以让带有玩家名称前缀/后缀的服务器 兼容MCDR
- 类型：`布尔值`
- 默认值：`false`
- 参考选项：`true`，`false`
- 分类：`ACA`，`SURVIVAL`
- 
### 旁观者拴绳不会掉落(neverDropLeashBySpectator)
修复1.21.10+ 旁观者无法使用拴绳的问题
- 类型：`布尔值`
- 默认值：`false`
- 参考选项：`true`，`false`
- 分类：`ACA`，`SURVIVAL`