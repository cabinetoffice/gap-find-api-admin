function checkNavBarItemIsRightForTheUserRole(userRole) {
  const isSuperAdmin = userRole === 'SUPER_ADMIN';
  const href = isSuperAdmin
    ? 'http://localhost:3000/apply/admin/super-admin-dashboard'
    : 'http://localhost:3000/apply/admin/dashboard';
  const displayName = isSuperAdmin ? 'Super admin dashboard' : 'Admin dashboard';
  cy.get('[data-cy="header-navbar-back-to-dashboard-link"]').should('have.attr', 'href', href).contains(displayName);
  if (!isSuperAdmin) {
    cy.get('[data-cy="header-navbar-api-documentation-link"]')
      .should('have.attr', 'href', 'http://localhost:8088/apply/open-data/swagger-ui/index.html')
      .contains('Api documentation');
  }
}

function signOutIsPresent() {
  cy.get('[data-cy="header-sign-out-link"]')
    .should('have.attr', 'href', 'http://localhost:8082/v2/logout')
    .contains('Sign out');
}

export { checkNavBarItemIsRightForTheUserRole, signOutIsPresent };
