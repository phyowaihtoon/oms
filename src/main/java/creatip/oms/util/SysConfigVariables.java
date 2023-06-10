package creatip.oms.util;

public class SysConfigVariables {

    /*
     * Values for DEFINITION, VALUE and ENABLED might be overwritten on load of application
     * based on configuration of "sys_config" table
     */

    // Variables for Workflow Authority //
    public static final String WORKFLOW_AUTHORITY = "WORKFLOW_AUTHORITY";
    public static String WORKFLOW_DEFINITION = "If enabled,workflow will be applied";
    public static String WORKFLOW_VALUE = "";
    public static String WORKFLOW_ENABLED = "N";

    // Variables for PDF Preview //
    public static final String PDF_PREVIEW_LIMIT = "PDF_PREVIEW_LIMIT";
    public static String PDF_PREVIEW_DEFINITION = "If enabled,Number of Page Count defined in value field will be applied in PDF preview";
    public static String PDF_PREVIEW_VALUE = "";
    public static String PDF_PREVIEW_ENABLED = "N";
}
