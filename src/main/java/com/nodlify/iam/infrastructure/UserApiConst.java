package com.nodlify.iam.infrastructure;

record UserApiConst() {

    public static final String USERS_EXAMPLE_JSON = """
            {
              "content": [
                {
                  "username": "john.doe",
                  "email": "john.doe@example.com"
                },
                {
                  "username": "jane.smith",
                  "email": "jane.smith@example.com"
                }
              ],
              "pageable": {
                "sort": { "sorted": true, "unsorted": false, "empty": false },
                "pageNumber": 0,
                "pageSize": 20,
                "offset": 0,
                "paged": true,
                "unpaged": false
              },
              "totalElements": 2,
              "totalPages": 1,
              "last": true,
              "size": 20,
              "number": 0,
              "sort": { "sorted": true, "unsorted": false, "empty": false },
              "first": true,
              "numberOfElements": 2,
              "empty": false
            }
            """;

    public static final String USER_BY_ID_EXAMPLE_JSON = """
            {
              "username": "jane.smith",
              "email": "jane.smith@example.com"
            }
            """;
}
