const BASE_URL = 'http://localhost:8086/find/api/admin';

describe('Security for all roles journey ', () => {
  // beforeEach(() => {
  //   cy.setMockTokenForUserWithTechnicalSupportAndAdminRoleWithFundingOrganisation1();
  // });
  // after(() =>
  //   cy
  //     .task('drop:apiKeyTable', Cypress.env())
  //     .then(() => cy.task('create:apiKeyTable', Cypress.env()))
  // );

  describe.skip('API not accessible by any other roles', () => {
    it('Should not allow users with role of Find, Admin or Applicant to access the app', () => {
      cy.setMockTokenForUserWithFindRole();

      cy.visit(`${BASE_URL}/api-keys`);

      cy.url().should('eq', `${BASE_URL}/api-keys/manage`);
    });
  });

  describe.skip('Error page', () => {
    it('Should render error page when trying to access super admin dashboard', () => {
      cy.visit(`${BASE_URL}/api-keys/manage`);

      cy.get('[data-cy="error-heading"]')
        .should('be.visible')
        .should('have.text', 'Something went wrong');

      cy.get('[data-cy="error-paragraph"]')
        .should('be.visible')
        .should(
          'have.text',
          'Something went wrong while trying to complete your request.'
        );

      cy.get('[data-cy="error-paragraph-with-link"]')
        .should('be.visible')
        .contains('You can return to the API dashboard to try again.');

      cy.get('[data-cy="error-back-link"]')
        .should('have.attr', 'href', '/find/api/admin/api-keys')
        .contains('return to the API dashboard')
        .click();

      cy.get('[data-cy="api-keys-heading"]')
        .should('be.visible')
        .should('have.text', 'Manage API keys');
    });
  });
});
