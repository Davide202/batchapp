package com.example.repository;


import com.example.domain.PersonEntity;
import org.springframework.batch.item.ItemWriter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface PersonRepository extends JpaRepository<PersonEntity,Long>
//        , ItemWriter<PersonEntity>
{
}
