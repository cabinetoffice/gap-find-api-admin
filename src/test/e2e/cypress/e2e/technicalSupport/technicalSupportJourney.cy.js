const today = new Date().toLocaleDateString('en-GB', {
  day: 'numeric',
  month: 'long',
  year: 'numeric',
});

const apiKeyName = 'apiKeyNameCypress';
const apiKeyName2 = 'apiKeyNameCypress2';

const BASE_URL = 'http://localhost:8086/find/api/admin';

import { checkNavBarItemIsRightForTheUserRole, signOutIsPresent } from '../../utils/helpers';

describe('Technical support and Admin Roles User journey ', () => {
  beforeEach(() => {
    cy.setMockTokenForUserWithTechnicalSupportAndAdminRoleWithFundingOrganisation1();
  });
  after(() =>
    cy
      .task('drop:apiKeyTable', Cypress.env())
      .then(() => cy.task('create:apiKeyTable', Cypress.env()))
  );

  describe('API key dashboard without keys', () => {
    it('Should render API key dashboard, display no API keys, header and footer', () => {
      cy.visit(`${BASE_URL}/api-keys`);
      cy.get('[data-cy="header"]')
        .should('be.visible')
        .contains('Find a Grant');

      cy.get('[data-cy="beta-banner"]').should('be.visible').contains('BETA');
      signOutIsPresent();

      checkNavBarItemIsRightForTheUserRole('ADMIN');

      cy.get('[data-cy="header-feedback-link"]')
        .should(
          'have.attr',
          'href',
          'https://docs.google.com/forms/d/e/1FAIpQLSd2V0IqOMpb2_yQnz_Ges0WCYFnDOTxZpF299gePV1j8kMdLA/viewform'
        )
        .contains('feedback');

      cy.get('[data-cy="footer"]')
        .should('be.visible')
        .contains(
          'All content is available under the Open Government Licence v3.0, except where otherwise stated'
        );

      cy.get('[data-cy="api-keys-heading"]')
        .should('be.visible')
        .contains('Manage API keys');

      cy.get('[data-cy="api-keys-department"]')
        .should('be.visible')
        .contains('Department');

      cy.get('[data-cy="api-keys-department-name"]')
        .should('be.visible')
        .contains('Test Org');

      cy.get('[data-cy="api-keys-no-api-keys"]')
        .should('be.visible')
        .contains(
          "You don't have any API keys yet. When you create one, you will be able to see a list of your API keys and revoke access to them on this screen."
        );

      cy.get('[data-cy="api-keys-create-button"]')
        .should('be.visible')
        .contains('Create an API key');
    });
  });

  describe('Create an API Key', () => {
    beforeEach(() => {
      cy.visit(`${BASE_URL}/api-keys/create`);
    });

    it('Should render create an API key page', () => {
      cy.visit(`${BASE_URL}/api-keys`);
      cy.get('[data-cy="api-keys-create-button"]')
        .should('be.visible')
        .contains('Create an API key')
        .click();

      cy.url().should('include', 'api-keys/create');

      signOutIsPresent();

      checkNavBarItemIsRightForTheUserRole('ADMIN');

      cy.get('[data-cy="create-key-error-banner"]').should('not.exist');

      cy.get('[data-cy="create-key-heading"]')
        .should('be.visible')
        .contains('Name your API key');

      cy.get('[data-cy="create-key-hint-text"]')
        .should('be.visible')
        .contains(
          'This name will be shown in your list of API keys to make it easier to find. We recommend using the name of the service you will be using the API key to integrate with'
        );

      cy.get('[data-cy="create-key-continue"]')
        .should('be.visible')
        .contains('Continue');

      cy.get('[data-cy="create-key-back-button"]')
        .should('be.visible')
        .should('have.text', 'Back')
        .click();

      cy.get('[data-cy="api-keys-heading"]')
        .should('be.visible')
        .should('have.text', 'Manage API keys');

      cy.get('[data-cy="api-keys-create-button"]')
        .should('be.visible')
        .contains('Create an API key')
        .click();

      cy.url().should('include', '/admin/api-keys/create');
    });

    it('Should display validation errors for blank name', () => {
      cy.get('[data-cy="create-key-continue"]')
        .should('be.visible')
        .contains('Continue')
        .click();

      cy.get('[data-cy="create-key-error-banner-heading"]')
        .should('be.visible')
        .contains('There is a problem');

      cy.get('[data-cy="create-key-error-summary-list"]')
        .should('have.length', 1)
        .contains('Enter a key name');

      cy.get('[data-cy="create-key-input-validation-error-details"]')
        .should('be.visible')
        .should('have.text', 'Enter a key name');
    });

    it('Should display validation errors for blank spaces', () => {
      cy.get('[data-cy="create-key-input"]').type('api key name');

      cy.get('[data-cy="create-key-continue"]')
        .should('be.visible')
        .contains('Continue')
        .click();

      cy.get('[data-cy="create-key-error-banner-heading"]')
        .should('be.visible')
        .contains('There is a problem');

      cy.get('[data-cy="create-key-error-summary-list"]')
        .should('have.length', 1)
        .contains('Key name must be alphanumeric');

      cy.get('[data-cy="create-key-input-validation-error-details"]')
        .should('be.visible')
        .should('have.text', 'Key name must be alphanumeric');
    });

    it('Should successfully create key and display value', () => {
      cy.get('[data-cy="create-key-input"]').type(apiKeyName);

      cy.get('[data-cy="create-key-continue"]')
        .should('be.visible')
        .contains('Continue')
        .click();

      cy.url().should('include', '/admin/api-keys/create');

      signOutIsPresent();

      checkNavBarItemIsRightForTheUserRole('ADMIN');

      cy.get('[data-cy="new-key-heading"]')
        .should('be.visible')
        .should('have.text', 'New API key');

      cy.get('[data-cy="new-key-your-api-key"]')
        .should('be.visible')
        .should('have.text', 'Your API key:');

      cy.get('[data-cy="new-key-value"]').then(($span) => {
        const apiKeyValue = $span.text();
        cy.get('[data-cy="new-key-copy-to-clipboard-button"]')
          .should('be.visible')
          .contains('Copy to clipboard')
          .click();

        cy.window()
          .then((win) => {
            cy.stub(win.navigator.clipboard, 'readText').resolves(apiKeyValue);
          })
          .then(() => {
            cy.window().then((win) => {
              win.navigator.clipboard.readText().then((clipboardContent) => {
                expect(clipboardContent).to.equal(apiKeyValue);
              });
            });
          });
      });

      cy.get('[data-cy="new-key-paragraph"]')
        .should('be.visible')
        .should(
          'have.text',
          'You should keep a copy of this API key somewhere safe.'
        );

      cy.get('[data-cy="new-key-paragraph-2"]')
        .should('be.visible')
        .should(
          'have.text',
          'When you leave this screen, you will not be able to see this API key again.'
        );

      cy.get('[data-cy="new-key-back-button"]')
        .should('be.visible')
        .contains('Back to your API keys')
        .click();

      cy.get('[data-cy="api-keys-heading"]')
        .should('be.visible')
        .should('have.text', 'Manage API keys');
    });

    it('Should display validation errors for existing API key name', () => {
      cy.get('[data-cy="create-key-input"]').type(apiKeyName);

      cy.get('[data-cy="create-key-continue"]')
        .should('be.visible')
        .contains('Continue')
        .click();

      cy.get('[data-cy="create-key-error-banner-heading"]')
        .should('be.visible')
        .contains('There is a problem');

      cy.get('[data-cy="create-key-error-summary-list"]')
        .should('have.length', 1)
        .contains('An API key with this name already exists');

      cy.get('[data-cy="create-key-input-validation-error-details"]')
        .should('be.visible')
        .should('have.text', 'An API key with this name already exists');

      cy.get('[data-cy="create-key-back-button"]')
        .should('be.visible')
        .should('have.text', 'Back')
        .click();
    });
  });

  describe('API key dashboard with keys', () => {
    it('Should render API key dashboard with list of API keys', () => {
      cy.visit(`${BASE_URL}/api-keys`);
      cy.get('[data-cy="api-keys-heading"]')
        .should('be.visible')
        .should('have.text', 'Manage API keys');

      cy.get('[data-cy="api-keys-department"]')
        .should('be.visible')
        .contains('Department');

      cy.get('[data-cy="api-keys-department-name"]')
        .should('be.visible')
        .should('have.text', 'Test Org');

      cy.get('[data-cy="create-key-summary-list"]')
        .children()
        .should('have.length', 1);

      cy.get(`[data-cy="api-key-name-${apiKeyName}"]`)
        .should('be.visible')
        .should('have.text', apiKeyName);

      cy.get(`[data-cy="api-key-created-date-${apiKeyName}"]`)
        .should('be.visible')
        .should('have.text', 'Created ' + today);

      cy.get(`[data-cy="api-key-revoke-${apiKeyName}"]`)
        .should('be.visible')
        .contains('Revoke');

      cy.get('[data-cy="api-keys-create-button"]')
        .should('be.visible')
        .contains('Create an API key');
    });
  });

  describe('Revoke API key', () => {
    it('Should render API key confirmation page and revoke API key', () => {
      cy.visit(`${BASE_URL}/api-keys`);

      cy.get(`[data-cy="api-key-revoke-${apiKeyName}"]`)
        .should('be.visible')
        .contains('Revoke')
        .click();

      cy.get('[data-cy="revoke-heading"]')
        .should('be.visible')
        .should('have.text', 'Revoke an API key');

      signOutIsPresent();

      checkNavBarItemIsRightForTheUserRole('ADMIN');

      cy.get('[data-cy="revoke-paragraph-1"]')
        .should('be.visible')
        .contains(
          'If you revoke this API key, you will no longer be able to use it to request data from Find a grant.'
        );

      cy.get('[data-cy="revoke-paragraph-2"]')
        .should('be.visible')
        .contains(
          'You cannot re-enable a revoked API key. If you want to request data again, you will need to create a new API key.'
        );

      cy.get('[data-cy="revoke-revoke-button"]')
        .should('be.visible')
        .should('have.text', 'Revoke key');

      cy.get('[data-cy="revoke-cancel-button"]')
        .should('be.visible')
        .should('have.text', 'Cancel')
        .click();

      cy.get(`[data-cy="api-key-revoke-${apiKeyName}"]`)
        .should('be.visible')
        .contains('Revoke')
        .click();

      cy.get('[data-cy="revoke-revoke-button"]')
        .should('be.visible')
        .should('have.text', 'Revoke key')
        .click();

      cy.get('[data-cy="create-key-summary-list"]')
        .children()
        .should('have.length', 1);

      cy.get(`[data-cy="api-key-name-${apiKeyName}"]`)
        .should('be.visible')
        .should('have.text', apiKeyName);

      cy.get(`[data-cy="api-key-created-date-${apiKeyName}"]`)
        .should('be.visible')
        .should('have.text', 'Created ' + today);

      cy.get(`[data-cy="api-key-revoked-${apiKeyName}"]`)
        .should('be.visible')
        .should('have.text', 'Revoked ' + today);
    });
    it('Should throw exception when revoking API key', () => {
      cy.visit(`${BASE_URL}/api-keys`);

      cy.get('[data-cy="api-keys-create-button"]')
        .click();

      cy.get('[data-cy="create-key-input"]').type(apiKeyName2);

      cy.get('[data-cy="create-key-continue"]')
        .click();

      cy.get('[data-cy="new-key-back-button"]')
        .click();

      cy.get(`[data-cy="api-key-revoke-${apiKeyName2}"]`)
        .click();

      cy.get('form').within(() => {
        cy.get('[data-cy="revoke-api-key-id"]').type(300, {
          force: true,
        });

        cy.get('[data-cy="revoke-revoke-button"]')
          .click();
      });

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

      cy.get(`[data-cy="api-key-revoke-${apiKeyName2}"]`)
        .should('be.visible')
        .should('have.attr', 'href', '/find/api/admin/api-keys/revoke/2')
        .contains('Revoke');
    });
  });

  describe('Error page', () => {
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
