package by.bsu.fpmi.cfg.repository;

import by.bsu.fpmi.cfg.dto.Page;
import by.bsu.fpmi.cfg.dto.PageRequest;
import by.bsu.fpmi.cfg.entity.CFG;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CFGRepositoryImpl implements CFGRepository{

    @Autowired
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private final ObjectMapper objectMapper;

    public long insert(CFG cfg) {

        KeyHolder keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("content", cfg.getContent())
                .addValue("time_created", LocalDateTime.now());

        jdbcTemplate.update("""
                INSERT INTO cfg(time_created, content)
                    VALUES(:time_created, :content::jsonb)
                """, parameterSource, keyHolder);

        return (long) keyHolder.getKeys().get("id");
    }

    @Override
    public Optional<CFG> selectById(long id) {

        MapSqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", id);

        List<CFG> result = jdbcTemplate.query("""
                        SELECT id, time_created, content
                            FROM cfg
                            WHERE id = :id
                        """,
                parameterSource,
                (resultSet, rowNum) -> {
                    String content = resultSet.getString("content");
                    LocalDateTime timeCreated = resultSet.getObject("time_created", LocalDateTime.class);
                    return new CFG(id, content, timeCreated);
                });

        if (result.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(result.get(0));
    }


    @Transactional
    public Page<CFG> selectPage(PageRequest pageRequest) {

        long totalCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM cfg", new EmptySqlParameterSource(), Long.class);

        long pageNumber = pageRequest.getPageNumber();
        long perPageCount = pageRequest.getPerPageCount();

        if ((pageNumber - 1) * perPageCount >= totalCount) {
            return new Page<>(List.of(),
                    pageNumber,
                    totalCount,
                    perPageCount,
                    false,
                    false);
        }

        MapSqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("offset", (pageNumber - 1) * perPageCount)
                .addValue("limit", perPageCount);

        List<CFG> selected = jdbcTemplate.query("""
                        SELECT id, time_created, content
                            FROM cfg
                        ORDER BY id OFFSET :offset LIMIT :limit
                        """,
                parameterSource,
                (resultSet, rowNum) -> {

                    long id = resultSet.getLong("id");
                    String content = resultSet.getString("content");
                    LocalDateTime timeCreated = resultSet.getObject("time_created", LocalDateTime.class);
                    return new CFG(id, content, timeCreated);
                });

        boolean isFirst = pageNumber == 1;

        boolean isLast = false;
        long offsetFromEnd = totalCount - (pageNumber - 1) * perPageCount;
        if (offsetFromEnd <= perPageCount && offsetFromEnd > 0) {
            isLast = true;
        }

        return new Page<>(selected,
                pageNumber,
                totalCount,
                perPageCount,
                isFirst,
                isLast);
    }
}

