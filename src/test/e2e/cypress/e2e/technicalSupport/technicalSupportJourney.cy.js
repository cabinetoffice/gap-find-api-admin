Date.prototype.toShortFormat = function () {
  const monthNames = [
    "January",
    "February",
    "March",
    "April",
    "May",
    "June",
    "July",
    "August",
    "September",
    "October",
    "November",
    "December",
  ];

  const day = this.getDate();

  const monthIndex = this.getMonth();
  const monthName = monthNames[monthIndex];

  const year = this.getFullYear();

  return `${day} ${monthName} ${year}`;
};

let today = new Date().toShortFormat();

const apiKeyName = "apiKeyNameCypress";

describe("API key dashboard without keys", () => {
  beforeEach(() => {
    const techSupportToken =
      "eyJraWQiOiIzNTlhYTk0OS1mNWEzLTQ1MWMtYmQyMi01YjllYWExMzk0ZGEiLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1cm46ZmRjOmdvdi51azoyMDIyOmZrRlMwTVFQWFdpN3FnRTNVdm53amIyMUJBRXQxVGQyMEQwYzRJUE95S2ciLCJhdWQiOiJGR1AiLCJyb2xlcyI6IltBUFBMSUNBTlQsIEZJTkQsIFRFQ0hOSUNBTF9TVVBQT1JUXSIsImlkVG9rZW4iOiJleUpyYVdRaU9pSTJORFJoWmpVNU9HSTNPREJtTlRReE1EWmpZVEJtTTJNd01UY3pOREZpWXpJek1HTTBaamd6TnpObU16Vm1NekpsTVRobE0yVTBNR05qTjJGalptWTJJaXdpWVd4bklqb2lSVk15TlRZaWZRLmV5SmhkRjlvWVhOb0lqb2ljWEYyV2paT1kzVjNNa2hUZGxOelRVRTFXbGt4UVNJc0luTjFZaUk2SW5WeWJqcG1aR002WjI5MkxuVnJPakl3TWpJNlptdEdVekJOVVZCWVYyazNjV2RGTTFWMmJuZHFZakl4UWtGRmRERlVaREl3UkRCak5FbFFUM2xMWnlJc0ltRjFaQ0k2SW1SM1dYVXhOVTl5ZVV4dVNGOUNSMVpZVldaUlNUTlZZek0zVlNJc0ltbHpjeUk2SW1oMGRIQnpPaTh2YjJsa1l5NXBiblJsWjNKaGRHbHZiaTVoWTJOdmRXNTBMbWR2ZGk1MWF5OGlMQ0oyYjNRaU9pSkRiQzVEYlNJc0ltVjRjQ0k2TVRZNU1qZzRPVEF6TlN3aWFXRjBJam94TmpreU9EZzRPVEUxTENKdWIyNWpaU0k2SWt0aGJWbElVMHh0YkdwSGJUSTJhblJyVlY5RFowZERTR2RsVlZwQlkwTjNVRUkyWVZGRk9WZFdNRzg0U1hZM2EwbElXR3RFZG0xYU5IVXhha2xSZVV3aUxDSjJkRzBpT2lKb2RIUndjem92TDI5cFpHTXVhVzUwWldkeVlYUnBiMjR1WVdOamIzVnVkQzVuYjNZdWRXc3ZkSEoxYzNSdFlYSnJJaXdpYzJsa0lqb2lhMEo0VkVkelEzRkhPVUZRTjBGSmRVNHlXVkpwTTBseVh5MXZJbjAuTWZMaDVBN3hoRUIwVVpPYjdWdzh6c1FDN3FqRzBQaUlrSXRXSGM1SUdjM2x0ZXZtbmpfb0FZdm5ad1ViRmtrVmJWYThmSFJVbUpxVGk5UnZYLWFuTkEiLCJpc3MiOiJodHRwczovL3NhbmRib3gtZ2FwLnNlcnZpY2UuY2FiaW5ldG9mZmljZS5nb3YudWsvYXBwbHkvdXNlciIsImRlcGFydG1lbnQiOiJDYWJpbmV0IE9mZmljZSIsImV4cCI6MTY5Mjg5MjUxNSwiaWF0IjoxNjkyODg4OTE1LCJlbWFpbCI6ImFudG9uaW8ubG9ydXNzb0BhbmQuZGlnaXRhbCIsImp0aSI6IjQ3MGM3MTIyLWQwYzgtNGUwYS05NWEyLTA4NzdiOTY1NDc4OSJ9.Q6XKSMptVdg-FSmgDj2P2IdYzkg-Jc9pk83AFvMwsP84k-yDYHwxdfsjWINKZo0PEWRvF04Xh9GNG0jrMuDXX6rBBuXNa7HyAh0WdBx0u8eOQxlbWsism_SShVFWHmIrl-EUHwkvigTAZ-KfzvZzM5TWwd1r_yRaepWO87NRrfC7-fyqzoQJNdvkVIhJP8geZcTps239uOCk6QJTAnnSxSH6-ue8CA_Dq4_kkRoSt2qUfY9F1Hi4f9WdooVsZGftaaiMQzARNHwe6ilExV8iygylvM19FNxV11XNYxeGn3DSlTbyrrv5-3KU0mthBJlqRvELNdiTC9AS2sL9YXQS9Q";
    cy.setCookie("user-service-token", techSupportToken);
  });

  it("Should render API key dashboard, display no API keys, header and footer", () => {
    cy.visit("http://localhost:8084/find/api/admin/api-keys");
    cy.get('[data-cy="header"]').should("be.visible").contains("Find a Grant");

    cy.get('[data-cy="beta-banner"]').should("be.visible").contains("BETA");

    cy.get('[data-cy="header-feedback-link"]')
      .should(
        "have.attr",
        "href",
        "https://docs.google.com/forms/d/e/1FAIpQLSd2V0IqOMpb2_yQnz_Ges0WCYFnDOTxZpF299gePV1j8kMdLA/viewform"
      )
      .contains("feedback");

    cy.get('[data-cy="footer"]')
      .should("be.visible")
      .contains(
        "All content is available under the Open Government Licence v3.0, except where otherwise stated"
      );

    cy.get('[data-cy="api-keys-heading"]')
      .should("be.visible")
      .contains("Manage API keys");

    cy.get('[data-cy="api-keys-department"]')
      .should("be.visible")
      .contains("Department");

    cy.get('[data-cy="api-keys-department-name"]')
      .should("be.visible")
      .contains("Evil Org");

    cy.get('[data-cy="api-keys-no-api-keys"]')
      .should("be.visible")
      .contains(
        "You don't have any API keys yet. When you create one, you will be able to see a list of your API keys and revoke access to them on this screen."
      );

    cy.get('[data-cy="api-keys-create-button"]')
      .should("be.visible")
      .contains("Create an API key");
  });
});

