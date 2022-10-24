//
//Copyright (c) 2020 Cloudera, Inc. All rights reserved.
//

import org.apache.spark.sql.SparkSession

//---------------------------------------------------
//              CREATE SPARK SESSION
//---------------------------------------------------
val user = System.getProperty("user.name")
val appName = user + "-AvgLoan-Texas"
val spark = SparkSession.builder.appName(appName).getOrCreate()

// Update s3 details here
val input_path ="s3a://pse-workshop/cde-workshop"

val base_df=spark.read.option("header","true").option("inferSchema","true").csv(input_path + "/PPP-Sub-150k-TX.csv")

//------------------------------------------------------------------------------------------------------
//               SELECT RELEVANT COLUMNS AND GET THE AVG LOAN AMOUNT FOR A CITY, LENDER
//------------------------------------------------------------------------------------------------------

val filtered_df = base_df.select("LoanAmount", "City", "State", "Zip", "BusinessType", "NonProfit", "JobsRetained", "DateApproved", "Lender")

val final_df = filtered_df.groupBy("Lender", "City").agg(avg($"LoanAmount").as("Average_Loan_Amount"))

print("Average loan amount by a lender in Texas:- ")
print("........................")

final_df.show()
