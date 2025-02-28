package com.exercise.caching.repository;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.exercise.caching.exception.DBException;
import com.exercise.caching.model.CachedEntity;

import jakarta.annotation.PostConstruct;

@Repository
public class CachingRepository {

	private static final String CREATE_SQL = """
	    CREATE TABLE IF NOT EXISTS cache_info (
	        id VARCHAR(255) PRIMARY KEY,
	        data VARCHAR(255),
	        last_accessed TIMESTAMP
	    )
	""";
	private static final String SAVE_SQL = "MERGE INTO cached_entities (id, data, last_accessed) VALUES (?, ?, ?)";
	private static final String SELECT_SQL = "SELECT * FROM cached_entities WHERE id = ?";
	private static final String DELETE_SQL = "DELETE FROM cached_entities WHERE id = ?";
	private static final String DELETE_ALL_SQL = "DELETE FROM cached_entities";
	private static final String EXISTS_BY_ID_SQL = "SELECT id FROM cache_entities WHERE id = ?";
	private static final Logger logger = LoggerFactory.getLogger(CachingRepository.class);
    private final JdbcTemplate jdbcTemplate;

    public CachingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void createTable() {
        try {
            logger.info("Creating cache_info table if not exists");
            jdbcTemplate.execute(CREATE_SQL);
            logger.info("Table creation is successful");
        } catch (DataAccessException e) {
            logger.error("Error creating table: {}", e.getMessage(), e);
            throw new DBException("Failed to create table", e);
        }
    }

    public void save(CachedEntity entity) {
        try {
            logger.debug("Saving entity with ID: {}", entity.getId());
            
            jdbcTemplate.update(SAVE_SQL, 
                entity.getId(), 
                entity.getData(), 
                Timestamp.valueOf(entity.getCacheAccessedTime())
            );
            logger.debug("Entity saved successfully");
        } catch (DataAccessException e) {
            logger.error("Error saving entity: {}", e.getMessage(), e);
            throw new DBException("Failed to save entity", e);
        }
    }

    public Optional<CachedEntity> findById(String id) {
        try {
            logger.debug("Finding entity by ID: {}", id);
            List<CachedEntity> results = jdbcTemplate.query(SELECT_SQL,cacheEntityRowMapper,id);
            
            return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
        } catch (DataAccessException e) {
            logger.error("Error finding entity: {}", e.getMessage(), e);
            throw new DBException("Failed to find entity", e);
        }
    }

    public void deleteById(String id) {
        try {
            logger.debug("Deleting entity by ID: {}", id);
            
            jdbcTemplate.update(DELETE_SQL, id);
            logger.debug("Entity deleted successfully");
        } catch (DataAccessException e) {
            logger.error("Error deleting entity: {}", e.getMessage(), e);
            throw new DBException("Failed to delete entity", e);
        }
    }

    public void deleteAll() {
        try {
            logger.debug("Deleting all entities");
          
            jdbcTemplate.update(DELETE_ALL_SQL);
            logger.debug("All entities deleted successfully");
        } catch (DataAccessException e) {
            logger.error("Error deleting all entities: {}", e.getMessage(), e);
            throw new DBException("Failed to delete all entities", e);
        }
    }
    @Transactional(readOnly = true)
    public boolean existsById(String id) {
       
        try {
            logger.debug("Checking if entity exists with ID: {}", id);
            Integer count = jdbcTemplate.queryForObject(EXISTS_BY_ID_SQL, Integer.class, id);
            return count != null && count > 0;
        } catch (DataAccessException e) {
            logger.error("Error checking entity existence: {}", e.getMessage());
            throw new DBException("Failed to check entity existence", e);
        } 
    }
 // RowMapper for CacheEntity
    private static final RowMapper<CachedEntity> cacheEntityRowMapper = (rs, rowNum) ->{
    
    	try{
    		return CachedEntity.builder()
            .id(rs.getString("id"))
            .data(rs.getString("data"))
            .cacheAccessedTime(rs.getTimestamp("last_accessed").toLocalDateTime())
            .build();
    	}
    	catch (SQLException e) {
            logger.error("Error mapping row to entity: {}", e.getMessage());
            throw new DBException("Failed to map database row to entity", e);
        }
    
    };
}
