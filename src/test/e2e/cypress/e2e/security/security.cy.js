import { ERROR_PAGE_BODY_SUPER_ADMIN, ERROR_PAGE_BODY_TECHNICAL_SUPPORT } from '../../utils/errorPageString';

const BASE_URL = 'http://localhost:8086/find/api/admin';
const SUPERADMIN_COOKIE =
  'user-service-token=eyJraWQiOiJlN2QzZjk0OS01MzdkLTQxMDItODcwNy0wY2NhZDIwYzBjNDAiLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ0Y28uY3lwcmVzcy50ZXN0LnN1cGVyLmFkbWluIiwiYXVkIjoiRkdQIiwicm9sZXMiOiJbQVBQTElDQU5ULCBGSU5ELCBTVVBFUl9BRE1JTl0iLCJpZFRva2VuIjoiYmFuYW5hIiwiaXNzIjoiYmFuYW5hIiwiZGVwYXJ0bWVudCI6IkJhbmFuYSIsImV4cCI6MTY5Mjk3MzIyMiwiaWF0IjoxNjkyOTY5NjIyLCJlbWFpbCI6ImJhbmFuYUBmcnVpdCIsImp0aSI6ImJhbmFuYSJ9.pjRY3K8YmVFzzzHZ5lBf9oHbqnzda4nQMoA03R4b-gLG6V0L9mfr7gktui1dzBiPYCtAb0oNIlaS-CcRsqNYNwOufLAblsLjZOF_HUTzsqVdRBR1gIwRiiv08GQ-e0FbBHQ47I3L7qlQCfjuyeoym4N01jT26ydnbfEjkYlekd9fQSuGTlCSd1J3hAj3mlOtpVnBxxf-fqGTRdbLXEvV3wn7yXRpYHhf-j2h9j1Hicx1t8FFjF7UVk3si31befzjyX4705xst2KMOQMtCDNgmywBPyIS2o7XnRUjXpMhqfomfCnL5q5GdUMn8xun9V8_CpvsZ33Bv46NhXMgWGoiSw';
const APPLICANT_COOKIE =
  'user-service-token=eyJraWQiOiJlN2QzZjk0OS01MzdkLTQxMDItODcwNy0wY2NhZDIwYzBjNDAiLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ0Y28uY3lwcmVzcy50ZXN0LnN1cGVyLmFkbWluIiwiYXVkIjoiRkdQIiwicm9sZXMiOiJbQVBQTElDQU5ULCBGSU5EXSIsImlkVG9rZW4iOiJiYW5hbmEiLCJpc3MiOiJiYW5hbmEiLCJkZXBhcnRtZW50IjoiQmFuYW5hIiwiZXhwIjoxNjkyOTczMjIyLCJpYXQiOjE2OTI5Njk2MjIsImVtYWlsIjoiYmFuYW5hQGZydWl0IiwianRpIjoiYmFuYW5hIn0.d_ovdI86q7rrq8dXOCgDtaVv7K5fXjENXWN4N2n3veSmJ4hPVYGefRNzhTqXOMGh474Z6bnRfPhYuvcrzHzSA-OlfPc285M0Rwb-rgA2fFrOKHLJGvjhzpzc_8QqWTdBQR7IKlQ0NdEZ__GizY1B8xaTAciOro3akjRBRWnpIit_-LRpB1twFBn0W-nUGnucw-vYomyV1M_MAasahbN23FP8tDC29_RCZztXeZmfe9DTDnojE2H6WwrvGIeiQ98Rznr4XP5M3cNCdynNIluTYWrTQqUFC74zsYnrPlSmTua8yn8VQVrylE29nQ7a79KmF4y-fD6U47rEa-XZrp5WCQ';
