
# Overview
In this project, we use distributed planning optimization for SAFe based pi plannings with 5 sprints.
Sprint 6 represents out of scope user stories which by constraints can't be placed in Sprints 1-6


## Step 1 Download project from GitHub, import to Itellij IDEA and run it as a SpringBoot application

## Step 3  Use Curl or Postman to test the REST apis created.    Use the example JSON files to trigger the solution solving


## APIs
POST call to solve the solution. JSON which represents features, user stories and sprints should be passed in a body

http://localhost:9090/solve

http://localhost:9090/solveWithTransform  - transforms output to a Sprint/user stories distribution

### JSON data. Model 1 contains the oen way how the sprints and user stories can be passed to the rest
model2 is second option, a bit more simplified, should be more convenient during the PI planning

              

## Docker commands to run it as a container
mvn clean install
docker build  -t  23123411222/swisscom .
docker push 23123411222/swisscom
docker run -it -p 80:80 --rm --name piplanning 23123411222/swisscom


