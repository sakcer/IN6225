export const localUser = {
    setUser(user: Employee) {
        localStorage.setItem('userInfo', JSON.stringify(user));
    },

    getUser() {
        const user = localStorage.getItem('userInfo');
        return user ? JSON.parse(user) : {};
    },

  getToken() {
    return localStorage.getItem('token');
  },

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('userInfo');
    localStorage.removeItem('role');
  }
};