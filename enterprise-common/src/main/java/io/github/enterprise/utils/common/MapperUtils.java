/**
 * 
 */
package io.github.enterprise.utils.common;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 封装 dozar 的映射工具
 * 
 * Created by Sheldon on 2017年12月12日
 *
 */
public class MapperUtils {

	@Autowired
	private Mapper mapper;
	
	   public <T> T map(Object source, Class<T> destinationClass) {
	        if (source == null) {
	            return null;
	        }
	        return mapper.map(source, destinationClass);
	    }

	    @SuppressWarnings({ "rawtypes", "unchecked" })
		public <T> List<T> map(List source, Class<T> clz) {
	        List target = new LinkedList();
	        for (Object o : source) {
	            Object to = mapper.map(o, clz);
	            target.add(to);
	        }
	        return target;
	    }

	    @SuppressWarnings({ "rawtypes", "unchecked" })
	    public <T, V> Map<T, V> map(Map map, Class<T> keyClz, Class<V> valueClz) {
	        Map<T, V> target = new HashMap<T, V>();
	        Set<Map.Entry> entrySet = map.entrySet();
	        for (Map.Entry entry : entrySet) {
	            T key = mapper.map(entry.getKey(), keyClz);
	            V value = mapper.map(entry.getValue(), valueClz);
	            target.put(key, value);
	        }
	        return target;
	    }
	    
}
