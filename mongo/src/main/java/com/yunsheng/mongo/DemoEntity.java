package com.yunsheng.mongo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Document(collection = "demo_collection")
public class DemoEntity implements Serializable {

    @Id
    private Long id;

    private String title;

    private String description;

    private String by;

    private String url;
	
}
