# Elasticsearch + Kibana 本地使用说明

## 启动服务

在项目根目录执行：

```bash
docker compose up -d
```

- **Elasticsearch**：http://localhost:9200  
- **Kibana**：http://localhost:5601  

## 使用 Kibana

1. 浏览器打开 **http://localhost:5601**
2. 无需登录（当前 ES 未开启安全认证）
3. 常用入口：
   - **Dev Tools**（左侧菜单）：执行 ES API，例如查看索引 `GET _cat/indices`、查询 `repositories` 索引等
   - **Stack Management → Index Management**：查看/管理索引（如 `repositories`）
   - **Discover**：选择索引 `repositories` 后可浏览、筛选文档

## 停止服务

```bash
docker compose down
```

数据在 `es-data` 卷中，`down` 不会删除；若要清空数据可加 `-v`：`docker compose down -v`。