const TECHNICAL_SUPPORT_COOKIE =
  'user-service-token=eyJraWQiOiJlN2QzZjk0OS01MzdkLTQxMDItODcwNy0wY2NhZDIwYzBjNDAiLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ0Y28uY3lwcmVzcy50ZXN0LnN1cGVyLmFkbWluIiwiYXVkIjoiRkdQIiwicm9sZXMiOiJbQVBQTElDQU5ULCBGSU5ELCBURUNITklDQUxfU1VQUE9SVF0iLCJpZFRva2VuIjoiYmFuYW5hIiwiaXNzIjoiYmFuYW5hIiwiZGVwYXJ0bWVudCI6IkJhbmFuYSIsImV4cCI6MTY5Mjk3MzIyMiwiaWF0IjoxNjkyOTY5NjIyLCJlbWFpbCI6ImJhbmFuYUBmcnVpdCIsImp0aSI6ImJhbmFuYSJ9.i2Am4ieDTw2sVNItCWLEciBfq6mfw7Zqvrq4CpCBfsFxOiKIzaPIHuk44ca8f-3eCT8pdKdx7r0TkOlU-jmYBBjXNisag5BbAKDb0B75aG0ND7oWBsPTbzQFJX-jknkTi3dUVlFpRyyeOykoWtK6r_Jx4rhsY9qMnlGJeiiAivTzVbx89Vl5oaCRPzFUGJ2dEINf25lVxNArt8u86ZTkLlMSarTTO7bIknCnq8TnNvi_rJwKqW_djzNcky5chdDINTsE96BopEaMrrnT4v1M-w9cqXCsCLjJRqogsX6WZZQO2Zph2xEmpzsNWOqi5jDj1VyjXbEkzBp7qFGfhnYT_A';
const SQL_AS_SUB_COOKIE =
  'user-service-token=eyJraWQiOiJlN2QzZjk0OS01MzdkLTQxMDItODcwNy0wY2NhZDIwYzBjNDAiLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJTZWxlY3QgKiBmcm9tIGdhcF91c2VyOyIsImF1ZCI6IkZHUCIsInJvbGVzIjoiW0FQUExJQ0FOVCwgRklORF0iLCJpZFRva2VuIjoiYmFuYW5hIiwiaXNzIjoiYmFuYW5hIiwiZGVwYXJ0bWVudCI6IkJhbmFuYSIsImV4cCI6MTY5Mjk3MzIyMiwiaWF0IjoxNjkyOTY5NjIyLCJlbWFpbCI6ImJhbmFuYUBmcnVpdCIsImp0aSI6ImJhbmFuYSJ9.B4-mECfPUPiohwMympJeBJoxcyERTQS5HkIjXDcktH41gAM0TEne6pey8CNwLkXoPwE4BUtoinPO3yUZjTv_yCs10ulLa_zgr6GPoQTPaatcDv5kaSrvs7sNQdEnshe0rKNdiC7cQo1e4aMsXdeVTZXj-PuAdCuyDt9fnukYH0IHVwBB1D0D7CwY4vWJqA13gnXL5q1uwyXik-Lb4v-07UP84AH3T85yDwmQzGOSUHm3nM9T-Xaaar3M7upFiy68wj9d-MH8nXpY9IOYkb4RmnJo0llQLMO5C-0PLsr9Qe6QVpg9lWGzh91ffH95sYhbTu2eGwbRrkcihXh7B3Am7w';
