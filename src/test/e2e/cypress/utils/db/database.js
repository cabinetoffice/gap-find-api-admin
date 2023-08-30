const { promises: fs } = require('fs');
const { Client } = require('pg');

const runSQL = async (filePath, environment) => {
  const config = {
    user: environment.DB_USERNAME,
    host: environment.DB_HOST,
    database: environment.DB_NAME,
    password: environment.DB_PASSWORD,
    port: environment.DB_PORT,
  };
  try {
    const sqlScript = await fs.readFile(filePath, 'utf8');
    const client = new Client(config);
    await client.connect();
    console.log('sqlScript: ', sqlScript);
    await client.query(sqlScript);
    await client.end();
    console.log('SQL script executed successfully.');
  } catch (error) {
    console.error('Error executing SQL script:\n\n', error);
  }
};

const createApiKeyTable = async (environment) => {
  await runSQL('cypress/utils/db/sql/createApiKeyTable.sql', environment);
  console.log('Successfully created api_key Table');
};

const dropApiKeyTable = async (environment) => {
  await runSQL('cypress/utils/db/sql/dropApiKeyTable.sql', environment);
  console.log('Successfully dropped api_key Table');
};

module.exports = {
  runSQL,
  createApiKeyTable,
  dropApiKeyTable,
};
