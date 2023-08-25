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

Cypress.Commands.add('setMockToken', () => {
  // Replace with your mock JWT token
  const mockToken =
    'eyJraWQiOiIzNTlhYTk0OS1mNWEzLTQ1MWMtYmQyMi01YjllYWExMzk0ZGEiLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1cm46ZmRjOmdvdi51azoyMDIyOmZrRlMwTVFQWFdpN3FnRTNVdm53amIyMUJBRXQxVGQyMEQwYzRJUE95S2ciLCJhdWQiOiJGR1AiLCJyb2xlcyI6IltBUFBMSUNBTlQsIEZJTkQsIFRFQ0hOSUNBTF9TVVBQT1JUXSIsImlkVG9rZW4iOiJleUpyYVdRaU9pSTJORFJoWmpVNU9HSTNPREJtTlRReE1EWmpZVEJtTTJNd01UY3pOREZpWXpJek1HTTBaamd6TnpObU16Vm1NekpsTVRobE0yVTBNR05qTjJGalptWTJJaXdpWVd4bklqb2lSVk15TlRZaWZRLmV5SmhkRjlvWVhOb0lqb2ljWEYyV2paT1kzVjNNa2hUZGxOelRVRTFXbGt4UVNJc0luTjFZaUk2SW5WeWJqcG1aR002WjI5MkxuVnJPakl3TWpJNlptdEdVekJOVVZCWVYyazNjV2RGTTFWMmJuZHFZakl4UWtGRmRERlVaREl3UkRCak5FbFFUM2xMWnlJc0ltRjFaQ0k2SW1SM1dYVXhOVTl5ZVV4dVNGOUNSMVpZVldaUlNUTlZZek0zVlNJc0ltbHpjeUk2SW1oMGRIQnpPaTh2YjJsa1l5NXBiblJsWjNKaGRHbHZiaTVoWTJOdmRXNTBMbWR2ZGk1MWF5OGlMQ0oyYjNRaU9pSkRiQzVEYlNJc0ltVjRjQ0k2TVRZNU1qZzRPVEF6TlN3aWFXRjBJam94TmpreU9EZzRPVEUxTENKdWIyNWpaU0k2SWt0aGJWbElVMHh0YkdwSGJUSTJhblJyVlY5RFowZERTR2RsVlZwQlkwTjNVRUkyWVZGRk9WZFdNRzg0U1hZM2EwbElXR3RFZG0xYU5IVXhha2xSZVV3aUxDSjJkRzBpT2lKb2RIUndjem92TDI5cFpHTXVhVzUwWldkeVlYUnBiMjR1WVdOamIzVnVkQzVuYjNZdWRXc3ZkSEoxYzNSdFlYSnJJaXdpYzJsa0lqb2lhMEo0VkVkelEzRkhPVUZRTjBGSmRVNHlXVkpwTTBseVh5MXZJbjAuTWZMaDVBN3hoRUIwVVpPYjdWdzh6c1FDN3FqRzBQaUlrSXRXSGM1SUdjM2x0ZXZtbmpfb0FZdm5ad1ViRmtrVmJWYThmSFJVbUpxVGk5UnZYLWFuTkEiLCJpc3MiOiJodHRwczovL3NhbmRib3gtZ2FwLnNlcnZpY2UuY2FiaW5ldG9mZmljZS5nb3YudWsvYXBwbHkvdXNlciIsImRlcGFydG1lbnQiOiJDYWJpbmV0IE9mZmljZSIsImV4cCI6MTY5Mjg5MjUxNSwiaWF0IjoxNjkyODg4OTE1LCJlbWFpbCI6ImFudG9uaW8ubG9ydXNzb0BhbmQuZGlnaXRhbCIsImp0aSI6IjQ3MGM3MTIyLWQwYzgtNGUwYS05NWEyLTA4NzdiOTY1NDc4OSJ9.Q6XKSMptVdg-FSmgDj2P2IdYzkg-Jc9pk83AFvMwsP84k-yDYHwxdfsjWINKZo0PEWRvF04Xh9GNG0jrMuDXX6rBBuXNa7HyAh0WdBx0u8eOQxlbWsism_SShVFWHmIrl-EUHwkvigTAZ-KfzvZzM5TWwd1r_yRaepWO87NRrfC7-fyqzoQJNdvkVIhJP8geZcTps239uOCk6QJTAnnSxSH6-ue8CA_Dq4_kkRoSt2qUfY9F1Hi4f9WdooVsZGftaaiMQzARNHwe6ilExV8iygylvM19FNxV11XNYxeGn3DSlTbyrrv5-3KU0mthBJlqRvELNdiTC9AS2sL9YXQS9Q';
  cy.setCookie('user-service-token', mockToken);
});