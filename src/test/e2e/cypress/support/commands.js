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

Cypress.Commands.add("setMockTokenForTechnicalSupport", () => {
  const techSupportToken =
    "eyJraWQiOiJlN2QzZjk0OS01MzdkLTQxMDItODcwNy0wY2NhZDIwYzBjNDAiLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ0Y28uY3lwcmVzcy50ZXN0LnRlY2huaWNhbC5zdXBwb3J0IiwiYXVkIjoiRkdQIiwicm9sZXMiOiJbQVBQTElDQU5ULCBGSU5ELCBURUNITklDQUxfU1VQUE9SVF0iLCJpZFRva2VuIjoiYmFuYW5hIiwiaXNzIjoiYmFuYW5hIiwiZGVwYXJ0bWVudCI6IkJhbmFuYSIsImV4cCI6MTY5Mjk3MzIyMiwiaWF0IjoxNjkyOTY5NjIyLCJlbWFpbCI6ImJhbmFuYUBmcnVpdCIsImp0aSI6ImJhbmFuYSJ9.RK7Rit6kwwXxLEaH2RR2zLGdl_mNSrJFX1sm6xVj1pTkQInRamLQsZrgDvDQjvOcKcXZ4Scb4JIQ9f0kIzB8UqsYeUk1_Wk6L_URavYyWz5TsOfzkzSoZ-7XXZN1p2gvJlKwuD-WFoRDVXNpUxn0gaCoCdtKJWlsrwEK20A--Jr2yTaZo3uSI00I2MYi7ZZnXF_f15xjTj58iUGsjjkxdEYS3fPizR5XoTMmftohDOzgqKuZDxEEbtKaVCsXcMvQn70rdDg08y1VAj2t05KoL4vxSazOotR6n48iiZqq_8XSbXUW2pOPc70qHPwXzqg74PegbvNbNScBA2v-RpaF1A";
  cy.setCookie("user-service-token", techSupportToken);
});
