import {
  checkFirst10RowsContent,
  createKeyForFundingOrganisation1,
  createKeyForFundingOrganisation2,
  paginationItemHasCurrentAsCssClass,
  paginationItemHasNotCurrentAsCssClass,
  paginationItemsDoesNotExist,
  paginationLinkHasTheRightHref,
  paginationNextItemHasTheRightHref,
  paginationPreviousItemHasTheRightHref,
} from '../../utils/superAdminHelpers';

const today = new Date().toLocaleDateString('en-GB', {
  day: 'numeric',
  month: 'long',
  year: 'numeric',
});

const BASE_URL = 'http://localhost:8080/find/api/admin';

describe('Super Admin Journey', () => {
  after(() => cy.task('drop:apiKeyTable', Cypress.env()).then(() => cy.task('create:apiKeyTable', Cypress.env())));
  it('should show No keys message when no apiKeys are present', () => {
    cy.setMockTokenForSuperAdmin();
    cy.visit(`${BASE_URL}/api-keys/manage`);
    cy.get('[data-cy="header"]').should('be.visible');
    cy.get(`[data-cy="beta-banner"]`).should('be.visible');
    cy.get(`[data-cy="admin-dashboard-heading"]`).should('be.visible').should('have.text', 'Manage API keys');
    cy.get(`[data-cy="admin-dashboard-active-key-count"]`)
      .should('be.visible')
      .should('have.text', '0 active API keys');
    cy.get(`[data-cy="admin-dashboard-no-api-key-paragraph"]`)
      .should('be.visible')
      .should(
        'have.text',
        'No one has any API keys yet. When one is created, you will be able to see a list of all API keys, the department it belongs, when it was created and revoke access to them on this screen.'
      );
    cy.get(`[data-cy="admin-dashboard-pagination-bar"]`).should('not.exist');

    cy.get(`[data-cy="footer"]`).should('be.visible');
  });

  it('creates keys for each department for superAdmin test', () => {
    createKeyForFundingOrganisation1(BASE_URL, 0, 45);
    createKeyForFundingOrganisation2(BASE_URL, 45, 110);
  });

  it('should show 110 keys and 2 organisation in the filters and the pagination bar', () => {
    cy.setMockTokenForSuperAdmin();
    cy.visit(`${BASE_URL}/api-keys/manage`);

    //Check filter side
    cy.get(`[data-cy="admin-dashboard-heading"]`).should('be.visible').should('have.text', 'Manage API keys');
    cy.get(`[data-cy="admin-dashboard-active-key-count"]`)
      .should('be.visible')
      .should('have.text', '110 active API keys');
    cy.get('[data-cy="admin-dashboard-filter-departments-div"]')
      .should('be.visible')
      .find('input[type="checkbox"]')
      .should('have.length', 2);
    cy.get(`[data-cy="admin-dashboard-filter-Evil Org-checkbox"]`).should('not.be.checked');
    cy.get(`[data-cy="admin-dashboard-filter-Evil Org-label"]`).should('have.text', 'Evil Org');
    cy.get(`[data-cy="admin-dashboard-filter-Test Org-checkbox"]`).should('not.be.checked');
    cy.get(`[data-cy="admin-dashboard-filter-Test Org-label"]`).should('have.text', 'Test Org');
    cy.get(`[data-cy="admin-dashboard-filter-apply-button"]`).should('be.visible');
    cy.get(`[data-cy="admin-dashboard-filter-clear-button"]`).should('be.visible');
    //check table side

    //headers
    cy.get('[data-cy="admin-dashboard-list-table-headers"]').should('be.visible').find('th').should('have.length', 4);
    cy.get(`[data-cy="admin-dashboard-list-table-API-key-header"]`).should('have.text', 'API key');
    cy.get(`[data-cy="admin-dashboard-list-table-Department-header"]`).should('have.text', 'Department');
    cy.get(`[data-cy="admin-dashboard-list-table-Created-header"]`).should('have.text', 'Created');
    cy.get(`[data-cy="admin-dashboard-list-table-Revoke-header"]`).should('have.text', 'Revoke');

    //rows
    cy.get(`[data-cy="admin-dashboard-list-table-body"]`).should('be.visible').find('tr').should('have.length', 10);
    //first 10 single rows content
    checkFirst10RowsContent(today);
    //pagination (further test below)
    cy.get('[data-cy="admin-dashboard-pagination-bar"]').should('exist');
    cy.get(`[data-cy="admin-dashboard-show-keys-count-paragraph"]`).should('have.text', 'Showing 1 to 10 of 110 keys');
  });

  it('should show only the filtered keys when department filters are set, and clear all filters should reset those filter', () => {
    cy.setMockTokenForSuperAdmin();
    cy.visit(`${BASE_URL}/api-keys/manage`);

    //select org1
    cy.get(`[data-cy="admin-dashboard-filter-Test Org-checkbox"]`).click();
    cy.get(`[data-cy="admin-dashboard-filter-Test Org-checkbox"]`).should('be.checked');
    cy.get(`[data-cy="admin-dashboard-filter-apply-button"]`).click();

    cy.url().should('eq', `${BASE_URL}/api-keys/manage?selectedDepartments=Test+Org`);
    cy.get(`[data-cy="admin-dashboard-filter-Test Org-checkbox"]`).should('be.checked');
    //rows
    cy.get(`[data-cy="admin-dashboard-list-table-body"]`).should('be.visible').find('tr').should('have.length', 10);
    //pagination(when less than 5 pages, no ellipses and all the pages showing in the pagination bar)
    cy.get(`[data-cy="admin-dashboard-pagination-ellipses"]`).should('not.exist');
    paginationLinkHasTheRightHref(1, 'Test Org');
    paginationLinkHasTheRightHref(2, 'Test Org');
    paginationLinkHasTheRightHref(3, 'Test Org');
    paginationLinkHasTheRightHref(4, 'Test Org');
    paginationLinkHasTheRightHref(5, 'Test Org');
    paginationNextItemHasTheRightHref(2, 'Test Org');
    cy.get(`[data-cy="admin-dashboard-show-keys-count-paragraph"]`).should('have.text', 'Showing 1 to 10 of 45 keys');

    //select org2
    cy.get(`[data-cy="admin-dashboard-filter-Evil Org-checkbox"]`).click();
    cy.get(`[data-cy="admin-dashboard-filter-Evil Org-checkbox"]`).should('be.checked');
    cy.get(`[data-cy="admin-dashboard-filter-apply-button"]`).click();

    cy.url().should('eq', '${/api-keys/manage?selectedDepartments=Evil+Org&selectedDepartments=Test+Org');
    //rows
    cy.get(`[data-cy="admin-dashboard-list-table-body"]`).should('be.visible').find('tr').should('have.length', 10);
    //pagination
    cy.get(`[data-cy="admin-dashboard-show-keys-count-paragraph"]`).should('have.text', 'Showing 1 to 10 of 110 keys');

    //press clear all filter
    cy.get(`[data-cy="admin-dashboard-filter-clear-button"]`).click();

    cy.url().should('eq', `${BASE_URL}/api-keys/manage`);
    cy.get(`[data-cy="admin-dashboard-filter-Test Org-checkbox"]`).should('not.be.checked');
    cy.get(`[data-cy="admin-dashboard-filter-Evil Org-checkbox"]`).should('not.be.checked');
    //rows
    cy.get(`[data-cy="admin-dashboard-list-table-body"]`).should('be.visible').find('tr').should('have.length', 10);
    //pagination
    cy.get(`[data-cy="admin-dashboard-show-keys-count-paragraph"]`).should('have.text', 'Showing 1 to 10 of 110 keys');
  });

  //pagination tests

  it('should show the correct pagination when going to next pages ', () => {
    cy.setMockTokenForSuperAdmin();
    cy.visit(`${BASE_URL}/api-keys/manage`);
    paginationItemHasCurrentAsCssClass(1);
    paginationLinkHasTheRightHref(1);
    paginationLinkHasTheRightHref(2);
    paginationItemHasNotCurrentAsCssClass(2);
    cy.get(`[data-cy="admin-dashboard-pagination-ellipses"]`).should('exist');
    paginationLinkHasTheRightHref(11);
    paginationItemHasNotCurrentAsCssClass(11);
    paginationNextItemHasTheRightHref(2);
    paginationItemsDoesNotExist([3, 4, 5, 6, 7, 8, 9, 10]);

    //go to page 2
    cy.get(`[data-cy="admin-dashboard-pagination-page-2-link"]`).click();
    cy.url().should('eq', `${BASE_URL}/api-keys/manage?page=2`);
    paginationPreviousItemHasTheRightHref(1);
    paginationLinkHasTheRightHref(1);
    paginationItemHasNotCurrentAsCssClass(1);
    paginationItemHasCurrentAsCssClass(2);
    paginationLinkHasTheRightHref(2);
    paginationLinkHasTheRightHref(3);
    paginationItemHasNotCurrentAsCssClass(3);
    cy.get(`[data-cy="admin-dashboard-pagination-ellipses"]`).should('exist');
    paginationLinkHasTheRightHref(11);
    paginationItemHasNotCurrentAsCssClass(11);
    paginationNextItemHasTheRightHref(3);
    paginationItemsDoesNotExist([4, 5, 6, 7, 8, 9, 10]);
    cy.get(`[data-cy="admin-dashboard-show-keys-count-paragraph"]`).should('have.text', 'Showing 11 to 20 of 110 keys');

    //go to page 3
    cy.get(`[data-cy="admin-dashboard-pagination-page-3-link"]`).click();
    cy.url().should('eq', `${BASE_URL}/api-keys/manage?page=3`);
    paginationPreviousItemHasTheRightHref(2);
    paginationLinkHasTheRightHref(1);
    paginationItemHasNotCurrentAsCssClass(1);
    paginationLinkHasTheRightHref(2);
    paginationItemHasNotCurrentAsCssClass(2);
    paginationItemHasCurrentAsCssClass(3);
    paginationLinkHasTheRightHref(3);
    paginationLinkHasTheRightHref(4);
    paginationItemHasNotCurrentAsCssClass(4);
    cy.get(`[data-cy="admin-dashboard-pagination-ellipses"]`).should('exist');
    paginationLinkHasTheRightHref(11);
    paginationItemHasNotCurrentAsCssClass(11);
    paginationNextItemHasTheRightHref(4);
    paginationItemsDoesNotExist([5, 6, 7, 8, 9, 10]);
    cy.get(`[data-cy="admin-dashboard-show-keys-count-paragraph"]`).should('have.text', 'Showing 21 to 30 of 110 keys');

    //go to page 4
    cy.get(`[data-cy="admin-dashboard-pagination-page-4-link"]`).click();
    cy.url().should('eq', `${BASE_URL}/api-keys/manage?page=4`);
    paginationPreviousItemHasTheRightHref(3);
    paginationLinkHasTheRightHref(1);
    paginationItemHasNotCurrentAsCssClass(1);
    paginationLinkHasTheRightHref(2);
    paginationItemHasNotCurrentAsCssClass(2);
    paginationLinkHasTheRightHref(3);
    paginationItemHasNotCurrentAsCssClass(3);
    paginationItemHasCurrentAsCssClass(4);
    paginationLinkHasTheRightHref(4);
    paginationLinkHasTheRightHref(5);
    paginationItemHasNotCurrentAsCssClass(5);
    cy.get(`[data-cy="admin-dashboard-pagination-ellipses"]`).should('exist');
    paginationLinkHasTheRightHref(11);
    paginationItemHasNotCurrentAsCssClass(11);
    paginationNextItemHasTheRightHref(5);
    paginationItemsDoesNotExist([6, 7, 8, 9, 10]);
    cy.get(`[data-cy="admin-dashboard-show-keys-count-paragraph"]`).should('have.text', 'Showing 31 to 40 of 110 keys');

    //go to page 5
    cy.get(`[data-cy="admin-dashboard-pagination-page-5-link"]`).click();
    cy.url().should('eq', `${BASE_URL}/api-keys/manage?page=5`);
    paginationPreviousItemHasTheRightHref(4);
    paginationLinkHasTheRightHref(1);
    paginationItemHasNotCurrentAsCssClass(1);
    paginationLinkHasTheRightHref(4);
    paginationItemHasNotCurrentAsCssClass(4);
    paginationItemHasCurrentAsCssClass(5);
    paginationLinkHasTheRightHref(5);
    paginationLinkHasTheRightHref(6);
    paginationItemHasNotCurrentAsCssClass(6);
    cy.get(`[data-cy="admin-dashboard-pagination-ellipses"]`).should('exist');
    paginationLinkHasTheRightHref(11);
    paginationItemHasNotCurrentAsCssClass(11);
    paginationNextItemHasTheRightHref(6);
    paginationItemsDoesNotExist([2, 3, 7, 8, 9, 10]);
    cy.get(`[data-cy="admin-dashboard-show-keys-count-paragraph"]`).should('have.text', 'Showing 41 to 50 of 110 keys');

    //go to page 6
    cy.get(`[data-cy="admin-dashboard-pagination-page-6-link"]`).click();
    cy.url().should('eq', `${BASE_URL}/api-keys/manage?page=6`);
    paginationPreviousItemHasTheRightHref(5);
    paginationLinkHasTheRightHref(1);
    paginationItemHasNotCurrentAsCssClass(1);
    paginationLinkHasTheRightHref(5);
    paginationItemHasNotCurrentAsCssClass(5);
    paginationItemHasCurrentAsCssClass(6);
    paginationLinkHasTheRightHref(6);
    paginationLinkHasTheRightHref(7);
    paginationItemHasNotCurrentAsCssClass(7);
    cy.get(`[data-cy="admin-dashboard-pagination-ellipses"]`).should('exist');
    paginationLinkHasTheRightHref(11);
    paginationItemHasNotCurrentAsCssClass(11);
    paginationNextItemHasTheRightHref(7);
    paginationItemsDoesNotExist([2, 3, 4, 8, 9, 10]);
    cy.get(`[data-cy="admin-dashboard-show-keys-count-paragraph"]`).should('have.text', 'Showing 51 to 60 of 110 keys');

    //go to page 7
    cy.get(`[data-cy="admin-dashboard-pagination-page-7-link"]`).click();
    cy.url().should('eq', `${BASE_URL}/api-keys/manage?page=7`);
    paginationPreviousItemHasTheRightHref(6);
    paginationLinkHasTheRightHref(1);
    paginationItemHasNotCurrentAsCssClass(1);
    paginationLinkHasTheRightHref(6);
    paginationItemHasNotCurrentAsCssClass(6);
    paginationItemHasCurrentAsCssClass(7);
    paginationLinkHasTheRightHref(7);
    paginationLinkHasTheRightHref(8);
    paginationItemHasNotCurrentAsCssClass(8);
    cy.get(`[data-cy="admin-dashboard-pagination-ellipses"]`).should('exist');
    paginationLinkHasTheRightHref(11);
    paginationItemHasNotCurrentAsCssClass(11);
    paginationNextItemHasTheRightHref(8);
    paginationItemsDoesNotExist([2, 3, 4, 5, 9, 10]);
    cy.get(`[data-cy="admin-dashboard-show-keys-count-paragraph"]`).should('have.text', 'Showing 61 to 70 of 110 keys');

    //go to page 8
    cy.get(`[data-cy="admin-dashboard-pagination-page-8-link"]`).click();
    cy.url().should('eq', `${BASE_URL}/api-keys/manage?page=8`);
    paginationPreviousItemHasTheRightHref(7);
    paginationLinkHasTheRightHref(1);
    paginationItemHasNotCurrentAsCssClass(1);
    paginationLinkHasTheRightHref(7);
    paginationItemHasNotCurrentAsCssClass(7);
    paginationItemHasCurrentAsCssClass(8);
    paginationLinkHasTheRightHref(8);
    paginationLinkHasTheRightHref(9);
    paginationItemHasNotCurrentAsCssClass(9);
    cy.get(`[data-cy="admin-dashboard-pagination-ellipses"]`).should('exist');
    paginationLinkHasTheRightHref(11);
    paginationItemHasNotCurrentAsCssClass(11);
    paginationNextItemHasTheRightHref(9);
    paginationItemsDoesNotExist([2, 3, 4, 5, 6, 10]);
    cy.get(`[data-cy="admin-dashboard-show-keys-count-paragraph"]`).should('have.text', 'Showing 71 to 80 of 110 keys');

    //go to page 9
    cy.get(`[data-cy="admin-dashboard-pagination-page-9-link"]`).click();
    cy.url().should('eq', `${BASE_URL}/api-keys/manage?page=9`);
    paginationPreviousItemHasTheRightHref(8);
    paginationLinkHasTheRightHref(1);
    paginationItemHasNotCurrentAsCssClass(1);
    cy.get(`[data-cy="admin-dashboard-pagination-ellipses"]`).should('exist');
    paginationLinkHasTheRightHref(8);
    paginationItemHasNotCurrentAsCssClass(8);
    paginationItemHasCurrentAsCssClass(9);
    paginationLinkHasTheRightHref(9);
    paginationLinkHasTheRightHref(10);
    paginationItemHasNotCurrentAsCssClass(10);
    paginationLinkHasTheRightHref(11);
    paginationItemHasNotCurrentAsCssClass(11);
    paginationNextItemHasTheRightHref(10);
    paginationItemsDoesNotExist([2, 3, 4, 5, 6, 7]);
    cy.get(`[data-cy="admin-dashboard-show-keys-count-paragraph"]`).should('have.text', 'Showing 81 to 90 of 110 keys');

    //go to page 10
    cy.get(`[data-cy="admin-dashboard-pagination-page-10-link"]`).click();
    cy.url().should('eq', `${BASE_URL}/api-keys/manage?page=10`);
    paginationPreviousItemHasTheRightHref(9);
    paginationLinkHasTheRightHref(1);
    paginationItemHasNotCurrentAsCssClass(1);
    cy.get(`[data-cy="admin-dashboard-pagination-ellipses"]`).should('exist');
    paginationLinkHasTheRightHref(9);
    paginationItemHasNotCurrentAsCssClass(9);
    paginationItemHasCurrentAsCssClass(10);
    paginationLinkHasTheRightHref(10);
    paginationLinkHasTheRightHref(11);
    paginationItemHasNotCurrentAsCssClass(11);
    paginationNextItemHasTheRightHref(11);
    paginationItemsDoesNotExist([2, 3, 4, 5, 6, 7, 8]);
    cy.get(`[data-cy="admin-dashboard-show-keys-count-paragraph"]`).should(
      'have.text',
      'Showing 91 to 100 of 110 keys'
    );
    //go to page 11
    cy.get(`[data-cy="admin-dashboard-pagination-page-11-link"]`).click();
    cy.url().should('eq', `${BASE_URL}/api-keys/manage?page=11`);
    paginationPreviousItemHasTheRightHref(10);
    paginationLinkHasTheRightHref(1);
    paginationItemHasNotCurrentAsCssClass(1);
    cy.get(`[data-cy="admin-dashboard-pagination-ellipses"]`).should('exist');
    paginationLinkHasTheRightHref(10);
    paginationItemHasNotCurrentAsCssClass(10);
    paginationItemHasCurrentAsCssClass(11);
    paginationLinkHasTheRightHref(11);
    cy.get(`[data-cy="admin-dashboard-pagination-next-page-link"]`).should('not.exist');
    paginationItemsDoesNotExist([2, 3, 4, 5, 6, 7, 8, 9]);
    cy.get(`[data-cy="admin-dashboard-show-keys-count-paragraph"]`).should(
      'have.text',
      'Showing 101 to 110 of 110 keys'
    );
  });

  it('should be able to revoke any key', () => {
    cy.setMockTokenForSuperAdmin();
    cy.visit(`${BASE_URL}/api-keys/manage`);

    cy.get(`[data-cy="admin-dashboard-list-table-row-Revoked-Org1Cypress005-link"]`).click();
    cy.url().should('include', '${/api-keys/revoke/');
    //test cancel button
    cy.get(`[data-cy="revoke-cancel-button"]`).click();
    cy.url().should('eq', `${BASE_URL}/api-keys/manage`);

    cy.get(`[data-cy="admin-dashboard-list-table-row-Revoked-Org1Cypress009-link"]`).click();
    cy.url().should('include', '${/api-keys/revoke/');
    //test revoke button
    cy.get(`[data-cy="revoke-revoke-button"]`).click();
    cy.url().should('eq', `${BASE_URL}/api-keys/manage`);
    //once revoked the revoked key will be at last page
    cy.get(`[data-cy="admin-dashboard-pagination-page-11-link"]`).click();
    cy.get(`[data-cy="admin-dashboard-list-table-row-Revoked-Org1Cypress009-link"]`).should('not.exist');
    cy.get(`[data-cy="admin-dashboard-list-table-row-API-key-Org1Cypress009"]`).should('have.text', `Org1Cypress009`);
    cy.get(`[data-cy="admin-dashboard-list-table-row-Department-Org1Cypress009"]`).should('have.text', 'Test Org');
    cy.get(`[data-cy="admin-dashboard-list-table-row-Created-Org1Cypress009"]`).should('have.text', today);
    cy.get(`[data-cy="admin-dashboard-list-table-row-Revoked-Org1Cypress009-date"]`).should('contain', today);
    cy.get(`[data-cy="admin-dashboard-active-key-count"]`)
      .should('be.visible')
      .should('have.text', '109 active API keys');
  });

  it('Show error page when trying to access Technical support create api key page', () => {
    cy.setMockTokenForSuperAdmin();
    cy.visit(`${BASE_URL}/api-keys/create`);
    cy.url().should('eq', `${BASE_URL}/api-keys/error`);
    cy.get(`[data-cy='error-heading']`).should('have.text', 'Something went wrong');
    cy.get(`[data-cy='error-paragraph']`).should(
      'have.text',
      'Something went wrong while trying to complete your request.'
    );
    cy.get(`[data-cy='error-back-link']`).should('have.attr', 'href', '/find/api/admin/api-keys/manage');
  });

  it('Show error page when trying to access Technical support dashboard page', () => {
    cy.setMockTokenForSuperAdmin();
    cy.visit(`${BASE_URL}/api-keys`);
    cy.url().should('eq', `${BASE_URL}/api-keys/error`);
    cy.get(`[data-cy='error-heading']`).should('have.text', 'Something went wrong');
    cy.get(`[data-cy='error-paragraph']`).should(
      'have.text',
      'Something went wrong while trying to complete your request.'
    );
    cy.get(`[data-cy='error-back-link']`).should('have.attr', 'href', '/find/api/admin/api-keys/manage');
  });
});
