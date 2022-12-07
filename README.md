# Quarkus lambda POC with native GraalVM image

### Description
The purpose of this project to deploy and experiment with a native GraalVM image created by the Quarkus framework + java 17 and investigate the cold start and the overall latency of a simple rest endpoint. The deployment is performed using the AWS Serverless Application Model framework (https://aws.amazon.com/serverless/sam/) 

### Requirements:
- Quarkus cli (`brew install quarkusio/tap/quarkus`)
- AWS cli (`brew install awscli`)
- SAM cli (`brew tap aws/tap`, `brew install aws-sam-cli`)
- JDK 17
- Gradle 7.5.1
- Docker

### Test the native image locally
```shell script
./deployments/lambda/sam-native-start-api.sh
curl localhost:3000/products
```


### Build and deploy the AWS lambda:
```shell script
./deployments/lambda/sam-native-deploy.sh
```

### Run the NFT test:
```shell script
./gradlew nft:runNft
```

### Performance test:

1) 100 TPS load, no provisioned concurrency is used -> *Failure*

Load profile 
- Ramp up from 0 to 100 TPS for 10 seconds
- Constant 100 TPS for 60 seconds
- Downsize from 100 TPS to 0 TPS for 10 seconds

Results:
```
================================================================================
---- Global Information --------------------------------------------------------
> request count                                       7000 (OK=5638   KO=1362  )
> min response time                                    105 (OK=105    KO=126   )
> max response time                                  10361 (OK=1069   KO=10361 )
> mean response time                                   508 (OK=489    KO=586   )
> std deviation                                        223 (OK=192    KO=308   )
> response time 50th percentile                        503 (OK=484    KO=578   )
> response time 75th percentile                        641 (OK=622    KO=695   )
> response time 95th percentile                        818 (OK=810    KO=838   )
> response time 99th percentile                        915 (OK=916    KO=898   )
> mean requests/sec                                 82.353 (OK=66.329 KO=16.024)
---- Response Time Distribution ------------------------------------------------
> t < 800 ms                                          5313 ( 76%)
> 800 ms < t < 1200 ms                                 325 (  5%)
> t > 1200 ms                                            0 (  0%)
> failed                                              1362 ( 19%)
---- Errors --------------------------------------------------------------------
> status.find.in([200, 209], 304), found 500                       1362 (100.0%)
================================================================================
```

AWS Cloudwatch insights:

