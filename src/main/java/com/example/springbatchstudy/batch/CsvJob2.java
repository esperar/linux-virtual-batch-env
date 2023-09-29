package com.example.springbatchstudy.batch;

import com.example.springbatchstudy.custom.CustomBeanWrapperFieldExtractor;
import com.example.springbatchstudy.dto.TwoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class CsvJob2 {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final static int chunkSize = 5;

    @Bean
    public Job csvJob2BatchBuild() {
        return jobBuilderFactory.get("csvJob2")
                .start(csvJob2BatchStep())
                .build();
    }

    @Bean
    public Step csvJob2BatchStep() {
        return stepBuilderFactory.get("csvJob2BatchStep")
                .<TwoDto, TwoDto>chunk(chunkSize)
                .reader(csvJob2FileReader())
                .writer(csvJob2FileWriter(new FileSystemResource("output/csvJob2_output.csv")))
                .build();
    }

    @Bean
    public FlatFileItemReader<TwoDto> csvJob2FileReader() {
        FlatFileItemReader<TwoDto> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(new ClassPathResource("/sample/csvJob2_input.csv"));
        flatFileItemReader.setLinesToSkip(1);

        DefaultLineMapper<TwoDto> defaultLineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
        delimitedLineTokenizer.setNames("one", "two");
        delimitedLineTokenizer.setDelimiter(":");

        BeanWrapperFieldSetMapper<TwoDto> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<>();
        beanWrapperFieldSetMapper.setTargetType(TwoDto.class);

        defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);
        defaultLineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);
        flatFileItemReader.setLineMapper(defaultLineMapper);

        return flatFileItemReader;
    }

    @Bean
    public FlatFileItemWriter<TwoDto> csvJob2FileWriter(Resource resource) {
        CustomBeanWrapperFieldExtractor<TwoDto> beanWrapperFieldExtractor = new CustomBeanWrapperFieldExtractor<>();
        beanWrapperFieldExtractor.setNames(new String[]{"one", "two"});
        beanWrapperFieldExtractor.afterPropertiesSet();

        DelimitedLineAggregator<TwoDto> dtoDelimitedLineAggregator = new DelimitedLineAggregator<>();
        dtoDelimitedLineAggregator.setDelimiter("@");
        dtoDelimitedLineAggregator.setFieldExtractor(beanWrapperFieldExtractor);

        return new FlatFileItemWriterBuilder<TwoDto>().name("csvJob2FileWriter")
                .resource(resource)
                .lineAggregator(dtoDelimitedLineAggregator)
                .build();
    }
}
