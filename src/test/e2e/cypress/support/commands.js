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
    'eyJraWQiOiJlN2QzZjk0OS01MzdkLTQxMDItODcwNy0wY2NhZDIwYzBjNDAiLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ0Y28uY3lwcmVzcy50ZXN0LnN1cGVyLmFkbWluIiwiYXVkIjoiRkdQIiwicm9sZXMiOiJbQVBQTElDQU5ULCBGSU5ELCBURUNITklDQUxfU1VQUE9SVCwgU1VQRVJfQURNSU5dIiwiaWRUb2tlbiI6ImJhbmFuYSIsImlzcyI6ImJhbmFuYSIsImRlcGFydG1lbnQiOiJCYW5hbmEiLCJleHAiOjE2OTI5NzMyMjIsImlhdCI6MTY5Mjk2OTYyMiwiZW1haWwiOiJiYW5hbmFAZnJ1aXQiLCJqdGkiOiJiYW5hbmEifQ.pDsgFxc1gijJhRmbYkCgJzD4eApYTxi8ue1Ziq29-MhzQCeUubj8i6_rvG3FzVZHI2wGkGqRL-ZDOSH4hVBfuFxP04SNOkQb3dq4uFvAaTngJYvz62GEUC-vwiiMDX9sp8LQrwW-bCkIvjPeAdJ2NxcSTyb1iaz7y2XFwxL_DBG2D4I8hbCekM5eOYmF9JGEVsxufqe350b-D6n-ojI8f1uPocYpHXdnMwI8S0tKYtef62XLPhPTSH-EsS0segvGvWcxeNBCTEKrKfhE6qvbN_heX52oOGhA4wpGoBkFqyWAiao8nlaPbmlmX1hpfxCA4csVhA2BHMMtjgNHIqb6Pg';
  cy.setCookie('user-service-token', superAdminToken);
});

Cypress.Commands.add('setMockTokenForTechnicalSupportFundingOrganisation1', () => {
  const techSupportToken =
    'eyJraWQiOiJlN2QzZjk0OS01MzdkLTQxMDItODcwNy0wY2NhZDIwYzBjNDAiLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ0Y28uY3lwcmVzcy50ZXN0LnRlY2huaWNhbC5zdXBwb3J0IiwiYXVkIjoiRkdQIiwicm9sZXMiOiJbQVBQTElDQU5ULCBGSU5ELCBURUNITklDQUxfU1VQUE9SVF0iLCJpZFRva2VuIjoiYmFuYW5hIiwiaXNzIjoiYmFuYW5hIiwiZGVwYXJ0bWVudCI6IkJhbmFuYSIsImV4cCI6MTY5Mjk3MzIyMiwiaWF0IjoxNjkyOTY5NjIyLCJlbWFpbCI6ImJhbmFuYUBmcnVpdCIsImp0aSI6ImJhbmFuYSJ9.RK7Rit6kwwXxLEaH2RR2zLGdl_mNSrJFX1sm6xVj1pTkQInRamLQsZrgDvDQjvOcKcXZ4Scb4JIQ9f0kIzB8UqsYeUk1_Wk6L_URavYyWz5TsOfzkzSoZ-7XXZN1p2gvJlKwuD-WFoRDVXNpUxn0gaCoCdtKJWlsrwEK20A--Jr2yTaZo3uSI00I2MYi7ZZnXF_f15xjTj58iUGsjjkxdEYS3fPizR5XoTMmftohDOzgqKuZDxEEbtKaVCsXcMvQn70rdDg08y1VAj2t05KoL4vxSazOotR6n48iiZqq_8XSbXUW2pOPc70qHPwXzqg74PegbvNbNScBA2v-RpaF1A';
  cy.setCookie('user-service-token', techSupportToken);
});

Cypress.Commands.add('setMockTokenForTechnicalSupportFundingOrganisation2', () => {
  const techSupportToken =
    'eyJraWQiOiJlN2QzZjk0OS01MzdkLTQxMDItODcwNy0wY2NhZDIwYzBjNDAiLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ0Y28uY3lwcmVzcy50ZXN0LnRlY2huaWNhbC5zdXBwb3J0LjIiLCJhdWQiOiJGR1AiLCJyb2xlcyI6IltBUFBMSUNBTlQsIEZJTkQsIFRFQ0hOSUNBTF9TVVBQT1JUXSIsImlkVG9rZW4iOiJiYW5hbmEiLCJpc3MiOiJiYW5hbmEiLCJkZXBhcnRtZW50IjoiQmFuYW5hIiwiZXhwIjoxNjkyOTczMjIyLCJpYXQiOjE2OTI5Njk2MjIsImVtYWlsIjoiYmFuYW5hQGZydWl0IiwianRpIjoiYmFuYW5hIn0.JoQEBfLB4zNtWBEFYAMq6nTYKMuSKMDZSZAcYLkzhsQ-IeCigqaohE-MCLSlnYnkH3l_BQivJ06ntl91C5oQFE2iFt8HdL7q7j7viRlrPLyAxy9De4LsSy96tgkHrOP5FU-FUI-Lh1HUCF5gghrVoD3jRn3ksdKTjh9vUA0WJ61IJSmdyLRaAX3IDIxjeEGHjSTO9-V4PjPyQv88sTey6jNsdbbGsUga7zMLMSFDGSEihd31h1-noP6rH7nlhvioj9zh5QzTJx_OXqcq_rtp9ibVRAZPDLkOBN2fkQgz1ZnxCproXdftZxX8hP8op4JulpQ_JvIJQ7-6WP2fnY5cDw';
  cy.setCookie('user-service-token', techSupportToken);
});
