Feature: End to End test for SD Factory

@TEST_CXGP-636
Scenario: TS_CX_Core_SD_Factory_Create a Verifiable credential
Given User is authorized
When User creates a Verifiable credential
Then User gets 202 success code

@TEST_CXGP-638
Scenario: TS_CX_Core_SD_Factory_Create a Verifiable credential with invalid Request and expect Bad Request
Given User is authorized
When User creates a Verifiable credential with invalid request body
Then User gets 400 error code

@TEST_CXGP-684
Scenario: TS_CX_Core_SD_Factory_Create a Verifiable credential and expect Unauthorized Response
Given User is authorized
When User creates a Verifiable credential with invalid wallet id
Then User gets 404 error code

@TEST_CXGP-683
Scenario: TS_CX_Core_SD_Factory_Create a Verifiable credential and expect Unauthorized Response
Given User is authorized
When User creates a Verifiable credential with invalid JWT token
Then User gets 401 error code

@TEST_CXGP-681
Scenario: TS_CX_Core_SD_Factory_Create a Verifiable credential and expect Forbidden Response
Given User is authorized but not having access to create VC
When User not having access creates a Verifiable credential
Then User gets 403 error code