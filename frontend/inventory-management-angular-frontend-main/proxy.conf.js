module.exports = {
  '/api': {
    target: 'http://localhost:8080',
    secure: false,
    changeOrigin: true,
    onProxyRes: function (proxyRes) {
      // Eliminar el header WWW-Authenticate para que el browser no muestre
      // el popup nativo de autenticación HTTP Basic cuando las credenciales son incorrectas.
      delete proxyRes.headers['www-authenticate'];
    }
  }
};
