package com.example.springbatchstudy.custom;

import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomBeanWrapperFieldExtractor<T> implements FieldExtractor<T>, InitializingBean {

    private String[] names;

    /**
     * @param names field names to be extracted by the {@link #extract(Object)} method.
     */
    public void setNames(String[] names) {
        Assert.notNull(names, "Names must be non-null");
        this.names = Arrays.asList(names).toArray(new String[names.length]);
    }

    /**
     * @see org.springframework.batch.item.file.transform.FieldExtractor#extract(java.lang.Object)
     */
    @Override
    public Object[] extract(T item) {
        List<Object> values = new ArrayList<>();

        BeanWrapper bw = new BeanWrapperImpl(item);
        for (String propertyName : this.names) {
            values.add(bw.getPropertyValue(propertyName));
        }
        return values.toArray();
    }

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(names, "The 'names' property must be set.");
    }
}

