package com.dataart.test.dto;

/**
 * Created by andrey on 23/05/2014.
 */
public class GroupInfo {
    private Long id;
    private String name;
    private Integer count;

    public GroupInfo(Long id, String name, Integer count) {
        this.name = name;
        this.count = count;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    public Integer getCount() {
        return count;
    }


    @Override
    public String toString() {
        return "GroupInfo{" +
                "name='" + name + '\'' +
                ", count=" + count +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GroupInfo groupInfo = (GroupInfo) o;

        if (count != null ? !count.equals(groupInfo.count) : groupInfo.count != null) return false;
        if (name != null ? !name.equals(groupInfo.name) : groupInfo.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (count != null ? count.hashCode() : 0);
        return result;
    }
}
