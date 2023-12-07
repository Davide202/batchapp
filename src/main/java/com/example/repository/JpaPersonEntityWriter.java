package com.example.repository;


import com.example.domain.PersonEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Repository;

import java.util.List;


@Transactional
@Repository
public class JpaPersonEntityWriter implements  ItemWriter<PersonEntity>
//        , InitializingBean
{
    private static final Logger logger = LoggerFactory.getLogger(JpaPersonEntityWriter.class);
    @PersistenceContext
    EntityManager entityManager;
    @Autowired
    PersonRepository personRepository;
    private boolean clearPersistenceContext = true;
    private boolean usePersist = true;

    @Transactional
//    @Override // FIGO MA NON FUNZIONA
    public void write1(Chunk<? extends PersonEntity> list) throws Exception {
        List<?> saved = personRepository.saveAll(list);
        logger.info(saved.toString());
    }

    @Transactional
    @Override
    public void write(Chunk<? extends PersonEntity> items) {
//        EntityManager entityManager = EntityManagerFactoryUtils.getTransactionalEntityManager(this.entityManagerFactory);
        if (entityManager == null) {
            throw new DataAccessResourceFailureException("Unable to obtain a transactional EntityManager");
        } else {
            entityManager.joinTransaction();
            this.doWrite(entityManager, items);
            entityManager.flush();
            if (this.clearPersistenceContext) {
                entityManager.clear();
            }
        }
    }


    protected void doWrite(EntityManager entityManager, Chunk<? extends PersonEntity> items) {
        logger.debug("Writing to JPA with " + items.size() + " items.");
        if (!items.isEmpty()) {
            long addedToContextCount = 0L;
            Chunk.ChunkIterator var5 = items.iterator();
            while (var5.hasNext()) {
                Object item = var5.next();
                if (!entityManager.contains(item)) {
                    if (this.usePersist) {
                        entityManager.persist(item);
                    } else {
                        entityManager.merge(item);
                    }
                    ++addedToContextCount;
                }
            }
            logger.debug("" + addedToContextCount + " entities " + (this.usePersist ? " persisted." : "merged."));
            logger.debug((long) items.size() - addedToContextCount + " entities found in persistence context.");
        }
    }

//    @Override
//    public void afterPropertiesSet()  {
//
//    }
}
