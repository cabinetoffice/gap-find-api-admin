const { defineConfig } = require('cypress');
const { createApiKeyTable, dropApiKeyTable } = require('./cypress/utils/db/database.js');

module.exports = defineConfig({
  e2e: {
    setupNodeEvents(on, config) {
      on('task', {
        'drop:apiKeyTable': async (envs) => {
          await dropApiKeyTable(envs);
          return null;
        },
        'create:apiKeyTable': async (envs) => {
          await createApiKeyTable(envs);
          return null;
        },
        log(message) {
          console.log(message);

          return null;
        },
        table(message) {
          console.table(message);

          return null;
        },
      });
    },
  },
  redirectionLimit: 200,
  video: false,
  env: {
    DB_NAME: 'gapapply',
    DB_USERNAME: 'postgres',
    DB_PASSWORD: 'mysecretpassword',
    DB_HOST: 'localhost',
    DB_PORT: '5432',
  },
});
