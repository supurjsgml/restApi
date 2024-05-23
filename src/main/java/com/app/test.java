package com.app;

import java.io.StringWriter;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;

import com.app.common.util.CommonUtil;
import com.app.common.util.PackageUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class test {
    
	public static void main(String[] args) {
	    test3();
	}
	
	public static void test3() {
//	    System.out.println(PackageUtil.randomSelection("com.example", "Test", "MyDto"));
	    VelocityEngine velocityEngine = new VelocityEngine();
	    Properties properties = new Properties();
        properties.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        velocityEngine.init(properties);

        
        VelocityContext context = new VelocityContext();
        context.put("packPath", "com.example");
        context.put("fileNm", "Test");
        context.put("dtoNm", "MyDto");
        context.put("serviceVarName", CommonUtil.decapitalize("Test") + "Service");
        context.put("dtoVarName", CommonUtil.decapitalize("MyDto"));

        // 템플릿 파일 로딩
        Template template = velocityEngine.getTemplate("templates/vm/controller.vm");
        StringWriter writer = new StringWriter();
        template.merge(context, writer);

        System.out.println(writer.toString());
	}
	
	public static void test2() {
	    double d = new Random().nextDouble();
	    
	    System.out.println(new Random().nextDouble());
	    System.out.println(new Random().nextDouble());
	    System.out.println(new Random().nextDouble());
	    System.out.println(new SecureRandom().nextDouble());
	    System.out.println(new SecureRandom().nextDouble());
	    System.out.println(new SecureRandom().nextDouble());
//	    System.out.println(d);
//	    System.out.println(d * 100.00);
    }

	public void test1() {
        ObjectMapper map = new ObjectMapper();
        
        List<Map<String, Object>> mocList = new ArrayList<>();
        
        Map<String,Object> mocMap = new HashMap<>();
        Map<String,Object> monthMap = new HashMap<>();
        
        String test = "{\"header\":{\"trNo\":\"61900f54-38c3-4de3-bff1-70b42e9b717c\",\"result\":true,\"code\":\"SUCCESS_SRCH\",\"message\":\"조회되었습니다\"},\"data\":[{\"cartNo\":\"15\",\"yrlLbrCntTotalSum\":1,\"lbrCntAgg\":1,\"mlyLbrCntList\":[{\"lbrYmDay\":\"01\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"02\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"03\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"04\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"05\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"06\",\"mlyLbrCnt\":1},{\"lbrYmDay\":\"07\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"08\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"09\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"10\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"11\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"12\",\"mlyLbrCnt\":0}]},{\"cartNo\":\"25\",\"yrlLbrCntTotalSum\":1,\"lbrCntAgg\":2,\"mlyLbrCntList\":[{\"lbrYmDay\":\"01\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"02\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"03\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"04\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"05\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"06\",\"mlyLbrCnt\":1},{\"lbrYmDay\":\"07\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"08\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"09\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"10\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"11\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"12\",\"mlyLbrCnt\":0}]},{\"cartNo\":\"35\",\"yrlLbrCntTotalSum\":1,\"lbrCntAgg\":1,\"mlyLbrCntList\":[{\"lbrYmDay\":\"01\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"02\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"03\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"04\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"05\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"06\",\"mlyLbrCnt\":1},{\"lbrYmDay\":\"07\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"08\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"09\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"10\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"11\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"12\",\"mlyLbrCnt\":0}]},{\"cartNo\":\"40\",\"yrlLbrCntTotalSum\":1,\"lbrCntAgg\":1,\"mlyLbrCntList\":[{\"lbrYmDay\":\"01\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"02\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"03\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"04\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"05\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"06\",\"mlyLbrCnt\":1},{\"lbrYmDay\":\"07\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"08\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"09\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"10\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"11\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"12\",\"mlyLbrCnt\":0}]},{\"cartNo\":\"46\",\"yrlLbrCntTotalSum\":1,\"lbrCntAgg\":1,\"mlyLbrCntList\":[{\"lbrYmDay\":\"01\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"02\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"03\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"04\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"05\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"06\",\"mlyLbrCnt\":1},{\"lbrYmDay\":\"07\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"08\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"09\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"10\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"11\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"12\",\"mlyLbrCnt\":0}]},{\"cartNo\":\"50\",\"yrlLbrCntTotalSum\":1,\"lbrCntAgg\":1,\"mlyLbrCntList\":[{\"lbrYmDay\":\"01\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"02\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"03\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"04\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"05\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"06\",\"mlyLbrCnt\":1},{\"lbrYmDay\":\"07\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"08\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"09\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"10\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"11\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"12\",\"mlyLbrCnt\":0}]},{\"cartNo\":\"60\",\"yrlLbrCntTotalSum\":1,\"lbrCntAgg\":7,\"mlyLbrCntList\":[{\"lbrYmDay\":\"01\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"02\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"03\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"04\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"05\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"06\",\"mlyLbrCnt\":1},{\"lbrYmDay\":\"07\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"08\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"09\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"10\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"11\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"12\",\"mlyLbrCnt\":0}]},{\"cartNo\":\"70\",\"yrlLbrCntTotalSum\":1,\"lbrCntAgg\":1,\"mlyLbrCntList\":[{\"lbrYmDay\":\"01\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"02\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"03\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"04\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"05\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"06\",\"mlyLbrCnt\":1},{\"lbrYmDay\":\"07\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"08\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"09\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"10\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"11\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"12\",\"mlyLbrCnt\":0}]},{\"cartNo\":\"80\",\"yrlLbrCntTotalSum\":1,\"lbrCntAgg\":2,\"mlyLbrCntList\":[{\"lbrYmDay\":\"01\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"02\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"03\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"04\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"05\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"06\",\"mlyLbrCnt\":1},{\"lbrYmDay\":\"07\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"08\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"09\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"10\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"11\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"12\",\"mlyLbrCnt\":0}]},{\"cartNo\":\"90\",\"yrlLbrCntTotalSum\":1,\"lbrCntAgg\":1,\"mlyLbrCntList\":[{\"lbrYmDay\":\"01\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"02\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"03\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"04\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"05\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"06\",\"mlyLbrCnt\":1},{\"lbrYmDay\":\"07\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"08\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"09\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"10\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"11\",\"mlyLbrCnt\":0},{\"lbrYmDay\":\"12\",\"mlyLbrCnt\":0}]}],\"page\":{\"pageNo\":1,\"size\":10,\"totalCount\":10,\"sort\":null}}";
        
    
//      mocList = map.convertValue(test, new TypeReference<List<Map<String,Object>>>(){});
        try {
            mocMap = map.readValue(test, new TypeReference<Map<String,Object>>(){});
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        mocList = (List<Map<String, Object>>) mocMap.get("data");
        
        mocList = mocList.stream().map(el -> {
            List<Map<String, Object>> mlyLbrCntList = (List<Map<String, Object>>) el.get("mlyLbrCntList");

            Comparator<String> comparator = String::compareTo;
            
//          Comparator<String> comparator = (o1, o2) -> o2.compareTo(o1);
            
            
            Map<String, Object> month = new TreeMap<>(mlyLbrCntList.stream().collect(Collectors.toMap(k -> k.get("lbrYmDay").toString(), v -> v.get("mlyLbrCnt"))));
            
            month.put("cartNo", el.get("cartNo"));
            
//          for (Map<String, Object> ml : mlyLbrCntList) {
//              resMap.put(String.valueOf(ml.get("lbrYmDay")), ml.get("mlyLbrCnt"));
//          }
            
            return month;
        }).collect(Collectors.toList());
        
        
        System.out.println(mocList);
        
        
        LocalDateTime date = LocalDateTime.parse("2024-04-01 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.KOREA));

        LocalDateTime date2 = LocalDateTime.now();
        
        log.info("date : {}", date);
        log.info("date2 : {}", date2);
        
        if (date.isAfter(date2)) {
            log.info("ttttttttttttt : {}", "ddddddddddddd");
        }
    }
}
