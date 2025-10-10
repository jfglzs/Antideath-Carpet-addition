## Antideath Carpet Addition
一个为Fabric服务器编写的 Carpet 拓展

_**感谢 @[_OptiJava_](https://github.com/OptiJava) 的指导**_

## 前置模组

| 名称          | 类型 | 链接                                                                                                                                                                       | 备注 |
|-------------|----|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----|
| Carpet      | 必须 | [MC百科](https://www.mcmod.cn/class/2361.html) &#124; [Modrinth](https://modrinth.com/mod/carpet) | -  |
| Fabric API  | 必须 | [MC百科](https://www.mcmod.cn/class/3124.html) &#124; [官方](https://fabricmc.net/)                                                                                          | - |

## 版本支持

| MC版本   | 当前开发状态 | 最后支持版本 |  
|--------|------|--------|
| 1.20.1 | 持续更新 | -      |
| 1.21.1 | 持续更新   | -      |
| 1.21.4 | 持续更新   | -      |
| 1.21.5 | 持续更新   | -      |
| 1.21.7 | 持续更新   | -      |
| 1.21.8 | 持续更新   | -      |

## ACA的所有规则

### 挖掘无减速(noMiningSlowDown)

当玩家处于流体/蜘蛛网/漂浮状态/水中时，挖掘方块无减速

- 类型：`布尔值`
- 默认值：`false`
- 参考选项：`true`，`false`
- 分类：`ACA`，`SURVIVAL`

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
   
### 快速获取Op(enableFastOpCommand)
用于没有MCDR的服务器，快速获取Op权限的指令
- 类型：`布尔值`
- 默认值：`false`
- 参考选项：`true`，`false`
- 分类：`ACA`，`COMMAND`

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

### 旁观者快捷TP命令(enableSpecTPCommand)
当玩家处于旁观者模式时，可使用此命令进行快速tp\
**格式 /sp 玩家**
- 类型：`布尔值`
- 默认值：`false`
- 参考选项：`true`，`false`
- 分类：`ACA`，`SURVIVAL`

### 末影人不会被玩家激怒(endermanNeverGetAngryByPlayer)
末影人不会被玩家激怒
- 类型：`布尔值`
- 默认值：`false`
- 参考选项：`true`，`false`
- 分类：`ACA`，`SURVIVAL`
