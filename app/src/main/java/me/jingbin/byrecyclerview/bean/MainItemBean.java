package me.jingbin.byrecyclerview.bean;

public class MainItemBean {

    private String title;
    private Class<?> cls;

    public MainItemBean(String title, Class<?> cls) {
        this.title = title;
        this.cls = cls;
    }

    public Class<?> getCls() {
        return cls;
    }

    public void setCls(Class<?> cls) {
        this.cls = cls;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
