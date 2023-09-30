package com.example.springbatchstudy.batch.page;

import com.example.springbatchstudy.domain.Dept;
import com.example.springbatchstudy.domain.Dept2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class JpaPageJob2 {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    private int chunkSize = 10;

    @Bean
    public Job jpaPageJob2BatchBuild() {
        return jobBuilderFactory.get("jpaPageJob2")
                .start(jpaPageJob2Step1())
                .build();
    }

    @Bean
    public Step jpaPageJob2Step1(){
        return stepBuilderFactory.get("jpaPageJob2_step1")
                .<Dept, Dept2>chunk(chunkSize)
                .reader(jpaPageJob2DbItemReader())
                .processor(jpaPageJob2Processor())
                .writer(jpaPageJob2PrintItemWriter())
                .build();

    }

    private ItemProcessor<Dept, Dept2> jpaPageJob2Processor() {
        return dept -> {
            return new Dept2(dept.getId(), "NEW_" + dept.getDName(), "NEW_" + dept.getLoc());
        };
    }

    @Bean
    public JpaPagingItemReader<Dept> jpaPageJob2DbItemReader() {
        return new JpaPagingItemReaderBuilder<Dept>()
                .name("jpaPageJob2DbItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                .queryString("SELECT d FROM Dept d order by dept_no asc")
                .build();
    }

    @Bean
    public ItemWriter<Dept2> jpaPageJob2PrintItemWriter() {
        JpaItemWriter<Dept2> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return jpaItemWriter;

    }

}
