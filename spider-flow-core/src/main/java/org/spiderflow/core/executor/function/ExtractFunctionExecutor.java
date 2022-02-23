package org.spiderflow.core.executor.function;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.spiderflow.annotation.Comment;
import org.spiderflow.annotation.Example;
import org.spiderflow.core.utils.ExtractUtils;
import org.spiderflow.executor.FunctionExecutor;
import org.springframework.stereotype.Component;

@Component
@Comment("数据抽取常用方法")
public class ExtractFunctionExecutor implements FunctionExecutor {

    @Override
    public String getFunctionPrefix() {
        return "extract";
    }

    @Comment("根据jsonpath提取内容")
    @Example("${extract.jsonpath(resp.json,'$.code')}")
    public static Object jsonpath(Object root, String jsonpath) {
        return ExtractUtils.getValueByJsonPath(root, jsonpath);
    }

    @Comment("根据正则表达式提取内容")
    @Example("${extract.regx(resp.html,'<title>(.*?)</title>')}")
    public static String regx(String content, String pattern) {
        return ExtractUtils.getFirstMatcher(content, pattern, true);
    }

    @Comment("根据正则表达式提取内容")
    @Example("${extract.regx(resp.html,'<title>(.*?)</title>',1)}")
    public static String regx(String content, String pattern, int groupIndex) {
        return ExtractUtils.getFirstMatcher(content, pattern, groupIndex);
    }

    @Comment("根据正则表达式提取内容")
    @Example("${extract.regx(resp.html,'<a href=\"(.*?)\">(.*?)</a>',[1,2])}")
    public static List<String> regx(String content, String pattern, List<Integer> groups) {
        return ExtractUtils.getFirstMatcher(content, pattern, groups);
    }

    @Comment("根据正则表达式提取内容")
    @Example("${extract.regxs(resp.html,'<h2>(.*?)</h2>')}")
    public static List<String> regxs(String content, String pattern) {
        return ExtractUtils.getMatchers(content, pattern, true);
    }

    @Comment("根据正则表达式提取内容")
    @Example("${extract.regxs(resp.html,'<h2>(.*?)</h2>',1)}")
    public static List<String> regxs(String content, String pattern, int groupIndex) {
        return ExtractUtils.getMatchers(content, pattern, groupIndex);
    }

    @Comment("根据正则表达式提取内容")
    @Example("${extract.regxs(resp.html,'<a href=\"(.*?)\">(.*?)</a>',[1,2])}")
    public static List<List<String>> regxs(String content, String pattern, List<Integer> groups) {
        return ExtractUtils.getMatchers(content, pattern, groups);
    }

    @Comment("根据xpath提取内容")
    @Example("${extract.xpath(resp.element(),'//title/text()')}")
    public static String xpath(Element element, String xpath) {
        return ExtractUtils.getValueByXPath(element, xpath);
    }

    @Comment("根据xpath提取内容")
    @Example("${extract.xpath(resp.html,'//title/text()')}")
    public static String xpath(String content, String xpath) {
        return xpath(Jsoup.parse(content), xpath);
    }

    @Comment("根据xpaths提取内容")
    @Example("${extract.xpaths(resp.element(),'//h2/text()')}")
    public static List<String> xpaths(Element element, String xpath) {
        return ExtractUtils.getValuesByXPath(element, xpath);
    }

    @Comment("根据xpaths提取内容")
    @Example("${extract.xpaths(resp.html,'//h2/text()')}")
    public static List<String> xpaths(String content, String xpath) {
        return xpaths(Jsoup.parse(content), xpath);
    }

    @Comment("根据css选择器提取内容")
    @Example("${extract.selectors(resp.html,'div > a')}")
    public static List<String> selectors(Object object, String selector) {
        return ExtractUtils.getHTMLBySelector(getElement(object), selector);
    }

    @Comment("根据css选择器提取内容")
    @Example("${extract.selector(resp.html,'div > a','text')}")
    public static Object selector(Object object, String selector, String type) {
        if ("element".equals(type)) {
            return ExtractUtils.getFirstElement(getElement(object), selector);
        } else if ("text".equals(type)) {
            return ExtractUtils.getFirstTextBySelector(getElement(object), selector);
        } else if ("outerhtml".equals(type)) {
            return ExtractUtils.getFirstOuterHTMLBySelector(getElement(object), selector);
        }
        return null;
    }

    @Comment("根据css选择器提取内容")
    @Example("${extract.selector(resp.html,'div > a','attr','href')}")
    public static String selector(Object object, String selector, String type, String attrValue) {
        if ("attr".equals(type)) {
            return ExtractUtils.getFirstAttrBySelector(getElement(object), selector, attrValue);
        }
        return null;
    }

    @Comment("根据css选择器提取内容")
    @Example("${extract.selector(resp.html,'div > a')}")
    public static String selector(Object object, String selector) {
        return ExtractUtils.getFirstHTMLBySelector(getElement(object), selector);
    }

    @Comment("根据css选择器提取内容")
    @Example("${extract.selectors(resp.html,'div > a','element')}")
    public static Object selectors(Object object, String selector, String type) {
        if ("element".equals(type)) {
            return ExtractUtils.getElements(getElement(object), selector);
        } else if ("text".equals(type)) {
            return ExtractUtils.getTextBySelector(getElement(object), selector);
        } else if ("outerhtml".equals(type)) {
            return ExtractUtils.getOuterHTMLBySelector(getElement(object), selector);
        }
        return null;
    }

    @Comment("根据css选择器提取内容")
    @Example("${extract.selectors(resp.html,'div > a','attr','href')}")
    public static Object selectors(Object object, String selector, String type, String attrValue) {
        if ("attr".equals(type)) {
            return ExtractUtils.getAttrBySelector(getElement(object), selector, attrValue);
        }
        return null;
    }

    @Comment("过滤HTML标签")
    @Example("${extract.htmlFilter(html)}")
    public static String htmlFilter(String inputString) {
        if (inputString == null) {
            return null;
        }
        // 含html标签的字符串
        String htmlStr = inputString.trim();
        String textStr = "";
        java.util.regex.Pattern p_script;
        java.util.regex.Matcher m_script;
        java.util.regex.Pattern p_style;
        java.util.regex.Matcher m_style;
        java.util.regex.Pattern p_html;
        java.util.regex.Matcher m_html;
        java.util.regex.Pattern p_special;
        java.util.regex.Matcher m_special;
        try {
            //定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
            String regExScript = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
            //定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
            String regExStyle = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
            // 定义HTML标签的正则表达式
            String regExHtml = "<[^>]+>";
            // 定义一些特殊字符的正则表达式 如：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            String regEx_special = "\\&[a-zA-Z]{1,10};";

            p_script = Pattern.compile(regExScript, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); // 过滤script标签
            p_style = Pattern.compile(regExStyle, Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); // 过滤style标签
            p_html = Pattern.compile(regExHtml, Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); // 过滤html标签
            p_special = Pattern.compile(regEx_special, Pattern.CASE_INSENSITIVE);
            m_special = p_special.matcher(htmlStr);
            htmlStr = m_special.replaceAll(""); // 过滤特殊标签
            textStr = htmlStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return textStr;// 返回文本字符串
    }

    private static Element getElement(Object object) {
        if (object != null) {
            return object instanceof Element ? (Element) object : Jsoup.parse((String) object);
        }
        return null;
    }
}
