package com.cvesters.crowdchoice.config;

import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;

@JdbcRepository(dialect = Dialect.POSTGRES)
public @interface Repository {

}
