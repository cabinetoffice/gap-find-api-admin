describe('Super Admin Journey', () => {
  // it('should show No keys message when no apiKeys are present', () => {
  //   cy.setMockTokenForSuperAdmin();
  //   cy.visit('http://localhost:8084/find/api/admin/api-keys/manage');
  //   cy.get('[data-cy="header"]').should('be.visible');
  //   cy.get(`[data-cy="beta-banner"]`).should('be.visible');
  //   cy.get(`[data-cy="admin-dashboard-heading"]`).should('be.visible').should('have.text', 'Manage API keys');
  //   cy.get(`[data-cy="admin-dashboard-active-key-count"]`)
  //     .should('be.visible')
  //     .should('have.text', '0 active API keys');
  //   cy.get(`[data-cy="admin-dashboard-no-api-key-paragraph"]`)
  //     .should('be.visible')
  //     .should(
  //       'have.text',
  //       'No one has any API keys yet. When one is created, you will be able to see a list of all API keys, the department it belongs, when it was created and revoke access to them on this screen.'
  //     );
  //   cy.get(`[data-cy="footer"]`).should('be.visible');
  // });

  // it('creates keys for superAdmin test', () => {
  //   create55KeyForFUndingOrganisation1();
  //   create55KeyForFUndingOrganisation2();
  // });

  it('should show 110 keys and 2 organisation in the filters', () => {
    cy.setMockTokenForSuperAdmin();
    cy.visit('http://localhost:8084/find/api/admin/api-keys/manage');

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
    const today = new Date().toLocaleDateString('en-GB', {
      day: 'numeric',
      month: 'long',
      year: 'numeric',
    });
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
  });

  //pagination
});

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
// function create55KeyForFUndingOrganisation1() {
//   cy.setMockTokenForTechnicalSupportFundingOrganisation1();
//   for (let i = 0; i < 55; i++) {
//     cy.visit('http://localhost:8084/find/api/admin/api-keys/create');
//     cy.get(`[data-cy="create-key-input"]`).type('Org1Cypress' + (i + 1));
//     cy.get(`[data-cy="create-key-continue"]`).click();
//     cy.get(`[data-cy="new-key-back-button"]`).click();
//   }
// }
// function create55KeyForFUndingOrganisation2() {
//   cy.setMockTokenForTechnicalSupportFundingOrganisation2();
//   for (let i = 55; i < 110; i++) {
//     cy.visit('http://localhost:8084/find/api/admin/api-keys/create');
//     cy.get(`[data-cy="create-key-input"]`).type('Org2Cypress' + (i + 1));
//     cy.get(`[data-cy="create-key-continue"]`).click();
//     cy.get(`[data-cy="new-key-back-button"]`).click();
//   }
// }
