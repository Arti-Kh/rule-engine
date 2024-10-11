package com.debricked.rule.engine.config;


import com.debricked.rule.engine.model.RuleAction;
import com.debricked.rule.engine.model.RuleType;
import com.debricked.rule.engine.model.Rules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Configuration class to load all rules mentioned in rules.csv file
 */
@Configuration
public class RuleConfig {
    private static final Logger logger = LoggerFactory.getLogger(RuleConfig.class);


    /**
     * Maps rules mentioned in rules.csv to Rules Record
     * @return List of rules provided in rules.csv
     */
    @Bean
    public List<Rules> rules() {
        List<Rules> rules = new ArrayList<>();

        String path = RuleConfig.class.getClassLoader().getResource("rules.csv").getPath();
        try (FileReader fileReader = new FileReader(path);
             BufferedReader reader = new BufferedReader(fileReader)) {
            String rule;
            while ((rule = reader.readLine()) != null) {
                String[] ruleString = rule.split(";");
                rules.add(new Rules(ruleString[0], RuleType.valueOf(ruleString[1]),Integer.parseInt(ruleString[2]), RuleAction.valueOf(ruleString[3])));
            }

            logger.info("Rules loaded - {}",rules.size());

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return rules;
    }

}
