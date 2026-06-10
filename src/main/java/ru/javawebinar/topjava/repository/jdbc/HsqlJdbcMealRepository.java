package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Profile("hsqldb")
@Repository
public class HsqlJdbcMealRepository extends AbstractJdbcMealRepository {

    private static final RowMapper<Meal> ROW_MAPPER = new RowMapper<Meal>() {
        @Override
        public Meal mapRow(ResultSet rs, int rowNum) throws SQLException {
            Meal meal = new Meal();
            meal.setId(rs.getInt("id"));
            meal.setDescription(rs.getString("description"));
            meal.setCalories(rs.getInt("calories"));
            Timestamp timestamp = rs.getTimestamp("date_time");
            if (timestamp != null) {
                meal.setDateTime(timestamp.toLocalDateTime());
            }
            return meal;
        }
    };

    public HsqlJdbcMealRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
    }

    @Override
    protected RowMapper<Meal> getRowMapper() {
        return ROW_MAPPER;
    }

    @Override
    protected Object convertDateTime(LocalDateTime dateTime) {
        return Timestamp.valueOf(dateTime);
    }
}