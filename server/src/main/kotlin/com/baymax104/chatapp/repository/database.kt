package com.baymax104.chatapp.repository

import com.alibaba.druid.pool.DruidDataSource
import org.ktorm.database.Database


val database = Database.connect(DruidDataSource().apply {
    url = "jdbc:mysql://localhost:3306/maxdb"
    driverClassName = "com.mysql.cj.jdbc.Driver"
    username = "max"
    password = "020411"
})