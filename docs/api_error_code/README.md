# DBsSA 接口错误码文档 （DBsSA API Error Code Document）

---

- [公共错误码](#公共错误码)
- [业务错误码](#业务错误码)


## 公共错误码

| 错误码 | 错误信息                    | HTTP 状态码 | 报错原因及解决办法                               |
| ------ | --------------------------- | ----------- | ------------------------------------------------ |
| 00000  | Success                     | 200         | 请求成功                                         |
| 10001  | Invalid Parameters          | 400         | 请求参数无效，检查参数是否符合要求               |
| 10002  | Missing Parameters          | 400         | 缺少必要的参数，请检查参数是否完整               |
| 10003  | Unauthorized Access         | 401         | 未授权的访问，请检查访问令牌是否有效             |
| 10004  | Access Forbidden            | 403         | 访问被拒绝，请检查访问权限是否足够               |
| 10005  | Invalid User Credentials    | 401         | 无效的用户凭证，请检查用户名和密码是否正确       |
| 10006  | User Already Exists         | 409         | 用户已存在，请检查输入的用户名或联系系统管理员   |
| 10007  | User Not found              | 404         | 用户不存在，请检查输入的用户名或联系系统管理员   |
| 10008  | User Disabled               | 403         | 用户已被禁用，请联系系统管理员                   |
| 10009  | Password Mismatch           | 400         | 密码不匹配，请检查输入的密码是否一致             |
| 11001  | Email Already Exists        | 409         | 该邮箱已被使用，请联系系统管理员                 |
| 11002  | Email Not Found             | 404         | 该邮箱不存在，请检查输入的邮箱地址是否正确       |
| 11003  | Phone Number Already Exists | 409         | 该手机号已被使用，请联系系统管理员               |
| 11004  | Phone Number Not found      | 404         | 该手机号不存在，请检查输入的手机号码是否正确     |
| 12001  | Token Expired               | 401         | 令牌已过期，请重新获取访问令牌                   |
| 12002  | Token Invalid               | 401         | 令牌无效，请检查令牌是否正确                     |
| 12003  | Invalid Signature           | 401         | 签名无效，请检查签名是否正确                     |
| 12004  | Invalid Timestamp           | 401         | 时间戳无效，请检查时间戳是否正确                 |
| 12005  | Invalid Nonce               | 401         | 随机数无效，请检查随机数是否正确                 |
| 30001  | Resource Not Found          | 404         | 资源不存在，请检查请求的资源是否存在             |
| 40001  | System Error                | 500         | 系统发生错误，请稍后再试或联系系统管理员         |
| 50001  | Third Party Error           | 500         | 第三方服务出错，请稍后再试或联系第三方服务提供商 |
| 50002  | Business Error              | 400         | 业务逻辑错误，请检查参数或联系系统管理员         |
| 50003  | Database Error              | 500         | 数据库错误，请检查数据库连接或联系系统管理员     |
| 50004  | Network Error               | 500         | 网络连接错误，请检查网络连接或稍后再试           |
| 50005  | Method Not Allowed          | 405         | 不支持的请求方法，请检查请求方法是否正确         |
| 50006  | Service Unavailable         | 503         | 服务不可用，请稍后再试或联系系统管理员           |
| 50007  | Request Timeout             | 408         | 请求超时，请稍后再试或联系系统管理员             |

## 业务错误码

| 错误码 | 错误信息                 | HTTP 状态码 | 报错原因及解决办法                               |
| ------ | ------------------------ | ----------- | ------------------------------------------------ |
| 100001 | Apartment Already Exists | 409         | 公寓楼已存在，请检查输入的公寓名或联系企业管理员 |
