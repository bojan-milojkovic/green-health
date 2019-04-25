package com.green.health.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
/*import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;*/

@Configuration
@ComponentScan(basePackages={"com.green.health.*"})
public class SpringConfig {
	
    public static final String ENCODING_UTF_8 = "UTF-8";

    public static final long MAX_UPLOAD_FILE_SIZE = 52428807;

    public static final long MAX_UPLOAD_PER_FILE_SIZE = 5242880;

/*	@Bean
    @Primary
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        // this makes sure that datetime in string format in input 
		// is automatically converted to LocalDateTime object
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return objectMapper;
    }*/
	
	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver ret = new CommonsMultipartResolver();

        ret.setMaxUploadSize(MAX_UPLOAD_FILE_SIZE);

        ret.setMaxUploadSizePerFile(MAX_UPLOAD_PER_FILE_SIZE);

        ret.setDefaultEncoding(ENCODING_UTF_8);

        return ret;
	}
}