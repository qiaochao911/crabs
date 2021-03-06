package com.code.crabs.jdbc.lang.expression;

import com.code.crabs.common.util.ReadonlyList;
import com.code.crabs.exception.SQL4ESException;
import com.code.crabs.jdbc.lang.Expression;

public abstract class Aggregation extends Expression {

    protected Aggregation(final Expression... operandExpressions) throws SQL4ESException {
        if (operandExpressions == null) {
            throw new IllegalArgumentException("Argument[operandExpressions] is null.");
        }
        for (int i = 0; i < operandExpressions.length; i++) {
            if (operandExpressions[i] == null) {
                throw new IllegalArgumentException("Argument[operandExpressions[" + i + "]] is null.");
            }
            checkOperandExpression(operandExpressions[i]);
        }
        this.operandExpressions = operandExpressions.clone();
    }

    final Expression[] operandExpressions;

    public final Expression getOperandExpression(final int index) {
        return this.operandExpressions[index];
    }

    @Override
    protected final ReadonlyList<Expression> doGetOperandExpressionList() throws SQL4ESException {
        return ReadonlyList.newInstance(this.operandExpressions);
    }

    private static void checkOperandExpression(final Expression operandExpression) throws SQL4ESException {
        if (operandExpression instanceof Aggregation) {
            throw new IllegalArgumentException("Aggregation can not be operand expression of aggregation.");
        } else if (operandExpression instanceof NonAggregation) {
            final ReadonlyList<Expression> operandExpressionList = operandExpression.getOperandExpressionList();
            if (!operandExpressionList.isEmpty()) {
                for (int i = 0, operandExpressionCount = operandExpressionList.size(); i < operandExpressionCount; i++) {
                    checkOperandExpression(operandExpressionList.get(i));
                }
            }
        }
    }

}
