function checkNavBarItemIsRightForTheUserRole(userRole) {
  const href =
    userRole === 'SUPER_ADMIN'
      ? 'http://localhost:3000/apply/admin/super-admin-dashboard'
      : 'http://localhost:3000/apply/admin/dashboard';
  const displayName = userRole === 'SUPER_ADMIN' ? 'Super Admin Dashboard' : 'Admin Dashboard';
  cy.get('[data-cy="header-navbar-back-to-dashboard-link"]').should('have.attr', 'href', href).contains(displayName);
}

function signOutIsPresent() {
  cy.get('[data-cy="header-sign-out-link"]')
    .should('have.attr', 'href', 'http://localhost:8082/v2/logout')
    .contains('Sign out');
}

export { checkNavBarItemIsRightForTheUserRole, signOutIsPresent };
