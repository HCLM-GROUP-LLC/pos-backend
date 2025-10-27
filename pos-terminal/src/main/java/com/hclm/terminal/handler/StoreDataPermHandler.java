package com.hclm.terminal.handler;

import com.baomidou.mybatisplus.extension.plugins.handler.MultiDataPermissionHandler;
import com.hclm.terminal.utils.StoreContext;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;

/**
 * 终端（门店）数据权限处理程序
 *
 * @author hanhua
 * @since 2025/10/27
 */
@Slf4j
public class StoreDataPermHandler implements MultiDataPermissionHandler {

    @Override
    public Expression getSqlSegment(Table table, Expression where, String mappedStatementId) {
        String storeId = StoreContext.getStoreId();
        if (storeId == null) { // 没有获取到门店ID
            return null;
        }
        try {
//            log.info("门店数据权限处理程序：{}", storeId);
            return CCJSqlParserUtil.parseCondExpression("store_id = '" + storeId + "'");
        } catch (JSQLParserException e) {
            throw new RuntimeException(e);
        }
    }
}
