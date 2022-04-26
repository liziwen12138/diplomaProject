package com.blog.demo.util;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 复制对象或集合属性类
 */
@Component
public class BeanCopyUtils {

    /**
     * 拷贝对象
     * @param source
     * @param target
     * @param <T>
     * @return object
     */
    public static <T> T copyObject(Object source, Class<T> target){
        T temp = null;
        try {
            temp = target.newInstance();
            if( source != null){
//                本质还是调用spring
                BeanUtils.copyProperties(source,temp);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return temp;
    }

    /**
     * 拷贝集合
     * @param source
     * @param target
     * @param <T>
     * @param <S>
     * @return list集合
     */
    public static <T,S> List<T> copyList(List<S> source, Class<T> target){
        List<T> list = new ArrayList<>();
        if(source != null && source.size() > 0){
            for(Object object : source){
                list.add(BeanCopyUtils.copyObject(object,target));
            }
        }
        return list;
    }

}
