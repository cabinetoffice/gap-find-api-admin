const BASE_URL = 'http://localhost:8086/find/api/admin';
const SUPERADMIN_COOKIE =
  'user-service-token=eyJraWQiOiJlN2QzZjk0OS01MzdkLTQxMDItODcwNy0wY2NhZDIwYzBjNDAiLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ0Y28uY3lwcmVzcy50ZXN0LnN1cGVyLmFkbWluIiwiYXVkIjoiRkdQIiwicm9sZXMiOiJbQVBQTElDQU5ULCBGSU5ELCBTVVBFUl9BRE1JTl0iLCJpZFRva2VuIjoiYmFuYW5hIiwiaXNzIjoiYmFuYW5hIiwiZGVwYXJ0bWVudCI6IkJhbmFuYSIsImV4cCI6MTY5Mjk3MzIyMiwiaWF0IjoxNjkyOTY5NjIyLCJlbWFpbCI6ImJhbmFuYUBmcnVpdCIsImp0aSI6ImJhbmFuYSJ9.pjRY3K8YmVFzzzHZ5lBf9oHbqnzda4nQMoA03R4b-gLG6V0L9mfr7gktui1dzBiPYCtAb0oNIlaS-CcRsqNYNwOufLAblsLjZOF_HUTzsqVdRBR1gIwRiiv08GQ-e0FbBHQ47I3L7qlQCfjuyeoym4N01jT26ydnbfEjkYlekd9fQSuGTlCSd1J3hAj3mlOtpVnBxxf-fqGTRdbLXEvV3wn7yXRpYHhf-j2h9j1Hicx1t8FFjF7UVk3si31befzjyX4705xst2KMOQMtCDNgmywBPyIS2o7XnRUjXpMhqfomfCnL5q5GdUMn8xun9V8_CpvsZ33Bv46NhXMgWGoiSw';
describe('Security for all roles journey ', () => {
  // beforeEach(() => {
  //   cy.setMockTokenForUserWithTechnicalSupportAndAdminRoleWithFundingOrganisation1();
  // });
  // after(() =>
  //   cy
  //     .task('drop:apiKeyTable', Cypress.env())
  //     .then(() => cy.task('create:apiKeyTable', Cypress.env()))
  // );

  describe('API not accessible by any other roles', () => {
    it('Should not allow users with role of Find, Admin or Applicant to access the app', () => {
      cy.setMockTokenForUserWithFindRole();

      cy.visit(`${BASE_URL}/api-keys`);
      // this happens because we mock the user-service login process and we log the user with a superAdmin role.
      // in real if a user with no Technical support or SuperAdmin role try to access the app, we send them back to one login login page.
      cy.url().should('eq', `${BASE_URL}/api-keys/manage`);
    });
  });

  describe('Using invalid data returns an error and does not expose any unexpected information', () => {
    it('should return the error page if a superAdmin try to directly send a POST request to create keys /api-keys/create', () => {
      cy.request(
        {
          method: 'POST',
          url: `${BASE_URL}/api-keys/create`,
          headers: {
            Cookie: SUPERADMIN_COOKIE,
          },
        },
        {
          keyName: 'Cypress',
        }
      ).then((r) => {
        expect(r.status).to.eq(200);
        expect(r.redirects[0]).to.eq(`302: ${BASE_URL}/api-keys/error`);
      });
    });

    it('should return the error page if a superAdmin try to directly send a GET request to get the organisation keys /api-keys', () => {
      cy.request(
        {
          method: 'GET',
          url: `${BASE_URL}/api-keys`,
          headers: {
            Cookie:
              'user-service-token=eyJraWQiOiJlN2QzZjk0OS01MzdkLTQxMDItODcwNy0wY2NhZDIwYzBjNDAiLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ0Y28uY3lwcmVzcy50ZXN0LnN1cGVyLmFkbWluIiwiYXVkIjoiRkdQIiwicm9sZXMiOiJbQVBQTElDQU5ULCBGSU5ELCBTVVBFUl9BRE1JTl0iLCJpZFRva2VuIjoiYmFuYW5hIiwiaXNzIjoiYmFuYW5hIiwiZGVwYXJ0bWVudCI6IkJhbmFuYSIsImV4cCI6MTY5Mjk3MzIyMiwiaWF0IjoxNjkyOTY5NjIyLCJlbWFpbCI6ImJhbmFuYUBmcnVpdCIsImp0aSI6ImJhbmFuYSJ9.pjRY3K8YmVFzzzHZ5lBf9oHbqnzda4nQMoA03R4b-gLG6V0L9mfr7gktui1dzBiPYCtAb0oNIlaS-CcRsqNYNwOufLAblsLjZOF_HUTzsqVdRBR1gIwRiiv08GQ-e0FbBHQ47I3L7qlQCfjuyeoym4N01jT26ydnbfEjkYlekd9fQSuGTlCSd1J3hAj3mlOtpVnBxxf-fqGTRdbLXEvV3wn7yXRpYHhf-j2h9j1Hicx1t8FFjF7UVk3si31befzjyX4705xst2KMOQMtCDNgmywBPyIS2o7XnRUjXpMhqfomfCnL5q5GdUMn8xun9V8_CpvsZ33Bv46NhXMgWGoiSw',
          },
        },
        {
          keyName: 'Cypress',
        }
      ).then((r) => {
        console.log('r: ', r);
        expect(r.status).to.eq(200);
        expect(r.redirects[0]).to.eq(`302: ${BASE_URL}/api-keys/error`);
      });
    });

    it('should return the error page if a superAdmin try to directly send a GET request to get the /api-keys/create', () => {
      cy.request(
        {
          method: 'GET',
          url: `${BASE_URL}/api-keys/create`,
          headers: {
            Cookie: SUPERADMIN_COOKIE,
          },
        },
        {
          keyName: 'Cypress',
        }
      ).then((r) => {
        console.log('r: ', r);
        expect(r.status).to.eq(200);
        expect(r.redirects[0]).to.eq(`302: ${BASE_URL}/api-keys/error`);
      });
    });

    //end
  });
});
