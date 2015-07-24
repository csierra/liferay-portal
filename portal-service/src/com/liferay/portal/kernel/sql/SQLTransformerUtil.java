package com.liferay.portal.kernel.sql;

/**
 * @author Miguel Pastor
 */
public class SQLTransformerUtil {
	public SQLTransformerUtil(SQLTransformer sqlTransformer) {
		_sqlTransformer = sqlTransformer;
	}

	public static String transform(String sql) {
		return _sqlTransformer.transform(sql);
	}

	private static SQLTransformer _sqlTransformer;

}
