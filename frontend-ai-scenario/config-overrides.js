// config-overrides.js
module.exports = function override(config, env) {
  // Return the config without trying to modify devServer
  return config;
};