package com.example.sampletest.demo.core;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.time.Duration;

@Repository
public class DBManager {
    private ConnectionFactory factory;
    private ConnectionPool pool;

    //연결
    @PostConstruct
    public void init() {
        factory = ConnectionFactories.get("r2dbcs:pool:mysql://deveun:root@localhost:3306/demo");
        ConnectionPoolConfiguration configuration = ConnectionPoolConfiguration.builder(factory)
                .maxIdleTime(Duration.ofMillis(1000))
                .maxSize(20)
                .build();
        pool =  new ConnectionPool(configuration);
    }

    //단순 셀렉트 예제
    public Flux<StdInfo> selectAll() {
       return Flux.from(pool.create()).concatMap(connection ->  //커넥션 가공 및 1차 변환
                Flux.from(connection.createStatement("select * from studentInfo").execute())
                        .concatMap( result-> //2차 변환
                                result.map((row, rowMetadata)->{
                                    StdInfo info = new StdInfo();
                                    info.setId(row.get("id", String.class));
                                    info.setName(row.get("name", String.class));
                                    info.setMajor(row.get("major", String.class));
                                    return info;
                                })
                        ).doFinally( (st)->{connection.close();})
        );
    }

}
