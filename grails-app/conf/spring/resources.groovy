import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

// Place your Spring DSL code here
beans = {
    jdbcTemplate(JdbcTemplate, ref('dataSource'))
}
