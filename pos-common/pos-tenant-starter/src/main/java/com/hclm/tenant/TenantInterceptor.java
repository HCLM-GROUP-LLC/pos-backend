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
    private String tenantId = "";
    private String tenantFieldName = "";

    @Override
    public String inspect(String sql) {
        tenantId = TenantContext.getCurrentTenant();
        tenantFieldName = TenantContext.getFieldName();
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
            return handleSelect(select);
        }
        if (statement instanceof Update update) {
            return handleUpdate(update);
        }
        if (statement instanceof Delete delete) {
            return handleDelete(delete);
        }
        return sql;
    }

    private String handleSelect(Select select) {
        if (select instanceof PlainSelect plainSelect) {
            plainSelect.setWhere(addTenantCondition(plainSelect.getWhere()));
            return plainSelect.toString();
        }
        if (select instanceof SetOperationList setOperationList) {
            // 处理UNION等集合操作
            setOperationList.getSelects().forEach(selectBodyItem -> {
                if (selectBodyItem instanceof PlainSelect plainSelect) {
                    plainSelect.setWhere(addTenantCondition(plainSelect.getWhere()));
                }
            });
        }

        return select.toString();
    }

//TODO 处理插入 目前无法 处理 因为 hibernate 只给了sql解析前的，不能动态修改参数
//    @SuppressWarnings("unchecked")
//    private String handleInsert(Insert insert) {
//        ExpressionList<Column> columns = insert.getColumns();
//        ExpressionList<Expression> values = (ExpressionList<Expression>) insert.getValues().getExpressions();
//        boolean hasTenantField = false;// 判断当前字段是否已经设置过租户字段
//        for (int i = 0; i < columns.size(); i++) {
//            Column column = columns.get(i);
//            if (tenantFieldName.equals(column.getColumnName())) {
//                values.set(i, new StringValue(tenantId));
//                hasTenantField = true;
//            }
//        }
//        // 如果不存在租户字段，则添加
//        if (!hasTenantField) {
//            columns.add(new Column(tenantFieldName));
//            values.add(new StringValue(tenantId));
//        }
//        return insert.toString();
//    }

    /**
     * 处理更新
     *
     * @param update 更新
     * @return {@link String }
     */
    private String handleUpdate(Update update) {
        update.setWhere(addTenantCondition(update.getWhere()));
        return update.toString();
    }

    /**
     * 处理删除
     *
     * @param delete 删除
     * @return {@link String }
     */
    private String handleDelete(Delete delete) {
        delete.setWhere(addTenantCondition(delete.getWhere()));
        return delete.toString();
    }

    /**
     * 添加租户条件
     *
     * @param existingWhere 现有地方
     * @return {@link Expression }
     */
    private Expression addTenantCondition(Expression existingWhere) {
        if (existingWhere == null) {
            return null;
        }
        EqualsTo tenantCondition = new EqualsTo();
        tenantCondition.setLeftExpression(new Column(tenantFieldName));
        tenantCondition.setRightExpression(new StringValue(tenantId));

        return new AndExpression(existingWhere, tenantCondition);
    }

}
