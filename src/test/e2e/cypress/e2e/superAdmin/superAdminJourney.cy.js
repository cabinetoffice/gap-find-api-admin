describe('template spec', () => {
  beforeEach(() => {
   const superAdminToken =
'eyJraWQiOiJlN2QzZjk0OS01MzdkLTQxMDItODcwNy0wY2NhZDIwYzBjNDAiLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1cm46ZmRjOmdvdi51azoyMDIyOmZrRlMwTVFQWFdpN3FnRTNVdm53amIyMUJBRXQxVGQyMEQwYzRJUE95S2ciLCJhdWQiOiJGR1AiLCJyb2xlcyI6IltBUFBMSUNBTlQsIEZJTkQsIFRFQ0hOSUNBTF9TVVBQT1JULCBTVVBFUl9BRE1JTl0iLCJpZFRva2VuIjoiZXlKcmFXUWlPaUkyTkRSaFpqVTVPR0kzT0RCbU5UUXhNRFpqWVRCbU0yTXdNVGN6TkRGaVl6SXpNR00wWmpnek56Tm1NelZtTXpKbE1UaGxNMlUwTUdOak4yRmpabVkySWl3aVlXeG5Jam9pUlZNeU5UWWlmUS5leUpoZEY5b1lYTm9Jam9pY1RVd1VrNTRkVWxaUWs4NVRIaFZOVE5PUTFseWR5SXNJbk4xWWlJNkluVnlianBtWkdNNloyOTJMblZyT2pJd01qSTZabXRHVXpCTlVWQllWMmszY1dkRk0xVjJibmRxWWpJeFFrRkZkREZVWkRJd1JEQmpORWxRVDNsTFp5SXNJbUYxWkNJNkltUjNXWFV4TlU5eWVVeHVTRjlDUjFaWVZXWlJTVE5WWXpNM1ZTSXNJbWx6Y3lJNkltaDBkSEJ6T2k4dmIybGtZeTVwYm5SbFozSmhkR2x2Ymk1aFkyTnZkVzUwTG1kdmRpNTFheThpTENKMmIzUWlPaUpEYkM1RGJTSXNJbVY0Y0NJNk1UWTVNamsyT1RjME1Td2lhV0YwSWpveE5qa3lPVFk1TmpJeExDSnViMjVqWlNJNklrWTNNbVp5YkVwT1lqbHVaVEpwTkdSSGNrbEJUQzEyY0dKMmFVUkZRbXBqZWxvMWMwa3lkazlRY0hKR1RuQk1Uems0UVZkU2NFeElaVXAxZWpNNE9Yb2lMQ0oyZEcwaU9pSm9kSFJ3Y3pvdkwyOXBaR011YVc1MFpXZHlZWFJwYjI0dVlXTmpiM1Z1ZEM1bmIzWXVkV3N2ZEhKMWMzUnRZWEpySWl3aWMybGtJam9pZUU1WGRHcGFNbmhIYUhka2NERTNZVlpGUjBwNFQyUjVOMVZWSW4wLnc5U0pheFlYNnYtdlhjbXlVbDRjZGpFdlAtMkVpZzZHY0RkbHdvajJIOWxCU0x5Yko5RVY2UDZSNkJLczgyZmhnMVFMY1NReDk1cm9vcjQ3cHBmZkx3IiwiaXNzIjoiaHR0cHM6Ly9zYW5kYm94LWdhcC5zZXJ2aWNlLmNhYmluZXRvZmZpY2UuZ292LnVrL2FwcGx5L3VzZXIiLCJkZXBhcnRtZW50IjoiQ2FiaW5ldCBPZmZpY2UiLCJleHAiOjE2OTI5NzMyMjIsImlhdCI6MTY5Mjk2OTYyMiwiZW1haWwiOiJhbnRvbmlvLmxvcnVzc29AYW5kLmRpZ2l0YWwiLCJqdGkiOiJhODdkZTRjMy1jM2MyLTRlOTgtODEwYi03MGI5OGQ0ZWJjYmYifQ.JPZGL-ikJ11rpGmMowBzGSDXxZGaj_ewOsJv5fYU-cH9P-qpr7wRBs9pdCOmu-C8q8AYmfAWcTwYO_Ruz-9fsjGnxkltzdsDv3e94AjhlQ4BibAmfPj4PncULwTX_Rn9vgUwA5lNiizCA-4NpJu8Y23l0LhsySk3LNwoG985utzL63V_K5YzOymHO8sYRRvOu9_w3fOmPI27EvG6QMSUgAmZYlnu6mVwLhcRLa0KX7HejvBDkfblqWub-BKNsCakVRsY-B1xTrOmoM6_L0mawwxHr767yi91oHWp9ab7WZGC618MvFAwjWNKDDHT9kagkK0bkXc3g0t8aRHFruqGjg'
   cy.setCookie('user-service-token', superAdminToken);
  });
  it('passes', () => {
    cy.visit('http://localhost:8084/find/api/admin/api-keys/manage');
  });
});