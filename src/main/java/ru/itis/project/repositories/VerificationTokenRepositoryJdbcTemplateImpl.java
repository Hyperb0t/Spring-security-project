package ru.itis.project.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import ru.itis.project.models.VerificationToken;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class VerificationTokenRepositoryJdbcTemplateImpl implements VerificationTokenRepository {

    //language=SQL
    private static final String SQL_SELECT_ALL = "SELECT * FROM public.\"VerificationToken\" ";
    //language=SQL
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM public.\"VerificationToken\" WHERE id = ?";
    //language=SQL
    private static final String SQL_INSERT = "INSERT INTO public.\"VerificationToken\" (id,token, expiry_date) VALUES (?,?,?)";
    //language=SQL
    private static final String SQL_DELETE = "DELETE FROM public.\"VerificationToken\" WHERE id = ?";
    //language=SQL
    private static final String SQL_SELECT_BY_TOKEN = "SELECT * FROM public.\"VerificationToken\" WHERE token = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<VerificationToken> verificationTokenRowMapper = (rs, rowNum) ->
        VerificationToken.builder()
                .id(rs.getLong("id"))
                .token(rs.getString("token"))
                .expiryDate(rs.getDate("expiry_date").toLocalDate())
                .build();


    @Override
    public Optional<VerificationToken> find(Long id) {
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(SQL_SELECT_BY_ID, new Object[]{id}, verificationTokenRowMapper));
    }

    @Override
    public List<VerificationToken> findAll() {
        return jdbcTemplate.query(SQL_SELECT_ALL, verificationTokenRowMapper);
    }

    @Override
    public Long save(VerificationToken entity) {
        if(entity.getId() == null) {
            throw new IllegalStateException("TOKEn ID IS GOD DAMN NULL !!!11!!!1!!!");
        }
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement st = con.prepareStatement(SQL_INSERT, new String[]{"id"});
            st.setLong(1, entity.getId());
            st.setString(2, entity.getToken());
            st.setDate(3, Date.valueOf(entity.getExpiryDate()));
            return st;
        }, keyHolder);
        entity.setId((Long)keyHolder.getKey());
        return entity.getId();
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(SQL_DELETE, id);
    }

    @Override
    public Optional<VerificationToken> findByToken(String token) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_SELECT_BY_TOKEN, new Object[]{token},
                verificationTokenRowMapper));
    }

}
