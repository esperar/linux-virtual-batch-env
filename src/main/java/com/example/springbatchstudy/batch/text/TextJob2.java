package com.example.springbatchstudy.batch.text;

import com.example.springbatchstudy.custom.CustomPassThroughLineAggregator;
import com.example.springbatchstudy.dto.OneDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class TextJob2 {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private static final int chunkSize = 5;

    @Bean
    public Job textJob2BatchBuild() {
        return jobBuilderFactory.get("textJob2")
                .start(textJob2BatchStep1())
                .build();
    }

    @Bean
    public Step textJob2BatchStep1() {
        return stepBuilderFactory.get("textJob2BatchStep1")
                .<OneDto, OneDto>chunk(chunkSize)
                .reader(textJob2FileReader())
                .writer(textJob2FileWriter())
                .build();
    }

    @Bean
    public FlatFileItemReader<OneDto> textJob2FileReader() {
        FlatFileItemReader<OneDto> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(new ClassPathResource("/sample/textJob2_input.txt"));
        flatFileItemReader.setLineMapper(((line, lineNumber) -> new OneDto(lineNumber + "_" + line)));
        return flatFileItemReader;
    }

    @Bean
    public FlatFileItemWriter<OneDto> textJob2FileWriter() {
        return new FlatFileItemWriterBuilder<OneDto>()
                .name("textJob2FileWriter")
                .resource(new FileSystemResource("output/textJob2_output.txt"))
                .lineAggregator(new CustomPassThroughLineAggregator<>())
                .build();
    }
}
