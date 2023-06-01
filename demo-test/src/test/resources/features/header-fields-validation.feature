Feature: Validate header fields mandatory for qrcode endpoint

  Scenario: Try to request with null field in header list
    Given I have a qrcode request with required default request body
    And I have the following headers
    When I make a request to qrcode endpoint
    Then QRcode response should have the follow data error
