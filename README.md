
# Spring Boot multi-tenant sample app

This sample app uses the [Spring Boot starter library from Quantics](https://github.com/quantics-io/multitenant-oauth2-spring-boot-starter) 
which contains all the configuration options that are required to build a multi-tenant Spring Boot application.

In addition to the final sample, there are two minimal working examples in this repo:

| Spring Profile                                                     | Tenant resolution mode            |
|--------------------------------------------------------------------|-----------------------------------|
| [default](src/main/resources/application.properties)               | Header (using custom HTTP header) |
| [production](src/main/resources/application-prod.properties) | JWT (using OAuth2 issuer claim)   |


The code in this repository accompanies the following blog posts:
- [How to build a multi-tenant SaaS solution: A Spring Boot sample app](https://jomatt.io/how-to-build-a-multi-tenant-saas-solution-spring-boot-sample-app)
- [How to make your multi-tenant Spring app production-ready](https://jomatt.io/how-to-make-your-multi-tenant-spring-app-production-ready)
