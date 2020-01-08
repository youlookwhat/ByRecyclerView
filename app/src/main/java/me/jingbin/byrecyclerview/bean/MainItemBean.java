package me.jingbin.byrecyclerview.bean;

public class MainItemBean {

    private boolean isCategoryName;
    private String title;
    private String sort;
    private Class<?> cls;

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public boolean isCategoryName() {
        return isCategoryName;
    }

    public MainItemBean setCategoryName() {
        isCategoryName = true;
        return this;
    }

    public MainItemBean(String title, Class<?> cls) {
        this.title = title;
        this.cls = cls;
    }

    public MainItemBean(String title, Class<?> cls, String sort) {
        this.title = title;
        this.cls = cls;
        this.sort = sort;
    }

    public MainItemBean(String title) {
        this.title = title;
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
