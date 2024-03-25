package cn.tockey.domain;

import lombok.Data;

@Data
public class GithubUser {
    // 用户的登录名
    private String login;
    // 用户的唯一标识符（ID）
    private long id;
    // 用户的节点ID，用于API操作
    private String node_id;
    // 用户的头像URL
    private String avatar_url;
    // Gravatar的用户ID，用于用户头像
    private String gravatar_id;
    // 用户的API URL
    private String url;
    // 用户的GitHub主页URL
    private String html_url;
    // 用户的关注者列表的API URL
    private String followers_url;
    // 用户正在关注的用户的API URL
    private String following_url;
    // 用户的Gists列表的API URL
    private String gists_url;
    // 用户收藏的仓库列表的API URL
    private String starred_url;
    // 用户的订阅列表的API URL
    private String subscriptions_url;
    // 用户所属组织的API URL
    private String organizations_url;
    // 用户的仓库列表的API URL
    private String repos_url;
    // 用户参与的事件列表的API URL
    private String events_url;
    // 接收到的事件列表的API URL
    private String received_events_url;
    // 此对象的类型，通常是"User"
    private String type;
    // 是否为GitHub网站的管理员
    private boolean site_admin;
    // 用户的真实姓名
    private String name;
    // 用户所在的公司
    private String company;
    // 用户的博客URL
    private String blog;
    // 用户的地理位置
    private String location;
    // 用户的电子邮件地址
    private String email;
    // 表示用户是否正在寻找工作
    private Object hireable;
    // 用户的个人简介
    private String bio;
    // 用户的Twitter用户名
    private String twitter_username;
    // 用户拥有的公共仓库数量
    private int public_repos;
    // 用户拥有的公共Gists数量
    private int public_gists;
    // 关注此用户的人数
    private int followers;
    // 用户关注的人数
    private int following;
    // 用户的账户创建时间
    private String created_at;
    // 用户信息的最后更新时间
    private String updated_at;
}