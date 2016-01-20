package com.hm.cache;

/**
 * Created by ant_shake_tree on 15/11/30.
 */
public class Cons {
    //解析规则错误
    public static int PARSE_RULE_ERROR=300;
    public static int PROPERTIES_STRING_TYPE_NULL_ERROR=301;


    //model
    public static int PARSE_RULE_MODEL=2;
    public static int PROPERTIES_MAPPING_MODEL=3;


    //sql
    //==========mxc-======
//    public static final String CUS_DRUG_CLASS_MAPPING_SQL="SELECT a.customer_drug_name,a.customer_guid,a.drug_trade_id,d.drug_common_id,d.class_id FROM hmcdss.customer_drug as a join hmcdss.drug_trade as b on a.drug_trade_id= b.id join hmcdss.drug_class_mapping as d on d.drug_common_id=b.drug_common_id";
//    public final static String  BY_TAB_HQL= SQLUtils.getProperty("expression");
}
