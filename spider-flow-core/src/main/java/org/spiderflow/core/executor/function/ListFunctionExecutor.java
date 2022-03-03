package org.spiderflow.core.executor.function;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import org.spiderflow.annotation.Comment;
import org.spiderflow.annotation.Example;
import org.spiderflow.executor.FunctionExecutor;
import org.springframework.stereotype.Component;

/**
 * List 工具类 防止NPE 添加了类似python的split()方法 
 * @author Administrator
 *
 */
@Component
@Comment("list常用方法")
public class ListFunctionExecutor implements FunctionExecutor{
	
	@Override
	public String getFunctionPrefix() {
		return "list";
	}

	@Comment("获取list的长度")
	@Example("${list.length(listVar)}")
	public static int length(List<?> list){
		return list != null ? list.size() : 0;
	}
	
	/**
	 * 
	 * @param list 原List
	 * @param len 按多长进行分割
	 * @return List<List<?>> 分割后的数组
	 */
	@Comment("分割List")
	@Example("${list.split(listVar,10)}")
	public static List<List<?>> split(List<?> list,int len){
		List<List<?>> result = new ArrayList<>();
		if (list == null || list.size() == 0 || len < 1) {
			return result;
		}
		int size = list.size();
		int count = (size + len - 1) / len;
		for (int i = 0; i < count; i++) {
			List<?> subList = list.subList(i * len, ((i + 1) * len > size ? size : len * (i + 1)));
			result.add(subList);
		}
		return result;
	}
	
	@Comment("截取List")
	@Example("${list.sublist(listVar,fromIndex,toIndex)}")
	public static List<?> sublist(List<?> list,int fromIndex,int toIndex){
		return list!= null ? list.subList(fromIndex, toIndex) : new ArrayList<>();
	}

	@Comment("过滤字符串list元素")
	@Example("${list.filterStr(list,pattern)}")
	public static List<String> filterStr(List<String> list, String pattern) {
		if (list == null || list.isEmpty()) {
			return null;
		}
		List<String> result = new ArrayList<>(list.size());
		for (String item : list) {
			if (!Pattern.matches(pattern, item)) {
				result.add(item);
			}
		}
		return result;
	}

	@Comment("List去重")
	@Example("${list.repeat(list)}")
	public static List<String> repeat(List<String> list) {
		if (list == null || list.isEmpty()) {
			return null;
		}
		Set<String> result = new HashSet<>(list);
		return new ArrayList<>(result);
	}

	@Comment("两个List合并")
	@Example("${list.pushAll(list,lists)}")
	public static List<String> pushAll(List<String> list,List<String> lists) {
		Set<String> result = new HashSet<>();
		result.addAll(list);
		result.addAll(lists);
		return new ArrayList<>(result);
	}

	@Comment("获取一个List<String>")
	@Example("${list.getList}")
	public static List<String> getList() {
		return new ArrayList<>();
	}

	@Comment("获取数组中最大的数字")
	@Example("${list.getMax(list)}")
	public static String getMax(List<String> list) {
		AtomicInteger max= new AtomicInteger();
		list.forEach(e-> {
			int num=Integer.parseInt(e);
			if (num> max.get()){
				max.set(num);
			}
		});
		return max.toString();
	}

}
