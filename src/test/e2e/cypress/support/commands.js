// ***********************************************
// This example commands.js shows you how to
// create various custom commands and overwrite
// existing commands.
//
// For more comprehensive examples of custom
// commands please read more here:
// https://on.cypress.io/custom-commands
// ***********************************************
//
//
// -- This is a parent command --
// Cypress.Commands.add('login', (email, password) => { ... })
//
//
// -- This is a child command --
// Cypress.Commands.add('drag', { prevSubject: 'element'}, (subject, options) => { ... })
//
//
// -- This is a dual command --
// Cypress.Commands.add('dismiss', { prevSubject: 'optional'}, (subject, options) => { ... })
//
//
// -- This will overwrite an existing command --
// Cypress.Commands.overwrite('visit', (originalFn, url, options) => { ... })

Cypress.Commands.add('setMockTokenForSuperAdmin', () => {
  const superAdminToken =
    'eyJraWQiOiJlN2QzZjk0OS01MzdkLTQxMDItODcwNy0wY2NhZDIwYzBjNDAiLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1cm46ZmRjOmdvdi51azoyMDIyOmZrRlMwTVFQWFdpN3FnRTNVdm53amIyMUJBRXQxVGQyMEQwYzRJUE95S2ciLCJhdWQiOiJGR1AiLCJyb2xlcyI6IltBUFBMSUNBTlQsIEZJTkQsIFRFQ0hOSUNBTF9TVVBQT1JULCBTVVBFUl9BRE1JTl0iLCJpZFRva2VuIjoiZXlKcmFXUWlPaUkyTkRSaFpqVTVPR0kzT0RCbU5UUXhNRFpqWVRCbU0yTXdNVGN6TkRGaVl6SXpNR00wWmpnek56Tm1NelZtTXpKbE1UaGxNMlUwTUdOak4yRmpabVkySWl3aVlXeG5Jam9pUlZNeU5UWWlmUS5leUpoZEY5b1lYTm9Jam9pY1RVd1VrNTRkVWxaUWs4NVRIaFZOVE5PUTFseWR5SXNJbk4xWWlJNkluVnlianBtWkdNNloyOTJMblZyT2pJd01qSTZabXRHVXpCTlVWQllWMmszY1dkRk0xVjJibmRxWWpJeFFrRkZkREZVWkRJd1JEQmpORWxRVDNsTFp5SXNJbUYxWkNJNkltUjNXWFV4TlU5eWVVeHVTRjlDUjFaWVZXWlJTVE5WWXpNM1ZTSXNJbWx6Y3lJNkltaDBkSEJ6T2k4dmIybGtZeTVwYm5SbFozSmhkR2x2Ymk1aFkyTnZkVzUwTG1kdmRpNTFheThpTENKMmIzUWlPaUpEYkM1RGJTSXNJbVY0Y0NJNk1UWTVNamsyT1RjME1Td2lhV0YwSWpveE5qa3lPVFk1TmpJeExDSnViMjVqWlNJNklrWTNNbVp5YkVwT1lqbHVaVEpwTkdSSGNrbEJUQzEyY0dKMmFVUkZRbXBqZWxvMWMwa3lkazlRY0hKR1RuQk1Uems0UVZkU2NFeElaVXAxZWpNNE9Yb2lMQ0oyZEcwaU9pSm9kSFJ3Y3pvdkwyOXBaR011YVc1MFpXZHlZWFJwYjI0dVlXTmpiM1Z1ZEM1bmIzWXVkV3N2ZEhKMWMzUnRZWEpySWl3aWMybGtJam9pZUU1WGRHcGFNbmhIYUhka2NERTNZVlpGUjBwNFQyUjVOMVZWSW4wLnc5U0pheFlYNnYtdlhjbXlVbDRjZGpFdlAtMkVpZzZHY0RkbHdvajJIOWxCU0x5Yko5RVY2UDZSNkJLczgyZmhnMVFMY1NReDk1cm9vcjQ3cHBmZkx3IiwiaXNzIjoiaHR0cHM6Ly9zYW5kYm94LWdhcC5zZXJ2aWNlLmNhYmluZXRvZmZpY2UuZ292LnVrL2FwcGx5L3VzZXIiLCJkZXBhcnRtZW50IjoiQ2FiaW5ldCBPZmZpY2UiLCJleHAiOjE2OTI5NzMyMjIsImlhdCI6MTY5Mjk2OTYyMiwiZW1haWwiOiJhbnRvbmlvLmxvcnVzc29AYW5kLmRpZ2l0YWwiLCJqdGkiOiJhODdkZTRjMy1jM2MyLTRlOTgtODEwYi03MGI5OGQ0ZWJjYmYifQ.JPZGL-ikJ11rpGmMowBzGSDXxZGaj_ewOsJv5fYU-cH9P-qpr7wRBs9pdCOmu-C8q8AYmfAWcTwYO_Ruz-9fsjGnxkltzdsDv3e94AjhlQ4BibAmfPj4PncULwTX_Rn9vgUwA5lNiizCA-4NpJu8Y23l0LhsySk3LNwoG985utzL63V_K5YzOymHO8sYRRvOu9_w3fOmPI27EvG6QMSUgAmZYlnu6mVwLhcRLa0KX7HejvBDkfblqWub-BKNsCakVRsY-B1xTrOmoM6_L0mawwxHr767yi91oHWp9ab7WZGC618MvFAwjWNKDDHT9kagkK0bkXc3g0t8aRHFruqGjg';
  cy.setCookie('user-service-token', superAdminToken);
});
Cypress.Commands.add('setMockTokenForTechnicalSupportFundingOrganisation1', () => {
  const techSupportToken =
    'eyJraWQiOiIzNTlhYTk0OS1mNWEzLTQ1MWMtYmQyMi01YjllYWExMzk0ZGEiLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1cm46ZmRjOmdvdi51azoyMDIyOmZrRlMwTVFQWFdpN3FnRTNVdm53amIyMUJBRXQxVGQyMEQwYzRJUE95S2ciLCJhdWQiOiJGR1AiLCJyb2xlcyI6IltBUFBMSUNBTlQsIEZJTkQsIFRFQ0hOSUNBTF9TVVBQT1JUXSIsImlkVG9rZW4iOiJleUpyYVdRaU9pSTJORFJoWmpVNU9HSTNPREJtTlRReE1EWmpZVEJtTTJNd01UY3pOREZpWXpJek1HTTBaamd6TnpObU16Vm1NekpsTVRobE0yVTBNR05qTjJGalptWTJJaXdpWVd4bklqb2lSVk15TlRZaWZRLmV5SmhkRjlvWVhOb0lqb2ljWEYyV2paT1kzVjNNa2hUZGxOelRVRTFXbGt4UVNJc0luTjFZaUk2SW5WeWJqcG1aR002WjI5MkxuVnJPakl3TWpJNlptdEdVekJOVVZCWVYyazNjV2RGTTFWMmJuZHFZakl4UWtGRmRERlVaREl3UkRCak5FbFFUM2xMWnlJc0ltRjFaQ0k2SW1SM1dYVXhOVTl5ZVV4dVNGOUNSMVpZVldaUlNUTlZZek0zVlNJc0ltbHpjeUk2SW1oMGRIQnpPaTh2YjJsa1l5NXBiblJsWjNKaGRHbHZiaTVoWTJOdmRXNTBMbWR2ZGk1MWF5OGlMQ0oyYjNRaU9pSkRiQzVEYlNJc0ltVjRjQ0k2TVRZNU1qZzRPVEF6TlN3aWFXRjBJam94TmpreU9EZzRPVEUxTENKdWIyNWpaU0k2SWt0aGJWbElVMHh0YkdwSGJUSTJhblJyVlY5RFowZERTR2RsVlZwQlkwTjNVRUkyWVZGRk9WZFdNRzg0U1hZM2EwbElXR3RFZG0xYU5IVXhha2xSZVV3aUxDSjJkRzBpT2lKb2RIUndjem92TDI5cFpHTXVhVzUwWldkeVlYUnBiMjR1WVdOamIzVnVkQzVuYjNZdWRXc3ZkSEoxYzNSdFlYSnJJaXdpYzJsa0lqb2lhMEo0VkVkelEzRkhPVUZRTjBGSmRVNHlXVkpwTTBseVh5MXZJbjAuTWZMaDVBN3hoRUIwVVpPYjdWdzh6c1FDN3FqRzBQaUlrSXRXSGM1SUdjM2x0ZXZtbmpfb0FZdm5ad1ViRmtrVmJWYThmSFJVbUpxVGk5UnZYLWFuTkEiLCJpc3MiOiJodHRwczovL3NhbmRib3gtZ2FwLnNlcnZpY2UuY2FiaW5ldG9mZmljZS5nb3YudWsvYXBwbHkvdXNlciIsImRlcGFydG1lbnQiOiJDYWJpbmV0IE9mZmljZSIsImV4cCI6MTY5Mjg5MjUxNSwiaWF0IjoxNjkyODg4OTE1LCJlbWFpbCI6ImFudG9uaW8ubG9ydXNzb0BhbmQuZGlnaXRhbCIsImp0aSI6IjQ3MGM3MTIyLWQwYzgtNGUwYS05NWEyLTA4NzdiOTY1NDc4OSJ9.Q6XKSMptVdg-FSmgDj2P2IdYzkg-Jc9pk83AFvMwsP84k-yDYHwxdfsjWINKZo0PEWRvF04Xh9GNG0jrMuDXX6rBBuXNa7HyAh0WdBx0u8eOQxlbWsism_SShVFWHmIrl-EUHwkvigTAZ-KfzvZzM5TWwd1r_yRaepWO87NRrfC7-fyqzoQJNdvkVIhJP8geZcTps239uOCk6QJTAnnSxSH6-ue8CA_Dq4_kkRoSt2qUfY9F1Hi4f9WdooVsZGftaaiMQzARNHwe6ilExV8iygylvM19FNxV11XNYxeGn3DSlTbyrrv5-3KU0mthBJlqRvELNdiTC9AS2sL9YXQS9Q';
  cy.setCookie('user-service-token', techSupportToken);
});
Cypress.Commands.add('setMockTokenForTechnicalSupportFundingOrganisation2', () => {
  const techSupportToken =
    'eyJraWQiOiIzNTlhYTk0OS1mNWEzLTQ1MWMtYmQyMi01YjllYWExMzk0ZGEiLCJ0eXAiOiJKV1QiLCJhbGciOiJub25lIn0.eyJzdWIiOiJ0Y28uY3lwcmVzcy50ZXN0IiwiYXVkIjoiRkdQIiwicm9sZXMiOiJbQVBQTElDQU5ULCBGSU5ELCBURUNITklDQUxfU1VQUE9SVF0iLCJpZFRva2VuIjoiZXlKcmFXUWlPaUkyTkRSaFpqVTVPR0kzT0RCbU5UUXhNRFpqWVRCbU0yTXdNVGN6TkRGaVl6SXpNR00wWmpnek56Tm1NelZtTXpKbE1UaGxNMlUwTUdOak4yRmpabVkySWl3aVlXeG5Jam9pUlZNeU5UWWlmUS5leUpoZEY5b1lYTm9Jam9pY1hGMldqWk9ZM1YzTWtoVGRsTnpUVUUxV2xreFFTSXNJbk4xWWlJNkluVnlianBtWkdNNloyOTJMblZyT2pJd01qSTZabXRHVXpCTlVWQllWMmszY1dkRk0xVjJibmRxWWpJeFFrRkZkREZVWkRJd1JEQmpORWxRVDNsTFp5SXNJbUYxWkNJNkltUjNXWFV4TlU5eWVVeHVTRjlDUjFaWVZXWlJTVE5WWXpNM1ZTSXNJbWx6Y3lJNkltaDBkSEJ6T2k4dmIybGtZeTVwYm5SbFozSmhkR2x2Ymk1aFkyTnZkVzUwTG1kdmRpNTFheThpTENKMmIzUWlPaUpEYkM1RGJTSXNJbVY0Y0NJNk1UWTVNamc0T1RBek5Td2lhV0YwSWpveE5qa3lPRGc0T1RFMUxDSnViMjVqWlNJNklrdGhiVmxJVTB4dGJHcEhiVEkyYW5SclZWOURaMGREU0dkbFZWcEJZME4zVUVJMllWRkZPVmRXTUc4NFNYWTNhMGxJV0d0RWRtMWFOSFV4YWtsUmVVd2lMQ0oyZEcwaU9pSm9kSFJ3Y3pvdkwyOXBaR011YVc1MFpXZHlZWFJwYjI0dVlXTmpiM1Z1ZEM1bmIzWXVkV3N2ZEhKMWMzUnRZWEpySWl3aWMybGtJam9pYTBKNFZFZHpRM0ZIT1VGUU4wRkpkVTR5V1ZKcE0wbHlYeTF2SW4wLk1mTGg1QTd4aEVCMFVaT2I3Vnc4enNRQzdxakcwUGlJa0l0V0hjNUlHYzNsdGV2bW5qX29BWXZuWndVYkZra1ZiVmE4ZkhSVW1KcVRpOVJ2WC1hbk5BIiwiaXNzIjoiaHR0cHM6Ly9zYW5kYm94LWdhcC5zZXJ2aWNlLmNhYmluZXRvZmZpY2UuZ292LnVrL2FwcGx5L3VzZXIiLCJkZXBhcnRtZW50IjoiQ2FiaW5ldCBPZmZpY2UiLCJleHAiOjE2OTI4OTI1MTUsImlhdCI6MTY5Mjg4ODkxNSwiZW1haWwiOiJ0Y28uY3lwcmVzcy50ZXN0QHR1dGFub3RhLmNvbSIsImp0aSI6IjQ3MGM3MTIyLWQwYzgtNGUwYS05NWEyLTA4NzdiOTY1NDc4OSJ9.';
  cy.setCookie('user-service-token', techSupportToken);
});