```
---------------------------------------------------------------------------------------------------------------------------------
|       @timestamp        |              @requestId              | @duration | @billedDuration | MaxMemoryUsedMb | MemorySizeMb |
|-------------------------|--------------------------------------|-----------|-----------------|-----------------|--------------|
| 2022-12-07 21:26:13.899 | 6232f3d9-7f51-4fee-b0e4-c601585ec724 | 59.63     | 60.0            | 97.2748         | 122.0703     |
| 2022-12-07 21:26:12.544 | 1814be01-4e41-4242-9bbd-dcc72199f3ab | 20.75     | 21.0            | 96.3211         | 122.0703     |
| 2022-12-07 21:26:16.554 | bf7b204b-0361-4b40-b2ee-100b03fe0a24 | 18.46     | 19.0            | 96.3211         | 122.0703     |
| 2022-12-07 21:26:18.699 | 23b7ce8c-6935-41f5-a6f0-77c5361a2777 | 16.7      | 17.0            | 96.3211         | 122.0703     |
| 2022-12-07 21:26:14.359 | 857653aa-8fdd-4e65-b68f-09d78c75cebe | 16.43     | 17.0            | 97.2748         | 122.0703     |
| 2022-12-07 21:26:12.163 | 763fa872-e093-44a3-83d6-4f90d4243ef7 | 14.68     | 15.0            | 96.3211         | 122.0703     |
| 2022-12-07 21:26:14.878 | bf395b62-ad97-4679-ab3d-423f9ca30393 | 14.52     | 15.0            | 95.3674         | 122.0703     |
| 2022-12-07 21:26:08.576 | 7d77d863-729e-4d5c-af54-36f29ab8d8db | 14.47     | 15.0            | 96.3211         | 122.0703     |
| 2022-12-07 21:26:13.919 | af710a5e-cbb6-4adf-a85d-6f1235547383 | 14.45     | 15.0            | 97.2748         | 122.0703     |
| 2022-12-07 21:26:07.659 | eeca0031-ea62-4fc7-b5a3-48febe2cbc23 | 13.98     | 14.0            | 96.3211         | 122.0703     |
| 2022-12-07 21:26:14.319 | f551c373-01fa-4a73-ad0c-84a45609a52f | 13.93     | 14.0            | 97.2748         | 122.0703     |
| 2022-12-07 21:26:07.276 | 265478a7-8c78-42c1-822a-9f675e510a1e | 12.94     | 13.0            | 96.3211         | 122.0703     |
| 2022-12-07 21:26:11.059 | 854a637a-b5b7-4ced-b1ab-ae3853d76755 | 12.83     | 13.0            | 96.3211         | 122.0703     |
| 2022-12-07 21:26:18.638 | 937ba531-c3b6-436f-ac44-f5fbdaf9c04d | 12.76     | 13.0            | 96.3211         | 122.0703     |
| 2022-12-07 21:26:12.833 | 79b6fcfb-3663-448a-aff7-aae120ca14ba | 12.47     | 13.0            | 97.2748         | 122.0703     |
| 2022-12-07 21:26:15.019 | e516f835-685b-47f9-bd5c-304ce93c9d16 | 12.36     | 13.0            | 97.2748         | 122.0703     |
| 2022-12-07 21:26:13.298 | 82fd0616-3f34-4a1e-8abd-a6d7916f0cf4 | 12.28     | 13.0            | 95.3674         | 122.0703     |
| 2022-12-07 21:26:13.619 | 2d80cdb8-dd6f-4430-b8b5-03157ca7c7fa | 12.26     | 13.0            | 96.3211         | 122.0703     |
| 2022-12-07 21:26:13.378 | 304ff0dc-9ed1-422f-b951-7940e3d95f1a | 11.91     | 12.0            | 95.3674         | 122.0703     |
| 2022-12-07 21:26:14.999 | 4bce7e18-9e26-413c-bb63-1785ce878182 | 10.96     | 11.0            | 97.2748         | 122.0703     |
| 2022-12-07 21:26:12.159 | 7e0e6ea7-a15a-4810-883d-ca23f72ac815 | 10.25     | 11.0            | 96.3211         | 122.0703     |
| 2022-12-07 21:26:11.519 | f006e93d-7a8b-4bf6-b569-7641a41695b3 | 10.12     | 11.0            | 95.3674         | 122.0703     |
| 2022-12-07 21:26:11.039 | f458a5d9-4d34-41f5-af8a-20b9df3024f5 | 9.9       | 10.0            | 96.3211         | 122.0703     |
| 2022-12-07 21:26:13.697 | 2d31bc55-ae55-4b6c-b657-fb1e3f21168d | 9.88      | 10.0            | 95.3674         | 122.0703     |
| 2022-12-07 21:26:12.598 | 411d78fb-e91c-47cf-89cc-fa9f92d7e94a | 9.86      | 10.0            | 95.3674         | 122.0703     |
| 2022-12-07 21:26:15.496 | c3fed2cf-e61c-482f-9df5-bf0c4dad98b6 | 9.75      | 10.0            | 98.2285         | 122.0703     |
| 2022-12-07 21:26:14.838 | 7cd46c3f-2268-4207-a559-091a87bfd881 | 9.49      | 10.0            | 95.3674         | 122.0703     |
| 2022-12-07 21:26:14.798 | 8ddfef6d-6d45-4bb6-ad1a-46082d7759d4 | 9.39      | 10.0            | 95.3674         | 122.0703     |
| 2022-12-07 21:26:15.473 | 968a3958-8902-4180-ad4b-54b4f559ddc0 | 9.29      | 10.0            | 98.2285         | 122.0703     |
| 2022-12-07 21:26:07.271 | 87939d73-275f-4693-8783-5150f5afcee7 | 9.08      | 10.0            | 95.3674         | 122.0703     |
---------------------------------------------------------------------------------------------------------------------------------