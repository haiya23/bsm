package com.haiya;

import com.haiya.mapper.WarnMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * ClassName: WarnTest
 * Package: com.haiya
 * Description:
 *
 * @ Author: haiYa
 * @ CreateTime: 2025/6/25 - 17:49
 * @ Version: 1.0
 */
@SpringBootTest
public class WarnTest {
    @Resource
    private WarnMapper warnMapper;
    @Test
    public void test1(){

            Map<String, Map<String, String>> rules = warnMapper.getRulesMap(null,"铁锂电池");
        System.out.println(rules);
    }

    @Test
    public void test2(){
        List<String> rulesByCarIdAndRuleId = warnMapper.getRulesByCarIdAndRuleId(null, 1);
        System.out.println(rulesByCarIdAndRuleId);

    }


}