const SQL_AS_SUB_AND_SUPER_ADMIN_ROLE_COOKIE =
  'user-service-token=eyJraWQiOiJlN2QzZjk0OS01MzdkLTQxMDItODcwNy0wY2NhZDIwYzBjNDAiLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJTZWxlY3QgKiBmcm9tIGdhcF91c2VyOyIsImF1ZCI6IkZHUCIsInJvbGVzIjoiW0FQUExJQ0FOVCwgRklORCwgU1VQRVJfQURNSU5dIiwiaWRUb2tlbiI6ImJhbmFuYSIsImlzcyI6ImJhbmFuYSIsImRlcGFydG1lbnQiOiJCYW5hbmEiLCJleHAiOjE2OTI5NzMyMjIsImlhdCI6MTY5Mjk2OTYyMiwiZW1haWwiOiJiYW5hbmFAZnJ1aXQiLCJqdGkiOiJiYW5hbmEifQ.3Jrgc7rmSGZxt9NZP5eMt1yHEtfPblySG09ij9hp__a_J1919YEa2ly5D3oQihST13p2r1Ow2zpFWQbdXGgCBKy-578xmtzywkvpa1Z-rAsuQyaHE2h4OTYAuXNgbrMVtGUljGvd3eWKImLUug15sjIvNV3KlaT38E2JgR9mNefv47mOS-0uZrNPrmnIS1Slr7VjZ6jmhJSXMAmX8DoFie9-aKOhgYmD0Fbyh9GAPndyIyUD9vbyttSOAfMQaMS1I7idu7q4qcB7T_ntpxZWJeM0SDsza3BzQok-Y1dKUgWGKFY2VA8s0hrXltgfXK_jw1L_0aFg0EC7l1QoiQUs2g';

