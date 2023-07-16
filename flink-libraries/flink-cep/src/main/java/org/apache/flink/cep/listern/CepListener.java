package org.apache.flink.cep.listern;

import org.apache.flink.cep.pattern.Pattern;

import java.io.Serializable;

/**
 * @title:
 * @author: zhangyf
 * @date: 2023/7/15 7:02
 **/
public interface CepListener<T> extends Serializable {

    /**
     * @Description: 留给用户判断当接受到元素的时候，是否需要更新CEP逻辑
     * @param: []
     * @return: java.lang.Boolean
     * @auther: zhangyf
     * @date: 2023/7/15 7:02
     */
    Boolean needChange(T element);
    /**
     * @Description: 当needChange为true时会被调用，留给用户实现返回一个新逻辑生成的pattern对象
     * @param: []
     * @return: org.apache.flink.cep.pattern.Pattern
     * @auther: zhangyf
     * @date: 2023/7/15 7:02
     */
    Pattern<T,?> returnPattern(T flagElement);
}
