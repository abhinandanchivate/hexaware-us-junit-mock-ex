package com.hexaware.junittesting;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

public class CalculatorFacade {
	
	
	private final MathContext mc;
	private final Tokenizer tokenizer = new Tokenizer();
	private final ShuntingYard sy  = new ShuntingYard();
	
	private final RpnEvaluator rpn;
	private final CalculatorCore core;
	private final Memory memory = new Memory();
	private final OperationHistory history = new OperationHistory();
	private  BigDecimal current = BigDecimal.ZERO;
	
	public CalculatorFacade(MathContext mc) {
		// TODO Auto-generated constructor stub
		this.mc = mc;
		this.rpn = new RpnEvaluator(mc);
		this.core  = new CalculatorCore(mc);
		
	}
	
	public BigDecimal getCurrent(){ return current; }
    public Memory memory(){ return memory; }
    public OperationHistory history(){ return history; }
	
    public BigDecimal add(BigDecimal x){
        history.push(current);
        current = core.add(current, x);
        return current;
    }
    public BigDecimal sub(BigDecimal x){
        history.push(current);
        current = core.sub(current, x);
        return current;
    }
    public BigDecimal mul(BigDecimal x){
        history.push(current);
        current = core.mult(current, x);
        return current;
    }
    public BigDecimal div(BigDecimal x){
        history.push(current);
        current = core.div(current, x);
        return current;
    }
    public BigDecimal undo(){
        current = history.undo(current);
        return current;
    }
    public void clear(){ current = BigDecimal.ZERO; history.clear(); }

    // Expression evaluation (stateless)
    public BigDecimal eval(String expr){
        List<Token> toks = tokenizer.tokenize(expr);
        List<Token> r = sy.toRpn(toks);
        return rpn.eval(r);
    }

    // Convenience: evaluate then set current
    public BigDecimal evalAndSet(String expr){
        history.push(current);
        current = eval(expr);
        return current;
    }
}