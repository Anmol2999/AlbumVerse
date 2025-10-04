const Logout = () => {
  localStorage.removeItem('authToken');
  // Redirect to login page after logout
  //can use navigate also
  window.location.href = '/login';
};

export default Logout;
