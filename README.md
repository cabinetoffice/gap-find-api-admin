# GAP Find API Admin

The GAP Find API Admin repository contains a Java application built with Thymeleaf. 
This application serves as an administration panel for managing API keys needed for the FindOpenApiGateway ecosystem.
It allows users with the TECH_SUPPORT role to view, create, and revoke API keys for their department, while users with the SUPER_ADMIN role can view and revoke keys from any department.
The Keys are stored in the admin database and in AWS Api Gateway, and associated to a usage plan.

## Overview

The GAP Find API Admin application provides a user-friendly interface for managing API keys within the FindOpenApiGateway ecosystem. 
It facilitates key management tasks for users with different roles, ensuring secure and efficient management of access to the API.

## Features

- **Role-based Access Control**: Users with the TECH_SUPPORT role can view, create, and revoke API keys for their department. Users with the SUPER_ADMIN role can view and revoke keys from any department.
- **API Key Management**: Allows for the creation and revocation of API keys, ensuring controlled access to the API.
- **Thymeleaf Templates**: Utilizes Thymeleaf templates for dynamic rendering of HTML pages, providing a responsive and interactive user interface.

## Role-based Access

- **TECH_SUPPORT Role**: Users with this role can view, create, and revoke API keys for their department.
- **SUPER_ADMIN Role**: Users with this role can view and revoke keys from any department.

## Local development

## Local Development

- You would need to set up the admin database as per [gap-find-admin-backend](https://github.com/cabinetoffice/gap-find-admin-backend/)
- From root go to the mockUserServiceValidation folder and run ```docker compose up -d``` to run the user-service mock(as default you would be logged in as superadmin, if you need this to change, you would need to change the jwt in line 11 of mockUserServiceValidation/wiremock/mappings/201b5d72-0b07-46ea-865f-41531883e295.json)
- Make sure in the tech_support_user table in admin database, you have a record for the user you are logged as in the mock(as default the user sub will be tco.cypress.test.super.admin)
- Run ```mvn spring-boot:run ```