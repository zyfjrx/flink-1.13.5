package org.apache.flink.streaming.api.functions.dynamicalcluate.utils;

import org.apache.flink.streaming.api.functions.dynamicalcluate.pojo.TagKafkaInfo;

import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QlexpressUtil {
    public static String regEx = "\\" + FormulaTag.START + "(.*?)" + FormulaTag.END;

    public static Set<String> getTagSet(String expressContent) {
        Set<String> tagSet = new HashSet<>();
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(expressContent);
        while (matcher.find()) {
            tagSet.add(matcher.group(1));
        }
        return tagSet;
    }

    public static String bracketedStr(String s) {
        return FormulaTag.START + s + FormulaTag.END;
    }

    public static boolean evlExpress(HashMap<String, Object> tagMap, String expressContent) {
        ExpressRunner runner = new ExpressRunner();
        DefaultContext<String, Object> context = new DefaultContext<String, Object>();
        Set<String> tagSet = getTagSet(expressContent);
        for (String s : tagSet) {
            context.put("t" + s, tagMap.get(s));
            expressContent = expressContent.replace(bracketedStr(s), "t" + s);
        }
        try {
            Object r = runner.execute(expressContent, context, null, true, false);
            return r.equals(true);
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    public static Object computeExpress(Map<String, TagKafkaInfo> tagMap, String expressContent)
            throws Exception {
        ExpressRunner runner = new ExpressRunner();
        DefaultContext<String, Object> context = new DefaultContext<String, Object>();
        Set<String> tagSet = getTagSet(expressContent);
        for (String s : tagSet) {
            String sReplaced = s.replaceAll("\\.|/|__|\\(|\\)|\\||\\*|\\\\|-|\\$|#|:", "_");
            context.put("t" + sReplaced, tagMap.get(s).getValue());
            expressContent = expressContent.replace(bracketedStr(s), "t" + sReplaced);
        }
        // System.out.println(expressContent);
        return runner.execute(expressContent, context, null, true, false);
    }

    public static void main(String[] args) throws Exception {
        String express = "([SERVER_OSC::C4T/C4AT03.MV] > [SERVER_OSC::C6T/C6T01.MV])?1:0";
        // String express = "[CXL2_ZLJLL_50] + [CXL2_XS_yewei]";
        HashMap<String, TagKafkaInfo> tagMap = new HashMap<>();
        TagKafkaInfo a = new TagKafkaInfo();
        a.setValue(new BigDecimal(37).setScale(3, BigDecimal.ROUND_HALF_UP));
        TagKafkaInfo b = new TagKafkaInfo();
        b.setValue(new BigDecimal(3).setScale(3, BigDecimal.ROUND_HALF_UP));
        tagMap.put("SERVER_OSC::C4T/C4AT03.MV", a);
        tagMap.put("SERVER_OSC::C6T/C6T01.MV", b);
        Object r = computeExpress(tagMap, express);
        System.out.println(r);

        //        String express = "([SERVER_OSC::C4T/C4AT03.MV] > [SERVER_OSC::C6T/C6T01.MV])?1:0";
        //        //String express = "[CXL2_ZLJLL_50] + [CXL2_XS_yewei]";
        //        HashMap<String, TagKafkaInfo> tagMap = new HashMap<>();
        //        TagKafkaInfo a = new TagKafkaInfo();
        //        a.setValue(new BigDecimal(37).setScale(3, BigDecimal.ROUND_HALF_UP));
        //        TagKafkaInfo b = new TagKafkaInfo();
        //        b.setValue(new BigDecimal(3).setScale(3, BigDecimal.ROUND_HALF_UP));
        //        tagMap.put("SERVER_OSC::C4T/C4AT03.MV", a);
        //        tagMap.put("SERVER_OSC::C6T/C6T01.MV", b);
        //        Object r = computeExpress(tagMap, express);
        //        System.out.println(r);
        //
        //        String express = "{SERVER_OSC::C4T/C4AT03.MV} - {SERVER_OSC::C6T/C6T01.MV}";
        //        System.out.println(regEx);
        //        System.out.println(getTagSet(express));
        //        HashMap<String, TagKafkaInfo> tagMap = new HashMap<>();
        //        TagKafkaInfo a = new TagKafkaInfo();
        //        a.setValue(new BigDecimal(9).setScale(3, BigDecimal.ROUND_HALF_UP));
        //        TagKafkaInfo b = new TagKafkaInfo();
        //        b.setValue(new BigDecimal(7).setScale(3, BigDecimal.ROUND_HALF_UP));
        //        tagMap.put("SERVER_OSC::C4T/C4AT03.MV", a);
        //        tagMap.put("SERVER_OSC::C6T/C6T01.MV", b);
        //        Object r = computeExpress(tagMap, express);
        //        System.out.println(r);
        //        System.out.println("hello");
        //      String tagName = "[SERVER_OSC::C4T/C4AT03.MV] - [SERVER_OSC::C6T/C6T01.MV]";
        //
        //        Set<String> tagSet = QlexpressUtil.getTagSet(tagName);
        //        for (String s : tagSet) {
        //            System.out.println(s);
        //        }
        // TagKafkaInfo originTag = tagInfoMap.get(tagSet.toArray()[0]);
    }
}
