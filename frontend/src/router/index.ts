import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/store/meStore'
import { USER_ROLES } from '@/utils/constants'

// Define admin routes
const adminRoutes = [
  {
    path: '/admin',
    component: () => import('@/layouts/DefaultLayout.vue'),
    redirect: '/admin/dashboard',
    meta: { requiresAdmin: true }, // Require admin access
    children: [
      {
        path: 'dashboard',
        name: 'AdminDashboard',
        component: () => import('@/views/admin/Dashboard.vue'),
        meta: { title: 'Dashboard', icon: 'Monitor' } // Dashboard title and icon
      },
      {
        path: 'employees',
        name: 'EmployeeList',
        component: () => import('@/views/employees/EmployeeList.vue'),
        meta: { title: 'Employees', icon: 'User' } // Employee management title and icon
      },
      {
        path: 'projects',
        name: 'ProjectList',
        component: () => import('@/views/projects/ProjectList.vue'),
        meta: { title: 'Projects', icon: 'Folder' } // Project management title and icon
      },
      {
        path: 'profile',
        name: 'AdminProfile',
        component: () => import('@/views/profile/Profile.vue'),
        meta: { title: 'Profile', icon: 'User' } // Personal information title and icon
      },
    ]
  }
]

// Define employee routes
const employeeRoutes = [
  {
    path: '/employee',
    component: () => import('@/layouts/DefaultLayout.vue'),
    redirect: '/employee/dashboard',
    meta: { requiresEmployee: true }, // Require employee access
    children: [
      {
        path: 'dashboard',
        name: 'UserDashboard',
        component: () => import('@/views/user/Dashboard.vue'),
        meta: { title: 'User Panel', icon: 'Monitor' } // User panel title and icon
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('@/views/profile/Profile.vue'),
        meta: { title: 'Profile', icon: 'User' } // Personal information title and icon
      }
    ]
  }
]

// Create the router instance
const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: () => import('@/views/common/Login.vue'),
      meta: { title: 'Login' } // Login title
    },
    ...adminRoutes, // Spread admin routes
    ...employeeRoutes, // Spread employee routes
    {
      path: '/:pathMatch(.*)*', // Catch-all route for 404
      name: 'NotFound',
      component: () => import('@/views/common/NotFound.vue'),
      meta: { title: '404' } // 404 title
    }
  ]
})

// Navigation guard
router.beforeEach((to, from, next) => {
  console.log(to, from);
  const meStore = useUserStore();
  const { role, token } = meStore;

  const isLogin = !!token; // Check if user is logged in
  const isAdmin = role === USER_ROLES.ADMIN; // Check if user is an admin
  if (!isLogin && to.path !== '/login') {
    console.log('Redirecting to login');
    return next({ path: '/login' }); // Redirect to login if not logged in
  }

  if (to.meta['requiresAdmin'] && !isAdmin) {
    console.log('Admin access required');
    return next({ path: '/NotFound' }); // Redirect to NotFound if admin access is required
  }

  if (to.meta['requiresEmployee'] && isAdmin) {
    console.log('Employee access required');
    return next({ path: '/NotFound' }); // Redirect to NotFound if employee access is required
  }

  if (to.path === '/') {
    console.log('Redirecting based on role');
    return next({ path: isAdmin ? '/admin/dashboard' : '/employee/dashboard' }); // Redirect based on user role
  }

//   console.log('Proceeding to', to.path);
  next(); // Proceed to the next route
});

// Export the router and routes
export default router 
export { adminRoutes, employeeRoutes };