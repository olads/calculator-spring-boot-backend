package com.migia.basic.service;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.springframework.stereotype.Service;

@Service
public class EvaluateMathService {

    public double getResult(String exprr){
        System.out.println("Expression to be evaluated is " + exprr);
        Expression expr = new ExpressionBuilder(exprr).build();
        var result = expr.evaluate();
        System.out.println(result);
        return result;
    }
}
