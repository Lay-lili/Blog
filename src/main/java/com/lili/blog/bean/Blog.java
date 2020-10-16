package com.lili.blog.bean;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//表明该类是一个实体类,当实体类与其映射的数据库表名不同名时需要使用
@Entity(name = "t_blog")
public class Blog {

    @Id  //标识主键
    @GeneratedValue  //自动生成id
    private Long id;
    private String title;

    @Basic(fetch = FetchType.LAZY)  //懒加载：使用时才会加载它的值
    @Lob  //在mysql中映射产生的字段的类型,String类的默认值为longtext,byte[]默认值为longblob
    private String content;
    private String description;  //博客描述
    private String firstPicture;  //博客顶部图片地址
    private String flag;
    private Integer views;  //浏览次数
    private boolean appreciation;  //是否打赏
    private boolean shareStatement;  //是否转载声明
    private boolean commentabled;  //是否评论
    private boolean published; //发布或保存
    private boolean recommend;  //是否推荐
    //指定java.util.Date或java.util.Calender属性与数据库类型date、time或timestamp中的那一种类型进行映射。
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    //指定java.util.Date或java.util.Calender属性与数据库类型date、time或timestamp中的那一种类型进行映射。
    @Temporal(TemporalType.TIMESTAMP)
    private  Date updateTime;

    @ManyToOne  //多对一映射
    private Type type;

    @ManyToMany(cascade = {CascadeType.PERSIST})  //多对多关系，存储博客时，无法自动存储级联的标签对象
    private List<Tag> tags = new ArrayList<>();

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "blog")
    private List<Comment> comments =new  ArrayList<>();

    @Transient  //表示该属性并非是一个要映射到数据库表中的字段,只是起辅助作用
    private Long typeId;

    @Transient
    private String tagIds;

    public Blog() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getTagIds() {
        return tagIds;
    }

    public void setTagIds(String tagIds) {
        this.tagIds = tagIds;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFirstPicture() {
        return firstPicture;
    }

    public void setFirstPicture(String firstPicture) {
        this.firstPicture = firstPicture;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public boolean isAppreciation() {
        return appreciation;
    }

    public void setAppreciation(boolean appreciation) {
        this.appreciation = appreciation;
    }

    public boolean isShareStatement() {
        return shareStatement;
    }

    public void setShareStatement(boolean shareStatement) {
        this.shareStatement = shareStatement;
    }

    public boolean isCommentabled() {
        return commentabled;
    }

    public void setCommentabled(boolean commentabled) {
        this.commentabled = commentabled;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public boolean isRecommend() {
        return recommend;
    }

    public void setRecommend(boolean recommend) {
        this.recommend = recommend;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", description='" + description + '\'' +
                ", firstPicture='" + firstPicture + '\'' +
                ", flag='" + flag + '\'' +
                ", views=" + views +
                ", appreciation=" + appreciation +
                ", shareStatement=" + shareStatement +
                ", commentabled=" + commentabled +
                ", published=" + published +
                ", recommend=" + recommend +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", type=" + type +
                ", tags=" + tags +
                ", user=" + user +
                ", comments=" + comments +
                ", typeId=" + typeId +
                ", tagIds='" + tagIds + '\'' +
                '}';
    }
}
