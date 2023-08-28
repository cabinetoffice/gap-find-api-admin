function paginationNextItemHasTheRightHref(expectedPageNumber, filteredDepartmentName) {
  cy.get(`[data-cy="admin-dashboard-pagination-next-page-link"]`).should(
    'have.attr',
    'href',
    `/find/api/admin/api-keys/manage?page=${expectedPageNumber}${
      !!filteredDepartmentName ? '&selectedDepartments=' + filteredDepartmentName.replace(' ', '%20') : ''
    }`
  );
}
function paginationPreviousItemHasTheRightHref(expectedPageNumber) {
  cy.get(`[data-cy="admin-dashboard-pagination-previous-page-link"]`).should(
    'have.attr',
    'href',
    `/find/api/admin/api-keys/manage?page=${expectedPageNumber}`
  );
}

function paginationItemHasCurrentAsCssClass(pageNumber) {
  cy.get(`[data-cy="admin-dashboard-pagination-page-${pageNumber}-item"]`).should(
    'have.class',
    'govuk-pagination__item--current'
  );
}

function paginationItemsDoesNotExist(pageNumbers) {
  pageNumbers.forEach((pageNumber) =>
    cy.get(`[data-cy="admin-dashboard-pagination-page-${pageNumber}-item"]`).should('not.exist')
  );
}

function paginationItemHasNotCurrentAsCssClass(pageNumber) {
  cy.get(`[data-cy="admin-dashboard-pagination-page-${pageNumber}-item"]`).should(
    'not.have.class',
    'govuk-pagination__item--current'
  );
}

function paginationLinkHasTheRightHref(pageNumber, filteredDepartmentName) {
  cy.get(`[data-cy="admin-dashboard-pagination-page-${pageNumber}-link"]`).should(
    'have.attr',
    'href',
    `/find/api/admin/api-keys/manage?page=${pageNumber}${
      !!filteredDepartmentName ? '&selectedDepartments=' + filteredDepartmentName.replace(' ', '%20') : ''
    }`
  );
}

function checkFirst10RowsContent(today) {
  for (let i = 0; i < 10; i++) {
    const index = i + 1;
    cy.get(`[data-cy="admin-dashboard-list-table-row-API-key-Org1Cypress${index}"]`).should(
      'have.text',
      `Org1Cypress${index}`
    );
    cy.get(`[data-cy="admin-dashboard-list-table-row-Department-Org1Cypress${index}"]`).should('have.text', 'Test Org');
    cy.get(`[data-cy="admin-dashboard-list-table-row-Created-Org1Cypress${index}"]`).should('have.text', today);
    cy.get(`[data-cy="admin-dashboard-list-table-row-Revoked-Org1Cypress${index}-link"]`).should('contain', 'Revoke');
  }
}

function createKeyForFundingOrganisation1(startingPoint, endingPoint) {
  cy.setMockTokenForTechnicalSupportFundingOrganisation1();
  for (let i = startingPoint; i < endingPoint; i++) {
    cy.visit('http://localhost:8084/find/api/admin/api-keys/create');
    cy.get(`[data-cy="create-key-input"]`).type('Org1Cypress' + (i + 1));
    cy.get(`[data-cy="create-key-continue"]`).click();
    cy.get(`[data-cy="new-key-back-button"]`).click();
  }
}

function createKeyForFundingOrganisation2(startingPoint, endingPoint) {
  cy.setMockTokenForTechnicalSupportFundingOrganisation2();
  for (let i = startingPoint; i < endingPoint; i++) {
    cy.visit('http://localhost:8084/find/api/admin/api-keys/create');
    cy.get(`[data-cy="create-key-input"]`).type('Org2Cypress' + (i + 1));
    cy.get(`[data-cy="create-key-continue"]`).click();
    cy.get(`[data-cy="new-key-back-button"]`).click();
  }
}

export {
  checkFirst10RowsContent,
  createKeyForFundingOrganisation1,
  createKeyForFundingOrganisation2,
  paginationItemHasCurrentAsCssClass,
  paginationItemHasNotCurrentAsCssClass,
  paginationItemsDoesNotExist,
  paginationLinkHasTheRightHref,
  paginationNextItemHasTheRightHref,
  paginationPreviousItemHasTheRightHref,
};
