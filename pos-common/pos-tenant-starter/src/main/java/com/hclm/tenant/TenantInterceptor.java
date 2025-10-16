package com.hclm.tenant;

import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SetOperationList;
import net.sf.jsqlparser.statement.update.Update;
import org.hibernate.resource.jdbc.spi.StatementInspector;

@Slf4j
public class TenantInterceptor implements StatementInspector {
    @Override
    public String inspect(String sql) {
        String tenantId = TenantContext.getCurrentTenant();
        if (tenantId == null || tenantId.isEmpty()) {
            return sql;
        }
        Statement statement;
        try {
            statement = CCJSqlParserUtil.parse(sql);
        } catch (JSQLParserException e) {
            log.error("解析sql错误: {}", e.getMessage());
            return sql;
        }
        if (statement == null) {
            return sql;
        }
        if (statement instanceof Select select) {
            return handleSelect(select, tenantId);
        }
        if (statement instanceof Update update) {
            return handleUpdate(update, tenantId);
        }
        if (statement instanceof Delete delete) {
            return handleDelete(delete, tenantId);
        }
        return sql;
    }

    private String handleSelect(Select select, String tenantId) {

        if (select instanceof PlainSelect plainSelect) {
            plainSelect.setWhere(addTenantCondition(plainSelect.getWhere(), tenantId));
            return plainSelect.toString();
        }
        if (select instanceof SetOperationList setOperationList) {
            // 处理UNION等集合操作
            setOperationList.getSelects().forEach(selectBodyItem -> {
                if (selectBodyItem instanceof PlainSelect plainSelect) {
                    plainSelect.setWhere(addTenantCondition(plainSelect.getWhere(), tenantId));
                }
            });
        }

        return select.toString();
    }

    private String handleUpdate(Update update, String tenantId) {
        update.setWhere(addTenantCondition(update.getWhere(), tenantId));
        return update.toString();
    }

    private String handleDelete(Delete delete, String tenantId) {
        delete.setWhere(addTenantCondition(delete.getWhere(), tenantId));
        return delete.toString();
    }

    private Expression addTenantCondition(Expression existingWhere, String tenantId) {
        if (existingWhere == null) {
            return null;
        }
        EqualsTo tenantCondition = new EqualsTo();
        tenantCondition.setLeftExpression(new Column(TenantContext.getFieldName()));
        tenantCondition.setRightExpression(new StringValue(tenantId));

        return new AndExpression(existingWhere, tenantCondition);
    }

}
