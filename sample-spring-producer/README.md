* `resources/application.properties` 수정   
    ```shell
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
    spring.datasource.url=jdbc:mysql://DB_HOST:DB_PORT/DB_TABLE_NAME?&useSSL=false&characterEncoding=UTF-8&serverTimezone=UTC
    spring.datasource.username=DB_USER_NAME
    spring.datasource.password=DB_USER_PASSWORD
    ```

* 실행
  ```shell
  $ curl http://localhost:8080/kafka/producer/start
  ```
  
* 중지
  ```shell
  $ curl http://localhost:8080/kafka/producer/stop
  ```


5개의 필드를 가져오는 데 2Mb가 조금 안됨   

메세지 사이즈에 관한 설정들을 바꿔주어야 할 필요가 있음

* `server.properties`
    * `message.max.bytes` : 브로커가 토픽에 쓰는 메세지의 최대 사이즈, 디폴드 값은 약 1메가 정도   
    예시 : `message.max.bytes=3072000`
* `consumer.properties` : kafka-console-consumer 사용 시
    * `fetch.max.bytes` : 컨슈머가 fetch할 수 있는 데이터의 최대 사이즈   
      예시 : `fetch.max.bytes=3072000`
* `ProducerConfig` : spring-kafka 사용 시
    * `MAX_REQUEST_SIZE_CONFIG` : 프로듀서가 카프카에 보낼 메세지의 최대 사이즈   
        예시 : `props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, 3072000);`