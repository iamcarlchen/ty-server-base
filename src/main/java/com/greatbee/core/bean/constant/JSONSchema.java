package com.greatbee.core.bean.constant;

/**
 *
 * {
 * 		ds_fields:'',
 * 		ois:[{
 * 			oi_fields:'',
 * 			fields:[{
 * 				field_fields:''
 *          }],
 * 			connectors:[{	//fromOiAlias==oiAlias
 * 				connector_fields:''
 *           }]
 *       }]
 *    }
 *
 *     api_type,
 *     api,
 *     app,
 *     page
 *
 * @Author: haonan
 * @Description
 * @Date: 8:41 PM 2019/3/25
 */
public class JSONSchema {

    public static String Mokelay_DS_Alias = "db_ty";

    public static String JSON_Field_Alias = "alias";
    public static String JSON_Field_OIS = "ois";
    public static String JSON_Field_Fields = "fields";
    public static String JSON_Field_Connectors = "connectors";
    public static String JSON_Field_Connector = "connector";

    public static String JSON_Field_From_Oi_Alias = "fromOiAlias";
    public static String JSON_Field_Resource = "resource";
    public static String JSON_Field_Field_Name = "fieldName";

}