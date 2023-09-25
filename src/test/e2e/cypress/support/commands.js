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
    'eyJraWQiOiJlN2QzZjk0OS01MzdkLTQxMDItODcwNy0wY2NhZDIwYzBjNDAiLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ0Y28uY3lwcmVzcy50ZXN0LnN1cGVyLmFkbWluIiwiYXVkIjoiRkdQIiwicm9sZXMiOiJbQVBQTElDQU5ULCBGSU5ELCBTVVBFUl9BRE1JTl0iLCJpZFRva2VuIjoiYmFuYW5hIiwiaXNzIjoiYmFuYW5hIiwiZGVwYXJ0bWVudCI6IkJhbmFuYSIsImV4cCI6MTY5Mjk3MzIyMiwiaWF0IjoxNjkyOTY5NjIyLCJlbWFpbCI6ImJhbmFuYUBmcnVpdCIsImp0aSI6ImJhbmFuYSJ9.pjRY3K8YmVFzzzHZ5lBf9oHbqnzda4nQMoA03R4b-gLG6V0L9mfr7gktui1dzBiPYCtAb0oNIlaS-CcRsqNYNwOufLAblsLjZOF_HUTzsqVdRBR1gIwRiiv08GQ-e0FbBHQ47I3L7qlQCfjuyeoym4N01jT26ydnbfEjkYlekd9fQSuGTlCSd1J3hAj3mlOtpVnBxxf-fqGTRdbLXEvV3wn7yXRpYHhf-j2h9j1Hicx1t8FFjF7UVk3si31befzjyX4705xst2KMOQMtCDNgmywBPyIS2o7XnRUjXpMhqfomfCnL5q5GdUMn8xun9V8_CpvsZ33Bv46NhXMgWGoiSw';
  cy.setCookie('user-service-token', superAdminToken);
});

Cypress.Commands.add('setMockTokenForUserWithTechnicalSupportAndAdminRoleWithFundingOrganisation1', () => {
  const techSupportToken =
    'eyJraWQiOiJlN2QzZjk0OS01MzdkLTQxMDItODcwNy0wY2NhZDIwYzBjNDAiLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ0Y28uY3lwcmVzcy50ZXN0LnRlY2huaWNhbC5zdXBwb3J0IiwiYXVkIjoiRkdQIiwicm9sZXMiOiJbQVBQTElDQU5ULCBGSU5ELCBURUNITklDQUxfU1VQUE9SVCwgQURNSU5dIiwiaWRUb2tlbiI6ImJhbmFuYSIsImlzcyI6ImJhbmFuYSIsImRlcGFydG1lbnQiOiJCYW5hbmEiLCJleHAiOjE2OTI5NzMyMjIsImlhdCI6MTY5Mjk2OTYyMiwiZW1haWwiOiJiYW5hbmFAZnJ1aXQiLCJqdGkiOiJiYW5hbmEifQ.j-cEvTmKXoVllMyM_vIkcrj1yne0yNgjKjAKqY5aqQfjqKBiw5OK9WRzeJi4_omdKE4JGuc3oRPxcYbxCs90BGdLPARL6CDoAj39Ts3WA6YBUVB7s7XTWpWzG4JPuEIQBvIjqzA0i0XchfmecbxQaKah22DmeBeRXnwDyHOLXDnQuEb8U3IQRrimFVQZjJP5FMh9vqkZGjE7ryc8IwDKz2Puo0EhNYkDKs-_8Gc37j0AMoB_yIeIjO_dYwlajOvWo35xmzSLI1JbSmTsJhFgNKiQjxk1EQmTdl_ugLACaM-pL5qb3qsBGx5cMlisDK3R5Zz9JQhoD3ez-b_PylmxEg';
  cy.setCookie('user-service-token', techSupportToken);
});

Cypress.Commands.add('setMockTokenForUserWithTechnicalSupportRoleWithFundingOrganisation2', () => {
  const techSupportToken =
    'eyJraWQiOiJlN2QzZjk0OS01MzdkLTQxMDItODcwNy0wY2NhZDIwYzBjNDAiLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ0Y28uY3lwcmVzcy50ZXN0LnRlY2huaWNhbC5zdXBwb3J0LjIiLCJhdWQiOiJGR1AiLCJyb2xlcyI6IltBUFBMSUNBTlQsIEZJTkQsIFRFQ0hOSUNBTF9TVVBQT1JUXSIsImlkVG9rZW4iOiJiYW5hbmEiLCJpc3MiOiJiYW5hbmEiLCJkZXBhcnRtZW50IjoiQmFuYW5hIiwiZXhwIjoxNjkyOTczMjIyLCJpYXQiOjE2OTI5Njk2MjIsImVtYWlsIjoiYmFuYW5hQGZydWl0IiwianRpIjoiYmFuYW5hIn0.JoQEBfLB4zNtWBEFYAMq6nTYKMuSKMDZSZAcYLkzhsQ-IeCigqaohE-MCLSlnYnkH3l_BQivJ06ntl91C5oQFE2iFt8HdL7q7j7viRlrPLyAxy9De4LsSy96tgkHrOP5FU-FUI-Lh1HUCF5gghrVoD3jRn3ksdKTjh9vUA0WJ61IJSmdyLRaAX3IDIxjeEGHjSTO9-V4PjPyQv88sTey6jNsdbbGsUga7zMLMSFDGSEihd31h1-noP6rH7nlhvioj9zh5QzTJx_OXqcq_rtp9ibVRAZPDLkOBN2fkQgz1ZnxCproXdftZxX8hP8op4JulpQ_JvIJQ7-6WP2fnY5cDw';
  cy.setCookie('user-service-token', techSupportToken);
});
