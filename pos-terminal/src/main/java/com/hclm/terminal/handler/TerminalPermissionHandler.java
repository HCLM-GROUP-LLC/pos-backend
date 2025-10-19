package com.hclm.terminal.handler;

import com.baomidou.mybatisplus.extension.plugins.handler.MultiDataPermissionHandler;
import com.hclm.terminal.utils.EmployeesLoginUtil;
import com.hclm.terminal.utils.StoreDataPermContext;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import org.springframework.stereotype.Component;

@Component
public class TerminalPermissionHandler implements MultiDataPermissionHandler {
    @Override
    public Expression getSqlSegment(Table table, Expression where, String mappedStatementId) {
        var cache = EmployeesLoginUtil.loginCache();
        if (!StoreDataPermContext.isEnabled() || cache == null) {
            return null;
        }
        try {
            return CCJSqlParserUtil.parseCondExpression("store_id = " + cache.getStoreId());
        } catch (JSQLParserException e) {
            throw new RuntimeException(e);
        }
    }
}