describe('Security for all roles journey ', () => {
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
        expect(r.body).to.eq(ERROR_PAGE_BODY_SUPER_ADMIN);
      });
    });

    it('should return the error page if a superAdmin try to directly send a GET request to get the organisation keys /api-keys', () => {
      cy.request({
        method: 'GET',
        url: `${BASE_URL}/api-keys`,
        headers: {
          Cookie: SUPERADMIN_COOKIE,
        },
      }).then((r) => {
        expect(r.status).to.eq(200);
        expect(r.redirects[0]).to.eq(`302: ${BASE_URL}/api-keys/error`);
        expect(r.body).to.eq(ERROR_PAGE_BODY_SUPER_ADMIN);
      });
    });

    it('should return the error page if a superAdmin try to directly send a GET request to get the /api-keys/create', () => {
      cy.request({
        method: 'GET',
        url: `${BASE_URL}/api-keys/create`,
        headers: {
          Cookie: SUPERADMIN_COOKIE,
        },
      }).then((r) => {
        expect(r.status).to.eq(200);
        expect(r.redirects[0]).to.eq(`302: ${BASE_URL}/api-keys/error`);
        expect(r.body).to.eq(ERROR_PAGE_BODY_SUPER_ADMIN);
      });
    });
    it('should return the error page if a technical_Support try to directly send a GET request to get the /api-keys/manage', () => {
      cy.request({
        method: 'GET',
        url: `${BASE_URL}/api-keys/manage`,
        headers: {
          Cookie: TECHNICAL_SUPPORT_COOKIE,
        },
      }).then((r) => {
        expect(r.status).to.eq(200);
        expect(r.redirects[0]).to.eq(`302: ${BASE_URL}/api-keys/error`);
        expect(r.body).to.eq(ERROR_PAGE_BODY_TECHNICAL_SUPPORT);
      });
    });
  });

  //in those following tests, we check if the first redirection is to the userService /login endpoint,
  // we cannot check the body because at the moment we mock the user service call to /login to redirect us to our app /redirect endpoint with a SUPERADMIN jwt token,
  //  which would then redirect us to the /api-keys/manage (the superAdmin dashboard)
  describe('Should redirect to login page if the jwt token has not the right roles', () => {
    it('should redirect to login page if an applicant try to directly send a POST request to create keys /api-keys/create', () => {
      cy.request({
        method: 'POST',
        url: `${BASE_URL}/api-keys/create`,
        headers: {
          Cookie: APPLICANT_COOKIE,
        },
      }).then((r) => {
        expect(r.status).to.eq(200);
        expect(r.redirects[0]).to.eq(
          '302: http://localhost:8082/v2/login?redirectUrl=http://localhost:8086/find/api/admin/redirect'
        );
      });
    });

    it('should redirect to login page if an applicant try to directly send a GET request to get the organisation keys /api-keys', () => {
      cy.request({
        method: 'GET',
        url: `${BASE_URL}/api-keys`,
        headers: {
          Cookie: APPLICANT_COOKIE,
        },
      }).then((r) => {
        expect(r.status).to.eq(200);
        expect(r.redirects[0]).to.eq(
          '302: http://localhost:8082/v2/login?redirectUrl=http://localhost:8086/find/api/admin/redirect'
        );
      });
    });

    it('should redirect to login page if an applicant try to directly send a GET request to get the /api-keys/create', () => {
      cy.request({
        method: 'GET',
        url: `${BASE_URL}/api-keys/create`,
        headers: {
          Cookie: APPLICANT_COOKIE,
        },
      }).then((r) => {
        expect(r.status).to.eq(200);
        expect(r.redirects[0]).to.eq(
          '302: http://localhost:8082/v2/login?redirectUrl=http://localhost:8086/find/api/admin/redirect'
        );
      });
    });

    it('should redirect to login page if an applicant try to directly send a GET request to get the /api-keys/manage', () => {
      cy.request({
        method: 'GET',
        url: `${BASE_URL}/api-keys/manage`,
        headers: {
          Cookie: APPLICANT_COOKIE,
        },
      }).then((r) => {
        expect(r.status).to.eq(200);
        expect(r.redirects[0]).to.eq(
          '302: http://localhost:8082/v2/login?redirectUrl=http://localhost:8086/find/api/admin/redirect'
        );
      });
    });

    it('should redirect to login page if an applicant try to directly send a GET request to get the /api-keys/revoke/1', () => {
      cy.request({
        method: 'GET',
        url: `${BASE_URL}/api-keys/revoke/1`,
        headers: {
          Cookie: APPLICANT_COOKIE,
        },
      }).then((r) => {
        expect(r.status).to.eq(200);
        expect(r.redirects[0]).to.eq(
          '302: http://localhost:8082/v2/login?redirectUrl=http://localhost:8086/find/api/admin/redirect'
        );
      });
    });
  });

  describe('Should redirect to login page if no jwt token is in the cookies', () => {
    it('should redirect to login page if an applicant try to directly send a POST request to create keys /api-keys/create', () => {
      cy.request(
        {
          method: 'POST',
          url: `${BASE_URL}/api-keys/create`,
        },
        {
          keyName: 'Cypress',
        }
      ).then((r) => {
        expect(r.status).to.eq(200);
        expect(r.redirects[0]).to.eq(
          '302: http://localhost:8082/v2/login?redirectUrl=http://localhost:8086/find/api/admin/redirect'
        );
      });
    });
  });

  describe('Using malicious data returns an error and does not expose any unexpected information', () => {
    it('should redirect to login page if an applicant with a (Select * from gap_user;) as sub try to directly send a GET request to create keys /api-keys/create', () => {
      cy.request({
        method: 'POST',
        url: `${BASE_URL}/api-keys/create`,
        headers: {
          Cookie: SQL_AS_SUB_COOKIE,
        },
      }).then((r) => {
        expect(r.status).to.eq(200);
        expect(r.redirects[0]).to.eq(
          '302: http://localhost:8082/v2/login?redirectUrl=http://localhost:8086/find/api/admin/redirect'
        );
      });
    });

    it('should return error if a superAdmin with a (Select * from gap_user;) as sub try to directly send a GET request to create keys /api-keys/create', () => {
      cy.request({
        method: 'GET',
        url: `${BASE_URL}/api-keys/create`,
        headers: {
          Cookie: SQL_AS_SUB_AND_SUPER_ADMIN_ROLE_COOKIE,
        },
      }).then((r) => {
        expect(r.status).to.eq(200);
        expect(r.redirects[0]).to.eq(`302: ${BASE_URL}/api-keys/error`);
        expect(r.body).to.eq(ERROR_PAGE_BODY_SUPER_ADMIN);
      });
    });
  });
});
