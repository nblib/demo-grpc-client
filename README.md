# grpc-java 客户端
## 使用方法
根据proto文件生成java代码有两种办法:
1. 复制.proto文件到`src/proto`文件夹下,和java版服务端一样,导入依赖,compile,便会自动生成
2. 从服务端生成java代码,然后打包成jar包,引入