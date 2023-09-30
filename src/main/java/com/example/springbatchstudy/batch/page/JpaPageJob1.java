package com.example.springbatchstudy.batch.page;

import com.example.springbatchstudy.domain.Dept;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class JpaPageJob1 {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    private int chunkSize = 10;

    @Bean
    public Job jpaPageJob1BatchBuild() {
        return jobBuilderFactory.get("jpaPageJob1")
                .start(jpaPageJob1Step1())
                .build();
    }

    @Bean
    public Step jpaPageJob1Step1(){
        return stepBuilderFactory.get("jpaPageJob1_step1")
                .<Dept, Dept>chunk(chunkSize)
                .reader(jpaPageJob1DbItemReader())
                .writer(jpaPageJob1PrintItemWriter())
                .build();

    }

    @Bean
    public JpaPagingItemReader<Dept> jpaPageJob1DbItemReader() {
        return new JpaPagingItemReaderBuilder<Dept>()
                .name("jpaPageJob1DbItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                .queryString("SELECT d FROM Dept d order by dept_no asc")
                .build();
    }

    @Bean
    public ItemWriter<Dept> jpaPageJob1PrintItemWriter() {
        return list -> {
            for(Dept dept: list) {
                log.debug(dept.toString());
            }
        };
    }

}
