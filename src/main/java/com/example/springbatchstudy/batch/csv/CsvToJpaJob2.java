package com.example.springbatchstudy.batch.csv;

import com.example.springbatchstudy.domain.Dept;
import com.example.springbatchstudy.dto.TwoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;

import javax.persistence.EntityManagerFactory;
import java.io.IOException;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CsvToJpaJob2 {

    private final ResourceLoader resourceLoader;

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    private int chunkSize = 10;

    @Bean
    public Job csvToJpaJob2BatchBuild() {
        return jobBuilderFactory.get("csvToJpaJob2")
                .start(csvToJpaJob2BatchStep1())
                .build();
    }

    @Bean
    public Step csvToJpaJob2BatchStep1() {
        return stepBuilderFactory.get("csvToJpaJob2BatchStep1")
                .<TwoDto, Dept>chunk(chunkSize)
                .reader(csvToJpaJob2FileReader())
                .processor(csvToJpaJob2Processor())
                .writer(csvToJpaJob2DbItemWriter())
                .build();
    }

    @Bean
    public MultiResourceItemReader<TwoDto> csvToJpaJob2FileReader() {
        MultiResourceItemReader<TwoDto> twoDtoMultiResourceItemReader = new MultiResourceItemReader<>();

        try {
            twoDtoMultiResourceItemReader.setResources(
                    ResourcePatternUtils.getResourcePatternResolver(this.resourceLoader).getResources(
                            "classpath:sample/csvToJpaJob2/*.txt"
                    )
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        twoDtoMultiResourceItemReader.setDelegate(multiFileItemReader());

        return twoDtoMultiResourceItemReader;
    }

    @Bean
    public FlatFileItemReader<TwoDto> multiFileItemReader() {
        FlatFileItemReader<TwoDto> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setLineMapper((line, lineNumber) -> {
            String[] lines = line.split("#");
            return new TwoDto(lines[0], lines[1]);
        });
        return flatFileItemReader;

    }

    @Bean
    public ItemProcessor<TwoDto, Dept> csvToJpaJob2Processor() {
        return twoDto -> new Dept(Long.parseLong(twoDto.getOne()), twoDto.getTwo(), "기타");

    }

    @Bean
    public JpaItemWriter<Dept> csvToJpaJob2DbItemWriter() {
        JpaItemWriter<Dept> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return jpaItemWriter;
    }

}
