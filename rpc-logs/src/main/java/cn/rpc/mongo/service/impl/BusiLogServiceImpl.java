package cn.rpc.mongo.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import cn.rpc.mongo.common.utils.Page;
import cn.rpc.mongo.dto.BusiLogDto;
import cn.rpc.mongo.entity.BusiLog;
import cn.rpc.mongo.repository.MongoRepositoryImpl;
import cn.rpc.mongo.service.BusiLogService;

/**
 * @author Vincent.wang
 *
 */
@Service
public class BusiLogServiceImpl extends MongoRepositoryImpl<BusiLog> implements BusiLogService {

    private static final Logger log = LoggerFactory.getLogger(BusiLogServiceImpl.class);

    @Override
    public Class<BusiLog> getEntityClass() {
        return BusiLog.class;
    }

    @Override
    public BusiLog findBusiLogById(String id) {
        log.warn("## find BusiLog By Id , id={}", id);
        return findOne(id);
    }

    @Override
    public List<BusiLog> findBusiLogAll() {
        return findAll();
    }

    @Override
    public Page<BusiLog> findBusiLogByPage(Page<BusiLog> page, BusiLogDto dto) {
        Query query = new Query();
        if (dto != null) {
            if (null != dto.getBeginTime() && null != dto.getEndTime())
                query.addCriteria(Criteria.where("create_time").gte(dto.getBeginTime()).lte(dto.getEndTime()));

            if (StringUtils.isNotBlank(dto.getSystemName()))
                query.addCriteria(Criteria.where("system_name").is(dto.getSystemName()));

            if (StringUtils.isNotBlank(dto.getLevel()))
                query.addCriteria(Criteria.where("level").is(dto.getLevel()));

            if (StringUtils.isNotBlank(dto.getThreadName()))
                query.addCriteria(Criteria.where("thread_name").regex(".*?" + dto.getThreadName() + ".*"));

            if (StringUtils.isNotBlank(dto.getLogName()))
                query.addCriteria(Criteria.where("log_name").regex(".*?" + dto.getLogName() + ".*"));

            if (StringUtils.isNotBlank(dto.getMessage()))
                query.addCriteria(Criteria.where("message").regex(".*?" + dto.getMessage() + ".*"));
        }
        return findPagination(page, query);
    }

}
