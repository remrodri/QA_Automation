package com.automation.eCommerce;


import static com.automation.core.UserConstants.EXCEL_TESTS_TO_RUN_FILE_NAME;

import com.automation.core.Excel;

public class Test
{
    private static Excel excel;

    public static void main(String[] args) throws Exception
    {
        // Create a new instance of Excel object with the name of the Excel file
        excel = new Excel(EXCEL_TESTS_TO_RUN_FILE_NAME);

        /*** IMPORTANT METHOD TO CALL TO START TESTS ***/
        // Read Excel file and run tests
        excel.run();
    }
}
