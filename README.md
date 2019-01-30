# DEMO-of-Storm
一个Storm连接Kafka的DEMO程序
## 注意事项
### 1.项目打包时，出现Storm-core异常
  解决方法:Jar冲突，在打包时将此项Jar包删除即可。"storm-core"
### 2.slf4j包冲突
  解决方法:Jar冲突，在打包时将此项Jar包删除即可。
### 3.最后重新打包，发布即可。
