package ru.itis.project.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.itis.project.models.User;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Component
public class UserRepositoryJdbcTemplateImpl implements UserRepository{

    //language=SQL
    private final static String SQL_SELECT_ALL = "SELECT * FROM public.\"User\"";
    //language=SQL
    private final static String SQL_SELECT_BY_ID = "SELECT * FROM public.\"User\" WHERE id = ?";
    //language=SQL
    private final static String SQL_INSERT = "INSERT INTO public.\"User\" (email, password, enabled) VALUES (?,?,?)";
    //language=SQL
    private final static String SQL_DELETE = "DELETE FROM public.\"User\" WHERE id = ?";
    //language=SQL
    private final static String SQL_SELECT_BY_EMAIL_AND_PASSWORD = "SELECT * FROM public.\"User\" " +
            " WHERE email = ? AND password = ?";
    //language=SQL
    private final static String SQL_SELECT_BY_EMAIL = "SELECT * FROM public.\"User\" WHERE email = ? LIMIT 1";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public UserRepositoryJdbcTemplateImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<User> userRowMapper = (rs, rowNum) ->
            User.builder()
            .id(rs.getLong("id"))
            .email(rs.getString("email"))
            .password(rs.getString("password"))
            .enabled(rs.getBoolean("enabled"))
            .build();


    @Override
    public Optional<User> find(Long id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_SELECT_BY_ID, new Object[]{id}, userRowMapper));
    }

    @Override
    public List<User> findAll() {
        return  jdbcTemplate.query(SQL_SELECT_ALL, userRowMapper);
    }

    @Override
    public Long save(User entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement st = con.prepareStatement(SQL_INSERT,new String[]{"id"});
            st.setString(1, entity.getEmail());
            st.setString(2, entity.getPassword());
            st.setBoolean(3, entity.getEnabled());
            return st;
        }, keyHolder);
        entity.setId((Long)keyHolder.getKey());
        return entity.getId();
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(SQL_DELETE,id);
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(SQL_SELECT_BY_EMAIL_AND_PASSWORD,
                        new Object[]{email, password}, userRowMapper));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(SQL_SELECT_BY_EMAIL, new Object[]{email}, userRowMapper)
        );
    }
}
