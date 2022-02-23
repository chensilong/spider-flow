package org.spiderflow.core.executor.function;


import org.spiderflow.annotation.Comment;
import org.spiderflow.annotation.Example;
import org.spiderflow.executor.FunctionExecutor;
import org.spiderflow.proxypool.ProxyPoolManager;
import org.spiderflow.proxypool.model.Proxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProxyPoolFunctionExecutor implements FunctionExecutor {
	
	private static ProxyPoolManager proxyPoolManager;

	@Override
	public String getFunctionPrefix() {
		return "proxypool";
	}

	@Comment("随机获取一个http代理")
	@Example("${proxypool.http(anonymous)}")
	public static String http(boolean anonymous){
		return convertToString(proxyPoolManager.getHttpProxy(anonymous));
	}

	@Comment("随机获取一个高匿http代理")
	@Example("${proxypool.http()}")
	public static String http(){
		return http(true);
	}

	@Comment("随机获取一个https代理")
	@Example("${proxypool.https(anonymous)}")
	public static String https(boolean anonymous){
		return convertToString(proxyPoolManager.getHttpsProxy(anonymous));
	}

	@Comment("随机获取一个高匿https代理")
	@Example("${proxypool.https()}")
	public static String https(){
		return https(true);
	}

	@Comment("添加代理到代理池中")
	@Example("${proxypool.add(ip,port,type,anonymous)}")
	public static void add(String ip,Integer port,String type,boolean anonymous){
		Proxy proxy = new Proxy();
		proxy.setIp(ip);
		proxy.setPort(Integer.valueOf(port));
		proxy.setType(type);
		proxy.setAnonymous(anonymous);
		proxyPoolManager.add(proxy);
	}

	private static String convertToString(Proxy proxy){
		if(proxy == null){
			return null;
		}
		return String.format("%s:%s", proxy.getIp(),proxy.getPort());
	}
	@Autowired
	public void setProxyPoolManager(ProxyPoolManager proxyPoolManager) {
		ProxyPoolFunctionExecutor.proxyPoolManager = proxyPoolManager;
	}
}
