package org.apache.flink.streaming.api.functions.dynamicalcluate.func;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.dynamicalcluate.pojo.TagKafkaInfo;
import org.apache.flink.streaming.api.functions.dynamicalcluate.utils.BytTagUtil;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import org.apache.commons.beanutils.BeanUtils;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @title: VAR算子函数
 * @author: zhangyf
 * @date: 2023/7/5 14:04
 */
public class VarProcessFunc extends KeyedProcessFunction<String, TagKafkaInfo, TagKafkaInfo> {
    private OutputTag<TagKafkaInfo> dwdOutPutTag;
    private transient Queue<TagKafkaInfo> lastQueue;

    public VarProcessFunc(OutputTag<TagKafkaInfo> dwdOutPutTag) {
        this.dwdOutPutTag = dwdOutPutTag;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        lastQueue = new LinkedList<>();
    }

    @Override
    public void processElement(
            TagKafkaInfo value,
            KeyedProcessFunction<String, TagKafkaInfo, TagKafkaInfo>.Context ctx,
            Collector<TagKafkaInfo> out)
            throws Exception {
        Integer nBefore = value.getnBefore();
        lastQueue.offer(value);
        int size = lastQueue.size();
        if (size > nBefore) {
            TagKafkaInfo firstTag = lastQueue.poll();
            BigDecimal firstTagValue = firstTag.getValue();
            TagKafkaInfo newTag = new TagKafkaInfo();
            BeanUtils.copyProperties(newTag, value);
            try {
                BigDecimal diffValue = value.getValue().subtract(firstTagValue);
                newTag.setValue(diffValue);
            } catch (Exception e) {
                newTag.setValue(null);
                e.printStackTrace();
            }
            BytTagUtil.outputByKeyed(newTag, ctx, out, dwdOutPutTag);
        }
    }
}
