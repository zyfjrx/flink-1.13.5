package org.apache.flink.streaming.api.functions.dynamicalcluate.pojo;



import java.util.Objects;

/**
 * @title: mysql配置信息pojo类
 * @author: zhang
 * @date: 2022/6/23 13:33
 */

public class TagProperties {
    public Integer id;
    public Integer line_id;
    public String tag_name;
    public String byt_name;
    public String tag_topic;
    public String tag_type;
    public String calculate_type;
    public String tag_desc;
    public String value_min;
    public String value_max;
    public String task_name;
    public String sink_table;
    public String param;
    public Integer status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagProperties that = (TagProperties) o;
        return Objects.equals(id, that.id)
                && Objects.equals(line_id, that.line_id)
                && Objects.equals(tag_name, that.tag_name)
                && Objects.equals(byt_name, that.byt_name)
                && Objects.equals(tag_topic, that.tag_topic)
                && Objects.equals(tag_type, that.tag_type)
                && Objects.equals(calculate_type, that.calculate_type)
                && Objects.equals(tag_desc, that.tag_desc)
                && Objects.equals(value_min, that.value_min)
                && Objects.equals(value_max, that.value_max)
                && Objects.equals(task_name, that.task_name)
                && Objects.equals(param, that.param)
                && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id,
                line_id,
                tag_name,
                byt_name,
                tag_topic,
                tag_type,
                calculate_type,
                tag_desc,
                value_min,
                value_max,
                task_name,
                param,
                status);
    }

    public TagProperties(
            Integer id,
            Integer line_id,
            String tag_name,
            String byt_name,
            String tag_topic,
            String tag_type,
            String calculate_type,
            String tag_desc,
            String value_min,
            String value_max,
            String task_name,
            String sink_table,
            String param,
            Integer status) {
        this.id = id;
        this.line_id = line_id;
        this.tag_name = tag_name;
        this.byt_name = byt_name;
        this.tag_topic = tag_topic;
        this.tag_type = tag_type;
        this.calculate_type = calculate_type;
        this.tag_desc = tag_desc;
        this.value_min = value_min;
        this.value_max = value_max;
        this.task_name = task_name;
        this.sink_table = sink_table;
        this.param = param;
        this.status = status;
    }

    public TagProperties() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLine_id() {
        return line_id;
    }

    public void setLine_id(Integer line_id) {
        this.line_id = line_id;
    }

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    public String getByt_name() {
        return byt_name;
    }

    public void setByt_name(String byt_name) {
        this.byt_name = byt_name;
    }

    public String getTag_topic() {
        return tag_topic;
    }

    public void setTag_topic(String tag_topic) {
        this.tag_topic = tag_topic;
    }

    public String getTag_type() {
        return tag_type;
    }

    public void setTag_type(String tag_type) {
        this.tag_type = tag_type;
    }

    public String getCalculate_type() {
        return calculate_type;
    }

    public void setCalculate_type(String calculate_type) {
        this.calculate_type = calculate_type;
    }

    public String getTag_desc() {
        return tag_desc;
    }

    public void setTag_desc(String tag_desc) {
        this.tag_desc = tag_desc;
    }

    public String getValue_min() {
        return value_min;
    }

    public void setValue_min(String value_min) {
        this.value_min = value_min;
    }

    public String getValue_max() {
        return value_max;
    }

    public void setValue_max(String value_max) {
        this.value_max = value_max;
    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public String getSink_table() {
        return sink_table;
    }

    public void setSink_table(String sink_table) {
        this.sink_table = sink_table;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
