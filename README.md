# AccountsRepository

Account Search Service 

API's provided are:
1) Login - To get Authentication token
2) Logout - Destroy token
3} Account Search - Search account details based on ROLE

Find additional details on swagger.

URL- https://${host}:443/swagger-ui.html#/ ---- Update with the host where application is running....

Steps to follow :

1) Run command "mvn clean install" to download all dependencies. It will also run JUNIT test cases.
2) Run as standalone application.
3) Run Test Suite "AllTests" to run the test cases and get coverage.

Steps to run the application:----

1) Login:- 

curl -X POST "https://localhost/bank/login" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"username\": \"user\", \"password\": \"user\"}"

Copy token from login response.

{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwiZXhwIjoxNjU1MzA5OTI3LCJpYXQiOjE2NTUzMDk2Mjd9.A_ynI2MSAi2rFvAiLu_Qve7gRf6pRTTv6fWZJBhog-GaNYnUi9OHCfIFvT2LjtXkzJ6eEF2NkpOmaCvdyI7PpQ"
}

2) Account Search:- Update token received from login after Bearer. If running through swagger update it in authorize block with "Bearer".

curl -X POST "https://localhost/bank/account/search" -H "accept: */*" -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwiZXhwIjoxNjU1MzA5OTI3LCJpYXQiOjE2NTUzMDk2Mjd9.A_ynI2MSAi2rFvAiLu_Qve7gRf6pRTTv6fWZJBhog-GaNYnUi9OHCfIFvT2LjtXkzJ6eEF2NkpOmaCvdyI7PpQ" -H "Content-Type: application/json" -d "{ \"accountId\": \"2\" }"

Response- Account DATA

3) Logout: -

curl -X POST "https://localhost/bank/logout" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"username\": \"user\", \"password\": \"user\"}"

Response - User is logged out successfully.