describe("Create an API Key", () => {
  beforeEach(() => {
    const techSupportToken =
      "eyJraWQiOiIzNTlhYTk0OS1mNWEzLTQ1MWMtYmQyMi01YjllYWExMzk0ZGEiLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1cm46ZmRjOmdvdi51azoyMDIyOmZrRlMwTVFQWFdpN3FnRTNVdm53amIyMUJBRXQxVGQyMEQwYzRJUE95S2ciLCJhdWQiOiJGR1AiLCJyb2xlcyI6IltBUFBMSUNBTlQsIEZJTkQsIFRFQ0hOSUNBTF9TVVBQT1JUXSIsImlkVG9rZW4iOiJleUpyYVdRaU9pSTJORFJoWmpVNU9HSTNPREJtTlRReE1EWmpZVEJtTTJNd01UY3pOREZpWXpJek1HTTBaamd6TnpObU16Vm1NekpsTVRobE0yVTBNR05qTjJGalptWTJJaXdpWVd4bklqb2lSVk15TlRZaWZRLmV5SmhkRjlvWVhOb0lqb2ljWEYyV2paT1kzVjNNa2hUZGxOelRVRTFXbGt4UVNJc0luTjFZaUk2SW5WeWJqcG1aR002WjI5MkxuVnJPakl3TWpJNlptdEdVekJOVVZCWVYyazNjV2RGTTFWMmJuZHFZakl4UWtGRmRERlVaREl3UkRCak5FbFFUM2xMWnlJc0ltRjFaQ0k2SW1SM1dYVXhOVTl5ZVV4dVNGOUNSMVpZVldaUlNUTlZZek0zVlNJc0ltbHpjeUk2SW1oMGRIQnpPaTh2YjJsa1l5NXBiblJsWjNKaGRHbHZiaTVoWTJOdmRXNTBMbWR2ZGk1MWF5OGlMQ0oyYjNRaU9pSkRiQzVEYlNJc0ltVjRjQ0k2TVRZNU1qZzRPVEF6TlN3aWFXRjBJam94TmpreU9EZzRPVEUxTENKdWIyNWpaU0k2SWt0aGJWbElVMHh0YkdwSGJUSTJhblJyVlY5RFowZERTR2RsVlZwQlkwTjNVRUkyWVZGRk9WZFdNRzg0U1hZM2EwbElXR3RFZG0xYU5IVXhha2xSZVV3aUxDSjJkRzBpT2lKb2RIUndjem92TDI5cFpHTXVhVzUwWldkeVlYUnBiMjR1WVdOamIzVnVkQzVuYjNZdWRXc3ZkSEoxYzNSdFlYSnJJaXdpYzJsa0lqb2lhMEo0VkVkelEzRkhPVUZRTjBGSmRVNHlXVkpwTTBseVh5MXZJbjAuTWZMaDVBN3hoRUIwVVpPYjdWdzh6c1FDN3FqRzBQaUlrSXRXSGM1SUdjM2x0ZXZtbmpfb0FZdm5ad1ViRmtrVmJWYThmSFJVbUpxVGk5UnZYLWFuTkEiLCJpc3MiOiJodHRwczovL3NhbmRib3gtZ2FwLnNlcnZpY2UuY2FiaW5ldG9mZmljZS5nb3YudWsvYXBwbHkvdXNlciIsImRlcGFydG1lbnQiOiJDYWJpbmV0IE9mZmljZSIsImV4cCI6MTY5Mjg5MjUxNSwiaWF0IjoxNjkyODg4OTE1LCJlbWFpbCI6ImFudG9uaW8ubG9ydXNzb0BhbmQuZGlnaXRhbCIsImp0aSI6IjQ3MGM3MTIyLWQwYzgtNGUwYS05NWEyLTA4NzdiOTY1NDc4OSJ9.Q6XKSMptVdg-FSmgDj2P2IdYzkg-Jc9pk83AFvMwsP84k-yDYHwxdfsjWINKZo0PEWRvF04Xh9GNG0jrMuDXX6rBBuXNa7HyAh0WdBx0u8eOQxlbWsism_SShVFWHmIrl-EUHwkvigTAZ-KfzvZzM5TWwd1r_yRaepWO87NRrfC7-fyqzoQJNdvkVIhJP8geZcTps239uOCk6QJTAnnSxSH6-ue8CA_Dq4_kkRoSt2qUfY9F1Hi4f9WdooVsZGftaaiMQzARNHwe6ilExV8iygylvM19FNxV11XNYxeGn3DSlTbyrrv5-3KU0mthBJlqRvELNdiTC9AS2sL9YXQS9Q";
    cy.setCookie("user-service-token", techSupportToken);
    cy.visit("http://localhost:8084/find/api/admin/api-keys/create");
  });

  it("Should render create an API key page", () => {
    cy.visit("http://localhost:8084/find/api/admin/api-keys");
    cy.get('[data-cy="api-keys-create-button"]')
      .should("be.visible")
      .contains("Create an API key")
      .click();

    cy.url().should("include", "api-keys/create");

    cy.get('[data-cy="create-key-error-banner"]').should("not.exist");

    cy.get('[data-cy="create-key-heading"]')
      .should("be.visible")
      .contains("Name your API key");

    cy.get('[data-cy="create-key-hint-text"]')
      .should("be.visible")
      .contains(
        "This name will be shown in your list of API keys to make it easier to find. We recommend using the name of the service you will be using the API key to integrate with"
      );

    cy.get('[data-cy="create-key-continue"]')
      .should("be.visible")
      .contains("Continue");

    cy.get('[data-cy="create-key-back-button"]')
      .should("be.visible")
      .should("have.text", "Back")
      .click();

    cy.get('[data-cy="api-keys-heading"]')
      .should("be.visible")
      .should("have.text", "Manage API keys");

    cy.get('[data-cy="api-keys-create-button"]')
      .should("be.visible")
      .contains("Create an API key")
      .click();

    cy.url().should("include", "/admin/api-keys/create");
  });

  it("Should display validation errors for blank name", () => {
    cy.get('[data-cy="create-key-continue"]')
      .should("be.visible")
      .contains("Continue")
      .click();

    cy.get('[data-cy="create-key-error-banner-heading"]')
      .should("be.visible")
      .contains("There is a problem");

    cy.get('[data-cy="create-key-error-summary-list"]')
      .should("have.length", 1)
      .contains("Enter a key name");

    cy.get('[data-cy="create-key-input-validation-error-details"]')
      .should("be.visible")
      .should("have.text", "Enter a key name");
  });

  it("Should display validation errors for blank spaces", () => {
    cy.get('[data-cy="create-key-input"]').type("api key name");

    cy.get('[data-cy="create-key-continue"]')
      .should("be.visible")
      .contains("Continue")
      .click();

    cy.get('[data-cy="create-key-error-banner-heading"]')
      .should("be.visible")
      .contains("There is a problem");

    cy.get('[data-cy="create-key-error-summary-list"]')
      .should("have.length", 1)
      .contains("Key name must be alphanumeric");

    cy.get('[data-cy="create-key-input-validation-error-details"]')
      .should("be.visible")
      .should("have.text", "Key name must be alphanumeric");
  });

  it("Should successfully create key and display value", () => {
    cy.get('[data-cy="create-key-input"]').type(apiKeyName);

    cy.get('[data-cy="create-key-continue"]')
      .should("be.visible")
      .contains("Continue")
      .click();

    cy.url().should("include", "/admin/api-keys/create");

    cy.get('[data-cy="new-key-heading"]')
      .should("be.visible")
      .should("have.text", "New API key");

    cy.get('[data-cy="new-key-your-api-key"]')
      .should("be.visible")
      .should("have.text", "Your API key:");

    cy.get('[data-cy="new-key-value"]').then(($span) => {
      const apiKeyValue = $span.text();
      cy.get('[data-cy="new-key-copy-to-clipboard-button"]')
        .should("be.visible")
        .contains("Copy to clipboard")
        .click();

      cy.window()
        .then((win) => {
          cy.stub(win.navigator.clipboard, "readText").resolves(apiKeyValue);
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
      .should("be.visible")
      .should(
        "have.text",
        "You should keep a copy of this API key somewhere safe."
      );

    cy.get('[data-cy="new-key-paragraph-2"]')
      .should("be.visible")
      .should(
        "have.text",
        "When you leave this screen, you will not be able to see this API key again."
      );

    cy.get('[data-cy="new-key-back-to-api-keys"]')
      .should("be.visible")
      .contains("Back to your API keys")
      .click();

    cy.get('[data-cy="api-keys-heading"]')
      .should("be.visible")
      .should("have.text", "Manage API keys");
  });

  it("Should display validation errors for existing API key name", () => {
    cy.get('[data-cy="create-key-input"]').type(apiKeyName);

    cy.get('[data-cy="create-key-continue"]')
      .should("be.visible")
      .contains("Continue")
      .click();

    cy.get('[data-cy="create-key-error-banner-heading"]')
      .should("be.visible")
      .contains("There is a problem");

    cy.get('[data-cy="create-key-error-summary-list"]')
      .should("have.length", 1)
      .contains("An API key with this name already exists");

    cy.get('[data-cy="create-key-input-validation-error-details"]')
      .should("be.visible")
      .should("have.text", "An API key with this name already exists");

    cy.get('[data-cy="create-key-back-button"]')
      .should("be.visible")
      .should("have.text", "Back")
      .click();
  });
});

describe("API key dashboard with keys", () => {
  beforeEach(() => {
    const techSupportToken =
      "eyJraWQiOiIzNTlhYTk0OS1mNWEzLTQ1MWMtYmQyMi01YjllYWExMzk0ZGEiLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1cm46ZmRjOmdvdi51azoyMDIyOmZrRlMwTVFQWFdpN3FnRTNVdm53amIyMUJBRXQxVGQyMEQwYzRJUE95S2ciLCJhdWQiOiJGR1AiLCJyb2xlcyI6IltBUFBMSUNBTlQsIEZJTkQsIFRFQ0hOSUNBTF9TVVBQT1JUXSIsImlkVG9rZW4iOiJleUpyYVdRaU9pSTJORFJoWmpVNU9HSTNPREJtTlRReE1EWmpZVEJtTTJNd01UY3pOREZpWXpJek1HTTBaamd6TnpObU16Vm1NekpsTVRobE0yVTBNR05qTjJGalptWTJJaXdpWVd4bklqb2lSVk15TlRZaWZRLmV5SmhkRjlvWVhOb0lqb2ljWEYyV2paT1kzVjNNa2hUZGxOelRVRTFXbGt4UVNJc0luTjFZaUk2SW5WeWJqcG1aR002WjI5MkxuVnJPakl3TWpJNlptdEdVekJOVVZCWVYyazNjV2RGTTFWMmJuZHFZakl4UWtGRmRERlVaREl3UkRCak5FbFFUM2xMWnlJc0ltRjFaQ0k2SW1SM1dYVXhOVTl5ZVV4dVNGOUNSMVpZVldaUlNUTlZZek0zVlNJc0ltbHpjeUk2SW1oMGRIQnpPaTh2YjJsa1l5NXBiblJsWjNKaGRHbHZiaTVoWTJOdmRXNTBMbWR2ZGk1MWF5OGlMQ0oyYjNRaU9pSkRiQzVEYlNJc0ltVjRjQ0k2TVRZNU1qZzRPVEF6TlN3aWFXRjBJam94TmpreU9EZzRPVEUxTENKdWIyNWpaU0k2SWt0aGJWbElVMHh0YkdwSGJUSTJhblJyVlY5RFowZERTR2RsVlZwQlkwTjNVRUkyWVZGRk9WZFdNRzg0U1hZM2EwbElXR3RFZG0xYU5IVXhha2xSZVV3aUxDSjJkRzBpT2lKb2RIUndjem92TDI5cFpHTXVhVzUwWldkeVlYUnBiMjR1WVdOamIzVnVkQzVuYjNZdWRXc3ZkSEoxYzNSdFlYSnJJaXdpYzJsa0lqb2lhMEo0VkVkelEzRkhPVUZRTjBGSmRVNHlXVkpwTTBseVh5MXZJbjAuTWZMaDVBN3hoRUIwVVpPYjdWdzh6c1FDN3FqRzBQaUlrSXRXSGM1SUdjM2x0ZXZtbmpfb0FZdm5ad1ViRmtrVmJWYThmSFJVbUpxVGk5UnZYLWFuTkEiLCJpc3MiOiJodHRwczovL3NhbmRib3gtZ2FwLnNlcnZpY2UuY2FiaW5ldG9mZmljZS5nb3YudWsvYXBwbHkvdXNlciIsImRlcGFydG1lbnQiOiJDYWJpbmV0IE9mZmljZSIsImV4cCI6MTY5Mjg5MjUxNSwiaWF0IjoxNjkyODg4OTE1LCJlbWFpbCI6ImFudG9uaW8ubG9ydXNzb0BhbmQuZGlnaXRhbCIsImp0aSI6IjQ3MGM3MTIyLWQwYzgtNGUwYS05NWEyLTA4NzdiOTY1NDc4OSJ9.Q6XKSMptVdg-FSmgDj2P2IdYzkg-Jc9pk83AFvMwsP84k-yDYHwxdfsjWINKZo0PEWRvF04Xh9GNG0jrMuDXX6rBBuXNa7HyAh0WdBx0u8eOQxlbWsism_SShVFWHmIrl-EUHwkvigTAZ-KfzvZzM5TWwd1r_yRaepWO87NRrfC7-fyqzoQJNdvkVIhJP8geZcTps239uOCk6QJTAnnSxSH6-ue8CA_Dq4_kkRoSt2qUfY9F1Hi4f9WdooVsZGftaaiMQzARNHwe6ilExV8iygylvM19FNxV11XNYxeGn3DSlTbyrrv5-3KU0mthBJlqRvELNdiTC9AS2sL9YXQS9Q";
    cy.setCookie("user-service-token", techSupportToken);
  });

  it("Should render API key dashboard with list of API keys", () => {
    cy.visit("http://localhost:8084/find/api/admin/api-keys");
    cy.get('[data-cy="api-keys-heading"]')
      .should("be.visible")
      .should("have.text", "Manage API keys");

    cy.get('[data-cy="api-keys-department"]')
      .should("be.visible")
      .contains("Department");

    cy.get('[data-cy="api-keys-department-name"]')
      .should("be.visible")
      .should("have.text", "Evil Org");

    cy.get('[data-cy="create-key-summary-list"]')
      .children()
      .should("have.length", 1);

    cy.get(`[data-cy="api-key-name-${apiKeyName}"]`)
      .should("be.visible")
      .should("have.text", apiKeyName);

    cy.get(`[data-cy="api-key-created-date-${apiKeyName}"]`)
      .should("be.visible")
      .should("have.text", "Created " + today);

    cy.get(`[data-cy="api-key-revoke-${apiKeyName}"]`)
      .should("be.visible")
      .contains("Revoke");

    cy.get('[data-cy="api-keys-create-button"]')
      .should("be.visible")
      .contains("Create an API key");
  });
});

describe("Revoke API key", () => {
  beforeEach(() => {
    const techSupportToken =
      "eyJraWQiOiIzNTlhYTk0OS1mNWEzLTQ1MWMtYmQyMi01YjllYWExMzk0ZGEiLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1cm46ZmRjOmdvdi51azoyMDIyOmZrRlMwTVFQWFdpN3FnRTNVdm53amIyMUJBRXQxVGQyMEQwYzRJUE95S2ciLCJhdWQiOiJGR1AiLCJyb2xlcyI6IltBUFBMSUNBTlQsIEZJTkQsIFRFQ0hOSUNBTF9TVVBQT1JUXSIsImlkVG9rZW4iOiJleUpyYVdRaU9pSTJORFJoWmpVNU9HSTNPREJtTlRReE1EWmpZVEJtTTJNd01UY3pOREZpWXpJek1HTTBaamd6TnpObU16Vm1NekpsTVRobE0yVTBNR05qTjJGalptWTJJaXdpWVd4bklqb2lSVk15TlRZaWZRLmV5SmhkRjlvWVhOb0lqb2ljWEYyV2paT1kzVjNNa2hUZGxOelRVRTFXbGt4UVNJc0luTjFZaUk2SW5WeWJqcG1aR002WjI5MkxuVnJPakl3TWpJNlptdEdVekJOVVZCWVYyazNjV2RGTTFWMmJuZHFZakl4UWtGRmRERlVaREl3UkRCak5FbFFUM2xMWnlJc0ltRjFaQ0k2SW1SM1dYVXhOVTl5ZVV4dVNGOUNSMVpZVldaUlNUTlZZek0zVlNJc0ltbHpjeUk2SW1oMGRIQnpPaTh2YjJsa1l5NXBiblJsWjNKaGRHbHZiaTVoWTJOdmRXNTBMbWR2ZGk1MWF5OGlMQ0oyYjNRaU9pSkRiQzVEYlNJc0ltVjRjQ0k2TVRZNU1qZzRPVEF6TlN3aWFXRjBJam94TmpreU9EZzRPVEUxTENKdWIyNWpaU0k2SWt0aGJWbElVMHh0YkdwSGJUSTJhblJyVlY5RFowZERTR2RsVlZwQlkwTjNVRUkyWVZGRk9WZFdNRzg0U1hZM2EwbElXR3RFZG0xYU5IVXhha2xSZVV3aUxDSjJkRzBpT2lKb2RIUndjem92TDI5cFpHTXVhVzUwWldkeVlYUnBiMjR1WVdOamIzVnVkQzVuYjNZdWRXc3ZkSEoxYzNSdFlYSnJJaXdpYzJsa0lqb2lhMEo0VkVkelEzRkhPVUZRTjBGSmRVNHlXVkpwTTBseVh5MXZJbjAuTWZMaDVBN3hoRUIwVVpPYjdWdzh6c1FDN3FqRzBQaUlrSXRXSGM1SUdjM2x0ZXZtbmpfb0FZdm5ad1ViRmtrVmJWYThmSFJVbUpxVGk5UnZYLWFuTkEiLCJpc3MiOiJodHRwczovL3NhbmRib3gtZ2FwLnNlcnZpY2UuY2FiaW5ldG9mZmljZS5nb3YudWsvYXBwbHkvdXNlciIsImRlcGFydG1lbnQiOiJDYWJpbmV0IE9mZmljZSIsImV4cCI6MTY5Mjg5MjUxNSwiaWF0IjoxNjkyODg4OTE1LCJlbWFpbCI6ImFudG9uaW8ubG9ydXNzb0BhbmQuZGlnaXRhbCIsImp0aSI6IjQ3MGM3MTIyLWQwYzgtNGUwYS05NWEyLTA4NzdiOTY1NDc4OSJ9.Q6XKSMptVdg-FSmgDj2P2IdYzkg-Jc9pk83AFvMwsP84k-yDYHwxdfsjWINKZo0PEWRvF04Xh9GNG0jrMuDXX6rBBuXNa7HyAh0WdBx0u8eOQxlbWsism_SShVFWHmIrl-EUHwkvigTAZ-KfzvZzM5TWwd1r_yRaepWO87NRrfC7-fyqzoQJNdvkVIhJP8geZcTps239uOCk6QJTAnnSxSH6-ue8CA_Dq4_kkRoSt2qUfY9F1Hi4f9WdooVsZGftaaiMQzARNHwe6ilExV8iygylvM19FNxV11XNYxeGn3DSlTbyrrv5-3KU0mthBJlqRvELNdiTC9AS2sL9YXQS9Q";
    cy.setCookie("user-service-token", techSupportToken);
  });

  it("Should render API key confirmation page and revoke API key", () => {
    cy.visit("http://localhost:8084/find/api/admin/api-keys");

    cy.get(`[data-cy="api-key-revoke-${apiKeyName}"]`)
      .should("be.visible")
      .contains("Revoke")
      .click();

    cy.get('[data-cy="revoke-heading"]')
      .should("be.visible")
      .should("have.text", "Revoke an API key");

    cy.get('[data-cy="revoke-paragraph-1"]')
      .should("be.visible")
      .contains(
        "If you revoke this API key, you will no longer be able to use it to request data from Find a grant."
      );

    cy.get('[data-cy="revoke-paragraph-2"]')
      .should("be.visible")
      .contains(
        "You cannot re-enable a revoked API key. If you want to request data again, you will need to create a new API key."
      );

    cy.get('[data-cy="revoke-revoke-button"]')
      .should("be.visible")
      .should("have.text", "Revoke key");

    cy.get('[data-cy="revoke-cancel-button"]')
      .should("be.visible")
      .should("have.text", "Cancel")
      .click();

    cy.get(`[data-cy="api-key-revoke-${apiKeyName}"]`)
      .should("be.visible")
      .contains("Revoke")
      .click();

    cy.get('[data-cy="revoke-revoke-button"]')
      .should("be.visible")
      .should("have.text", "Revoke key")
      .click();

    cy.get('[data-cy="create-key-summary-list"]')
      .children()
      .should("have.length", 1);

    cy.get(`[data-cy="api-key-name-${apiKeyName}"]`)
      .should("be.visible")
      .should("have.text", apiKeyName);

    cy.get(`[data-cy="api-key-created-date-${apiKeyName}"]`)
      .should("be.visible")
      .should("have.text", "Created " + today);

    cy.get(`[data-cy="api-key-revoked-${apiKeyName}"]`)
      .should("be.visible")
      .should("have.text", "Revoked " + today);
  });
});

describe("Error page", () => {
  beforeEach(() => {
    const techSupportToken =
      "eyJraWQiOiIzNTlhYTk0OS1mNWEzLTQ1MWMtYmQyMi01YjllYWExMzk0ZGEiLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1cm46ZmRjOmdvdi51azoyMDIyOmZrRlMwTVFQWFdpN3FnRTNVdm53amIyMUJBRXQxVGQyMEQwYzRJUE95S2ciLCJhdWQiOiJGR1AiLCJyb2xlcyI6IltBUFBMSUNBTlQsIEZJTkQsIFRFQ0hOSUNBTF9TVVBQT1JUXSIsImlkVG9rZW4iOiJleUpyYVdRaU9pSTJORFJoWmpVNU9HSTNPREJtTlRReE1EWmpZVEJtTTJNd01UY3pOREZpWXpJek1HTTBaamd6TnpObU16Vm1NekpsTVRobE0yVTBNR05qTjJGalptWTJJaXdpWVd4bklqb2lSVk15TlRZaWZRLmV5SmhkRjlvWVhOb0lqb2ljWEYyV2paT1kzVjNNa2hUZGxOelRVRTFXbGt4UVNJc0luTjFZaUk2SW5WeWJqcG1aR002WjI5MkxuVnJPakl3TWpJNlptdEdVekJOVVZCWVYyazNjV2RGTTFWMmJuZHFZakl4UWtGRmRERlVaREl3UkRCak5FbFFUM2xMWnlJc0ltRjFaQ0k2SW1SM1dYVXhOVTl5ZVV4dVNGOUNSMVpZVldaUlNUTlZZek0zVlNJc0ltbHpjeUk2SW1oMGRIQnpPaTh2YjJsa1l5NXBiblJsWjNKaGRHbHZiaTVoWTJOdmRXNTBMbWR2ZGk1MWF5OGlMQ0oyYjNRaU9pSkRiQzVEYlNJc0ltVjRjQ0k2TVRZNU1qZzRPVEF6TlN3aWFXRjBJam94TmpreU9EZzRPVEUxTENKdWIyNWpaU0k2SWt0aGJWbElVMHh0YkdwSGJUSTJhblJyVlY5RFowZERTR2RsVlZwQlkwTjNVRUkyWVZGRk9WZFdNRzg0U1hZM2EwbElXR3RFZG0xYU5IVXhha2xSZVV3aUxDSjJkRzBpT2lKb2RIUndjem92TDI5cFpHTXVhVzUwWldkeVlYUnBiMjR1WVdOamIzVnVkQzVuYjNZdWRXc3ZkSEoxYzNSdFlYSnJJaXdpYzJsa0lqb2lhMEo0VkVkelEzRkhPVUZRTjBGSmRVNHlXVkpwTTBseVh5MXZJbjAuTWZMaDVBN3hoRUIwVVpPYjdWdzh6c1FDN3FqRzBQaUlrSXRXSGM1SUdjM2x0ZXZtbmpfb0FZdm5ad1ViRmtrVmJWYThmSFJVbUpxVGk5UnZYLWFuTkEiLCJpc3MiOiJodHRwczovL3NhbmRib3gtZ2FwLnNlcnZpY2UuY2FiaW5ldG9mZmljZS5nb3YudWsvYXBwbHkvdXNlciIsImRlcGFydG1lbnQiOiJDYWJpbmV0IE9mZmljZSIsImV4cCI6MTY5Mjg5MjUxNSwiaWF0IjoxNjkyODg4OTE1LCJlbWFpbCI6ImFudG9uaW8ubG9ydXNzb0BhbmQuZGlnaXRhbCIsImp0aSI6IjQ3MGM3MTIyLWQwYzgtNGUwYS05NWEyLTA4NzdiOTY1NDc4OSJ9.Q6XKSMptVdg-FSmgDj2P2IdYzkg-Jc9pk83AFvMwsP84k-yDYHwxdfsjWINKZo0PEWRvF04Xh9GNG0jrMuDXX6rBBuXNa7HyAh0WdBx0u8eOQxlbWsism_SShVFWHmIrl-EUHwkvigTAZ-KfzvZzM5TWwd1r_yRaepWO87NRrfC7-fyqzoQJNdvkVIhJP8geZcTps239uOCk6QJTAnnSxSH6-ue8CA_Dq4_kkRoSt2qUfY9F1Hi4f9WdooVsZGftaaiMQzARNHwe6ilExV8iygylvM19FNxV11XNYxeGn3DSlTbyrrv5-3KU0mthBJlqRvELNdiTC9AS2sL9YXQS9Q";
    cy.setCookie("user-service-token", techSupportToken);
  });

  it("Should render error page when trying to access super admin dashboard", () => {
    cy.visit("http://localhost:8084/find/api/admin/api-keys/manage");

    cy.get('[data-cy="error-heading"]')
      .should("be.visible")
      .should("have.text", "Something went wrong");

    cy.get('[data-cy="error-paragraph"]')
      .should("be.visible")
      .should(
        "have.text",
        "Something went wrong while trying to complete your request."
      );

    cy.get('[data-cy="error-paragraph-with-link"]')
      .should("be.visible")
      .contains("You can return to the API dashboard to try again.");

    cy.get('[data-cy="error-back-link"]')
      .should("have.attr", "href", "/find/api/admin/api-keys")
      .contains("return to the API dashboard")
      .click();

    cy.get('[data-cy="api-keys-heading"]')
      .should("be.visible")
      .should("have.text", "Manage API keys");
  });
});
