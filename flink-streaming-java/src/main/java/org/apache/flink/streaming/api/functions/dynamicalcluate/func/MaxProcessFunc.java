package org.apache.flink.streaming.api.functions.dynamicalcluate.func;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.dynamicalcluate.pojo.TagKafkaInfo;
import org.apache.flink.streaming.api.functions.dynamicalcluate.utils.BytTagUtil;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

/**
 * @title: MAX算子函数
 * @author: zhangyf
 * @date: 2023/7/5 14:38
 */
public class MaxProcessFunc
        extends ProcessWindowFunction<TagKafkaInfo, TagKafkaInfo, String, TimeWindow> {
    private transient SimpleDateFormat sdf;
    private OutputTag<TagKafkaInfo> dwdOutPutTag;

    public MaxProcessFunc(OutputTag<TagKafkaInfo> dwdOutPutTag) {
        this.dwdOutPutTag = dwdOutPutTag;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    @Override
    public void process(
            String key,
            ProcessWindowFunction<TagKafkaInfo, TagKafkaInfo, String, TimeWindow>.Context context,
            Iterable<TagKafkaInfo> elements,
            Collector<TagKafkaInfo> out)
            throws Exception {
        Iterator<TagKafkaInfo> iterator = elements.iterator();
        ArrayList<TagKafkaInfo> arrayList = new ArrayList<>();
        while (iterator.hasNext()) {
            TagKafkaInfo next = iterator.next();
            arrayList.add(next);
        }
        arrayList.sort(
                new Comparator<TagKafkaInfo>() {
                    @Override
                    public int compare(TagKafkaInfo o1, TagKafkaInfo o2) {
                        return o1.getValue().intValue() - o2.getValue().intValue();
                    }
                });
        TagKafkaInfo tagKafkaInfo = arrayList.get(arrayList.size() - 1);
        tagKafkaInfo.setTime(sdf.format(context.window().getEnd()));
        BytTagUtil.outputByWindow(tagKafkaInfo, context, out, dwdOutPutTag);
        arrayList.clear();
    }
}
